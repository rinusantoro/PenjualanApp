package util;

import db.Koneksi;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.sql.Connection;
import java.sql.ResultSet;

public class ChartUtil {

    // ===================== GRAFIK PENJUALAN =====================
    public static void grafikPenjualan() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            Connection conn = Koneksi.getConnection();

            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT DATE(tanggal) AS tgl, SUM(total) AS total " +
                "FROM transaksi GROUP BY DATE(tanggal)"
            );

            while (rs.next()) {
                dataset.addValue(
                    rs.getDouble("total"),
                    "Penjualan",
                    rs.getString("tgl")
                );
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Grafik Penjualan",
                    "Tanggal",
                    "Total",
                    dataset
            );

            ChartFrame frame = new ChartFrame("Grafik Penjualan", chart);
            frame.setSize(600, 400);
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== GRAFIK INVENTORY =====================
    public static void grafikInventory() {
        try {
            DefaultPieDataset dataset = new DefaultPieDataset();
            Connection conn = Koneksi.getConnection();

            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT jenis, SUM(qty) AS total FROM inventory GROUP BY jenis"
            );

            while (rs.next()) {
                dataset.setValue(
                    rs.getString("jenis"),
                    rs.getInt("total")
                );
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Inventory",
                    dataset,
                    true,
                    true,
                    false
            );

            ChartFrame frame = new ChartFrame("Grafik Inventory", chart);
            frame.setSize(600, 400);
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}