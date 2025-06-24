package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.mapper.AuthorMapper;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private AuthorMapper authorMapper;

    @Transactional
    public Book createBook(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        String authorName = book.getAuthor().getName();

        // Tìm tác giả theo tên
        Author author = authorRepository.findByName(authorName)
                .orElseGet(() -> {
                    Author newAuthor = authorMapper.toEntity(bookDTO.getAuthor());
                    return authorRepository.save(newAuthor);
                });

        book.setAuthor(author);

        Optional<Book> existing = bookRepository.findByTitleAndAuthor(book.getTitle(), author);
        if (existing.isPresent()) {
            return existing.get(); // trả lại bản cũ, không tạo mới
        }
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Long id, BookDTO bookDTO) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        bookMapper.updateBookFromDto(bookDTO, existing);

        // Nếu author != null, gán lại bản author từ DB theo name
        if (bookDTO.getAuthor() != null && bookDTO.getAuthor().getName() != null) {
            String authorName = bookDTO.getAuthor().getName();

            Author author = authorRepository.findByName(authorName)
                    .orElseGet(() -> {
                        Author newAuthor = authorMapper.toEntity(bookDTO.getAuthor());
                        return authorRepository.save(newAuthor);
                    });

            existing.setAuthor(author);
        }

        return bookRepository.save(existing);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}

