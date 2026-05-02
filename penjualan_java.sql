-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for penjualan_java
CREATE DATABASE IF NOT EXISTS `penjualan_java` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `penjualan_java`;

-- Dumping structure for table penjualan_java.barang
CREATE TABLE IF NOT EXISTS `barang` (
  `id_barang` int NOT NULL AUTO_INCREMENT,
  `kode_barang` varchar(20) DEFAULT NULL,
  `nama_barang` varchar(100) DEFAULT NULL,
  `harga` double DEFAULT NULL,
  `stok` int DEFAULT NULL,
  PRIMARY KEY (`id_barang`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table penjualan_java.barang: ~0 rows (approximately)
INSERT INTO `barang` (`id_barang`, `kode_barang`, `nama_barang`, `harga`, `stok`) VALUES
	(1, 'K01', 'Laptop Lenovo', 6000000, 9);

-- Dumping structure for table penjualan_java.customer
CREATE TABLE IF NOT EXISTS `customer` (
  `id_customer` int NOT NULL AUTO_INCREMENT,
  `nama` varchar(100) DEFAULT NULL,
  `alamat` text,
  `no_hp` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id_customer`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table penjualan_java.customer: ~0 rows (approximately)
INSERT INTO `customer` (`id_customer`, `nama`, `alamat`, `no_hp`) VALUES
	(1, 'Sonasa', 'Depok', '081214');

-- Dumping structure for table penjualan_java.detail_transaksi
CREATE TABLE IF NOT EXISTS `detail_transaksi` (
  `id_detail` int NOT NULL AUTO_INCREMENT,
  `id_transaksi` int DEFAULT NULL,
  `id_barang` int DEFAULT NULL,
  `qty` int DEFAULT NULL,
  `subtotal` double DEFAULT NULL,
  PRIMARY KEY (`id_detail`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table penjualan_java.detail_transaksi: ~0 rows (approximately)
INSERT INTO `detail_transaksi` (`id_detail`, `id_transaksi`, `id_barang`, `qty`, `subtotal`) VALUES
	(1, 1, 1, 1, 6000000);

-- Dumping structure for table penjualan_java.inventory
CREATE TABLE IF NOT EXISTS `inventory` (
  `id_inventory` int NOT NULL AUTO_INCREMENT,
  `tanggal` datetime DEFAULT NULL,
  `id_barang` int DEFAULT NULL,
  `jenis` enum('MASUK','KELUAR') DEFAULT NULL,
  `qty` int DEFAULT NULL,
  `keterangan` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_inventory`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table penjualan_java.inventory: ~0 rows (approximately)
INSERT INTO `inventory` (`id_inventory`, `tanggal`, `id_barang`, `jenis`, `qty`, `keterangan`) VALUES
	(1, '2026-05-02 11:53:03', 1, 'MASUK', 5, 'masuk');

-- Dumping structure for table penjualan_java.supplier
CREATE TABLE IF NOT EXISTS `supplier` (
  `id_supplier` int NOT NULL AUTO_INCREMENT,
  `nama_supplier` varchar(100) DEFAULT NULL,
  `alamat` text,
  `kontak` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id_supplier`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table penjualan_java.supplier: ~0 rows (approximately)
INSERT INTO `supplier` (`id_supplier`, `nama_supplier`, `alamat`, `kontak`) VALUES
	(1, 'Lenovo', 'Jakarta', '081111'),
	(2, 'Acer', 'Tangerang', '082222');

-- Dumping structure for table penjualan_java.transaksi
CREATE TABLE IF NOT EXISTS `transaksi` (
  `id_transaksi` int NOT NULL AUTO_INCREMENT,
  `tanggal` date DEFAULT NULL,
  `id_customer` int DEFAULT NULL,
  `total` double DEFAULT NULL,
  PRIMARY KEY (`id_transaksi`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table penjualan_java.transaksi: ~0 rows (approximately)
INSERT INTO `transaksi` (`id_transaksi`, `tanggal`, `id_customer`, `total`) VALUES
	(1, '2026-05-02', 1, 6000000);

-- Dumping structure for table penjualan_java.user
CREATE TABLE IF NOT EXISTS `user` (
  `id_user` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` enum('admin','kasir') DEFAULT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table penjualan_java.user: ~1 rows (approximately)
INSERT INTO `user` (`id_user`, `username`, `password`, `role`) VALUES
	(1, 'admin', '123', 'admin');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
