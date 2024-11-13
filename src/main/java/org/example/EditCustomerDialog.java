package org.example;

import javax.swing.*;
import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EditCustomerDialog extends JDialog {

    private JTextField namaField;
    private JTextField nomorTeleponField;
    private JTextField alamatField;
    private JTextField emailField;
    private JTextField passwordField;
    private DatabaseManager dbManager;
    private int customerId;
    private DataPelangganPanel pelangganPanel;

    public EditCustomerDialog(JFrame parent, int customerId, Object[] customerData, DataPelangganPanel pelangganPanel) {
        super(parent, "Edit Data Pelanggan", true);
        this.dbManager = DatabaseManager.getInstance();
        this.customerId = customerId;
        this.pelangganPanel = pelangganPanel;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Menambahkan field input dan mengisi data pelanggan saat ini
        namaField = new JTextField(customerData[1].toString(), 15);
        nomorTeleponField = new JTextField(customerData[2].toString(), 15);
        alamatField = new JTextField(customerData[3].toString(), 15);
        emailField = new JTextField(customerData[4].toString(), 15);
        passwordField = new JTextField(15); // Field untuk password baru (opsional)

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Nama Pelanggan:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(namaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Nomor Telepon:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(nomorTeleponField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Alamat:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(alamatField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(passwordField, gbc);

        // Tombol simpan
        JButton saveButton = new JButton("Simpan");
        saveButton.addActionListener(e -> saveCustomerData());

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);
    }

    private void saveCustomerData() {
        String nama = namaField.getText();
        String nomorTelepon = nomorTeleponField.getText();
        String alamat = alamatField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Validasi input
        if (nama.isEmpty() || nomorTelepon.isEmpty() || alamat.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi kecuali password (jika tidak diubah).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Hash password jika password tidak kosong (artinya pengguna ingin mengubah password)
        String hashedPassword = null;
        if (!password.isEmpty()) {
            hashedPassword = hashWithSHA1(password);
        }

        // Query untuk update data pelanggan di database, dengan atau tanpa password
        String query;
        Object[] params;
        if (hashedPassword != null) {
            query = "UPDATE pelanggan SET nama_pelanggan = ?, nomor_telepon = ?, alamat = ?, email = ?, password = ? WHERE id = ?";
            params = new Object[]{nama, nomorTelepon, alamat, email, hashedPassword, customerId};
        } else {
            query = "UPDATE pelanggan SET nama_pelanggan = ?, nomor_telepon = ?, alamat = ?, email = ? WHERE id = ?";
            params = new Object[]{nama, nomorTelepon, alamat, email, customerId};
        }

        int rowsUpdated = dbManager.updateData(query, params);

        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(this, "Data pelanggan berhasil diperbarui.");
            pelangganPanel.refreshData(); // Refresh data di panel pelanggan setelah update
            dispose(); // Tutup dialog setelah update berhasil
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data pelanggan.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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
