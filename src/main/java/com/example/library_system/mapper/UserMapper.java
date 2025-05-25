package com.example.library_system.mapper;

import com.example.library_system.dto.UserDTO;
import com.example.library_system.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserDTO toDTO(User user){
        if(user == null){
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public User toEntity(UserDTO dto){
        if(dto == null){
            return null;
        }
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        return user;
    }

    public List<UserDTO> toDTOList(List<User> user){
        if(user == null){
            return Collections.emptyList();
        }
        return user.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<User> toEntityList(List<UserDTO> DTOs){
        if(DTOs == null){
            return Collections.emptyList();
        }
        return DTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }

}
