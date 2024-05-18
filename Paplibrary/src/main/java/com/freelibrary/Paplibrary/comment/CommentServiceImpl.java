package com.freelibrary.Paplibrary.comment;

import com.freelibrary.Paplibrary.comment.CommentDto;
import com.freelibrary.Paplibrary.comment.Comment;
import com.freelibrary.Paplibrary.book.Book;
import com.freelibrary.Paplibrary.user.User;
import com.freelibrary.Paplibrary.comment.CommentMapper;
import com.freelibrary.Paplibrary.comment.CommentRepository;
import com.freelibrary.Paplibrary.book.BookRepository;
import com.freelibrary.Paplibrary.user.UserRepository;
import com.freelibrary.Paplibrary.comment.CommentService;
import com.freelibrary.Paplibrary.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              BookRepository bookRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createComment(Long bookId, CommentDto commentDto) {

        Book book = bookRepository.findById(bookId).get(); // co to znaczy?
        Comment comment = CommentMapper.mapToComment(commentDto);
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userRepository.findByEmail(email);
        LocalDateTime currentDateTime = LocalDateTime.now();
        comment.setBook(book);
        comment.setCreatedBy(user);
        comment.setEmail(email);
        comment.setCreatedOn(currentDateTime);
        comment.setName("Temp name suka");
        commentRepository.save(comment);
    }

    @Override
    public List<CommentDto> findAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

    //tutaj raczej listAll to this book?

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public void modifyComment(Long commentId) {


    }



    @Override
    public List<CommentDto> findCommentsByBook() {
        String email = SecurityUtils.getCurrentUser().getUsername();
        User createdBy = userRepository.findByEmail(email);
        Long userId = createdBy.getId();
        List<Comment> comments = commentRepository.findCommentsByBook(userId);
        return comments.stream()
                .map((comment) -> CommentMapper.mapToCommentDto(comment))
                .collect(Collectors.toList());
    }


    @Override
    public List<CommentDto> findCommentsByBookId(Long bookId) {

        List<Comment> comments = commentRepository.findCommentsByBookId(bookId);
        return comments.stream()
                .map((comment) -> CommentMapper.mapToCommentDto(comment))
                .collect(Collectors.toList());
    }


//    public void modifyComment(Long commentId, ModifyCommentRequest request) {
//        // Pobierz komentarz na podstawie jego identyfikatora
//        Optional<Comment> optionalComment = commentRepository.findById(commentId);
//        if (optionalComment.isPresent()) {
//            Comment comment = optionalComment.get();
//            // Dokonaj modyfikacji komentarza na podstawie danych z żądania
//            comment.setContent(request.getContent());
//            // Zapisz zmodyfikowany komentarz w bazie danych
//            commentRepository.save(comment);
//        } else {
//            // Obsłuż przypadek, gdy komentarz o podanym identyfikatorze nie został znaleziony
//            throw new CommentNotFoundException("Comment with id " + commentId + " not found");
//        }
//    }
}
