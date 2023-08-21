package com.example.blogapp.service;

import com.example.blogapp.dto.GetAllPostsResponseDto;
import com.example.blogapp.dto.PostDto;
import com.example.blogapp.model.Post;
import org.springframework.data.domain.Page;


public interface PostService {
    Post createPost(String title, String description, String content);
    Page<Post> getAllPosts(int pageNum, int pageSize, String sortBy, String sortDir);
    Post getPostById(Long id);
    Post updatePost(String title, String description, String content, long id);

    void deletePost(Long id);
}
