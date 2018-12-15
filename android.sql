-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- 主機: 127.0.0.1
-- 產生時間： 2017-01-13 10:03:01
-- 伺服器版本: 10.1.16-MariaDB
-- PHP 版本： 5.6.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `android`
--

-- --------------------------------------------------------

--
-- 資料表結構 `sample`
--

CREATE TABLE `sample` (
  `latit` text,
  `longt` text,
  `time` timestamp NULL DEFAULT NULL,
  `numb` varchar(50) DEFAULT NULL,
  `customer_id` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `sample`
--

INSERT INTO `sample` (`latit`, `longt`, `time`, `numb`, `customer_id`) VALUES
('24.121287791805038', '120.67763237991225', '2017-01-04 16:06:45', '214', 'oldman');

--
-- 已匯出資料表的索引
--

--
-- 資料表索引 `sample`
--
ALTER TABLE `sample`
  ADD PRIMARY KEY (`customer_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
