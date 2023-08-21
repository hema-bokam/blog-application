package com.example.blogapp.controller;

import com.example.blogapp.dto.*;
import com.example.blogapp.model.Post;
import com.example.blogapp.service.PostService;
import com.example.blogapp.utils.AppConstants;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("blog/posts")
public class PostController {
    private PostService postService;
    private ModelMapper modelMapper;
    @Autowired
    public PostController(PostService postService, ModelMapper modelMapper){
        this.postService = postService;
        this.modelMapper = modelMapper;
    }

    //It will create new Post
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CreatePostResponseDto> createPost(@Valid @RequestBody CreatePostRequestDto requestDto){
        Post post = postService.createPost(requestDto.getTitle(), requestDto.getDescription(),requestDto.getContent());
        CreatePostResponseDto responseDto = new CreatePostResponseDto();
        responseDto.setId(post.getId());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<GetAllPostsResponseDto> getAllPosts(
            @RequestParam (value = "pageNum", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNum,
           @RequestParam (value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam (value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam (value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){

        Page<Post> page = postService.getAllPosts(pageNum, pageSize, sortBy, sortDir);
        List<Post> posts = page.getContent();
        List<PostDto> postDtos = new ArrayList<>();
        postDtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        GetAllPostsResponseDto responseDto = new GetAllPostsResponseDto();
        responseDto.setContent(postDtos);
        responseDto.setPageNum(page.getNumber());
        responseDto.setTotalPages(page.getTotalPages());
        responseDto.setTotalElements(page.getTotalElements());
        responseDto.setLast(page.isLast());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id){
        Post post = postService.getPostById(id);
        PostDto postDto = mapToDto(post);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody UpdatePostDto requestDto, @PathVariable(name = "id") Long id){
        Post post = postService.updatePost(requestDto.getTitle(), requestDto.getDescription(), requestDto.getContent(), id);
        PostDto postDto = mapToDto(post);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id){
        postService.deletePost(id);
        return new ResponseEntity<>("Post deleted Successfully", HttpStatus.OK);
    }

    public PostDto mapToDto(Post post){
        return modelMapper.map(post, PostDto.class);
    }
}
