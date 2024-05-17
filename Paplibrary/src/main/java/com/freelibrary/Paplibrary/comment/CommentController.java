package com.freelibrary.Paplibrary.comment;

import jakarta.validation.Valid;
import com.freelibrary.Paplibrary.comment.CommentDto;
import com.freelibrary.Paplibrary.book.BookDto;
import com.freelibrary.Paplibrary.comment.CommentService;
import com.freelibrary.Paplibrary.book.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class CommentController {

    private CommentService commentService;
    private BookService bookService;

    public CommentController(CommentService commentService,BookService bookService) {
        this.commentService = commentService;
        this.bookService = bookService;
    }

    // handler method to create form submit request
    @PostMapping("/{bookId}/comments")
    public String createComment(@PathVariable("bookId") Long bookId,
                                @Valid @ModelAttribute("comment") CommentDto commentDto,
                                BindingResult result,
                                Model model){

        BookDto bookDto = bookService.findBookById(bookId);
        if(result.hasErrors()){
            model.addAttribute("book", bookDto);
            model.addAttribute("comment", commentDto);
           // return "book/book_comment";
            return "/book/";
        }

        commentService.createComment(bookId, commentDto);
        return "redirect:/book/" + bookId;
    }
    @GetMapping("/{bookId}/comments")
    public List<CommentDto> getCommentsByBookId(@PathVariable Long bookId) {
        return commentService.findCommentsByBookId(bookId);
    }



}
