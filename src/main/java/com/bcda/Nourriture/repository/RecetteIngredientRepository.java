package com.bcda.Nourriture.repository;

import com.bcda.Nourriture.entity.Recette;
import com.bcda.Nourriture.entity.RecetteIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetteIngredientRepository extends JpaRepository<RecetteIngredient, Long> {
    List<RecetteIngredient> findByRecette(Recette recette);
}
