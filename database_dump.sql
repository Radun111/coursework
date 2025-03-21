-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: coursework_crs
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
INSERT INTO `admins` VALUES (1,'Admin One','admin1@example.com','admin1','2025-03-20 07:05:24'),(2,'Admin Two','admin2@example.com','admin2','2025-03-20 07:05:24');
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_registrations`
--

DROP TABLE IF EXISTS `course_registrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_registrations` (
  `registration_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `dob` date NOT NULL,
  `address` text NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `course_id` int NOT NULL,
  `course_name` varchar(255) NOT NULL,
  `ol_math_passed` tinyint(1) DEFAULT '0',
  `ol_english_passed` tinyint(1) DEFAULT '0',
  `al_minimum_c_passes` tinyint(1) DEFAULT '0',
  `additional_academic_info` text,
  `registration_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('Pending','Approved','Rejected') DEFAULT 'Pending',
  PRIMARY KEY (`registration_id`),
  UNIQUE KEY `email` (`email`),
  KEY `student_id` (`student_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `course_registrations_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
  CONSTRAINT `course_registrations_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_registrations`
--

LOCK TABLES `course_registrations` WRITE;
/*!40000 ALTER TABLE `course_registrations` DISABLE KEYS */;
INSERT INTO `course_registrations` VALUES (1,1,'Alice Johnson','2002-04-15','123 Maple Street, NY','alice.johnson@example.com','9876543210',4,'Software Engineering',1,1,1,'','2025-03-18 16:58:32','Rejected'),(5,2,'Robert Smith','2001-08-22','456 Oak Avenue, CA','robert.smith@example.com','8765432109',5,'Operating Systems',1,1,1,'','2025-03-18 17:10:24','Approved'),(32,3,'Emily Davis','2003-02-10','789 Pine Road, TX','emily.davis@example.com','7654321098',3,'Database Management Systems',1,1,1,NULL,'2025-03-21 12:16:43','Approved'),(33,4,'Michael Brown','2000-11-05','321 Birch Lane, FL','michael.brown@example.com','6543210987',4,'Software Engineering',1,1,1,NULL,'2025-03-21 12:16:43','Pending'),(34,5,'Sarah Wilson','2002-06-18','987 Cedar Court, IL','sarah.wilson@example.com','5432109876',5,'Operating Systems',1,1,1,NULL,'2025-03-21 12:16:43','Pending'),(35,6,'Daniel Martinez','2001-12-30','741 Elm Street, NV','daniel.martinez@example.com','4321098765',6,'Computer Networks',1,1,1,NULL,'2025-03-21 12:16:43','Pending'),(36,7,'Laura White','2003-09-25','852 Spruce Blvd, WA','laura.white@example.com','3210987654',7,'Artificial Intelligence',1,1,1,NULL,'2025-03-21 12:16:43','Pending'),(37,8,'James Anderson','2002-07-08','963 Redwood Drive, AZ','james.anderson@example.com','2109876547',8,'Cybersecurity',1,1,1,NULL,'2025-03-21 12:16:43','Pending'),(38,9,'Christopher Lee','2000-03-14','159 Willow Parkway, CO','christopher.lee@example.com','1098765432',9,'Cloud Computing',1,1,1,NULL,'2025-03-21 12:16:43','Pending'),(39,10,'Olivia Harris','2003-05-20','753 Aspen Trail, OR','olivia.harris@example.com','0987654329',10,'Machine Learning',1,1,1,NULL,'2025-03-21 12:16:43','Approved');
/*!40000 ALTER TABLE `course_registrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `course_id` int NOT NULL AUTO_INCREMENT,
  `course_name` varchar(100) NOT NULL,
  `credit_hours` int NOT NULL,
  `instructor` varchar(100) NOT NULL,
  `department` varchar(100) NOT NULL,
  `prerequisites` text,
  `max_capacity` int NOT NULL,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,'Introduction to Computer Science',3,'Dr. Alice Johnson','Computer Science',NULL,50),(2,'Data Structures and Algorithms',4,'Dr. Robert Smith','Computer Science','Introduction to Computer Science',40),(3,'Database Management Systems',3,'Dr. Emily Davis','Computer Science','Introduction to Computer Science',45),(4,'Software Engineering',3,'Dr. Michael Brown','Computer Science','Data Structures and Algorithms',35),(5,'Operating Systems',4,'Dr. Sarah Wilson','Computer Science','Data Structures and Algorithms',41),(6,'Computer Networks',3,'Dr. Daniel Martinez','Computer Science','Operating Systems',50),(7,'Artificial Intelligence',3,'Dr. Laura White','Computer Science','Data Structures and Algorithms',30),(8,'Cybersecurity',3,'Dr. James Anderson','Information Technology','Operating Systems',40),(9,'Cloud Computing',3,'Dr. Christopher Lee','Information Technology','Computer Networks',50),(10,'Machine Learning',4,'Dr. Olivia Harris','Computer Science','Artificial Intelligence',30),(11,'Digital Marketing',3,'Dr. Sophia Clark','Business',NULL,60),(12,'Business Analytics',3,'Dr. Matthew Lewis','Business','Statistics',50),(13,'Accounting Principles',3,'Dr. Benjamin Young','Business',NULL,70),(14,'Physics for Engineers',3,'Dr. William Walker','Engineering',NULL,60),(15,'Thermodynamics',4,'Dr. Ava Hall','Mechanical Engineering','Physics for Engineers',40),(16,'Embedded Systems',3,'Dr. Lucas Allen','Electrical Engineering','Digital Logic Design',35),(17,'Civil Engineering Design',3,'Dr. Sophia Turner','Civil Engineering','Structural Analysis',45);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrollments`
--

DROP TABLE IF EXISTS `enrollments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollments` (
  `enrollment_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `student_name` varchar(100) NOT NULL,
  `course_id` int NOT NULL,
  `course_title` varchar(100) NOT NULL,
  `enrollment_date` date NOT NULL,
  PRIMARY KEY (`enrollment_id`),
  KEY `student_id` (`student_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`) ON DELETE CASCADE,
  CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollments`
--

LOCK TABLES `enrollments` WRITE;
/*!40000 ALTER TABLE `enrollments` DISABLE KEYS */;
INSERT INTO `enrollments` VALUES (1,2,'Robert Smith',5,'Operating Systems','2025-03-21'),(3,3,'Emily Davis',3,'Database Management Systems','2025-03-21'),(4,10,'Olivia Harris',10,'Machine Learning','2025-03-21');
/*!40000 ALTER TABLE `enrollments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_academic_records`
--

DROP TABLE IF EXISTS `student_academic_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_academic_records` (
  `record_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `course_id` int NOT NULL,
  `grade` varchar(2) NOT NULL,
  `status` varchar(20) NOT NULL,
  `term` varchar(10) NOT NULL,
  `year` int NOT NULL,
  PRIMARY KEY (`record_id`),
  KEY `student_id` (`student_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `student_academic_records_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
  CONSTRAINT `student_academic_records_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_academic_records`
--

LOCK TABLES `student_academic_records` WRITE;
/*!40000 ALTER TABLE `student_academic_records` DISABLE KEYS */;
INSERT INTO `student_academic_records` VALUES (11,1,1,'A','Completed','Term 1',2023),(12,1,4,'B+','Completed','Term 2',2023),(14,2,5,'A-','In Progress','Term 2',2024),(15,3,3,'A-','In Progress','Term 1',2024),(16,3,6,'B','Completed','Term 2',2023),(17,4,4,'B','Completed','Term 1',2023),(18,4,7,'A','In Progress','Term 2',2024),(19,5,5,'A','Completed','Term 1',2023),(20,5,8,'B+','In Progress','Term 2',2024),(21,6,6,'B+','In Progress','Term 1',2024),(22,6,9,'A-','Completed','Term 2',2023),(23,7,7,'A-','Completed','Term 1',2023),(24,7,10,'B','In Progress','Term 2',2024),(25,8,8,'B','Completed','Term 1',2023),(26,8,1,'A','In Progress','Term 2',2024),(27,9,9,'A','In Progress','Term 1',2024),(28,9,2,'B+','Completed','Term 2',2023),(29,10,10,'B+','In Progress','Term 1',2024),(30,10,3,'A-','Completed','Term 2',2023);
/*!40000 ALTER TABLE `student_academic_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_courses`
--

DROP TABLE IF EXISTS `student_courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_courses` (
  `student_id` int NOT NULL,
  `course_id` int NOT NULL,
  PRIMARY KEY (`student_id`,`course_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `student_courses_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
  CONSTRAINT `student_courses_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_courses`
--

LOCK TABLES `student_courses` WRITE;
/*!40000 ALTER TABLE `student_courses` DISABLE KEYS */;
INSERT INTO `student_courses` VALUES (1,1),(2,1),(3,1),(4,2),(5,2),(6,2),(7,3),(8,3),(9,3),(10,4),(15,4),(16,4),(17,5),(18,5),(19,5),(1,6),(2,6),(3,6),(4,7),(5,7),(6,7),(7,8),(8,8),(9,8),(10,9),(15,9),(16,9),(17,10),(18,10),(19,10);
/*!40000 ALTER TABLE `student_courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `student_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone_number` varchar(15) NOT NULL,
  `password` varchar(255) NOT NULL,
  `date_of_birth` date NOT NULL,
  `address` text,
  PRIMARY KEY (`student_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone_number` (`phone_number`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (1,'Alice Johnson','alice.johnson@example.com','9876543210','12345','2002-04-15','123 Maple Street, NY'),(2,'Robert Smith','robert.smith@example.com','8765432109','securePass456','2001-08-22','456 Oak Avenue, CA'),(3,'Emily Davis','emily.davis@example.com','7654321098','mypassword789','2003-02-10','789 Pine Road, TX'),(4,'Michael Brown','michael.brown@example.com','6543210987','passMichael007','2000-11-05','321 Birch Lane, FL'),(5,'Sarah Wilson','sarah.wilson@example.com','5432109876','SarahSecurePass','2002-06-18','987 Cedar Court, IL'),(6,'Daniel Martinez','daniel.martinez@example.com','4321098765','martinezDaniel999','2001-12-30','741 Elm Street, NV'),(7,'Laura White','laura.white@example.com','3210987654','WhiteLaura456','2003-09-25','852 Spruce Blvd, WA'),(8,'James Anderson','james.anderson@example.com','2109876547','AndersonJ123','2002-07-08','963 Redwood Drive, AZ'),(9,'Christopher Lee','christopher.lee@example.com','1098765432','LeeChris987','2000-03-14','159 Willow Parkway, CO'),(10,'Olivia Harris','olivia.harris@example.com','0987654329','OliviaHarris789','2003-05-20','753 Aspen Trail, OR'),(15,'John Doe','john.doe@example.com','1234567890','482c811da5d5b4bc6d497ffa98491e38','2000-01-15','123 Main St, Springfield'),(16,'Jane Smith','jane.smith@example.com','0987654321','b05a14dae42608f1909cfbd3c3e84f1d','1999-05-22','456 Elm St, Shelbyville'),(17,'Alice Johnson','alice.j@example.com','5551234567','e5fdf178bdcf12c5807a8c57a0cff5d4','2001-11-30','789 Oak St, Capital City'),(18,'Bob Brown','bob.brown@example.com','4445556666','f07cde0aea6fee02ca3543928d0585da','1998-03-10','101 Pine St, Metropolis'),(19,'Charlie Davis','charlie.d@example.com','7778889999','3741a7541fa70d48c1f3af77557d386e','2002-07-18','202 Maple St, Gotham');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-21 23:23:51
