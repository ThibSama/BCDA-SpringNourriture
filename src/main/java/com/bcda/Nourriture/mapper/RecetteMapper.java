package com.bcda.Nourriture.mapper;

import com.bcda.Nourriture.dto.RecetteDTO;
import com.bcda.Nourriture.entity.Recette;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.stream.Collectors;

@UtilityClass
public class RecetteMapper {

    public RecetteDTO toDTO(Recette recette) {
        if (recette == null) {
            return null;
        }

        return RecetteDTO.builder()
                .id(recette.getId())
                .nomPlat(recette.getNomPlat())
                .dureePreparation(recette.getDureePreparation())
                .dureeCuisson(recette.getDureeCuisson())
                .nombreCalorie(recette.getNombreCalorie())
                .partage(recette.getPartage())
                .userId(recette.getUser() != null ? recette.getUser().getId() : null)
                .user(recette.getUser() != null ? UserMapper.toDTO(recette.getUser()) : null)
                .recetteIngredients(
                    recette.getRecetteIngredients() != null 
                        ? recette.getRecetteIngredients().stream()
                            .map(RecetteIngredientMapper::toDTO)
                            .collect(Collectors.toList())
                        : Collections.emptyList()
                )
                .nombreUtilisateursFavoris(
                    recette.getUsersFavorites() != null ? recette.getUsersFavorites().size() : 0
                )
                .build();
    }

    public Recette toEntity(RecetteDTO recetteDTO) {
        if (recetteDTO == null) {
            return null;
        }

        return Recette.builder()
                .id(recetteDTO.getId())
                .nomPlat(recetteDTO.getNomPlat())
                .dureePreparation(recetteDTO.getDureePreparation())
                .dureeCuisson(recetteDTO.getDureeCuisson())
                .nombreCalorie(recetteDTO.getNombreCalorie())
                .partage(recetteDTO.getPartage() != null ? recetteDTO.getPartage() : false)
                .build();
    }
}
