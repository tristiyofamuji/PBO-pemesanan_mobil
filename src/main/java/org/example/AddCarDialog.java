package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class AddCarDialog extends JDialog {
    private DataMobilPanel mobilPanel;

    public AddCarDialog(JFrame parent, DataMobilPanel mobilPanel) {
        super(parent, "Tambah Mobil", true);
        this.mobilPanel = mobilPanel;
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Menambahkan field input sesuai dengan kolom tabel
        JTextField namaMobilField = new JTextField(20);
        JTextField tipeMobilField = new JTextField(20);
        JTextField tahunMobilField = new JTextField(4);
        JTextField platNomorField = new JTextField(20);
        JTextField hargaSewaField = new JTextField(20);

        // Dropdown untuk status mobil dengan enum "tersedia" dan "tidak tersedia"
        JComboBox<String> statusMobilField = new JComboBox<>(new String[]{"tersedia", "tidak tersedia"});

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nama Mobil:"), gbc);
        gbc.gridx = 1;
        add(namaMobilField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Tipe Mobil:"), gbc);
        gbc.gridx = 1;
        add(tipeMobilField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Tahun Mobil:"), gbc);
        gbc.gridx = 1;
        add(tahunMobilField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Plat Nomor:"), gbc);
        gbc.gridx = 1;
        add(platNomorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Harga Sewa per Hari:"), gbc);
        gbc.gridx = 1;
        add(hargaSewaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Status Mobil:"), gbc);
        gbc.gridx = 1;
        add(statusMobilField, gbc);

        // Tombol simpan
        JButton saveButton = new JButton("Simpan");
        saveButton.addActionListener(e -> saveCar(
                namaMobilField.getText(),
                tipeMobilField.getText(),
                tahunMobilField.getText(),
                platNomorField.getText(),
                hargaSewaField.getText(),
                (String) statusMobilField.getSelectedItem()
        ));

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);
    }

    private void saveCar(String namaMobil, String tipeMobil, String tahunMobil, String platNomor, String hargaSewa, String statusMobil) {
        // Validasi input
        if (namaMobil.isEmpty() || tipeMobil.isEmpty() || tahunMobil.isEmpty() || platNomor.isEmpty() || hargaSewa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Konversi input harga sewa dan tahun mobil
        try {
            int tahun = Integer.parseInt(tahunMobil);
            double harga = Double.parseDouble(hargaSewa);

            // Simpan ke database
            DatabaseManager dbManager = DatabaseManager.getInstance();
            String query = "INSERT INTO mobil (nama_mobil, tipe_mobil, tahun_mobil, plat_nomor, harga_sewa_per_hari, status_mobil, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
            Object[] params = {namaMobil, tipeMobil, tahun, platNomor, harga, statusMobil, new Timestamp(System.currentTimeMillis())};
            int rowsInserted = dbManager.updateData(query, params);

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Mobil berhasil ditambahkan.");
                dispose(); // Tutup dialog setelah berhasil simpan
                mobilPanel.refreshData(); // Refresh data mobil di panel utama
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan mobil.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format tahun atau harga tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
