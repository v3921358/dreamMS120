/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : tms120

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2017-10-17 17:21:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `drop_data_global`
-- ----------------------------
DROP TABLE IF EXISTS `drop_data_global`;
CREATE TABLE `drop_data_global` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `continent` int(11) NOT NULL,
  `dropType` tinyint(1) NOT NULL DEFAULT '0',
  `itemid` int(11) NOT NULL DEFAULT '0',
  `minimum_quantity` int(11) NOT NULL DEFAULT '1',
  `maximum_quantity` int(11) NOT NULL DEFAULT '1',
  `questid` int(11) NOT NULL DEFAULT '0',
  `chance` int(11) NOT NULL DEFAULT '0',
  `comments` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `mobid` (`continent`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of drop_data_global
-- ----------------------------
INSERT INTO `drop_data_global` VALUES ('1', '-1', '0', '4001126', '1', '5', '0', '40000', '楓葉');
INSERT INTO `drop_data_global` VALUES ('2', '0', '0', '4031365', '1', '1', '0', '10000', '楓彩票');
INSERT INTO `drop_data_global` VALUES ('3', '0', '0', '5220040', '1', '1', '0', '4000', '楓葉轉蛋券');
INSERT INTO `drop_data_global` VALUES ('4', '0', '0', '5220010', '1', '1', '0', '2000', '超級轉蛋券');
INSERT INTO `drop_data_global` VALUES ('6', '-1', '0', '2460000', '1', '1', '0', '6000', '放大鏡(下)');
INSERT INTO `drop_data_global` VALUES ('7', '-1', '0', '2460001', '1', '1', '0', '3000', '放大鏡(中)');
INSERT INTO `drop_data_global` VALUES ('8', '-1', '0', '2460002', '1', '1', '0', '1000', '放大鏡(上)');
INSERT INTO `drop_data_global` VALUES ('9', '-1', '0', '2460003', '1', '1', '0', '100', '放大鏡(最上級)');
