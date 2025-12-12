package com.brasilburger.app.repository.impl;

import com.brasilburger.app.model.Burger;
import com.brasilburger.app.repository.BurgerRepository;
import com.brasilburger.app.service.DatabaseService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BurgerRepositoryImpl implements BurgerRepository {

    @Override
    public Burger creerBurger(String nom, double prix, String image) throws SQLException {
        String SQL_INSERT = "INSERT INTO BURGERS (nom, prix, image, est_archive) VALUES (?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nom);
            stmt.setDouble(2, prix);
            stmt.setString(3, image);
            stmt.setBoolean(4, false);

            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Échec de la création du burger, aucune ligne affectée.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                } else {
                    throw new SQLException("Échec de la création du burger, pas d'ID généré.");
                }
            }
        }

        System.out.println("✅ Burger créé et inséré : " + nom + " (ID: " + generatedId + ")");
        return new Burger(generatedId, nom, prix, image);
    }

    @Override
    public List<Burger> lireTousBurgers() throws SQLException {
        List<Burger> burgerList = new ArrayList<>();
        String SQL_SELECT = "SELECT id_burger, nom, prix, image FROM BURGERS WHERE est_archive = false ORDER BY id_burger DESC LIMIT 100";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Burger burger = new Burger(
                    rs.getInt("id_burger"),
                    rs.getString("nom"),
                    rs.getDouble("prix"),
                    rs.getString("image")
                );
                burgerList.add(burger);
            }
        }
        return burgerList;
    }

    @Override
    public List<Burger> lireTousBurgers(Connection conn) throws SQLException {
        List<Burger> burgerList = new ArrayList<>();
        String SQL_SELECT = "SELECT id_burger, nom, prix, image FROM BURGERS WHERE est_archive = false ORDER BY id_burger DESC LIMIT 100";

        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Burger burger = new Burger(
                    rs.getInt("id_burger"),
                    rs.getString("nom"),
                    rs.getDouble("prix"),
                    rs.getString("image")
                );
                burgerList.add(burger);
            }
        }
        return burgerList;
    }

    @Override
    public Burger lireBurgerParId(int id) throws SQLException {
        String SQL_SELECT = "SELECT id_burger, nom, prix, image FROM BURGERS WHERE id_burger = ? AND est_archive = false";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Burger(
                        rs.getInt("id_burger"),
                        rs.getString("nom"),
                        rs.getDouble("prix"),
                        rs.getString("image")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void supprimerBurger(int id) throws SQLException {
        String SQL_UPDATE = "UPDATE BURGERS SET est_archive = true WHERE id_burger = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Burger supprimé (archivé) : ID " + id);
        }
    }
}
