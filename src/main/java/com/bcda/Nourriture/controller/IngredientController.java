package com.bcda.Nourriture.controller;

import com.bcda.Nourriture.dto.IngredientDTO;
import com.bcda.Nourriture.entity.IngredientType;
import com.bcda.Nourriture.mapper.IngredientMapper;
import com.bcda.Nourriture.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
@Slf4j
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IngredientDTO> createIngredient(@Valid @RequestBody IngredientDTO ingredientDTO) {
        log.info("Création d'un nouvel ingrédient : {}", ingredientDTO.getLibelle());
        
        var ingredient = ingredientService.createIngredient(IngredientMapper.toEntity(ingredientDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(IngredientMapper.toDTO(ingredient));
    }

    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAllIngredients() {
        log.info("Récupération de tous les ingrédients");
        
        List<IngredientDTO> ingredients = ingredientService.getAllIngredients().stream()
                .map(IngredientMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getIngredientById(@PathVariable Long id) {
        log.info("Récupération de l'ingrédient avec l'ID : {}", id);
        
        var ingredient = ingredientService.getIngredientById(id)
                .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé"));
        
        return ResponseEntity.ok(IngredientMapper.toDTO(ingredient));
    }

    @GetMapping("/libelle/{libelle}")
    public ResponseEntity<IngredientDTO> getIngredientByLibelle(@PathVariable String libelle) {
        log.info("Récupération de l'ingrédient avec le libellé : {}", libelle);
        
        var ingredient = ingredientService.getIngredientByLibelle(libelle)
                .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé"));
        
        return ResponseEntity.ok(IngredientMapper.toDTO(ingredient));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<IngredientDTO>> getIngredientsByType(@PathVariable IngredientType type) {
        log.info("Récupération des ingrédients du type : {}", type);
        
        List<IngredientDTO> ingredients = ingredientService.getIngredientsByType(type).stream()
                .map(IngredientMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ingredients);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IngredientDTO> updateIngredient(
            @PathVariable Long id,
            @Valid @RequestBody IngredientDTO ingredientDTO) {
        log.info("Mise à jour de l'ingrédient avec l'ID : {}", id);
        
        var updatedIngredient = ingredientService.updateIngredient(id, IngredientMapper.toEntity(ingredientDTO));
        return ResponseEntity.ok(IngredientMapper.toDTO(updatedIngredient));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        log.info("Suppression de l'ingrédient avec l'ID : {}", id);
        
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
