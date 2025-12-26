<?php
namespace App\Repository;

use App\Entity\Burger;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Burger>
 */
class BurgerRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Burger::class);
    }

    /**
     * Retourne tous les burgers non archivés
     */
    public function findAllActive(): array
    {
        return $this->createQueryBuilder('b')
            ->andWhere('b.isArchived = :val')
            ->setParameter('val', false)
            ->orderBy('b.nom', 'ASC')
            ->getQuery()
            ->getResult();
    }
}