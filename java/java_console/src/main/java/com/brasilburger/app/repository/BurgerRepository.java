package com.brasilburger.app.repository;

import com.brasilburger.app.model.Burger;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;

public interface BurgerRepository {
    Burger creerBurger(String nom, double prix, String image) throws SQLException;
    List<Burger> lireTousBurgers() throws SQLException;
    List<Burger> lireTousBurgers(Connection conn) throws SQLException;
    Burger lireBurgerParId(int id) throws SQLException;
    void supprimerBurger(int id) throws SQLException;
}
