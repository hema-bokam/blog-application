package com.example.blogapp.service.impl;

import com.example.blogapp.exception.ResourceNotFoundException;
import com.example.blogapp.model.Post;
import com.example.blogapp.repository.PostRepository;
import com.example.blogapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    @Autowired
    public PostServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public Post createPost(String title, String description, String content){
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setContent(content);
        Post savedPost = postRepository.save(post);
        return savedPost;
    }

    @Override
    public Page<Post> getAllPosts(int pageNum, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Post> page = postRepository.findAll(pageable);
        return page;
    }

    @Override
    public Post getPostById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if(postOptional.isEmpty()){
            throw new ResourceNotFoundException("posts", "id", id);
        }
        return postOptional.get();
    }

    @Override
    public Post updatePost(String title, String description, String content, long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        post.setTitle(title);
        post.setDescription(description);
        post.setContent(content);

        Post savedPost = postRepository.save(post);
        return savedPost;
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }
}
