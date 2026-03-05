package com.bcda.Nourriture.repository;

import com.bcda.Nourriture.entity.Recette;
import com.bcda.Nourriture.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetteRepository extends JpaRepository<Recette, Long> {
    List<Recette> findByUser(User user);
    List<Recette> findByPartageTrue();
}
