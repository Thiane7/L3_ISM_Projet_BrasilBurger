FROM php:8.4-apache

# Installation des dépendances système et extensions PHP
RUN apt-get update && apt-get install -y \
    libpq-dev \
    git \
    unzip \
    libzip-dev \
    && docker-php-ext-install pdo pdo_pgsql zip

# Configuration d'Apache
RUN a2enmod rewrite
COPY . /var/www/html
WORKDIR /var/www/html

# Configuration de l'environnement Symfony
ENV APP_ENV=prod

# Installation de Composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

# Création du dossier var et gestion des permissions
RUN mkdir -p /var/www/html/var && chown -R www-data:www-data /var/www/html/var

# Installation des dépendances Symfony (sans les outils de debug)
RUN composer install --no-dev --optimize-autoloader



# Configuration de la racine web
ENV APACHE_DOCUMENT_ROOT /var/www/html/public
RUN sed -ri -e 's!/var/www/html!${APACHE_DOCUMENT_ROOT}!g' /etc/apache2/sites-available/*.conf
RUN sed -ri -e 's!/var/www/html!${APACHE_DOCUMENT_ROOT}!g' /etc/apache2/apache2.conf /etc/apache2/conf-available/*.conf


RUN echo "<Directory /var/www/html/public>\n\
    Options Indexes FollowSymLinks\n\
    AllowOverride All\n\
    Require all granted\n\
</Directory>" > /etc/apache2/conf-available/symfony.conf \
    && a2enconf symfony


EXPOSE 80