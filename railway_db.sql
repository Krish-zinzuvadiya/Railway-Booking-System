-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 29, 2024 at 02:50 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `railway_db`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteTrain` (IN `in_id` INT(10))   BEGIN

delete from train where train_no=in_id;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getPassenger` ()   BEGIN

select * from passenger;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insertTrain` (IN `in_name` VARCHAR(220), IN `in_source` VARCHAR(320), IN `in_destination` VARCHAR(500), IN `in_dep` TIME(6), IN `in_arr` TIME(6), IN `in_fare` DOUBLE, IN `in_seat` INT(11))   BEGIN

insert into train(train_name, source, destination, departure_time, arrival_time, fare, total_seats) values(in_name, in_source, in_destination, in_dep, in_arr, in_fare, in_seat);

END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `booking_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `train_no` int(11) NOT NULL,
  `booking_date` date NOT NULL,
  `seat_no` varchar(100) DEFAULT NULL,
  `total_fare` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`booking_id`, `user_id`, `train_no`, `booking_date`, `seat_no`, `total_fare`) VALUES
(22, 1, 15, '2024-07-17', '6', 0),
(26, 15, 15, '2024-07-17', '3', 0),
(30, 27, 16, '2024-07-21', '2', 0),
(31, 1, 15, '2024-07-21', '2', 0),
(32, 1, 15, '2024-07-21', '1', 0),
(33, 1, 17, '2024-07-21', '1', 0),
(34, 1, 17, '2024-07-21', '2', 0),
(35, 27, 16, '2024-07-21', '1', 240),
(36, 27, 16, '2024-07-21', '2', 240),
(37, 27, 16, '2024-07-21', '1', 240),
(38, 27, 16, '2024-07-21', '1', 240),
(39, 1, 17, '2024-07-21', '1', 2400),
(40, 1, 17, '2024-07-21', '2', 2400),
(44, 27, 17, '2024-07-21', '1,2', 1200),
(47, 1, 15, '2024-07-24', '3', 1200),
(48, 1, 15, '2024-07-24', '3', 1200),
(49, 27, 16, '2024-07-24', '2', 120),
(56, 1, 15, '2024-07-24', '2', 1200),
(58, 27, 17, '2024-07-24', '1', 1200),
(59, 27, 18, '2024-07-24', '12', 145),
(60, 1, 16, '2024-07-24', '34', 120),
(61, 27, 16, '2024-07-30', '12', 120),
(62, 27, 16, '2024-07-30', '2', 120),
(66, 27, 16, '2024-08-02', '5', 120),
(67, 27, 16, '2024-08-02', '786', 120),
(69, 27, 16, '2024-08-02', '3', 120),
(70, 23, 15, '2024-08-02', '5,7', 2400),
(76, 25, 27, '2333-12-22', '1,2', 4400),
(77, 27, 16, '2024-08-05', '9,10', 240),
(78, 26, 15, '2024-05-23', '9', 1200),
(79, 25, 18, '2023-03-23', '7', 145),
(80, 27, 15, '2024-08-23', '8', 1200),
(81, 23, 15, '2025-01-23', '32', 1200),
(82, 26, 17, '2024-10-21', '65', 1200),
(83, 29, 18, '2025-03-09', '13', 145),
(84, 30, 19, '2025-02-28', '3', 2300);

-- --------------------------------------------------------

--
-- Table structure for table `passenger`
--

CREATE TABLE `passenger` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `age` int(100) NOT NULL,
  `gender` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `passenger`
--

INSERT INTO `passenger` (`id`, `name`, `age`, `gender`) VALUES
(1, 'Rahul', 25, 'M'),
(15, 'KrishRami', 19, 'M'),
(16, '1whs', 23, 'M'),
(17, 'asz', 12, 'M'),
(18, 'nakx', 12, 'M'),
(19, 'idskx', 10, 'F'),
(20, 'Rahul890', 35, 'M'),
(21, 'Rs', 1, 'M'),
(22, 'SmitPanchal', 20, 'M'),
(23, 'KrishRami', 20, 'M'),
(24, 'KunjeshPanara', 23, 'M'),
(25, 'Manthan', 24, 'M'),
(26, 'RishiSheth', 25, 'M'),
(27, 'SmitPandya', 20, 'M'),
(28, 'MitanshuThakkar', 29, 'M'),
(29, 'java-2', 80, 'M'),
(30, 'hola', 99, 'M'),
(31, 'qwe', 12, 'M'),
(32, 'JeetVamja', 20, 'M'),
(33, 'Kaushal', 21, 'M'),
(34, 'YKZ', 23, 'F');

-- --------------------------------------------------------

--
-- Table structure for table `station`
--

CREATE TABLE `station` (
  `train_no` int(100) NOT NULL,
  `station_name` varchar(255) NOT NULL,
  `sequence` int(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `station`
--

INSERT INTO `station` (`train_no`, `station_name`, `sequence`) VALUES
(17, 'Agra', 1),
(17, 'Pune', 2),
(18, 'Patan', 1),
(19, 'rajasthan', 1),
(23, 'surat', 1),
(23, 'vadodra', 2),
(27, 'Mumbai', 1),
(27, 'Delhi', 2),
(28, 'surat', 1),
(29, 'vartej', 1),
(29, 'sihor', 2),
(29, 'botad', 3),
(30, 'Jaipur', 1),
(30, 'Noida', 2);

-- --------------------------------------------------------

--
-- Table structure for table `train`
--

CREATE TABLE `train` (
  `train_no` int(11) NOT NULL,
  `train_name` varchar(50) NOT NULL,
  `source` varchar(50) NOT NULL,
  `destination` varchar(50) NOT NULL,
  `departure_time` time NOT NULL,
  `arrival_time` time NOT NULL,
  `fare` double NOT NULL,
  `total_seats` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `train`
--

INSERT INTO `train` (`train_no`, `train_name`, `source`, `destination`, `departure_time`, `arrival_time`, `fare`, `total_seats`) VALUES
(15, 'VandeBharat', 'Ahm', 'Mumbai', '06:30:00', '11:20:00', 1200, 50),
(16, 'Express', 'Bhav', 'Ahm', '06:00:00', '07:00:00', 120, 50),
(17, 'DelhiExpress', 'delhi', 'mumbai', '06:00:00', '17:00:00', 1200, 50),
(18, 'Bhuj', 'Ahm', 'Bhuj', '11:00:00', '12:00:00', 145, 50),
(19, 'Kashmir', 'Ahmedabad', 'Jammu', '11:00:00', '23:00:00', 2300, 50),
(23, 'VandeBharat-2', 'Mumbau', 'Ahmedabad', '14:00:00', '06:40:00', 2200, 50),
(27, 'KashiExp', 'Kashi', 'Ahmedabad', '11:00:00', '18:00:00', 2200, 50),
(28, 'VandeBharat2', 'Ahmedabad', 'Pune', '11:00:00', '20:00:00', 12350, 50),
(29, 'BhavnagarExp', 'Bhavnagar', 'Ahemdabad', '06:00:00', '10:30:00', 156, 50),
(30, 'MumbaiExp', 'Mumbai', 'Delhi', '12:05:00', '00:10:00', 1560, 50);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `name`, `age`, `gender`, `phone_number`, `email`) VALUES
(3, 'hola', 99, 'M', '9099887766', 'lj@gmail.com'),
(4, 'qwe', 12, 'M', '1234567890', '12@gmail.com'),
(5, 'JeetVamja', 20, 'M', '8977678899', 'Jeet@gmail.com'),
(6, 'Kaushal', 21, 'M', '1234567789', 'Kaushal@gmail.com'),
(7, 'YKZ', 23, 'F', '9988776644', 'YKZ@gmail.com');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`booking_id`),
  ADD KEY `Test` (`user_id`),
  ADD KEY `Test1` (`train_no`);

--
-- Indexes for table `passenger`
--
ALTER TABLE `passenger`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `station`
--
ALTER TABLE `station`
  ADD PRIMARY KEY (`train_no`,`sequence`);

--
-- Indexes for table `train`
--
ALTER TABLE `train`
  ADD PRIMARY KEY (`train_no`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `booking`
--
ALTER TABLE `booking`
  MODIFY `booking_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=85;

--
-- AUTO_INCREMENT for table `passenger`
--
ALTER TABLE `passenger`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `train`
--
ALTER TABLE `train`
  MODIFY `train_no` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `Test` FOREIGN KEY (`user_id`) REFERENCES `passenger` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Test1` FOREIGN KEY (`train_no`) REFERENCES `train` (`train_no`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `station`
--
ALTER TABLE `station`
  ADD CONSTRAINT `test3` FOREIGN KEY (`train_no`) REFERENCES `train` (`train_no`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
