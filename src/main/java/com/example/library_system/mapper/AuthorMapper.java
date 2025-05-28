package com.example.library_system.mapper;

import com.example.library_system.dto.AuthorDTO;
import com.example.library_system.entity.Author;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {
    public AuthorDTO toDTO(Author author){
        if(author == null){
            return null;
        }
        AuthorDTO dto = new AuthorDTO();
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        dto.setBirthYear(author.getBirthYear());
        dto.setNationality(author.getNationality());
        return dto;
    }

    public Author toEntity(AuthorDTO dto){
        if(dto == null){
            return null;
        }
        Author author = new Author();
        author.setFirstName(dto.getFirstName());
        author.setLastName(dto.getLastName());
        author.setBirthYear(dto.getBirthYear());
        author.setNationality(dto.getNationality());
        return author;
    }

    public List<AuthorDTO> toDTOList(List<Author> author){
        if(author == null){
            return Collections.emptyList();
        }
        return author.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<Author> toEntityList(List<AuthorDTO> DTOs){
        if(DTOs == null){
            return Collections.emptyList();
        }
        return DTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
