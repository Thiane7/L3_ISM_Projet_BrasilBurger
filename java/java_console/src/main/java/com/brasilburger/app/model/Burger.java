package com.brasilburger.app.model;

public class Burger {
    private int id;
    private String nom;
    private double prix;
    private String image;

    public Burger(int id, String nom, double prix, String image) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.image = image;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public double getPrix() { return prix; }
    public String getImage() { return image; }

    @Override
    public String toString() {
        return id + "] Burger: " + nom + " - " + prix + " CFA";
    }
}