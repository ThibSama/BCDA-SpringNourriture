package com.bcda.Nourriture.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetteDTO {

    private Long id;

    @NotBlank(message = "Le nom du plat est requis")
    @JsonProperty("nom_plat")
    private String nomPlat;

    @NotNull(message = "La durée de préparation est requise")
    @Positive(message = "La durée de préparation doit être positive")
    @JsonProperty("duree_preparation")
    private Integer dureePreparation;

    @NotNull(message = "La durée de cuisson est requise")
    @Positive(message = "La durée de cuisson doit être positive")
    @JsonProperty("duree_cuisson")
    private Integer dureeCuisson;

    @NotNull(message = "Le nombre de calories est requis")
    @Positive(message = "Le nombre de calories doit être positif")
    @JsonProperty("nombre_calorie")
    private Integer nombreCalorie;

    @JsonProperty("partage")
    private Boolean partage;

    @JsonProperty("user_id")
    private Long userId;

    private UserDTO user;

    @JsonProperty("ingredients")
    private List<RecetteIngredientDTO> recetteIngredients;

    @JsonProperty("nombre_utilisateurs_favoris")
    private Integer nombreUtilisateursFavoris;

    @JsonProperty("created_at")
    private String createdAt;
}
