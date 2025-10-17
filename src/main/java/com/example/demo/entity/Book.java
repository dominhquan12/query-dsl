package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
