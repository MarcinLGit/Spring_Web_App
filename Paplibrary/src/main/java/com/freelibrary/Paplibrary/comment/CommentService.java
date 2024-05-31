package com.freelibrary.Paplibrary.comment;

import com.freelibrary.Paplibrary.book.BookDto;

import java.util.List;

public interface CommentService {
    void createComment(Long bookId, CommentDto commentDto);

    List<CommentDto> findAllComments();
    void deleteComment(Long commentId);
    void modifyComment(CommentDto commentDto,Long bookId);
    List<CommentDto> findCommentsByBook();
    List<CommentDto> findCommentsByBookId(Long bookId);
    List<CommentDto> findCommentsByUserId(Long userId);
    CommentDto findCommentById(Long commentId);
}
