package com.brasilburger.app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryDbConstraints {
    public static void main(String[] args) {
        String sql = "SELECT conname, pg_get_constraintdef(oid) AS def FROM pg_constraint WHERE conrelid = 'complements'::regclass;";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Contraintes pour la table 'complements':");
            while (rs.next()) {
                System.out.println(rs.getString("conname") + " => " + rs.getString("def"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la lecture des contraintes : " + e.getMessage());
        }
    }
}
