alter table `accounts` add `PayNT` int(11) DEFAULT '0' NOT NULL;

DROP TABLE IF EXISTS `accounts_PayNT`;
CREATE TABLE `accounts_PayNT` (
  `Order` varchar(17) NOT NULL,
  `Account` varchar(30) NOT NULL,
  `Name` varchar(30) NOT NULL,
  `Phone` varchar(17) NOT NULL,
  `PayNT` int(11) NOT NULL,
  `Player` varchar(30) NOT NULL,
  `Time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Valid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Order`)
) ENGINE=InnoDB DEFAULT CHARSET=big5;