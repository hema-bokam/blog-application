package com.example.blogapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetAllCommentsResponseDto {
    private List<CommentDto> content;
    private int pageNum;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
