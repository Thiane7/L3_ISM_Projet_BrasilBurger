<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

final class Version20250101000000 extends AbstractMigration
{
    public function getDescription(): string
    {
        return 'Add nom and prenom to GESTIONNAIRES table';
    }

    public function up(Schema $schema): void
    {
        // Add columns only if they don't exist to avoid errors
        $this->addSql('ALTER TABLE GESTIONNAIRES ADD COLUMN IF NOT EXISTS nom VARCHAR(50)');
        $this->addSql('ALTER TABLE GESTIONNAIRES ADD COLUMN IF NOT EXISTS prenom VARCHAR(50)');
    }

    public function down(Schema $schema): void
    {
        $this->addSql('ALTER TABLE GESTIONNAIRES DROP COLUMN nom');
        $this->addSql('ALTER TABLE GESTIONNAIRES DROP COLUMN prenom');
    }
}
