package com.example.demo.controller;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BookFilter;
import com.example.demo.dto.PageResponse;
import com.example.demo.entity.Book;
import com.example.demo.mapper.BookMapper;
import com.example.demo.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {

    private BookService bookService;

    private BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books.stream().map(bookMapper::toDto).toList());
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<BookDTO>> searchBooks(@RequestBody BookFilter bookFilter) {
        PageResponse<BookDTO> books = bookService.searchBooks(bookFilter);
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        Book created = bookService.createBook(bookDTO);
        return ResponseEntity.ok(bookMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        Book updated = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(bookMapper.toDto(updated));
    }
}
