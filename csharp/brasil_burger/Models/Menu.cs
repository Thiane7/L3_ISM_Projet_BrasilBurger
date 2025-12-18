using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace brasil_burger.Models;

[Table("menus")]
public class Menu
{
    [Key, Column("id_menu")]
    public int Id { get; set; }
    [Column("nom")]
    public string Nom { get; set; } = null!;
    [Column("prix")]
    public decimal Prix { get; set; }
    [Column("image")]
    public string? Image { get; set; }
    [Column("est_archive")]
    public bool EstArchive { get; set; }
}