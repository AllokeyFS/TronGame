CREATE DATABASE IF NOT EXISTS tron_game;

USE tron_game;

CREATE TABLE tron_game (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    red INT NOT NULL,
    green INT NOT NULL,
    blue INT NOT NULL,
    score INT DEFAULT 0
);
-- DESCRIBE tron_game;

-- DROP TABLE tron_game.tron_game;

-- DROP DATABASE tron_game;
