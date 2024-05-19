package com.freelibrary.Paplibrary.comment;

import com.freelibrary.Paplibrary.book.*;
import com.freelibrary.Paplibrary.comment.CommentDto;
import com.freelibrary.Paplibrary.comment.Comment;
import com.freelibrary.Paplibrary.user.User;
import com.freelibrary.Paplibrary.comment.CommentMapper;
import com.freelibrary.Paplibrary.comment.CommentRepository;
import com.freelibrary.Paplibrary.user.UserRepository;
import com.freelibrary.Paplibrary.comment.CommentService;
import com.freelibrary.Paplibrary.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;
    @Autowired
    private BookService bookService;


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
        comment.setName(user.getLogin());
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

        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userRepository.findByEmail(email);

        Comment comment = commentRepository.findById(commentId).get();

        if(user.getId() != comment.getCreatedBy().getId()) {
            throw new SecurityException("You are not authorized to delete this book");
        }

        commentRepository.deleteById(commentId);
    }



    // to działa ale trzeba żeby kod był czysty poprawić to
    @Override
    public void modifyComment(CommentDto commentDto,Long bookId) {


        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userRepository.findByEmail(email);


        Long comment_id = commentDto.getId();
        Comment comment1 = commentRepository.findById(comment_id).get();

        if(user.getId() != comment1.getCreatedBy().getId()) {
            throw new SecurityException("You are not authorized to delete this book");
        }
            CommentDto  commentDto1 = findCommentById(commentDto.getId());
            commentDto1.setContent(commentDto.getContent());
            commentDto1.setUpdatedOn(LocalDateTime.now());
            Comment comment = CommentMapper.mapToComment(commentDto1);
            comment.setCreatedBy(user);
            BookDto bookDto = bookService.findBookById(bookId);
            Book book = BookMapper.mapToBook(bookDto);
            comment.setBook(book);
            commentRepository.save(comment);

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

    //sprawdzić czy to nie jest to samo bo wygląda podobnie
    @Override
    public List<CommentDto> findCommentsByBookId(Long bookId) {

        List<Comment> comments = commentRepository.findCommentsByBookId(bookId);
        return comments.stream()
                .map((comment) -> CommentMapper.mapToCommentDto(comment))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto findCommentById(Long commentId) {

            Comment comment = commentRepository.findById(commentId).get();
            return CommentMapper.mapToCommentDto(comment);
    }



}
