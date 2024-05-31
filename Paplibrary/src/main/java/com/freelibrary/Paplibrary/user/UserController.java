package com.freelibrary.Paplibrary.user;

import com.freelibrary.Paplibrary.book.BookDto;
import com.freelibrary.Paplibrary.book.BookService;
import com.freelibrary.Paplibrary.book.BookServiceImpl;
import com.freelibrary.Paplibrary.comment.CommentService;
import com.freelibrary.Paplibrary.file.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Controller
@RequestMapping("/user")
public class UserController {



    private BookService bookService;

    public UserController(BookService bookService) {
        this.bookService = bookService;

    }



    @GetMapping("/")
    public String viewBooks(Model model){
        List<BookDto> booksResponse = bookService.getBooksByUser();
        model.addAttribute("booksResponse", booksResponse);
        return "user/home_page";
    }
}
