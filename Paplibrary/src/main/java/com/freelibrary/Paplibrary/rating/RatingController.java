package com.freelibrary.Paplibrary.rating;


import com.freelibrary.Paplibrary.book.*;
import com.freelibrary.Paplibrary.user.User;
import com.freelibrary.Paplibrary.user.UserRepository;
import com.freelibrary.Paplibrary.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{bookId}/rate")
    public String addRating(@PathVariable Long bookId, @RequestParam int rating, Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        BookDto bookDto = bookService.findBookById(bookId);
        Book book = BookMapper.mapToBook(bookDto);

        String email1 =bookDto.getEmail();
        User user1 = userRepository.findByEmail(email1);


        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }

        book.setAddedBy(user1);

        ratingService.addRating(user, book, rating);
        book.setStarRating(String.valueOf(ratingService.getAverageRatingForBook(book)));
        bookRepository.save(book);

        return "redirect:/book/" + bookId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{bookId}/averageRating")
    public double getAverageRating(@PathVariable Long bookId) {
        Book book = BookMapper.mapToBook(bookService.findBookById(bookId));
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        return ratingService.getAverageRatingForBook(book);
    }
}
