namespace brasil_burger.Models;

public class CatalogueViewModel {
    public List<Burger> Burgers { get; set; } = new();
    public List<Menu> Menus { get; set; } = new();
    public List<Complement> Complements { get; set; } = new();
}