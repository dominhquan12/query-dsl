package com.example.demo.repository;

import com.example.demo.dto.AuthorDTO;
import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BookFilter;
import com.example.demo.dto.PageResponse;
import com.example.demo.entity.QAuthor;
import com.example.demo.entity.QBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookRepositoryCustom(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public PageResponse<BookDTO> searchBook(BookFilter bookFilter) {
        QBook book = QBook.book;
        QAuthor author = QAuthor.author;

        // Predicate
        BooleanBuilder where = buildWhereCondition(bookFilter, book, author);

        // Sorting
        OrderSpecifier<?> order = getOrderSpecifier(bookFilter, book, author);

        JPAQuery<BookDTO> baseQuery = queryFactory
                .select(Projections.constructor(BookDTO.class,
                        book.id,
                        book.title,
                        Projections.constructor(AuthorDTO.class, author.id, author.name)))
                .from(book)
                .join(book.author(), author)
                .where(where);

        List<BookDTO> content = baseQuery.clone()
                .orderBy(order)
                .offset((long) bookFilter.getPageIndex() * bookFilter.getPageSize())
                .limit(bookFilter.getPageSize())
                .fetch();

        Long total = baseQuery.clone()
                .select(book.count())
                .fetchOne();

        long totalElements = Optional.ofNullable(total).orElse(0L);
        int totalPages = (int) Math.ceil((double) totalElements / bookFilter.getPageSize());

        return new PageResponse<>(content, totalElements, totalPages, bookFilter.getPageIndex(), bookFilter.getPageSize());
    }

    private BooleanBuilder buildWhereCondition(BookFilter bookFilter, QBook book, QAuthor author) {
        BooleanBuilder where = new BooleanBuilder();

        String title = bookFilter.getTitle();
        String authorName = bookFilter.getAuthorName();

        if (title != null && !title.isBlank()) {
            where.and(book.title.containsIgnoreCase(title));
        }
        if (authorName != null && !authorName.isBlank()) {
            where.and(author.name.containsIgnoreCase(authorName));
        }

        return where;
    }

    private OrderSpecifier<?> getOrderSpecifier(BookFilter bookFilter, QBook book, QAuthor author) {
        String sortBy = bookFilter.getSortBy() != null ? bookFilter.getSortBy() : "id";
        boolean isDesc = "DESC".equalsIgnoreCase(bookFilter.getSortDirection());

        return switch (sortBy) {
            case "title" -> isDesc ? book.title.desc() : book.title.asc();
            case "authorName" -> isDesc ? author.name.desc() : author.name.asc();
            case "id" -> isDesc ? book.id.desc() : book.id.asc();
            default -> book.id.asc();
        };
    }
}
