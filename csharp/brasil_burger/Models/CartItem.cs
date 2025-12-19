namespace brasil_burger.Models;

public class CartItem
{
    public int Id { get; set; }
    public string Nom { get; set; } = null!;
    public decimal Prix { get; set; }
    public int Quantite { get; set; }
    public string? Image { get; set; }
    public string Type { get; set; } = "Burger";
    public decimal Total => Prix * Quantite;
}