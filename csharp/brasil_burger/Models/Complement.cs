using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace brasil_burger.Models;

[Table("complements")]
public class Complement {
    [Key, Column("id_complement")]
    public int Id { get; set; }
    public string Nom { get; set; } = null!;
    public decimal Prix { get; set; }
    public string? Image { get; set; }
    [Column("type_complement")]
    public string TypeComplement { get; set; } = null!; // Boisson, Frites, Sauce
    [Column("est_archive")]
    public bool EstArchive { get; set; }
}