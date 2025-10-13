package com.example.demo.dto;

import lombok.Data;

@Data
public class BaseFilter {
    private Integer pageIndex;
    private Integer pageSize;
    private String sortBy;
    private String sortDirection;
}