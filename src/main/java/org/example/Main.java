package org.example;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private JPanel contentArea;
    private DataTablePanel dataTablePanel;
    private DataPelangganPanel dataPelangganPanel;

    // Variabel untuk menyimpan referensi ke menu dinamis
    private JMenu tambahPelangganMenu;
    private JMenu tambahPemesanMenu;
    private JMenuBar menuBar;

    public Main() {
        setTitle("Aplikasi Pemesanan Mobil");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Membuat menu bar dengan styling khusus
        menuBar = new JMenuBar();
        menuBar.setBackground(new Color(0, 121, 107));
        Font menuFont = new Font("SanSerif", Font.BOLD, 14);

        // Menambahkan item menu utama
        menuBar.add(createMenu("Home", menuFont, e -> showHome()));
        menuBar.add(createMenu("Data Pelanggan", menuFont, e -> showDataPelanggan()));
        menuBar.add(createMenu("Data Mobil", menuFont, null));
        menuBar.add(createMenu("Data Sopir", menuFont, null));

        // Menambahkan menu dinamis
        tambahPelangganMenu = createStyledMenu("Tambah Pelanggan", menuFont, e -> openAddCustomerDialog());
        tambahPemesanMenu = createStyledMenu("Tambah Pemesanan", menuFont, e -> openAddOrderDialog());

        // Menu di sebelah kanan, tampilkan menu dinamis (awalnya "Tambah Pemesanan" untuk halaman Home)
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(tambahPemesanMenu); // Awalnya tampilkan "Tambah Pemesanan"

        // Set menu bar
        setJMenuBar(menuBar);

        // Area konten dengan tabel data pemesan_mobil
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(new Color(224, 242, 241));

        // Inisialisasi dan tambahkan DataTablePanel dan DataPelangganPanel
        dataTablePanel = new DataTablePanel();
        dataPelangganPanel = new DataPelangganPanel();
        contentArea.add(dataTablePanel, BorderLayout.CENTER);
        add(contentArea, BorderLayout.CENTER);
    }

    private JMenu createMenu(String title, Font font, java.awt.event.ActionListener action) {
        JMenu menu = new JMenu(title);
        menu.setFont(font);
        menu.setForeground(Color.WHITE);
        if (action != null) {
            menu.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    action.actionPerformed(null);
                }
            });
        }
        return menu;
    }

    private JMenu createStyledMenu(String title, Font font, java.awt.event.ActionListener action) {
        JMenu menu = new JMenu(title);
        menu.setFont(font);
        menu.setForeground(Color.WHITE);
        menu.setBackground(Color.CYAN);

        // Hover effects
        menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu.setBackground(Color.BLUE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menu.setBackground(Color.CYAN);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                action.actionPerformed(null);
            }
        });

        return menu;
    }

    private void showHome() {
        contentArea.removeAll();
        contentArea.add(dataTablePanel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();

        // Ganti menu dinamis ke "Tambah Pemesanan" saat berada di Home
        switchToMenu(tambahPemesanMenu);
    }

    private void showDataPelanggan() {
        contentArea.removeAll();
        contentArea.add(dataPelangganPanel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();

        // Ganti menu dinamis ke "Tambah Pelanggan" saat berada di Data Pelanggan
        switchToMenu(tambahPelangganMenu);
    }

    private void openAddCustomerDialog() {
        new AddCustomerDialog(this, dataPelangganPanel).setVisible(true);
    }

    private void openAddOrderDialog() {
        Object[] emptyData = new Object[]{"", "", "", "", "", "", "", "", "", ""};
        EditDialog addDialog = new EditDialog(this, emptyData, DatabaseManager.getInstance(), -1, dataTablePanel, false);
        addDialog.setVisible(true);
    }

    // Metode untuk mengganti menu dinamis
    private void switchToMenu(JMenu menu) {
        menuBar.remove(tambahPelangganMenu);
        menuBar.remove(tambahPemesanMenu);
        menuBar.add(menu);  // Tambahkan menu dinamis baru di akhir
        menuBar.revalidate();
        menuBar.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
