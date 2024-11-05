package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AppMenuBar extends JPanel {

    public AppMenuBar(ActionListener actionListener) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(new Color(0, 121, 107));

        // Daftar menu
        String[] menuItems = {"Home", "Data Pelanggan", "Data Mobil", "Data Sopir", "Data Transaksi", "Tambah Pemesanan"};

        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(0, 150, 136));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setFont(new Font("SansSerif", Font.BOLD, 14));
            button.setOpaque(true);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addActionListener(actionListener);  // Tambahkan action listener
            add(button);
        }

        // Tombol keluar
        JButton exitButton = new JButton("Keluar");
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(255, 87, 34));
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        exitButton.setOpaque(true);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);
    }
}
