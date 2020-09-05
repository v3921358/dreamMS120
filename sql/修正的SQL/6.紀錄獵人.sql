DROP TABLE IF EXISTS `accounts_hunterEXP`;
CREATE TABLE `accounts_hunterEXP` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `expireFirst` bigint(20) NOT NULL DEFAULT '-1',
  `expireLast` bigint(20) NOT NULL DEFAULT '-1',
  `dc` varchar(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=big5;