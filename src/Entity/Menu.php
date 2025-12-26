<?php
namespace App\Entity;

use App\Repository\MenuRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: MenuRepository::class)]
#[ORM\Table(name: 'MENUS')]
class Menu
{
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy: 'IDENTITY')]
    #[ORM\Column(name: 'id_menu', type: 'integer')] // Ajout du type explicite
    private ?int $id = null;

    #[ORM\Column(length: 100)]
    private ?string $nom = null;

    #[ORM\Column(type: 'decimal', precision: 10, scale: 2)]
    private ?string $prix = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $image = null;

    #[ORM\Column(name: 'est_archive', type: 'boolean')]
    private bool $isArchived = false;

     
    
    private ?Burger $burger = null;

    public function getId(): ?int { return $this->id; }
    public function getNom(): ?string { return $this->nom; }
    public function setNom(string $nom): self { $this->nom = $nom; return $this; }
    public function getPrix(): ?string { return $this->prix; }
    public function setPrix(string $prix): self { $this->prix = $prix; return $this; }
    public function getImage(): ?string { return $this->image; }
    public function setImage(?string $image): self { $this->image = $image; return $this; }
    public function isArchived(): bool { return $this->isArchived; }
    public function setIsArchived(bool $isArchived): self { $this->isArchived = $isArchived; return $this; }

    public function getBurger(): ?Burger { return $this->burger; }
    public function setBurger(?Burger $burger): self { $this->burger = $burger; return $this; }
}