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
INSERT INTO `administrador` VALUES (11,'Erikson','Orozco Monterroso','erikson@gmail.com','1234'),(22,'Denilson','Orozco','denilmonterroso@gmail.com','1234'),(33,'Carlos','Coyoy','coyoy@gmail.com','1234');
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
INSERT INTO `bodega` VALUES (1,21),(2,31),(3,41),(4,21),(5,41),(6,21),(7,21),(8,31);
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
INSERT INTO `cliente` VALUES ('Erikson','Orozco',28,21,1,37044884),('Erikson','Orozco',8,31,2,37044884),('Erikson','Orozco',35,31,2,37044884),('Denil','Mendez',18,41,3,34886),('Denil','Diaz',48,41,3,37044886);
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
INSERT INTO `operador` VALUES (21,'Juan','Diaz','juanito@gmail.com','abcd'),(31,'Pedro','Mendez','pedromendez@gmail.com','abcd'),(41,'Susi','Mendez','mendez@gmail.com','abcd');
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
INSERT INTO `paquetes` VALUES (1,21,1,1,1,28,5,1,50,'Xela'),(2,21,1,3,1,28,5,0,40,'San Juan'),(3,31,2,2,2,8,15,1,50,'Xela'),(4,31,2,4,2,48,1,1,35,'San Juan'),(5,21,1,5,1,8,52,1,135,'Solola');
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
INSERT INTO `pedido` VALUES (1,1,21,'E'),(2,2,31,'E'),(3,1,21,'S'),(4,2,31,'S'),(5,1,21,'S');
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
INSERT INTO `punto_control` VALUES (1,'Xela',21),(2,'San Juan',31),(3,'Huehue',41),(4,'Costa',21),(5,'Xela zona 1',41),(6,'Xela zona 6',21),(7,'San Juan 1',21),(8,'Solola',31),(9,'Peten',41);
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
INSERT INTO `recepcionista` VALUES (1,'Erikson Denilson','Orozco Monterroso','denilmonterros22@gmail.com','654321',21),(2,'Sosa','Soroy','sosasoro@gmail.com','123456',31),(3,'Deni','Soroy','denisoro@gmail.com','123456',41);
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
INSERT INTO `ruta` VALUES (1,4,'Xela','Peten'),(2,4,'Xela','Peten'),(3,4,'Xela','Peten'),(4,4,'Xela','Peten'),(5,4,'Xela','Peten');
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
INSERT INTO `tarifaglobal` VALUES (1,5.5,'2023-05-02',12);
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
INSERT INTO `tarifalocal` VALUES (1,6,1,21);
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

-- Dump completed on 2024-04-06  6:05:34
