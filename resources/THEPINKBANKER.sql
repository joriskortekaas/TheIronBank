-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 10, 2017 at 03:09 PM
-- Server version: 5.7.17-0ubuntu0.16.04.1
-- PHP Version: 7.0.15-0ubuntu0.16.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `THEPINKBANKER`
--

-- --------------------------------------------------------

--
-- Table structure for table `ACCOUNTS`
--

CREATE TABLE `ACCOUNTS` (
  `bank#` int(11) NOT NULL,
  `pincode` int(11) NOT NULL,
  `balance` double NOT NULL,
  `cardID` int(11) NOT NULL,
  `personID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ATM`
--

CREATE TABLE `ATM` (
  `atmID` int(11) NOT NULL,
  `city` varchar(40) NOT NULL,
  `zipCode` varchar(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PERMISSIONS`
--

CREATE TABLE `PERMISSIONS` (
  `personID` int(11) NOT NULL,
  `permission` char(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PERSONS`
--

CREATE TABLE `PERSONS` (
  `personID` int(11) NOT NULL,
  `firstName` varchar(35) NOT NULL,
  `middleName` varchar(35) NOT NULL,
  `lastName` varchar(35) NOT NULL,
  `ssn` int(11) NOT NULL,
  `birthDate` date NOT NULL,
  `email` char(50) NOT NULL,
  `adres` char(100) NOT NULL,
  `telefoon#` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `TRANSACTIONS`
--

CREATE TABLE `TRANSACTIONS` (
  `transactionID` int(11) NOT NULL,
  `bank#` int(11) NOT NULL,
  `atmID` int(11) NOT NULL,
  `amount` double NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ACCOUNTS`
--
ALTER TABLE `ACCOUNTS`
  ADD PRIMARY KEY (`bank#`),
  ADD UNIQUE KEY `personID` (`personID`);

--
-- Indexes for table `ATM`
--
ALTER TABLE `ATM`
  ADD PRIMARY KEY (`atmID`);

--
-- Indexes for table `PERMISSIONS`
--
ALTER TABLE `PERMISSIONS`
  ADD UNIQUE KEY `personID` (`personID`);

--
-- Indexes for table `PERSONS`
--
ALTER TABLE `PERSONS`
  ADD PRIMARY KEY (`personID`),
  ADD UNIQUE KEY `ssn` (`ssn`);

--
-- Indexes for table `TRANSACTIONS`
--
ALTER TABLE `TRANSACTIONS`
  ADD PRIMARY KEY (`transactionID`),
  ADD UNIQUE KEY `atmID` (`atmID`),
  ADD UNIQUE KEY `bank#` (`bank#`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `PERSONS`
--
ALTER TABLE `PERSONS`
  MODIFY `personID` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `ACCOUNTS`
--
ALTER TABLE `ACCOUNTS`
  ADD CONSTRAINT `ACCOUNTS_ibfk_1` FOREIGN KEY (`personID`) REFERENCES `PERSONS` (`personID`);

--
-- Constraints for table `ATM`
--
ALTER TABLE `ATM`
  ADD CONSTRAINT `ATM_ibfk_1` FOREIGN KEY (`atmID`) REFERENCES `TRANSACTIONS` (`atmID`);

--
-- Constraints for table `PERMISSIONS`
--
ALTER TABLE `PERMISSIONS`
  ADD CONSTRAINT `PERMISSIONS_ibfk_1` FOREIGN KEY (`personID`) REFERENCES `PERSONS` (`personID`);

--
-- Constraints for table `TRANSACTIONS`
--
ALTER TABLE `TRANSACTIONS`
  ADD CONSTRAINT `TRANSACTIONS_ibfk_1` FOREIGN KEY (`bank#`) REFERENCES `ACCOUNTS` (`bank#`),
  ADD CONSTRAINT `TRANSACTIONS_ibfk_2` FOREIGN KEY (`atmID`) REFERENCES `ATM` (`atmID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
