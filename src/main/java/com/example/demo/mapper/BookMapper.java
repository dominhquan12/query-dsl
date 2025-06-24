package com.example.demo.mapper;

import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO toDto(Book book);

    Book toEntity(BookDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "author", ignore = true)
    void updateBookFromDto(BookDTO dto, @MappingTarget Book entity);
}
