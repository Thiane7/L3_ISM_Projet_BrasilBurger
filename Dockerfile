FROM php:8.4-apache

# Installation des extensions PHP nécessaires pour PostgreSQL
RUN apt-get update && apt-get install -y libpq-dev \
    && docker-php-ext-install pdo pdo_pgsql

# Configuration d'Apache
RUN a2enmod rewrite
COPY . /var/www/html
WORKDIR /var/www/html

# Installation de Composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

# Installation des dépendances (PHP 8.4 satisfera Symfony 8.0)
RUN composer install --no-dev --optimize-autoloader
# Correction des permissions pour éviter les erreurs d'écriture
RUN chown -R www-data:www-data /var/www/html/var



# On définit le dossier public de Symfony comme racine web
ENV APACHE_DOCUMENT_ROOT /var/www/html/public
RUN sed -ri -e 's!/var/www/html!${APACHE_DOCUMENT_ROOT}!g' /etc/apache2/sites-available/*.conf
RUN sed -ri -e 's!/var/www/html!${APACHE_DOCUMENT_ROOT}!g' /etc/apache2/apache2.conf /etc/apache2/conf-available/*.conf

EXPOSE 80