using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace brasil_burger.Models;

[Table("menus")]
public class Menu {
    [Key, Column("id_menu")]
    public int Id { get; set; }
    public string Nom { get; set; } = null!;
    public decimal Prix { get; set; }
    public string? Image { get; set; }
    [Column("est_archive")]
    public bool EstArchive { get; set; }
}