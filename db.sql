-- MySQL dump 10.15  Distrib 10.0.34-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: privacyDB
-- ------------------------------------------------------
-- Server version	10.0.34-MariaDB-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Conversation`
--

DROP TABLE IF EXISTS `Conversation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Conversation` (
  `idConversation` int(11) NOT NULL AUTO_INCREMENT,
  `convName` varchar(255) DEFAULT NULL,
  `creationDate` varchar(255) DEFAULT NULL,
  `lastDate` varchar(255) DEFAULT NULL,
  `exponent` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idConversation`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Conversation`
--

LOCK TABLES `Conversation` WRITE;
/*!40000 ALTER TABLE `Conversation` DISABLE KEYS */;
INSERT INTO `Conversation` VALUES (1,'First Conv','25/05/2018','12/06/2018 21:14',NULL),(2,'Second Conv','25/05/2018','26/05/2018 10:15',NULL),(3,'La 3eme Conv','03/15/2013','13/06/2018 02:56',NULL),(19,'ggg','13/06/2018','13/06/2018 17:39','65537');
/*!40000 ALTER TABLE `Conversation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Invitation`
--

DROP TABLE IF EXISTS `Invitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Invitation` (
  `idInvitation` int(11) NOT NULL AUTO_INCREMENT,
  `idUser` int(11) DEFAULT NULL,
  `idConversation` int(11) DEFAULT NULL,
  `isOK` tinyint(1) DEFAULT NULL,
  `idTarget` int(11) DEFAULT NULL,
  PRIMARY KEY (`idInvitation`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Invitation`
--

LOCK TABLES `Invitation` WRITE;
/*!40000 ALTER TABLE `Invitation` DISABLE KEYS */;
INSERT INTO `Invitation` VALUES (10,2,19,0,1);
/*!40000 ALTER TABLE `Invitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Message`
--

DROP TABLE IF EXISTS `Message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Message` (
  `idMessage` int(11) NOT NULL AUTO_INCREMENT,
  `idConversation` int(11) DEFAULT NULL,
  `idUser` int(11) DEFAULT NULL,
  `content` text,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`idMessage`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Message`
--

LOCK TABLES `Message` WRITE;
/*!40000 ALTER TABLE `Message` DISABLE KEYS */;
INSERT INTO `Message` VALUES (1,1,1,'Salut !','2018-05-25 10:00:34'),(2,1,2,'Salut ! Comment tu vas ?!','2018-05-25 10:45:21'),(3,1,1,'Tranquille, j\'ai des bonbons dans ma camionette...','2018-05-25 10:55:34'),(4,1,1,'Th','2018-05-25 11:59:56'),(5,1,1,'Test','2018-06-05 14:12:55'),(7,1,2,'Trpp','2018-06-05 14:57:20'),(8,3,2,'Salut ! ','2018-06-05 14:57:49'),(9,3,1,'','2018-06-05 14:59:20'),(10,3,1,'Salut l\'ami ','2018-06-05 14:59:33'),(11,3,2,'Coucou','2018-06-05 15:02:37'),(12,3,1,'','2018-06-05 15:03:09'),(13,3,1,'Salut salut ','2018-06-05 15:03:24'),(14,1,2,'Jtai','2018-06-05 15:03:57'),(15,1,1,'Pont','2018-06-05 15:04:12'),(16,3,2,'Je is ','2018-06-05 15:06:37'),(17,3,2,'I\'ve been e for a ','2018-06-05 15:06:39'),(18,3,2,'Ggzzu s','2018-06-05 15:06:40'),(24,1,1,'Trop','2018-06-06 13:18:54'),(25,1,1,'Test','2018-06-06 14:37:34'),(26,1,1,'Rhgfg','2018-06-06 14:37:49'),(46,1,2,'Yoyo','2018-06-10 21:12:57'),(47,1,1,'bien','2018-06-10 21:21:17'),(48,1,1,'bien','2018-06-10 21:22:44'),(57,1,2,'gggdddd','2018-06-11 19:01:53'),(58,1,2,'another one','2018-06-11 19:14:27'),(59,1,2,'another another one','2018-06-11 19:28:15'),(60,1,2,'coucou','2018-06-11 19:36:12'),(61,1,2,'another another one','2018-06-11 21:54:54'),(62,1,2,'this is a test','2018-06-11 22:22:07'),(63,1,2,'test agin','2018-06-11 23:02:15'),(64,1,1,'ça va','2018-06-11 23:23:25'),(65,1,1,'tu es la \n','2018-06-11 23:23:50'),(66,3,1,'oui','2018-06-11 23:24:06'),(67,3,1,'oiiii','2018-06-11 23:24:10'),(68,1,1,'ça va ','2018-06-11 23:24:27'),(69,1,2,'c\'est bon','2018-06-11 23:27:14'),(70,1,2,'je suis grosminet','2018-06-11 23:27:25'),(71,1,1,'Iyaizb ça va ou quoi ','2018-06-11 23:27:41'),(72,3,2,'sis ikkkis','2018-06-11 23:28:29'),(73,1,2,'oui','2018-06-11 23:28:40'),(74,3,2,'si mais c\'est long qui','2018-06-11 23:29:12'),(75,3,2,'quoi ','2018-06-11 23:29:16'),(76,3,1,'Kkkk','2018-06-11 23:30:20'),(77,1,2,'oui','2018-06-11 23:34:45'),(78,3,2,'oui','2018-06-11 23:34:56'),(79,3,1,'J\'ai reçu ','2018-06-11 23:36:39'),(80,1,1,'T\'as la notif ? ','2018-06-11 23:41:29'),(81,1,2,'c\'est moi','2018-06-11 23:43:32'),(82,1,2,'knnddksk','2018-06-11 23:46:31'),(83,1,2,'tu es moche gros','2018-06-11 23:55:50'),(84,1,2,'tu le sais ça ?!','2018-06-11 23:56:00'),(85,1,1,'C\'est toi le plus moche ','2018-06-11 23:56:33'),(86,3,1,'Tu as les notif ? ','2018-06-11 23:57:09'),(87,1,2,'MDR par contre il ne renvoit pas sur la bonne conv','2018-06-11 23:57:35'),(88,3,2,'oui','2018-06-11 23:57:41'),(89,3,2,'c\'est bon ça marche','2018-06-11 23:57:51'),(90,3,1,'Ah ouais il renvoie pas sur la bonne conv ','2018-06-11 23:58:22'),(91,3,2,'ah si c\'est bon :)','2018-06-11 23:58:52'),(92,3,2,'😂','2018-06-11 23:59:01'),(93,3,2,'😊😋😋😎☺☺😊😙😙😊','2018-06-11 23:59:05'),(94,3,2,'😎😍😍🤔☺😙😚🤕😷😷😕😌','2018-06-11 23:59:16'),(95,3,2,'_😄😄😄😄😄😄😄😄😄😄😄😃😃🤣🤣😃😄😃😎🤣😂😚😚☺😍😍🤔🙂😓😒😓😌😩😡😠😳','2018-06-11 23:59:34'),(96,3,2,'👿👿👿👿👿👿👿👿👿','2018-06-11 23:59:39'),(97,3,2,'👿👿👿👿👿👿👿👿😈😈','2018-06-11 23:59:45'),(98,3,2,'👿👿👿👿😈😈😈😈😈','2018-06-11 23:59:48'),(99,3,1,'Nan il renvoie pas sur la bonne conv si ? ','2018-06-12 00:00:41'),(100,3,2,'si il renvoi ','2018-06-12 00:01:17'),(101,3,2,'😃😃😃😃😃😃😃😃😃😃😃😃😃','2018-06-12 00:01:27'),(102,3,2,'😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃','2018-06-12 00:01:32'),(103,3,2,'😃😃😃😃😃😃😃😃😃😃😄😄😄😃😃😃😃','2018-06-12 00:01:36'),(104,3,2,'😃😃😃😃😃😃😃😃😃😃','2018-06-12 00:01:39'),(105,3,2,'😃😃😃😃😃😃😃😃','2018-06-12 00:01:42'),(106,3,2,'😃😃😃😃😃😃😃😃😃','2018-06-12 00:01:46'),(107,3,2,'😃😃😃😃😃😃','2018-06-12 00:01:50'),(108,3,2,'😅😅😅😅😅😅😅😅😅😅😅😅😅😅','2018-06-12 00:01:57'),(109,3,2,'😅😅😅😅😅😅😅😅😅','2018-06-12 00:02:00'),(110,3,2,'🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂🤗☺☺☺😎','2018-06-12 00:02:04'),(111,3,1,'Nan moi je tombe sur la première conv ','2018-06-12 00:02:08'),(112,3,2,'moi non ','2018-06-12 00:02:41'),(113,3,2,'je ne sais pas ','2018-06-12 00:02:50'),(114,3,2,'ujvvjjdddvdvdb','2018-06-12 00:02:59'),(115,3,2,'jbklllkbbbx','2018-06-12 00:03:03'),(116,3,2,'vvhjjjsyzgcz','2018-06-12 00:03:07'),(140,1,2,'heleobdodbdod this isba veruvlogb message that dont dit on one lane only','2018-06-12 21:06:47'),(141,1,2,'date','2018-06-12 21:13:44'),(142,1,2,'tte','2018-06-12 21:14:36'),(143,3,2,'3 conv','2018-06-12 21:15:03'),(144,3,1,'Test','2018-06-13 02:55:48'),(145,3,1,'Trr','2018-06-13 02:56:37');
/*!40000 ALTER TABLE `Message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MessageStatus`
--

DROP TABLE IF EXISTS `MessageStatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MessageStatus` (
  `idMessageStatus` int(11) NOT NULL AUTO_INCREMENT,
  `idMessage` int(11) DEFAULT NULL,
  `idUser` int(11) DEFAULT NULL,
  `unread` tinyint(1) DEFAULT NULL,
  `notified` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`idMessageStatus`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MessageStatus`
--

LOCK TABLES `MessageStatus` WRITE;
/*!40000 ALTER TABLE `MessageStatus` DISABLE KEYS */;
/*!40000 ALTER TABLE `MessageStatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Profile`
--

DROP TABLE IF EXISTS `Profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Profile` (
  `idProfile` int(11) NOT NULL AUTO_INCREMENT,
  `idUser` int(11) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idProfile`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Profile`
--

LOCK TABLES `Profile` WRITE;
/*!40000 ALTER TABLE `Profile` DISABLE KEYS */;
INSERT INTO `Profile` VALUES (1,1,'Loiseau','Titi','06.98.00.23.11'),(2,2,'Lechat','Grosminet','06.98.01.23.11');
/*!40000 ALTER TABLE `Profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `cookie` varchar(255) DEFAULT NULL,
  `pub_key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idUser`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'titi','463dfe4008d391a10dd6d1faaaa9fc83b9528392bc5930a28b271485df1418be','47df23e9321f778e16bbfa67993ca434dba30bff03175d4d51bbddcfc5421abe',NULL),(2,'grosminet','e64b0c8af7c39513dc33ec7a448b929a2504276058b3178563bae6fb2f1c581f','29ff03d9c2ce758aa9b51d5bc6bc0a2925adcfaa941b9fe67b48b29c1def13cf',NULL);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `linkConversation`
--

DROP TABLE IF EXISTS `linkConversation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `linkConversation` (
  `idLinkConversation` int(11) NOT NULL AUTO_INCREMENT,
  `idUser` int(11) DEFAULT NULL,
  `idConversation` int(11) DEFAULT NULL,
  `modulus` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`idLinkConversation`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `linkConversation`
--

LOCK TABLES `linkConversation` WRITE;
/*!40000 ALTER TABLE `linkConversation` DISABLE KEYS */;
INSERT INTO `linkConversation` VALUES (1,1,1,NULL),(2,2,1,NULL),(3,1,3,NULL),(4,2,3,NULL),(27,2,19,'22039286093676265378168488360988812742436880684763013967818791319347166184096748754028339759469763381206775326659442824330526443304571704772130572390842987569734921647042162185655278318150400512167526900589328448320340751132749031210984645543829517422426738172260622608221889630838913552210247326500422803446666495064694304973240453250376633193869525442626942314211027842698309124337858611311146933438452085609424970760224273390276899715056116540645980324661355081571552802934894531873704957747017907186938538496608397124500569725468990841367736588402068971427706470787906997076193110619977174959194849024959207806787'),(28,1,19,'');
/*!40000 ALTER TABLE `linkConversation` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `Friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Friend` (
  `idFriend` int(11) NOT NULL AUTO_INCREMENT,
  `idUser` int(11) DEFAULT NULL,
  `idUserFriend` int(11) DEFAULT NULL,
  PRIMARY KEY (`idFriend`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-13 18:00:30
