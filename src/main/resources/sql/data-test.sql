-- ============================================
-- Données de test pour Nourriture Application
-- ============================================
-- Note: Les mots de passe sont encodés en BCrypt
-- Mot de passe: "password123" -> $2a$10$slYQmyNdGzin7olVN3p5be3DlH.PKZbv5H8KnzzVgXXbVxzy990qm

DROP DATABASE IF EXISTS nourriture_db;

-- ============================================
-- CRÉATION DE LA BASE DE DONNÉES
-- ============================================
CREATE DATABASE IF NOT EXISTS nourriture_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE nourriture_db;


-- ============================================
-- CRÉATION DES TABLES
-- ============================================

-- Table USERS
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    mail VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telephone VARCHAR(20),
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table INGREDIENTS
CREATE TABLE ingredients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    nombre_calorie INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table RECETTES
CREATE TABLE recettes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom_plat VARCHAR(255) NOT NULL,
    duree_preparation INT,
    duree_cuisson INT,
    nombre_calorie INT,
    partage BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table RECETTE_INGREDIENT (table de jointure)
CREATE TABLE recette_ingredient (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quantite DOUBLE NOT NULL,
    unite VARCHAR(50) NOT NULL,
    recette_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (recette_id) REFERENCES recettes(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE,
    UNIQUE KEY uk_recette_ingredient (recette_id, ingredient_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table USER_RECETTE_FAVORITE (table de jointure)
CREATE TABLE user_recette_favorite (
    user_id BIGINT NOT NULL,
    recette_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (recette_id) REFERENCES recettes(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, recette_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- NOTE: Les tables seront créées par Hibernate
-- Ce script charge uniquement les données
-- ============================================

-- ============================================
-- INSERTION DES UTILISATEURS
-- ============================================
INSERT INTO `users` (nom, prenom, mail, password, telephone, role) VALUES
('Dupont', 'Alice', 'alice.dupont@example.com', '$2a$10$slYQmyNdGzin7olVN3p5be3DlH.PKZbv5H8KnzzVgXXbVxzy990qm', '06 12 34 56 78', 'USER'),
('Martin', 'Robert', 'robert.martin@example.com', '$2a$10$slYQmyNdGzin7olVN3p5be3DlH.PKZbv5H8KnzzVgXXbVxzy990qm', '06 23 45 67 89', 'USER'),
('Bernard', 'Marie', 'marie.bernard@example.com', '$2a$10$slYQmyNdGzin7olVN3p5be3DlH.PKZbv5H8KnzzVgXXbVxzy990qm', '06 34 56 78 90', 'ADMIN'),
('Petit', 'Jean', 'jean.petit@example.com', '$2a$10$slYQmyNdGzin7olVN3p5be3DlH.PKZbv5H8KnzzVgXXbVxzy990qm', '06 45 67 89 01', 'USER');

-- ============================================
-- INSERTION DES INGREDIENTS
-- ============================================
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES
-- FRUITS
('Pomme', 'FRUITS', 52),
('Banane', 'FRUITS', 89),
('Fraise', 'FRUITS', 32),
('Orange', 'FRUITS', 47),
('Citron', 'FRUITS', 29),

-- VEGETABLES
('Carotte', 'VEGETABLES', 41),
('Brocoli', 'VEGETABLES', 34),
('Tomate', 'VEGETABLES', 18),
('Oignon', 'VEGETABLES', 40),
('Poivron rouge', 'VEGETABLES', 31),

-- PROTEINS
('Poulet', 'PROTEINS', 165),
('Beef', 'PROTEINS', 250),
('Œuf', 'PROTEINS', 155),
('Poisson blanc', 'PROTEINS', 82),
('Saumon', 'PROTEINS', 208),

-- DAIRY
('Lait', 'DAIRY', 61),
('Fromage cheddar', 'DAIRY', 402),
('Yaourt nature', 'DAIRY', 59),
('Beurre', 'DAIRY', 717),
('Crème fraîche', 'DAIRY', 340),

-- GRAINS
('Riz blanc', 'GRAINS', 130),
('Pâtes', 'GRAINS', 131),
('Pain complet', 'GRAINS', 247),
('Avoine', 'GRAINS', 389),
('Farine blanche', 'GRAINS', 364),

-- SPICES
('Sel', 'SPICES', 0),
('Poivre noir', 'SPICES', 251),
('Curcuma', 'SPICES', 325),
('Paprika', 'SPICES', 289),
('Ail', 'SPICES', 149),

-- OTHER
('Huile d''olive', 'OTHER', 884),
('Miel', 'OTHER', 304),
('Vinaigre balsamique', 'OTHER', 88);

-- ============================================
-- INSERTION DES RECETTES
-- ============================================
INSERT INTO `recettes` (nom_plat, duree_preparation, duree_cuisson, nombre_calorie, partage, user_id) VALUES
-- Recettes d'Alice (1)
('Salade César', 15, 0, 350, true, 1),
('Pâtes Carbonara', 10, 20, 550, true, 1),
('Poulet rôti aux herbes', 20, 45, 450, true, 1),

-- Recettes de Robert (2)
('Riz aux légumes', 15, 25, 320, false, 2),
('Fish and Chips', 30, 40, 680, true, 2),

-- Recettes de Marie (3)
('Tarte aux pommes', 30, 50, 420, true, 3),
('Soupe de légumes', 20, 30, 180, true, 3),

-- Recettes de Jean (4)
('Omelette simple', 5, 10, 280, false, 4),
('Sandwich au fromage', 5, 0, 380, false, 4);

-- ============================================
-- INSERTION DES RECETTE_INGREDIENT
-- ============================================
-- Salade César (1)
INSERT INTO recette_ingredient (quantite, unite, recette_id, ingredient_id) VALUES
(200, 'g', 1, 6),      -- Carotte
(150, 'g', 1, 8),      -- Tomate
(100, 'g', 1, 7),      -- Brocoli
(50, 'g', 1, 17),      -- Fromage cheddar
(2, 'cuillerée', 1, 26); -- Huile d'olive

-- Pâtes Carbonara (2)
INSERT INTO recette_ingredient (quantite, unite, recette_id, ingredient_id) VALUES
(400, 'g', 2, 22),     -- Pâtes
(150, 'g', 2, 13),     -- Œuf
(200, 'g', 2, 11),     -- Poulet
(50, 'g', 2, 17),      -- Fromage cheddar
(3, 'g', 2, 24);       -- Poivre noir

-- Poulet rôti aux herbes (3)
INSERT INTO recette_ingredient (quantite, unite, recette_id, ingredient_id) VALUES
(800, 'g', 3, 11),     -- Poulet
(200, 'g', 3, 6),      -- Carotte
(150, 'g', 3, 9),      -- Oignon
(50, 'ml', 3, 26),     -- Huile d'olive
(5, 'g', 3, 24);       -- Poivre noir

-- Riz aux légumes (4)
INSERT INTO recette_ingredient (quantite, unite, recette_id, ingredient_id) VALUES
(300, 'g', 4, 21),     -- Riz blanc
(150, 'g', 4, 6),      -- Carotte
(100, 'g', 4, 8),      -- Tomate
(100, 'g', 4, 10),     -- Poivron rouge
(2, 'cuillerée', 4, 26); -- Huile d'olive

-- Fish and Chips (5)
INSERT INTO recette_ingredient (quantite, unite, recette_id, ingredient_id) VALUES
(500, 'g', 5, 14),     -- Poisson blanc
(300, 'g', 5, 6),      -- Carotte
(400, 'ml', 5, 26),    -- Huile d'olive (pour frire)
(200, 'g', 5, 25),     -- Farine blanche
(5, 'g', 5, 16);       -- Sel

-- Tarte aux pommes (6)
INSERT INTO recette_ingredient (quantite, unite, recette_id, ingredient_id) VALUES
(600, 'g', 6, 1),      -- Pomme
(200, 'g', 6, 25),     -- Farine blanche
(100, 'g', 6, 19),     -- Beurre
(50, 'g', 6, 27),      -- Miel
(2, 'pincée', 6, 16);  -- Sel

-- Soupe de légumes (7)
INSERT INTO recette_ingredient (quantite, unite, recette_id, ingredient_id) VALUES
(200, 'g', 7, 6),      -- Carotte
(150, 'g', 7, 9),      -- Oignon
(100, 'g', 7, 8),      -- Tomate
(100, 'g', 7, 7),      -- Brocoli
(1, 'litre', 7, 16),   -- Lait
(2, 'cuillerée', 7, 26); -- Huile d'olive

-- Omelette simple (8)
INSERT INTO recette_ingredient (quantite, unite, recette_id, ingredient_id) VALUES
(3, 'unité', 8, 13),   -- Œuf
(20, 'g', 8, 19),      -- Beurre
(2, 'g', 8, 24),       -- Poivre noir
(1, 'pincée', 8, 16);  -- Sel

-- Sandwich au fromage (9)
INSERT INTO recette_ingredient (quantite, unite, recette_id, ingredient_id) VALUES
(2, 'tranche', 9, 23),   -- Pain complet
(50, 'g', 9, 17),        -- Fromage cheddar
(20, 'g', 9, 19),        -- Beurre
(1, 'tranche', 9, 8);    -- Tomate

-- ============================================
-- INSERTION DES FAVORIS (USER_RECETTE_FAVORITE)
-- ============================================
-- Alice ajoute des recettes à ses favoris
INSERT INTO user_recette_favorite (user_id, recette_id) VALUES
(1, 2),  -- Pates Carbonara
(1, 5);  -- Fish and Chips

-- Robert ajoute des recettes à ses favoris
INSERT INTO user_recette_favorite (user_id, recette_id) VALUES
(2, 1),  -- Salade César
(2, 3);  -- Poulet rôti

-- Marie ajoute des recettes à ses favoris
INSERT INTO user_recette_favorite (user_id, recette_id) VALUES
(3, 1),  -- Salade César
(3, 2),  -- Pâtes Carbonara
(3, 6);  -- Tarte aux pommes

-- Jean ajoute des recettes à ses favoris
INSERT INTO user_recette_favorite (user_id, recette_id) VALUES
(4, 1),  -- Salade César
(4, 7);  -- Soupe de légumes

COMMIT;
