package ui;

import db.Koneksi;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormBarang extends JFrame {

    JTextField txtId, txtKode, txtNama, txtHarga, txtStok;
    JTable table;
    DefaultTableModel model;

    public FormBarang() {
        setTitle("Manajemen Data Barang");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // ================= PANEL FORM =================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Input Data Barang"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField();
        txtId.setEditable(false);
        txtKode = new JTextField();
        txtNama = new JTextField();
        txtHarga = new JTextField();
        txtStok = new JTextField();

        addField(formPanel, gbc, 0, "ID", txtId);
        addField(formPanel, gbc, 1, "Kode Barang", txtKode);
        addField(formPanel, gbc, 2, "Nama Barang", txtNama);
        addField(formPanel, gbc, 3, "Harga", txtHarga);
        addField(formPanel, gbc, 4, "Stok", txtStok);

        // ================= BUTTON =================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));

        JButton btnSimpan = new JButton("💾 Simpan");
        JButton btnUpdate = new JButton("✏️ Update");
        JButton btnHapus = new JButton("🗑 Hapus");
        JButton btnReset = new JButton("🔄 Reset");

        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnReset);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // ================= TABEL =================
        model = new DefaultTableModel(
                new String[]{"ID","Kode","Nama","Harga","Stok"},0
        );

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new TitledBorder("Data Barang"));

        add(scroll, BorderLayout.CENTER);

        // ================= EVENT =================
        btnSimpan.addActionListener(e -> simpan());
        btnUpdate.addActionListener(e -> update());
        btnHapus.addActionListener(e -> hapus());
        btnReset.addActionListener(e -> reset());

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                klikTabel();
            }
        });

        loadData();

        setVisible(true);
    }

    // ================= TAMBAH FIELD =================
    void addField(JPanel panel, GridBagConstraints gbc, int y, String label, JTextField field){
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.gridy = y;
        panel.add(field, gbc);
    }

    // ================= CRUD =================
    void simpan() {
        try {
            Connection conn = Koneksi.getConnection();
            String sql = "INSERT INTO barang(kode_barang,nama_barang,harga,stok) VALUES(?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtKode.getText());
            ps.setString(2, txtNama.getText());
            ps.setDouble(3, Double.parseDouble(txtHarga.getText()));
            ps.setInt(4, Integer.parseInt(txtStok.getText()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
            loadData();
            reset();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void update() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Pilih data dulu!");
                return;
            }

            Connection conn = Koneksi.getConnection();
            String sql = "UPDATE barang SET kode_barang=?, nama_barang=?, harga=?, stok=? WHERE id_barang=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtKode.getText());
            ps.setString(2, txtNama.getText());
            ps.setDouble(3, Double.parseDouble(txtHarga.getText()));
            ps.setInt(4, Integer.parseInt(txtStok.getText()));
            ps.setInt(5, Integer.parseInt(txtId.getText()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil diupdate");
            loadData();
            reset();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void hapus() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Pilih data dulu!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null, "Yakin hapus?");
            if (confirm != 0) return;

            Connection conn = Koneksi.getConnection();
            String sql = "DELETE FROM barang WHERE id_barang=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
            loadData();
            reset();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void reset() {
        txtId.setText("");
        txtKode.setText("");
        txtNama.setText("");
        txtHarga.setText("");
        txtStok.setText("");
    }

    void klikTabel() {
        int row = table.getSelectedRow();

        txtId.setText(model.getValueAt(row,0).toString());
        txtKode.setText(model.getValueAt(row,1).toString());
        txtNama.setText(model.getValueAt(row,2).toString());
        txtHarga.setText(model.getValueAt(row,3).toString());
        txtStok.setText(model.getValueAt(row,4).toString());
    }

    void loadData() {
        model.setRowCount(0);

        try {
            Connection conn = Koneksi.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM barang");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_barang"),
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getDouble("harga"),
                        rs.getInt("stok")
                });
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}