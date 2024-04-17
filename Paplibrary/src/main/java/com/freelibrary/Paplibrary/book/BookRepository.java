package com.freelibrary.Paplibrary.book;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.freelibrary.Paplibrary.book.Book;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


        @Query("SELECT b FROM Book b WHERE b.title = :title")
        Optional<Book> findByTitle(@Param("title") String title);

        @Query("SELECT b FROM Book b WHERE b.author = :author")
        List<Book> findByAuthor(@Param("author") String author);

//TODO make a lot SQL requests for filers

}

