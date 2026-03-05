package com.bcda.Nourriture.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le libellé est requis")
    @Column(nullable = false)
    private String libelle;

    @NotNull(message = "Le type est requis")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IngredientType type;

    @NotNull(message = "Le nombre de calories est requis")
    @PositiveOrZero(message = "Le nombre de calories doit être positif ou zéro")
    @Column(name = "nombre_calorie", nullable = false)
    private Integer nombreCalorie;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecetteIngredient> recetteIngredients = new ArrayList<>();

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", type=" + type +
                ", nombreCalorie=" + nombreCalorie +
                '}';
    }
}
