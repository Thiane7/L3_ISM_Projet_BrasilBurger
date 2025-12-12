package com.brasilburger.app.repository.impl;

import com.brasilburger.app.enums.TypeComplement;
import com.brasilburger.app.model.Burger;
import com.brasilburger.app.model.Complement;
import com.brasilburger.app.model.Menu;
import com.brasilburger.app.repository.MenuRepository;
import com.brasilburger.app.service.DatabaseService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuRepositoryImpl implements MenuRepository {

    @Override
    public Menu creerMenu(String nom, String image, Burger burger, List<Complement> complements) throws SQLException {
        if (burger == null) {
            throw new IllegalArgumentException("Le menu doit contenir un burger.");
        }

        // 1. Calcul du prix total
        double prixTotal = burger.getPrix();
        for (Complement c : complements) {
            prixTotal += c.getPrix();
        }

        // 2. Insertion dans la table MENUS
        int menuId = -1;
        String SQL_INSERT_MENU = "INSERT INTO MENUS (nom, image, prix, est_archive) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmtMenu = conn.prepareStatement(SQL_INSERT_MENU, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmtMenu.setString(1, nom);
            stmtMenu.setString(2, image);
            stmtMenu.setDouble(3, prixTotal);
            stmtMenu.setBoolean(4, false);

            if (stmtMenu.executeUpdate() == 0) {
                throw new SQLException("Échec de la création du menu.");
            }

            try (ResultSet rs = stmtMenu.getGeneratedKeys()) {
                if (rs.next()) {
                    menuId = rs.getInt(1);
                } else {
                    throw new SQLException("Échec de la création du menu, pas d'ID généré.");
                }
            }
        }

        // 3. Insertion dans COMPOSITION_MENUS
        String SQL_INSERT_COMPOSITION = "INSERT INTO COMPOSITION_MENUS (id_menu, id_burger, id_boisson, id_frites) VALUES (?, ?, ?, ?)";
        Integer idBoisson = null;
        Integer idFrites = null;

        for (Complement c : complements) {
            if (c.getTypeComplement() == TypeComplement.BOISSON) idBoisson = c.getId();
            else if (c.getTypeComplement() == TypeComplement.FRITE) idFrites = c.getId();
        }

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmtComposition = conn.prepareStatement(SQL_INSERT_COMPOSITION)) {

            stmtComposition.setInt(1, menuId);
            stmtComposition.setInt(2, burger.getId());
            if (idBoisson != null) stmtComposition.setInt(3, idBoisson); else stmtComposition.setNull(3, java.sql.Types.INTEGER);
            if (idFrites != null) stmtComposition.setInt(4, idFrites); else stmtComposition.setNull(4, java.sql.Types.INTEGER);

            if (stmtComposition.executeUpdate() == 0) {
                throw new SQLException("Échec de l'insertion dans COMPOSITION_MENUS.");
            }
        }

        System.out.println("✅ Menu créé avec " + complements.size() + " compléments. (ID: " + menuId + ")");
        return new Menu(menuId, nom, image, prixTotal, burger, complements);
    }

    @Override
    public List<Menu> lireTousMenus() throws SQLException {
        List<Menu> menuList = new ArrayList<>();
        String SQL_SELECT = "SELECT id_menu, nom, image, prix FROM MENUS WHERE est_archive = false ORDER BY id_menu DESC LIMIT 100";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int menuId = rs.getInt("id_menu");
                List<Complement> menuComplements = lireComplementsMenu(menuId);
                Burger burger = new Burger(0, "Burger", 0, "");

                Menu menu = new Menu(
                    menuId,
                    rs.getString("nom"),
                    rs.getString("image"),
                    rs.getDouble("prix"),
                    burger,
                    menuComplements
                );
                menuList.add(menu);
            }
        }
        return menuList;
    }

    @Override
    public List<Menu> lireTousMenus(Connection conn) throws SQLException {
        List<Menu> menuList = new ArrayList<>();
        String SQL_SELECT = "SELECT id_menu, nom, image, prix FROM MENUS WHERE est_archive = false ORDER BY id_menu DESC LIMIT 100";

        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int menuId = rs.getInt("id_menu");
                List<Complement> menuComplements = lireComplementsMenu(conn, menuId);
                Burger burger = new Burger(0, "Burger", 0, "");

                Menu menu = new Menu(
                    menuId,
                    rs.getString("nom"),
                    rs.getString("image"),
                    rs.getDouble("prix"),
                    burger,
                    menuComplements
                );
                menuList.add(menu);
            }
        }
        return menuList;
    }

    @Override
    public Menu lireMenuParId(int id) throws SQLException {
        String SQL_SELECT = "SELECT id_menu, nom, image, prix FROM MENUS WHERE id_menu = ? AND est_archive = false";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List<Complement> complements = lireComplementsMenu(id);
                    Burger burger = new Burger(0, "Burger", 0, "");
                    return new Menu(
                        rs.getInt("id_menu"),
                        rs.getString("nom"),
                        rs.getString("image"),
                        rs.getDouble("prix"),
                        burger,
                        complements
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void supprimerMenu(int id) throws SQLException {
        String SQL_UPDATE = "UPDATE MENUS SET est_archive = true WHERE id_menu = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Menu supprimé (archivé) : ID " + id);
        }
    }

    @Override
    public List<Complement> lireComplementsMenu(int menuId) throws SQLException {
        List<Complement> complements = new ArrayList<>();
        String SQL_SELECT = "SELECT c.id_complement, c.nom, c.prix, c.image, c.type_complement " +
                            "FROM COMPLEMENTS c " +
                            "JOIN COMPOSITION_MENUS cm ON cm.id_boisson = c.id_complement OR cm.id_frites = c.id_complement " +
                            "WHERE cm.id_menu = ? AND c.est_archive = false";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT)) {

            stmt.setInt(1, menuId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TypeComplement type = rs.getString("type_complement").trim().equals("Frites") ? TypeComplement.FRITE : TypeComplement.BOISSON;
                    Complement complement = new Complement(
                        rs.getInt("id_complement"),
                        rs.getString("nom"),
                        rs.getDouble("prix"),
                        rs.getString("image"),
                        type
                    );
                    complements.add(complement);
                }
            }
        }
        return complements;
    }

    @Override
    public List<Complement> lireComplementsMenu(Connection conn, int menuId) throws SQLException {
        List<Complement> complements = new ArrayList<>();
        String SQL_SELECT = "SELECT c.id_complement, c.nom, c.prix, c.image, c.type_complement " +
                            "FROM COMPLEMENTS c " +
                            "JOIN COMPOSITION_MENUS cm ON cm.id_boisson = c.id_complement OR cm.id_frites = c.id_complement " +
                            "WHERE cm.id_menu = ? AND c.est_archive = false";

        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT)) {
            stmt.setInt(1, menuId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TypeComplement type = rs.getString("type_complement").trim().equals("Frites") ? TypeComplement.FRITE : TypeComplement.BOISSON;
                    Complement complement = new Complement(
                        rs.getInt("id_complement"),
                        rs.getString("nom"),
                        rs.getDouble("prix"),
                        rs.getString("image"),
                        type
                    );
                    complements.add(complement);
                }
            }
        }
        return complements;
    }
}
