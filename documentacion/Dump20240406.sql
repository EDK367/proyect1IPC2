-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: proyecto
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `administrador`
--

DROP TABLE IF EXISTS `administrador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrador` (
  `cui_admin` bigint NOT NULL,
  `nombre` char(100) NOT NULL,
  `apellido` char(100) NOT NULL,
  `correo` char(100) NOT NULL,
  `contraseña` char(100) NOT NULL,
  PRIMARY KEY (`cui_admin`),
  UNIQUE KEY `cui` (`cui_admin`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrador`
--

LOCK TABLES `administrador` WRITE;
/*!40000 ALTER TABLE `administrador` DISABLE KEYS */;
INSERT INTO `administrador` VALUES (284832448090,'Erikson','Denilson','denilmonterroso222@gmail.com','1234');
/*!40000 ALTER TABLE `administrador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adminpuntocontrol`
--

DROP TABLE IF EXISTS `adminpuntocontrol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `adminpuntocontrol` (
  `IDControl` int NOT NULL,
  `IDTrabjador` int NOT NULL,
  `cui_admin` bigint NOT NULL,
  `cui_operador` bigint NOT NULL,
  PRIMARY KEY (`IDControl`,`IDTrabjador`,`cui_admin`,`cui_operador`),
  KEY `administrado` (`IDControl`,`cui_operador`),
  KEY `administra` (`cui_admin`),
  CONSTRAINT `administra` FOREIGN KEY (`cui_admin`) REFERENCES `administrador` (`cui_admin`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `administrado` FOREIGN KEY (`IDControl`, `cui_operador`) REFERENCES `punto_control` (`IDControl`, `cui_operador`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adminpuntocontrol`
--

LOCK TABLES `adminpuntocontrol` WRITE;
/*!40000 ALTER TABLE `adminpuntocontrol` DISABLE KEYS */;
/*!40000 ALTER TABLE `adminpuntocontrol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bodega`
--

DROP TABLE IF EXISTS `bodega`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bodega` (
  `NoBodega` int NOT NULL,
  `cui_operador` bigint NOT NULL,
  PRIMARY KEY (`NoBodega`,`cui_operador`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bodega`
--

LOCK TABLES `bodega` WRITE;
/*!40000 ALTER TABLE `bodega` DISABLE KEYS */;
INSERT INTO `bodega` VALUES (1,12),(12,12),(45,123),(12345,12);
/*!40000 ALTER TABLE `bodega` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `nombre` char(100) NOT NULL,
  `apellido` char(100) NOT NULL,
  `NIT` int NOT NULL,
  `cui_operador` bigint NOT NULL,
  `cui_recepcionista` bigint NOT NULL,
  `telefono` int NOT NULL,
  PRIMARY KEY (`cui_operador`,`cui_recepcionista`,`NIT`),
  UNIQUE KEY `NIT` (`NIT`),
  CONSTRAINT `atiende` FOREIGN KEY (`cui_operador`, `cui_recepcionista`) REFERENCES `recepcionista` (`cui_operador`, `cui_recepcionista`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES ('Denilson','Orozco',123455,12,1,370484),('Ee','DD',12345,12,3,370484);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facturas`
--

DROP TABLE IF EXISTS `facturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facturas` (
  `No.Factura` int NOT NULL AUTO_INCREMENT,
  `cui_operador` bigint NOT NULL,
  `cui_recepcionista` bigint NOT NULL,
  `No.Pedido` int NOT NULL,
  `No.Bodega` int NOT NULL,
  PRIMARY KEY (`No.Factura`,`cui_operador`,`cui_recepcionista`,`No.Pedido`,`No.Bodega`),
  UNIQUE KEY `Attribute1` (`No.Factura`),
  KEY `hace` (`cui_operador`,`cui_recepcionista`),
  KEY `forma` (`No.Pedido`,`No.Bodega`),
  CONSTRAINT `forma` FOREIGN KEY (`No.Pedido`, `No.Bodega`) REFERENCES `pedido` (`NoPedido`, `NoBodega`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `hace` FOREIGN KEY (`cui_operador`, `cui_recepcionista`) REFERENCES `recepcionista` (`cui_operador`, `cui_recepcionista`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facturas`
--

LOCK TABLES `facturas` WRITE;
/*!40000 ALTER TABLE `facturas` DISABLE KEYS */;
/*!40000 ALTER TABLE `facturas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operador`
--

DROP TABLE IF EXISTS `operador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operador` (
  `cui_operador` bigint NOT NULL,
  `nombre` char(100) NOT NULL,
  `apellido` char(100) NOT NULL,
  `correo` char(100) NOT NULL,
  `contraseña` char(100) NOT NULL,
  PRIMARY KEY (`cui_operador`),
  UNIQUE KEY `cui` (`cui_operador`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operador`
--

LOCK TABLES `operador` WRITE;
/*!40000 ALTER TABLE `operador` DISABLE KEYS */;
INSERT INTO `operador` VALUES (12,'b','c','da','fa'),(123,'b','c','dd','f'),(123456,'b','c','d@gmail.com','f');
/*!40000 ALTER TABLE `operador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paquetes`
--

DROP TABLE IF EXISTS `paquetes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paquetes` (
  `Paquete` int NOT NULL AUTO_INCREMENT,
  `cui_operador` bigint NOT NULL,
  `cui_recepcionista` bigint NOT NULL,
  `NoPedido` int NOT NULL,
  `NoBodega` int NOT NULL,
  `NIT` int NOT NULL,
  `peso` float NOT NULL,
  `tarifaGlobal` tinyint NOT NULL,
  `total` float NOT NULL,
  `destino` varchar(100) NOT NULL,
  PRIMARY KEY (`Paquete`,`cui_operador`,`cui_recepcionista`,`NoPedido`,`NoBodega`,`NIT`),
  UNIQUE KEY `Attribute1` (`Paquete`),
  KEY `da` (`cui_operador`,`cui_recepcionista`,`NIT`),
  KEY `envia` (`NoPedido`,`NoBodega`),
  CONSTRAINT `envia` FOREIGN KEY (`NoPedido`, `NoBodega`) REFERENCES `pedido` (`NoPedido`, `NoBodega`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paquetes`
--

LOCK TABLES `paquetes` WRITE;
/*!40000 ALTER TABLE `paquetes` DISABLE KEYS */;
INSERT INTO `paquetes` VALUES (1,12,3,1,1,12345,10,1,30,'Xela'),(2,12,3,1,1,12345,11,1,30,'Xela'),(3,12,3,2,1,12345,10,1,55,'Xela'),(4,12,3,2,1,12345,10,1,100,'Xela'),(5,12,3,2,1,12345,10,1,100,'Xela'),(6,12,3,2,1,12345,10,0,60,'Xela');
/*!40000 ALTER TABLE `paquetes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido`
--

DROP TABLE IF EXISTS `pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido` (
  `NoPedido` int NOT NULL AUTO_INCREMENT,
  `NoBodega` int NOT NULL,
  `cui_operador` bigint NOT NULL,
  `estado` char(1) NOT NULL,
  PRIMARY KEY (`NoPedido`,`NoBodega`,`cui_operador`),
  UNIQUE KEY `No.Pedido` (`NoPedido`),
  KEY `llega` (`NoBodega`,`cui_operador`),
  CONSTRAINT `llega` FOREIGN KEY (`NoBodega`, `cui_operador`) REFERENCES `bodega` (`NoBodega`, `cui_operador`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
INSERT INTO `pedido` VALUES (1,1,12,'E'),(2,1,12,'E'),(3,45,123,'T');
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `punto_control`
--

DROP TABLE IF EXISTS `punto_control`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `punto_control` (
  `IDControl` int NOT NULL,
  `nombreControl` char(100) NOT NULL,
  `cui_operador` bigint NOT NULL,
  PRIMARY KEY (`IDControl`,`cui_operador`),
  UNIQUE KEY `IDControl` (`IDControl`),
  KEY `asignado` (`cui_operador`),
  CONSTRAINT `asignado` FOREIGN KEY (`cui_operador`) REFERENCES `operador` (`cui_operador`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `punto_control`
--

LOCK TABLES `punto_control` WRITE;
/*!40000 ALTER TABLE `punto_control` DISABLE KEYS */;
INSERT INTO `punto_control` VALUES (1,'Xela',12),(8,'Huehue',123),(9,'Huehue',123),(12,'Xela',12),(45,'Huehue',123),(123,'Guate',123),(1234,'Xela',12),(1285,'Xela',12),(9091,'San Juan',12),(12345,'Xela',12);
/*!40000 ALTER TABLE `punto_control` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recepcionista`
--

DROP TABLE IF EXISTS `recepcionista`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recepcionista` (
  `cui_recepcionista` bigint NOT NULL,
  `nombre` char(100) NOT NULL,
  `apellido` char(100) NOT NULL,
  `correo` char(100) NOT NULL,
  `contraseña` char(100) NOT NULL,
  `cui_operador` bigint NOT NULL,
  PRIMARY KEY (`cui_operador`,`cui_recepcionista`),
  UNIQUE KEY `cuiRecepcionista` (`cui_recepcionista`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recepcionista`
--

LOCK TABLES `recepcionista` WRITE;
/*!40000 ALTER TABLE `recepcionista` DISABLE KEYS */;
INSERT INTO `recepcionista` VALUES (1,'Erikson Denilson','Orozco Monterroso','denilmonterros@gmail.com','654321',12),(3,'Tete','adsf','adfaas','22',12);
/*!40000 ALTER TABLE `recepcionista` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ruta`
--

DROP TABLE IF EXISTS `ruta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ruta` (
  `IdRuta` int NOT NULL,
  `cantidadPuntoControl` int NOT NULL,
  `puntoInicio` varchar(100) NOT NULL,
  `puntoFin` varchar(100) NOT NULL,
  PRIMARY KEY (`IdRuta`),
  UNIQUE KEY `IDRuta` (`IdRuta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ruta`
--

LOCK TABLES `ruta` WRITE;
/*!40000 ALTER TABLE `ruta` DISABLE KEYS */;
INSERT INTO `ruta` VALUES (2,5,'San Juan','Guate'),(909089,10,'San Juan','Guate');
/*!40000 ALTER TABLE `ruta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rutascontrol`
--

DROP TABLE IF EXISTS `rutascontrol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rutascontrol` (
  `IDRuta` int NOT NULL,
  `IDControl` int NOT NULL,
  `idRutaControl` int NOT NULL,
  `cui_operador` bigint NOT NULL,
  PRIMARY KEY (`IDRuta`,`IDControl`,`idRutaControl`,`cui_operador`),
  KEY `pertenece` (`IDControl`,`cui_operador`),
  CONSTRAINT `pertenece` FOREIGN KEY (`IDControl`, `cui_operador`) REFERENCES `punto_control` (`IDControl`, `cui_operador`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tiene` FOREIGN KEY (`IDRuta`) REFERENCES `ruta` (`IdRuta`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rutascontrol`
--

LOCK TABLES `rutascontrol` WRITE;
/*!40000 ALTER TABLE `rutascontrol` DISABLE KEYS */;
/*!40000 ALTER TABLE `rutascontrol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tarifaglobal`
--

DROP TABLE IF EXISTS `tarifaglobal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tarifaglobal` (
  `IdTarifaGloblal` int NOT NULL AUTO_INCREMENT,
  `tarifaGlobal` float NOT NULL,
  `fechaCreacion` date NOT NULL,
  `cui_admin` bigint NOT NULL,
  PRIMARY KEY (`cui_admin`,`IdTarifaGloblal`),
  UNIQUE KEY `IdTarifaGloblal` (`IdTarifaGloblal`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tarifaglobal`
--

LOCK TABLES `tarifaglobal` WRITE;
/*!40000 ALTER TABLE `tarifaglobal` DISABLE KEYS */;
INSERT INTO `tarifaglobal` VALUES (1,5.5,'2023-05-02',284832448090),(3,5.5,'2023-05-02',284832448090),(4,10,'2023-05-02',284832448090);
/*!40000 ALTER TABLE `tarifaglobal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tarifalocal`
--

DROP TABLE IF EXISTS `tarifalocal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tarifalocal` (
  `IdTarifa` int NOT NULL AUTO_INCREMENT,
  `tarifaLocal` float NOT NULL,
  `IDControl` int NOT NULL,
  `cui_operador` bigint NOT NULL,
  PRIMARY KEY (`IDControl`,`cui_operador`,`IdTarifa`),
  UNIQUE KEY `IdTarifa` (`IdTarifa`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tarifalocal`
--

LOCK TABLES `tarifalocal` WRITE;
/*!40000 ALTER TABLE `tarifalocal` DISABLE KEYS */;
INSERT INTO `tarifalocal` VALUES (1,6,1,12),(3,8,8,123),(4,8,8,123);
/*!40000 ALTER TABLE `tarifalocal` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-06  4:09:10
