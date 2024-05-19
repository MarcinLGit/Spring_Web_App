package com.freelibrary.Paplibrary.rating;


import com.freelibrary.Paplibrary.book.Book;
import com.freelibrary.Paplibrary.book.BookMapper;
import com.freelibrary.Paplibrary.book.BookService;
import com.freelibrary.Paplibrary.user.User;
import com.freelibrary.Paplibrary.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

//    @PostMapping("/{bookId}/rate")
//    public Rating addRating(@PathVariable Long bookId, @RequestParam int ratingValue, Authentication authentication) {
//        String email = authentication.getName();
//        User user = userService.findByEmail(email);
//        Book book = BookMapper.mapToBook(bookService.findBookById(bookId));
//        return ratingService.addRating(user, book, ratingValue);
//    }

    @PostMapping("/{bookId}/rate")
    public Rating addRating(@PathVariable Long bookId, @RequestParam int rating, Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        Book book = BookMapper.mapToBook(bookService.findBookById(bookId));
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        return ratingService.addRating(user, book, rating);
    }

//    @GetMapping("/{bookId}/averageRating")
//    public double getAverageRating(@PathVariable Long bookId) {
//        Book book = BookMapper.mapToBook(bookService.findBookById(bookId));
//        return ratingService.getAverageRatingForBook(book);
//    }


    @GetMapping("/{bookId}/averageRating")
    public double getAverageRating(@PathVariable Long bookId) {
        Book book = BookMapper.mapToBook(bookService.findBookById(bookId));
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        return ratingService.getAverageRatingForBook(book);
    }
}
