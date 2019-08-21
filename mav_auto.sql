-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 24, 2019 at 02:26 PM
-- Server version: 10.1.40-MariaDB
-- PHP Version: 7.3.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mav_auto`
--

-- --------------------------------------------------------

--
-- Table structure for table `mav_tc_execute`
--

CREATE TABLE `mav_tc_execute` (
  `row_id` int(15) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(15) DEFAULT NULL,
  `test_case_id` varchar(15) DEFAULT NULL,
  `execution_status` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mav_tc_execute`
--

INSERT INTO `mav_tc_execute` (`row_id`, `created`, `created_by`, `test_case_id`, `execution_status`) VALUES
(5, '2019-06-21 02:50:11', 'sathish', '103', 'In Progress'),
(6, '2019-06-23 11:46:17', 'sathish', '45', 'In Progress'),
(7, '2019-06-23 11:46:17', 'sathish', '46', 'In Progress'),
(8, '2019-06-23 11:46:17', 'sathish', '47', 'In Progress'),
(9, '2019-06-23 11:46:17', 'sathish', '48', 'In Progress'),
(10, '2019-06-23 11:46:17', 'sathish', '65', 'In Progress'),
(11, '2019-06-23 11:47:38', 'sathish', '46', 'In Progress'),
(12, '2019-06-23 11:48:58', 'sathish', '48', 'In Progress'),
(13, '2019-06-23 11:48:58', 'sathish', '65', 'In Progress'),
(14, '2019-06-23 11:49:04', 'sathish', '47', 'In Progress'),
(15, '2019-06-23 11:50:42', 'sathish', '47', 'In Progress'),
(16, '2019-06-23 11:50:42', 'sathish', '48', 'In Progress'),
(17, '2019-06-23 11:50:42', 'sathish', '65', 'In Progress'),
(18, '2019-06-23 11:53:18', 'sathish', '45', 'In Progress'),
(19, '2019-06-23 11:53:18', 'sathish', '46', 'In Progress'),
(20, '2019-06-23 11:53:18', 'sathish', '47', 'In Progress'),
(21, '2019-06-23 11:55:36', 'sathish', '46', 'In Progress'),
(22, '2019-06-23 11:55:36', 'sathish', '47', 'In Progress');

-- --------------------------------------------------------

--
-- Table structure for table `mav_test_case`
--

CREATE TABLE `mav_test_case` (
  `row_id` int(15) NOT NULL,
  `Test_Case_ID` varchar(15) DEFAULT NULL,
  `Test_Scenario` varchar(256) DEFAULT NULL,
  `Test_Case` varchar(256) DEFAULT NULL,
  `Test_Case_Desc` longtext,
  `MSISDN` varchar(15) DEFAULT NULL,
  `Product_Name` varchar(100) DEFAULT NULL,
  `Recharge_Coupon` varchar(15) DEFAULT NULL,
  `Call_TO_MSISDN` varchar(15) DEFAULT NULL,
  `CALL_DURATION` int(11) DEFAULT NULL,
  `RECEIVER_MSISDN` varchar(15) DEFAULT NULL,
  `Message_To_Send` varchar(200) DEFAULT NULL,
  `SMS_COUNT` int(11) DEFAULT NULL,
  `TRANSFER_AMOUNT` varchar(20) DEFAULT NULL,
  `TRANSFER_TO_MSISDN` varchar(15) DEFAULT NULL,
  `Test_Device` varchar(25) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mav_test_case`
--

INSERT INTO `mav_test_case` (`row_id`, `Test_Case_ID`, `Test_Scenario`, `Test_Case`, `Test_Case_Desc`, `MSISDN`, `Product_Name`, `Recharge_Coupon`, `Call_TO_MSISDN`, `CALL_DURATION`, `RECEIVER_MSISDN`, `Message_To_Send`, `SMS_COUNT`, `TRANSFER_AMOUNT`, `TRANSFER_TO_MSISDN`, `Test_Device`, `created`, `created_by`) VALUES
(40, '121', 'OPT OUT', 'Long Code', 'sa', '', '', '', '', 0, '', '', 0, '', '', NULL, '2019-06-12 13:00:59', ''),
(41, 'TC001', 'OPT OUT', 'Long Code', 'for testing', '9999999', 'Local PPU', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-13 11:19:08', ''),
(45, 'TC0001', 'LIVE USAGE VOICE', 'Voice_Roaming - International_DU', 'adkak', '12121121', NULL, NULL, '122121', 2, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-13 13:22:45', 'sathish'),
(46, 'TC003', 'OPT IN', 'Long Code', 'for testing', '7878989', 'Kabayan DataPack LifeStyle  Recurrent daily pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-13 13:24:07', 'sathish'),
(47, 'TC003', 'RECHARGE', 'Recharge_More_International', 'hsjdaddjkk', '78788778', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-13 13:30:40', 'sathish'),
(48, 'Test1121', 'LIVE USAGE VOICE', 'Voice_Roaming - International_DU', '', '89999', NULL, NULL, '999999', 888, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-13 13:31:01', 'sathish'),
(58, '99', 'OPT OUT', 'Long Code', 'sasa', '90909', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-14 09:26:54', 'ss'),
(59, '99', 'OPT OUT', 'Long Code', 'sasa', '90909', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-14 09:28:01', 'ss'),
(60, '99', 'OPT OUT', 'Long Code', 'sasa', '90909', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-14 09:28:05', 'ss'),
(61, '909', 'OPT OUT', 'Long Code', 'sas', '90909', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-14 09:28:16', 'ss'),
(62, '909', 'OPT OUT', 'Long Code', 'sas', '999999', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-14 09:29:31', 'ss'),
(63, '9090', 'OPT OUT', 'Long Code', 'sasa', 'sassa', 'Call Home for Less', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-14 09:29:48', 'ss'),
(65, 'TS_009', 'LIVE USAGE DATA', 'DATA_REGULAR', '', '9611976987', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 04:14:46', 'sathish'),
(68, '90', 'OPT IN', 'Long Code', NULL, '9611976987', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 05:33:57', 'sathish'),
(69, '90', 'OPT IN', 'Long Code', NULL, '9611976987', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 05:34:42', 'sathish'),
(70, '90', 'OPT IN', 'Long Code', NULL, '90', 'Call Home for Less', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 05:34:56', 'sathish'),
(71, '900', 'P2P TRANSFER', NULL, NULL, '9611976987', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '9099090', '90909', NULL, '2019-06-20 05:42:52', 'sathish'),
(73, '909', 'OPT IN', 'Short Code', NULL, '9611976987', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:00:21', 'sathish'),
(74, '909', 'OPT IN', 'Short Code', NULL, '9090909', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:04:25', 'sathish'),
(75, '909', 'OPT OUT', 'Short Code', NULL, '9099090', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:05:09', 'sathish'),
(76, '9099090090', 'OPT IN', 'Long Code', NULL, '9611976987', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:06:09', 'sathish'),
(77, '90909', 'RECHARGE', 'Recharge_More_International', NULL, '99999090900', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:07:33', 'sathish'),
(78, '909', 'OPT OUT', 'Short Code', NULL, '909009', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:10:52', 'sathish'),
(79, '9090', 'LIVE USAGE DATA', 'DATA_SOCIAL', NULL, '90000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:11:58', 'sathish'),
(80, '9090', 'RECHARGE', 'Recharge_More_International', NULL, '90009', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:12:12', 'sathish'),
(81, '909', 'OPT OUT', 'Long Code', NULL, '909009', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:14:00', 'sathish'),
(82, '9090', 'OPT OUT', 'Short Code', NULL, '909009', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:14:49', 'sathish'),
(83, '90990', 'LIVE USAGE DATA', 'DATA_SOCIAL', NULL, '9909909', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:15:08', 'sathish'),
(84, '90090', 'LIVE USAGE DATA', 'DATA_REGULAR', NULL, '909009090', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:16:00', 'sathish'),
(85, '9099', 'OPT OUT', 'Long Code', NULL, '9611976987', 'Daily Saver Bundle', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:20:15', 'sathish'),
(86, '9009', 'OPT OUT', 'Long Code', NULL, '90000', 'Local PPU', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:20:50', 'sathish'),
(87, '090909', 'OPT IN', 'Short Code', NULL, '9611976987', 'Local PPU', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:21:20', 'sathish'),
(88, '909', 'OPT IN', 'Long Code', NULL, '9611976987', 'Local PPU', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:22:07', 'sathish'),
(89, '90909', 'OPT IN', 'Long Code', NULL, '9611976987', 'Daily Saver Bundle', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:23:54', 'sathish'),
(90, 'TC_008', 'OPT OUT', 'Long Code', NULL, '9611976987', 'Daily Saver Bundle', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:24:21', 'sathish'),
(91, 'TC-00', 'OPT IN', 'Long Code', NULL, '9611976987', 'Local PPU', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 06:24:50', 'sathish'),
(92, '909', 'OPT OUT', 'Short Code', NULL, '90900', 'Roaming PPU ,A B GCC', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 07:44:44', 'sathish'),
(93, 'TC_009', 'OPT IN', 'Long Code', NULL, '90900900', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 08:04:02', 'sathish'),
(94, '99898', 'OPT IN', 'Long Code', NULL, '9090', '2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 08:05:22', 'sathish'),
(95, 'TC_002', 'RECHARGE', 'Recharge_More_Data', NULL, 'opo', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 09:45:46', 'sathish'),
(96, 'TC_009', 'OPT OUT', 'Short Code', NULL, '', 'RoamBndlBSub Monthly', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 09:46:41', 'sathish'),
(97, 'TC_00099', 'OPT OUT', 'Long Code', NULL, '9611976987', 'ZNGR_OneOffDaily1_AED1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 09:48:01', 'sathish'),
(99, '977', 'OPT OUT', 'Short Code', NULL, '99', 'Local PPU', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-06-20 09:52:04', 'sathish'),
(100, 'Test', 'OPT IN', 'Short Code', NULL, '8989', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Device1', '2019-06-20 10:05:16', 'sathish'),
(101, '788787', 'OPT OUT', 'Long Code', NULL, '9009090', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Device2', '2019-06-20 10:09:28', 'sathish'),
(102, 'TestCase888', 'LIVE USAGE SMS', 'SMS_DU_TO_DU', NULL, 'sasa', NULL, NULL, NULL, NULL, '5678', 'sa', 9, NULL, NULL, 'Device2', '2019-06-20 10:29:00', 'sathish'),
(103, '677', 'LIVE USAGE VOICE', 'Voice_Roaming - International_DU', NULL, '99009', NULL, NULL, '1212121', 121, NULL, NULL, NULL, NULL, NULL, 'Device2', '2019-06-20 10:30:30', 'sathish'),
(104, '909', 'OPT IN', 'Short Code', NULL, '9090', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Device1', '2019-06-20 11:57:35', 'sathish'),
(105, '9090', 'OPT OUT', 'Short Code', NULL, '', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'SelectScenario', '2019-06-20 12:38:13', 'sathish'),
(108, '9090', 'SelectScenario', 'Select Test Case..', NULL, '90909009', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Device1', '2019-06-20 13:10:52', 'sathish'),
(109, '909', 'OPT OUT', 'Short Code', NULL, '9090900', 'Local PPU', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Device2', '2019-06-21 02:52:55', 'sathish'),
(110, '90909', 'OPT IN', 'Long Code', NULL, '090900909', 'Roaming PPU ,A B GCC', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Device1', '2019-06-21 02:54:32', 'sathish'),
(111, '9009', 'OPT IN', 'Short Code', NULL, '909009', 'Local PPU', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Device1', '2019-06-21 03:07:31', 'sathish'),
(112, 'TC_909099', 'OPT IN', 'Short Code', NULL, '9611976987', 'ZNGR_OneOffDaily2_AED2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Device1', '2019-06-21 07:31:48', 'sathish'),
(113, '9009', 'OPT IN', 'Short Code', NULL, '900900909', 'International Booster Pack', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Device1', '2019-06-21 07:54:54', 'sathish'),
(114, 'TC7888', 'LIVE USAGE DATA', 'DATA_REGULAR', NULL, '8989989', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Device2', '2019-06-21 07:56:36', 'sathish'),
(121, '', 'RECHARGE', 'Recharge_More_International', NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'SelectScenario', '2019-06-21 08:17:16', 'sathish'),
(122, '', 'OPT OUT', 'Short Code', NULL, '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Device1', '2019-06-21 09:22:15', 'sathish'),
(136, '90000090909_SA', 'RECHARGE', 'Recharge_More_International', NULL, '909009', '', '9090909_RECHA', '', 0, '', '', 0, '', '', 'Device1', '2019-06-21 11:39:14', 'sathish'),
(137, '909090', 'LIVE USAGE DATA', 'DATA_SOCIAL', NULL, '90909', '', '', '', 0, '', '', 0, '', '', 'Device2', '2019-06-21 11:43:53', 'sathish'),
(138, '9090', 'OPT IN', 'Short Code', NULL, '90009090', 'Data Credit Protection', '', '', 0, '', '', 0, '', '', 'Device1', '2019-06-21 11:52:18', 'sathish'),
(139, '8899', 'RECHARGE', 'Recharge_More_International', NULL, '9611976987', '', '900099', '', 0, '', '', 0, '', '', 'Device1', '2019-06-21 11:53:24', 'sathish'),
(140, '889990', 'LIVE USAGE VOICE', 'Voice_Regular - International_Other', NULL, '900090', '', '', '9090909', 9090000, '', '', 0, '', '', 'Device1', '2019-06-21 11:53:44', 'sathish'),
(141, '889990', 'LIVE USAGE VOICE', 'Voice_Regular - International_Other', NULL, '900090', '', '', '9090909', 2147483647, '', '', 0, '', '', 'Device1', '2019-06-21 11:53:51', 'sathish'),
(142, '90900', 'LIVE USAGE DATA', 'DATA_REGULAR', NULL, '900000', '', '', '', 0, '', '', 0, '', '', 'Device1', '2019-06-21 11:56:54', 'sathish'),
(143, '6666', 'LIVE USAGE VOICE', 'Voice_Regular - Etisalat', NULL, '9000', '', '', '90090', 9, '', '', 0, '', '', 'Device1', '2019-06-21 11:57:17', 'sathish'),
(145, 'TC6767767676', 'LIVE USAGE VOICE', 'Voice_Regular - International_Other', NULL, '9090909', '', '', '90090909', 909090909, '', '', 0, '', '', 'Device1', '2019-06-21 13:12:20', 'sathish'),
(146, 'TC_007', 'OPT OUT', 'Short Code', NULL, '', 'International Booster Pack', '', '', 0, '', '', 0, '', '', 'Device1', '2019-06-21 13:16:32', 'sathish'),
(147, '90909', 'OPT IN', 'Short Code', NULL, '', '', '', '', 0, '', '', 0, '', '', 'SelectScenario', '2019-06-23 10:46:19', 'sathish'),
(158, 'TC_1111', 'RECHARGE', 'Recharge_More_International', NULL, '111111', '', '11111', '', 0, '', '', 0, '', '', 'Device1', '2019-06-23 14:14:27', 'sathish'),
(159, 'TC_1114', 'OPT IN', 'Short Code', NULL, '9611976987', 'International Booster Pack', '', '', 0, '', '', 0, '', '', 'Device1', '2019-06-23 14:14:27', 'sathish'),
(160, 'TC_1114', 'LIVE USAGE VOICE', 'Voice_Regular - International_Other', NULL, '90909009', '', '', '99999999', 90, '', '', 0, '', '', 'Device1', '2019-06-23 14:14:27', 'sathish'),
(161, '002', 'RECHARGE', 'Recharge_More_International', NULL, '', '', '', '', 0, '', '', 0, '', '', 'Device1', '2019-06-23 14:15:45', 'sathish'),
(162, '002', 'LIVE USAGE VOICE', 'Voice_Regular - International_DU', NULL, '90909', '', '', '90900909', 999, '', '', 0, '', '', 'Device2', '2019-06-23 14:15:45', 'sathish'),
(163, '002', 'OPT OUT', 'Short Code', NULL, '', 'International Booster Pack', '', '', 0, '', '', 0, '', '', 'Device1', '2019-06-23 14:15:45', 'sathish'),
(164, '5577', 'LIVE USAGE DATA', 'DATA_REGULAR', NULL, '900999999', '', '', '', 0, '', '', 0, '', '', 'Device1', '2019-06-24 08:55:39', 'sathish'),
(165, '55771', 'LIVE USAGE VOICE', 'Du-CUG calls: VOICE_DU_CUG', NULL, '9990000000', '', '', '00000', 99, '', '', 0, '', '', 'Device1', '2019-06-24 08:55:39', 'sathish');

-- --------------------------------------------------------

--
-- Table structure for table `mav_user`
--

CREATE TABLE `mav_user` (
  `username` varchar(100) NOT NULL,
  `password` varchar(256) NOT NULL,
  `email` varchar(300) NOT NULL,
  `firstname` varchar(300) NOT NULL,
  `lastname` varchar(300) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mav_user`
--

INSERT INTO `mav_user` (`username`, `password`, `email`, `firstname`, `lastname`, `created`) VALUES
('sathish', 'admin@123', 'sathish@testemail.com', 'sathish', 'panthagani', '2019-06-03 05:45:51'),
('ss', 'ss', 'ssss', 'sss', 'sss', '2019-06-14 07:48:18');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `mav_tc_execute`
--
ALTER TABLE `mav_tc_execute`
  ADD PRIMARY KEY (`row_id`);

--
-- Indexes for table `mav_test_case`
--
ALTER TABLE `mav_test_case`
  ADD PRIMARY KEY (`row_id`);

--
-- Indexes for table `mav_user`
--
ALTER TABLE `mav_user`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `mav_tc_execute`
--
ALTER TABLE `mav_tc_execute`
  MODIFY `row_id` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `mav_test_case`
--
ALTER TABLE `mav_test_case`
  MODIFY `row_id` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=166;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
