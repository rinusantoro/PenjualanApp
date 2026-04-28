package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import db.Koneksi;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

import ui.LoginForm;
import com.formdev.flatlaf.FlatLightLaf;

public class LoginForm extends JFrame {
    JTextField txtUser;
    JPasswordField txtPass;

    public LoginForm() {
        setTitle("Login Kasir");
        setSize(300,200);
        setLayout(new GridLayout(3,2));

        add(new JLabel("Username"));
        txtUser = new JTextField();
        add(txtUser);

        add(new JLabel("Password"));
        txtPass = new JPasswordField();
        add(txtPass);

        JButton btn = new JButton("Login");
        add(btn);

        btn.addActionListener(e -> login());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    void login() {
        try {
            Connection c = Koneksi.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM user WHERE username=? AND password=?"
            );
            ps.setString(1, txtUser.getText());
            ps.setString(2, new String(txtPass.getPassword()));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                new Dashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Login gagal");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}