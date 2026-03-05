package com.bcda.Nourriture.dto;

import com.bcda.Nourriture.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String nom;

    private String prenom;

    @JsonProperty("email")
    private String mail;

    private String telephone;

    private UserRole role;

    @JsonProperty("created_at")
    private String createdAt;
}
