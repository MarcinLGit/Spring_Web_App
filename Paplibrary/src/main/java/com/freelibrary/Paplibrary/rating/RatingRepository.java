package com.freelibrary.Paplibrary.rating;



import com.freelibrary.Paplibrary.book.Book;
import com.freelibrary.Paplibrary.comment.Comment;
import com.freelibrary.Paplibrary.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserAndBook(User user, Book book);
    List<Rating> findByBook(Book book);

    @Query(value = "select r.* from rating r\n" +
            "inner join book b on r.book_id = b.book_id\n" +
            "where b.book_id = :bookId\n", nativeQuery = true)
    List<Rating> findRatingsByBookId(Long bookId);
}
