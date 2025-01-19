-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: zarnicms
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `zarnicms`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `zarnicms` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `zarnicms`;

--
-- Table structure for table `payment_method`
--

DROP TABLE IF EXISTS `payment_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_method` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `discount` decimal(38,2) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_method`
--

LOCK TABLES `payment_method` WRITE;
/*!40000 ALTER TABLE `payment_method` DISABLE KEYS */;
INSERT INTO `payment_method` VALUES (1,10.00,'VISA'),(2,0.00,'Mastercard');
/*!40000 ALTER TABLE `payment_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voucher`
--

DROP TABLE IF EXISTS `voucher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voucher` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `buy_limit` int NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `expiry_date` datetime(6) DEFAULT NULL,
  `gift_limit` int NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voucher`
--

LOCK TABLES `voucher` WRITE;
/*!40000 ALTER TABLE `voucher` DISABLE KEYS */;
INSERT INTO `voucher` VALUES (1,100.00,_binary '\0',5,'This is a sample description for the voucher.','2025-02-01 23:59:59.000000',3,'1 UPDATED VOUCHER FOR YOU'),(2,100.00,_binary '',7,'This is a sample description for the voucher.','2025-02-01 23:59:59.000000',8,'1 UPDATED VOUCHER FOR YOU'),(3,200.00,_binary '',7,'This is a sample description for the voucher.','2025-02-01 23:59:59.000000',8,'3 UPDATED VOUCHER FOR YOU'),(4,1.00,_binary '',1500,'This is a sample description for the voucher.','2025-02-01 23:59:59.000000',1500,'VOUCHER FOR YOU'),(5,1.00,_binary '',1500,'This is a sample description for the voucher.','2025-02-01 23:59:59.000000',1500,'VOUCHER FOR YOU'),(6,1.00,_binary '',15000,'This is a sample description for the voucher.','2025-02-01 23:59:59.000000',15000,'VOUCHER FOR YOU'),(7,1.00,_binary '',15000,'This is a sample description for the voucher.','2025-02-01 23:59:59.000000',15000,'VOUCHER FOR YOU');
/*!40000 ALTER TABLE `voucher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voucher_bought`
--

DROP TABLE IF EXISTS `voucher_bought`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voucher_bought` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `due_amount` decimal(38,2) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `paid` bit(1) NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `quantity` int NOT NULL,
  `voucher_type` enum('GiftToOthers','OnlyMeUsage') DEFAULT NULL,
  `payment_method_id` bigint DEFAULT NULL,
  `voucher_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_vb_payment_method_id` (`payment_method_id`),
  KEY `fk_vb_voucher_id` (`voucher_id`),
  CONSTRAINT `fk_vb_payment_method_id` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_method` (`id`),
  CONSTRAINT `fk_vb_voucher_id` FOREIGN KEY (`voucher_id`) REFERENCES `voucher` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voucher_bought`
--

LOCK TABLES `voucher_bought` WRITE;
/*!40000 ALTER TABLE `voucher_bought` DISABLE KEYS */;
INSERT INTO `voucher_bought` VALUES (1,_binary '',900.00,'Smith',_binary '','+1234567890',5,'GiftToOthers',1,3),(2,_binary '',450.00,'Smith1',_binary '','+1234567890',5,'GiftToOthers',1,2),(3,_binary '',180.00,'Smith1',_binary '','+1234567890',2,'GiftToOthers',1,1),(4,_binary '',1350.00,'Smith1',_binary '','+1234567890',1500,'GiftToOthers',1,4),(5,_binary '',1350.00,'Smith1',_binary '','+1234567890',1500,'GiftToOthers',1,5),(6,_binary '',1350.00,'Smith1',_binary '','+1234567890',1500,'GiftToOthers',1,5),(7,_binary '',1350.00,'Smith1',_binary '','+1234567890',1500,'GiftToOthers',1,5),(8,_binary '',13500.00,'Smith1',_binary '','+1234567890',15000,'GiftToOthers',1,6),(9,_binary '',13500.00,'Smith1',_binary '','+1234567890',15000,'GiftToOthers',1,7);
/*!40000 ALTER TABLE `voucher_bought` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voucher_promo_code`
--

DROP TABLE IF EXISTS `voucher_promo_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voucher_promo_code` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `promo_code` varchar(255) DEFAULT NULL,
  `qr_image` longblob,
  `used` bit(1) NOT NULL,
  `voucher_bought_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_vpc_voucher_bought_id` (`voucher_bought_id`),
  CONSTRAINT `fk_vpc_voucher_bought_id` FOREIGN KEY (`voucher_bought_id`) REFERENCES `voucher_bought` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37514 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voucher_promo_code`
--

LOCK TABLES `voucher_promo_code` WRITE;
/*!40000 ALTER TABLE `voucher_promo_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `voucher_promo_code` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-18 22:49:50
