CREATE DATABASE  IF NOT EXISTS `openpdv` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `openpdv`;
-- MySQL dump 10.13  Distrib 5.5.24, for osx10.5 (i386)
--
-- Host: 127.0.0.1    Database: openpdv
-- ------------------------------------------------------
-- Server version	5.5.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sis_usuario`
--

DROP TABLE IF EXISTS `sis_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sis_usuario` (
  `sis_usuario_id` int(11) NOT NULL AUTO_INCREMENT,
  `sis_usuario_login` varchar(40) NOT NULL,
  `sis_usuario_senha` varchar(40) NOT NULL,
  `sis_usuario_desconto` int(11) NOT NULL,
  `sis_usuario_ativo` bit(1) NOT NULL,
  `sis_usuario_caixa` bit(1) NOT NULL,
  `sis_usuario_gerente` bit(1) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`sis_usuario_id`),
  UNIQUE KEY `UK_sis_usuario_1` (`sis_usuario_login`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ibpt`
--

DROP TABLE IF EXISTS `ibpt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ibpt` (
  `ibpt_codigo` varchar(10) NOT NULL,
  `ibpt_ex` varchar(100) DEFAULT NULL,
  `ibpt_tabela` int(11) NOT NULL,
  `ibpt_descricao` varchar(500) NOT NULL,
  `ibpt_aliqNac` decimal(10,2) NOT NULL,
  `ibpt_aliqImp` decimal(10,2) NOT NULL,
  `ibpt_versao` varchar(5) DEFAULT NULL,
  `ead` varchar(260) NULL
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_impressora`
--

DROP TABLE IF EXISTS `ecf_impressora`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_impressora` (
  `ecf_impressora_id` int(11) NOT NULL AUTO_INCREMENT,
  `ecf_impressora_codigo` varchar(2) NOT NULL,
  `ecf_impressora_mfadicional` varchar(1) NOT NULL,
  `ecf_impressora_identificacao` varchar(6) NOT NULL,
  `ecf_impressora_serie` varchar(20) NOT NULL,
  `ecf_impressora_tipo` varchar(7) NOT NULL,
  `ecf_impressora_marca` varchar(20) NOT NULL,
  `ecf_impressora_modelo` varchar(20) NOT NULL,
  `ecf_impressora_caixa` int(3) NOT NULL,
  `ecf_impressora_ativo` bit(1) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_impressora_id`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_troca`
--

DROP TABLE IF EXISTS `ecf_troca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_troca` (
  `ecf_troca_id` int(11) NOT NULL AUTO_INCREMENT,
  `ecf_venda_id` int(11) DEFAULT NULL,
  `ecf_troca_cliente` varchar(14) NOT NULL,
  `ecf_troca_data` datetime NOT NULL,
  `ecf_troca_valor` decimal(10,2) NOT NULL,
  `ecf_troca_ecf` int(11) NOT NULL,
  `ecf_troca_coo` int(11) NOT NULL,
  `ecf_troca_ativo` bit(1) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_troca_id`),
  CONSTRAINT `FK_ecf_troca_1` FOREIGN KEY (`ecf_venda_id`) REFERENCES `ecf_venda` (`ecf_venda_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sis_municipio`
--

DROP TABLE IF EXISTS `sis_municipio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sis_municipio` (
  `sis_municipio_id` int(11) NOT NULL AUTO_INCREMENT,
  `sis_estado_id` int(11) NOT NULL,
  `sis_municipio_ibge` int(11) NOT NULL,
  `sis_municipio_descricao` varchar(100) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`sis_municipio_id`),
  UNIQUE KEY `UK_sis_municipio_1` (`sis_municipio_ibge`),
  KEY `FK_sis_municipio_1` (`sis_estado_id`),
  CONSTRAINT `FK_sis_municipio_1` FOREIGN KEY (`sis_estado_id`) REFERENCES `sis_estado` (`sis_estado_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_troca_produto`
--

DROP TABLE IF EXISTS `ecf_troca_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_troca_produto` (
  `ecf_troca_produto_id` int(11) NOT NULL AUTO_INCREMENT,
  `ecf_troca_id` int(11) NOT NULL,
  `prod_produto_id` int(11) NOT NULL,
  `prod_embalagem_id` int(11) NOT NULL,
  `ecf_troca_produto_barra` varchar(30) DEFAULT NULL,
  `ecf_troca_produto_quantidade` decimal(10,4) NOT NULL,
  `ecf_troca_produto_valor` decimal(10,2) NOT NULL,
  `ecf_troca_produto_total` decimal(10,2) NOT NULL,
  `ecf_troca_produto_ordem` int(11) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_troca_produto_id`),
  KEY `FK_ecf_troca_produto_1` (`ecf_troca_id`),
  KEY `FK_ecf_troca_produto_2` (`prod_produto_id`),
  KEY `FK_ecf_troca_produto_3` (`prod_embalagem_id`),
  CONSTRAINT `FK_ecf_troca_produto_1` FOREIGN KEY (`ecf_troca_id`) REFERENCES `ecf_troca` (`ecf_troca_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ecf_troca_produto_2` FOREIGN KEY (`prod_produto_id`) REFERENCES `prod_produto` (`prod_produto_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ecf_troca_produto_3` FOREIGN KEY (`prod_embalagem_id`) REFERENCES `prod_embalagem` (`prod_embalagem_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_z_totais`
--

DROP TABLE IF EXISTS `ecf_z_totais`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_z_totais` (
  `ecf_z_totais_id` int(11) NOT NULL AUTO_INCREMENT,
  `ecf_z_id` int(11) NOT NULL,
  `ecf_z_totais_codigo` varchar(7) NOT NULL,
  `ecf_z_totais_valor` decimal(13,2) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_z_totais_id`),
  UNIQUE KEY `UK_ecf_z_totais_1` (`ecf_z_id`,`ecf_z_totais_codigo`),
  CONSTRAINT `FK_z_totais_1` FOREIGN KEY (`ecf_z_id`) REFERENCES `ecf_z` (`ecf_z_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_pagamento`
--

DROP TABLE IF EXISTS `ecf_pagamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_pagamento` (
  `ecf_pagamento_id` int(11) NOT NULL AUTO_INCREMENT,
  `ecf_venda_id` int(11) NOT NULL,
  `ecf_pagamento_tipo_id` int(11) NOT NULL,
  `ecf_pagamento_gnf` int(11) NOT NULL,
  `ecf_pagamento_data` datetime NOT NULL,
  `ecf_pagamento_valor` decimal(12,2) NOT NULL,
  `ecf_pagamento_nsu` varchar(32) NOT NULL,
  `ecf_pagamento_estorno` char(1) NOT NULL,
  `ecf_pagamento_estorno_gnf` int(11) DEFAULT NULL,
  `ecf_pagamento_estorno_data` datetime DEFAULT NULL,
  `ecf_pagamento_estorno_valor` decimal(12,2) DEFAULT NULL,
  `ecf_pagamento_estorno_nsu` varchar(12) DEFAULT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_pagamento_id`),
  KEY `FK_ecf_pagamento_1` (`ecf_venda_id`),
  KEY `FK_ecf_pagamento_2` (`ecf_pagamento_tipo_id`),
  CONSTRAINT `FK_ecf_pagamento_1` FOREIGN KEY (`ecf_venda_id`) REFERENCES `ecf_venda` (`ecf_venda_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ecf_pagamento_2` FOREIGN KEY (`ecf_pagamento_tipo_id`) REFERENCES `ecf_pagamento_tipo` (`ecf_pagamento_tipo_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prod_composicao`
--

DROP TABLE IF EXISTS `prod_composicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prod_composicao` (
  `prod_composicao_id` int(11) NOT NULL AUTO_INCREMENT,
  `prod_produto_principal` int(11) NOT NULL,
  `prod_produto_id` int(11) NOT NULL,
  `prod_embalagem_id` int(11) NOT NULL,
  `prod_composicao_quantidade` decimal(10,4) NOT NULL,
  `prod_composicao_valor` decimal(10,2) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`prod_composicao_id`),
  KEY `FK_prod_composicao_1` (`prod_produto_principal`),
  KEY `FK_prod_composicao_2` (`prod_produto_id`),
  KEY `FK_prod_composicao_3` (`prod_embalagem_id`),
  CONSTRAINT `FK_prod_composicao_1` FOREIGN KEY (`prod_produto_principal`) REFERENCES `prod_produto` (`prod_produto_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_prod_composicao_2` FOREIGN KEY (`prod_produto_id`) REFERENCES `prod_produto` (`prod_produto_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_prod_composicao_3` FOREIGN KEY (`prod_embalagem_id`) REFERENCES `prod_embalagem` (`prod_embalagem_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prod_embalagem`
--

DROP TABLE IF EXISTS `prod_embalagem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prod_embalagem` (
  `prod_embalagem_id` int(11) NOT NULL AUTO_INCREMENT,
  `prod_embalagem_nome` varchar(6) NOT NULL,
  `prod_embalagem_descricao` varchar(100) NOT NULL,
  `prod_embalagem_unidade` int(11) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`prod_embalagem_id`),
  UNIQUE KEY `UK_prod_embalagem_1` (`prod_embalagem_nome`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prod_preco`
--

DROP TABLE IF EXISTS `prod_preco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prod_preco` (
  `prod_preco_id` int(11) NOT NULL AUTO_INCREMENT,
  `prod_produto_id` int(11) NOT NULL,
  `prod_embalagem_id` int(11) NOT NULL,
  `prod_preco_valor` decimal(10,2) NOT NULL,
  `prod_preco_barra` varchar(30) DEFAULT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`prod_preco_id`),
  KEY `FK_prod_preco_1` (`prod_produto_id`),
  KEY `FK_prod_preco_2` (`prod_embalagem_id`),
  CONSTRAINT `FK_prod_preco_1` FOREIGN KEY (`prod_produto_id`) REFERENCES `prod_produto` (`prod_produto_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_prod_preco_2` FOREIGN KEY (`prod_embalagem_id`) REFERENCES `prod_embalagem` (`prod_embalagem_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sis_estado`
--

DROP TABLE IF EXISTS `sis_estado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sis_estado` (
  `sis_estado_id` int(11) NOT NULL AUTO_INCREMENT,
  `sis_estado_ibge` int(11) NOT NULL,
  `sis_estado_descricao` varchar(100) NOT NULL,
  `sis_estado_sigla` char(2) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`sis_estado_id`),
  UNIQUE KEY `UK_sis_estado_1` (`sis_estado_ibge`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_nota_produto`
--

DROP TABLE IF EXISTS `ecf_nota_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_nota_produto` (
  `ecf_nota_produto_id` int(11) NOT NULL AUTO_INCREMENT,
  `ecf_nota_id` int(11) NOT NULL,
  `prod_produto_id` int(11) NOT NULL,
  `prod_embalagem_id` int(11) NOT NULL,
  `ecf_nota_produto_barra` varchar(30) DEFAULT NULL,
  `ecf_nota_produto_quantidade` decimal(10,4) NOT NULL,
  `ecf_nota_produto_bruto` decimal(10,2) NOT NULL,
  `ecf_nota_produto_desconto` decimal(10,2) NOT NULL,
  `ecf_nota_produto_liquido` decimal(10,2) NOT NULL,
  `ecf_nota_produto_icms` decimal(4,2) NOT NULL,
  `ecf_nota_produto_ipi` decimal(4,2) NOT NULL,
  `ecf_nota_produto_ordem` int(11) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_nota_produto_id`),
  KEY `FK_ecf_nota_produto_1` (`ecf_nota_id`),
  KEY `FK_ecf_nota_produto_2` (`prod_produto_id`),
  KEY `FK_ecf_nota_produto_3` (`prod_embalagem_id`),
  CONSTRAINT `FK_ecf_nota_produto_1` FOREIGN KEY (`ecf_nota_id`) REFERENCES `ecf_nota` (`ecf_nota_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ecf_nota_produto_2` FOREIGN KEY (`prod_produto_id`) REFERENCES `prod_produto` (`prod_produto_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ecf_nota_produto_3` FOREIGN KEY (`prod_embalagem_id`) REFERENCES `prod_embalagem` (`prod_embalagem_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_nota_eletronica`
--

DROP TABLE IF EXISTS `ecf_nota_eletronica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_nota_eletronica` (
  `ecf_nota_eletronica_id` int(11) NOT NULL AUTO_INCREMENT,
  `sis_cliente_id` int(11) DEFAULT NULL,
  `ecf_nota_eletronica_status` varchar(15) NOT NULL,
  `ecf_nota_eletronica_numero` int(11) NOT NULL,
  `ecf_nota_eletronica_data` datetime NOT NULL,
  `ecf_nota_eletronica_valor` decimal(10,2) NOT NULL,
  `ecf_nota_eletronica_chave` varchar(44) NOT NULL,
  `ecf_nota_eletronica_protocolo` varchar(15) NOT NULL,
  `ecf_nota_eletronica_icms` decimal(10,2) NOT NULL,
  `ecf_nota_eletronica_ipi` decimal(10,2) NOT NULL,
  `ecf_nota_eletronica_pis` decimal(10,2) NOT NULL,
  `ecf_nota_eletronica_cofins` decimal(10,2) NOT NULL,
  `ecf_nota_eletronica_xml` longtext NOT NULL,
  `ecf_nota_eletronica_protocolo_cancelado` varchar(15) NOT NULL,
  `ecf_nota_eletronica_xml_cancelado` text NOT NULL,
  `ecf_nota_eletronica_recibo` varchar(15) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_nota_eletronica_id`),
  UNIQUE KEY `UK_ecf_nota_eletronica_1` (`ecf_nota_eletronica_chave`),
  KEY `FK_ecf_nota_eletronica_1` (`sis_cliente_id`),
  CONSTRAINT `FK_ecf_nota_eletronica_1` FOREIGN KEY (`sis_cliente_id`) REFERENCES `sis_cliente` (`sis_cliente_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sis_empresa`
--

DROP TABLE IF EXISTS `sis_empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sis_empresa` (
  `sis_empresa_id` int(11) NOT NULL AUTO_INCREMENT,
  `sis_municipio_id` int(11) NOT NULL,
  `sis_empresa_razao` varchar(100) NOT NULL,
  `sis_empresa_fantasia` varchar(100) NOT NULL,
  `sis_empresa_cnpj` varchar(14) NOT NULL,
  `sis_empresa_ie` varchar(14) NOT NULL,
  `sis_empresa_im` varchar(14) NOT NULL,
  `sis_empresa_logradouro` varchar(100) NOT NULL,
  `sis_empresa_numero` int(11) NOT NULL,
  `sis_empresa_complemento` varchar(50) NOT NULL,
  `sis_empresa_bairro` varchar(50) NOT NULL,
  `sis_empresa_cep` varchar(8) NOT NULL,
  `sis_empresa_responsavel` varchar(50) NOT NULL,
  `sis_empresa_fone` varchar(10) NOT NULL,
  `sis_empresa_email` varchar(100) NOT NULL,
  `sis_empresa_contador` bit(1) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`sis_empresa_id`),
  KEY `FK_sis_empresa_1` (`sis_municipio_id`),
  CONSTRAINT `FK_sis_empresa_1` FOREIGN KEY (`sis_municipio_id`) REFERENCES `sis_municipio` (`sis_municipio_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_pagamento_totais`
--

DROP TABLE IF EXISTS `ecf_pagamento_totais`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_pagamento_totais` (
  `ecf_pagamento_totais_id` int(11) NOT NULL AUTO_INCREMENT,
  `ecf_pagamento_tipo_id` int(11) NOT NULL,
  `ecf_pagamento_totais_data` date NOT NULL,
  `ecf_pagamento_totais_documento` varchar(20) NOT NULL,
  `ecf_pagamento_totais_valor` decimal(12,2) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_pagamento_totais_id`),
  KEY `FK_ecf_pagamento_totais_1` (`ecf_pagamento_tipo_id`),
  CONSTRAINT `FK_ecf_pagamento_totais_1` FOREIGN KEY (`ecf_pagamento_tipo_id`) REFERENCES `ecf_pagamento_tipo` (`ecf_pagamento_tipo_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_venda_produto`
--

DROP TABLE IF EXISTS `ecf_venda_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_venda_produto` (
  `ecf_venda_produto_id` int(11) NOT NULL AUTO_INCREMENT,
  `ecf_venda_id` int(11) NOT NULL,
  `prod_embalagem_id` int(11) NOT NULL,
  `prod_produto_id` int(11) NOT NULL,
  `ecf_venda_produto_barra` varchar(30) DEFAULT NULL,
  `ecf_venda_produto_cst_cson` varchar(3) NOT NULL,
  `ecf_venda_produto_tributacao` char(1) NOT NULL,
  `ecf_venda_produto_icms` decimal(4,2) NOT NULL,
  `ecf_venda_produto_issqn` decimal(4,2) NOT NULL,
  `ecf_venda_produto_quantidade` decimal(10,4) NOT NULL,
  `ecf_venda_produto_bruto` decimal(10,2) NOT NULL,
  `ecf_venda_produto_desconto` decimal(10,2) NOT NULL,
  `ecf_venda_produto_acrescimo` decimal(10,2) NOT NULL,
  `ecf_venda_produto_liquido` decimal(10,2) NOT NULL,
  `ecf_venda_produto_total` decimal(10,2) NOT NULL,
  `ecf_venda_produto_cancelado` bit(1) NOT NULL,
  `ecf_venda_produto_ordem` int(11) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_venda_produto_id`),
  KEY `FK_ecf_venda_produto_1` (`ecf_venda_id`),
  KEY `FK_ecf_venda_produto_2` (`prod_embalagem_id`),
  KEY `FK_ecf_venda_produto_3` (`prod_produto_id`),
  CONSTRAINT `FK_ecf_venda_produto_1` FOREIGN KEY (`ecf_venda_id`) REFERENCES `ecf_venda` (`ecf_venda_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ecf_venda_produto_2` FOREIGN KEY (`prod_embalagem_id`) REFERENCES `prod_embalagem` (`prod_embalagem_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ecf_venda_produto_3` FOREIGN KEY (`prod_produto_id`) REFERENCES `prod_produto` (`prod_produto_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_venda`
--

DROP TABLE IF EXISTS `ecf_venda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_venda` (
  `ecf_venda_id` int(11) NOT NULL AUTO_INCREMENT,
  `sis_usuario_id` int(11) NOT NULL,
  `sis_vendedor_id` int(11) DEFAULT NULL,
  `sis_gerente_id` int(11) DEFAULT NULL,
  `sis_cliente_id` int(11) DEFAULT NULL,
  `ecf_impressora_id` int(11) NOT NULL,
  `ecf_z_id` int(11) DEFAULT NULL,
  `ecf_venda_ccf` int(6) NOT NULL,
  `ecf_venda_coo` int(6) NOT NULL,
  `ecf_venda_data` datetime NOT NULL,
  `ecf_venda_bruto` decimal(10,2) NOT NULL,
  `ecf_venda_desconto` decimal(10,2) NOT NULL,
  `ecf_venda_acrescimo` decimal(10,2) NOT NULL,
  `ecf_venda_liquido` decimal(10,2) NOT NULL,
  `ecf_venda_fechada` bit(1) NOT NULL,
  `ecf_venda_cancelada` bit(1) NOT NULL,
  `ecf_venda_sinc` bit(1) NOT NULL,
  `ecf_venda_observacao` varchar(255) DEFAULT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_venda_id`),
  KEY `FK_ecf_venda_1` (`sis_usuario_id`),
  KEY `FK_ecf_venda_2` (`ecf_z_id`),
  KEY `FK_ecf_venda_3` (`sis_cliente_id`),
  KEY `FK_ecf_venda_4` (`sis_vendedor_id`),
  KEY `FK_ecf_venda_5` (`sis_gerente_id`),
  KEY `FK_ecf_venda_6` (`ecf_impressora_id`),
  CONSTRAINT `FK_ecf_venda_1` FOREIGN KEY (`sis_usuario_id`) REFERENCES `sis_usuario` (`sis_usuario_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ecf_venda_2` FOREIGN KEY (`ecf_z_id`) REFERENCES `ecf_z` (`ecf_z_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ecf_venda_3` FOREIGN KEY (`sis_cliente_id`) REFERENCES `sis_cliente` (`sis_cliente_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ecf_venda_4` FOREIGN KEY (`sis_vendedor_id`) REFERENCES `sis_usuario` (`sis_usuario_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ecf_venda_5` FOREIGN KEY (`sis_gerente_id`) REFERENCES `sis_usuario` (`sis_usuario_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ecf_venda_6` FOREIGN KEY (`ecf_impressora_id`) REFERENCES `ecf_impressora` (`ecf_impressora_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sis_cliente`
--

DROP TABLE IF EXISTS `sis_cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sis_cliente` (
  `sis_cliente_id` int(11) NOT NULL AUTO_INCREMENT,
  `sis_municipio_id` int(11) NOT NULL,
  `sis_cliente_doc` varchar(14) NOT NULL,
  `sis_cliente_doc1` varchar(14) NOT NULL,
  `sis_cliente_nome` varchar(60) NOT NULL,
  `sis_cliente_endereco` varchar(60) NOT NULL,
  `sis_cliente_numero` int(11) NOT NULL,
  `sis_cliente_complemento` varchar(60) NOT NULL,
  `sis_cliente_bairro` varchar(60) NOT NULL,
  `sis_cliente_cep` varchar(8) NOT NULL,
  `sis_cliente_telefone` varchar(14) NOT NULL,
  `sis_cliente_email` varchar(100) NOT NULL,
  `sis_cliente_data` datetime NOT NULL,
  `sis_cliente_aniversario` date NULL,
  `sis_cliente_sinc` bit(1) NOT NULL,
  `sis_cliente_observacao` varchar(255) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`sis_cliente_id`),
  KEY `FK_sis_cliente_1` (`sis_municipio_id`),
  CONSTRAINT `FK_sis_cliente_1` FOREIGN KEY (`sis_municipio_id`) REFERENCES `sis_municipio` (`sis_municipio_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_z`
--

DROP TABLE IF EXISTS `ecf_z`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_z` (
  `ecf_z_id` int(11) NOT NULL AUTO_INCREMENT,
  `ecf_impressora_id` int(11) NOT NULL,
  `ecf_z_usuario` int(11) NOT NULL,
  `ecf_z_crz` int(11) NOT NULL,
  `ecf_z_coo_ini` int(11) NOT NULL,
  `ecf_z_coo_fin` int(11) NOT NULL,
  `ecf_z_cro` int(11) NOT NULL,
  `ecf_z_movimento` date NOT NULL,
  `ecf_z_emissao` datetime NOT NULL,
  `ecf_z_bruto` decimal(14,2) NOT NULL,
  `ecf_z_gt` decimal(14,2) NOT NULL,
  `ecf_z_issqn` bit(1) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_z_id`),
  KEY `FK_ecf_z_1` (`ecf_impressora_id`),
  CONSTRAINT `FK_ecf_z_1` FOREIGN KEY (`ecf_impressora_id`) REFERENCES `ecf_impressora` (`ecf_impressora_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_nota`
--

DROP TABLE IF EXISTS `ecf_nota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_nota` (
  `ecf_nota_id` int(11) NOT NULL AUTO_INCREMENT,
  `sis_cliente_id` int(11) DEFAULT NULL,
  `ecf_nota_serie` varchar(3) NOT NULL,
  `ecf_nota_subserie` varchar(3) NOT NULL,
  `ecf_nota_numero` int(11) NOT NULL,
  `ecf_nota_data` date NOT NULL,
  `ecf_nota_bruto` decimal(10,2) NOT NULL,
  `ecf_nota_desconto` decimal(10,2) NOT NULL,
  `ecf_nota_liquido` decimal(10,2) NOT NULL,
  `ecf_nota_pis` decimal(10,2) NOT NULL,
  `ecf_nota_cofins` decimal(10,2) NOT NULL,
  `ecf_nota_cancelada` bit(1) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_nota_id`),
  UNIQUE KEY `UK_ecf_nota_1` (`ecf_nota_serie`,`ecf_nota_subserie`,`ecf_nota_numero`),
  KEY `FK_ecf_nota_1` (`sis_cliente_id`),
  CONSTRAINT `FK_ecf_nota_1` FOREIGN KEY (`sis_cliente_id`) REFERENCES `sis_cliente` (`sis_cliente_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prod_produto`
--

DROP TABLE IF EXISTS `prod_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prod_produto` (
  `prod_produto_id` int(11) NOT NULL AUTO_INCREMENT,
  `prod_embalagem_id` int(11) NOT NULL,
  `prod_produto_ncm` varchar(8) NOT NULL,
  `prod_produto_barra` varchar(14) DEFAULT NULL,
  `prod_produto_descricao` varchar(120) NOT NULL,
  `prod_produto_referencia` varchar(60) NOT NULL,
  `prod_produto_preco` decimal(10,2) NOT NULL,
  `prod_produto_estoque` decimal(10,4) NOT NULL,
  `prod_produto_tipo` varchar(2) NOT NULL,
  `prod_produto_origem` char(1) NOT NULL,
  `prod_produto_cst_cson` varchar(3) NOT NULL,
  `prod_produto_tributacao` char(1) NOT NULL,
  `prod_produto_icms` decimal(4,2) NOT NULL,
  `prod_produto_issqn` decimal(4,2) NOT NULL,
  `prod_produto_iat` char(1) NOT NULL,
  `prod_produto_ippt` char(1) NOT NULL,
  `prod_produto_cadastrado` datetime NOT NULL,
  `prod_produto_alterado` datetime DEFAULT NULL,
  `prod_produto_ativo` bit(1) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`prod_produto_id`),
  KEY `FK_prod_produto_1` (`prod_embalagem_id`),
  KEY `IK_prod_produto_1` (`prod_produto_barra`),
  KEY `IK_prod_produto_2` (`prod_produto_descricao`),
  KEY `IK_prod_produto_3` (`prod_produto_referencia`),
  KEY `IK_prod_produto_4` (`prod_produto_preco`),
  KEY `IK_prod_produto_5` (`prod_produto_estoque`),
  KEY `IK_prod_produto_6` (`prod_produto_cadastrado`),
  KEY `IK_prod_produto_7` (`prod_produto_alterado`),
  KEY `IK_prod_produto_8` (`prod_produto_ativo`),
  CONSTRAINT `FK_prod_produto_1` FOREIGN KEY (`prod_embalagem_id`) REFERENCES `prod_embalagem` (`prod_embalagem_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prod_grade`
--

DROP TABLE IF EXISTS `prod_grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prod_grade` (
  `prod_grade_id` int(11) NOT NULL AUTO_INCREMENT,
  `prod_produto_id` int(11) NOT NULL,
  `prod_grade_barra` varchar(30) NOT NULL,
  `prod_grade_tamanho` varchar(50) NOT NULL,
  `prod_grade_cor` varchar(50) NOT NULL,
  `prod_grade_opcao` varchar(50) NOT NULL,
  `prod_grade_estoque` decimal(10,4) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`prod_grade_id`),
  KEY `FK_prod_grade_1` (`prod_produto_id`),
  CONSTRAINT `FK_prod_grade_1` FOREIGN KEY (`prod_produto_id`) REFERENCES `prod_produto` (`prod_produto_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_pagamento_parcela`
--

DROP TABLE IF EXISTS `ecf_pagamento_parcela`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_pagamento_parcela` (
  `ecf_pagamento_parcela_id` int(11) NOT NULL AUTO_INCREMENT,
  `ecf_pagamento_id` int(11) NOT NULL,
  `ecf_pagamento_parcela_data` date NOT NULL,
  `ecf_pagamento_parcela_valor` decimal(12,2) NOT NULL,
  `ecf_pagamento_parcela_nsu` varchar(32) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_pagamento_parcela_id`),
  KEY `FK_ecf_pagamento_parcela_1` (`ecf_pagamento_id`),
  CONSTRAINT `FK_ecf_pagamento_parcela_1` FOREIGN KEY (`ecf_pagamento_id`) REFERENCES `ecf_pagamento` (`ecf_pagamento_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_documento`
--

DROP TABLE IF EXISTS `ecf_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_documento` (
  `ecf_documento_id` int(11) NOT NULL AUTO_INCREMENT,
  `ecf_z_id` int(11) DEFAULT NULL,
  `ecf_impressora_id` int(11) NOT NULL,
  `ecf_documento_usuario` int(11) NOT NULL,
  `ecf_documento_coo` int(11) NOT NULL,
  `ecf_documento_gnf` int(11) NOT NULL,
  `ecf_documento_grg` int(11) NOT NULL,
  `ecf_documento_cdc` int(11) NOT NULL,
  `ecf_documento_tipo` varchar(2) NOT NULL,
  `ecf_documento_data` datetime NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_documento_id`),
  KEY `FK_ecf_documento_1` (`ecf_impressora_id`),
  CONSTRAINT `FK_ecf_documento_1` FOREIGN KEY (`ecf_impressora_id`) REFERENCES `ecf_impressora` (`ecf_impressora_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ecf_documento_2` FOREIGN KEY (`ecf_z_id`) REFERENCES `ecf_z` (`ecf_z_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ecf_pagamento_tipo`
--

DROP TABLE IF EXISTS `ecf_pagamento_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecf_pagamento_tipo` (
  `ecf_pagamento_tipo_id` int(11) NOT NULL AUTO_INCREMENT,
  `ecf_pagamento_tipo_codigo` varchar(2) NOT NULL,
  `ecf_pagamento_tipo_descricao` varchar(20) NOT NULL,
  `ecf_pagamento_tipo_tef` bit(1) NOT NULL,
  `ecf_pagamento_tipo_vinculado` bit(1) NOT NULL,
  `ecf_pagamento_tipo_debito` bit(1) NOT NULL,
  `ecf_pagamento_tipo_rede` varchar(20) NOT NULL,
  `ead` varchar(260) NULL,
  PRIMARY KEY (`ecf_pagamento_tipo_id`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;


/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-08-01 12:46:37
