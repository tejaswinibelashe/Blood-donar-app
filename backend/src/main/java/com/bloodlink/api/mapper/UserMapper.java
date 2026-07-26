package com.bloodlink.api.mapper;

import com.bloodlink.api.dto.UserDTO;
import com.bloodlink.api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setBloodGroup(user.getBloodGroup());
        dto.setGender(user.getGender());
        dto.setAge(user.getAge());
        dto.setRole(user.getRole());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        
        return dto;
    }
}
