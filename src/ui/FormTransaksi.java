package ui;

import db.Koneksi;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormTransaksi extends JFrame {

    JComboBox<String> cbCustomer;
    JTextField txtKode, txtQty, txtBayar;
    JTable table;
    DefaultTableModel model;
    JLabel lblTotal, lblKembali;

    double total = 0;

    public FormTransaksi() {
        setTitle("Kasir Penjualan");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // ================= TOP PANEL =================
        JPanel top = new JPanel(new GridBagLayout());
        top.setBorder(new TitledBorder("Input Transaksi"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbCustomer = new JComboBox<>();
        txtKode = new JTextField();
        txtQty = new JTextField("1");

        addField(top, gbc, 0, "Customer", cbCustomer);
        addField(top, gbc, 1, "Kode Barang", txtKode);
        addField(top, gbc, 2, "Qty", txtQty);

        JButton btnTambah = new JButton("➕ Tambah");
        gbc.gridx = 1;
        gbc.gridy = 3;
        top.add(btnTambah, gbc);

        add(top, BorderLayout.NORTH);

        // ================= TABLE =================
        model = new DefaultTableModel(
                new String[]{"ID","Nama","Harga","Qty","Subtotal"},0
        );
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ================= BOTTOM =================
        JPanel bottom = new JPanel(new GridLayout(3,2,10,10));
        bottom.setBorder(new TitledBorder("Pembayaran"));

        lblTotal = new JLabel("0");
        txtBayar = new JTextField();
        lblKembali = new JLabel("0");

        bottom.add(new JLabel("Total"));
        bottom.add(lblTotal);

        bottom.add(new JLabel("Bayar"));
        bottom.add(txtBayar);

        bottom.add(new JLabel("Kembali"));
        bottom.add(lblKembali);

        JButton btnProses = new JButton("💳 Proses Transaksi");
        add(bottom, BorderLayout.SOUTH);
        add(btnProses, BorderLayout.EAST);

        // ================= EVENT =================
        btnTambah.addActionListener(e -> tambahItem());
        btnProses.addActionListener(e -> prosesTransaksi());
        txtBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                hitungKembali();
            }
        });

        loadCustomer();

        setVisible(true);
    }

    void addField(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field){
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // ================= LOAD CUSTOMER =================
    void loadCustomer() {
        try {
            Connection conn = Koneksi.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM customer");

            while (rs.next()) {
                cbCustomer.addItem(rs.getInt("id_customer") + "-" + rs.getString("nama"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= TAMBAH ITEM =================
    void tambahItem() {
        try {
            Connection conn = Koneksi.getConnection();

            String sql = "SELECT * FROM barang WHERE kode_barang=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtKode.getText());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_barang");
                String nama = rs.getString("nama_barang");
                double harga = rs.getDouble("harga");
                int stok = rs.getInt("stok");
                int qty = Integer.parseInt(txtQty.getText());

                if (qty > stok) {
                    JOptionPane.showMessageDialog(null, "Stok tidak cukup!");
                    return;
                }

                double subtotal = harga * qty;

                model.addRow(new Object[]{id, nama, harga, qty, subtotal});

                hitungTotal();

            } else {
                JOptionPane.showMessageDialog(null, "Barang tidak ditemukan!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= HITUNG TOTAL =================
    void hitungTotal() {
        total = 0;
        for (int i=0; i<model.getRowCount(); i++) {
            total += (double) model.getValueAt(i,4);
        }
        lblTotal.setText(String.valueOf(total));
    }

    // ================= HITUNG KEMBALIAN =================
    void hitungKembali() {
        try {
            double bayar = Double.parseDouble(txtBayar.getText());
            double kembali = bayar - total;
            lblKembali.setText(String.valueOf(kembali));
        } catch (Exception e) {
            lblKembali.setText("0");
        }
    }

    // ================= PROSES TRANSAKSI =================
    void prosesTransaksi() {
        try {
            Connection conn = Koneksi.getConnection();
            conn.setAutoCommit(false);

            int idCustomer = Integer.parseInt(cbCustomer.getSelectedItem().toString().split("-")[0]);

            // simpan transaksi
            String sqlT = "INSERT INTO transaksi(tanggal,id_customer,total) VALUES(NOW(),?,?)";
            PreparedStatement psT = conn.prepareStatement(sqlT, Statement.RETURN_GENERATED_KEYS);
            psT.setInt(1, idCustomer);
            psT.setDouble(2, total);
            psT.executeUpdate();

            ResultSet rs = psT.getGeneratedKeys();
            rs.next();
            int idTrans = rs.getInt(1);

            // detail transaksi
            for (int i=0; i<model.getRowCount(); i++) {
                int idBarang = (int) model.getValueAt(i,0);
                int qty = (int) model.getValueAt(i,3);
                double subtotal = (double) model.getValueAt(i,4);

                PreparedStatement psD = conn.prepareStatement(
                        "INSERT INTO detail_transaksi(id_transaksi,id_barang,qty,subtotal) VALUES(?,?,?,?)"
                );
                psD.setInt(1, idTrans);
                psD.setInt(2, idBarang);
                psD.setInt(3, qty);
                psD.setDouble(4, subtotal);
                psD.executeUpdate();

                // update stok
                PreparedStatement psU = conn.prepareStatement(
                        "UPDATE barang SET stok=stok-? WHERE id_barang=?"
                );
                psU.setInt(1, qty);
                psU.setInt(2, idBarang);
                psU.executeUpdate();
            }

            conn.commit();

            JOptionPane.showMessageDialog(null, "Transaksi berhasil!");

            resetTransaksi();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void resetTransaksi() {
        model.setRowCount(0);
        total = 0;
        lblTotal.setText("0");
        txtBayar.setText("");
        lblKembali.setText("0");
    }
}