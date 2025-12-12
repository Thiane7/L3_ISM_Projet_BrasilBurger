package com.brasilburger.app.repository.impl;

import com.brasilburger.app.enums.TypeComplement;
import com.brasilburger.app.model.Complement;
import com.brasilburger.app.repository.ComplementRepository;
import com.brasilburger.app.service.DatabaseService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplementRepositoryImpl implements ComplementRepository {

    @Override
    public Complement creerComplement(String nom, double prix, String image, TypeComplement type) throws SQLException {
        String SQL_INSERT = "INSERT INTO COMPLEMENTS (nom, prix, image, type_complement, est_archive) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nom);
            stmt.setDouble(2, prix);
            stmt.setString(3, image);
            stmt.setString(4, type.getValeurSQL());
            stmt.setBoolean(5, false);

            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Échec de la création du complément, aucune ligne affectée.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                } else {
                    throw new SQLException("Échec de la création du complément, pas d'ID généré.");
                }
            }
        }

        System.out.println("✅ Complément créé et inséré : " + nom + " (ID: " + generatedId + ")");
        return new Complement(generatedId, nom, prix, image, type);
    }

    @Override
    public List<Complement> lireTousComplements() throws SQLException {
        List<Complement> complementList = new ArrayList<>();
        String SQL_SELECT = "SELECT id_complement, nom, prix, image, type_complement FROM COMPLEMENTS WHERE est_archive = false ORDER BY id_complement DESC LIMIT 100";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TypeComplement type = rs.getString("type_complement").trim().equals("Frites") ? TypeComplement.FRITE : TypeComplement.BOISSON;
                Complement complement = new Complement(
                    rs.getInt("id_complement"),
                    rs.getString("nom"),
                    rs.getDouble("prix"),
                    rs.getString("image"),
                    type
                );
                complementList.add(complement);
            }
        }
        return complementList;
    }

    @Override
    public List<Complement> lireTousComplements(Connection conn) throws SQLException {
        List<Complement> complementList = new ArrayList<>();
        String SQL_SELECT = "SELECT id_complement, nom, prix, image, type_complement FROM COMPLEMENTS WHERE est_archive = false ORDER BY id_complement DESC LIMIT 100";

        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TypeComplement type = rs.getString("type_complement").trim().equals("Frites") ? TypeComplement.FRITE : TypeComplement.BOISSON;
                Complement complement = new Complement(
                    rs.getInt("id_complement"),
                    rs.getString("nom"),
                    rs.getDouble("prix"),
                    rs.getString("image"),
                    type
                );
                complementList.add(complement);
            }
        }
        return complementList;
    }

    @Override
    public Complement lireComplementParId(int id) throws SQLException {
        String SQL_SELECT = "SELECT id_complement, nom, prix, image, type_complement FROM COMPLEMENTS WHERE id_complement = ? AND est_archive = false";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    TypeComplement type = rs.getString("type_complement").trim().equals("Frites") ? TypeComplement.FRITE : TypeComplement.BOISSON;
                    return new Complement(
                        rs.getInt("id_complement"),
                        rs.getString("nom"),
                        rs.getDouble("prix"),
                        rs.getString("image"),
                        type
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void supprimerComplement(int id) throws SQLException {
        String SQL_UPDATE = "UPDATE COMPLEMENTS SET est_archive = true WHERE id_complement = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Complément supprimé (archivé) : ID " + id);
        }
    }
}
