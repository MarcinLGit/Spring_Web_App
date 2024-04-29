package com.freelibrary.Paplibrary.book;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.freelibrary.Paplibrary.book.BookController;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookServiceImpl bookServiceImpl;
    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String viewBooks(Model model){
        List<BookDto> booksResponse = bookService.getAllBooks();
        model.addAttribute("booksResponse", booksResponse);
        return "book/home_page";
    }

//
//    //change
//    @GetMapping
//    public String viewHomePage(Model model){
//        List<BookDto> listBook =  bookServiceImpl.getAllBooks();
//        model.addAttribute("books",listBook);
//        return  "book/book";
//    }

    @GetMapping("/newbook")
    public String showNewForm(Model model) {
        BookDto bookDto = new BookDto();
        model.addAttribute("bookDto", bookDto);
        return "book/new_form";
    }



    @PostMapping("/savebook")
    public String createPost(@Valid @ModelAttribute("book") BookDto bookDto,
                             BindingResult result,
                             Model model){
        if(result.hasErrors()){
            model.addAttribute("book", bookDto);
            return "book/new_form";
        }
        bookService.addBook(bookDto);
        return "redirect:/book/";
    }

    @GetMapping("/edit/{bookId}")
    public String showEditForm(@PathVariable("bookId") Long bookId, Model model) {
        BookDto bookDto = bookService.findBookById(bookId);
        model.addAttribute("bookDto", bookDto);
        return "book/edit_form";
    }

    @GetMapping("/deletebook")
    public String showEditForm() {
        return "book/delete";
    }


    @GetMapping("/delete/{bookId}")
    public String delete(@PathVariable("bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return "redirect:/book/";
    }




    // handler method to handle edit post form submit request
    @PostMapping("update/{bookId}")
    public String updatePost(@PathVariable("bookId") Long bookId,
                             @Valid @ModelAttribute("book") BookDto book,
                             BindingResult result,
                             Model model){
        if(result.hasErrors()){
            model.addAttribute("book", book);
            return "book/edit_form";
        }
        book.setBookId(bookId);
        bookService.updateBook(book);
        return "redirect:/book/";
    }


}
