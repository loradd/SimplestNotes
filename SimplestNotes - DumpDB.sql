-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: localhost:8889
-- Generation Time: Jul 21, 2014 at 06:07 PM
-- Server version: 5.5.34
-- PHP Version: 5.5.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `simpleNotes`
--
CREATE DATABASE IF NOT EXISTS `simpleNotes` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `simpleNotes`;

-- --------------------------------------------------------

--
-- Table structure for table `note_dynamic`
--

CREATE TABLE `note_dynamic` (
  `note_id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `title` varchar(45) NOT NULL,
  `content` varchar(300) NOT NULL,
  PRIMARY KEY (`version`,`note_id`),
  KEY `note_id` (`note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `note_static`
--

CREATE TABLE `note_static` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=173 ;

-- --------------------------------------------------------

--
-- Table structure for table `note_tag`
--

CREATE TABLE `note_tag` (
  `tag_id` int(11) NOT NULL,
  `note_id` int(11) NOT NULL,
  `note_version` int(11) NOT NULL,
  PRIMARY KEY (`tag_id`,`note_id`,`note_version`),
  KEY `note_id` (`note_id`,`note_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tag`
--

CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email_address` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=114 ;

-- --------------------------------------------------------

--
-- Table structure for table `user_note`
--

CREATE TABLE `user_note` (
  `user_id` int(11) NOT NULL,
  `note_id` int(11) NOT NULL,
  `permission` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`note_id`),
  KEY `note_id` (`note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `note_dynamic`
--
ALTER TABLE `note_dynamic`
  ADD CONSTRAINT `note_dynamic_ibfk_1` FOREIGN KEY (`note_id`) REFERENCES `note_static` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `note_tag`
--
ALTER TABLE `note_tag`
  ADD CONSTRAINT `note_tag_ibfk_1` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `note_tag_ibfk_2` FOREIGN KEY (`note_id`, `note_version`) REFERENCES `note_dynamic` (`note_id`, `version`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `user_note`
--
ALTER TABLE `user_note`
  ADD CONSTRAINT `note_id` FOREIGN KEY (`note_id`) REFERENCES `note_static` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
