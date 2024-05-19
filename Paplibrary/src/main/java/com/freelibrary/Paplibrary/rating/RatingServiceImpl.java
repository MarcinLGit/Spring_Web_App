package com.freelibrary.Paplibrary.rating;



import com.freelibrary.Paplibrary.book.Book;
import com.freelibrary.Paplibrary.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

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
    public double getAverageRatingForBook(Book book) {
        List<Rating> ratings = ratingRepository.findByBook(book);
        return ratings.stream().mapToInt(Rating::getRating).average().orElse(0.0);
    }
}
