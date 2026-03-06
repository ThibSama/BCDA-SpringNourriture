package com.bcda.Nourriture.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
public class CreateRecetteDTO {

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

    private Boolean partage;
}
