package com.brasilburger.app.repository;

import com.brasilburger.app.model.Burger;
import com.brasilburger.app.model.Complement;
import com.brasilburger.app.model.Menu;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;

public interface MenuRepository {
    Menu creerMenu(String nom, String image, Burger burger, List<Complement> complements) throws SQLException;
    List<Menu> lireTousMenus() throws SQLException;
    List<Menu> lireTousMenus(Connection conn) throws SQLException;
    Menu lireMenuParId(int id) throws SQLException;
    void supprimerMenu(int id) throws SQLException;
    List<Complement> lireComplementsMenu(int menuId) throws SQLException;
    List<Complement> lireComplementsMenu(Connection conn, int menuId) throws SQLException;
}
