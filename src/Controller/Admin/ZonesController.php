<?php

namespace App\Controller\Admin;

use App\Entity\Zones;
use App\Form\ZonesType;
use App\Repository\ZonesRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/admin/zones')]
class ZonesController extends AbstractController
{
   
    #[Route('/', name: 'app_zones_index', methods: ['GET'])]
    public function index(ZonesRepository $zonesRepository): Response
    {
        return $this->render('admin/zones/index.html.twig', [
            'zones' => $zonesRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_zones_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $zone = new Zones();
        $form = $this->createForm(ZonesType::class, $zone);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($zone);
            $entityManager->flush();

            $this->addFlash('success', 'La zone a été créée avec succès !');
            return $this->redirectToRoute('app_zones_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('admin/zones/new.html.twig', [
            'zone' => $zone,
            'form' => $form,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_zones_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Zones $zone, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(ZonesType::class, $zone);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            $this->addFlash('success', 'La zone a été mise à jour !');
            return $this->redirectToRoute('app_zones_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('admin/zones/edit.html.twig', [
            'zone' => $zone,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_zones_delete', methods: ['POST'])]
    public function delete(Request $request, Zones $zone, EntityManagerInterface $entityManager): Response
    {
     
        if ($this->isCsrfTokenValid('delete'.$zone->getIdZone(), $request->request->get('_token'))) {
            $entityManager->remove($zone);
            $entityManager->flush();
            $this->addFlash('danger', 'La zone a été supprimée.');
        }

        return $this->redirectToRoute('app_zones_index', [], Response::HTTP_SEE_OTHER);
    }
}