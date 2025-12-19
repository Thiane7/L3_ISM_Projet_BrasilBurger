namespace brasil_burger.Models;

public class CartItem {
    public string UniqueId { get; set; } = Guid.NewGuid().ToString(); 
    public int Id { get; set; }
    public string Nom { get; set; } = null!;
    public decimal Prix { get; set; }
    public string? Image { get; set; }
    public List<string> SelectedComplements { get; set; } = new();
    public decimal PrixComplements { get; set; }
    public decimal Total => Prix + PrixComplements;
}