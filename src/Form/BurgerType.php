<?php

namespace App\Form;

use App\Entity\Burger;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\MoneyType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\File;
use Symfony\Component\Validator\Constraints\NotBlank;

class BurgerType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom', TextType::class, [
                'label' => 'Nom du Burger',
                'attr' => ['placeholder' => 'Ex: Le Classic Cheeseburger'],
                'constraints' => [
                    
                    new NotBlank(message: 'Le nom est obligatoire'),
                ],
            ])
            ->add('prix', MoneyType::class, [
                'label' => 'Prix (FCFA)',
                'currency' => 'XOF',
                'constraints' => [
                   
                    new NotBlank(message: 'Le prix est obligatoire'),
                ],
            ])
            ->add('image', FileType::class, [
                'label' => 'Image du produit (JPG/PNG)',
                'mapped' => false,
                'required' => false,
                'constraints' => [
                   
                    new File(
                        maxSize: '2M',
                        mimeTypes: ['image/jpeg', 'image/png'],
                        mimeTypesMessage: 'Veuillez uploader une image JPG ou PNG valide'
                    )
                ],
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Burger::class,
        ]);
    }
}