-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : jeu. 02 mai 2024 à 18:55
-- Version du serveur : 8.2.0
-- Version de PHP : 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `events`
--

-- --------------------------------------------------------

--
-- Structure de la table `billet`
--

DROP TABLE IF EXISTS `billet`;
CREATE TABLE IF NOT EXISTS `billet` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `Eventid` int DEFAULT NULL,
  `Inviteid` int DEFAULT NULL,
  `Price` float DEFAULT NULL,
  `Status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `Eventid` (`Eventid`),
  KEY `fk_invite` (`Inviteid`)
) ENGINE=MyISAM AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `billet`
--

INSERT INTO `billet` (`Id`, `Eventid`, `Inviteid`, `Prix`, `Status`) VALUES
(50, 12, 13, 150, 'available');

-- --------------------------------------------------------

--
-- Structure de la table `event`
--

DROP TABLE IF EXISTS `event`;
CREATE TABLE IF NOT EXISTS `event` (
  `Eventid` int NOT NULL AUTO_INCREMENT,
  `Titre` varchar(255) DEFAULT NULL,
  `Description` text,
  `Date` date DEFAULT NULL,
  `Lieu` varchar(255) DEFAULT NULL,
  `Type` varchar(255) DEFAULT NULL,
  `Status` varchar(255) DEFAULT NULL,
  `Prix` float DEFAULT NULL,
  `Capacite` int DEFAULT NULL,
  PRIMARY KEY (`Eventid`)
) ENGINE=MyISAM AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `event`
--

INSERT INTO `event` (`Eventid`, `Titre`, `Description`, `Date`, `Lieu`, `Type`, `Status`, `Prix`, `Capacite`) VALUES
(6, 'The Mirage Museum', ' holographic artworks', '2024-04-20', 'Marrakech,Mamounia hotel', 'Art', 'Available', 300, 90),
(3, 'Asilah Arts Festival', 'national and international artists are invited to create mural artworks, alongside exhibitions, performances, and workshops.', '2024-02-20', 'Asilah', 'Art', 'Not available', 200, 100),
(1, 'MAWAZINE', ' A music festival ', '2024-06-20', 'Rabat', 'Music ', 'Available', 300, 1000),
(2, 'Marrakech International Film Festival', 'An annual film festival', '2024-08-02', 'Marrakech', 'Theatre', 'Available', 800, 3000);

-- --------------------------------------------------------

--
-- Structure de la table `invite`
--

DROP TABLE IF EXISTS `invite`;
CREATE TABLE IF NOT EXISTS `invite` (
  `Inviteid` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  Eventid` int NOT NULL,
  PRIMARY KEY (`Inviteid`),
  KEY `fk_event` (`Eventid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
--
-- Déchargement des données de la table `invite`
--

INSERT INTO `invite` (`Inviteid`, `Name`, `Email`, `Eventid`) VALUES
(11, 'nadia', 'nadialah@gmail.com', 2),
(12, 'nada', 'nadalh@gmail.com', 3);

--
-- Structure de la table `participant`
--

DROP TABLE IF EXISTS `participant`;
CREATE TABLE IF NOT EXISTS `participant` (
  `ParticipantID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `Eventid` int NOT NULL,
  PRIMARY KEY (`ParticipantID`),
  KEY `fk_event` (`Eventid`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `participant`
--

INSERT INTO `participant` (`ParticipantID`, `Name`, `Email`, `Eventid`) VALUES
(13, 'asmaa', 'asmaeki@gmail.com', 13),
(12, 'asma', 'asma@gmail.com', 5);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `Email` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
