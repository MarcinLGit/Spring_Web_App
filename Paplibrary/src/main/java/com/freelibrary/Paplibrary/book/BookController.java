package com.freelibrary.Paplibrary.book;

import com.freelibrary.Paplibrary.comment.Comment;
import com.freelibrary.Paplibrary.comment.CommentDto;
import com.freelibrary.Paplibrary.comment.CommentRepository;
import com.freelibrary.Paplibrary.comment.CommentService;
import com.freelibrary.Paplibrary.rating.Rating;
import com.freelibrary.Paplibrary.rating.RatingRepository;
import com.freelibrary.Paplibrary.rating.RatingService;
import com.freelibrary.Paplibrary.user.User;
import com.freelibrary.Paplibrary.user.UserRepository;
import com.freelibrary.Paplibrary.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.freelibrary.Paplibrary.book.BookController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import  com.freelibrary.Paplibrary.file.storage.StorageService;




import static com.freelibrary.Paplibrary.book.HashCalculator.calculateFileHash;


@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookServiceImpl bookServiceImpl;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private RatingService ratingService;


    @Autowired
    private CommentService commentService;
    public final StorageService storageService;

    public BookController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String viewBooks(Model model,Authentication authentication){
        List<BookDto> booksResponse = bookService.getAllBooks();
        if (authentication != null) {
            String email = authentication.getName();
            User currentUserUser = userRepository.findByEmail(email);
            Long currentUser = currentUserUser.getId();
            model.addAttribute("currentUser", currentUser);
        } else {
            model.addAttribute("currentUser", null); // or handle the unauthenticated case differently
        }

        model.addAttribute("booksResponse", booksResponse);
        return "book/home_page";
    }

    //info o książce
    @GetMapping("/{bookId}")
    public String showBookById(@PathVariable("bookId") Long bookId, Model model,Authentication authentication) {
        BookDto bookDto = bookService.findBookById(bookId);
        double averageRating = ratingService.getAverageRatingForBook(BookMapper.mapToBook(bookService.findBookById(bookId)));
        List<Rating>  ratinglist = ratingService.findRatingsByBookId(bookId);
        int amountOfRatings = ratinglist.size();

        model.addAttribute("ratingAmount", amountOfRatings);
        model.addAttribute("averageRating", averageRating);



        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User currentUserUser = userRepository.findByEmail(email);
            Long currentUser= currentUserUser.getId();
            model.addAttribute("currentUser", currentUser);
            Long bookOwner = bookService.getUserOwner(bookDto);

            // Jeśli użytkownik jest właścicielem książki, przekaż go do szablonu
            if ( bookService.getUserOwner(bookDto) !=0 && currentUserUser.getId().equals(bookOwner) ){
                model.addAttribute("bookOwner", true);
            }

            List<Rating>  ratings = ratingService.findRatingsByBookId(bookId);
            model.addAttribute("ratingAdded",false);
            for(Rating rating:ratings){
                if(rating.getUser().getId() == currentUser){
                    model.addAttribute("ratingAdded",true );
                }
            }

        } else {
            model.addAttribute("currentUser", null);
        }

        List<CommentDto> comments = commentService.findCommentsByBookId(bookId);
        for(CommentDto commentDto:comments)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = commentDto.getCreatedOn().format(formatter);
            String formattedDate1 = commentDto.getUpdatedOn().format(formatter);
            commentDto.setCreatedOnFormat(formattedDate);
            commentDto.setUpdatedOnFormat(formattedDate1);

        }

        model.addAttribute("bookDto", bookDto);
        model.addAttribute("comments", comments);
        return "book/book";
    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/newbook")
    public String showNewForm(Model model) {
        BookDto bookDto = new BookDto();
        model.addAttribute("bookDto", bookDto);
        return "book/new_form";
    }



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/savebook")
    public String createBook(@Valid @ModelAttribute("book") BookDto bookDto,
                             BindingResult result,
                             Model model, @RequestParam("file") MultipartFile file){
        String hash = calculateFileHash(file);
        bookDto.setHash(hash);
        bookDto.setStarRating("0");
        String email = SecurityUtils.getCurrentUser().getUsername();
        bookDto.setEmail(email);
        if(result.hasErrors()){
            model.addAttribute("book", bookDto);
            System.out.println("Error occurred");
            return "book/new_form";
        }
        try {
            storageService.store(file,hash);
            bookService.saveBook(bookDto);
            System.out.println("Book saved successfully");
            return "redirect:/book/";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{bookId}")
    public String delete(@PathVariable("bookId") Long bookId) {

        bookService.deleteBook(bookId);
        return "redirect:/book/";
    }




    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit/{bookId}")
    public String showEditForm(@PathVariable("bookId") Long bookId, Model model) {


        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);


        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));


        if (!user.getId().equals(book.getAddedBy().getId())) {
            throw new SecurityException("You are not authorized to edit this book");
        }
        BookDto bookDto = bookService.findBookById(bookId);
        model.addAttribute("bookDto", bookDto);
        return "book/edit_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("update/{bookId}")
    public String updateBook(@PathVariable("bookId") Long bookId,
                             @Valid @ModelAttribute("book") BookDto book,
                             BindingResult result,
                             Model model){
        if(result.hasErrors()){
            model.addAttribute("book", book);
            return "book/edit_form";
        }
        book.setBookId(bookId);
        bookService.updateBook(book);
        return "redirect:/book/"+bookId;
    }


    @GetMapping("/search")
    public String searchBooks(@RequestParam(required = false) String title,
                              @RequestParam(required = false) String author,
                              @RequestParam(required = false) String publicationYear,
                              @RequestParam(required = false) String genre,
                              @RequestParam(required = false) String starRating,
                              @RequestParam(required = false) String language,
                              Model model,
                              Authentication authentication) {
        List<BookDto> books = bookService.searchBooks(title, author, publicationYear, genre, starRating, language);
        if (authentication != null) {
            String email = authentication.getName();
            User currentUserUser = userRepository.findByEmail(email);
            Long currentUser = currentUserUser.getId();
            model.addAttribute("currentUser", currentUser);
        } else {
            model.addAttribute("currentUser", null); // or handle the unauthenticated case differently
        }
        model.addAttribute("books", books);

        return "/book/search_results";

    }





}
