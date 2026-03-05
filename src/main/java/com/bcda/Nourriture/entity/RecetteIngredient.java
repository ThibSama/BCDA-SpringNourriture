package com.bcda.Nourriture.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recette_ingredient", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"recette_id", "ingredient_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetteIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La quantité est requise")
    @Positive(message = "La quantité doit être positive")
    @Column(nullable = false)
    private Double quantite;

    @NotBlank(message = "L'unité est requise")
    @Column(nullable = false)
    private String unite; // g, ml, cuillère à café, etc.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recette_id", nullable = false)
    private Recette recette;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Override
    public String toString() {
        return "RecetteIngredient{" +
                "id=" + id +
                ", quantite=" + quantite +
                ", unite='" + unite + '\'' +
                '}';
    }
}
