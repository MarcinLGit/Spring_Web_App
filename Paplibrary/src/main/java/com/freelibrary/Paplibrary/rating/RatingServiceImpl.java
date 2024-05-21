package com.freelibrary.Paplibrary.rating;



import com.freelibrary.Paplibrary.book.Book;
import com.freelibrary.Paplibrary.book.BookRepository;
import com.freelibrary.Paplibrary.book.BookService;
import com.freelibrary.Paplibrary.comment.Comment;
import com.freelibrary.Paplibrary.comment.CommentRepository;
import com.freelibrary.Paplibrary.user.User;
import com.freelibrary.Paplibrary.user.UserRepository;
import com.freelibrary.Paplibrary.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;
    private CommentRepository commentRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;
    @Autowired
    private BookService bookService;

    @Override
    public Rating addRating(User user, Book book, int ratingValue) {
        Optional<Rating> existingRating = ratingRepository.findByUserAndBook(user, book);
        if (existingRating.isPresent()) {
            throw new IllegalArgumentException("User has already rated this book.");
        }
        Rating rating = new Rating();
        rating.setUser(user);
        rating.setBook(book);
        rating.setRating(ratingValue);
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getRatingsForBook(Book book) {
        return ratingRepository.findByBook(book);
    }

    @Override
    public List<Rating> findRatingsByBookId(Long bookId) {
        List<Rating> ratings = ratingRepository.findRatingsByBookId(bookId);
        return ratings;
    }

    @Override
    public double getAverageRatingForBook(Book book) {
        List<Rating> ratings = ratingRepository.findByBook(book);
        return ratings.stream().mapToInt(Rating::getRating).average().orElse(0.0);
    }



    @Override
    public void deleteRating(Long ratingId) {

        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userRepository.findByEmail(email);

        Comment comment = commentRepository.findById(ratingId).get();

        if(user.getId() != comment.getCreatedBy().getId()) {
            throw new SecurityException("You are not authorized to delete this book");
        }

        ratingRepository.deleteById(ratingId);
    }





}
