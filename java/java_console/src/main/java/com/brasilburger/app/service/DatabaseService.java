package com.brasilburger.app.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {

    // VOS IDENTIFIANTS NEON
    private static final String DB_HOST = "ep-sparkling-sea-agx6nlwz-pooler.c-2.eu-central-1.aws.neon.tech";
    private static final String DB_NAME = "neondb";
    private static final String DB_USER = "neondb_owner";
    private static final String DB_PASSWORD = "npg_Uqao7VzLZRy3";
    private static final String DB_PORT = "5432";
    
    private static final String URL = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;

    /**
     * Établit et retourne une nouvelle connexion à la base de données.
     * @return Connection active
     * @throws SQLException si la connexion échoue
     */
    public static Connection getConnection() throws SQLException {
        // Chargement du driver (sans try-catch autour de Class.forName, comme demandé)
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL introuvable. Vérifiez votre pom.xml: " + e.getMessage());
        }
        
        System.out.println("Tentative de connexion à la base de données Neon...");
        Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
        System.out.println("✅ Connexion à Neon établie avec succès.");
        return conn;
    }
}