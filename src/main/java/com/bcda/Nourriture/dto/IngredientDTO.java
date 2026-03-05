package com.bcda.Nourriture.dto;

import com.bcda.Nourriture.entity.IngredientType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDTO {

    private Long id;

    @NotBlank(message = "Le libellé de l'ingrédient est requis")
    private String libelle;

    @NotNull(message = "Le type d'ingrédient est requis")
    private IngredientType type;

    @NotNull(message = "Le nombre de calories est requis")
    @PositiveOrZero(message = "Le nombre de calories doit être positif ou zéro")
    @JsonProperty("nombre_calorie")
    private Integer nombreCalorie;

    @JsonProperty("created_at")
    private String createdAt;
}
