package com.example.demo.service;

import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    // CASE 1: Transaction cơ bản
    @Transactional
    public void createAuthorWithBooks() {
        Author author = new Author(null, "J.K. Rowling");
        authorRepository.save(author);

        Book b1 = new Book();
        b1.setTitle("Harry Potter 1");
        b1.setAuthor(author);
        bookRepository.save(b1);

        Book b2 = new Book();
        b2.setTitle("Harry Potter 2");
        b2.setAuthor(author);
        bookRepository.save(b2);
    }

    // CASE 2: Rollback RuntimeException
    @Transactional
    public void createAuthorWithError() {
        Author author = new Author(null, "George R.R. Martin");
        authorRepository.save(author);

        Book b1 = new Book();
        b1.setTitle("Game of Thrones");
        b1.setAuthor(author);
        bookRepository.save(b1);

        throw new RuntimeException("Simulated failure!");
    }

    // CASE 3: Không rollback khi catch
    @Transactional
    public void createAuthorAndCatchError() {
        Author author = new Author(null, "J.R.R. Tolkien");
        authorRepository.save(author);

        try {
            throw new RuntimeException("Caught exception");
        } catch (Exception e) {
            System.out.println("Error caught -> transaction will COMMIT");
        }
    }

    // CASE 4: Rollback checked exception
    @Transactional(rollbackFor = Exception.class)
    public void createAuthorWithCheckedException() throws Exception {
        Author author = new Author(null, "Agatha Christie");
        authorRepository.save(author);
        throw new Exception("Checked exception -> rollback");
    }

    // CASE 5: inner() join transaction outerCall -> rollback checked exception
    @Transactional(rollbackFor = Exception.class)
    public void inner() {
        Author author = new Author(null, "Self Invocation");
        authorRepository.save(author);
    }

    // CASE 8: Isolation level
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void readAuthor(Long id) {
        Author a1 = authorRepository.findById(id).orElseThrow();
        System.out.println("First read: " + a1.getName());

        Author a2 = authorRepository.findById(id).orElseThrow();
        System.out.println("Second read (still same): " + a2.getName());
    }

    // CASE 9: @Async
    @Async
    @Transactional
    public void asyncCreate() {
        Author author = new Author(null, "Async Author");
        authorRepository.save(author);
        throw new RuntimeException("This will NOT rollback — runs on new thread");
    }

    // CASE 10: NOT_SUPPORTED
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void nonTransactionalOperation() {
        System.out.println("Running without transaction");
    }

    // CASE 7 helper
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveBookWithRequiresNew(Author author) {
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle("Inferno");
        bookRepository.save(book);
        throw new RuntimeException("Book save failed");
    }

    // CASE 7 main
    @Transactional
    public void createAuthorWithIndependentLog() {
        Author author = new Author(null, "Dan Brown");
        authorRepository.save(author);
        try {
            AuthorService proxy = (AuthorService) AopContext.currentProxy();
            proxy.saveBookWithRequiresNew(author);
        } catch (Exception e) {
            System.out.println("Log failed, main transaction still commits");
        }
    }
}
