<?php
namespace App\Repository;

use App\Entity\Complement;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Complement>
 */
class ComplementRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Complement::class);
    }

    public function findAllActive(): array
    {
        return $this->createQueryBuilder('c')
            ->andWhere('c.isArchived = :val')
            ->setParameter('val', false)
            ->orderBy('c.nom', 'ASC')
            ->getQuery()
            ->getResult();
    }
}