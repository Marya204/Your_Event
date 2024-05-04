-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : sam. 04 mai 2024 à 15:00
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

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

CREATE TABLE `billet` (
  `Id` int(11) NOT NULL,
  `Eventid` int(11) DEFAULT NULL,
  `Inviteid` int(11) DEFAULT NULL,
  `Price` float DEFAULT NULL,
  `Status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `billet`
--

INSERT INTO `billet` (`Id`, `Eventid`, `Inviteid`, `Price`, `Status`) VALUES
(1, 1, 1, 150, 'Paid'),
(2, 1, 2, 150, 'Paid'),
(3, 2, 3, 200, 'Unpaid'),
(4, 2, 4, 200, 'Paid'),
(5, 3, 5, 250, 'Paid'),
(6, 3, 6, 250, 'Cancelled'),
(7, 4, 7, 100, 'Paid'),
(8, 4, 8, 100, 'Unpaid');

-- --------------------------------------------------------

--
-- Structure de la table `event`
--

CREATE TABLE `event` (
  `Eventid` int(11) NOT NULL,
  `Titre` varchar(255) DEFAULT NULL,
  `Description` text DEFAULT NULL,
  `Date` date DEFAULT NULL,
  `Lieu` varchar(255) DEFAULT NULL,
  `Type` varchar(255) DEFAULT NULL,
  `Status` varchar(255) DEFAULT NULL,
  `Prix` float DEFAULT NULL,
  `Capacite` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `event`
--

INSERT INTO `event` (`Eventid`, `Titre`, `Description`, `Date`, `Lieu`, `Type`, `Status`, `Prix`, `Capacite`) VALUES
(2, 'Exposition d art contemporain', 'Une exposition présentant des œuvres d art contemporain marocaines.', '2024-07-20', 'Musée d Art Moderne et Contemporain, Rabat', 'Exposition', 'À venir', 50, 200),
(3, 'Festival de cinéma', 'Un festival de cinéma mettant en avant des films marocains et internationaux.', '2024-07-10', 'Théâtre National Mohammed V, Casablanca', 'Festival', 'À venir', 75, 300),
(4, 'Semaine de la mode', 'Une semaine dédiée à la mode marocaine avec des défilés, des ateliers et des conférences.', '2024-08-25', 'Village des Arts, Casablanca', 'Mode', 'À venir', 80, 400),
(5, 'Spectacle de danse traditionnelle', 'Un spectacle mettant en scène des danses traditionnelles marocaines.', '2024-09-12', 'Théâtre Royal, Marrakech', 'Spectacle', 'À venir', 60, 250),
(6, 'Salon du livre', 'Un événement littéraire réunissant des écrivains marocains et internationaux.', '2024-10-05', 'Centre International des Conférences, Tanger', 'Salon', 'À venir', 70, 300);

-- --------------------------------------------------------

--
-- Structure de la table `invite`
--

CREATE TABLE `invite` (
  `Inviteid` int(11) NOT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `Eventid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `invite`
--

INSERT INTO `invite` (`Inviteid`, `Name`, `Email`, `Eventid`) VALUES
(1, 'Fatima Zahra', 'fatima@example.com', 2),
(2, 'Youssef', 'youssef@example.com', 1),
(3, 'Leila', 'leila@example.com', 2),
(4, 'Ahmed', 'ahmed@example.com', 3),
(5, 'Amina', 'amina@example.com', 3);

-- --------------------------------------------------------

--
-- Structure de la table `participant`
--

CREATE TABLE `participant` (
  `ParticipantID` int(11) NOT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `Eventid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `participant`
--

INSERT INTO `participant` (`ParticipantID`, `Name`, `Email`, `Eventid`) VALUES
(1, 'Karim Benzema', 'karim@example.com', 1),
(2, 'Nadia Tazi', 'nadia@example.com', 2),
(3, 'Mehdi M', 'mehdi@example.com', 3),
(4, 'Sara Bien', 'sara@example.com', 4),
(5, 'Yassine Loco', 'yassine@example.com', 4);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `Id` int(11) NOT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`Id`, `Email`, `Password`) VALUES
(1, 'marysakouti@gmail.com', '1234'),
(2, 'lahrourinadia02@gmail.com', '5678\r\n'),
(3, 'misbahasmae123@gmail.com', '0987');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `billet`
--
ALTER TABLE `billet`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `Eventid` (`Eventid`),
  ADD KEY `fk_invite` (`Inviteid`);

--
-- Index pour la table `event`
--
ALTER TABLE `event`
  ADD PRIMARY KEY (`Eventid`);

--
-- Index pour la table `invite`
--
ALTER TABLE `invite`
  ADD PRIMARY KEY (`Inviteid`),
  ADD KEY `fk_event` (`Eventid`);

--
-- Index pour la table `participant`
--
ALTER TABLE `participant`
  ADD PRIMARY KEY (`ParticipantID`),
  ADD KEY `fk_event` (`Eventid`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `billet`
--
ALTER TABLE `billet`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `event`
--
ALTER TABLE `event`
  MODIFY `Eventid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `invite`
--
ALTER TABLE `invite`
  MODIFY `Inviteid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `participant`
--
ALTER TABLE `participant`
  MODIFY `ParticipantID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
