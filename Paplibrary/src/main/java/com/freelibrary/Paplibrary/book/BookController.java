package com.freelibrary.Paplibrary.book;

import com.freelibrary.Paplibrary.comment.Comment;
import com.freelibrary.Paplibrary.comment.CommentDto;
import com.freelibrary.Paplibrary.comment.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.freelibrary.Paplibrary.book.BookController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private CommentService commentService;
    public final StorageService storageService;

    public BookController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String viewBooks(Model model){
        List<BookDto> booksResponse = bookService.getAllBooks();
        model.addAttribute("booksResponse", booksResponse);
        return "book/home_page";
    }




        @GetMapping("/{bookId}")
    public String showBookById(@PathVariable("bookId") Long bookId, Model model) {
        BookDto bookDto = bookService.findBookById(bookId);
        List<CommentDto> comments = commentService.findCommentsByBookId(bookId);
        model.addAttribute("bookDto", bookDto);
        model.addAttribute("comments", comments);
        return "book/book";
    }



//    @GetMapping("/{bookId}")
//    public String showBookById(@PathVariable("bookId") Long bookId, Model model) {
//        BookDto bookDto = bookService.findBookById(bookId);
//        model.addAttribute("bookDto", bookDto);
//        return "book/book";
//    }




//    @GetMapping("/{tittle}}")
//    public String showBookByTittle(@PathVariable("tittle") String tittle, Model model) {
//        BookDto bookDto = bookService.findBookByTitle(tittle);
//        model.addAttribute("bookDto", bookDto);
//        return "book/book";
//    }


    @GetMapping("/newbook")
    public String showNewForm(Model model) {
        BookDto bookDto = new BookDto();
        model.addAttribute("bookDto", bookDto);
        return "book/new_form";
    }


    @PostMapping("/savebook")
    public String createBook(@Valid @ModelAttribute("book") BookDto bookDto,
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



    @GetMapping("/delete/{bookId}")
    public String delete(@PathVariable("bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return "redirect:/book/";
    }



    @GetMapping("/edit/{bookId}")
    public String showEditForm(@PathVariable("bookId") Long bookId, Model model) {
        BookDto bookDto = bookService.findBookById(bookId);
        model.addAttribute("bookDto", bookDto);
        return "book/edit_form";
    }

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
                              Model model) {
        List<BookDto> books = bookService.searchBooks(title, author, publicationYear, genre, starRating, language);
        model.addAttribute("books", books);
        return "/book/search_results";

    }

    @GetMapping("/searcher")
    public String searcher() {
        return "/book/searcher";
    }


    @GetMapping("/addcomment")
    public String addcomment() {
        return "/book/book_comment";
    }

    @GetMapping("/getcomment")
    public String agetcomment() {
        return "/book/getcomments";
    }

}
