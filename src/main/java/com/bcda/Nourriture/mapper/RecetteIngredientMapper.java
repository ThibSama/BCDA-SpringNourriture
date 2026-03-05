package com.bcda.Nourriture.mapper;

import com.bcda.Nourriture.dto.RecetteIngredientDTO;
import com.bcda.Nourriture.entity.RecetteIngredient;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RecetteIngredientMapper {

    public RecetteIngredientDTO toDTO(RecetteIngredient recetteIngredient) {
        if (recetteIngredient == null) {
            return null;
        }

        return RecetteIngredientDTO.builder()
                .id(recetteIngredient.getId())
                .ingredient(IngredientMapper.toDTO(recetteIngredient.getIngredient()))
                .quantite(recetteIngredient.getQuantite())
                .unite(recetteIngredient.getUnite())
                .build();
    }

    public RecetteIngredient toEntity(RecetteIngredientDTO recetteIngredientDTO) {
        if (recetteIngredientDTO == null) {
            return null;
        }

        return RecetteIngredient.builder()
                .id(recetteIngredientDTO.getId())
                .ingredient(IngredientMapper.toEntity(recetteIngredientDTO.getIngredient()))
                .quantite(recetteIngredientDTO.getQuantite())
                .unite(recetteIngredientDTO.getUnite())
                .build();
    }
}
