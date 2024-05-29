package com.freelibrary.Paplibrary.book;

import com.freelibrary.Paplibrary.comment.Comment;
import com.freelibrary.Paplibrary.comment.CommentRepository;
import com.freelibrary.Paplibrary.comment.CommentService;
import com.freelibrary.Paplibrary.rating.Rating;
import com.freelibrary.Paplibrary.rating.RatingRepository;
import com.freelibrary.Paplibrary.user.User;
import com.freelibrary.Paplibrary.user.UserRepository;
import com.freelibrary.Paplibrary.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(BookMapper::mapToBookDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveBook(BookDto bookDto) {
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userRepository.findByEmail(email);
        Book book = BookMapper.mapToBook(bookDto);
        book.setAddedBy(user);
        bookRepository.save(book);
    }

    @Override
    public void saveBook(BookDto bookDto, Long userId) {
        User user = userRepository.findById(userId).get();
        Book book = BookMapper.mapToBook(bookDto);
        book.setAddedBy(user);
        bookRepository.save(book);
    }



    @Override
    public void updateBook(BookDto bookDto) {

        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userRepository.findByEmail(email);
        Long nr_book = bookDto.getBookId();
        Book book = bookRepository.findById(nr_book).get();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(user.getId() != book.getAddedBy().getId() && auth != null
                && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            throw new SecurityException("You are not authorized to modify this book");
        }
        Book book1 = BookMapper.mapToBook(bookDto);
        book1.setAddedBy(book.getAddedBy());
        book1.setHash(book.getHash());
        book1.setEmail(book1.getEmail());
        bookRepository.save(book1);
    }



    @Override
    public void deleteBook(Long nr_book) {
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userRepository.findByEmail(email);
        Book book = bookRepository.findById(nr_book).get();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(user.getId() != book.getAddedBy().getId() && auth != null
                && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            throw new SecurityException("You are not authorized to delete this book");
        }
        List<Comment> comments= commentRepository.findCommentsByBookId(nr_book);
        List<Rating> ratings = ratingRepository.findByBook(bookRepository.findById(nr_book).get());
        for(Comment comment : comments) {
            commentRepository.delete(comment);
        }
        for(Rating rating : ratings) {
            ratingRepository.delete(rating);
        }
        bookRepository.deleteById(nr_book);
    }

    
    @Override
    public BookDto findBookById(Long bookId) {
        Book book = bookRepository.findById(bookId).get();
        System.out.println(book);
        return BookMapper.mapToBookDto(book);
    }

    @Override
    public Long getUserOwner(BookDto bookDto) {
        Long userOwner= bookRepository.findById(bookDto.getBookId()).get().getAddedBy().getId();
        return userOwner;
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
                        && (starRating == null || starRating.isEmpty() || Double.parseDouble(book.getStarRating()) >= Double.parseDouble(starRating))
                        && (language == null || book.getLanguage().contains(language)))
                .collect(Collectors.toList());

        return filteredBooks;
    }

//    @Override
//    public List<BookDto> searchBooks(String addedBy) {
//        // Pobierz wszystkie książki
//        List<BookDto> allBooks = getAllBooks();
//
//        // Wykonaj filtrowanie na podstawie wszystkich parametrów
//        List<BookDto> filteredBooks = allBooks.stream()
//                .filter(book -> (book.getAuthor().contains()))
//                .collect(Collectors.toList());
//
//        return filteredBooks;
//    }



    @Override
    public BookDto findBookByHash(String bookHash) {
        Book book = bookRepository.findByHash(bookHash).get();
        return BookMapper.mapToBookDto(book);
    }

    @Override
    public List<BookDto> getBooksByUser() {
        String email = SecurityUtils.getCurrentUser().getUsername();
        User createdBy = userRepository.findByEmail(email);
        Long userId = createdBy.getId();
        List<Book> books = bookRepository.findBooksByUser(userId);
        return books.stream()
                .map(BookMapper::mapToBookDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> getBooksByUserId(Long id) {
        List<Book> books = bookRepository.findBooksByUser(id);
        return books.stream()
                .map(BookMapper::mapToBookDto)
                .collect(Collectors.toList());
    }
}

