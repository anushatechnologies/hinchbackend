-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: mysql-34011113-hinchmart-aafe.j.aivencloud.com    Database: hinchmartdb
-- ------------------------------------------------------
-- Server version	8.4.8

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '916941de-9c55-11f1-8057-926cb20853d9:1-761,
a849f9cd-9d43-11f1-b163-f2b10cab70b0:1-17';

--
-- Table structure for table `activity_logs`
--

DROP TABLE IF EXISTS `activity_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action` varchar(100) NOT NULL,
  `details` text,
  `entity_id` bigint DEFAULT NULL,
  `entity_type` varchar(50) DEFAULT NULL,
  `ip_address` varchar(50) DEFAULT NULL,
  `timestamp` datetime(6) NOT NULL,
  `user_email` varchar(150) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_log_user` (`user_id`),
  KEY `idx_log_action` (`action`),
  KEY `idx_log_created_at` (`timestamp`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_logs`
--

LOCK TABLES `activity_logs` WRITE;
/*!40000 ALTER TABLE `activity_logs` DISABLE KEYS */;
INSERT INTO `activity_logs` VALUES (1,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 11:40:23.515730','buyer@demo.com',4),(2,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 11:42:00.689952','buyer@demo.com',4),(3,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 13:22:26.912659','buyer@demo.com',4),(4,'USER_OTP_LOGIN','Logged in via OTP verification',2,'USER',NULL,'2026-08-20 13:22:46.824717','admin@hinchmart.com',2),(5,'USER_OTP_LOGIN','Logged in via OTP verification',2,'USER',NULL,'2026-08-20 13:23:03.588669','admin@hinchmart.com',2),(6,'USER_OTP_LOGIN','Logged in via OTP verification',2,'USER',NULL,'2026-08-20 13:23:11.038527','admin@hinchmart.com',2),(7,'USER_OTP_LOGIN','Logged in via OTP verification',2,'USER',NULL,'2026-08-20 13:23:18.853914','admin@hinchmart.com',2),(8,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 13:23:27.814647','buyer@demo.com',4),(9,'USER_OTP_LOGIN','Logged in via OTP verification',5,'USER',NULL,'2026-08-20 13:23:28.942919','seller@tata.com',5),(10,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 13:24:09.293943','buyer@demo.com',4),(11,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 13:24:42.716827','buyer@demo.com',4),(12,'ORDER_PLACED','Placed order ORD-2283377-D06E with total amount Ôé╣361220.00',1,'ORDER',NULL,'2026-08-20 13:24:43.718605','buyer@demo.com',4),(13,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 13:24:50.945969','buyer@demo.com',4),(14,'USER_OTP_LOGIN','Logged in via OTP verification',5,'USER',NULL,'2026-08-20 13:25:00.596994','seller@tata.com',5),(15,'SHIPMENT_CREATED','Created shipment SHP-2026-2301424-D535 for order ORD-2283377-D06E',1,'SHIPMENT',NULL,'2026-08-20 13:25:01.748262','seller@tata.com',5),(16,'USER_OTP_LOGIN','Logged in via OTP verification',5,'USER',NULL,'2026-08-20 13:25:14.308276','seller@tata.com',5),(17,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 13:25:14.796713','buyer@demo.com',4),(18,'USER_OTP_LOGIN','Logged in via OTP verification',2,'USER',NULL,'2026-08-20 13:26:01.731600','admin@hinchmart.com',2),(19,'USER_OTP_LOGIN','Logged in via OTP verification',5,'USER',NULL,'2026-08-20 13:26:02.330559','seller@tata.com',5),(20,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 13:26:02.893133','buyer@demo.com',4),(21,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 13:26:18.528839','buyer@demo.com',4),(22,'USER_OTP_LOGIN','Logged in via OTP verification',5,'USER',NULL,'2026-08-20 13:26:19.097386','seller@tata.com',5),(23,'USER_OTP_LOGIN','Logged in via OTP verification',2,'USER',NULL,'2026-08-20 13:26:19.582142','admin@hinchmart.com',2),(24,'USER_OTP_LOGIN','Logged in via OTP verification',2,'USER',NULL,'2026-08-20 13:26:28.526513','admin@hinchmart.com',2),(25,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 13:26:29.008400','buyer@demo.com',4),(26,'USER_OTP_LOGIN','Logged in via OTP verification',5,'USER',NULL,'2026-08-20 13:26:29.596733','seller@tata.com',5),(27,'USER_OTP_LOGIN','Logged in via OTP verification',2,'USER',NULL,'2026-08-20 13:26:37.102288','admin@hinchmart.com',2),(28,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 13:26:44.498749','buyer@demo.com',4),(29,'RFQ_CREATED','Created RFQ: RFQ-405096-5EF7',2,'RFQ',NULL,'2026-08-20 13:26:45.168207','buyer@demo.com',4),(30,'USER_OTP_LOGIN','Logged in via OTP verification',5,'USER',NULL,'2026-08-20 13:26:53.100982','seller@tata.com',5),(31,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 13:26:53.662612','buyer@demo.com',4),(32,'USER_OTP_LOGIN','Logged in via OTP verification',5,'USER',NULL,'2026-08-20 13:27:01.471570','seller@tata.com',5),(33,'USER_OTP_LOGIN','Logged in via OTP verification',5,'USER',NULL,'2026-08-20 13:27:09.603848','seller@tata.com',5),(34,'RFQ_QUOTE_SUBMITTED','Submitted quote of Ôé╣2925000 for RFQ RFQ-405096-5EF7',2,'RFQ_QUOTE',NULL,'2026-08-20 13:27:10.297010','seller@tata.com',5),(35,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 13:27:16.796907','buyer@demo.com',4),(36,'RFQ_QUOTE_ACCEPTED','Accepted quote from Anand Verma for RFQ RFQ-405096-5EF7',2,'RFQ_QUOTE',NULL,'2026-08-20 13:27:19.068856','buyer@demo.com',4),(37,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-20 13:35:23.068786','buyer@demo.com',4),(38,'USER_OTP_LOGIN','Logged in via OTP verification',2,'USER',NULL,'2026-08-21 03:47:14.685385','admin@hinchmart.com',2),(39,'USER_OTP_LOGIN','Logged in via OTP verification',5,'USER',NULL,'2026-08-21 03:47:15.230057','seller@tata.com',5),(40,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-21 03:47:16.710308','buyer@demo.com',4),(41,'USER_OTP_LOGIN','Logged in via OTP verification',4,'USER',NULL,'2026-08-21 03:47:17.187609','buyer@demo.com',4),(42,'SELLER_STATUS_CHANGED','Status changed to APPROVED',1,'SELLER_PROFILE',NULL,'2026-08-21 03:47:22.387635',NULL,5),(43,'USER_OTP_LOGIN','Logged in via OTP verification',2,'USER',NULL,'2026-08-21 03:47:59.016751','admin@hinchmart.com',2);
/*!40000 ALTER TABLE `activity_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `brands`
--

DROP TABLE IF EXISTS `brands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brands` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `description` text,
  `is_active` bit(1) NOT NULL,
  `logo_url` varchar(500) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `slug` varchar(120) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKoce3937d2f4mpfqrycbr0l93m` (`name`),
  UNIQUE KEY `UKpnhnc9urm6fro7oseu9vka70q` (`slug`),
  KEY `idx_brand_slug` (`slug`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brands`
--

LOCK TABLES `brands` WRITE;
/*!40000 ALTER TABLE `brands` DISABLE KEYS */;
INSERT INTO `brands` VALUES (1,'2026-08-20 11:35:29.129264','India\'s premier steel manufacturer',_binary '','https://images.unsplash.com/photo-1504917599217-d4dc5ebe6122?w=200','TATA Steel','tata-steel','2026-08-20 11:35:29.129264'),(2,'2026-08-20 11:35:29.179336','The Engineer\'s Choice',_binary '','https://images.unsplash.com/photo-1589939705384-5185137a7f0f?w=200','UltraTech Cement','ultratech-cement','2026-08-20 11:35:29.179336'),(3,'2026-08-20 11:35:29.217605','Leader in plumbing and drainage piping systems',_binary '','https://images.unsplash.com/photo-1541888946425-d0fbb18f15f6?w=200','Astral Pipes','astral-pipes','2026-08-20 11:35:29.217605'),(4,'2026-08-20 11:35:29.275767','Leading Fast Moving Electrical Goods (FMEG) company',_binary '','https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=200','Havells','havells','2026-08-20 11:35:29.275767'),(5,'2026-08-20 11:35:29.311529','Professional power tools and machinery',_binary '','https://images.unsplash.com/photo-1504148455328-c376907d081c?w=200','Bosch Power Tools','bosch-power-tools','2026-08-20 11:35:29.311529'),(6,'2026-08-20 11:35:29.376346','India\'s leading paint and waterproofing manufacturer',_binary '','https://images.unsplash.com/photo-1562259949-e8e7689d7828?w=200','Asian Paints','asian-paints','2026-08-20 11:35:29.376346'),(7,'2026-08-20 11:35:29.423102','Personal Protective Equipment and industrial safety solutions',_binary '','https://images.unsplash.com/photo-1578873375969-d652264e101b?w=200','Karam Safety','karam-safety','2026-08-20 11:35:29.423102');
/*!40000 ALTER TABLE `brands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `buyer_profiles`
--

DROP TABLE IF EXISTS `buyer_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `buyer_profiles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `annual_turnover` decimal(15,2) DEFAULT NULL,
  `billing_address` text,
  `business_type` varchar(50) DEFAULT NULL,
  `city` varchar(60) DEFAULT NULL,
  `company_name` varchar(150) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `credit_limit` decimal(15,2) DEFAULT NULL,
  `gstin` varchar(20) DEFAULT NULL,
  `pincode` varchar(10) DEFAULT NULL,
  `shipping_address` text,
  `state` varchar(60) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKda43mt8r29e906idq64yom4se` (`user_id`),
  CONSTRAINT `FKqqr3n387jcywod092ljqwt9t1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buyer_profiles`
--

LOCK TABLES `buyer_profiles` WRITE;
/*!40000 ALTER TABLE `buyer_profiles` DISABLE KEYS */;
INSERT INTO `buyer_profiles` VALUES (1,25000000.00,'Plot 45, MIDC Industrial Area, Phase 2, Pune','Infrastructure Contractor','Pune','Apex Infra Projects Pvt Ltd','2026-08-20 11:35:28.360665',5000000.00,'27AAAAA0000A1Z5','411057','Site #7, Metro Line 3 Corridor, Hinjewadi, Pune','Maharashtra','2026-08-20 11:35:28.360665',4);
/*!40000 ALTER TABLE `buyer_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `gst_percentage` decimal(5,2) NOT NULL,
  `quantity` int NOT NULL,
  `subtotal` decimal(14,2) NOT NULL,
  `unit_price` decimal(12,2) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cart_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `seller_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_cart_item_cart` (`cart_id`),
  KEY `idx_cart_item_product` (`product_id`),
  KEY `idx_cart_item_seller` (`seller_id`),
  CONSTRAINT `FK1re40cjegsfvw58xrkdp6bac6` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKlvvywj37bcn6hatxmi939gl45` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKpcttvuq4mxppo8sxggjtn5i2c` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK64t7ox312pqal3p7fg9o503c2` (`user_id`),
  KEY `idx_cart_user` (`user_id`),
  CONSTRAINT `FKb5o626f86h46m4s7ms6ginnop` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
INSERT INTO `carts` VALUES (1,'2026-08-20 13:24:09.972104','2026-08-20 13:24:09.972128',4);
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `description` text,
  `display_order` int DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `slug` varchar(120) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt8o6pivur7nn124jehx7cygw5` (`name`),
  UNIQUE KEY `UKoul14ho7bctbefv8jywp5v3i2` (`slug`),
  KEY `idx_category_slug` (`slug`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'2026-08-20 11:35:29.477513','Structural steel, TMT rebars, MS angles, channels, and binding wires for heavy construction.',1,'https://images.unsplash.com/photo-1504917599217-d4dc5ebe6122?w=600',_binary '','Steel Rods & Rebars','steel-rods-rebars','2026-08-20 11:35:29.477513'),(2,'2026-08-20 11:35:29.660750','OPC 53 Grade, PPC, Ready Mix Concrete (RMC), and curing compounds.',2,'https://images.unsplash.com/photo-1589939705384-5185137a7f0f?w=600',_binary '','Cement & Concrete','cement-concrete','2026-08-20 11:35:29.660750'),(3,'2026-08-20 11:35:29.846116','PVC, CPVC, HDPE, SWR pipes, and brass industrial valves.',3,'https://images.unsplash.com/photo-1541888946425-d0fbb18f15f6?w=600',_binary '','Pipes & Fittings','pipes-fittings','2026-08-20 11:35:29.846116'),(4,'2026-08-20 11:35:29.997555','Armoured cables, HT/LT wires, switchgears, and distribution panels.',4,'https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=600',_binary '','Electrical & Cables','electrical-cables','2026-08-20 11:35:29.997555'),(5,'2026-08-20 11:35:30.112324','Concrete mixers, rotary hammers, angle grinders, and compactors.',5,'https://images.unsplash.com/photo-1504148455328-c376907d081c?w=600',_binary '','Power Tools & Machinery','power-tools-machinery','2026-08-20 11:35:30.112324'),(6,'2026-08-20 11:35:30.211002','Vitrified tiles, epoxy industrial floor coatings, granite slabs, and adhesive grouts.',6,'https://images.unsplash.com/photo-1513694203232-719a280e022f?w=600',_binary '','Tiles & Flooring','tiles-flooring','2026-08-20 11:35:30.211002'),(7,'2026-08-20 11:35:30.261412','Elastomeric waterproofing membranes, exterior emulsions, and primers.',7,'https://images.unsplash.com/photo-1562259949-e8e7689d7828?w=600',_binary '','Paints & Waterproofing','paints-waterproofing','2026-08-20 11:35:30.261412'),(8,'2026-08-20 11:35:30.360882','ISI marked helmets, safety shoes, full body fall arrest harnesses, and reflective wear.',8,'https://images.unsplash.com/photo-1578873375969-d652264e101b?w=600',_binary '','Safety Equipment','safety-equipment','2026-08-20 11:35:30.360882'),(9,'2026-08-21 03:47:18.246152',NULL,0,NULL,_binary '','Test Category','test-category','2026-08-21 03:47:18.246167'),(10,'2026-08-21 03:47:59.304219','Marine plywood, commercial blockboards, and hardwood timber.',9,NULL,_binary '','Timber & Plywood','timber-plywood','2026-08-21 03:47:59.304224');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery_partners`
--

DROP TABLE IF EXISTS `delivery_partners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery_partners` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `contact_number` varchar(30) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `is_active` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `tracking_url_template` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfr5xaeisfre8aid96y6nu5d43` (`code`),
  UNIQUE KEY `UKjqm40g4fmn8rn2kper66ohb64` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_partners`
--

LOCK TABLES `delivery_partners` WRITE;
/*!40000 ALTER TABLE `delivery_partners` DISABLE KEYS */;
INSERT INTO `delivery_partners` VALUES (1,'DELHIVERY_B2B','+91-124-6719500','2026-08-20 11:35:31.627981',_binary '','Delhivery B2B Freight','https://www.delhivery.com/track/package/{trackingNumber}'),(2,'VRL_LOGISTICS','+91-836-2237511','2026-08-20 11:35:31.681284',_binary '','VRL Logistics Heavy Freight','https://www.vrlgroup.in/track_consignment.aspx?lr_no={trackingNumber}'),(3,'RIVIGO_SURFACE','+91-124-4354500','2026-08-20 11:35:31.726411',_binary '','Rivigo Express Surface','https://www.rivigo.com/tracking?awb={trackingNumber}'),(4,'BLUEDART_CARGO','1860-233-1234','2026-08-20 11:35:31.760634',_binary '','Blue Dart Apex Cargo','https://www.bluedart.com/tracking/{trackingNumber}');
/*!40000 ALTER TABLE `delivery_partners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device_tokens`
--

DROP TABLE IF EXISTS `device_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `device_type` enum('ANDROID','IOS','WEB') NOT NULL,
  `fcm_token` varchar(500) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_device_user` (`user_id`),
  KEY `idx_device_token` (`fcm_token`),
  CONSTRAINT `FKhc7d11bnr8x9gs5biohdhnx1c` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_tokens`
--

LOCK TABLES `device_tokens` WRITE;
/*!40000 ALTER TABLE `device_tokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `device_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `low_stock_threshold` int DEFAULT NULL,
  `quantity` int NOT NULL,
  `reserved_quantity` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `warehouse_location` varchar(150) DEFAULT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKce3rbi3bfstbvvyne34c1dvyv` (`product_id`),
  CONSTRAINT `FKq2yge7ebtfuvwufr6lwfwqy9l` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1,5,20,0,'2026-08-20 13:24:43.750224','Kalamboli Yard - Bay 4',1),(2,100,1200,0,'2026-08-20 11:35:31.027658','Bhiwandi Central Depot',2),(3,200,3000,0,'2026-08-20 11:35:31.375939','Panvel Logistics Center',3);
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_items`
--

DROP TABLE IF EXISTS `invoice_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cgst_amount` decimal(12,2) DEFAULT NULL,
  `cgst_rate` decimal(5,2) DEFAULT NULL,
  `gst_rate` decimal(5,2) NOT NULL,
  `hsn_code` varchar(30) DEFAULT NULL,
  `igst_amount` decimal(12,2) DEFAULT NULL,
  `igst_rate` decimal(5,2) DEFAULT NULL,
  `product_name` varchar(200) NOT NULL,
  `quantity` int NOT NULL,
  `sgst_amount` decimal(12,2) DEFAULT NULL,
  `sgst_rate` decimal(5,2) DEFAULT NULL,
  `taxable_value` decimal(14,2) NOT NULL,
  `total_amount` decimal(14,2) NOT NULL,
  `unit` enum('BAG','BOX','BUNDLE','FEET','KG','LITER','METER','PIECE','ROLL','SET','SQ_FT','TON') NOT NULL,
  `unit_price` decimal(12,2) NOT NULL,
  `invoice_id` bigint NOT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_invoice_item_invoice` (`invoice_id`),
  KEY `FKs3tu9gmkgshq8oeq5n0rinxeu` (`product_id`),
  CONSTRAINT `FK46ae0lhu1oqs7cv91fn6y9n7w` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`),
  CONSTRAINT `FKs3tu9gmkgshq8oeq5n0rinxeu` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_items`
--

LOCK TABLES `invoice_items` WRITE;
/*!40000 ALTER TABLE `invoice_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `billing_address` text,
  `buyer_company_name` varchar(150) DEFAULT NULL,
  `buyer_gstin` varchar(30) DEFAULT NULL,
  `buyer_name` varchar(150) DEFAULT NULL,
  `cgst_amount` decimal(12,2) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `delivery_charge` decimal(10,2) DEFAULT NULL,
  `grand_total` decimal(14,2) NOT NULL,
  `igst_amount` decimal(12,2) NOT NULL,
  `invoice_date` date NOT NULL,
  `invoice_number` varchar(60) NOT NULL,
  `is_intra_state` bit(1) NOT NULL,
  `order_number` varchar(60) NOT NULL,
  `payment_status` enum('FAILED','PAID','PARTIALLY_REFUNDED','PENDING','REFUNDED','SUCCESS') NOT NULL,
  `place_of_supply` varchar(100) DEFAULT NULL,
  `seller_company_name` varchar(150) DEFAULT NULL,
  `seller_gstin` varchar(30) DEFAULT NULL,
  `seller_name` varchar(150) DEFAULT NULL,
  `sgst_amount` decimal(12,2) NOT NULL,
  `shipping_address` text,
  `taxable_value` decimal(14,2) NOT NULL,
  `total_gst` decimal(14,2) NOT NULL,
  `buyer_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `seller_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKl1x55mfsay7co0r3m9ynvipd5` (`invoice_number`),
  UNIQUE KEY `UKe718q5klx5pempy28p2nx88a6` (`order_id`),
  KEY `idx_invoice_number` (`invoice_number`),
  KEY `idx_invoice_order` (`order_id`),
  KEY `idx_invoice_buyer` (`buyer_id`),
  KEY `idx_invoice_seller` (`seller_id`),
  CONSTRAINT `FK4ko3y00tkkk2ya3p6wnefjj2f` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKfbhkm00dc92q4jb1wytnlkqo4` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKm98rm5tt3nb7cita4ebnaahd0` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_preferences`
--

DROP TABLE IF EXISTS `notification_preferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification_preferences` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email_enabled` bit(1) NOT NULL,
  `order_updates` bit(1) NOT NULL,
  `promotional` bit(1) NOT NULL,
  `push_enabled` bit(1) NOT NULL,
  `rfq_updates` bit(1) NOT NULL,
  `sms_enabled` bit(1) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKn2jopkbm16qv3xelbvoyjkd0g` (`user_id`),
  KEY `idx_notif_pref_user` (`user_id`),
  CONSTRAINT `FKt9qjvmcl36i14utm5uptyqg84` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_preferences`
--

LOCK TABLES `notification_preferences` WRITE;
/*!40000 ALTER TABLE `notification_preferences` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_preferences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `is_read` bit(1) NOT NULL,
  `message` text NOT NULL,
  `reference_id` bigint DEFAULT NULL,
  `reference_type` varchar(50) DEFAULT NULL,
  `title` varchar(200) NOT NULL,
  `type` enum('LOW_STOCK','ORDER_CONFIRMED','ORDER_DELIVERED','ORDER_PLACED','ORDER_SHIPPED','OUT_FOR_DELIVERY','PAYMENT_FAILED','PAYMENT_SUCCESS','PRODUCT_APPROVED','PRODUCT_REJECTED','QUOTE_ACCEPTED','QUOTE_RECEIVED','RFQ_RECEIVED') NOT NULL,
  `recipient_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_notif_recipient` (`recipient_id`),
  KEY `idx_notif_type` (`type`),
  KEY `idx_notif_is_read` (`is_read`),
  CONSTRAINT `FKqqnsjxlwleyjbxlmm213jaj3f` FOREIGN KEY (`recipient_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,'2026-08-20 13:25:01.571447',_binary '\0','Your order ORD-2283377-D06E has been packed. Tracking Number: VRL-2026-998811',1,'ORDER','Order Packed & Ready to Ship!','ORDER_SHIPPED',4),(2,'2026-08-20 13:25:15.470791',_binary '\0','Your order ORD-2283377-D06E is currently in transit (Pune Highway Toll Plaza Checkpoint).',1,'ORDER','Order Shipped & In Transit!','ORDER_SHIPPED',4);
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gst_amount` decimal(12,2) NOT NULL,
  `gst_percentage` decimal(5,2) NOT NULL,
  `product_name` varchar(200) NOT NULL,
  `quantity` int NOT NULL,
  `sku` varchar(100) DEFAULT NULL,
  `total_price` decimal(14,2) NOT NULL,
  `unit` enum('BAG','BOX','BUNDLE','FEET','KG','LITER','METER','PIECE','ROLL','SET','SQ_FT','TON') NOT NULL,
  `unit_price` decimal(12,2) NOT NULL,
  `order_id` bigint NOT NULL,
  `product_id` bigint DEFAULT NULL,
  `seller_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_order_item_order` (`order_id`),
  KEY `idx_order_item_product` (`product_id`),
  KEY `idx_order_item_seller` (`seller_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKiteu7744jhts0njdk0g9cmew6` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,54720.00,18.00,'TATA Tiscon 550D TMT Bar',5,'TATA-TISCON-550D-12MM',358720.00,'TON',60800.00,1,1,5);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_status_history`
--

DROP TABLE IF EXISTS `order_status_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_status_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `notes` text,
  `status` enum('CANCELLED','CONFIRMED','DELIVERED','OUT_FOR_DELIVERY','PLACED','PROCESSING','READY_TO_SHIP','RETURNED','RETURN_REQUESTED','SHIPPED') NOT NULL,
  `changed_by_user_id` bigint DEFAULT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_status_history_order` (`order_id`),
  KEY `FKbnuj0gvhjwxodmmu7gj3iivse` (`changed_by_user_id`),
  CONSTRAINT `FKbnuj0gvhjwxodmmu7gj3iivse` FOREIGN KEY (`changed_by_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKnmcbg3mmbt8wfva97ra40nmp3` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_status_history`
--

LOCK TABLES `order_status_history` WRITE;
/*!40000 ALTER TABLE `order_status_history` DISABLE KEYS */;
INSERT INTO `order_status_history` VALUES (1,'2026-08-20 13:24:43.619192','Order created and confirmed by buyer','PLACED',4,1),(2,'2026-08-20 13:25:01.525257','Shipment booked with tracking VRL-2026-998811','READY_TO_SHIP',5,1),(3,'2026-08-20 13:25:15.586029','In transit via carrier. Location: Pune Highway Toll Plaza Checkpoint','SHIPPED',5,1);
/*!40000 ALTER TABLE `order_status_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `billing_address` text,
  `city` varchar(100) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `delivery_charge` decimal(10,2) NOT NULL,
  `gst_amount` decimal(14,2) NOT NULL,
  `notes` text,
  `order_number` varchar(60) NOT NULL,
  `order_status` enum('CANCELLED','CONFIRMED','DELIVERED','OUT_FOR_DELIVERY','PLACED','PROCESSING','READY_TO_SHIP','RETURNED','RETURN_REQUESTED','SHIPPED') NOT NULL,
  `payment_method` enum('CASH_ON_DELIVERY','CREDIT_CARD','CREDIT_LINE','DEBIT_CARD','NEFT_RTGS','NET_BANKING','UPI') NOT NULL,
  `payment_status` enum('FAILED','PAID','PARTIALLY_REFUNDED','PENDING','REFUNDED','SUCCESS') NOT NULL,
  `pincode` varchar(20) DEFAULT NULL,
  `shipping_address` text,
  `state` varchar(100) DEFAULT NULL,
  `subtotal` decimal(14,2) NOT NULL,
  `total_amount` decimal(14,2) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `buyer_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnthkiu7pgmnqnu86i2jyoe2v7` (`order_number`),
  KEY `idx_order_number` (`order_number`),
  KEY `idx_order_buyer` (`buyer_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_order_payment_status` (`payment_status`),
  CONSTRAINT `FKhtx3insd5ge6w486omk4fnk54` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'Plot 45, MIDC Industrial Area, Phase 2, Pune','Pune','2026-08-20 13:24:43.555851',2500.00,54720.00,'Gate #3 entry. Unloading crane available.','ORD-2283377-D06E','SHIPPED','UPI','PAID','411057','Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune','Maharashtra',304000.00,361220.00,'2026-08-20 13:25:15.704613',4);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `otp_verifications`
--

DROP TABLE IF EXISTS `otp_verifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `otp_verifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `expiry_time` datetime(6) NOT NULL,
  `identifier` varchar(150) NOT NULL,
  `is_used` bit(1) NOT NULL,
  `otp_code` varchar(10) NOT NULL,
  `purpose` enum('LOGIN','PASSWORD_RESET','REGISTRATION','VERIFICATION') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_otp_identifier` (`identifier`),
  KEY `idx_otp_code` (`otp_code`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `otp_verifications`
--

LOCK TABLES `otp_verifications` WRITE;
/*!40000 ALTER TABLE `otp_verifications` DISABLE KEYS */;
INSERT INTO `otp_verifications` VALUES (1,'2026-08-20 11:40:05.523335','2026-08-20 11:50:05.516173','buyer@demo.com',_binary '\0','123456','LOGIN'),(2,'2026-08-20 11:40:23.052117','2026-08-20 11:50:23.051764','buyer@demo.com',_binary '','123456','LOGIN'),(3,'2026-08-20 11:42:00.242205','2026-08-20 11:52:00.241598','buyer@demo.com',_binary '','123456','LOGIN'),(4,'2026-08-20 13:22:19.650465','2026-08-20 13:32:19.640644','buyer@demo.com',_binary '','123456','LOGIN'),(5,'2026-08-20 13:22:37.448055','2026-08-20 13:32:37.447821','admin@demo.com',_binary '\0','123456','LOGIN'),(6,'2026-08-20 13:22:37.680794','2026-08-20 13:32:37.680431','admin@hinchmart.com',_binary '','123456','LOGIN'),(7,'2026-08-20 13:22:37.855088','2026-08-20 13:32:37.854847','seller@demo.com',_binary '\0','123456','LOGIN'),(8,'2026-08-20 13:22:38.006388','2026-08-20 13:32:38.005925','seller@hinchmart.com',_binary '\0','123456','LOGIN'),(9,'2026-08-20 13:22:38.150210','2026-08-20 13:32:38.149958','anand@tatasteel.com',_binary '\0','123456','LOGIN'),(10,'2026-08-20 13:23:03.202054','2026-08-20 13:33:03.201789','admin@hinchmart.com',_binary '','123456','LOGIN'),(11,'2026-08-20 13:23:10.667668','2026-08-20 13:33:10.667410','admin@hinchmart.com',_binary '','123456','LOGIN'),(12,'2026-08-20 13:23:18.497981','2026-08-20 13:33:18.497697','admin@hinchmart.com',_binary '','123456','LOGIN'),(13,'2026-08-20 13:23:27.260154','2026-08-20 13:33:27.259937','buyer@demo.com',_binary '','123456','LOGIN'),(14,'2026-08-20 13:23:28.644777','2026-08-20 13:33:28.644497','seller@tata.com',_binary '','123456','LOGIN'),(15,'2026-08-20 13:24:08.907218','2026-08-20 13:34:08.906995','buyer@demo.com',_binary '','123456','LOGIN'),(16,'2026-08-20 13:24:42.338287','2026-08-20 13:34:42.338066','buyer@demo.com',_binary '','123456','LOGIN'),(17,'2026-08-20 13:24:50.587073','2026-08-20 13:34:50.586856','buyer@demo.com',_binary '','123456','LOGIN'),(18,'2026-08-20 13:25:00.145196','2026-08-20 13:35:00.144910','seller@tata.com',_binary '','123456','LOGIN'),(19,'2026-08-20 13:25:13.913727','2026-08-20 13:35:13.913502','seller@tata.com',_binary '','123456','LOGIN'),(20,'2026-08-20 13:25:14.499888','2026-08-20 13:35:14.499674','buyer@demo.com',_binary '','123456','LOGIN'),(21,'2026-08-20 13:26:01.334641','2026-08-20 13:36:01.334355','admin@hinchmart.com',_binary '','123456','LOGIN'),(22,'2026-08-20 13:26:01.987688','2026-08-20 13:36:01.987449','seller@tata.com',_binary '','123456','LOGIN'),(23,'2026-08-20 13:26:02.549398','2026-08-20 13:36:02.549144','buyer@demo.com',_binary '','123456','LOGIN'),(24,'2026-08-20 13:26:18.137116','2026-08-20 13:36:18.136865','buyer@demo.com',_binary '','123456','LOGIN'),(25,'2026-08-20 13:26:18.805895','2026-08-20 13:36:18.805629','seller@tata.com',_binary '','123456','LOGIN'),(26,'2026-08-20 13:26:19.275522','2026-08-20 13:36:19.275017','admin@hinchmart.com',_binary '','123456','LOGIN'),(27,'2026-08-20 13:26:28.047768','2026-08-20 13:36:28.047566','admin@hinchmart.com',_binary '','123456','LOGIN'),(28,'2026-08-20 13:26:28.706162','2026-08-20 13:36:28.705914','buyer@demo.com',_binary '','123456','LOGIN'),(29,'2026-08-20 13:26:29.195404','2026-08-20 13:36:29.195204','seller@tata.com',_binary '','123456','LOGIN'),(30,'2026-08-20 13:26:36.742316','2026-08-20 13:36:36.742014','admin@hinchmart.com',_binary '','123456','LOGIN'),(31,'2026-08-20 13:26:43.982749','2026-08-20 13:36:43.982504','buyer@demo.com',_binary '','123456','LOGIN'),(32,'2026-08-20 13:26:52.704132','2026-08-20 13:37:01.043194','seller@tata.com',_binary '','123456','LOGIN'),(33,'2026-08-20 13:26:53.355156','2026-08-20 13:36:53.354956','buyer@demo.com',_binary '','123456','LOGIN'),(34,'2026-08-20 13:27:01.043443','2026-08-20 13:37:01.043194','seller@tata.com',_binary '','123456','LOGIN'),(35,'2026-08-20 13:27:09.223177','2026-08-20 13:37:09.222973','seller@tata.com',_binary '','123456','LOGIN'),(36,'2026-08-20 13:27:16.421830','2026-08-20 13:37:16.421637','buyer@demo.com',_binary '','123456','LOGIN'),(37,'2026-08-20 13:35:22.752711','2026-08-20 13:45:22.752526','buyer@demo.com',_binary '','123456','LOGIN'),(38,'2026-08-21 03:47:13.450480','2026-08-21 03:57:13.448798','admin@hinchmart.com',_binary '','123456','LOGIN'),(39,'2026-08-21 03:47:14.925070','2026-08-21 03:57:14.924854','seller@tata.com',_binary '','123456','LOGIN'),(40,'2026-08-21 03:47:15.921393','2026-08-21 03:57:15.921035','buyer@demo.com',_binary '','123456','LOGIN'),(41,'2026-08-21 03:47:16.897363','2026-08-21 03:57:16.897014','buyer@demo.com',_binary '','123456','LOGIN'),(42,'2026-08-21 03:47:58.692140','2026-08-21 03:57:58.691926','admin@hinchmart.com',_binary '','123456','LOGIN'),(43,'2026-08-21 10:24:14.930724','2026-08-21 10:34:14.930516','test@example.com',_binary '\0','123456','LOGIN'),(44,'2026-08-21 10:24:23.171075','2026-08-21 10:34:23.170884','9876543210',_binary '\0','123456','LOGIN');
/*!40000 ALTER TABLE `otp_verifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_transactions`
--

DROP TABLE IF EXISTS `payment_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(14,2) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `gateway_reference` varchar(150) DEFAULT NULL,
  `response_payload` text,
  `status` varchar(30) NOT NULL,
  `transaction_type` enum('PAYMENT','REFUND') NOT NULL,
  `payment_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_trx_payment` (`payment_id`),
  CONSTRAINT `FKgu8q4u0cjr8aljtknj557g2i8` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_transactions`
--

LOCK TABLES `payment_transactions` WRITE;
/*!40000 ALTER TABLE `payment_transactions` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(14,2) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `currency` varchar(10) NOT NULL,
  `error_code` varchar(50) DEFAULT NULL,
  `error_description` text,
  `gateway_order_id` varchar(100) DEFAULT NULL,
  `gateway_payment_id` varchar(100) DEFAULT NULL,
  `gateway_signature` varchar(255) DEFAULT NULL,
  `payment_method` enum('CASH_ON_DELIVERY','CREDIT_CARD','CREDIT_LINE','DEBIT_CARD','NEFT_RTGS','NET_BANKING','UPI') NOT NULL,
  `payment_number` varchar(60) NOT NULL,
  `payment_status` enum('FAILED','PAID','PARTIALLY_REFUNDED','PENDING','REFUNDED','SUCCESS') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `buyer_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKc6nxg52ow66u8ut91bytspy64` (`payment_number`),
  UNIQUE KEY `UK8vo36cen604as7etdfwmyjsxt` (`order_id`),
  KEY `idx_payment_number` (`payment_number`),
  KEY `idx_payment_order` (`order_id`),
  KEY `idx_payment_buyer` (`buyer_id`),
  KEY `idx_payment_status` (`payment_status`),
  CONSTRAINT `FK81gagumt0r8y3rmudcgpbk42l` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKg08mb5mpvbebidb9wewidf5al` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_bulk_prices`
--

DROP TABLE IF EXISTS `product_bulk_prices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_bulk_prices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `discount_percentage` decimal(5,2) DEFAULT NULL,
  `max_quantity` int DEFAULT NULL,
  `min_quantity` int NOT NULL,
  `price_per_unit` decimal(12,2) NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_bulk_product` (`product_id`),
  CONSTRAINT `FK93j3dalfhmij4r5vpw5p3cmg5` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_bulk_prices`
--

LOCK TABLES `product_bulk_prices` WRITE;
/*!40000 ALTER TABLE `product_bulk_prices` DISABLE KEYS */;
INSERT INTO `product_bulk_prices` VALUES (1,'2026-08-20 11:35:30.527504',0.00,4,1,61500.00,1),(2,'2026-08-20 11:35:30.570116',1.14,9,5,60800.00,1),(3,'2026-08-20 11:35:30.610105',2.60,24,10,59900.00,1),(4,'2026-08-20 11:35:30.643744',4.88,NULL,25,58500.00,1),(5,'2026-08-20 11:35:30.895477',0.00,199,50,380.00,2),(6,'2026-08-20 11:35:30.943625',3.95,499,200,365.00,2),(7,'2026-08-20 11:35:30.979393',7.89,NULL,500,350.00,2),(8,'2026-08-20 11:35:31.179440',0.00,199,50,120.00,3),(9,'2026-08-20 11:35:31.244090',6.67,999,200,112.00,3),(10,'2026-08-20 11:35:31.327065',12.50,NULL,1000,105.00,3);
/*!40000 ALTER TABLE `product_bulk_prices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_images`
--

DROP TABLE IF EXISTS `product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_images` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `display_order` int DEFAULT NULL,
  `image_url` varchar(500) NOT NULL,
  `is_primary` bit(1) NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_product_image_product` (`product_id`),
  CONSTRAINT `FKqnq71xsohugpqwf3c9gxmsuy` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_images`
--

LOCK TABLES `product_images` WRITE;
/*!40000 ALTER TABLE `product_images` DISABLE KEYS */;
INSERT INTO `product_images` VALUES (1,'2026-08-20 11:35:30.761491',0,'https://images.unsplash.com/photo-1504917599217-d4dc5ebe6122?w=800',_binary '',1),(2,'2026-08-20 11:35:30.811615',1,'https://images.unsplash.com/photo-1535813547-99c456a41d4a?w=800',_binary '\0',1),(3,'2026-08-20 11:35:31.079333',0,'https://images.unsplash.com/photo-1589939705384-5185137a7f0f?w=800',_binary '',2),(4,'2026-08-20 11:35:31.410204',0,'https://images.unsplash.com/photo-1541888946425-d0fbb18f15f6?w=800',_binary '',3);
/*!40000 ALTER TABLE `product_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `approval_status` enum('APPROVED','PENDING','REJECTED') NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `delivery_days` int DEFAULT NULL,
  `description` text,
  `gst_rate` decimal(5,2) NOT NULL,
  `hsn_code` varchar(30) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `moq` int NOT NULL,
  `mrp` decimal(12,2) NOT NULL,
  `product_name` varchar(200) NOT NULL,
  `selling_price` decimal(12,2) NOT NULL,
  `sku` varchar(100) NOT NULL,
  `slug` varchar(250) NOT NULL,
  `specifications` text,
  `stock` int NOT NULL,
  `unit` enum('BAG','BOX','BUNDLE','FEET','KG','LITER','METER','PIECE','ROLL','SET','SQ_FT','TON') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `brand_id` bigint DEFAULT NULL,
  `category_id` bigint NOT NULL,
  `seller_id` bigint NOT NULL,
  `subcategory_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfhmd06dsmj6k0n90swsh8ie9g` (`sku`),
  UNIQUE KEY `UKostq1ec3toafnjok09y9l7dox` (`slug`),
  KEY `idx_product_sku` (`sku`),
  KEY `idx_product_slug` (`slug`),
  KEY `idx_product_category` (`category_id`),
  KEY `idx_product_brand` (`brand_id`),
  KEY `idx_product_seller` (`seller_id`),
  KEY `idx_product_status` (`approval_status`),
  KEY `FKappm930ygdfv4qkkhc05pbr5s` (`subcategory_id`),
  CONSTRAINT `FKa3a4mpsfdf4d2y6r8ra3sc8mv` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`),
  CONSTRAINT `FKappm930ygdfv4qkkhc05pbr5s` FOREIGN KEY (`subcategory_id`) REFERENCES `subcategories` (`id`),
  CONSTRAINT `FKbgw3lyxhsml3kfqnfr45o0vbj` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'APPROVED','2026-08-20 11:35:30.461451',2,'TATA Tiscon 550D is India\'s first GreenPro certified rebar. Made with superior virgin iron ore and primary steelmaking technology. Exceptional ductility, higher bendability, and superior seismic resistance for all residential, commercial and bridge infrastructure.',18.00,'72142090',_binary '',1,65000.00,'TATA Tiscon 550D TMT Bar',61500.00,'TATA-TISCON-550D-12MM','tata-tiscon-550d-tmt-bar','{\"Grade\":\"Fe 550D\",\"Diameter\":\"12mm (also available 8mm, 10mm, 16mm, 20mm, 25mm, 32mm)\",\"Standard\":\"IS 1786:2008\",\"Carbon Content\":\"0.25% Max\",\"Yield Strength\":\"550 N/mm┬▓ Min\",\"Elongation\":\"14.5% Min\",\"Certifications\":\"BIS Certified, GreenPro Certified\"}',20,'TON','2026-08-20 13:24:43.750414',1,1,5,1),(2,'APPROVED','2026-08-20 11:35:30.844501',1,'UltraTech Super is a finely blended Portland Pozzolana Cement engineered with micro-particles for denser, damp-proof concrete and superior surface finish.',28.00,'25232910',_binary '',50,420.00,'UltraTech Super Cement (PPC 50kg Bag)',380.00,'ULTRATECH-SUPER-PPC-50KG','ultratech-super-cement-ppc-50kg','{\"Bag Weight\":\"50 Kg\",\"Packaging\":\"HDPE Tamper-proof bag\",\"Setting Time (Initial)\":\"140 mins\",\"Setting Time (Final)\":\"240 mins\",\"Standard\":\"IS 1489 (Part 1)\"}',1200,'BAG','2026-08-20 11:35:30.844501',2,2,5,4),(3,'APPROVED','2026-08-20 11:35:31.128590',3,'High Density Polyethylene pipe manufactured from virgin grade raw materials. Resists chemical aggression, zero corrosion, and 50+ year operational lifespan.',18.00,'39172110',_binary '',50,145.00,'Astral Taurus PE 100 HDPE Pipe 63mm PN 10',120.00,'ASTRAL-HDPE-63MM-PN10','astral-taurus-hdpe-pipe-63mm-pn10','{\"Outside Diameter\":\"63 mm\",\"Pressure Rating\":\"PN 10 (10 kgf/cm┬▓)\",\"Raw Material\":\"PE 100\",\"Coil Length\":\"100 Meters / 500 Meters\"}',3000,'METER','2026-08-20 11:35:31.128590',3,3,5,6);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_tokens`
--

DROP TABLE IF EXISTS `refresh_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `expiry_date` datetime(6) NOT NULL,
  `revoked` bit(1) NOT NULL,
  `token` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKghpmfn23vmxfu3spu3lfg4r2d` (`token`),
  KEY `idx_refresh_token_token` (`token`),
  KEY `idx_refresh_token_user` (`user_id`),
  CONSTRAINT `FK1lih5y2npsf8u5o3vhdb9y0os` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_tokens`
--

LOCK TABLES `refresh_tokens` WRITE;
/*!40000 ALTER TABLE `refresh_tokens` DISABLE KEYS */;
INSERT INTO `refresh_tokens` VALUES (1,'2026-08-20 11:40:23.440611','2026-09-19 11:40:23.440589',_binary '\0','8fed4dfd-4c66-43e2-928b-d5f8399e086e-1787226023438',4),(2,'2026-08-20 11:42:00.655071','2026-09-19 11:42:00.655065',_binary '\0','dc9fe9da-1130-40a7-b8ef-4769e03a0cee-1787226120655',4),(3,'2026-08-20 13:22:26.850233','2026-09-19 13:22:26.850207',_binary '\0','dd226181-3815-4993-8fd1-b1348811cda8-1787232146848',4),(4,'2026-08-20 13:22:46.794914','2026-09-19 13:22:46.794907',_binary '\0','dee870ec-cc9f-44f6-b264-9a6eb0de6e39-1787232166794',2),(5,'2026-08-20 13:23:03.559395','2026-09-19 13:23:03.559389',_binary '\0','d34d4291-d72e-4be8-8c99-da53f65eaf9a-1787232183559',2),(6,'2026-08-20 13:23:11.009052','2026-09-19 13:23:11.009047',_binary '\0','3628d47f-de9d-44ce-9121-91ca56483767-1787232191009',2),(7,'2026-08-20 13:23:18.823982','2026-09-19 13:23:18.823977',_binary '\0','26b49ffc-de9e-49f3-a540-e1f7a131ec8b-1787232198823',2),(8,'2026-08-20 13:23:27.784449','2026-09-19 13:23:27.784443',_binary '\0','9bdcad85-51d5-43c7-8de7-2624161164d7-1787232207784',4),(9,'2026-08-20 13:23:28.914043','2026-09-19 13:23:28.914037',_binary '\0','faa43361-3088-403a-ba91-9cac6fc1035a-1787232208914',5),(10,'2026-08-20 13:24:09.264660','2026-09-19 13:24:09.264654',_binary '\0','7848d74d-b527-4b41-809c-2dd732126993-1787232249264',4),(11,'2026-08-20 13:24:42.687661','2026-09-19 13:24:42.687657',_binary '\0','27b75c3c-76a1-477f-8034-bbcbcd4c4631-1787232282687',4),(12,'2026-08-20 13:24:50.915167','2026-09-19 13:24:50.915161',_binary '\0','693ad0c7-6233-416d-be98-865bbd09c204-1787232290915',4),(13,'2026-08-20 13:25:00.567277','2026-09-19 13:25:00.567270',_binary '\0','6e432c14-b3f5-48d8-82e5-e4ab44401688-1787232300567',5),(14,'2026-08-20 13:25:14.278132','2026-09-19 13:25:14.278125',_binary '\0','2cb0be47-c5a1-4098-aab4-b431a1142128-1787232314278',5),(15,'2026-08-20 13:25:14.767450','2026-09-19 13:25:14.767445',_binary '\0','6636c903-bd65-4798-90f7-ccbb5229cc86-1787232314767',4),(16,'2026-08-20 13:26:01.691748','2026-09-19 13:26:01.691743',_binary '\0','83920b04-1f1f-4e41-a5e4-4f76595b8809-1787232361691',2),(17,'2026-08-20 13:26:02.292781','2026-09-19 13:26:02.292775',_binary '\0','0e7cd64d-6d5d-42ee-9c49-ce43a2cdefb4-1787232362292',5),(18,'2026-08-20 13:26:02.859438','2026-09-19 13:26:02.859432',_binary '\0','538e4395-5efe-441a-a29f-2e3dc0945b28-1787232362859',4),(19,'2026-08-20 13:26:18.493739','2026-09-19 13:26:18.493733',_binary '\0','edb9286b-ba1e-465e-aada-ea3b9906757a-1787232378493',4),(20,'2026-08-20 13:26:19.068133','2026-09-19 13:26:19.068128',_binary '\0','ac3f5073-01d7-40f8-bee2-7afcf4b45390-1787232379068',5),(21,'2026-08-20 13:26:19.552360','2026-09-19 13:26:19.552354',_binary '\0','a4ee4c51-31cf-4d49-814a-b3a4a22b3350-1787232379552',2),(22,'2026-08-20 13:26:28.497710','2026-09-19 13:26:28.497705',_binary '\0','bf8aeaf1-be01-4829-85df-796c0bcbc5a3-1787232388497',2),(23,'2026-08-20 13:26:28.978859','2026-09-19 13:26:28.978853',_binary '\0','d2b2008d-c69c-4ec6-a39d-ad750f2fb379-1787232388978',4),(24,'2026-08-20 13:26:29.567701','2026-09-19 13:26:29.567695',_binary '\0','0699389e-b1d2-49a8-b3c8-b3198d7a436b-1787232389567',5),(25,'2026-08-20 13:26:37.073109','2026-09-19 13:26:37.073103',_binary '\0','875afebb-2e63-48ba-94a5-b8c27119ca39-1787232397073',2),(26,'2026-08-20 13:26:44.469015','2026-09-19 13:26:44.469008',_binary '\0','22b52a3b-efbc-4ebc-8248-e695ebd02de7-1787232404468',4),(27,'2026-08-20 13:26:53.072142','2026-09-19 13:26:53.072136',_binary '\0','04db8121-b61b-4323-a34c-e0444700bf2e-1787232413072',5),(28,'2026-08-20 13:26:53.633574','2026-09-19 13:26:53.633569',_binary '\0','c58a270e-3181-4f5c-8268-029ecd41bad8-1787232413633',4),(29,'2026-08-20 13:27:01.435687','2026-09-19 13:27:01.435681',_binary '\0','047d5aa3-7241-4c54-99bd-45fc594d02a9-1787232421435',5),(30,'2026-08-20 13:27:09.574339','2026-09-19 13:27:09.574333',_binary '\0','02042a88-e6f1-4090-807d-7b25aac6925d-1787232429574',5),(31,'2026-08-20 13:27:16.767383','2026-09-19 13:27:16.767376',_binary '\0','44d492b2-567b-410d-904e-17e2441de8fc-1787232436767',4),(32,'2026-08-20 13:35:23.039625','2026-09-19 13:35:23.039619',_binary '\0','b7049485-8b51-48cf-b441-b394dceef108-1787232923039',4),(33,'2026-08-21 03:47:14.623600','2026-09-20 03:47:14.623593',_binary '\0','dd32c3b1-d385-485c-8529-a2d201a1c497-1787284034623',2),(34,'2026-08-21 03:47:15.201008','2026-09-20 03:47:15.201001',_binary '\0','75f93780-3b0a-43ef-ac3e-67ee602ec2eb-1787284035200',5),(35,'2026-08-21 03:47:16.679974','2026-09-20 03:47:16.679968',_binary '\0','67e5093c-bd73-41d6-9d81-e7f2c45e1f69-1787284036679',4),(36,'2026-08-21 03:47:17.157120','2026-09-20 03:47:17.157114',_binary '\0','19a641af-9303-4d6b-8620-ea9aa3e64906-1787284037157',4),(37,'2026-08-21 03:47:58.986668','2026-09-20 03:47:58.986663',_binary '\0','59d60b34-eb81-4593-a2f5-9688d21ffca3-1787284078986',2);
/*!40000 ALTER TABLE `refresh_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refunds`
--

DROP TABLE IF EXISTS `refunds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refunds` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(14,2) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `gateway_refund_id` varchar(100) DEFAULT NULL,
  `reason` text,
  `refund_number` varchar(60) NOT NULL,
  `refund_status` enum('FAILED','PENDING','PROCESSED') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `payment_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK2j6f6ugkfsth9ads7vl6lxblt` (`refund_number`),
  KEY `idx_refund_number` (`refund_number`),
  KEY `idx_refund_payment` (`payment_id`),
  KEY `idx_refund_order` (`order_id`),
  CONSTRAINT `FKpt9ic0j1y6xwlej99wnynvnpy` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`),
  CONSTRAINT `FKsk9rqm7f6y8b1g0qob018hdm7` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refunds`
--

LOCK TABLES `refunds` WRITE;
/*!40000 ALTER TABLE `refunds` DISABLE KEYS */;
/*!40000 ALTER TABLE `refunds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rfq_items`
--

DROP TABLE IF EXISTS `rfq_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rfq_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_name` varchar(200) NOT NULL,
  `quantity` int NOT NULL,
  `specifications` text,
  `target_price` decimal(12,2) DEFAULT NULL,
  `unit` enum('BAG','BOX','BUNDLE','FEET','KG','LITER','METER','PIECE','ROLL','SET','SQ_FT','TON') NOT NULL,
  `product_id` bigint DEFAULT NULL,
  `rfq_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnfkcbf9murxflfcj4icxh3gbl` (`product_id`),
  KEY `FKpfpagtftvdx2nqs64tkx88ntm` (`rfq_id`),
  CONSTRAINT `FKnfkcbf9murxflfcj4icxh3gbl` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKpfpagtftvdx2nqs64tkx88ntm` FOREIGN KEY (`rfq_id`) REFERENCES `rfqs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rfq_items`
--

LOCK TABLES `rfq_items` WRITE;
/*!40000 ALTER TABLE `rfq_items` DISABLE KEYS */;
INSERT INTO `rfq_items` VALUES (1,'TATA Tiscon 550D TMT Bar (12mm)',50,NULL,59000.00,'TON',NULL,2);
/*!40000 ALTER TABLE `rfq_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rfq_quotes`
--

DROP TABLE IF EXISTS `rfq_quotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rfq_quotes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `delivery_charge` decimal(10,2) DEFAULT NULL,
  `delivery_days` int DEFAULT NULL,
  `gst_percentage` decimal(5,2) NOT NULL,
  `payment_terms` varchar(150) DEFAULT NULL,
  `price` decimal(14,2) NOT NULL,
  `remarks` text,
  `status` enum('ACCEPTED','EXPIRED','REJECTED','SUBMITTED') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `valid_until` datetime(6) DEFAULT NULL,
  `rfq_id` bigint NOT NULL,
  `seller_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_quote_rfq` (`rfq_id`),
  KEY `idx_quote_seller` (`seller_id`),
  KEY `idx_quote_status` (`status`),
  CONSTRAINT `FK54oken1dkukbmwj7xdmc7d1ak` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKs6mwupptho6kugqd940pb158v` FOREIGN KEY (`rfq_id`) REFERENCES `rfqs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rfq_quotes`
--

LOCK TABLES `rfq_quotes` WRITE;
/*!40000 ALTER TABLE `rfq_quotes` DISABLE KEYS */;
INSERT INTO `rfq_quotes` VALUES (1,'2026-08-20 11:35:31.577785',3500.00,3,18.00,'100% Against Dispatch / RTGS',58500.00,'Ex-stock available at Kalamboli yard. Test certificate with heat numbers included.','SUBMITTED','2026-08-20 11:35:31.577785','2026-08-30 11:35:31.576312',1,5),(2,'2026-08-20 13:27:10.266170',0.00,NULL,18.00,NULL,2925000.00,NULL,'ACCEPTED','2026-08-20 13:27:18.840725','2026-08-30 18:00:00.000000',2,5);
/*!40000 ALTER TABLE `rfq_quotes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rfqs`
--

DROP TABLE IF EXISTS `rfqs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rfqs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `delivery_city` varchar(100) DEFAULT NULL,
  `delivery_location` text,
  `delivery_pincode` varchar(10) DEFAULT NULL,
  `delivery_timeline_days` int DEFAULT NULL,
  `notes` text,
  `required_by_date` varchar(30) DEFAULT NULL,
  `rfq_number` varchar(50) NOT NULL,
  `status` enum('ACCEPTED','CLOSED','EXPIRED','IN_REVIEW','OPEN','QUOTED','REJECTED') NOT NULL,
  `title` varchar(200) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `buyer_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfmbwk1b7aiu831255dy7y7i2h` (`rfq_number`),
  KEY `idx_rfq_number` (`rfq_number`),
  KEY `idx_rfq_buyer` (`buyer_id`),
  KEY `idx_rfq_status` (`status`),
  CONSTRAINT `FKgecs65ln1jiif6d1sdphv39e4` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rfqs`
--

LOCK TABLES `rfqs` WRITE;
/*!40000 ALTER TABLE `rfqs` DISABLE KEYS */;
INSERT INTO `rfqs` VALUES (1,'2026-08-20 11:35:31.444179',NULL,NULL,'411057',5,'Urgent dispatch required to Hinjewadi site. Unloading facilities available at location. Mill test certificates and GST tax invoice mandatory.',NULL,'RFQ-2026-0001-DEMO','OPEN','Procurement of 50 Tons TMT Fe550D & 500 Bags Cement for Metro Site #7','2026-08-20 11:35:31.444179',4),(2,'2026-08-20 13:26:45.097451',NULL,NULL,'411057',5,'Unloading crane available.',NULL,'RFQ-405096-5EF7','CLOSED','50 Tons Fe-550D TMT Rebar for Commercial Mall Project','2026-08-20 13:27:19.098897',4);
/*!40000 ALTER TABLE `rfqs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seller_documents`
--

DROP TABLE IF EXISTS `seller_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seller_documents` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `document_number` varchar(100) DEFAULT NULL,
  `document_type` enum('BUSINESS_PROOF','CANCELLED_CHEQUE','GST_CERTIFICATE','MSME_CERTIFICATE','OTHER','PAN_CARD','TRADE_LICENSE') NOT NULL,
  `document_url` varchar(500) NOT NULL,
  `verification_status` enum('APPROVED','PENDING','REJECTED') NOT NULL,
  `verified_at` datetime(6) DEFAULT NULL,
  `seller_profile_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_doc_seller` (`seller_profile_id`),
  CONSTRAINT `FKksej650rqdp14923eow6oobmp` FOREIGN KEY (`seller_profile_id`) REFERENCES `seller_profiles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seller_documents`
--

LOCK TABLES `seller_documents` WRITE;
/*!40000 ALTER TABLE `seller_documents` DISABLE KEYS */;
INSERT INTO `seller_documents` VALUES (1,'2026-08-20 11:35:28.612057','27AAACT2727Q1ZW','GST_CERTIFICATE','https://images.unsplash.com/photo-1554224155-8d04cb21cd6c?w=600','APPROVED','2026-07-21 11:35:28.543725',1),(2,'2026-08-20 11:35:28.643814','AAACT2727Q','PAN_CARD','https://images.unsplash.com/photo-1554224154-26032ffc0d07?w=600','APPROVED','2026-07-21 11:35:28.543725',1),(3,'2026-08-20 11:35:28.694998','CHQ-981245','CANCELLED_CHEQUE','https://images.unsplash.com/photo-1554224155-6726b3ff858f?w=600','APPROVED','2026-07-21 11:35:28.543725',1),(4,'2026-08-20 11:35:28.728417','REG-MAH-2021-9988','BUSINESS_PROOF','https://images.unsplash.com/photo-1450133064473-71024230f91b?w=600','APPROVED','2026-07-21 11:35:28.543725',1),(5,'2026-08-20 11:35:28.944255','24AABCG1234F1Z1','GST_CERTIFICATE','https://images.unsplash.com/photo-1554224155-8d04cb21cd6c?w=600','PENDING',NULL,2),(6,'2026-08-20 11:35:28.994412','AABCG1234F','PAN_CARD','https://images.unsplash.com/photo-1554224154-26032ffc0d07?w=600','PENDING',NULL,2),(7,'2026-08-20 11:35:29.027722','CHQ-334129','CANCELLED_CHEQUE','https://images.unsplash.com/photo-1554224155-6726b3ff858f?w=600','PENDING',NULL,2),(8,'2026-08-20 11:35:29.078604','UDYAM-GJ-01-0012345','BUSINESS_PROOF','https://images.unsplash.com/photo-1450133064473-71024230f91b?w=600','PENDING',NULL,2);
/*!40000 ALTER TABLE `seller_documents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seller_profiles`
--

DROP TABLE IF EXISTS `seller_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seller_profiles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bank_account_name` varchar(150) DEFAULT NULL,
  `bank_account_number` varchar(35) DEFAULT NULL,
  `bank_ifsc_code` varchar(20) DEFAULT NULL,
  `bank_name` varchar(100) DEFAULT NULL,
  `business_type` varchar(50) DEFAULT NULL,
  `city` varchar(60) DEFAULT NULL,
  `company_name` varchar(150) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `gstin` varchar(20) DEFAULT NULL,
  `pan_number` varchar(20) DEFAULT NULL,
  `pincode` varchar(10) DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `rejection_reason` text,
  `state` varchar(60) DEFAULT NULL,
  `status` enum('APPROVED','PENDING','REJECTED','SUSPENDED','UNDER_REVIEW') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `verified_at` datetime(6) DEFAULT NULL,
  `warehouse_address` text,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK2264dwvu9q06u7388998fl3he` (`user_id`),
  KEY `idx_seller_status` (`status`),
  KEY `idx_seller_gstin` (`gstin`),
  CONSTRAINT `FKcpr5ibp9058g7a9u58wh7xf2y` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seller_profiles`
--

LOCK TABLES `seller_profiles` WRITE;
/*!40000 ALTER TABLE `seller_profiles` DISABLE KEYS */;
INSERT INTO `seller_profiles` VALUES (1,'Tata Steel Distribution Hub Pvt Ltd','0025102000012345','HDFC0000025','HDFC Bank','Authorized Direct Distributor','Navi Mumbai','Tata Steel Distribution Hub','2026-08-20 11:35:28.543725','27AAACT2727Q1ZW','AAACT2727Q','410218',4.9,NULL,'Maharashtra','APPROVED','2026-08-21 03:47:22.530772','2026-08-21 03:47:22.386601','Godown 12-B, Logistics Park, Kalamboli Steel Yard, Navi Mumbai',5),(2,'Gujarat Industrial Spares','919010045678901','UTIB0000919','Axis Bank','Wholesaler','Ahmedabad','Gujarat Industrial Spares & Tools','2026-08-20 11:35:28.894052','24AABCG1234F1Z1','AABCG1234F','382445',4.5,NULL,'Gujarat','PENDING','2026-08-20 11:35:28.894052',NULL,'GIDC Estate, Phase 3, Vatva, Ahmedabad',6);
/*!40000 ALTER TABLE `seller_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seller_stores`
--

DROP TABLE IF EXISTS `seller_stores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seller_stores` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` text,
  `banner` varchar(500) DEFAULT NULL,
  `business_email` varchar(150) DEFAULT NULL,
  `business_mobile` varchar(20) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` text,
  `gstin` varchar(30) DEFAULT NULL,
  `logo` varchar(500) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','SUSPENDED') NOT NULL,
  `store_name` varchar(150) NOT NULL,
  `store_slug` varchar(180) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `seller_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKh7kkyce3ivyue99le1yvpy8yh` (`store_slug`),
  UNIQUE KEY `UKnj1bjneiw8yq6i51e3b4779n6` (`seller_id`),
  KEY `idx_store_seller` (`seller_id`),
  KEY `idx_store_slug` (`store_slug`),
  KEY `idx_store_status` (`status`),
  CONSTRAINT `FKbg4hkux46uggatkdxuom83w0` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seller_stores`
--

LOCK TABLES `seller_stores` WRITE;
/*!40000 ALTER TABLE `seller_stores` DISABLE KEYS */;
INSERT INTO `seller_stores` VALUES (1,'Godown 12-B, Logistics Park, Kalamboli Steel Yard, Navi Mumbai','https://images.unsplash.com/photo-1587293852726-70cdb56c2866?w=1200','sales@tatasteelhub.com','9822012345','2026-08-20 11:35:31.511717','Official direct supply point for Tata Tiscon TMT rebars, structural sections, and high-tensile wire rods with mill test certifications.','27AAACT2727Q1ZW','https://images.unsplash.com/photo-1504917599217-d4dc5ebe6122?w=400','ACTIVE','Tata Steel Authorized Hub','tata-steel-hub','2026-08-20 11:35:31.511717',5);
/*!40000 ALTER TABLE `seller_stores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shipment_tracking`
--

DROP TABLE IF EXISTS `shipment_tracking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipment_tracking` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` text,
  `location` varchar(150) DEFAULT NULL,
  `status` enum('DELIVERED','FAILED_DELIVERY','IN_TRANSIT','OUT_FOR_DELIVERY','PENDING','PICKED_UP','PICKUP_SCHEDULED','REACHED_DESTINATION','RETURN_TO_ORIGIN') NOT NULL,
  `timestamp` datetime(6) NOT NULL,
  `shipment_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_tracking_shipment` (`shipment_id`),
  CONSTRAINT `FKcg3x538n3rgh19k45ibkkabps` FOREIGN KEY (`shipment_id`) REFERENCES `shipments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipment_tracking`
--

LOCK TABLES `shipment_tracking` WRITE;
/*!40000 ALTER TABLE `shipment_tracking` DISABLE KEYS */;
INSERT INTO `shipment_tracking` VALUES (1,'Shipment created by seller. Carrier pickup scheduled.','Origin Logistics Yard','PICKUP_SCHEDULED','2026-08-20 13:25:01.484798',1),(2,'Status updated to IN_TRANSIT','Pune Highway Toll Plaza Checkpoint','IN_TRANSIT','2026-08-20 13:25:15.411316',1);
/*!40000 ALTER TABLE `shipment_tracking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shipments`
--

DROP TABLE IF EXISTS `shipments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `actual_delivery_date` datetime(6) DEFAULT NULL,
  `awb_code` varchar(100) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `estimated_delivery_date` date DEFAULT NULL,
  `notes` text,
  `shipment_number` varchar(60) NOT NULL,
  `shipping_address` text,
  `shipping_label_url` varchar(500) DEFAULT NULL,
  `status` enum('DELIVERED','FAILED_DELIVERY','IN_TRANSIT','OUT_FOR_DELIVERY','PENDING','PICKED_UP','PICKUP_SCHEDULED','REACHED_DESTINATION','RETURN_TO_ORIGIN') NOT NULL,
  `tracking_number` varchar(100) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `delivery_partner_id` bigint DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `seller_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKil6gfafk66ly6rpmjugdmd1ne` (`shipment_number`),
  KEY `idx_shipment_number` (`shipment_number`),
  KEY `idx_shipment_tracking` (`tracking_number`),
  KEY `idx_shipment_order` (`order_id`),
  KEY `idx_shipment_seller` (`seller_id`),
  KEY `idx_shipment_status` (`status`),
  KEY `FK3f2iaqlhlxcvf45fi2djc4q24` (`delivery_partner_id`),
  CONSTRAINT `FK3f2iaqlhlxcvf45fi2djc4q24` FOREIGN KEY (`delivery_partner_id`) REFERENCES `delivery_partners` (`id`),
  CONSTRAINT `FKj05328merp9fb73umn3dupdji` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKrnt4wht95lxxplspltrg9681s` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipments`
--

LOCK TABLES `shipments` WRITE;
/*!40000 ALTER TABLE `shipments` DISABLE KEYS */;
INSERT INTO `shipments` VALUES (1,NULL,'AWB-VRL-998811','2026-08-20 13:25:01.426094','2026-08-25',NULL,'SHP-2026-2301424-D535','Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune','https://cdn.hinchmart.com/labels/vrl_998811.pdf','IN_TRANSIT','VRL-2026-998811','2026-08-20 13:25:15.704348',2,1,5);
/*!40000 ALTER TABLE `shipments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subcategories`
--

DROP TABLE IF EXISTS `subcategories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subcategories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `description` text,
  `image_url` varchar(500) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `slug` varchar(120) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `category_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_subcategory_slug` (`slug`),
  KEY `idx_subcategory_category` (`category_id`),
  CONSTRAINT `FKiborb6ptvy1t1n3v6klb56l5s` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcategories`
--

LOCK TABLES `subcategories` WRITE;
/*!40000 ALTER TABLE `subcategories` DISABLE KEYS */;
INSERT INTO `subcategories` VALUES (1,'2026-08-20 11:35:29.526788','High yield strength thermo-mechanically treated bars.','https://images.unsplash.com/photo-1504917599217-d4dc5ebe6122?w=400',_binary '','TMT Rebars (Fe 500D / 550D)','tmt-rebars','2026-08-20 11:35:29.526788',1),(2,'2026-08-20 11:35:29.577927','ISMB, ISMC heavy structural steel.','https://images.unsplash.com/photo-1535813547-99c456a41d4a?w=400',_binary '','Structural MS Channels & Beams','ms-channels-beams','2026-08-20 11:35:29.577927',1),(3,'2026-08-20 11:35:29.619619','GI and MS binding wires for rebar mesh reinforcement.','https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=400',_binary '','Binding Wires & Stirrups','binding-wires','2026-08-20 11:35:29.619619',1),(4,'2026-08-20 11:35:29.694165','Durable hydraulic cement for residential and commercial structures.','https://images.unsplash.com/photo-1589939705384-5185137a7f0f?w=400',_binary '','PPC Cement (Portland Pozzolana)','ppc-cement','2026-08-20 11:35:29.694165',2),(5,'2026-08-20 11:35:29.778334','High early strength cement for bridges, flyovers, and high-rise RCC.','https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=400',_binary '','OPC 53 Grade Cement','opc-53-grade','2026-08-20 11:35:29.778334',2),(6,'2026-08-20 11:35:29.912888','High density polyethylene piping for water distribution and gas.','https://images.unsplash.com/photo-1541888946425-d0fbb18f15f6?w=400',_binary '','HDPE Industrial Pressure Pipes','hdpe-pipes','2026-08-20 11:35:29.912888',3),(7,'2026-08-20 11:35:29.960848','Chlorinated polyvinyl chloride pipes for plumbing.','https://images.unsplash.com/photo-1607472586893-edb57bdc0e39?w=400',_binary '','CPVC Hot & Cold Water Pipes','cpvc-pipes','2026-08-20 11:35:29.960848',3),(8,'2026-08-20 11:35:30.061635','Multi-core copper and aluminium underground cables.','https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=400',_binary '','Armoured Heavy Duty Cables','armoured-cables','2026-08-20 11:35:30.061635',4),(9,'2026-08-20 11:35:30.160893','Industrial abrasive cutters and angle grinders.','https://images.unsplash.com/photo-1504148455328-c376907d081c?w=400',_binary '','Heavy Angle Grinders & Cutters','angle-grinders','2026-08-20 11:35:30.160893',5),(10,'2026-08-20 11:35:30.310938','Polymer modified cementitious coatings for terrace and basements.','https://images.unsplash.com/photo-1562259949-e8e7689d7828?w=400',_binary '','Liquid Waterproofing Membranes','waterproofing-membranes','2026-08-20 11:35:30.310938',7),(11,'2026-08-20 11:35:30.412182','Industrial safety helmets, harnesses and safety lanyards.','https://images.unsplash.com/photo-1578873375969-d652264e101b?w=400',_binary '','Head & Fall Protection PPE','head-fall-protection','2026-08-20 11:35:30.412182',8);
/*!40000 ALTER TABLE `subcategories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `email` varchar(150) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `role` enum('ADMIN','BUYER','SELLER','SUPER_ADMIN','SUPPORT') NOT NULL,
  `status` enum('ACTIVE','INACTIVE','PENDING_VERIFICATION','SUSPENDED') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKdu5v5sr43g5bfnji4vb8hg5s3` (`phone`),
  KEY `idx_user_email` (`email`),
  KEY `idx_user_phone` (`phone`),
  KEY `idx_user_role` (`role`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2026-08-20 11:35:27.843353','superadmin@hinchmart.com','Super Admin','$2a$10$YiRrADG/bzXKhbh3.OrJl.zXmep2CyyS2/H1xJWXROOQuttjqNCOi','9999999990','SUPER_ADMIN','ACTIVE','2026-08-20 11:35:27.843353'),(2,'2026-08-20 11:35:28.068242','admin@hinchmart.com','Platform Administrator','$2a$10$eIcZ6aaAieTrTqjz7PYOMuMpcpc.DwArOFVAJARRFEhYkRidCpOc6','9999999991','ADMIN','ACTIVE','2026-08-20 11:35:28.068242'),(3,'2026-08-20 11:35:28.176863','support@hinchmart.com','Customer Support Lead','$2a$10$b2/NTHHnYrigh.dgpLPILOVfEpfW2lVSMz14qBxjorHJYl7wgcJv.','9999999992','SUPPORT','ACTIVE','2026-08-20 11:35:28.176863'),(4,'2026-08-20 11:35:28.293027','buyer@demo.com','Rajesh Sharma','$2a$10$T2Z2OCpMPuAVEfdLy2fwDukrrfqRS9vUlnn3c.4IoCezmUwItE9fi','9876543210','BUYER','ACTIVE','2026-08-20 11:35:28.293027'),(5,'2026-08-20 11:35:28.495334','seller@tata.com','Anand Verma','$2a$10$49LayHvxTJkZmjSqt/GzwO1taLJm3KVEjiPDraFyfpP/tslSuPtcS','9822012345','SELLER','ACTIVE','2026-08-20 11:35:28.495334'),(6,'2026-08-20 11:35:28.843490','seller2@demo.com','Vikram Patel','$2a$10$ty6l5ecFLEtSast03dN.W.HfbQP9rhQ7atif2VfE.OmTyP/9SLsCS','9833098765','SELLER','ACTIVE','2026-08-20 11:35:28.843490');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-21 15:55:48
