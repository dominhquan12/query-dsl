package com.example.demo.service;

import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class NestTransactional {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void outerForNested() {
        // 🧩 1️⃣ Bắt đầu transaction cha
        Author outerAuthor = new Author(null, "Outer Author");
        authorRepository.save(outerAuthor);

        NestTransactional proxy = (NestTransactional) AopContext.currentProxy();
        try {
            // 🧩 2️⃣ Gọi vào transaction con (nested)
            proxy.nestedOperation(outerAuthor);
        } catch (Exception e) {
            System.out.println("Nested transaction rolled back, outer continues");
        }

        // 🧩 3️⃣ Sau khi nested rollback, outer vẫn tiếp tục
        Book book = new Book();
        book.setAuthor(outerAuthor);
        book.setTitle("Book from Outer");
        bookRepository.save(book);

        // Outer commit ở đây
    }
    @Transactional(propagation = Propagation.NESTED)
    public void nestedOperation(Author author) {
        // 🧩 1️⃣ Nested bắt đầu, tạo savepoint
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle("Book from Nested");
        bookRepository.save(book);

        // 🧩 2️⃣ Lỗi xảy ra → rollback phần nested
        throw new RuntimeException("Nested TX failed");
    }

}
