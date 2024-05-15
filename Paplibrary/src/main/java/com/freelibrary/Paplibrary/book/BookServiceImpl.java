package com.freelibrary.Paplibrary.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(BookMapper::mapToBookDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveBook(BookDto bookDto) {
        Book book = BookMapper.mapToBook(bookDto);
        bookRepository.save(book);
    }



//    public void updateBook(BookDto bookDto) {
//        // Pobierz aktualnie zalogowanego użytkownika
//        String email = SecurityUtils.getCurrentUser().getUsername();
//        User createdBy = userRepository.findByEmail(email);
//
//        // Mapuj BookDto na obiekt Book
//        Book book = BookMapper.mapToBook(bookDto);
//
//        // Ustaw informacje o tym, kto utworzył książkę
//        book.setCreatedBy(createdBy);
//
//        // Zapisz zaktualizowaną książkę do bazy danych
//        bookRepository.save(book);
//    }
    @Override
    public void updateBook(BookDto bookDto) {
        Book book = BookMapper.mapToBook(bookDto);
        bookRepository.save(book);
    }


    @Override
    public Book getBook(Long nr_book) {
        return bookRepository.findById(nr_book).orElse(null);
    }





    @Override
    public void deleteBook(Long nr_book) {
        bookRepository.deleteById(nr_book);
    }

    @Override
    public BookDto findBookByTitle(String title) {
        Optional<Book> bookOptional = bookRepository.findByTitle(title);
        return bookOptional.map(BookMapper::mapToBookDto).orElse(null);
    }

    @Override
    public List<BookDto> findBookByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        return books.stream().map(BookMapper::mapToBookDto).collect(Collectors.toList());
    }


    @Override
    public List<BookDto> searchBooks(String query) {
        return null;
    }



    @Override
    public BookDto findBookById(Long bookId) {
        Book book = bookRepository.findById(bookId).get();
        System.out.println(book);
        return BookMapper.mapToBookDto(book);
    }


    @Override
    public List<BookDto> searchBooks(String title, String author, String publicationYear,
                                     String genre, String starRating, String language) {
        // Pobierz wszystkie książki
        List<BookDto> allBooks = getAllBooks();

        // Wykonaj filtrowanie na podstawie wszystkich parametrów
        List<BookDto> filteredBooks = allBooks.stream()
                .filter(book -> (title == null || book.getTitle().contains(title))
                        && (author == null || book.getAuthor().contains(author))
                        && (publicationYear == null || book.getPublicationYear().contains(publicationYear))
                        && (genre == null || book.getGenre().contains(genre))
                        && (starRating == null || starRating.isEmpty() || Integer.parseInt(book.getStarRating()) >= Integer.parseInt(starRating))
                        && (language == null || book.getLanguage().contains(language)))
                .collect(Collectors.toList());

        return filteredBooks;
    }

    @Override
    public BookDto findBookByHash(String bookHash) {
        Book book = bookRepository.findByHash(bookHash).get();
        return BookMapper.mapToBookDto(book);
    }


}


