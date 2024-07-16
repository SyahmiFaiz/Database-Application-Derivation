-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 28, 2024 at 11:08 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bike_rental`
--

-- --------------------------------------------------------

--
-- Table structure for table `bike`
--

CREATE TABLE `bike` (
  `BikeID` int(11) NOT NULL,
  `BikeModel` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bike`
--

INSERT INTO `bike` (`BikeID`, `BikeModel`) VALUES
(1, 'Yamahmud'),
(2, 'ModenNas'),
(3, 'Merceday'),
(4, 'Hondo'),
(5, 'Hyundui'),
(6, 'Moonton');

-- --------------------------------------------------------

--
-- Table structure for table `rentallist`
--

CREATE TABLE `rentallist` (
  `RentalID` int(10) NOT NULL,
  `BikeID` int(10) NOT NULL,
  `RentalStatus` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rentallist`
--

INSERT INTO `rentallist` (`RentalID`, `BikeID`, `RentalStatus`) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 4, 1),
(4, 5, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bike`
--
ALTER TABLE `bike`
  ADD PRIMARY KEY (`BikeID`);

--
-- Indexes for table `rentallist`
--
ALTER TABLE `rentallist`
  ADD PRIMARY KEY (`RentalID`),
  ADD KEY `BikeID` (`BikeID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `rentallist`
--
ALTER TABLE `rentallist`
  MODIFY `RentalID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `rentallist`
--
ALTER TABLE `rentallist`
  ADD CONSTRAINT `rentallist_ibfk_1` FOREIGN KEY (`BikeID`) REFERENCES `bike` (`BikeID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
