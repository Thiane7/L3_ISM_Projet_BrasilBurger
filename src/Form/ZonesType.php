<?php
namespace App\Form;

use App\Entity\Zones;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class ZonesType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nomZone', TextType::class, [
                'label' => 'Nom de la Zone (ex: Zone A)',
                'attr' => ['class' => 'form-control', 'placeholder' => 'Entrez le nom...']
            ])
            ->add('prixLivraison', NumberType::class, [
                'label' => 'Prix de livraison (FCFA)',
                'attr' => ['class' => 'form-control']
            ])
            ->add('quartiers', TextareaType::class, [
                'label' => 'Quartiers (séparez par des virgules)',
                'attr' => ['class' => 'form-control', 'rows' => 3, 'placeholder' => 'Plateau, Médina, etc.']
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults(['data_class' => Zones::class]);
    }
}