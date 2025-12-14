"# L3 ISM Projet Brasil Burger" 
## 🛠 Compilation et Exécution

Ce projet est basé sur **Maven** et nécessite **Java 17 ou supérieur**.

### 1. Compilation du Projet

Pour compiler le projet et générer le fichier JAR exécutable (shaded JAR) dans le dossier `java/java_console/target/`, utilisez la commande suivante depuis le répertoire `java/java_console/` :

```bash
mvn clean install


2. Exécution de l'Application
# Placez-vous dans le répertoire du JAR
cd java/java_console

# Exécutez le JAR
java -jar target/java_console-1.0-SNAPSHOT-shaded.jar