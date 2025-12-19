using Microsoft.EntityFrameworkCore;
using brasil_burger.Models;

namespace brasil_burger.Data;

public class BrasilBurgerContext : DbContext {
    public BrasilBurgerContext(DbContextOptions<BrasilBurgerContext> options) : base(options) { }

    public DbSet<Burger> Burgers { get; set; }
    public DbSet<Menu> Menus { get; set; }
    public DbSet<Complement> Complements { get; set; }
    public DbSet<Commande> Commandes { get; set; }
    public DbSet<Paiement> Paiements { get; set; }
}