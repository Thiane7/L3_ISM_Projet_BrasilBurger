<?php

namespace App\Form;

use App\Entity\Menu;
use App\Entity\Burger;
use App\Entity\Complement;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\MoneyType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\File;
use Symfony\Component\Validator\Constraints\NotBlank;

class MenuType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom', TextType::class, [
                'attr' => ['class' => 'form-control', 'placeholder' => 'Nom du menu'],
                'constraints' => [
                    new NotBlank(message: 'Le nom est obligatoire'),
                ],
            ])
            ->add('prix', MoneyType::class, [
                'currency' => 'XOF',
                'attr' => ['class' => 'form-control'],
                'constraints' => [
                    new NotBlank(message: 'Le prix est obligatoire'),
                ],
            ])
            ->add('burger', EntityType::class, [
                'class' => Burger::class,
                'choice_label' => 'nom',
                'placeholder' => 'Choisir un burger',
                'attr' => ['class' => 'form-select'],
              
                'mapped' => false, 
                'constraints' => [
                    new NotBlank(message: 'Veuillez sélectionner un burger'),
                ],
            ])
            ->add('complements', EntityType::class, [
                'class' => Complement::class,
                'choice_label' => 'nom',
                'multiple' => true,
                'expanded' => true,
                'label' => 'Compléments inclus',
                
                'mapped' => false,
            ])
            ->add('image', FileType::class, [
                'label' => 'Image du menu',
                'mapped' => false,
                'required' => false,
                'attr' => ['class' => 'form-control'],
                'constraints' => [
                    new File(
                        maxSize: '2M',
                        mimeTypes: ['image/jpeg', 'image/png'],
                        mimeTypesMessage: 'Veuillez uploader une image JPG ou PNG valide'
                    )
                ],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Menu::class,
        ]);
    }
}