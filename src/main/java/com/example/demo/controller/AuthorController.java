package com.example.demo.controller;

import com.example.demo.service.AuthorService;
import com.example.demo.service.AuthorServiceCaller;
import com.example.demo.service.NestTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorServiceCaller caller; // thêm
    private final NestTransactional nestTransactional; // thêm

    @GetMapping("/test/{id}")
    public void test(@PathVariable int id) throws Exception {
        switch (id) {
            case 1 -> authorService.createAuthorWithBooks();
            case 2 -> authorService.createAuthorWithError();
            case 3 -> authorService.createAuthorAndCatchError();
            case 4 -> authorService.createAuthorWithCheckedException();

            // CASE 5,6,7 gọi qua caller để transaction có hiệu lực
            case 5 -> caller.outerCall();
            case 6 -> caller.outerCallProxy();
            case 7 -> caller.createAuthorWithIndependentLog();

            case 8 -> authorService.readAuthor(1L);
            case 9 -> authorService.asyncCreate();
            case 10 -> authorService.nonTransactionalOperation();
            case 11 -> nestTransactional.outerForNested();
        }
    }
}


