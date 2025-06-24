package com.example.demo;

import com.example.demo.dto.AuthorDTO;
import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.mapper.AuthorMapper;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private BookService bookService;

    private BookDTO bookDTO;
    private Book bookEntity;
    private Author author;

    @BeforeEach
    void setup() {
        author = new Author();
        author.setId(1L);
        author.setName("Nguyen Van A");

        bookDTO = new BookDTO();
        bookDTO.setTitle("Java 101");

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Nguyen Van A");
        bookDTO.setAuthor(authorDTO);

        bookEntity = new Book();
        bookEntity.setTitle("Java 101");
        bookEntity.setAuthor(author);
    }

    @Test
    void createBook_shouldUseExistingAuthorAndCreateBook() {
        when(bookMapper.toEntity(bookDTO)).thenReturn(bookEntity);
        when(authorRepository.findByName("Nguyen Van A")).thenReturn(Optional.of(author));
        when(bookRepository.findByTitleAndAuthor("Java 101", author)).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(bookEntity);

        Book result = bookService.createBook(bookDTO);

        assertThat(result.getAuthor().getName()).isEqualTo("Nguyen Van A");
        verify(authorRepository, never()).save(any());
        verify(bookRepository).save(bookEntity);
    }

    @Test
    void createBook_shouldCreateNewAuthorIfNotFound() {
        when(bookMapper.toEntity(bookDTO)).thenReturn(bookEntity);
        when(authorRepository.findByName("Nguyen Van A")).thenReturn(Optional.empty());
        when(authorMapper.toEntity(bookDTO.getAuthor())).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(bookRepository.findByTitleAndAuthor("Java 101", author)).thenReturn(Optional.empty());
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);

        Book result = bookService.createBook(bookDTO);

        assertThat(result.getAuthor().getName()).isEqualTo("Nguyen Van A");
        verify(authorRepository).save(author);
    }

    @Test
    void createBook_shouldReturnExistingBookIfDuplicate() {
        when(bookMapper.toEntity(bookDTO)).thenReturn(bookEntity);
        when(authorRepository.findByName("Nguyen Van A")).thenReturn(Optional.of(author));
        when(bookRepository.findByTitleAndAuthor("Java 101", author)).thenReturn(Optional.of(bookEntity));

        Book result = bookService.createBook(bookDTO);

        assertThat(result).isEqualTo(bookEntity);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_shouldChangeAuthorIfDifferent() {
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Old Title");
        existingBook.setAuthor(new Author(999L, "Old Author"));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        doAnswer(inv -> {
            BookDTO dto = inv.getArgument(0);
            Book target = inv.getArgument(1);
            target.setTitle(dto.getTitle());
            return null;
        }).when(bookMapper).updateBookFromDto(any(), any());

        when(authorRepository.findByName("Nguyen Van A")).thenReturn(Optional.of(author));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        Book updated = bookService.updateBook(1L, bookDTO);

        assertThat(updated.getTitle()).isEqualTo("Java 101");
        assertThat(updated.getAuthor().getName()).isEqualTo("Nguyen Van A");
    }

    @Test
    void updateBook_shouldCreateAuthorIfNotExist() {
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Old Title");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        doAnswer(inv -> {
            BookDTO dto = inv.getArgument(0);
            Book target = inv.getArgument(1);
            target.setTitle(dto.getTitle());
            return null;
        }).when(bookMapper).updateBookFromDto(any(), any());

        when(authorRepository.findByName("Nguyen Van A")).thenReturn(Optional.empty());
        when(authorMapper.toEntity(bookDTO.getAuthor())).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        Book updated = bookService.updateBook(1L, bookDTO);

        assertThat(updated.getAuthor().getName()).isEqualTo("Nguyen Van A");
        verify(authorRepository).save(author);
    }
}




