package com.freelibrary.Paplibrary.comment;

import com.freelibrary.Paplibrary.book.Book;
import com.freelibrary.Paplibrary.user.User;
import com.freelibrary.Paplibrary.user.UserRepository;
import jakarta.validation.Valid;
import com.freelibrary.Paplibrary.comment.CommentDto;
import com.freelibrary.Paplibrary.book.BookDto;
import com.freelibrary.Paplibrary.comment.CommentService;
import com.freelibrary.Paplibrary.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
public class CommentController {

    private CommentService commentService;
    private BookService bookService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    public CommentController(CommentService commentService,BookService bookService) {
        this.commentService = commentService;
        this.bookService = bookService;
    }

    // oglądanie komentarzy do książki
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{bookId}/addcomment")
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



    //modyfikacja komentarza wsunąć do comments bo nie działało i dodać w takim razie /book/ na początku
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/book/{bookId}/comment/edit/{commentId}")
    public String editCommentForm(@PathVariable("bookId") Long bookId, @PathVariable("commentId") Long commentId, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        Comment comment1 = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));


        if (!user.getId().equals(comment1.getCreatedBy().getId())) {
            throw new SecurityException("You are not authorized to edit this comment");
        }

        CommentDto comment = commentService.findCommentById(commentId);
        model.addAttribute("bookId", bookId);
        model.addAttribute("comment", comment);
        return "book/editCommentPage";
    }

    //  modyfikacja komentarza
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/book/{bookId}/comment/edit")
    public String editComment(@PathVariable("bookId") Long bookId,
                              @ModelAttribute("comment") CommentDto commentDto) {

        commentService.modifyComment(commentDto, bookId);
        return "redirect:/book/" + bookId;
    }
    //usunięcie komentarza do książki
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/book/{bookId}/comment/delete/{commentId}")
    public String deleteComment(@PathVariable("bookId") Long bookId,
                                @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/book/" + bookId;
    }


    @GetMapping("/{bookId}/comments")
    public List<CommentDto> getCommentsByBookId(@PathVariable Long bookId) {
        return commentService.findCommentsByBookId(bookId);
    }



}
