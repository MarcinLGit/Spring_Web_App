package com.freelibrary.Paplibrary.comment;

import com.freelibrary.Paplibrary.comment.CommentDto;

import java.util.List;

public interface CommentService {
    void createComment(String postUrl, CommentDto commentDto);

    List<CommentDto> findAllComments();

    void deleteComment(Long commentId);
    void modifyComment(Long commentId);
    List<CommentDto> findCommentsByBook();
}
