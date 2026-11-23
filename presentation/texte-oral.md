# Texte de l'oral — Gestion de stock d'une boutique solidaire

## Répartition

Cinq orateurs, deux minutes chacun, onze slides. Chaque passage fait environ 260 mots, soit deux minutes à un débit normal.

| Orateur | Slides | Sujet | Durée |
| --- | --- | --- | --- |
| Orateur 1 | 1 à 3 | Introduction, plan et sujet | 2 min |
| Orateur 2 | 4 et 5 | Les classes et le calcul des prix | 2 min |
| Orateur 3 | 6 et 7 | Etablissement et bons de dépôt | 2 min |
| Orateur 4 | 8 et 9 | Fichiers texte et menu | 2 min |
| Orateur 5 | 10 et 11 | Tests et bilan | 2 min |

Remplacez « Orateur 1 » à « Orateur 5 » par vos prénoms. La phrase de passage à la fin de chaque partie sert à donner la parole au suivant.

## Orateur 1 : introduction et sujet

**Slide 1, titre**

Bonjour à tous. Nous allons vous présenter notre projet de programmation objet en Java : un programme de gestion de stock pour une boutique solidaire d'articles culturels d'occasion.

**Slide 2, plan**

Notre présentation suit cinq parties. Je commence par le sujet. Ensuite, Orateur 2 présentera les classes et le calcul des prix. Orateur 3 expliquera la classe Etablissement et les bons de dépôt. Orateur 4 parlera des fichiers et du menu. Enfin, Orateur 5 présentera nos tests et le bilan.

**Slide 3, le sujet**

La boutique récupère des dons de particuliers et des invendus, puis les revend à petit prix. Elle propose trois types d'articles : des livres, des manuels scolaires et des magazines. Tous les articles ont une description, un prix initial et un nombre d'exemplaires. Un livre a en plus un numéro ISBN et un nombre de pages. Un manuel est un livre, avec une matière et un niveau scolaire. Un magazine a un numéro ISSN, une périodicité et une date de publication.

Le programme doit faire quatre choses. D'abord gérer le catalogue : ajouter des articles, augmenter ou diminuer le stock. Ensuite calculer le prix du jour, parce que des réductions s'appliquent selon la date. Il doit aussi garder une trace de chaque dépôt, avec un bon de dépôt numéroté et daté automatiquement. Enfin, tout doit être sauvegardé dans deux fichiers texte et rechargé au démarrage. Le tout est piloté par un menu en mode console.

Je laisse la parole à Orateur 2 pour la conception.

## Orateur 2 : les classes et le calcul des prix

**Slide 4, les classes**

Nous avons écrit huit classes. Au centre, il y a la classe Article. Elle est abstraite : elle regroupe ce qui est commun à tous les articles, c'est-à-dire la description, le prix et le nombre d'exemplaires. On ne crée jamais un article tout court, on crée toujours un livre, un manuel ou un magazine. Livre et Magazine héritent d'Article, et Manuel hérite de Livre, puisqu'un manuel est un livre avec deux informations en plus.

Deux méthodes sont abstraites dans Article : getNumero, qui renvoie l'ISBN ou l'ISSN, et calculerPrix. Chaque sous-classe les écrit à sa façon. Grâce à ça, la boutique manipule un seul tableau d'articles, sans se demander de quel type ils sont.

À droite, la classe Etablissement contient le tableau des articles et le tableau des bons de dépôt. Un bon de dépôt contient lui-même un tableau de lignes.

**Slide 5, les prix**

Le prix dépend de la date. Pour un livre ou un manuel, c'est simple : moitié prix pendant tout le mois d'avril. Pour un magazine, la réduction dépend de son ancienneté et de sa périodicité : moins cinquante pour cent après deux semaines pour un hebdomadaire, deux mois pour un mensuel et deux trimestres pour un trimestriel. Et moins soixante-quinze pour cent après quatre.

Dans le code, on calcule deux dates limites avec plusWeeks ou plusMonths, puis on compare avec isAfter. Par exemple, notre magazine Geo est un mensuel publié en août 2024. Il a largement dépassé quatre mois, donc il passe de 6,50 euros à 1,625 euro.

Orateur 3 va maintenant expliquer comment la boutique range tous ces articles.

## Orateur 3 : Etablissement et bons de dépôt

**Slide 6, un tableau toujours trié**

La classe Etablissement stocke les articles dans un tableau de cent cases, avec un compteur qui dit combien de cases sont utilisées. L'énoncé demande que ce tableau reste trié par numéro ISBN ou ISSN. Plutôt que de trier à chaque fois, on insère chaque nouvel article directement à sa place.

On part de la fin du tableau, et on décale d'une case vers la droite tous les articles qui doivent être après le nouveau. Pour comparer, on utilise la méthode placerApres, qui s'appuie sur compareTo. Quand on arrive sur un article plus petit, on s'arrête et on pose le nouvel article dans la case libre. Avant d'insérer, on vérifie que le tableau n'est pas plein et que le numéro n'existe pas déjà.

La méthode rechercher parcourt le tableau et renvoie l'article, ou null s'il n'existe pas. Elle sert partout : pour augmenter un stock, pour le diminuer, et pour les bons de dépôt.

**Slide 7, les bons de dépôt**

À chaque dépôt, on crée un bon. Son numéro vient d'un compteur static dans la classe BonDepot, donc partagé par tous les bons : la numérotation est automatique et continue. La date est celle du jour. Chaque ligne du bon donne un numéro d'article et une quantité. Si l'article déposé n'est pas encore au catalogue, le programme le crée avec zéro exemplaire : c'est le bon qui apporte le stock.

Quand on enregistre le bon, il est inséré dans le tableau des bons, trié par date, avec le même principe de décalage. Puis le stock de chaque article déposé est augmenté.

Pour consulter, nous avons trois méthodes lister : les articles par stock croissant avec leur prix du jour, les bons d'un client à partir de son téléphone, et les bons d'un article sur une période.

Orateur 4 va présenter la sauvegarde.

## Orateur 4 : fichiers texte et menu

**Slide 8, la sauvegarde**

Toutes les données sont sauvegardées dans deux fichiers texte, au format imposé par l'énoncé. Dans articles.txt, chaque article occupe deux lignes : le numéro, puis les informations séparées par espace, deux-points, espace.

Chaque classe a une méthode versFichier qui renvoie sa partie du texte. Livre appelle celle d'Article avec super et ajoute le nombre de pages. Manuel appelle celle de Livre et ajoute la matière et le niveau.

Pour relire, on utilise un BufferedReader et la méthode split. Le fichier ne dit pas le type de l'article, alors on compte les informations : quatre pour un livre, cinq pour un magazine, six pour un manuel.

Pour les bons, un détail est important. À la relecture, on range le bon dans le tableau sans toucher au stock, parce que le stock enregistré dans le fichier des articles tient déjà compte de ces dépôts. Sinon chaque dépôt serait compté deux fois.

**Slide 9, le menu**

Le programme principal charge les deux fichiers, affiche un menu de neuf choix dans une boucle, et sauvegarde quand on quitte.

Nous avons séparé les rôles : la classe Main s'occupe des saisies et des messages, et les autres classes font les traitements et renvoient vrai ou faux.

Toutes les saisies sont contrôlées. Un texte vide est refusé. Le caractère deux-points aussi, puisqu'il sert de séparateur dans les fichiers. Un nombre hors limites ou une date mal écrite sont redemandés. Et on enlève les espaces des numéros, pour qu'un ISBN tapé avec ou sans espaces désigne le même article.

Je passe la parole à Orateur 5 pour les tests.

## Orateur 5 : tests et bilan

**Slide 10, les tests**

Pour vérifier le programme, nous avons repris l'énoncé question par question : les huit questions du TP3 et du TP4 sont réalisées, avec les noms de classes et de méthodes demandés. Le programme compile sans aucun avertissement.

Nous avons écrit une classe Tests, qui crée des articles et des bons à la main et compare vingt et un résultats avec les valeurs attendues. Elle vérifie par exemple les prix le jour de chaque seuil de réduction et le lendemain.

Nous avons ensuite joué des scénarios complets au clavier : ajout de chaque type d'article, modification du stock, retrait refusé quand le stock est insuffisant, bon de dépôt, puis fermeture et relance. Après la relance, on retrouve exactement les mêmes données, et le bon suivant prend bien le numéro suivant. Les fichiers produits respectent le modèle de l'énoncé. Les mauvaises saisies sont redemandées sans arrêter le programme.

**Slide 11, le bilan**

Pour conclure : le programme répond à tout ce qui était demandé, avec un code volontairement simple, seulement des tableaux et les classes vues en cours.

Il a quand même des limites. Le catalogue est limité à cent articles et cent bons. On ne peut pas supprimer un article ni corriger sa description. Et le caractère deux-points est interdit dans les textes.

Avec plus de temps, nous ajouterions la suppression et la modification d'un article, une vérification de la longueur des numéros ISBN et ISSN, et une interface graphique.

Ce projet nous a surtout permis de pratiquer l'héritage, les classes abstraites et la lecture de fichiers. Merci de votre attention, nous sommes prêts à répondre à vos questions.
