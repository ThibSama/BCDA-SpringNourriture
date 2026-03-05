package com.bcda.Nourriture.controller;

import com.bcda.Nourriture.dto.LoginRequest;
import com.bcda.Nourriture.dto.LoginResponse;
import com.bcda.Nourriture.dto.RegisterRequest;
import com.bcda.Nourriture.dto.UserDTO;
import com.bcda.Nourriture.mapper.UserMapper;
import com.bcda.Nourriture.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Tentative d'enregistrement pour : {}", request.getMail());
        
        try {
            var user = authService.register(
                request.getNom(),
                request.getPrenom(),
                request.getMail(),
                request.getPassword(),
                request.getTelephone()
            );
            
            UserDTO userDTO = UserMapper.toDTO(user);
            log.info("Utilisateur enregistré avec succès : {}", request.getMail());
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (RuntimeException e) {
            log.error("Erreur lors de l'enregistrement : {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Tentative de connexion pour : {}", request.getMail());
        
        try {
            String accessToken = authService.login(request.getMail(), request.getPassword());
            String refreshToken = authService.generateRefreshToken(request.getMail());
            
            LoginResponse response = new LoginResponse(accessToken, refreshToken, 86400000L);
            log.info("Utilisateur connecté avec succès : {}", request.getMail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la connexion : {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestParam String email) {
        log.info("Tentative de rafraîchissement du token pour : {}", email);
        
        try {
            String newAccessToken = authService.refreshToken(email);
            String refreshToken = authService.generateRefreshToken(email);
            
            LoginResponse response = new LoginResponse(newAccessToken, refreshToken, 86400000L);
            log.info("Token rafraîchi avec succès pour : {}", email);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Erreur lors du rafraîchissement du token : {}", e.getMessage());
            throw e;
        }
    }
}
