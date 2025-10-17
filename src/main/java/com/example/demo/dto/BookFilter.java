package com.example.demo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class BookFilter extends BaseFilter {
    private String title;
    private String authorName;
}