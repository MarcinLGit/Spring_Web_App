
package com.freelibrary.Paplibrary.rating;

import com.freelibrary.Paplibrary.book.Book;
import com.freelibrary.Paplibrary.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface RatingService {
    Rating addRating(User user, Book book, int ratingValue);
    List<Rating> getRatingsForBook(Book book);
    double getAverageRatingForBook(Book book);
}

