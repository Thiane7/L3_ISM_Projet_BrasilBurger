package com.brasilburger.app.model;

import java.util.List;

public class Menu {
    private int id;
    private String nom;
    private String image;
    private double prixTotal; 
    private Burger burger;
    private List<Complement> complements;

    public Menu(int id, String nom, String image, double prixTotal, Burger burger, List<Complement> complements) {
        this.id = id;
        this.nom = nom;
        this.image = image;
        this.prixTotal = prixTotal;
        this.burger = burger;
        this.complements = complements;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getImage() { return image; }
    public double getPrixTotal() { return prixTotal; }
    public Burger getBurger() { return burger; }
    public List<Complement> getComplements() { return complements; }

    @Override
    public String toString() {
        return id + "] Menu: " + nom + " (avec " + burger.getNom() + ") - Total: " + String.format("%.2f", prixTotal) + " CFA";
    }
}