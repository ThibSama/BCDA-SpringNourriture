package com.bcda.Nourriture.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcda.Nourriture.entity.User;
import com.bcda.Nourriture.entity.UserRole;
import com.bcda.Nourriture.exception.UserAlreadyExistsException;
import com.bcda.Nourriture.repository.UserRepository;
import com.bcda.Nourriture.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public User register(String nom, String prenom, String email, String password, String telephone) {
        if (userRepository.existsByMail(email)) {
            throw new UserAlreadyExistsException("L'email " + email + " est déjà enregistré");
        }

        User user = User.builder()
                .nom(nom)
                .prenom(prenom)
                .mail(email)
                .password(passwordEncoder.encode(password))
                .telephone(telephone)
                .role(UserRole.USER)
                .build();

        User registeredUser = userRepository.save(user);
        log.info("Nouvel utilisateur enregistré : {}", email);
        return registeredUser;
    }

    public String login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            
            String token = jwtTokenProvider.generateAccessToken(authentication);
            log.info("Utilisateur connecté : {}", email);
            return token;
        } catch (org.springframework.security.core.AuthenticationException e) {
            log.error("Erreur de connexion pour l'utilisateur : {}", email);
            throw new RuntimeException("Identifiants invalides");
        }
    }

    public String refreshToken(String email) {
        userRepository.findByMail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));
        
        String newToken = jwtTokenProvider.generateAccessToken(email);
        log.info("Token rafraîchi pour l'utilisateur : {}", email);
        return newToken;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByMail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));
    }

    public String generateRefreshToken(String email) {
        return jwtTokenProvider.generateRefreshToken(email);
    }
}
