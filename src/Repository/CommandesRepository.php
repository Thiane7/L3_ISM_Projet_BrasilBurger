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
    
    public function countByStatusAndDay(string $status): int
    {
        $today = new \DateTime('today');
        $tomorrow = new \DateTime('tomorrow');

        return (int) $this->createQueryBuilder('c')
            ->select('count(c.idCommande)')
            ->where('c.statut = :status')
            ->andWhere('c.dateCommande >= :today')
            ->andWhere('c.dateCommande < :tomorrow')
            ->setParameter('status', $status)
            ->setParameter('today', $today)
            ->setParameter('tomorrow', $tomorrow)
            ->getQuery()
            ->getSingleScalarResult();
    }

    
    public function getDailyRevenue(): float
    {
        $today = new \DateTime('today');

        return (float) $this->createQueryBuilder('c')
            ->select('SUM(c.montantTotal)')
            ->where('c.statut = :status')
            ->andWhere('c.dateCommande >= :today')
            ->setParameter('status', 'VALIDE')
            ->setParameter('today', $today)
            ->getQuery()
            ->getSingleScalarResult() ?? 0;
    }

    
    public function getConsumptionModeStats(): array
    {
        return $this->createQueryBuilder('c')
            ->select('c.modeConsommation as mode, COUNT(c.idCommande) as total')
            ->where('c.dateCommande >= :today')
            ->setParameter('today', new \DateTime('today'))
            ->groupBy('c.modeConsommation')
            ->getQuery()
            ->getResult();
    }

    public function getTopProducts(int $limit = 5): array
    {
        $conn = $this->getEntityManager()->getConnection();

        $sql = '
            SELECT nom, SUM(total_vendu) as total
            FROM (
                SELECT b.nom, SUM(lcb.quantite) as total_vendu
                FROM LIGNE_COMMANDES_BURGER lcb
                JOIN BURGERS b ON lcb.id_burger = b.id_burger
                JOIN COMMANDES c ON lcb.id_commande = c.id_commande
                WHERE c.statut = :status AND c.date_commande >= :today
                GROUP BY b.nom
                
                UNION ALL
                
                SELECT m.nom, SUM(lcm.quantite) as total_vendu
                FROM LIGNE_COMMANDES_MENU lcm
                JOIN MENUS m ON lcm.id_menu = m.id_menu
                JOIN COMMANDES c ON lcm.id_commande = c.id_commande
                WHERE c.statut = :status AND c.date_commande >= :today
                GROUP BY m.nom
            ) as resultats
            GROUP BY nom
            ORDER BY total DESC
            LIMIT :limit
        ';

        $stmt = $conn->prepare($sql);
        $result = $stmt->executeQuery([
            'today' => (new \DateTime('today'))->format('Y-m-d H:i:s'),
            'status' => 'VALIDE',
            'limit' => $limit
        ]);

        return $result->fetchAllAssociative();
    }
}