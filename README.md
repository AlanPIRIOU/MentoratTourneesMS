<h1 align="center">Bienvenue dans le projet entrepots-api 👋</h1>
<p>
  <a href="https://github.com/ugieiris/entrepots-api#readme" target="_blank">
    <img alt="Documentation" src="https://img.shields.io/badge/documentation-yes-brightgreen.svg" />
  </a>
  <a href="https://github.com/ugieiris/entrepots-api/graphs/commit-activity" target="_blank">
    <img alt="Maintenance" src="https://img.shields.io/badge/Maintained%3F-yes-green.svg" />
  </a>
</p>

> API Mentorat.<br />

## Glossaire

[Voir le glossaire](docs/glossary.md)

## Technologies

Cette API est développée avec les technologies suivantes :

- `Java 11 :` le langage

- `SpringBoot 2 :` le cadre de développement

- `Docker / Kubernetes :` l'application est conteneurisée et déployée sur un cluster Kubernetes, les logs sont émis dans
  la console uniquement et les variables d'environnements sont utilisées pour paramétrer l'application

- `Flyway :` pour gérer le versionning du schéma de la BDD

- `u-iris-back-java :` socle technique Iris pour ajouter l'authentification, l'actuator, ...

- `Mockito :` développement des tests unitaires (obligatoires)

- `TestRestTemplate :` développement des tests d'intégrations (obligatoires)

- `Springdoc :` annotations permettant la génération d'un fichier de spécification de l'API au
  format [OpenAPI](https://www.openapis.org/).

## Getting started

### 💾 Paramétrer votre BDD

2 scripts sont à initialiser :

- `src/main/resources/db/migration/V1_ddl.sql :` script d'initialisation de la BDD utilisé à la fois par **Flyway** pour
  initialiser la 1ère version de votre schéma au démarrage de l'API, et également pour initialiser votre **BDD locale
  conteneurisée**.

- `src/test/resources/IT_datas.sql :` script pout construire vos jeux de données, ce script est utilisé pour votre **BDD
  local conteneurisée** et pour les **TIs**.

### 📦 Paramétrer vos variables d'environnements

Pour paramétrer votre application, vous devez utiliser des variables d'environnement, pour cela :

- dans les fichiers properties, les propriétés qui varient selon l'environnement doivent référencer une variable d'
  environnement grâce à la notation :
  `ma.propriete=${MA_VARIABLE_ENVIRONNEMENT}`

- le fichier `src/main/resources/env.properties` permet de valoriser les variables d'environnements pour vos
  développements.

### 🐳 Démarrage de la BDD en local avec Docker

- Démarrage

```sh
docker-compose up
```

- Arrêt

```sh
docker-compose down
```

### 🎬 Démarrage de l'api

Installation des dépendances

```sh
$ mvn clean install
```

Lancement en ligne de commande

```sh
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

Pour lancer l'api en local sous Intellij, il fait indiquer à SpringBoot d'utiliser le profil dev au démarrage de votre
application, ajouter la VM options suivantes :

```sh
-Dspring.profiles.active=dev
```

### 🔧 Exemple présent dans l'enveloppe

Un exemple de CRUD est déjà présent dans ce projet : Sample. Il présente la façon la plus simple de faire du CRUD, ainsi
que l'utilisation des annotations [SpringDoc](https://springdoc.org/) permettant de générer le fichier OpenAPI.

##Liens complémentaires

- 📄 [Smoke tests](http://wikidev.groupement.systeme-u.fr/wikidev/doku.php/tran/java/usine/iris-pipelines-testrunner)
- 📄 [Tests unitaires](http://wikidev.groupement.systeme-u.fr/wikidev/doku.php/tran/java/socle/tests_unitaires)
-

📄 [Test d'intégration avec Springboot 2](http://wikidev.groupement.systeme-u.fr/wikidev/doku.php/tran/java/socle/tests_d_integration_avec_springboot_2)
-
📄 [Migrer un projet existant en Java 11](https://wikidev.groupement.systeme-u.fr/wikidev/doku.php/tran/java/faq/migrer_un_projet_existant_en_java_11)
-
📄 [Conteneuriser et migrer votre API Spring Boot sous Kubernetes](https://wikidev.groupement.systeme-u.fr/wikidev/doku.php/tran/java/faq/conteneuriser_et_migrer_votre_api_spring_boot_sur_kubernetes)
-
📄 [Framework Flywaydb](https://wikidev.groupement.systeme-u.fr/wikidev/doku.php/tran/java/faq/comment_mettre_en_place_flyway_pour_gerer_la_bdd_de_son_application?s[]=flyway)

- 📁 [Descripteurs Kubernetes de base](https://github.com/ugieiris/k8s-deploy-base/tree/master/apps)
- 📁 [Descripteurs Kubernetes d'intégration](https://github.com/ugieiris/k8s-deploy-int/tree/master/ONPREM/apps)
- 📗 [Concevoir une API avec Stoplight](https://drive.google.com/drive/folders/1Pnd1cSZ5yEiqdTo5kLqEWEJhLwIU4Yqv)
-

🐋 [Installer Docker sous Windows 10](https://wikidev.groupement.systeme-u.fr/wikidev/doku.php/tran/java/faq/configuration_docker_dev)
