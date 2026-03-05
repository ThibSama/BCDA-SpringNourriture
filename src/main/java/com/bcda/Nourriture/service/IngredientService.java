package com.bcda.Nourriture.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcda.Nourriture.entity.Ingredient;
import com.bcda.Nourriture.entity.IngredientType;
import com.bcda.Nourriture.repository.IngredientRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public Ingredient createIngredient(Ingredient ingredient) {
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        log.info("Nouvel ingrédient créé : {} ({})", savedIngredient.getLibelle(), savedIngredient.getType());
        return savedIngredient;
    }

    public Optional<Ingredient> getIngredientById(Long id) {
        return ingredientRepository.findById(id);
    }

    public Optional<Ingredient> getIngredientByLibelle(String libelle) {
        return ingredientRepository.findByLibelle(libelle);
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public List<Ingredient> getIngredientsByType(IngredientType type) {
        return ingredientRepository.findAll().stream()
                .filter(i -> i.getType() == type)
                .toList();
    }

    public Ingredient updateIngredient(Long id, Ingredient ingredientDetails) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé avec l'ID : " + id));
        
        ingredient.setLibelle(ingredientDetails.getLibelle());
        ingredient.setType(ingredientDetails.getType());
        ingredient.setNombreCalorie(ingredientDetails.getNombreCalorie());
        
        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        log.info("Ingrédient mis à jour : {}", updatedIngredient.getLibelle());
        return updatedIngredient;
    }

    public void deleteIngredient(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new RuntimeException("Ingrédient non trouvé avec l'ID : " + id);
        }
        ingredientRepository.deleteById(id);
        log.info("Ingrédient supprimé avec l'ID : {}", id);
    }
}
