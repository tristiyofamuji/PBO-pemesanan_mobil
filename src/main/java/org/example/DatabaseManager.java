package org.example;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String URL = "jdbc:mysql://localhost:3306/db_pemesanan_mobil?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Private constructor to prevent instantiation
    private DatabaseManager() {}

    // Method to get the singleton instance
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // Koneksi database
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Ambil data dari tabel pemesan_mobil
    public ResultSet fetchData() {
        String query = "SELECT * FROM pemesan_mobil ORDER BY id DESC";
        return executeQuery(query);
    }

    // Ambil data dari tabel mobil dengan kolom spesifik
    public ResultSet fetchMobilData() {
        String query = "SELECT id, nama_mobil, tipe_mobil, tahun_mobil, plat_nomor, harga_sewa_per_hari, status_mobil, created_at FROM mobil ORDER BY id DESC";
        return executeQuery(query);
    }

    // Ambil data dari tabel pelanggan
    public ResultSet fetchPelangganData() {
        String query = "SELECT id, nama_pelanggan, nomor_telepon, alamat, email, created_at FROM pelanggan ORDER BY id DESC";
        return executeQuery(query);
    }

    // Ambil data dari tabel sopir
    public ResultSet fetchSopirData() {
        String query = "SELECT * FROM sopir ORDER BY id DESC";
        return executeQuery(query);
    }

    // Method to execute a query and return ResultSet
    public ResultSet executeQuery(String query) {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengambil data dari database.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Mengambil data mobil sebagai list untuk pengelolaan yang lebih aman
    public List<Object[]> fetchMobilDataAsList() {
        String query = "SELECT id, nama_mobil, tipe_mobil, tahun_mobil, plat_nomor, harga_sewa_per_hari, status_mobil, created_at FROM mobil ORDER BY id DESC";
        List<Object[]> dataList = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                dataList.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nama_mobil"),
                        rs.getString("tipe_mobil"),
                        rs.getInt("tahun_mobil"),
                        rs.getString("plat_nomor"),
                        rs.getDouble("harga_sewa_per_hari"),
                        rs.getString("status_mobil"),
                        rs.getTimestamp("created_at")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengambil data mobil.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return dataList;
    }

    // Mengambil data pelanggan sebagai list untuk pengelolaan yang lebih aman
    public List<Object[]> fetchPelangganDataAsList() {
        String query = "SELECT id, nama_pelanggan, nomor_telepon, alamat, email, created_at FROM pelanggan ORDER BY id DESC";
        List<Object[]> dataList = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                dataList.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nama_pelanggan"),
                        rs.getString("nomor_telepon"),
                        rs.getString("alamat"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengambil data pelanggan.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return dataList;
    }

    // Mengambil data sopir sebagai list untuk pengelolaan yang lebih aman
    public List<Object[]> fetchSopirDataAsList() {
        String query = "SELECT * FROM sopir ORDER BY id DESC";
        List<Object[]> dataList = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                dataList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengambil data sopir.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return dataList;
    }

    // Update data
    public int updateData(String query, Object[] params) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengupdate data.", "Error", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }
}
