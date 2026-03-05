package com.bcda.Nourriture.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recettes")
@Data
@EqualsAndHashCode(exclude = {"user", "recetteIngredients", "usersFavorites"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recette {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du plat est requis")
    @Column(nullable = false)
    private String nomPlat;

    @NotNull(message = "La durée de préparation est requise")
    @Positive(message = "La durée de préparation doit être positive")
    @Column(name = "duree_preparation", nullable = false)
    private Integer dureePreparation; // en minutes

    @NotNull(message = "La durée de cuisson est requise")
    @Positive(message = "La durée de cuisson doit être positive")
    @Column(name = "duree_cuisson", nullable = false)
    private Integer dureeCuisson; // en minutes

    @NotNull(message = "Le nombre calorique est requis")
    @Positive(message = "Le nombre calorique doit être positif")
    @Column(name = "nombre_calorie", nullable = false)
    private Integer nombreCalorie;

    @Builder.Default
    @Column(nullable = false)
    private Boolean partage = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // Propriétaire de la recette personnelle

    @OneToMany(mappedBy = "recette", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecetteIngredient> recetteIngredients = new ArrayList<>();

    @ManyToMany(mappedBy = "recettesFavorites")
    @Builder.Default
    private Set<User> usersFavorites = new HashSet<>();

    @Override
    public String toString() {
        return "Recette{" +
                "id=" + id +
                ", nomPlat='" + nomPlat + '\'' +
                ", dureePreparation=" + dureePreparation +
                ", dureeCuisson=" + dureeCuisson +
                ", nombreCalorie=" + nombreCalorie +
                ", partage=" + partage +
                '}';
    }
}
