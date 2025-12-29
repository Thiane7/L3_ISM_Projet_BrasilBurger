<?php
    
namespace App\Controller\Admin;

use App\Repository\CommandesRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/admin/commandes')]
class CommandeGestionController extends AbstractController
{
    #[Route('/', name: 'app_admin_commandes_index')]
    public function index(Request $request, CommandesRepository $repo): Response
    {
       
        $statut = $request->query->get('etat', 'Tous');
        $date = $request->query->get('date');
        $search = $request->query->get('search');

        $commandes = $repo->findByFilters($statut, $date, $search);

        return $this->render('admin/commandes/index.html.twig', [
            'commandes' => $commandes,
        ]);
    }

    #[Route('/{id}/status/{newStatus}', name: 'app_admin_commandes_change_status')]
    public function changeStatus(int $id, string $newStatus, CommandesRepository $repo, EntityManagerInterface $em): Response
    {
        $commande = $repo->find($id);
        if ($commande) {
            $commande->setStatut($newStatus);
            $em->flush();
            $this->addFlash('success', 'Statut mis à jour !');
        }
        return $this->redirectToRoute('app_admin_commandes_index');
    }
}