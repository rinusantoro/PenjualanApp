package ui;

import db.Koneksi;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormInventory extends JFrame {

    JComboBox<String> cbBarang, cbJenis;
    JTextField txtQty, txtKet;
    JTable table;
    DefaultTableModel model;

    public FormInventory() {
        setTitle("Inventory Barang");
        setSize(900,500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // ================= FORM =================
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new TitledBorder("Input Inventory"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbBarang = new JComboBox<>();
        cbJenis = new JComboBox<>(new String[]{"MASUK","KELUAR"});
        txtQty = new JTextField();
        txtKet = new JTextField();

        addField(form, gbc, 0, "Barang", cbBarang);
        addField(form, gbc, 1, "Jenis", cbJenis);
        addField(form, gbc, 2, "Qty", txtQty);
        addField(form, gbc, 3, "Keterangan", txtKet);

        JButton btnSimpan = new JButton("💾 Simpan");
        gbc.gridx = 1;
        gbc.gridy = 4;
        form.add(btnSimpan, gbc);

        add(form, BorderLayout.WEST);

        // ================= TABLE =================
        model = new DefaultTableModel(
                new String[]{"Tanggal","Barang","Jenis","Qty","Keterangan"},0
        );

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ================= EVENT =================
        btnSimpan.addActionListener(e -> simpan());

        loadBarang();
        loadData();

        setVisible(true);
    }

    void addField(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field){
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // ================= LOAD BARANG =================
    void loadBarang() {
        try {
            Connection conn = Koneksi.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM barang");

            while (rs.next()) {
                cbBarang.addItem(rs.getInt("id_barang") + "-" + rs.getString("nama_barang"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SIMPAN =================
void simpan() {
    try {
        Connection conn = Koneksi.getConnection();
        conn.setAutoCommit(false);

        int idBarang = Integer.parseInt(cbBarang.getSelectedItem().toString().split("-")[0]);
        String jenis = cbJenis.getSelectedItem().toString();
        int qty = Integer.parseInt(txtQty.getText());
        String ket = txtKet.getText();

        // INSERT INVENTORY
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO inventory(tanggal,id_barang,jenis,qty,keterangan) VALUES(NOW(),?,?,?,?)"
        );
        ps.setInt(1, idBarang);
        ps.setString(2, jenis);
        ps.setInt(3, qty);
        ps.setString(4, ket);
        ps.executeUpdate();

        // UPDATE STOK
        if (jenis.equals("MASUK")) {
            PreparedStatement ps2 = conn.prepareStatement(
                "UPDATE barang SET stok = stok + ? WHERE id_barang=?"
            );
            ps2.setInt(1, qty);
            ps2.setInt(2, idBarang);
            ps2.executeUpdate();

        } else {
            PreparedStatement ps2 = conn.prepareStatement(
                "UPDATE barang SET stok = stok - ? WHERE id_barang=?"
            );
            ps2.setInt(1, qty);
            ps2.setInt(2, idBarang);
            ps2.executeUpdate();
        }

        conn.commit();

        JOptionPane.showMessageDialog(null, "Inventory berhasil disimpan");

        loadData();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

void loadData() {
    model.setRowCount(0);

    try {
        Connection conn = Koneksi.getConnection();
        ResultSet rs = conn.createStatement().executeQuery(
            "SELECT i.tanggal, b.nama_barang, i.jenis, i.qty, i.keterangan " +
            "FROM inventory i JOIN barang b ON i.id_barang=b.id_barang"
        );

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getInt(4),
                rs.getString(5)
            });
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}