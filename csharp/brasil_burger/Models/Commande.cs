using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace brasil_burger.Models
{
    [Table("commandes")]
    public class Commande
    {
        [Key]
        [Column("id_commande")]
        public int Id { get; set; }

        [Column("id_client")]
        public int IdClient { get; set; }

        [Column("date_commande")]
        public DateTime DateCommande { get; set; } = DateTime.UtcNow;

        [Column("statut")]
        public string Statut { get; set; } = "EN_COURS";

        [Column("mode_consommation")]
        public string ModeConsommation { get; set; } = string.Empty;

        [Column("montant_total")]
        public decimal MontantTotal { get; set; }
    }
}