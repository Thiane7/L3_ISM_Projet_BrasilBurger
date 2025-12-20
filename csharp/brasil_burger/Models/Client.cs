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

        [Required]
        [Column("nom")]
        public string Nom { get; set; } = string.Empty;

        [Column("prenom")]
        public string Prenom { get; set; } = string.Empty;

        [Required]
        [EmailAddress]
        [Column("email")]
        public string Email { get; set; } = string.Empty;

        [Required]
        [Column("mot_de_passe")]
        public string MotDePasse { get; set; } = string.Empty;

        [Column("telephone")]
        public string? Telephone { get; set; }
    }
}