package com.example.demo.mapper;

import com.example.demo.dto.AuthorDTO;
import com.example.demo.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDTO toDto(Author author);

    Author toEntity(AuthorDTO dto);

}