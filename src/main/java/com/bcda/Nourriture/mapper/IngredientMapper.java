package com.bcda.Nourriture.mapper;

import com.bcda.Nourriture.dto.IngredientDTO;
import com.bcda.Nourriture.entity.Ingredient;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IngredientMapper {

    public IngredientDTO toDTO(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        return IngredientDTO.builder()
                .id(ingredient.getId())
                .libelle(ingredient.getLibelle())
                .type(ingredient.getType())
                .nombreCalorie(ingredient.getNombreCalorie())
                .build();
    }

    public Ingredient toEntity(IngredientDTO ingredientDTO) {
        if (ingredientDTO == null) {
            return null;
        }

        return Ingredient.builder()
                .id(ingredientDTO.getId())
                .libelle(ingredientDTO.getLibelle())
                .type(ingredientDTO.getType())
                .nombreCalorie(ingredientDTO.getNombreCalorie())
                .build();
    }
}
