-- Create syntax for TABLE 'clientes'
CREATE TABLE `clientes` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fname` asdf(50) DEFAULT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `doc_id` varchar(12) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);