<?php

namespace App\Entity;

use App\Repository\CommandesRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: CommandesRepository::class)]
#[ORM\Table(name: "COMMANDES")]
class Commandes
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: "id_commande")]
    private ?int $idCommande = null;

    // CORRECTION : On remplace User::class par Clients::class
    #[ORM\ManyToOne(targetEntity: Clients::class)]
    #[ORM\JoinColumn(name: "id_client", referencedColumnName: "id_client", nullable: false)]
    private ?Clients $client = null;

    #[ORM\ManyToOne(targetEntity: Zones::class)]
    #[ORM\JoinColumn(name: "id_zone", referencedColumnName: "id_zone", nullable: true)]
    private ?Zones $zone = null;

    #[ORM\Column(name: "date_commande", type: Types::DATETIME_MUTABLE)]
    private ?\DateTimeInterface $dateCommande = null;

    #[ORM\Column(length: 50)]
    private ?string $statut = 'EN_COURS'; 

    #[ORM\Column(name: "mode_consommation", length: 50)]
    private ?string $modeConsommation = null;

    #[ORM\Column(name: "montant_total", type: Types::DECIMAL, precision: 10, scale: 2)]
    private ?string $montantTotal = '0.00';

    public function __construct() {
        $this->dateCommande = new \DateTime();
    }

    public function getIdCommande(): ?int { return $this->idCommande; }

    // CORRECTION : On change le type de retour User par Clients
    public function getClient(): ?Clients { return $this->client; }
    public function setClient(?Clients $client): self { $this->client = $client; return $this; }

    public function getZone(): ?Zones { return $this->zone; }
    public function setZone(?Zones $zone): self { $this->zone = $zone; return $this; }
    public function getStatut(): ?string { return $this->statut; }
    public function setStatut(string $statut): self { $this->statut = $statut; return $this; }
    public function getDateCommande(): ?\DateTimeInterface { return $this->dateCommande; }
    public function getMontantTotal(): ?string { return $this->montantTotal; }
    public function setMontantTotal(string $montantTotal): self { $this->montantTotal = $montantTotal; return $this; }
}