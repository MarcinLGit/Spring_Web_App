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
    public void createComment(String bookHash, CommentDto commentDto) {

        Book book = bookRepository.findByHash(bookHash).get(); // co to znaczy?
        Comment comment = CommentMapper.mapToComment(commentDto);
        comment.setBook(book);
        commentRepository.save(comment);
    }

    @Override
    public List<CommentDto> findAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

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
