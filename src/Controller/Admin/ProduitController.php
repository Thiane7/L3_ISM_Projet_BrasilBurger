<?php

namespace App\Controller\Admin;

use App\Entity\Burger;
use App\Entity\Menu;
use App\Entity\Complement;
use App\Form\BurgerType;
use App\Form\MenuType;
use App\Form\ComplementType;
use App\Repository\BurgerRepository;
use App\Repository\MenuRepository;
use App\Repository\ComplementRepository;
use Doctrine\ORM\EntityManagerInterface;
use Doctrine\DBAL\Connection;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\String\Slugger\SluggerInterface;
use Symfony\Component\HttpFoundation\File\Exception\FileException;

#[Route('/admin/produits')]
class ProduitController extends AbstractController
{
    #[Route('/', name: 'admin_produit_index')]
    public function index(
        BurgerRepository $burgerRepo, 
        MenuRepository $menuRepo, 
        ComplementRepository $complementRepo
    ): Response {
        return $this->render('admin/produit/index.html.twig', [
            'burgers' => $burgerRepo->findBy(['isArchived' => false]),
            'menus' => $menuRepo->findBy(['isArchived' => false]),
            'complements' => $complementRepo->findBy(['isArchived' => false]),
        ]);
    }

    // --- ARCHIVAGE GÉNÉRIQUE ---
    #[Route('/archive/{type}/{id}', name: 'admin_produit_archive')]
    public function archive(
        string $type, 
        int $id, 
        EntityManagerInterface $em, 
        BurgerRepository $br, 
        MenuRepository $mr, 
        ComplementRepository $cr
    ): Response {
        $entity = match($type) {
            'burger' => $br->find($id),
            'menu' => $mr->find($id),
            'complement' => $cr->find($id),
            default => null
        };

        if ($entity) {
            $entity->setIsArchived(true);
            $em->flush();
            $this->addFlash('success', "Le produit a été archivé avec succès.");
        }
        return $this->redirectToRoute('admin_produit_index');
    }

    // --- GESTION DES BURGERS ---
    #[Route('/nouveau-burger', name: 'admin_burger_new')]
    #[Route('/modifier-burger/{id}', name: 'admin_burger_edit')]
    public function editBurger(
        Burger $burger = null, 
        Request $request, 
        EntityManagerInterface $em, 
        SluggerInterface $slugger
    ): Response {
        $isEdit = $burger !== null;
        if (!$burger) $burger = new Burger();
        
        $form = $this->createForm(BurgerType::class, $burger);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->handleFileUpload($form, $burger, $slugger);
            $em->persist($burger);
            $em->flush();

            $this->addFlash('success', 'Burger enregistré !');
            return $this->redirectToRoute('admin_produit_index');
        }

        return $this->render('admin/produit/edit.html.twig', [
            'form' => $form->createView(),
            'isEdit' => $isEdit,
            'type' => 'Burger',
            'item' => $burger
        ]);
    }

    // --- GESTION DES COMPLÉMENTS ---
    #[Route('/nouveau-complement', name: 'admin_complement_new')]
    #[Route('/modifier-complement/{id}', name: 'admin_complement_edit')]
    public function editComplement(
        Complement $complement = null, 
        Request $request, 
        EntityManagerInterface $em, 
        SluggerInterface $slugger
    ): Response {
        $isEdit = $complement !== null;
        if (!$complement) $complement = new Complement();

        $form = $this->createForm(ComplementType::class, $complement);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->handleFileUpload($form, $complement, $slugger);
            $em->persist($complement);
            $em->flush();

            $this->addFlash('success', 'Complément enregistré !');
            return $this->redirectToRoute('admin_produit_index');
        }

        return $this->render('admin/produit/edit.html.twig', [
            'form' => $form->createView(),
            'isEdit' => $isEdit,
            'type' => 'Complément',
            'item' => $complement
        ]);
    }

    // --- GESTION DES MENUS ---
    #[Route('/nouveau-menu', name: 'admin_menu_new')]
    #[Route('/modifier-menu/{id}', name: 'admin_menu_edit')]
    public function editMenu(
        Menu $menu = null, 
        Request $request, 
        EntityManagerInterface $em, 
        SluggerInterface $slugger,
        Connection $connection 
    ): Response {
        $isEdit = $menu !== null;
        if (!$menu) $menu = new Menu();

        $form = $this->createForm(MenuType::class, $menu);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->handleFileUpload($form, $menu, $slugger);
            $em->persist($menu);
            $em->flush();

            // Gestion de la composition Neon
            $burger = $form->get('burger')->getData();
            $complements = $form->get('complements')->getData();
            $idB = null; $idF = null;

            foreach ($complements as $comp) {
                if (method_exists($comp, 'getTypeComplement') && $comp->getTypeComplement() === 'Boisson') {
                    $idB = $comp->getId();
                } else {
                    $idF = $comp->getId();
                }
            }

            try {
                if ($isEdit) {
                    $connection->executeStatement(
                        'UPDATE COMPOSITION_MENUS SET id_burger = ?, id_boisson = ?, id_frites = ? WHERE id_menu = ?',
                        [$burger->getId(), $idB ?? 0, $idF ?? 0, $menu->getId()]
                    );
                } else {
                    $connection->executeStatement(
                        'INSERT INTO COMPOSITION_MENUS (id_menu, id_burger, id_boisson, id_frites) VALUES (?, ?, ?, ?)',
                        [$menu->getId(), $burger->getId(), $idB ?? 0, $idF ?? 0]
                    );
                }
                $this->addFlash('success', 'Menu enregistré avec sa composition !');
            } catch (\Exception $e) {
                $this->addFlash('warning', 'Menu créé, mais erreur de composition : ' . $e->getMessage());
            }

            return $this->redirectToRoute('admin_produit_index');
        }

        return $this->render('admin/produit/edit.html.twig', [
            'form' => $form->createView(),
            'isEdit' => $isEdit,
            'type' => 'Menu',
            'item' => $menu
        ]);
    }

    // --- UTILITAIRE UPLOAD ---
    private function handleFileUpload($form, $entity, SluggerInterface $slugger): void
    {
        $imageFile = $form->get('image')->getData();
        if ($imageFile) {
            $newFilename = $slugger->slug($entity->getNom()).'-'.uniqid().'.'.$imageFile->guessExtension();
            try {
                $imageFile->move(
                    $this->getParameter('kernel.project_dir').'/public/uploads/images',
                    $newFilename
                );
                $entity->setImage($newFilename);
            } catch (FileException $e) {
                $this->addFlash('error', "Erreur d'upload de l'image");
            }
        }
    }
}