using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace brasil_burger.Models
{
    [Table("clients")]
    public class Client
    {
        [Key]
        [Column("id_client")]
        public int Id { get; set; }

        [Column("nom")]
        [Required]
        public string Nom { get; set; } = string.Empty;

        [Column("prenom")]
        [Required]
        public string Prenom { get; set; } = string.Empty;

        [Column("telephone")]
        [Required]
        public string Telephone { get; set; } = string.Empty;

        [Column("email")]
        [Required]
        [EmailAddress]
        public string Email { get; set; } = string.Empty;

        [Column("mot_de_passe")]
        [Required]
        public string MotDePasse { get; set; } = string.Empty;

        [Column("adresse")]
        public string? Adresse { get; set; }
    }
}