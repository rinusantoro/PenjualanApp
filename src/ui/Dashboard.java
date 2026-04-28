package ui;

import javax.swing.*;
import java.awt.*;
import util.ReportUtil;
import util.ChartUtil;


public class Dashboard extends JFrame {
    public Dashboard() {
        setTitle("Dashboard");
        setSize(400,300);
        setLayout(new FlowLayout());

        JButton btnBarang = new JButton("Data Barang");
        JButton btnCustomer = new JButton("Customer");
        JButton btnSupplier = new JButton("Supplier");
        JButton btnKasir = new JButton("Kasir");
        JButton btnInventory = new JButton("Inventory");
        JButton btnLaporan = new JButton("📄 Laporan Penjualan");
        JButton btnGrafik = new JButton("📊 Grafik Penjualan");
        JButton btnInv = new JButton("📦 Grafik Inventory");

        btnBarang.addActionListener(e -> new FormBarang());
        btnCustomer.addActionListener(e -> new FormCustomer());
        btnSupplier.addActionListener(e -> new FormSupplier());
        btnKasir.addActionListener(e -> new FormTransaksi());
        btnInventory.addActionListener(e -> new FormInventory());
        btnLaporan.addActionListener(e -> ReportUtil.laporanPenjualan());
        btnGrafik.addActionListener(e -> ChartUtil.grafikPenjualan());
        btnInv.addActionListener(e -> ChartUtil.grafikInventory());

        add(btnBarang);
        add(btnCustomer);
        add(btnSupplier);
        add(btnKasir);
        add(btnInventory);
        add(btnLaporan);
        add(btnGrafik);
        add(btnInv);

        setVisible(true);
    }
}