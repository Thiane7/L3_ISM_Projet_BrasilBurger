<?php

namespace App\Controller\Admin;

use App\Repository\CommandesRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;

class StatsController extends AbstractController
{
    public function index(CommandesRepository $repo): Response
    {
        // Données des cartes
        $stats = [
            'en_cours' => $repo->countByStatusAndDay('EN_COURS'),
            'validees' => $repo->countByStatusAndDay('VALIDE'),
            'annulees' => $repo->countByStatusAndDay('ANNULE'),
            'recette'  => $repo->getDailyRevenue(),
        ];

        // Données du graphique des modes de consommation
        $modes = $repo->getConsumptionModeStats();
        $modeLabels = array_column($modes, 'mode');
        $modeData = array_column($modes, 'total');

        return $this->render('admin/stats/index.html.twig', [
            'stats' => $stats,
            'modeLabels' => json_encode($modeLabels),
            'modeData' => json_encode($modeData),
        ]);
    }
}