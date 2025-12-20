using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace brasil_burger.Models;

[Table("burgers")]
public class Burger
{
    [Key, Column("id_burger")]
    public int Id { get; set; }
    [Column("nom")]
    public string Nom { get; set; } = null!;
    [Column("prix")]
    public decimal Prix { get; set; }
    [Column("image")]
    public string? Image { get; set; }
    [Column("est_archive")]
    public bool EstArchive { get; set; }
    [Column("description")]
    public string? Description { get; set; }
}