package com.brasilburger.app.view;

import com.brasilburger.app.enums.TypeComplement;
import com.brasilburger.app.model.Burger;
import com.brasilburger.app.model.Complement;
import com.brasilburger.app.model.Menu;
import com.brasilburger.app.service.CatalogueService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InteractiveMenu {
    private CatalogueService catalogueService;
    private Scanner scanner;
    private List<Burger> burgers;
    private List<Complement> complements;
    private List<Menu> menus;

    public InteractiveMenu() {
        this.catalogueService = new CatalogueService();
        this.scanner = new Scanner(System.in);
        this.burgers = new ArrayList<>();
        this.complements = new ArrayList<>();
        this.menus = new ArrayList<>();
    }

    public void run() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     BIENVENUE DANS LE SYSTÈME DE GESTION BRASILBURGER       ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        boolean running = true;
        while (running) {
            afficherMenuPrincipal();
            int choix = lireEntier();

            if (choix == -99) {
                System.out.println("\nAucune entrée disponible. Fermeture du programme.");
                break;
            }

            switch (choix) {
                case 1:
                    creerComplement();
                    break;
                case 2:
                    creerBurger();
                    break;
                case 3:
                    creerMenu();
                    break;
                case 4:
                    afficherCatalogue();
                    break;
                case 5:
                    running = false;
                    System.out.println("\n✅ Merci d'avoir utilisé BrasilBurger. À bientôt!\n");
                    break;
                default:
                    System.out.println("❌ Choix invalide. Veuillez réessayer.\n");
            }
        }
        scanner.close();
    }

    private void afficherMenuPrincipal() {
        System.out.println("\n┌─── MENU PRINCIPAL ───┐");
        System.out.println("│ 1. Créer un Complément│");
        System.out.println("│ 2. Créer un Burger   │");
        System.out.println("│ 3. Créer un Menu     │");
        System.out.println("│ 4. Afficher Catalogue│");
        System.out.println("│ 5. Quitter           │");
        System.out.println("└──────────────────────┘");
        System.out.print("Votre choix : ");
    }

    private void creerComplement() {
        System.out.println("\n--- Création d'un Complément ---");
        String nom;
        while (true) {
            System.out.print("Nom (ou tapez 'annuler' pour revenir) : ");
            nom = scanner.nextLine().trim();
            if (nom.equalsIgnoreCase("annuler")) {
                System.out.println("Opération annulée.");
                return;
            }
            if (nom.isEmpty()) {
                System.out.println("❌ Le nom ne peut pas être vide.");
                continue;
            }
            break;
        }

        double prix = 0;
        while (true) {
            System.out.print("Prix (CFA) (supérieur à 0) : ");
            String s = scanner.nextLine().trim();
            try {
                prix = Double.parseDouble(s);
                if (prix <= 0) {
                    System.out.println("❌ Le prix doit être supérieur à 0.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("❌ Prix invalide. Veuillez saisir un nombre.");
            }
        }

        System.out.print("Chemin de l'image : ");
        String image = scanner.nextLine().trim();

        System.out.println("Type de Complément :");
        System.out.println("  1. Frites");
        System.out.println("  2. Boisson");
        TypeComplement type;
        while (true) {
            System.out.print("Votre choix : ");
            int typeChoix = lireEntier();
            if (typeChoix == 1) { type = TypeComplement.FRITE; break; }
            if (typeChoix == 2) { type = TypeComplement.BOISSON; break; }
            System.out.println("❌ Choix invalide. Tapez 1 pour Frites ou 2 pour Boisson.");
        }

        try {
            Complement complement = catalogueService.creerComplement(nom, prix, image, type);
            complements.add(complement);
            System.out.println("✅ Complément créé : " + complement);
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la création : " + e.getMessage());
        }
    }

    private void creerBurger() {
        System.out.println("\n--- Création d'un Burger ---");
        String nom;
        while (true) {
            System.out.print("Nom (ou tapez 'annuler' pour revenir) : ");
            nom = scanner.nextLine().trim();
            if (nom.equalsIgnoreCase("annuler")) {
                System.out.println("Opération annulée.");
                return;
            }
            if (nom.isEmpty()) {
                System.out.println("❌ Le nom ne peut pas être vide.");
                continue;
            }
            break;
        }

        double prix = 0;
        while (true) {
            System.out.print("Prix (CFA) (supérieur à 0) : ");
            String s = scanner.nextLine().trim();
            try {
                prix = Double.parseDouble(s);
                if (prix <= 0) {
                    System.out.println("❌ Le prix doit être supérieur à 0.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("❌ Prix invalide. Veuillez saisir un nombre.");
            }
        }

        System.out.print("Chemin de l'image : ");
        String image = scanner.nextLine().trim();

        try {
            Burger burger = catalogueService.creerBurger(nom, prix, image);
            burgers.add(burger);
            System.out.println("✅ Burger créé : " + burger);
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la création : " + e.getMessage());
        }
    }

    private void creerMenu() {
        System.out.println("\n--- Création d'un Menu ---");
        
        try {
            if (burgers.isEmpty()) {
                List<Burger> burgersDB = catalogueService.lireTousLeBurgers();
                if (burgersDB != null && !burgersDB.isEmpty()) {
                    burgers = new ArrayList<>(burgersDB);
                }
            }
            if (complements.isEmpty()) {
                List<Complement> compsDB = catalogueService.lireTousLesComplements();
                if (compsDB != null && !compsDB.isEmpty()) {
                    complements = new ArrayList<>(compsDB);
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Impossible de charger les ressources depuis la DB : " + e.getMessage());
        }

        if (burgers.isEmpty()) {
            System.out.println("❌ Aucun burger disponible. Créez d'abord un burger.");
            return;
        }

        if (complements.isEmpty()) {
            System.out.println("❌ Aucun complément disponible. Créez d'abord des compléments.");
            return;
        }

        String nom;
        while (true) {
            System.out.print("Nom du Menu (obligatoire, ou 'annuler' pour revenir) : ");
            nom = scanner.nextLine().trim();
            if (nom.equalsIgnoreCase("annuler")) {
                System.out.println("Opération annulée.");
                return;
            }
            if (nom.isEmpty()) {
                System.out.println("❌ Le nom du menu est obligatoire.");
                continue;
            }
            break;
        }

        System.out.print("Chemin de l'image : ");
        String image = scanner.nextLine().trim();

        // Sélection du Burger
        System.out.println("\nBurgers disponibles :");
        for (int i = 0; i < burgers.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + burgers.get(i));
        }
        Burger burger;
        while (true) {
            System.out.print("Sélectionnez un burger : ");
            int burgerIdx = lireEntier() - 1;
            if (burgerIdx >= 0 && burgerIdx < burgers.size()) {
                burger = burgers.get(burgerIdx);
                break;
            }
            System.out.println("❌ Burger invalide. Réessayez.");
        }

        // Sélection des Compléments
        System.out.println("\nCompléments disponibles :");
        for (int i = 0; i < complements.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + complements.get(i));
        }
        List<Complement> selectedComplements = new ArrayList<>();
        while (true) {
            System.out.print("Sélectionnez les compléments (séparés par des virgules, ex: 1,2) : ");
            String line = scanner.nextLine().trim();
            String[] indices = line.split(",");
            selectedComplements.clear();
            boolean ok = true;
            boolean hasBoisson = false;
            boolean hasFrite = false;
            for (String idx : indices) {
                try {
                    int complementIdx = Integer.parseInt(idx.trim()) - 1;
                    if (complementIdx >= 0 && complementIdx < complements.size()) {
                        Complement c = complements.get(complementIdx);
                        selectedComplements.add(c);
                        if (c.getType() == TypeComplement.BOISSON) hasBoisson = true;
                        if (c.getType() == TypeComplement.FRITE) hasFrite = true;
                    } else {
                        ok = false; break;
                    }
                } catch (NumberFormatException e) {
                    ok = false; break;
                }
            }
            if (!ok || selectedComplements.isEmpty()) {
                System.out.println("❌ Sélection invalide. Réessayez.");
                continue;
            }
            if (!hasBoisson || !hasFrite) {
                System.out.println("❌ Un menu doit contenir au moins une Boisson et des Frites. Réessayez.");
                continue;
            }
            break;
        }

        try {
            Menu menu = catalogueService.creerMenu(nom, image, burger, selectedComplements);
            menus.add(menu);
            System.out.println("✅ Menu créé : " + menu);
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la création : " + e.getMessage());
        }
    }

    private void afficherCatalogue() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    CATALOGUE ACTUEL                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // Utiliser une seule connexion pour toutes les lectures du catalogue
        try (java.sql.Connection conn = com.brasilburger.app.service.DatabaseService.getConnection()) {
            List<Burger> burgersDB = catalogueService.lireTousLeBurgers(conn);
            List<Complement> complementsDB = catalogueService.lireTousLesComplements(conn);
            List<Menu> menusDB = catalogueService.lireTousLesMenus(conn);

            if (!burgersDB.isEmpty()) {
                System.out.println("--- Burgers (Base de Données) ---");
                for (Burger burger : burgersDB) {
                    System.out.println("  " + burger);
                }
            } else {
                System.out.println("Aucun burger en base de données.");
            }

            if (!complementsDB.isEmpty()) {
                System.out.println("\n--- Compléments (Base de Données) ---");
                for (Complement complement : complementsDB) {
                    System.out.println("  " + complement);
                }
            } else {
                System.out.println("Aucun complément en base de données.");
            }

            if (!menusDB.isEmpty()) {
                System.out.println("\n--- Menus (Base de Données) ---");
                for (Menu menu : menusDB) {
                    System.out.println("  " + menu);
                }
            } else {
                System.out.println("Aucun menu en base de données.");
            }

            System.out.println();
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du chargement du catalogue : " + e.getMessage());
        }
    }

    private int lireEntier() {
        try {
            String line = scanner.nextLine();
            if (line == null) return -99;
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return -1;
        } catch (java.util.NoSuchElementException e) {
            return -99;
        }
    }
}
