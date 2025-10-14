package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class BaseFilter {
    private Integer pageIndex;
    private Integer pageSize;
    protected List<SortRequest> sort;

    @Data
    public static class SortRequest {
        private String field;
        private String direction;
    }
}

