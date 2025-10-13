package com.example.demo.dto;

import lombok.Data;

@Data
public class BookFilter {
    private Long id;
    private Integer pageIndex;
    private Integer pageSize;
    private String sortBy;
    private String sortDirection;
    private String title;
    private String authorName;
}