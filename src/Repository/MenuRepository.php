<?php
namespace App\Repository;

use App\Entity\Menu;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Menu>
 */
class MenuRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Menu::class);
    }

    public function findAllActive(): array
    {
        return $this->createQueryBuilder('m')
            ->andWhere('m.isArchived = :val')
            ->setParameter('val', false)
            ->orderBy('m.nom', 'ASC')
            ->getQuery()
            ->getResult();
    }
}