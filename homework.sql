CREATE DATABASE  IF NOT EXISTS `homework` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `homework`;
-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: homework
-- ------------------------------------------------------
-- Server version	8.0.29

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
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_id` int NOT NULL AUTO_INCREMENT COMMENT '客户id',
  `customer_number` varchar(50) NOT NULL COMMENT '客户编码',
  `customer_name` varchar(50) NOT NULL COMMENT '客户名称',
  `customer_type` varchar(30) DEFAULT '暂无' COMMENT '客户类型',
  `email` varchar(300) DEFAULT '暂无' COMMENT '邮箱',
  `status` varchar(50) NOT NULL DEFAULT '有效' COMMENT '客户状态',
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customerlocation`
--

DROP TABLE IF EXISTS `customerlocation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customerlocation` (
  `location_id` int NOT NULL AUTO_INCREMENT COMMENT '地点id',
  `customer_id` int NOT NULL COMMENT '客户id',
  `address` varchar(20) NOT NULL COMMENT '客户收货地址',
  `phone` varchar(20) NOT NULL COMMENT '客户收货电话',
  PRIMARY KEY (`location_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `customerlocation_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客户地点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `item_id` int NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `item_name` varchar(300) NOT NULL COMMENT '商品名称',
  `uom` varchar(10) NOT NULL COMMENT '单位',
  `price` decimal(10,4) DEFAULT NULL COMMENT '参考单价',
  `status` varchar(50) NOT NULL DEFAULT '有效' COMMENT '商品状态',
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orderheader`
--

DROP TABLE IF EXISTS `orderheader`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderheader` (
  `order_id` int NOT NULL AUTO_INCREMENT COMMENT '订单头id',
  `order_number` varchar(50) NOT NULL COMMENT '订单编号',
  `customer_id` int NOT NULL COMMENT '客户id',
  `order_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单日期',
  `status` varchar(50) NOT NULL DEFAULT '登记' COMMENT '订单状态',
  PRIMARY KEY (`order_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `orderheader_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1100001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单头表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orderline`
--

DROP TABLE IF EXISTS `orderline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderline` (
  `line_id` int NOT NULL AUTO_INCREMENT COMMENT '订单行id',
  `order_id` int NOT NULL COMMENT '订单头id',
  `item_id` int NOT NULL COMMENT '商品id',
  `price` decimal(10,4) NOT NULL COMMENT '单价',
  `quantity` decimal(10,4) NOT NULL COMMENT '行数量',
  PRIMARY KEY (`line_id`),
  KEY `order_id` (`order_id`),
  KEY `item_id` (`item_id`),
  CONSTRAINT `orderline_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orderheader` (`order_id`),
  CONSTRAINT `orderline_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=500001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单行表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shipment`
--

DROP TABLE IF EXISTS `shipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipment` (
  `shipment_id` int NOT NULL AUTO_INCREMENT COMMENT '订单发货行id',
  `line_id` int NOT NULL COMMENT '订单行id',
  `address` varchar(200) NOT NULL COMMENT '客户收货地址',
  `phone` varchar(20) NOT NULL COMMENT '客户收货电话',
  `estimated_shipment_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '预计发货日期',
  `actual_shipment_date` datetime DEFAULT NULL COMMENT 's实际发货日期',
  `quantity` decimal(10,4) NOT NULL COMMENT '发货行数量',
  `status` varchar(50) NOT NULL DEFAULT '待发货' COMMENT '发货状态',
  PRIMARY KEY (`shipment_id`),
  KEY `line_id` (`line_id`),
  CONSTRAINT `shipment_ibfk_1` FOREIGN KEY (`line_id`) REFERENCES `orderline` (`line_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5100001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单发货行表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-08-26 16:10:41
