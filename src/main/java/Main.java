import java.io.File;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static final String FICHIER_ARTICLES = "articles.txt";
    private static final String FICHIER_DEPOTS = "depots.txt";

    public static void main(String[] args) {
        System.out.println("=== Gestion de la Boutique ===");

        Etablissement boutique = new Etablissement("Boutique Solidaire");

        File fArt = new File(FICHIER_ARTICLES);
        File fDep = new File(FICHIER_DEPOTS);

        // Chargement si existant, sinon initialisation avec les exemples
        if (fArt.exists() && fArt.length() > 0) {
            boutique.depuisFichierArticles(FICHIER_ARTICLES);
            if (fDep.exists() && fDep.length() > 0) {
                boutique.depuisFichierDepots(FICHIER_DEPOTS);
            }
        } else {
            System.out.println("Initialisation des donnees d'exemple...");
            initialiserExemples(boutique);
            boutique.versFichierArticles(FICHIER_ARTICLES);
            boutique.versFichierDepots(FICHIER_DEPOTS);
        }

        Scanner sc = new Scanner(System.in);
        boolean quitter = false;

        while (!quitter) {
            afficherMenu();
            System.out.print("\nChoix : ");
            String choix = sc.nextLine().trim();

            switch (choix) {
                case "1":
                    boutique.afficher();
                    break;

                case "2":
                    boutique.lister();
                    break;

                case "3":
                    System.out.print("Numero ISBN ou ISSN : ");
                    String num = sc.nextLine().trim();
                    Article a = boutique.rechercher(num);
                    if (a != null) {
                        System.out.println("Trouve : " + a);
                    } else {
                        System.out.println("Aucun article trouve pour : " + num);
                    }
                    break;

                case "4":
                    ajouterLivre(boutique, sc);
                    break;

                case "5":
                    ajouterManuel(boutique, sc);
                    break;

                case "6":
                    ajouterMagazine(boutique, sc);
                    break;

                case "7":
                    System.out.print("Reference ISBN/ISSN : ");
                    String refAdd = sc.nextLine().trim();
                    int qteAdd = lireEntier(sc, "Quantite recue : ");
                    if (boutique.ajouter(refAdd, qteAdd)) {
                        System.out.println("Stock mis a jour.");
                    } else {
                        System.out.println("Erreur : reference introuvable.");
                    }
                    break;

                case "8":
                    System.out.print("Reference ISBN/ISSN : ");
                    String refRet = sc.nextLine().trim();
                    int qteRet = lireEntier(sc, "Quantite vendue : ");
                    if (boutique.retirer(refRet, qteRet)) {
                        System.out.println("Stock mis a jour.");
                    } else {
                        System.out.println("Erreur : stock insuffisant ou reference introuvable.");
                    }
                    break;

                case "9":
                    creerBonDepot(boutique, sc);
                    break;

                case "10":
                    System.out.println("\n--- Tous les bons de depot ---");
                    for (int i = 0; i < boutique.getNbBonsDepot(); i++) {
                        boutique.getBonsDepot()[i].afficher();
                    }
                    if (boutique.getNbBonsDepot() == 0) {
                        System.out.println("Aucun bon de depot.");
                    }
                    break;

                case "11":
                    System.out.print("Numero de telephone : ");
                    String tel = sc.nextLine().trim();
                    boutique.lister(tel);
                    break;

                case "12":
                    System.out.print("Reference ISBN ou ISSN : ");
                    String refRech = sc.nextLine().trim();
                    LocalDate d1 = lireDate(sc, "Date de debut (AAAA-MM-JJ) : ");
                    LocalDate d2 = lireDate(sc, "Date de fin (AAAA-MM-JJ) : ");
                    boutique.lister(refRech, d1, d2);
                    break;

                case "13":
                    boutique.versFichierArticles(FICHIER_ARTICLES);
                    boutique.versFichierDepots(FICHIER_DEPOTS);
                    break;

                case "0":
                    System.out.println("Sauvegarde avant fermeture...");
                    boutique.versFichierArticles(FICHIER_ARTICLES);
                    boutique.versFichierDepots(FICHIER_DEPOTS);
                    System.out.println("Au revoir !");
                    quitter = true;
                    break;

                default:
                    System.out.println("Choix invalide.");
                    break;
            }

            if (!quitter) {
                System.out.println("\n[Entree pour continuer]");
                sc.nextLine();
            }
        }

        sc.close();
    }

    private static void afficherMenu() {
        System.out.println("\n----------- MENU -----------");
        System.out.println("1.  Afficher le catalogue");
        System.out.println("2.  Lister les articles par stock croissant");
        System.out.println("3.  Rechercher un article");
        System.out.println("4.  Ajouter un livre");
        System.out.println("5.  Ajouter un manuel scolaire");
        System.out.println("6.  Ajouter un magazine");
        System.out.println("7.  Augmenter le stock d'un article");
        System.out.println("8.  Diminuer le stock d'un article");
        System.out.println("9.  Enregistrer un bon de depot");
        System.out.println("10. Afficher tous les bons de depot");
        System.out.println("11. Lister les bons d'un client (telephone)");
        System.out.println("12. Lister les bons d'un article sur une periode");
        System.out.println("13. Sauvegarder dans les fichiers");
        System.out.println("0.  Quitter");
        System.out.println("----------------------------");
    }

    private static void initialiserExemples(Etablissement boutique) {
        boutique.ajouter("La maison vide", 25.0, 10, "9782707356741", 752);
        boutique.ajouter("Nouvelle grammaire du college", 38.0, 31, "9782210446694", 512, "Francais", "College");
        boutique.ajouter("Geo", 6.50, 20, "00208245", "mensuel", LocalDate.of(2024, 8, 1));

        BonDepot bon1 = new BonDepot(1, "061236547890", LocalDate.of(2025, 10, 10));
        bon1.ajouterLigne("9782210446694", 10);
        bon1.ajouterLigne("9782707356741", 20);
        boutique.insererBonTrie(bon1);
    }

    private static void ajouterLivre(Etablissement boutique, Scanner sc) {
        System.out.println("\n--- Ajout d'un livre ---");
        System.out.print("Titre : ");
        String titre = sc.nextLine().trim();
        double prix = lireDouble(sc, "Prix (euros) : ");
        int stock = lireEntier(sc, "Stock : ");
        System.out.print("ISBN : ");
        String isbn = sc.nextLine().trim();
        int pages = lireEntier(sc, "Pages : ");

        boutique.ajouter(titre, prix, stock, isbn, pages);
        System.out.println("Livre ajoute.");
    }

    private static void ajouterManuel(Etablissement boutique, Scanner sc) {
        System.out.println("\n--- Ajout d'un manuel scolaire ---");
        System.out.print("Titre : ");
        String titre = sc.nextLine().trim();
        double prix = lireDouble(sc, "Prix (euros) : ");
        int stock = lireEntier(sc, "Stock : ");
        System.out.print("ISBN : ");
        String isbn = sc.nextLine().trim();
        int pages = lireEntier(sc, "Pages : ");
        System.out.print("Matiere : ");
        String matiere = sc.nextLine().trim();
        System.out.print("Niveau : ");
        String niveau = sc.nextLine().trim();

        boutique.ajouter(titre, prix, stock, isbn, pages, matiere, niveau);
        System.out.println("Manuel ajoute.");
    }

    private static void ajouterMagazine(Etablissement boutique, Scanner sc) {
        System.out.println("\n--- Ajout d'un magazine ---");
        System.out.print("Titre : ");
        String titre = sc.nextLine().trim();
        double prix = lireDouble(sc, "Prix (euros) : ");
        int stock = lireEntier(sc, "Stock : ");
        System.out.print("ISSN : ");
        String issn = sc.nextLine().trim();
        System.out.print("Periodicite (hebdomadaire, mensuel, trimestriel) : ");
        String periodicite = sc.nextLine().trim();
        LocalDate date = lireDate(sc, "Date de publication (AAAA-MM-JJ) : ");

        boutique.ajouter(titre, prix, stock, issn, periodicite, date);
        System.out.println("Magazine ajoute.");
    }

    private static void creerBonDepot(Etablissement boutique, Scanner sc) {
        System.out.println("\n--- Nouveau bon de depot ---");
        System.out.print("Telephone du deposant : ");
        String tel = sc.nextLine().trim();

        BonDepot bon = new BonDepot(tel);

        boolean encore = true;
        while (encore) {
            System.out.print("Reference de l'article (ISBN ou ISSN) : ");
            String ref = sc.nextLine().trim();
            int qte = lireEntier(sc, "Nombre d'exemplaires : ");

            bon.ajouterLigne(ref, qte);

            System.out.print("Autre article pour ce bon ? (o/n) : ");
            String rep = sc.nextLine().trim();
            if (!rep.equalsIgnoreCase("o")) {
                encore = false;
            }
        }

        boutique.ajouter(bon);
        System.out.println("Bon de depot enregistre :");
        bon.afficher();
    }

    private static int lireEntier(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entier invalide, reessayez.");
            }
        }
    }

    private static double lireDouble(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Nombre invalide, reessayez.");
            }
        }
    }

    private static LocalDate lireDate(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            try {
                return LocalDate.parse(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Date invalide (format attendu AAAA-MM-JJ).");
            }
        }
    }
}
