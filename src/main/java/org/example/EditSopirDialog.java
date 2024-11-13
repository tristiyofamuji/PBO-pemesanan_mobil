package org.example;

import javax.swing.*;
import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EditSopirDialog extends JDialog {
    private JTextField namaSopirField;
    private JTextField emailField;
    private JTextField passwordField;
    private JTextField teleponField;
    private JTextField alamatField;
    private JTextField hargaSewaField;
    private JComboBox<String> statusSopirComboBox;
    private DatabaseManager dbManager;
    private int sopirId;
    private DataSopirPanel sopirPanel;

    // Constructor for the dialog
    public EditSopirDialog(JFrame parent, int sopirId, Object[] sopirData, DataSopirPanel sopirPanel) {
        super(parent, "Edit Data Sopir", true);
        this.dbManager = DatabaseManager.getInstance();
        this.sopirId = sopirId;
        this.sopirPanel = sopirPanel;

        setSize(400, 400);
        if (parent != null) {
            setLocationRelativeTo(parent);
        } else {
            setLocationRelativeTo(null); // Centers on screen if parent is null
        }
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Adding input fields
        namaSopirField = new JTextField(sopirData[1].toString(), 20);
        emailField = new JTextField(sopirData[2].toString(), 20);
        teleponField = new JTextField(sopirData[3].toString(), 20);
        alamatField = new JTextField(sopirData[4].toString(), 20);
        hargaSewaField = new JTextField(sopirData[5].toString(), 20);

        statusSopirComboBox = new JComboBox<>(new String[]{"tersedia", "disewa"});
        statusSopirComboBox.setSelectedItem(sopirData[6].toString());
        passwordField = new JTextField(20); // Initialize password field

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
        add(new JLabel("No. Telepon:"), gbc);

        gbc.gridx = 1;
        add(teleponField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Alamat:"), gbc);

        gbc.gridx = 1;
        add(alamatField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Harga Sewa per Hari:"), gbc);

        gbc.gridx = 1;
        add(hargaSewaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Status Sopir:"), gbc);

        gbc.gridx = 1;
        add(statusSopirComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Kata Sandi:"), gbc);

        gbc.gridx = 1;
        add(passwordField, gbc);

        // Save button
        JButton saveButton = new JButton("Simpan");
        saveButton.addActionListener(e -> saveSopirData());

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);
    }

    private void saveSopirData() {
        String namaSopir = namaSopirField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String alamat = alamatField.getText();
        String telepon = teleponField.getText();
        String statusSopir = statusSopirComboBox.getSelectedItem().toString();

        // Validate and parse hargaSewa
        double hargaSewa;
        try {
            hargaSewa = Double.parseDouble(hargaSewaField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga sewa harus berupa angka.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Hash password if it's not empty
        String hashedPassword = null;
        if (!password.isEmpty()) {
            hashedPassword = hashWithSHA1(password);
        }

        // Prepare the SQL query for updating sopir data, with or without password
        String query;
        Object[] params;
        if (hashedPassword != null) {
            query = "UPDATE sopir SET nama_sopir = ?, email = ?, password = ?, nomor_telepon = ?, alamat = ?, status_sopir = ?, harga_sewa_per_hari = ? WHERE id = ?";
            params = new Object[]{namaSopir, email, hashedPassword, telepon, alamat, statusSopir, hargaSewa, sopirId};
        } else {
            query = "UPDATE sopir SET nama_sopir = ?, email = ?, nomor_telepon = ?, alamat = ?, status_sopir = ?, harga_sewa_per_hari = ? WHERE id = ?";
            params = new Object[]{namaSopir, email, telepon, alamat, statusSopir, hargaSewa, sopirId};
        }

        int rowsUpdated = dbManager.updateData(query, params);

        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(this, "Data sopir berhasil diperbarui.");
            sopirPanel.refreshData(); // Refresh table data after update
            dispose(); // Close dialog
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data sopir.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Utility method to hash password with SHA-1
    private String hashWithSHA1(String input) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = sha1.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
