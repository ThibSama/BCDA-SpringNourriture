package com.bcda.Nourriture.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcda.Nourriture.entity.Recette;
import com.bcda.Nourriture.entity.RecetteIngredient;
import com.bcda.Nourriture.entity.User;
import com.bcda.Nourriture.repository.RecetteIngredientRepository;
import com.bcda.Nourriture.repository.RecetteRepository;
import com.bcda.Nourriture.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecetteService {

    private final RecetteRepository recetteRepository;
    private final RecetteIngredientRepository recetteIngredientRepository;
    private final UserRepository userRepository;

    public Recette createRecette(Recette recette) {
        Recette savedRecette = recetteRepository.save(recette);
        log.info("Nouvelle recette créée : {} by {}", savedRecette.getNomPlat(), 
                savedRecette.getUser() != null ? savedRecette.getUser().getMail() : "anonyme");
        return savedRecette;
    }

    public Optional<Recette> getRecetteById(Long id) {
        return recetteRepository.findById(id);
    }

    public List<Recette> getAllRecettes() {
        return recetteRepository.findAll();
    }

    public List<Recette> getRecettesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return recetteRepository.findByUser(user);
    }

    public List<Recette> getSharedRecettes() {
        return recetteRepository.findByPartageTrue();
    }

    public Recette updateRecette(Long id, Recette recetteDetails) {
        Recette recette = recetteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée avec l'ID : " + id));
        
        recette.setNomPlat(recetteDetails.getNomPlat());
        recette.setDureePreparation(recetteDetails.getDureePreparation());
        recette.setDureeCuisson(recetteDetails.getDureeCuisson());
        recette.setNombreCalorie(recetteDetails.getNombreCalorie());
        recette.setPartage(recetteDetails.getPartage());
        
        Recette updatedRecette = recetteRepository.save(recette);
        log.info("Recette mise à jour : {}", updatedRecette.getNomPlat());
        return updatedRecette;
    }

    public void deleteRecette(Long id) {
        if (!recetteRepository.existsById(id)) {
            throw new RuntimeException("Recette non trouvée avec l'ID : " + id);
        }
        recetteRepository.deleteById(id);
        log.info("Recette supprimée avec l'ID : {}", id);
    }

    public void addIngredientToRecette(Long recetteId, RecetteIngredient recetteIngredient) {
        Recette recette = recetteRepository.findById(recetteId)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée"));
        recetteIngredient.setRecette(recette);
        recetteIngredientRepository.save(recetteIngredient);
        log.info("Ingrédient ajouté à la recette : {}", recette.getNomPlat());
    }

    public void removeIngredientFromRecette(Long recetteIngredientId) {
        if (!recetteIngredientRepository.existsById(recetteIngredientId)) {
            throw new RuntimeException("Lien recette-ingrédient non trouvé");
        }
        recetteIngredientRepository.deleteById(recetteIngredientId);
        log.info("Ingrédient retiré de la recette");
    }

    public List<RecetteIngredient> getRecetteIngredients(Long recetteId) {
        Recette recette = recetteRepository.findById(recetteId)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée"));
        return recetteIngredientRepository.findByRecette(recette);
    }

    public void addFavorite(Long userId, Long recetteId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Recette recette = recetteRepository.findById(recetteId)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée"));
        
        user.getRecettesFavorites().add(recette);
        userRepository.save(user);
        log.info("Recette ajoutée aux favoris de l'utilisateur : {}", user.getMail());
    }

    public void removeFavorite(Long userId, Long recetteId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Recette recette = recetteRepository.findById(recetteId)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée"));
        
        user.getRecettesFavorites().remove(recette);
        userRepository.save(user);
        log.info("Recette retirée des favoris de l'utilisateur : {}", user.getMail());
    }

    public Set<Recette> getUserFavorites(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return user.getRecettesFavorites();
    }
}
