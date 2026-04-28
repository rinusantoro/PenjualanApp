package ui;

import db.Koneksi;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormSupplier extends JFrame {

    JTextField txtId, txtNama, txtAlamat, txtKontak;
    JTable table;
    DefaultTableModel model;

    public FormSupplier() {
        setTitle("Manajemen Supplier");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // ================= FORM =================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Input Data Supplier"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField();
        txtId.setEditable(false);
        txtNama = new JTextField();
        txtAlamat = new JTextField();
        txtKontak = new JTextField();

        addField(formPanel, gbc, 0, "ID", txtId);
        addField(formPanel, gbc, 1, "Nama Supplier", txtNama);
        addField(formPanel, gbc, 2, "Alamat", txtAlamat);
        addField(formPanel, gbc, 3, "Kontak", txtKontak);

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
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // ================= TABEL =================
        model = new DefaultTableModel(
                new String[]{"ID","Nama Supplier","Alamat","Kontak"},0
        );

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new TitledBorder("Data Supplier"));

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

    void addField(JPanel panel, GridBagConstraints gbc, int y, String label, JTextField field){
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // ================= CRUD =================
    void simpan() {
        try {
            Connection conn = Koneksi.getConnection();
            String sql = "INSERT INTO supplier(nama_supplier,alamat,kontak) VALUES(?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtNama.getText());
            ps.setString(2, txtAlamat.getText());
            ps.setString(3, txtKontak.getText());

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
            String sql = "UPDATE supplier SET nama_supplier=?, alamat=?, kontak=? WHERE id_supplier=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtNama.getText());
            ps.setString(2, txtAlamat.getText());
            ps.setString(3, txtKontak.getText());
            ps.setInt(4, Integer.parseInt(txtId.getText()));

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
            String sql = "DELETE FROM supplier WHERE id_supplier=?";

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
        txtNama.setText("");
        txtAlamat.setText("");
        txtKontak.setText("");
    }

    void klikTabel() {
        int row = table.getSelectedRow();

        txtId.setText(model.getValueAt(row,0).toString());
        txtNama.setText(model.getValueAt(row,1).toString());
        txtAlamat.setText(model.getValueAt(row,2).toString());
        txtKontak.setText(model.getValueAt(row,3).toString());
    }

    void loadData() {
        model.setRowCount(0);

        try {
            Connection conn = Koneksi.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM supplier");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_supplier"),
                        rs.getString("nama_supplier"),
                        rs.getString("alamat"),
                        rs.getString("kontak")
                });
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}