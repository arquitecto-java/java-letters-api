-- Create syntax for TABLE 'clientes'
CREATE TABLE `clientes` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fname` varchar(50) DEFAULT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `doc_id` varchar(12) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

-- Create syntax for TABLE 'terceros'
CREATE TABLE `terceros` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre_empresa` varchar(50) NOT NULL DEFAULT '',
  `nombres` varchar(50) DEFAULT NULL,
  `apellidos` varchar(50) DEFAULT NULL,
  `CC` int(11) DEFAULT NULL,
  `NIT` int(11) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `banco` varchar(50) DEFAULT NULL,
  `tipo_cuenta` varchar(11) DEFAULT NULL,
  `numero_cuenta` varchar(20) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(50) DEFAULT NULL,
  `ciudad` varchar(20) DEFAULT NULL,
  `creado` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

-- Create syntax for TABLE 'productos'
CREATE TABLE `productos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `proveedor_id` int(10) unsigned NOT NULL,
  `retailcompass_id` int(11) DEFAULT NULL,
  `sku` varchar(20) DEFAULT NULL,
  `nombre` varchar(250) DEFAULT NULL,
  `descripcion` varchar(2000) DEFAULT NULL,
  `marca` varchar(100) DEFAULT NULL,
  `cantidad_por_empaque` int(11) DEFAULT NULL,
  `ean` varchar(15) DEFAULT NULL,
  `categoria` varchar(100) DEFAULT NULL,
  `iva` int(11) DEFAULT NULL,
  `precio` int(11) DEFAULT NULL,
  `creado` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `estado` int(11) NOT NULL DEFAULT '0',
  `inventario` int(11) DEFAULT NULL,
  `imagen` varchar(250) DEFAULT NULL,
  `url` varchar(250) DEFAULT NULL,
  `is_parent` bit(1) NOT NULL DEFAULT '0',
  `entitled_item` int(11) DEFAULT NULL,
  `package_item` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `proveedor_sku` (`proveedor_id`,`sku`),
  CONSTRAINT `productos_tiendas_fk1` FOREIGN KEY (`proveedor_id`) REFERENCES `terceros` (`id`)
);

-- Create syntax for TABLE 'compras'
CREATE TABLE `compras` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `proveedor_id` int(11) unsigned NOT NULL,
  `valor_sin_iva` int(11) DEFAULT NULL,
  `iva` int(11) NOT NULL DEFAULT '0',
  `total` int(11) DEFAULT NULL,
  `factura` varchar(50) DEFAULT '',
  `creado` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `pagado` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `compras_terceros_fk1` FOREIGN KEY (`proveedor_id`) REFERENCES `terceros` (`id`)
);

-- Create syntax for TABLE 'detalles_compras'
CREATE TABLE `detalles_compras` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `compra_id` int(11) unsigned NOT NULL,
  `producto_id` int(11) unsigned DEFAULT NULL,
  `item` varchar(50) DEFAULT NULL,
  `cantidad` int(11) DEFAULT NULL,
  `precio_sin_iva` int(11) DEFAULT NULL,
  `iva` int(11) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `detalles_compras_compras_fk1` FOREIGN KEY (`compra_id`) REFERENCES `compras` (`id`),
  CONSTRAINT `detalles_compras_productos_fk1` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`)
);

-- Create syntax for TABLE 'empleados'
CREATE TABLE `empleados` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `persona_id` int(11) unsigned NOT NULL,
  `fecha_ingreso` date DEFAULT NULL,
  `salario` int(11) DEFAULT NULL,
  `creado` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `empleados_terceros_fk1` FOREIGN KEY (`persona_id`) REFERENCES `terceros` (`id`)
);

-- Create syntax for TABLE 'liquidaciones'
CREATE TABLE `liquidaciones` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fecha_inicio_periodo` date NOT NULL,
  `fecha_fin_periodo` date NOT NULL,
  `creado` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

-- Create syntax for TABLE 'detalles_liquidaciones'
CREATE TABLE `detalles_liquidaciones` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `liquidacion_id` int(10) unsigned NOT NULL,
  `empleado_id` int(11) unsigned NOT NULL,
  `dias_liquidados` int(11) DEFAULT NULL,
  `valor_liquidado` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `detalles_liquidaciones_empleados_fk1` FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`),
  CONSTRAINT `detalles_liquidaciones_liquidaciones_fk1` FOREIGN KEY (`liquidacion_id`) REFERENCES `liquidaciones` (`id`)
);

-- Create syntax for TABLE 'ventas'
CREATE TABLE `ventas` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cliente_id` int(10) unsigned NOT NULL,
  `subtotal` int(11) DEFAULT NULL,
  `descuento` int(11) NOT NULL,
  `valor_sin_iva` int(11) NOT NULL,
  `iva` int(11) NOT NULL,
  `total` int(11) NOT NULL,
  `efectivo` int(11) NOT NULL,
  `tarjeta` int(11) NOT NULL,
  `creado` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `ventas_clientes` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`)
);

-- Create syntax for TABLE 'detalles_ventas'
CREATE TABLE `detalles_ventas` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `venta_id` int(11) unsigned NOT NULL,
  `detalle_venta_real_id` int(11) unsigned,
  `orden` int(11) unsigned NOT NULL,
  `producto_id` int(11) unsigned NOT NULL,
  `cantidad` int(11) NOT NULL,
  `valor_sin_iva` int(11) NOT NULL,
  `iva` int(11) NOT NULL,
  `total` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `detalles_ventas_productos` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `detalles_ventas_ventas` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`)
);

-- Create syntax for TABLE 'matches'
CREATE TABLE `matches` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `producto_id` int(11) unsigned DEFAULT NULL,
  `match_id` int(11) unsigned DEFAULT NULL,
  `creado` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `matches_productos_fk1` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `matches_productos_fk2` FOREIGN KEY (`match_id`) REFERENCES `productos` (`id`)
);

-- Create syntax for TABLE 'pedidos'
CREATE TABLE `pedidos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `proveedor_id` int(11) DEFAULT NULL,
  `creado` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `solicitado` timestamp NULL DEFAULT NULL,
  `recibido` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- Create syntax for TABLE 'productos_h'
CREATE TABLE `productos_h` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `producto_id` int(10) unsigned NOT NULL,
  `precio` int(11) DEFAULT NULL,
  `precio_anterior` int(11) DEFAULT NULL,
  `estado` int(11) DEFAULT NULL,
  `estado_anterior` int(11) DEFAULT NULL,
  `inventario` int(11) DEFAULT NULL,
  `inventario_anterior` int(11) DEFAULT NULL,
  `actualizado` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado_anterior` timestamp NULL DEFAULT NULL,
  `detalle_venta_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `productos_h_productos` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create syntax for TABLE 'productos_pedidos'
CREATE TABLE `productos_pedidos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `pedido_id` int(11) NOT NULL,
  `producto_id` int(11) DEFAULT NULL,
  `cantidad` int(11) DEFAULT NULL,
  `precio` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- Create syntax for TABLE 'kits_productos'
CREATE TABLE `kits_productos` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `producto_kit_id` int(11) NOT NULL,
  `producto_id` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL DEFAULT '1',
  `creado` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);