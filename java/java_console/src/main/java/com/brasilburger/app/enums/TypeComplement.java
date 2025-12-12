package com.brasilburger.app.enums;

public enum TypeComplement {
    FRITE("Frites"),
    BOISSON("Boisson");

    private final String valeurSQL;

    TypeComplement(String valeurSQL) {
        this.valeurSQL = valeurSQL;
    }

    public String getValeurSQL() {
        return valeurSQL;
    }
}