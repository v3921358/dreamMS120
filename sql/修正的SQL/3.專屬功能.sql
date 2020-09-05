SET FOREIGN_KEY_CHECKS=0;

-- --------------------------------------------------------

--
-- accounts_event[今日在線]
--

DROP TABLE IF EXISTS `accounts_event`;
CREATE TABLE `accounts_event` (

  `id` int(11) NOT NULL AUTO_INCREMENT,

  `accId` int(11) NOT NULL DEFAULT '0',

  `eventId` varchar(20) NOT NULL DEFAULT '',

  `count` int(11) NOT NULL DEFAULT '0',

  `type` int(11) NOT NULL DEFAULT '0',

  `updateTime` timestamp NULL DEFAULT NULL,

  PRIMARY KEY (`id`),

  KEY `accid` (`accId`),

  KEY `eventid` (`eventId`) USING BTREE

) ENGINE=InnoDB DEFAULT CHARSET=big5;

-- --------------------------------------------------------

--
-- accounts_exp[氣象系統]
--

DROP TABLE IF EXISTS `accounts_exp`;
CREATE TABLE `accounts_exp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `day` int(11) NOT NULL DEFAULT '0',
  `hour` int(11) NOT NULL DEFAULT '0',
  `Read` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=big5;

-- --------------------------------------------------------

--
-- Gachapon[轉蛋機]
--

DROP TABLE IF EXISTS `Gachapon`;
CREATE TABLE `Gachapon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `onlySelf` int(11) NOT NULL DEFAULT '0',
  `continent` int(11) NOT NULL DEFAULT '0',
  `itemid` int(11) NOT NULL DEFAULT '0',
  `chance` int(11) NOT NULL DEFAULT '0',
  `comments` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=big5;

-- --------------------------------------------------------

--
-- accounts_log[升級獎勵日誌]
--

DROP TABLE IF EXISTS `accounts_log`;
CREATE TABLE `accounts_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `accountid` int(10) unsigned NOT NULL,
  `characterid` int(10) unsigned NOT NULL,
  `bossid` varchar(20) NOT NULL,
  `lastattempt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=big5;