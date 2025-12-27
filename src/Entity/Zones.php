<?php
namespace App\Entity;

use App\Repository\ZonesRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ZonesRepository::class)]
#[ORM\Table(name: "ZONES")]
class Zones
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: "id_zone")]
    private ?int $idZone = null;

    #[ORM\Column(name: "nom_zone", length: 100)]
    private ?string $nomZone = null;

    #[ORM\Column(name: "prix_livraison", type: Types::DECIMAL, precision: 10, scale: 2)]
    private ?string $prixLivraison = null;

    #[ORM\Column(type: Types::TEXT, nullable: true)]
    private ?string $quartiers = null;

    
    public function getIdZone(): ?int { return $this->idZone; }
    public function getNomZone(): ?string { return $this->nomZone; }
    public function setNomZone(string $nomZone): self { $this->nomZone = $nomZone; return $this; }
    public function getPrixLivraison(): ?string { return $this->prixLivraison; }
    public function setPrixLivraison(string $prixLivraison): self { $this->prixLivraison = $prixLivraison; return $this; }
    public function getQuartiers(): ?string { return $this->quartiers; }
    public function setQuartiers(?string $quartiers): self { $this->quartiers = $quartiers; return $this; }
}