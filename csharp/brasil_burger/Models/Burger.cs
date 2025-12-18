using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace brasil_burger.Models;

[Table("burgers")]
public class Burger {
    [Key, Column("id_burger")]
    public int Id { get; set; }
    public string Nom { get; set; } = null!;
    public decimal Prix { get; set; }
    public string? Image { get; set; }
    [Column("est_archive")]
    public bool EstArchive { get; set; }
}