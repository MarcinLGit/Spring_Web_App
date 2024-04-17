package com.freelibrary.Paplibrary.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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



    public void updateBook(Book book) {
        Book bookToUpdate;
        bookToUpdate = book;
        bookRepository.save(bookToUpdate);
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
        Book book = bookRepository.findById(bookId).orElse(null);
        return BookMapper.mapToBookDto(book);
    }

    @Override
    public void addBook(BookDto bookDto) {
        Book book = BookMapper.mapToBook(bookDto);
        bookRepository.save(book);
    }



}


