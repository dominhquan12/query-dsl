package com.example.demo;

import com.example.demo.entity.Author;
import com.example.demo.repository.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OptionalTest {

    @Mock
    private AuthorRepository authorRepository;

    @Test
    void testOrElse_vs_orElseGet() {
        Author fakeAuthor = new Author();
        fakeAuthor.setName("Nguyen Van A");

        // Giả vờ tìm thấy Author trong DB
        Mockito.when(authorRepository.findByName("Nguyen Van A"))
                .thenReturn(Optional.of(fakeAuthor));

        // `orElse` — luôn gọi save, dù không cần
        System.out.println("===> orElse example");
        Author author1 = authorRepository.findByName("Nguyen Van A")
                .orElse(saveAuthor("OR_ELSE"));

        // `orElseGet` — chỉ gọi save nếu không tìm thấy
        System.out.println("===> orElseGet example");
        Author author2 = authorRepository.findByName("Nguyen Van A")
                .orElseGet(() -> saveAuthor("OR_ELSE_GET"));
    }

    private Author saveAuthor(String tag) {
        System.out.println("💾 saveAuthor(" + tag + ") is called!");
        Author a = new Author();
        a.setName(tag);
        return a;
    }
}
