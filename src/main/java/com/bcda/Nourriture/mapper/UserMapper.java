package com.bcda.Nourriture.mapper;

import com.bcda.Nourriture.dto.UserDTO;
import com.bcda.Nourriture.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .mail(user.getMail())
                .telephone(user.getTelephone())
                .role(user.getRole())
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return User.builder()
                .id(userDTO.getId())
                .nom(userDTO.getNom())
                .prenom(userDTO.getPrenom())
                .mail(userDTO.getMail())
                .telephone(userDTO.getTelephone())
                .role(userDTO.getRole())
                .build();
    }
}
