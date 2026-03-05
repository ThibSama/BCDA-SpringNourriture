package com.bcda.Nourriture.controller;

import com.bcda.Nourriture.dto.RecetteDTO;
import com.bcda.Nourriture.dto.RecetteIngredientDTO;
import com.bcda.Nourriture.mapper.RecetteIngredientMapper;
import com.bcda.Nourriture.mapper.RecetteMapper;
import com.bcda.Nourriture.service.RecetteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recettes")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class RecetteController {

    private final RecetteService recetteService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RecetteDTO> createRecette(
            @Valid @RequestBody RecetteDTO recetteDTO,
            Authentication authentication) {
        log.info("Création d'une nouvelle recette : {}", recetteDTO.getNomPlat());
        
        var recette = recetteService.createRecette(RecetteMapper.toEntity(recetteDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(RecetteMapper.toDTO(recette));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RecetteDTO>> getAllRecettes() {
        log.info("Récupération de toutes les recettes");
        
        List<RecetteDTO> recettes = recetteService.getAllRecettes().stream()
                .map(RecetteMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(recettes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RecetteDTO> getRecetteById(@PathVariable Long id) {
        log.info("Récupération de la recette avec l'ID : {}", id);
        
        var recette = recetteService.getRecetteById(id)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée"));
        
        return ResponseEntity.ok(RecetteMapper.toDTO(recette));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RecetteDTO>> getRecettesByUser(@PathVariable Long userId) {
        log.info("Récupération des recettes de l'utilisateur : {}", userId);
        
        List<RecetteDTO> recettes = recetteService.getRecettesByUser(userId).stream()
                .map(RecetteMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(recettes);
    }

    @GetMapping("/shared")
    public ResponseEntity<List<RecetteDTO>> getSharedRecettes() {
        log.info("Récupération des recettes partagées");
        
        List<RecetteDTO> recettes = recetteService.getSharedRecettes().stream()
                .map(RecetteMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(recettes);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RecetteDTO> updateRecette(
            @PathVariable Long id,
            @Valid @RequestBody RecetteDTO recetteDTO) {
        log.info("Mise à jour de la recette avec l'ID : {}", id);
        
        var updatedRecette = recetteService.updateRecette(id, RecetteMapper.toEntity(recetteDTO));
        return ResponseEntity.ok(RecetteMapper.toDTO(updatedRecette));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteRecette(@PathVariable Long id) {
        log.info("Suppression de la recette avec l'ID : {}", id);
        
        recetteService.deleteRecette(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== GESTION DES INGRÉDIENTS ====================

    @PostMapping("/{recetteId}/ingredients")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RecetteIngredientDTO> addIngredientToRecette(
            @PathVariable Long recetteId,
            @Valid @RequestBody RecetteIngredientDTO ingredientDTO) {
        log.info("Ajout d'un ingrédient à la recette : {}", recetteId);
        
        recetteService.addIngredientToRecette(
            recetteId,
            RecetteIngredientMapper.toEntity(ingredientDTO)
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientDTO);
    }

    @GetMapping("/{recetteId}/ingredients")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RecetteIngredientDTO>> getRecetteIngredients(@PathVariable Long recetteId) {
        log.info("Récupération des ingrédients de la recette : {}", recetteId);
        
        List<RecetteIngredientDTO> ingredients = recetteService.getRecetteIngredients(recetteId).stream()
                .map(RecetteIngredientMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ingredients);
    }

    @DeleteMapping("/{recetteId}/ingredients/{ingredientId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeIngredientFromRecette(
            @PathVariable Long recetteId,
            @PathVariable Long ingredientId) {
        log.info("Suppression de l'ingrédient {} de la recette {}", ingredientId, recetteId);
        
        recetteService.removeIngredientFromRecette(recetteId);
        return ResponseEntity.noContent().build();
    }

    // ==================== GESTION DES FAVORIS ====================

    @PostMapping("/{recetteId}/favorites/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addFavorite(
            @PathVariable Long recetteId,
            @PathVariable Long userId) {
        log.info("Ajout de la recette {} aux favoris de l'utilisateur {}", recetteId, userId);
        
        recetteService.addFavorite(userId, recetteId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{recetteId}/favorites/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable Long recetteId,
            @PathVariable Long userId) {
        log.info("Suppression de la recette {} des favoris de l'utilisateur {}", recetteId, userId);
        
        recetteService.removeFavorite(userId, recetteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/favorites")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RecetteDTO>> getUserFavorites(@PathVariable Long userId) {
        log.info("Récupération des favoris de l'utilisateur : {}", userId);
        
        List<RecetteDTO> favorites = recetteService.getUserFavorites(userId).stream()
                .map(RecetteMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(favorites);
    }
}
