package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorServiceCaller {

    private final AuthorService authorService;

    // CASE 5: outerCall rollback checked exception
    @Transactional(rollbackFor = Exception.class)
    public void outerCall() throws Exception {
        authorService.inner(); // join transaction
        throw new Exception("Checked exception -> rollback");
    }

    // CASE 6
    @Transactional
    public void outerCallProxy() {
        authorService.inner();
    }

    // CASE 7
    public void createAuthorWithIndependentLog() {
        authorService.createAuthorWithIndependentLog();
    }
}
