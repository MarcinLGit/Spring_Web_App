package com.freelibrary.Paplibrary.user.admin;

import com.freelibrary.Paplibrary.book.BookDto;
import com.freelibrary.Paplibrary.book.BookMapper;
import com.freelibrary.Paplibrary.book.BookService;
import com.freelibrary.Paplibrary.comment.CommentDto;
import com.freelibrary.Paplibrary.comment.CommentService;
import com.freelibrary.Paplibrary.file.storage.StorageService;
import com.freelibrary.Paplibrary.rating.Rating;
import com.freelibrary.Paplibrary.rating.RatingService;
import com.freelibrary.Paplibrary.user.User;
import com.freelibrary.Paplibrary.user.UserDto;
import com.freelibrary.Paplibrary.user.UserRepository;
import com.freelibrary.Paplibrary.user.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.freelibrary.Paplibrary.book.HashCalculator.calculateFileHash;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private RatingService ratingService;

    @Autowired
    private CommentService commentService;
    public final StorageService storageService;

    public AdminController(StorageService storageService) {
        this.storageService = storageService;
    }


    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin")
    public String viewAdminPanel(){
        return "admin/admin_panel";
    }


    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/users")
    public String viewUsers(Model model){
        List<UserDto> usersResponse = userService.getAllUsers();
        model.addAttribute("usersResponse", usersResponse);
        return "/admin/users_page";
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping("/admin/user/delete/{userId}")
    public String deleteUserById(@PathVariable("userId") Long userId) {
        System.out.println("test");
        userService.deleteUserByID(userId);
        return "redirect:/admin/users";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/books")
    public String adminViewBooks(Model model,Authentication authentication){
        List<BookDto> booksResponse = bookService.getAllBooks();
        model.addAttribute("booksResponse", booksResponse);

        return "/admin/books";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/user/{userId}/profile")
    public String adminViewUserBooks(Model model, @PathVariable("userId") Long userId){

        List<BookDto> booksResponse = bookService.getBooksByUser();
        User currentUser = userService.findByUserId(userId);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("booksResponse", booksResponse);
        return "book/home_page";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/books/{bookId}")
    public String adminShowBookById(@PathVariable("bookId") Long bookId, Model model, Authentication authentication) {
        BookDto bookDto = bookService.findBookById(bookId);
        double averageRating = ratingService.getAverageRatingForBook(BookMapper.mapToBook(bookService.findBookById(bookId)));
        model.addAttribute("averageRating", averageRating);

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User currentUserUser = userRepository.findByEmail(email);
            Long currentUser= currentUserUser.getId();
            model.addAttribute("currentUser", currentUser);
            Long bookOwner = bookService.getUserOwner(bookDto);

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

        model.addAttribute("bookDto", bookDto);
        model.addAttribute("comments", comments);
        return "admin/book";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/books/newbook")
    public String adminShowNewForm(Model model) {
        BookDto bookDto = new BookDto();
        model.addAttribute("bookDto", bookDto);
        return "admin/new_form";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/books/newbook/{userId}")
    public String adminShowNewForm(@PathVariable("userId") Long userId, Model model) {
        BookDto bookDto = new BookDto();
        model.addAttribute("bookDto", bookDto);
        model.addAttribute("userId", userId);
        return "admin/new_form";
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/admin/books/savebook/{userId}")
    public String adminCreateBook(@PathVariable("userId") Long userId, @Valid @ModelAttribute("book") BookDto bookDto,
                                  BindingResult result, Model model, @RequestParam("file") MultipartFile file){
        String hash = calculateFileHash(file);
        bookDto.setHash(hash);
        bookDto.setStarRating("0");

        if(result.hasErrors()){
            model.addAttribute("book", bookDto);
            System.out.println("Error occurred");
            return "book/new_form";
        }
        try {
            bookService.saveBook(bookDto, userId);
            storageService.store(file,hash);
            System.out.println("Book saved successfully");
            return "redirect:/book/";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/admin/books/savebook")
    public String adminCreateBook(@Valid @ModelAttribute("book") BookDto bookDto,
                                  BindingResult result,
                                  Model model, @RequestParam("file") MultipartFile file){
        String hash = calculateFileHash(file);
        bookDto.setHash(hash);
        bookDto.setStarRating("0");
        if(result.hasErrors()){
            model.addAttribute("book", bookDto);
            System.out.println("Error occurred");
            return "book/new_form";
        }
        try {
            bookService.saveBook(bookDto);
            storageService.store(file,hash);
            System.out.println("Book saved successfully");
            return "redirect:/book/";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/delete/{bookId}")
    public String adminDelete(@PathVariable("bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return "redirect:/admin/book/";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/book/{bookId}/edit")
    public String adminEditForm(@PathVariable("bookId") Long bookId, Model model) {
        BookDto bookDto = bookService.findBookById(bookId);
        model.addAttribute("bookDto", bookDto);
        return "/admin/edit_form";
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/admin/book/{bookId}/update")
    public String adminUpdateBook(@PathVariable("bookId") Long bookId,
                                  @Valid @ModelAttribute("book") BookDto book,
                                  BindingResult result,
                                  Model model){
        if(result.hasErrors()){
            model.addAttribute("book", book);
            return "/admin/edit_form";
        }
        book.setBookId(bookId);
        bookService.updateBook(book);
        return "redirect:/book/"+bookId;
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/book/search")
    public String adminSearchBooks(@RequestParam(required = false) String title,
                                   @RequestParam(required = false) String author,
                                   @RequestParam(required = false) String publicationYear,
                                   @RequestParam(required = false) String genre,
                                   @RequestParam(required = false) String starRating,
                                   @RequestParam(required = false) String language,
                                   Model model) {
        List<BookDto> books = bookService.searchBooks(title, author, publicationYear, genre, starRating, language);
        model.addAttribute("books", books);
        return "/admin/search_results";

    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin/book/addcomment")
    public String adminAddcomment() {
        return "/admin/book_comment";
    }

  // @RolesAllowed("ROLE_ADMIN")
  // @GetMapping("/admin/book/search")
  // public String adminSearchBooksByAddedBy(@RequestParam(required = false) String addedBy,
  //                                Model model) {
  //     List<BookDto> books = bookService.searchBooks();
  //     model.addAttribute("books", books);
  //     return "/admin/search_results";

  // }

}