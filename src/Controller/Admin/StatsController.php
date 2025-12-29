<?php

namespace App\Controller\Admin;

use App\Repository\CommandesRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class StatsController extends AbstractController
{
    #[Route('/admin/stats', name: 'app_admin_stats')]
    public function index(CommandesRepository $repo): Response
    {
        
        $stats = [
            'en_cours' => $repo->countByStatusAndDay('EN_COURS'),
            'validees' => $repo->countByStatusAndDay('VALIDE'),
            'annulees' => $repo->countByStatusAndDay('ANNULE'),
            'recette'  => $repo->getDailyRevenue(),
        ];

       
        $modes = $repo->getConsumptionModeStats();
        $modeLabels = array_column($modes, 'mode');
        $modeData = array_column($modes, 'total');

       
        $topProducts = $repo->getTopProducts(5);
        $productLabels = array_column($topProducts, 'nom');
        $productData = array_column($topProducts, 'total');

        return $this->render('admin/stats/index.html.twig', [
            'stats' => $stats,
            'modeLabels' => json_encode($modeLabels),
            'modeData' => json_encode($modeData),
            'productLabels' => json_encode($productLabels),
            'productData' => json_encode($productData),
        ]);
    }
}