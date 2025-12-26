<?php
namespace App\Entity;

use App\Repository\ComplementRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ComplementRepository::class)]
#[ORM\Table(name: 'COMPLEMENTS')]
class Complement
{
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy: 'IDENTITY')]
    #[ORM\Column(name: 'id_complement')]
    private ?int $id = null;

    #[ORM\Column(length: 100)]
    private ?string $nom = null;

    #[ORM\Column(type: 'decimal', precision: 10, scale: 2)]
    private ?string $prix = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $image = null;

    #[ORM\Column(name: 'type_complement', length: 50)]
    private ?string $type = null;

    #[ORM\Column(name: 'est_archive', type: 'boolean', options: ['default' => false])]
    private bool $isArchived = false;

    
    public function getId(): ?int { return $this->id; }
    public function getNom(): ?string { return $this->nom; }
    public function setNom(string $nom): self { $this->nom = $nom; return $this; }
    public function getPrix(): ?string { return $this->prix; }
    public function setPrix(string $prix): self { $this->prix = $prix; return $this; }
    public function getType(): ?string { return $this->type; }
    public function setType(string $type): self { $this->type = $type; return $this; }
    public function getImage(): ?string { return $this->image; }
    public function setImage(?string $image): self { $this->image = $image; return $this; }
    public function isArchived(): bool { return $this->isArchived; }
    public function setIsArchived(bool $isArchived): self { $this->isArchived = $isArchived; return $this; }
}