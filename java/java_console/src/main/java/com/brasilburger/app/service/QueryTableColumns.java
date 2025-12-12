package com.brasilburger.app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryTableColumns {
    public static void main(String[] args) {
        String table = (args != null && args.length > 0) ? args[0] : "menus";
        String sql = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = ? ORDER BY ordinal_position;";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, table);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Colonnes de la table '" + table + "':");
                while (rs.next()) {
                    System.out.println(rs.getString("column_name") + " - " + rs.getString("data_type"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la lecture des colonnes : " + e.getMessage());
        }
    }
}
