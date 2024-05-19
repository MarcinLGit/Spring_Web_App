package com.freelibrary.Paplibrary.rating;



import com.freelibrary.Paplibrary.book.Book;
import com.freelibrary.Paplibrary.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserAndBook(User user, Book book);
    List<Rating> findByBook(Book book);
}
