-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 18, 2017 at 03:11 PM
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
-- Table structure for table `ADRES`
--

CREATE TABLE `ADRES` (
  `adresID` int(30) NOT NULL,
  `zipcode` varchar(10) NOT NULL,
  `street` varchar(50) NOT NULL,
  `number` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ATM`
--

CREATE TABLE `ATM` (
  `atmID` int(20) NOT NULL,
  `adresID` int(20) NOT NULL,
  `details` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BANKACCOUNTS`
--

CREATE TABLE `BANKACCOUNTS` (
  `accountNumber` int(20) NOT NULL,
  `clientID` int(20) NOT NULL,
  `pincode` int(10) NOT NULL,
  `balance` double NOT NULL,
  `cardID` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `CLIENTS`
--

CREATE TABLE `CLIENTS` (
  `clientID` int(20) NOT NULL,
  `cardID` int(20) NOT NULL,
  `adresID` int(30) NOT NULL,
  `firstName` varchar(30) NOT NULL,
  `middleName` varchar(30) NOT NULL,
  `lastName` varchar(30) NOT NULL,
  `ssn` int(20) NOT NULL,
  `birthDate` date NOT NULL,
  `email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PROVINCE`
--

CREATE TABLE `PROVINCE` (
  `zipcode` varchar(10) NOT NULL,
  `province` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `TRANSACTIONS`
--

CREATE TABLE `TRANSACTIONS` (
  `transactionID` int(20) NOT NULL,
  `accountNumber` int(20) NOT NULL,
  `atmID` int(20) NOT NULL,
  `ammount` double NOT NULL,
  `transactionDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ADRES`
--
ALTER TABLE `ADRES`
  ADD PRIMARY KEY (`adresID`),
  ADD UNIQUE KEY `zipcode` (`zipcode`);

--
-- Indexes for table `ATM`
--
ALTER TABLE `ATM`
  ADD PRIMARY KEY (`atmID`),
  ADD UNIQUE KEY `adresID` (`adresID`);

--
-- Indexes for table `BANKACCOUNTS`
--
ALTER TABLE `BANKACCOUNTS`
  ADD PRIMARY KEY (`accountNumber`),
  ADD UNIQUE KEY `clientID` (`clientID`);

--
-- Indexes for table `CLIENTS`
--
ALTER TABLE `CLIENTS`
  ADD PRIMARY KEY (`clientID`),
  ADD UNIQUE KEY `ssn` (`ssn`),
  ADD UNIQUE KEY `adresID` (`adresID`),
  ADD UNIQUE KEY `cardID` (`cardID`);

--
-- Indexes for table `PROVINCE`
--
ALTER TABLE `PROVINCE`
  ADD PRIMARY KEY (`zipcode`);

--
-- Indexes for table `TRANSACTIONS`
--
ALTER TABLE `TRANSACTIONS`
  ADD PRIMARY KEY (`transactionID`),
  ADD UNIQUE KEY `accountNumber` (`accountNumber`),
  ADD UNIQUE KEY `atmID` (`atmID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `CLIENTS`
--
ALTER TABLE `CLIENTS`
  MODIFY `clientID` int(20) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `ADRES`
--
ALTER TABLE `ADRES`
  ADD CONSTRAINT `ADRES_ibfk_1` FOREIGN KEY (`zipcode`) REFERENCES `PROVINCE` (`zipcode`);

--
-- Constraints for table `ATM`
--
ALTER TABLE `ATM`
  ADD CONSTRAINT `ATM_ibfk_1` FOREIGN KEY (`adresID`) REFERENCES `ADRES` (`adresID`);

--
-- Constraints for table `BANKACCOUNTS`
--
ALTER TABLE `BANKACCOUNTS`
  ADD CONSTRAINT `BANKACCOUNTS_ibfk_1` FOREIGN KEY (`clientID`) REFERENCES `CLIENTS` (`clientID`);

--
-- Constraints for table `CLIENTS`
--
ALTER TABLE `CLIENTS`
  ADD CONSTRAINT `CLIENTS_ibfk_1` FOREIGN KEY (`adresID`) REFERENCES `ADRES` (`adresID`);

--
-- Constraints for table `TRANSACTIONS`
--
ALTER TABLE `TRANSACTIONS`
  ADD CONSTRAINT `TRANSACTIONS_ibfk_1` FOREIGN KEY (`atmID`) REFERENCES `ATM` (`atmID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
