package com.bcda.Nourriture.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetteIngredientDTO {

    private Long id;

    @NotNull(message = "L'ingrédient est requis")
    private IngredientDTO ingredient;

    @NotNull(message = "La quantité est requise")
    @Positive(message = "La quantité doit être positive")
    private Double quantite;

    @NotNull(message = "L'unité est requise")
    private String unite;

    @JsonProperty("created_at")
    private String createdAt;
}
