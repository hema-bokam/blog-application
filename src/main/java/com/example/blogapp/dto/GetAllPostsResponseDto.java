package com.example.blogapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetAllPostsResponseDto {
    private List<PostDto> content;
    private int pageNum;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
