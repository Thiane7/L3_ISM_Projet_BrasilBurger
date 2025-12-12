package com.brasilburger.app.service;

import com.brasilburger.app.model.Burger;
import com.brasilburger.app.model.Complement;
import com.brasilburger.app.model.Menu;
import com.brasilburger.app.enums.TypeComplement;
import com.brasilburger.app.repository.BurgerRepository;
import com.brasilburger.app.repository.ComplementRepository;
import com.brasilburger.app.repository.MenuRepository;
import com.brasilburger.app.repository.impl.BurgerRepositoryImpl;
import com.brasilburger.app.repository.impl.ComplementRepositoryImpl;
import com.brasilburger.app.repository.impl.MenuRepositoryImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CatalogueService {
    private BurgerRepository burgerRepository;
    private ComplementRepository complementRepository;
    private MenuRepository menuRepository;

    public CatalogueService() {
        this.burgerRepository = new BurgerRepositoryImpl();
        this.complementRepository = new ComplementRepositoryImpl();
        this.menuRepository = new MenuRepositoryImpl();
    }

    // --- DÉLÉGATION : CRÉER UN COMPLÉMENT ---
    public Complement creerComplement(String nom, double prix, String image, TypeComplement typeComplement)
            throws SQLException {
        return complementRepository.creerComplement(nom, prix, image, typeComplement);
    }

    // --- DÉLÉGATION : CRÉER UN BURGER ---
    public Burger creerBurger(String nom, double prix, String image) throws SQLException {
        return burgerRepository.creerBurger(nom, prix, image);
    }

    // --- DÉLÉGATION : CRÉER UN MENU ---
    public Menu creerMenu(String nom, String image, Burger burger, List<Complement> complements) throws SQLException {
        return menuRepository.creerMenu(nom, image, burger, complements);
    }


    // --- DÉLÉGATION : LIRE TOUS LES BURGERS ---
    public List<Burger> lireTousLeBurgers() throws SQLException {
        return burgerRepository.lireTousBurgers();
    }

    public List<Burger> lireTousLeBurgers(Connection conn) throws SQLException {
        return burgerRepository.lireTousBurgers(conn);
    }

    // --- DÉLÉGATION : LIRE TOUS LES COMPLÉMENTS ---
    public List<Complement> lireTousLesComplements() throws SQLException {
        return complementRepository.lireTousComplements();
    }

    public List<Complement> lireTousLesComplements(Connection conn) throws SQLException {
        return complementRepository.lireTousComplements(conn);
    }

    // --- DÉLÉGATION : LIRE TOUS LES MENUS ---
    public List<Menu> lireTousLesMenus() throws SQLException {
        return menuRepository.lireTousMenus();
    }

    public List<Menu> lireTousLesMenus(Connection conn) throws SQLException {
        return menuRepository.lireTousMenus(conn);
    }

    // --- HELPER : LIRE LES COMPLÉMENTS D'UN MENU ---
    public List<Complement> lireComplementsMenu(int menuId) throws SQLException {
        return menuRepository.lireComplementsMenu(menuId);
    }

    public List<Complement> lireComplementsMenu(Connection conn, int menuId) throws SQLException {
        return menuRepository.lireComplementsMenu(conn, menuId);
    }
}