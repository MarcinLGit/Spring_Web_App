package com.freelibrary.Paplibrary.comment;

import com.freelibrary.Paplibrary.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select c.* from comments c inner join book p\n" +
            "where c.book_id = b.id and p.created_by =:userId", nativeQuery = true)
    List<Comment> findCommentsByBook(Long userId);


    @Query(value = "select c.* from comments c\n" +
            "inner join book b on c.book_id = b.book_id\n" +
            "where b.book_id = :bookId\n", nativeQuery = true)
    List<Comment> findCommentsByBookId(Long bookId);

}
