package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class AddSopirDialog extends JDialog {
    private DataSopirPanel sopirPanel;

    public AddSopirDialog(JFrame parent, DataSopirPanel sopirPanel) {
        super(parent, "Tambah Sopir", true);
        this.sopirPanel = sopirPanel;
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Menambahkan field input sesuai dengan kolom tabel
        JTextField namaSopirField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField passwordField = new JTextField(4);
        JTextField alamatField = new JTextField(20);
        JTextField teleponField = new JTextField(20);
        JTextField hargaSewaField = new JTextField(20);

        // Dropdown untuk status mobil dengan enum "tersedia" dan "tidak tersedia"
        JComboBox<String> statusSopirField = new JComboBox<>(new String[]{"tersedia", "disewa"});

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nama Sopir:"), gbc);
        gbc.gridx = 1;
        add(namaSopirField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Email Sopir:"), gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Kata Sandi:"), gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("No. Telepon:"), gbc);
        gbc.gridx = 1;
        add(teleponField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Alamat:"), gbc);
        gbc.gridx = 1;
        add(alamatField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Harga Sewa per Hari:"), gbc);
        gbc.gridx = 1;
        add(hargaSewaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Status Sopir:"), gbc);
        gbc.gridx = 1;
        add(statusSopirField, gbc);

        // Tombol simpan
        JButton saveButton = new JButton("Simpan");
        saveButton.addActionListener(e -> saveSopir(
                namaSopirField.getText(),
                emailField.getText(),
                passwordField.getText(),
                teleponField.getText(),
                alamatField.getText(),
                hargaSewaField.getText(),
                (String) statusSopirField.getSelectedItem()
        ));

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);
    }

    private void saveSopir(String namaSopir, String email, String password, String telepon, String alamat, String hargaSewa, String statusSopir) {
        // Validasi input
        if (namaSopir.isEmpty() || email.isEmpty() || password.isEmpty() || telepon.isEmpty() || alamat.isEmpty() || hargaSewa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Konversi input harga sewa dan tahun mobil
        try {
            double harga = Double.parseDouble(hargaSewa);

            // Simpan ke database
            DatabaseManager dbManager = DatabaseManager.getInstance();
            String query = "INSERT INTO mobil (nama_sopir, email, password, nomor_telepon, alamat, harga_sewa_per_hari, status_sopir, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
            Object[] params = {namaSopir, email, password, telepon, alamat, harga, statusSopir, new Timestamp(System.currentTimeMillis())};
            int rowsInserted = dbManager.updateData(query, params);

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Mobil berhasil ditambahkan.");
                dispose(); // Tutup dialog setelah berhasil simpan
                sopirPanel.refreshData(); // Refresh data mobil di panel utama
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan mobil.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format tahun atau harga tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
