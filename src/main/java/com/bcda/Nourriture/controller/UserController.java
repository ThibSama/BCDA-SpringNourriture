package com.bcda.Nourriture.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcda.Nourriture.dto.UserDTO;
import com.bcda.Nourriture.exception.UserNotFoundException;
import com.bcda.Nourriture.mapper.UserMapper;
import com.bcda.Nourriture.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Récupération de tous les utilisateurs");
        
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("Récupération de l'utilisateur avec l'ID : {}", id);
        
        var user = userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));
        
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        log.info("Récupération de l'utilisateur avec l'email : {}", email);
        
        var user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));
        
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {
        log.info("Mise à jour de l'utilisateur avec l'ID : {}", id);
        
        var updatedUser = userService.updateUser(id, UserMapper.toEntity(userDTO));
        return ResponseEntity.ok(UserMapper.toDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Suppression de l'utilisateur avec l'ID : {}", id);
        
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
