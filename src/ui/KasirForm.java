import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class KasirForm extends JFrame {
    JTextField txtBarcode, txtQty;
    JTable table;
    DefaultTableModel model;
    JLabel lblTotal;

    public KasirForm() {
        setTitle("Kasir");
        setSize(700,500);

        txtBarcode = new JTextField(15);
        txtQty = new JTextField("1",5);

        JButton btnTambah = new JButton("Tambah");

        model = new DefaultTableModel(
            new String[]{"ID","Nama","Harga","Qty","Subtotal"},0
        );
        table = new JTable(model);

        lblTotal = new JLabel("Total: 0");

        btnTambah.addActionListener(e -> tambahItem());

        JPanel top = new JPanel();
        top.add(new JLabel("Barcode"));
        top.add(txtBarcode);
        top.add(new JLabel("Qty"));
        top.add(txtQty);
        top.add(btnTambah);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(lblTotal, BorderLayout.SOUTH);

        setVisible(true);
    }

    void tambahItem() {
        try {
            Connection c = Koneksi.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM barang WHERE kode_barang=?"
            );
            ps.setString(1, txtBarcode.getText());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double harga = rs.getDouble("harga");
                int qty = Integer.parseInt(txtQty.getText());
                double sub = harga * qty;

                model.addRow(new Object[]{
                    rs.getInt("id_barang"),
                    rs.getString("nama_barang"),
                    harga,
                    qty,
                    sub
                });

                hitungTotal();
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    void hitungTotal() {
        double total = 0;
        for (int i=0; i<model.getRowCount(); i++) {
            total += (double) model.getValueAt(i,4);
        }
        lblTotal.setText("Total: " + total);
    }
}