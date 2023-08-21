package com.example.blogapp.controller;

import com.example.blogapp.dto.*;
import com.example.blogapp.model.Comment;
import com.example.blogapp.service.CommentService;
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
@RequestMapping("/blog/")
public class CommentController {
    private CommentService commentService;
    private ModelMapper modelMapper;
    @Autowired
    public CommentController(CommentService commentService, ModelMapper modelMapper){
        this.commentService = commentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> addCommentToPost(@Valid @RequestBody AddCommentDtoRequest request,
                                                      @PathVariable(name = "postId") Long post_id){
        Comment comment = commentService.addComment(post_id, request.getName(), request.getEmail(),request.getBody());
        return new ResponseEntity<>(mapToDto(comment), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<GetAllCommentsResponseDto> getAllComments(
            @PathVariable(name = "postId") long postId,
            @RequestParam(value = "pageNum", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        Page<Comment> page = commentService.getAllComments(postId, pageNum, pageSize, sortBy, sortDir);
        List<Comment> comments = page.getContent();
        List<CommentDto> commentDtos = new ArrayList<>();
        commentDtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
        GetAllCommentsResponseDto responseDto = new GetAllCommentsResponseDto();
        responseDto.setContent(commentDtos);
        responseDto.setPageNum(page.getNumber());
        responseDto.setTotalElements(page.getNumberOfElements());
        responseDto.setTotalPages(page.getTotalPages());
        responseDto.setLast(page.isLast());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentByIdAndPostId(@PathVariable(name = "postId") long postId,
                                                              @PathVariable(name = "id") long id){
        Comment comment = commentService.getCommentById(postId, id);
        return new ResponseEntity<>(mapToDto(comment), HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody UpdateCommentDto request,
                                                    @PathVariable(name = "postId") long postId,
                                                    @PathVariable(name = "id") long id

    ){
        Comment comment = commentService.updateComment(postId, id, request.getBody());
        return new ResponseEntity<>(mapToDto(comment), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "postId") long postId,
                                                @PathVariable(name = "id") long id
    ){
        commentService.deleteComment(postId, id);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
    private CommentDto mapToDto(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }
}
