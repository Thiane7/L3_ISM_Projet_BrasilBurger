using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace brasil_burger.Models;

[Table("complements")]
public class Complement
{
    [Key, Column("id_complement")]
    public int Id { get; set; }
    [Column("nom")]
    public string Nom { get; set; } = null!;
    [Column("prix")]
    public decimal Prix { get; set; }
    [Column("image")]
    public string? Image { get; set; }
    [Column("type_complement")]
    public string TypeComplement { get; set; } = null!;
    [Column("est_archive")]
    public bool EstArchive { get; set; }
}