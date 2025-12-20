using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace brasil_burger.Models
{
    [Table("paiements")]
    public class Paiement
    {
        [Key]
        [Column("id_paiement")]
        public int IdPaiement { get; set; }

        [Column("id_commande")]
        public int IdCommande { get; set; }

        [Column("date_paiement")]
        public DateTime DatePaiement { get; set; } = DateTime.UtcNow;
        [Column("montant")]
        public decimal Montant { get; set; }

        [Column("mode_paiement")]
        public string ModePaiement { get; set; } = string.Empty;
    }
}