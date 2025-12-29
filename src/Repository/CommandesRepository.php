<?php

namespace App\Repository;

use App\Entity\Commandes;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class CommandesRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Commandes::class);
    }

    public function findByFilters($statut, $date, $search)
    {
        $qb = $this->createQueryBuilder('c')
            ->join('c.client', 'u')
            ->orderBy('c.dateCommande', 'DESC');

        if ($statut && $statut !== 'Tous') {
            $qb->andWhere('c.statut = :statut')->setParameter('statut', $statut);
        }

        if ($date) {
            $qb->andWhere('c.dateCommande LIKE :date')->setParameter('date', $date . '%');
        }

        if ($search) {
            $qb->andWhere('u.nom LIKE :search OR u.prenom LIKE :search OR u.telephone LIKE :search')
               ->setParameter('search', '%' . $search . '%');
        }

        return $qb->getQuery()->getResult();
    }
}