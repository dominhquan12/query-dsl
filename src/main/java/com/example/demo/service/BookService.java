package com.example.demo.service;

import com.example.demo.aop.CustomNotFoundException;
import com.example.demo.aop.ErrorCode;
import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.mapper.AuthorMapper;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {

    BookRepository bookRepository;
    AuthorRepository authorRepository;
    BookMapper bookMapper;
    AuthorMapper authorMapper;

    @Transactional
    public Book createBook(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);

        Author author = authorRepository.findByName(book.getAuthor().getName())
                .orElseGet(() -> authorRepository.save(book.getAuthor()));

        book.setAuthor(author);

        return bookRepository.findByTitleAndAuthor(book.getTitle(), author)
                .orElseGet(() -> bookRepository.save(book));
    }

    @Transactional
    public Book updateBook(Long id, BookDTO bookDTO) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.BOOK_NOT_FOUND));

        bookMapper.updateBookFromDto(bookDTO, existing);

        Author author = authorRepository.findByName(bookDTO.getAuthor().getName())
                .orElseGet(() -> authorRepository.save(authorMapper.toEntity(bookDTO.getAuthor())));

        existing.setAuthor(author);

        return bookRepository.save(existing);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}

