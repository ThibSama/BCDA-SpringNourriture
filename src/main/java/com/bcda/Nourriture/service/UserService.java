package com.bcda.Nourriture.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcda.Nourriture.entity.User;
import com.bcda.Nourriture.exception.UserAlreadyExistsException;
import com.bcda.Nourriture.exception.UserNotFoundException;
import com.bcda.Nourriture.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        if (userRepository.existsByMail(user.getMail())) {
            throw new UserAlreadyExistsException("L'email " + user.getMail() + " est déjà utilisé");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("Nouvel utilisateur créé : {}", savedUser.getMail());
        return savedUser;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByMail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID : " + id));
        
        if (userDetails.getNom() != null && !userDetails.getNom().isEmpty()) {
            user.setNom(userDetails.getNom());
        }
        if (userDetails.getPrenom() != null && !userDetails.getPrenom().isEmpty()) {
            user.setPrenom(userDetails.getPrenom());
        }
        if (userDetails.getTelephone() != null && !userDetails.getTelephone().isEmpty()) {
            user.setTelephone(userDetails.getTelephone());
        }
        
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        User updatedUser = userRepository.save(user);
        log.info("Utilisateur mis à jour : {}", updatedUser.getMail());
        return updatedUser;
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Utilisateur non trouvé avec l'ID : " + id);
        }
        userRepository.deleteById(id);
        log.info("Utilisateur supprimé avec l'ID : {}", id);
    }

    public User addRecipeToFavorites(Long userId, Long recetteId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));
        return user;
    }
}
