package com.freelibrary.Paplibrary.book;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.freelibrary.Paplibrary.book.Book;


import java.util.List;



@Service
public interface BookService {
    List<BookDto> getAllBooks();
    void saveBook(BookDto bookdto);
    void saveBook(BookDto bookDto, Long userId);
    void updateBook(BookDto book);
    List<BookDto> getBooksByUser();
    List<BookDto> getBooksByUserId(Long id);
    void deleteBook(Long nr_book);
    BookDto findBookById(Long BookId);
    Long getUserOwner(BookDto bookDto);
    BookDto findBookByHash(String bookHash);
    List<BookDto> searchBooks(String title, String author, String publicationYear,
                              String genre, String starRating, String language);
}
