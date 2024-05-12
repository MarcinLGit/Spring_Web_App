package com.freelibrary.Paplibrary.comment;

import jakarta.validation.Valid;
import com.freelibrary.Paplibrary.comment.CommentDto;
import com.freelibrary.Paplibrary.book.BookDto;
import com.freelibrary.Paplibrary.comment.CommentService;
import com.freelibrary.Paplibrary.book.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class CommentController {

    private CommentService commentService;
    private BookService bookService;

    public CommentController(CommentService commentService,BookService bookService) {
        this.commentService = commentService;
        this.bookService = bookService;
    }

    // handler method to create form submit request
    @PostMapping("/{bookHash}/comments")
    public String createComment(@PathVariable("bookHash") String bookHash,
                                @Valid @ModelAttribute("comment") CommentDto commentDto,
                                BindingResult result,
                                Model model){

        BookDto bookDto = bookService.findBookByHash(bookHash);
        if(result.hasErrors()){
            model.addAttribute("book", bookDto);
            model.addAttribute("comment", commentDto);
            return "Book/book_comment";
        }

        commentService.createComment(bookHash, commentDto);
        return "redirect:/book/" + bookHash;
    }
}
