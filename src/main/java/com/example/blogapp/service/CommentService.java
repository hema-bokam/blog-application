package com.example.blogapp.service;

import com.example.blogapp.model.Comment;
import org.springframework.data.domain.Page;

public interface CommentService {
    Comment addComment(Long postId, String name, String email, String body);
    Page<Comment> getAllComments(Long postId, int pageNum, int pageSize, String sortBy, String sortDir);
    Comment getCommentById(Long postId, Long id);
    Comment updateComment(long postId, long id, String body);
    void deleteComment(long postId, long id);
}
