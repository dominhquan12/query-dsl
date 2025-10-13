package com.example.demo.dto;

import lombok.Data;

@Data
public class BookFilter extends BaseFilter {
    private String title;
    private String authorName;
}