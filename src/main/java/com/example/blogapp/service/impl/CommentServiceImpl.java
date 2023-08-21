package com.example.blogapp.service.impl;

import com.example.blogapp.exception.ApiException;
import com.example.blogapp.exception.ResourceNotFoundException;
import com.example.blogapp.model.Comment;
import com.example.blogapp.model.Post;
import com.example.blogapp.repository.CommentRepository;
import com.example.blogapp.repository.PostRepository;
import com.example.blogapp.service.CommentService;
import com.example.blogapp.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class CommentServiceImpl implements CommentService {
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    @Autowired
    public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository){
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }
    @Override
    public Comment addComment(Long postId, String name, String email, String body) {
        Post post = getPostFromDB(postId);
        Comment comment = new Comment();
        comment.setName(name);
        comment.setEmail(email);
        comment.setBody(body);
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        return newComment;
    }

    @Override
    public Page<Comment> getAllComments(Long postId,int pageNum, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(AppConstants.DEFAULT_SORT_DIRECTION) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Comment> page = commentRepository.findAllByPostId(postId, pageable);
        return page;
    }

    @Override
    public Comment getCommentById(Long postId, Long id) {
        Post post = getPostFromDB(postId);
        Comment comment = getCommentFromDB(id);
        if(!comment.getPost().getId().equals(post.getId())){
            //comment doesn't belong to given post, so throw exception
            throw new ApiException(HttpStatus.BAD_REQUEST, "Comment doesn't belong to given post_id");
        }
        return comment;
    }

    @Override
    public Comment updateComment(long postId, long id, String body) {
        Post post = getPostFromDB(postId);
        Comment comment = getCommentFromDB(id);
        if(!comment.getPost().getId().equals(post.getId())){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Comment doesn't belong to given post_id");
        }
        comment.setBody(body);
        Comment savedComment = commentRepository.save(comment);
        return savedComment;
    }

    @Override
    public void deleteComment(long postId, long id) {
        Post post = getPostFromDB(postId);
        Comment comment = getCommentFromDB(id);
        if(!comment.getPost().getId().equals(post.getId())){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Comment doesn't belong to given post_id");
        }
        commentRepository.delete(comment);
    }
    public Post getPostFromDB(long postId){
        return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "id", postId));
    }
    public Comment getCommentFromDB(long id){
       return commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
    }
}
