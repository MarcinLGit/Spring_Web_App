package com.freelibrary.Paplibrary.book;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.freelibrary.Paplibrary.book.Book;


import java.util.List;



@Service
public interface BookService {

    List<BookDto> getAllBooks();

    void saveBook(BookDto bookdto);

    void updateBook(BookDto book);



    Book getBook(Long nr_book);

    void deleteBook(Long nr_book);

    BookDto findBookByTitle(String title);
    List<BookDto> findBookByAuthor(String author);
    List<BookDto> searchBooks(String query);
    BookDto findBookById(Long BookId);

    BookDto findBookByHash(String bookHash);
    List<BookDto> searchBooks(String title, String author, String publicationYear,
                              String genre, String starRating, String language);
}
