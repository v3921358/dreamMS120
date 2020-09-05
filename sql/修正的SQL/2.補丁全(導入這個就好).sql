SET FOREIGN_KEY_CHECKS=0;

-- --------------------------------------------------------

--
-- days_check_log[³sÄòÃ±¨ì]
--

DROP TABLE IF EXISTS `days_check_log`;
CREATE TABLE `days_check_log` (

  `id` int(11) NOT NULL AUTO_INCREMENT,

  `charid` int(11) NOT NULL,

  `times` int(1) NOT NULL DEFAULT '0',

  `lasttime` timestamp NULL DEFAULT '1999-01-01 00:00:00' ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
--  bosslog[¶i¶¥¤é»x]
--

DROP TABLE IF EXISTS `bosslog`;
CREATE TABLE `bosslog` (

  `id` int(10) NOT NULL AUTO_INCREMENT,

  `accountid` int(10) DEFAULT NULL,

  `characterid` int(10) unsigned NOT NULL,

  `bossid` varchar(20) NOT NULL,

  `count` int(10) NOT NULL DEFAULT '0',

  `type` int(10) NOT NULL DEFAULT '0',

  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),

  KEY `characterid` (`characterid`) USING BTREE,

  KEY `bossid` (`bossid`) USING BTREE

) ENGINE=MyISAM DEFAULT CHARSET=big5;

-- --------------------------------------------------------

--
-- suggest[¯d¨¥¨t²Î]
--

DROP TABLE IF EXISTS `suggest`;
CREATE TABLE `suggest` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `charid` varchar(20) NOT NULL,
  `text` text NOT NULL,
  `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `title` varchar(50) NOT NULL,
  `Reply` varchar(50) DEFAULT NULL,
PRIMARY KEY (`id`)


) ENGINE=InnoDB DEFAULT CHARSET=big5;

-- --------------------------------------------------------

--
-- notes[Â÷½u¨t²Î]
--

DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail` (
  `MessageID` int(10) NOT NULL AUTO_INCREMENT,
  `MailSender` varchar(13) NOT NULL,
  `MailReciever` varchar(13) NOT NULL,
  `Message` varchar(128) NOT NULL,
  `Read` tinyint(4) NOT NULL DEFAULT '0',
  `Deleted` tinyint(4) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`MessageID`)
) ENGINE=InnoDB DEFAULT CHARSET=big5;

-- --------------------------------------------------------

--
-- nxcode[§Ç¸¹]
--

DROP TABLE IF EXISTS `nxcode`;
CREATE TABLE `nxcode` (
  `code` varchar(20) NOT NULL,
  `valid` int(11) NOT NULL DEFAULT '1',
  `user` varchar(15) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `item` int(11) NOT NULL DEFAULT '10000',
  `size` int(11) NOT NULL DEFAULT '1',
  `time` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- lastmac[µù¥U]
--

  alter table `accounts` add `lastmac` tinytext NOT NULL;
  
-- --------------------------------------------------------

--
-- ¨g¯TÂàÂ¾©Ò»Ý­nªºª««~
--

INSERT INTO `drop_data` VALUES ('99999999', '9001013', '4032339', '1', '1', '21303', '999999');
  
-- --------------------------------------------------------

--
-- ·¬¸­ªZ¾¹±¼¸¨
--

-- 20
INSERT INTO `drop_data` VALUES ('1000000','4230114','1092030','1','1','0','500');-- ·¬¸­¤§¬Þ
-- 35
INSERT INTO `drop_data` VALUES ('1000001','1140100','1492020','1','1','0','500');-- ·¬¸­¤õºj
INSERT INTO `drop_data` VALUES ('1000002','5200002','1492020','1','1','0','500');-- ·¬¸­¤õºj
INSERT INTO `drop_data` VALUES ('1000003','3230405','1492020','1','1','0','500');-- ·¬¸­¤õºj
INSERT INTO `drop_data` VALUES ('1000004','5130101','1492020','1','1','0','500');-- ·¬¸­¤õºj
INSERT INTO `drop_data` VALUES ('1000005','2230101','1482020','1','1','0','500');-- ·¬¸­«üªê
INSERT INTO `drop_data` VALUES ('1000006','3230400','1482020','1','1','0','500');-- ·¬¸­«üªê
INSERT INTO `drop_data` VALUES ('1000007','4230116','1482020','1','1','0','500');-- ·¬¸­«üªê
INSERT INTO `drop_data` VALUES ('1000008','4230506','1482020','1','1','0','500');-- ·¬¸­«üªê
INSERT INTO `drop_data` VALUES ('1000009','5130104','1482020','1','1','0','500');-- ·¬¸­«üªê
INSERT INTO `drop_data` VALUES ('1000010','4230109','1452016','1','1','0','500');-- ·¬¸­¯«¤}
INSERT INTO `drop_data` VALUES ('1000011','4230113','1452016','1','1','0','500');-- ·¬¸­¯«¤}
INSERT INTO `drop_data` VALUES ('1000012','4090000','1452016','1','1','0','500');-- ·¬¸­¯«¤}
INSERT INTO `drop_data` VALUES ('1000013','2230102','1452016','1','1','0','500');-- ·¬¸­¯«¤}
INSERT INTO `drop_data` VALUES ('1000014','6230100','1452016','1','1','0','500');-- ·¬¸­¯«¤}
INSERT INTO `drop_data` VALUES ('1000015','2110200','1452016','1','1','0','500');-- ·¬¸­¯«¤}
INSERT INTO `drop_data` VALUES ('1000016','2230104','1452016','1','1','0','500');-- ·¬¸­¯«¤}
INSERT INTO `drop_data` VALUES ('1000017','4230106','1452016','1','1','0','500');-- ·¬¸­¯«¤}
INSERT INTO `drop_data` VALUES ('1000018','5120502','1452016','1','1','0','500');-- ·¬¸­¯«¤}
INSERT INTO `drop_data` VALUES ('1000019','4230101','1472030','1','1','0','500');-- ·¬¸­«ü®M
INSERT INTO `drop_data` VALUES ('1000020','4230102','1472030','1','1','0','500');-- ·¬¸­«ü®M
INSERT INTO `drop_data` VALUES ('1000021','7130100','1472030','1','1','0','500');-- ·¬¸­«ü®M
INSERT INTO `drop_data` VALUES ('1000022','2230101','1472030','1','1','0','500');-- ·¬¸­«ü®M
INSERT INTO `drop_data` VALUES ('1000023','3210200','1472030','1','1','0','500');-- ·¬¸­«ü®M
INSERT INTO `drop_data` VALUES ('1000024','7130200','1472030','1','1','0','500');-- ·¬¸­«ü®M
INSERT INTO `drop_data` VALUES ('1000025','2230110','1472030','1','1','0','500');-- ·¬¸­«ü®M
INSERT INTO `drop_data` VALUES ('1000026','5200001','1472030','1','1','0','500');-- ·¬¸­«ü®M
INSERT INTO `drop_data` VALUES ('1000027','4130101','1472030','1','1','0','500');-- ·¬¸­«ü®M
INSERT INTO `drop_data` VALUES ('1000028','5120506','1472030','1','1','0','500');-- ·¬¸­«ü®M
INSERT INTO `drop_data` VALUES ('1000029','3230200','1462014','1','1','0','500');-- ·¬¸­¥É©¸
INSERT INTO `drop_data` VALUES ('1000030','4230109','1462014','1','1','0','500');-- ·¬¸­¥É©¸
INSERT INTO `drop_data` VALUES ('1000031','2230102','1462014','1','1','0','500');-- ·¬¸­¥É©¸
INSERT INTO `drop_data` VALUES ('1000032','4230109','1302020','1','1','0','500');-- ·¬¸­¤§¼C
INSERT INTO `drop_data` VALUES ('1000033','3100101','1302020','1','1','0','500');-- ·¬¸­¤§¼C
INSERT INTO `drop_data` VALUES ('1000034','5100005','1302020','1','1','0','500');-- ·¬¸­¤§¼C
INSERT INTO `drop_data` VALUES ('1000035','7130300','1382009','1','1','0','500');-- ·¬¸­¤§§ú
INSERT INTO `drop_data` VALUES ('1000036','3000000','1382009','1','1','0','500');-- ·¬¸­¤§§ú
INSERT INTO `drop_data` VALUES ('1000037','3110100','1382009','1','1','0','500');-- ·¬¸­¤§§ú
INSERT INTO `drop_data` VALUES ('1000038','2230103','1382009','1','1','0','500');-- ·¬¸­¤§§ú
INSERT INTO `drop_data` VALUES ('1000039','3210100','1382009','1','1','0','500');-- ·¬¸­¤§§ú
INSERT INTO `drop_data` VALUES ('1000040','2100107','1382009','1','1','0','500');-- ·¬¸­¤§§ú
INSERT INTO `drop_data` VALUES ('1000041','4230502','1382009','1','1','0','500');-- ·¬¸­¤§§ú
INSERT INTO `drop_data` VALUES ('1000042','5120003','1382009','1','1','0','500');-- ·¬¸­¤§§ú
-- 43
INSERT INTO `drop_data` VALUES ('1000043','3000006','1492021','1','1','0','500');-- ·¬¸­¼É­·¤õºj
INSERT INTO `drop_data` VALUES ('1000044','3230102','1492021','1','1','0','500');-- ·¬¸­¼É­·¤õºj
INSERT INTO `drop_data` VALUES ('1000045','6300001','1492021','1','1','0','500');-- ·¬¸­¼É­·¤õºj
INSERT INTO `drop_data` VALUES ('1000046','6300100','1492021','1','1','0','500');-- ·¬¸­¼É­·¤õºj
INSERT INTO `drop_data` VALUES ('1000047','8140110','1492021','1','1','0','500');-- ·¬¸­¼É­·¤õºj
INSERT INTO `drop_data` VALUES ('1000048','4230500','1482021','1','1','0','500');-- ·¬¸­¼É­·«üªê
INSERT INTO `drop_data` VALUES ('1000049','4230116','1482021','1','1','0','500');-- ·¬¸­¼É­·«üªê
INSERT INTO `drop_data` VALUES ('1000050','6130208','1482021','1','1','0','500');-- ·¬¸­¼É­·«üªê
INSERT INTO `drop_data` VALUES ('1000051','7130501','1482021','1','1','0','500');-- ·¬¸­¼É­·«üªê
INSERT INTO `drop_data` VALUES ('1000052','7160000','1482021','1','1','0','500');-- ·¬¸­¼É­·«üªê
INSERT INTO `drop_data` VALUES ('1000053','3230400','1452022','1','1','0','500');-- ·¬¸­¤§¤}
INSERT INTO `drop_data` VALUES ('1000054','2230102','1452022','1','1','0','500');-- ·¬¸­¤§¤}
INSERT INTO `drop_data` VALUES ('1000055','6130100','1452022','1','1','0','500');-- ·¬¸­¤§¤}
INSERT INTO `drop_data` VALUES ('1000056','4090000','1452022','1','1','0','500');-- ·¬¸­¤§¤}
INSERT INTO `drop_data` VALUES ('1000057','5130100','1452022','1','1','0','500');-- ·¬¸­¤§¤}
INSERT INTO `drop_data` VALUES ('1000058','3210208','1452022','1','1','0','500');-- ·¬¸­¤§¤}
INSERT INTO `drop_data` VALUES ('1000059','6130204','1452022','1','1','0','500');-- ·¬¸­¤§¤}
INSERT INTO `drop_data` VALUES ('1000060','7130103','1452022','1','1','0','500');-- ·¬¸­¤§¤}
INSERT INTO `drop_data` VALUES ('1000061','8140000','1452022','1','1','0','500');-- ·¬¸­¤§¤}
INSERT INTO `drop_data` VALUES ('1000062','5200000','1452022','1','1','0','500');-- ·¬¸­¤§¤}
INSERT INTO `drop_data` VALUES ('1000063','2100104','1452022','1','1','0','500');-- ·¬¸­¤§¤}
INSERT INTO `drop_data` VALUES ('1000064','3210800','1452022','1','1','0','500');-- ·¬¸­¤§¤}
INSERT INTO `drop_data` VALUES ('1000065','4230121','1452022','1','1','0','500');-- ·¬¸­¤§¤}
INSERT INTO `drop_data` VALUES ('1000066','2230102','1472032','1','1','0','500');-- ·¬¸­®±®M
INSERT INTO `drop_data` VALUES ('1000067','1130100','1472032','1','1','0','500');-- ·¬¸­®±®M
INSERT INTO `drop_data` VALUES ('1000068','4230111','1472032','1','1','0','500');-- ·¬¸­®±®M
INSERT INTO `drop_data` VALUES ('1000069','4230112','1472032','1','1','0','500');-- ·¬¸­®±®M
INSERT INTO `drop_data` VALUES ('1000070','7130102','1472032','1','1','0','500');-- ·¬¸­®±®M
INSERT INTO `drop_data` VALUES ('1000071','3210100','1472032','1','1','0','500');-- ·¬¸­®±®M
INSERT INTO `drop_data` VALUES ('1000072','4130100','1472032','1','1','0','500');-- ·¬¸­®±®M
INSERT INTO `drop_data` VALUES ('1000073','3230306','1472032','1','1','0','500');-- ·¬¸­®±®M
INSERT INTO `drop_data` VALUES ('1000074','2100108','1472032','1','1','0','500');-- ·¬¸­®±®M
INSERT INTO `drop_data` VALUES ('1000075','6130203','1472032','1','1','0','500');-- ·¬¸­®±®M
INSERT INTO `drop_data` VALUES ('1000076','7130000','1472032','1','1','0','500');-- ·¬¸­®±®M
INSERT INTO `drop_data` VALUES ('1000077','8140002','1472032','1','1','0','500');-- ·¬¸­®±®M
INSERT INTO `drop_data` VALUES ('1000078','2230101','1472032','1','1','0','500');-- ·¬¸­®±®M
INSERT INTO `drop_data` VALUES ('1000079','5300100','1462019','1','1','0','500');-- ·¬¸­¤§©¸
INSERT INTO `drop_data` VALUES ('1000080','4230109','1462019','1','1','0','500');-- ·¬¸­¤§©¸
INSERT INTO `drop_data` VALUES ('1000081','4230110','1462019','1','1','0','500');-- ·¬¸­¤§©¸
INSERT INTO `drop_data` VALUES ('1000082','5400000','1462019','1','1','0','500');-- ·¬¸­¤§©¸
INSERT INTO `drop_data` VALUES ('1000083','4230505','1462019','1','1','0','500');-- ·¬¸­¤§©¸
INSERT INTO `drop_data` VALUES ('1000084','6230602','1462019','1','1','0','500');-- ·¬¸­¤§©¸
INSERT INTO `drop_data` VALUES ('1000085','7140000','1462019','1','1','0','500');-- ·¬¸­¤§©¸
INSERT INTO `drop_data` VALUES ('1000086','2230102','1332025','1','1','0','500');-- ·¬¸­µu¤M
INSERT INTO `drop_data` VALUES ('1000087','8140500','1332025','1','1','0','500');-- ·¬¸­µu¤M
INSERT INTO `drop_data` VALUES ('1000088','2230103','1332025','1','1','0','500');-- ·¬¸­µu¤M
INSERT INTO `drop_data` VALUES ('1000089','4230101','1332025','1','1','0','500');-- ·¬¸­µu¤M
INSERT INTO `drop_data` VALUES ('1000090','3100101','1332025','1','1','0','500');-- ·¬¸­µu¤M
INSERT INTO `drop_data` VALUES ('1000091','4230117','1332025','1','1','0','500');-- ·¬¸­µu¤M
INSERT INTO `drop_data` VALUES ('1000092','7130500','1332025','1','1','0','500');-- ·¬¸­µu¤M
INSERT INTO `drop_data` VALUES ('1000093','7130200','1332025','1','1','0','500');-- ·¬¸­µu¤M
INSERT INTO `drop_data` VALUES ('1000094','3000005','1302030','1','1','0','500');-- ·¬¸­¾Ô¼C
INSERT INTO `drop_data` VALUES ('1000095','4230102','1302030','1','1','0','500');-- ·¬¸­¾Ô¼C
INSERT INTO `drop_data` VALUES ('1000096','3110102','1302030','1','1','0','500');-- ·¬¸­¾Ô¼C
INSERT INTO `drop_data` VALUES ('1000097','4230124','1302030','1','1','0','500');-- ·¬¸­¾Ô¼C
INSERT INTO `drop_data` VALUES ('1000098','5150001','1302030','1','1','0','500');-- ·¬¸­¾Ô¼C
INSERT INTO `drop_data` VALUES ('1000099','6400000','1302030','1','1','0','500');-- ·¬¸­¾Ô¼C
INSERT INTO `drop_data` VALUES ('1000100','3110100','1442024','1','1','0','500');-- ·¬¸­¤§¥Ù
INSERT INTO `drop_data` VALUES ('1000101','4230105','1442024','1','1','0','500');-- ·¬¸­¤§¥Ù
INSERT INTO `drop_data` VALUES ('1000102','3230100','1442024','1','1','0','500');-- ·¬¸­¤§¥Ù
INSERT INTO `drop_data` VALUES ('1000103','4230300','1442024','1','1','0','500');-- ·¬¸­¤§¥Ù
INSERT INTO `drop_data` VALUES ('1000104','6130207','1442024','1','1','0','500');-- ·¬¸­¤§¥Ù
INSERT INTO `drop_data` VALUES ('1000105','7130002','1442024','1','1','0','500');-- ·¬¸­¤§¥Ù
INSERT INTO `drop_data` VALUES ('1000106','3230305','1432012','1','1','0','500');-- ·¬¸­¤§ºj
INSERT INTO `drop_data` VALUES ('1000107','6230300','1432012','1','1','0','500');-- ·¬¸­¤§ºj
INSERT INTO `drop_data` VALUES ('1000108','2230102','1432012','1','1','0','500');-- ·¬¸­¤§ºj
INSERT INTO `drop_data` VALUES ('1000109','3230200','1432012','1','1','0','500');-- ·¬¸­¤§ºj
INSERT INTO `drop_data` VALUES ('1000110','4230118','1432012','1','1','0','500');-- ·¬¸­¤§ºj
INSERT INTO `drop_data` VALUES ('1000111','6130201','1432012','1','1','0','500');-- ·¬¸­¤§ºj
INSERT INTO `drop_data` VALUES ('1000112','7130004','1432012','1','1','0','500');-- ·¬¸­¤§ºj
INSERT INTO `drop_data` VALUES ('1000113','3110102','1382012','1','1','0','500');-- ·¬¸­ªk§ú
INSERT INTO `drop_data` VALUES ('1000114','2110200','1382012','1','1','0','500');-- ·¬¸­ªk§ú
INSERT INTO `drop_data` VALUES ('1000115','5300001','1382012','1','1','0','500');-- ·¬¸­ªk§ú
INSERT INTO `drop_data` VALUES ('1000116','4230123','1382012','1','1','0','500');-- ·¬¸­ªk§ú
INSERT INTO `drop_data` VALUES ('1000117','7130600','1382012','1','1','0','500');-- ·¬¸­ªk§ú
INSERT INTO `drop_data` VALUES ('1000118','8140200','1382012','1','1','0','500');-- ·¬¸­ªk§ú
INSERT INTO `drop_data` VALUES ('1000119','2230102','1412011','1','1','0','500');-- ·¬¸­¾Ô©ò
INSERT INTO `drop_data` VALUES ('1000120','3210205','1412011','1','1','0','500');-- ·¬¸­¾Ô©ò
INSERT INTO `drop_data` VALUES ('1000121','7130001','1412011','1','1','0','500');-- ·¬¸­¾Ô©ò
INSERT INTO `drop_data` VALUES ('1000122','5140000','1412011','1','1','0','500');-- ·¬¸­¾Ô©ò
INSERT INTO `drop_data` VALUES ('1000123','7130601','1412011','1','1','0','500');-- ·¬¸­¾Ô©ò
INSERT INTO `drop_data` VALUES ('1000124','4230100','1422014','1','1','0','500');-- ·¬¸­¾Ôºl
INSERT INTO `drop_data` VALUES ('1000125','7130100','1422014','1','1','0','500');-- ·¬¸­¾Ôºl
INSERT INTO `drop_data` VALUES ('1000126','5130100','1422014','1','1','0','500');-- ·¬¸­¾Ôºl
INSERT INTO `drop_data` VALUES ('1000127','3000005','1422014','1','1','0','500');-- ·¬¸­¾Ôºl
INSERT INTO `drop_data` VALUES ('1000128','7130000','1422014','1','1','0','500');-- ·¬¸­¾Ôºl
INSERT INTO `drop_data` VALUES ('1000129','7130001','1422014','1','1','0','500');-- ·¬¸­¾Ôºl
INSERT INTO `drop_data` VALUES ('1000130','3230302','1422014','1','1','0','500');-- ·¬¸­¾Ôºl
INSERT INTO `drop_data` VALUES ('1000131','4230503','1422014','1','1','0','500');-- ·¬¸­¾Ôºl
INSERT INTO `drop_data` VALUES ('1000132','5130107','1422014','1','1','0','500');-- ·¬¸­¾Ôºl
INSERT INTO `drop_data` VALUES ('1000133','7130104','1422014','1','1','0','500');-- ·¬¸­¾Ôºl
INSERT INTO `drop_data` VALUES ('1000134','7130003','1422014','1','1','0','500');-- ·¬¸­¾Ôºl

-- --------------------------------------------------------

--
-- °Ó«°§Þ¯à®Ñ
--

-- [§Þ¯à®Ñ]¨g¤b­·¼É 20
INSERT INTO cashshop_modified_items (serial, showup,itemid,priority,period,gender,count,meso,discount_price,mark, unk_1, unk_2, unk_3, name) VALUES ( 50300192, 1, 0, -1, 0, 2, 0, 0, 250, 0, 0, 0, 0, '[§Þ¯à®Ñ]¨g¤b­·¼É 20');
-- [§Þ¯à®Ñ]Âù¤b±Û 20
INSERT INTO cashshop_modified_items (serial, showup,itemid,priority,period,gender,count,meso,discount_price,mark, unk_1, unk_2, unk_3, name) VALUES ( 50300193, 1, 0, -1, 0, 2, 0, 0, 360, 0, 0, 0, 0, '[§Þ¯à®Ñ]Âù¤b±Û 20');
-- [§Þ¯à®Ñ]´À¨­³N 30
INSERT INTO cashshop_modified_items (serial, showup,itemid,priority,period,gender,count,meso,discount_price,mark, unk_1, unk_2, unk_3, name) VALUES ( 50300194, 1, 0, -1, 0, 2, 0, 0, 750, 0, 0, 0, 0, '[§Þ¯à®Ñ]´À¨­³N 30');
-- [§Þ¯à®Ñ]µ¾ªÅ¸¨¸­±Ù 20
INSERT INTO cashshop_modified_items (serial, showup,itemid,priority,period,gender,count,meso,discount_price,mark, unk_1, unk_2, unk_3, name) VALUES ( 50300195, 1, 0, -1, 0, 2, 0, 0, 360, 0, 0, 0, 0, '[§Þ¯à®Ñ]µ¾ªÅ¸¨¸­±Ù 20');
-- [§Þ¯à®Ñ]Â©¤gÂà¥Í 30
INSERT INTO cashshop_modified_items (serial, showup,itemid,priority,period,gender,count,meso,discount_price,mark, unk_1, unk_2, unk_3, name) VALUES ( 50300196, 1, 0, -1, 0, 2, 0, 0, 750, 0, 0, 0, 0, '[§Þ¯à®Ñ]Â©¤gÂà¥Í 30');
-- [§Þ¯à®Ñ]¯ð´Æ¯S®Ä 30
INSERT INTO cashshop_modified_items (serial, showup,itemid,priority,period,gender,count,meso,discount_price,mark, unk_1, unk_2, unk_3, name) VALUES ( 50300197, 1, 0, -1, 0, 2, 0, 0, 750, 0, 0, 0, 0, '[§Þ¯à®Ñ]¯ð´Æ¯S®Ä 30');
-- [§Þ¯à®Ñ]¨g¤b­·¼É 20
INSERT INTO cashshop_modified_items (serial, showup,itemid,priority,period,gender,count,meso,discount_price,mark, unk_1, unk_2, unk_3, name) VALUES ( 50300201, 1, 0, -1, 0, 2, 0, 0, 20, 0, 0, 0, 0, '[§Þ¯à®Ñ]¨g¤b­·¼É 20');

-- --------------------------------------------------------

--
-- AddEquipOnlyId[°ß¤@ID]
--

alter table `csitems` add `equipOnlyId` int(15) DEFAULT '-1' NOT NULL;
alter table `inventoryitems` add `equipOnlyId` int(15) DEFAULT '-1' NOT NULL;
alter table `dueyitems` add `equipOnlyId` int(15) DEFAULT '-1' NOT NULL;
alter table `hiredmerchitems` add `equipOnlyId` int(15) DEFAULT '-1' NOT NULL;
alter table `hiredfishingitems` add `equipOnlyId` int(15) DEFAULT '-1' NOT NULL;