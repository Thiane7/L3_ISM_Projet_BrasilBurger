package com.brasilburger.app.repository;

import com.brasilburger.app.enums.TypeComplement;
import com.brasilburger.app.model.Complement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;

public interface ComplementRepository {
    Complement creerComplement(String nom, double prix, String image, TypeComplement type) throws SQLException;
    List<Complement> lireTousComplements() throws SQLException;
    List<Complement> lireTousComplements(Connection conn) throws SQLException;
    Complement lireComplementParId(int id) throws SQLException;
    void supprimerComplement(int id) throws SQLException;
}
