package com.example.demo.dto;

import lombok.Data;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private AuthorDTO author;
}
