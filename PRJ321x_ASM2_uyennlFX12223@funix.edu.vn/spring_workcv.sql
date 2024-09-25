-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: spring_workcv
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `applypost`
--

DROP TABLE IF EXISTS `applypost`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applypost` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` varchar(255) DEFAULT NULL,
  `recruitment_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `name_cv` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_recruitment` (`user_id`,`recruitment_id`),
  KEY `fk_applypost_recruitment` (`recruitment_id`),
  CONSTRAINT `fk_applypost_recruitment` FOREIGN KEY (`recruitment_id`) REFERENCES `recruitment` (`id`),
  CONSTRAINT `fk_applypost_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applypost`
--

LOCK TABLES `applypost` WRITE;
/*!40000 ALTER TABLE `applypost` DISABLE KEYS */;
INSERT INTO `applypost` VALUES (31,NULL,1,47,'Uyennguyenlam_Businessman.CV -1726381564142.pdf',1,'\r\n                                                    '),(32,NULL,1,1,'LouisNguyen_IB_Physics&Math_Coach-1725953270214.pdf',1,'123\r\n                                                    '),(33,NULL,2,1,'LouisNguyen_IB_Physics&Math_Coach-1726463951533.pdf',1,'16.9\r\n                                                    '),(34,NULL,1,46,'CV_NGUYENLAMUYEN_EmbeddedEngineer-1726464114766.pdf',1,'\r\n                                                    '),(35,NULL,2,46,'Uyennguyenlam_Businessman.CV -1726465328653.pdf',1,'16.9\r\n                                                    '),(36,NULL,10,46,'Uyennguyenlam_Businessman.CV -1726465360700.pdf',1,'\r\n                                                    '),(37,NULL,11,46,'Uyennguyenlam_Businessman.CV -1726465377794.pdf',1,'\r\n                                                    ');
/*!40000 ALTER TABLE `applypost` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `number_choose` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'IT',10),(2,'Marketing',5),(3,'Finance',7);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `name_company` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_company_user` (`user_id`),
  CONSTRAINT `fk_company_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (7,'528 Huỳnh Tấn Phát','<p>Learn with mentors&nbsp;</p>','giasuxanh@gmail.com','/assets/images/company-2-1725636644758-company-2.jpg',0,44,'Renesas','0983997517'),(10,'D7','<p>Com2</p>','com2@gmail.com','/assets/images/company-2-1724581735377-company-2.jpg',0,45,'Gia Su Xanh','22222');
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cv`
--

DROP TABLE IF EXISTS `cv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cv` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user` (`user_id`),
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cv`
--

LOCK TABLES `cv` WRITE;
/*!40000 ALTER TABLE `cv` DISABLE KEYS */;
INSERT INTO `cv` VALUES (62,'LouisNguyen_IB_Physics&Math_Coach-1726463951533.pdf',NULL),(63,'Uyennguyenlam_Businessman.CV -1726379172742.pdf',NULL),(64,'Uyennguyenlam_Businessman.CV -1726381564142.pdf',NULL),(65,'Uyennguyenlam_Businessman.CV -1726465377794.pdf',NULL);
/*!40000 ALTER TABLE `cv` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follow_company`
--

DROP TABLE IF EXISTS `follow_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follow_company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company_id` int DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_follow_company_company` (`company_id`),
  KEY `fk_follow_company_user` (`user_id`),
  CONSTRAINT `fk_follow_company_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `fk_follow_company_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follow_company`
--

LOCK TABLES `follow_company` WRITE;
/*!40000 ALTER TABLE `follow_company` DISABLE KEYS */;
INSERT INTO `follow_company` VALUES (2,7,1);
/*!40000 ALTER TABLE `follow_company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recruitment`
--

DROP TABLE IF EXISTS `recruitment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recruitment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `experience` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `job_rank` varchar(255) DEFAULT NULL,
  `salary` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `view` int DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `company_id` int DEFAULT NULL,
  `deadline` varchar(255) DEFAULT NULL,
  `created_at` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_recruitment_company` (`company_id`),
  KEY `fk_recruitment_category` (`category_id`),
  CONSTRAINT `fk_recruitment_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `fk_recruitment_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recruitment`
--

LOCK TABLES `recruitment` WRITE;
/*!40000 ALTER TABLE `recruitment` DISABLE KEYS */;
INSERT INTO `recruitment` VALUES (1,'Recruitment Address 1','Recruitment 1 Description','2 years',5,'Senior','$5000',1,'Software Engineer','Full-time',100,1,7,'2024-09-01','2024-07-01'),(2,'D4','<p>AAA</p>','2',15,NULL,'$20000',1,'A','Full time',50,3,7,'2024-08-14',NULL),(3,'D4','<p>BBB</p>','10',7,NULL,'$20000',1,'aaaa','Full time',100,2,7,'2024-08-22',NULL),(5,'D4','<p>CCC</p>','10',100,NULL,'$2500',1,'abcd','Full time',59,1,7,'2024-08-21',NULL),(6,'D7,HCMC','<p>DD</p>','10',2,NULL,'$1600',1,'Dabc','Full time',60,3,7,'2024-08-15',NULL),(7,'D7,HCMC','<p>EEE</p>','10',149,NULL,'$1600',NULL,'E','Full time',NULL,3,10,'2024-08-23',NULL),(8,'D7','<p>AAAA</p>','10',5,NULL,'$3000',NULL,'AA','Part time',NULL,2,10,'2024-09-06',NULL),(9,'528 Huỳnh Tấn Phát','<p>nguễn nguyễn nguyễn</p>','10',222,NULL,'$3000',NULL,'AAA','Full time',NULL,3,10,'2024-08-28',NULL),(10,'4','<p>4</p>','4',4,NULL,'$20000',NULL,'44','Full time',NULL,2,10,'2024-08-30',NULL),(11,'44','<p>44</p>','44',44,NULL,'44',NULL,'44','Freelancer',NULL,1,10,'2024-08-29',NULL),(13,'d8','<p>AAAAA</p>','13',12,NULL,'$12000',NULL,'AAAAA','Full time',NULL,2,10,'2024-09-07',NULL),(14,'d8','','13',12,NULL,'$12000',NULL,'AAAAA','Full time',NULL,2,10,'2024-09-07',NULL),(15,'D4','<p>abcd</p>','44',12,NULL,'$20000',NULL,'abcd','Freelancer',NULL,3,10,'',NULL),(16,'D7','<p>hhh</p>','4',44,NULL,'$3000',NULL,'hhh','Full time',NULL,2,7,'2024-08-30',NULL),(18,'1','<p>1</p>','1',0,NULL,'1',NULL,'1','Full time',NULL,1,7,'2024-09-07',NULL),(19,'D7','<p>BBB</p>','3',1,NULL,'$20000',NULL,'BBB','Full time',NULL,2,7,'2024-09-07',NULL),(20,'2','<p>2</p>','2',2,NULL,'2',NULL,'2','Freelancer',NULL,3,7,'2024-08-31',NULL),(21,'3','<p>3</p>','3',3,NULL,'$20000',NULL,'3','Part time',NULL,3,7,'2024-08-30',NULL),(22,'528 Huỳnh Tấn Phát','<p>k</p>','2',6,NULL,'$2500',NULL,'k','Full time',NULL,1,7,'2024-09-07',NULL);
/*!40000 ALTER TABLE `recruitment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ROLE_USER'),(2,'ROLE_HR');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `save_job`
--

DROP TABLE IF EXISTS `save_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `save_job` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `recruitment_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_save_job_recruitment` (`recruitment_id`),
  KEY `fk_save_job_user` (`user_id`),
  CONSTRAINT `fk_save_job_recruitment` FOREIGN KEY (`recruitment_id`) REFERENCES `recruitment` (`id`),
  CONSTRAINT `fk_save_job_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `save_job`
--

LOCK TABLES `save_job` WRITE;
/*!40000 ALTER TABLE `save_job` DISABLE KEYS */;
INSERT INTO `save_job` VALUES (1,1,47),(4,3,46),(5,1,46),(6,2,46),(7,5,46),(8,6,46),(9,10,46),(10,1,1),(11,2,1);
/*!40000 ALTER TABLE `save_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  `cv_id` bigint DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_role` (`role_id`),
  KEY `fk_user_cv` (`cv_id`),
  CONSTRAINT `fk_user_cv` FOREIGN KEY (`cv_id`) REFERENCES `cv` (`id`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'528 HuynhTanPhat','<p>i am a User</p>','user1@gmail.com','/assets/images/person_2-1725461833328-person_2.jpg','$2a$10$g4FKWXoODRodhsr5uvcvX.dKS460yJmBcBPRkmCnVjeAIk0k6D1rK',1,1,62,'user1nguyen','11111'),(44,'D7,HCMC','<p>Hello ae 16.8</p>','nguyenlamuyenbk@gmail.com','/assets/images/9f089b7d7435826bdb24-1725468902513-9f089b7d7435826bdb24.jpg','$2a$10$2dREotCLN967hjYooka4l.uA/xC08uB3PhD02yBFlme.96fhbi/Sm',1,2,NULL,'Uyen Nguyenlam','0983997517'),(45,'Thu Duc','<p>im GSX HR</p>','hello.giasu@gmail.com','/assets/images/person_4-1724581728618-person_4.jpg','$2a$10$3zT8SjBxGsedPwYj2uU0g.EVPInkvCVGx2tQwEF4WgCMxhQqNZdUu',1,2,NULL,'Gia Su Xanh','123456789'),(46,'d8','<p>user2</p>','user2@gmail.com','/assets/images/person_3-1724163563250-person_3.jpg','$2a$10$oJ7T.g6NBtCqwWExCdz2WOuAYkh9nKhUkZd1aB9zsf/PEuBH8rL06',1,1,NULL,'user2','22222'),(47,'D4','<p>333</p>','user3@gmail.com','/assets/images/person_5-1724748474333-person_5.jpg','$2a$10$XwqHJy7aTzfZQsNH2wAQaepMzKUwECExSmWG2Ozjgmg0ttUuGu/rm',1,1,NULL,'user3','333');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verificationtoken`
--

DROP TABLE IF EXISTS `verificationtoken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verificationtoken` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `expiry_date` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_verificationtoken_user` (`user_id`),
  CONSTRAINT `fk_verificationtoken_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verificationtoken`
--

LOCK TABLES `verificationtoken` WRITE;
/*!40000 ALTER TABLE `verificationtoken` DISABLE KEYS */;
INSERT INTO `verificationtoken` VALUES (17,'14372415-4ca5-44e4-b95e-fca242c168bc',44,'2024-08-07 07:29:29'),(18,'2242d37d-b8ff-48a8-9908-ab1522ee2c5b',44,'2024-08-07 15:28:13'),(19,'af3c433a-b7ea-4c30-ae95-ba855d8e5d9c',44,'2024-08-07 15:34:47'),(20,'af7c2982-d623-4a97-a1d2-6fe80f410c48',45,'2024-08-12 00:03:46'),(21,'365a8bbe-0236-443a-94fb-51417c733f09',46,'2024-08-21 07:44:04'),(22,'f9fe5217-c39d-4e74-9804-1cb83d329288',47,'2024-08-28 08:46:43');
/*!40000 ALTER TABLE `verificationtoken` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-18 12:04:45
