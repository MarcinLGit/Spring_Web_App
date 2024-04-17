package com.freelibrary.Paplibrary.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookServiceImpl bookServiceImpl;

    @GetMapping
    public String viewHomePage(Model model){
        List<BookDto> listBook =  bookServiceImpl.getAllBooks();
        model.addAttribute("books",listBook);
        return  "book/book";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        BookDto bookDto = new BookDto();
        model.addAttribute("bookDto", bookDto);
        return "book/new_form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("bookDto") BookDto bookDto) {
        bookServiceImpl.addBook(bookDto);
        return "redirect:/book";
    }

    @GetMapping("/edit/{bookId}")
    public String showEditForm(@PathVariable("bookId") Long bookId, Model model) {
        BookDto bookDto = bookServiceImpl.findBookById(bookId);
        model.addAttribute("bookDto", bookDto);
        return "book/edit_form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("bookDto") Book bookDto) {
        bookServiceImpl.updateBook(bookDto);
        return "redirect:/book";
    }

    @GetMapping("/delete/{bookId}")
    public String delete(@PathVariable("bookId") Long bookId) {
        bookServiceImpl.deleteBook(bookId);
        return "redirect:/book";
    }
}
