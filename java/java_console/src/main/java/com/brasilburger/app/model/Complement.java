package com.brasilburger.app.model;

import com.brasilburger.app.enums.TypeComplement;

public class Complement {
    private int id;
    private String nom;
    private double prix;
    private String image;
    private TypeComplement typeComplement;

    public Complement(int id, String nom, double prix, String image, TypeComplement typeComplement) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.image = image;
        this.typeComplement = typeComplement;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public double getPrix() {
        return prix;
    }

    public String getImage() {
        return image;
    }

    public TypeComplement getTypeComplement() {
        return typeComplement;
    }

    // Compatibility accessor
    public TypeComplement getType() {
        return typeComplement;
    }

    @Override
    public String toString() {
        return id + "] Complément (" + typeComplement + "): " + nom + " - " + prix + " CFA";
    }
}
