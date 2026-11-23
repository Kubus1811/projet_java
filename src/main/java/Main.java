import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static final String FICHIER_ARTICLES = "articles.txt";
    private static final String FICHIER_DEPOTS = "depots.txt";
    private static final int MAX = 1000000;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Etablissement boutique = new Etablissement("Boutique Solidaire");
        boutique.depuisFichierArticles(FICHIER_ARTICLES);
        boutique.depuisFichierDepots(FICHIER_DEPOTS);

        int choix;
        do {
            afficherMenu();
            choix = lireEntier("Choix : ", 0, 9);
            switch (choix) {
                case 1:
                    boutique.afficher();
                    break;
                case 2:
                    boutique.lister();
                    break;
                case 3:
                    rechercher(boutique);
                    break;
                case 4:
                    ajouterArticle(boutique);
                    break;
                case 5:
                    changerStock(boutique, true);
                    break;
                case 6:
                    changerStock(boutique, false);
                    break;
                case 7:
                    enregistrerBon(boutique);
                    break;
                case 8:
                    boutique.lister(lireNumero("Telephone du client : "));
                    break;
                case 9:
                    listerPeriode(boutique);
                    break;
            }
        } while (choix != 0);

        boutique.versFichierArticles(FICHIER_ARTICLES);
        boutique.versFichierDepots(FICHIER_DEPOTS);
        System.out.println("Au revoir.");
    }

    private static void afficherMenu() {
        System.out.println();
        System.out.println("1. Afficher la boutique");
        System.out.println("2. Lister les articles par stock");
        System.out.println("3. Rechercher un article");
        System.out.println("4. Ajouter un article");
        System.out.println("5. Augmenter un stock");
        System.out.println("6. Diminuer un stock");
        System.out.println("7. Enregistrer un bon de depot");
        System.out.println("8. Lister les bons d'un client");
        System.out.println("9. Lister les bons d'un article sur une periode");
        System.out.println("0. Quitter");
    }

    private static void rechercher(Etablissement boutique) {
        Article a = boutique.rechercher(lireNumero("ISBN ou ISSN : "));
        if (a == null) {
            System.out.println("Article inconnu.");
        } else {
            System.out.println(a);
        }
    }

    private static void ajouterArticle(Etablissement boutique) {
        String numero = lireNumero("ISBN ou ISSN : ");
        if (boutique.rechercher(numero) != null) {
            System.out.println("Refuse : numero deja utilise.");
        } else {
            int nbExemplaires = lireEntier("Nombre d'exemplaires : ", 0, MAX);
            creerArticle(boutique, numero, nbExemplaires);
        }
    }

    private static boolean creerArticle(Etablissement boutique, String numero, int nbExemplaires) {
        int type = lireEntier("Type (1 livre, 2 manuel, 3 magazine) : ", 1, 3);
        String description = lireTexte("Description : ");
        double prix = lirePrix("Prix : ");
        boolean ok;
        if (type == 3) {
            String periodicite = lireTexte("Periodicite (hebdomadaire, mensuel, trimestriel) : ");
            while (!periodicite.equals("hebdomadaire") && !periodicite.equals("mensuel")
                    && !periodicite.equals("trimestriel")) {
                periodicite = lireTexte("Periodicite inconnue, recommencez : ");
            }
            LocalDate datePublication = lireDate("Date de publication (AAAA-MM-JJ) : ");
            ok = boutique.ajouter(description, prix, nbExemplaires, numero, periodicite, datePublication);
        } else {
            int nbPages = lireEntier("Nombre de pages : ", 1, MAX);
            if (type == 1) {
                ok = boutique.ajouter(description, prix, nbExemplaires, numero, nbPages);
            } else {
                String matiere = lireTexte("Matiere : ");
                String niveauScolaire = lireTexte("Niveau scolaire : ");
                ok = boutique.ajouter(description, prix, nbExemplaires, numero, nbPages,
                        matiere, niveauScolaire);
            }
        }
        afficherResultat(ok, "catalogue plein");
        return ok;
    }

    private static void changerStock(Etablissement boutique, boolean ajout) {
        String numero = lireNumero("ISBN ou ISSN : ");
        int quantite = lireEntier("Quantite : ", 1, MAX);
        boolean ok;
        if (ajout) {
            ok = boutique.ajouter(numero, quantite);
        } else {
            ok = boutique.retirer(numero, quantite);
        }
        afficherResultat(ok, "article inconnu ou stock insuffisant");
    }

    private static void enregistrerBon(Etablissement boutique) {
        BonDepot bon = new BonDepot(lireNumero("Telephone du deposant : "));
        String reponse;
        do {
            String numero = lireNumero("ISBN ou ISSN de l'article depose : ");
            boolean connu = boutique.rechercher(numero) != null;
            if (!connu) {
                // nouvel article : cree avec 0 exemplaire, c'est le bon qui apporte le stock
                System.out.println("Article inconnu, il faut le creer.");
                connu = creerArticle(boutique, numero, 0);
            }
            if (connu) {
                int quantite = lireEntier("Nombre d'exemplaires deposes : ", 1, MAX);
                if (!bon.ajouterLigne(numero, quantite)) {
                    System.out.println("Refuse : le bon est plein.");
                }
            }
            reponse = lireTexte("Autre article ? (o/n) : ");
        } while (!reponse.equals("n"));
        if (boutique.ajouter(bon)) {
            bon.afficher();
        } else {
            System.out.println("Refuse : trop de bons de depot.");
        }
    }

    private static void listerPeriode(Etablissement boutique) {
        String numero = lireNumero("ISBN ou ISSN : ");
        LocalDate debut = lireDate("Date de debut (AAAA-MM-JJ) : ");
        LocalDate fin = lireDate("Date de fin (AAAA-MM-JJ) : ");
        boutique.lister(numero, debut, fin);
    }

    private static void afficherResultat(boolean ok, String raison) {
        if (ok) {
            System.out.println("C'est fait.");
        } else {
            System.out.println("Refuse : " + raison + ".");
        }
    }

    private static String lireTexte(String message) {
        System.out.print(message);
        String texte = sc.nextLine().trim();
        // un texte vide ou avec ":" casserait la relecture des fichiers
        while (texte.equals("") || texte.contains(":")) {
            System.out.print("Texte vide ou avec \":\" interdit, recommencez : ");
            texte = sc.nextLine().trim();
        }
        return texte;
    }

    // sans espaces : "978 2 7073 5674 1" et "9782707356741" sont le meme numero
    private static String lireNumero(String message) {
        return lireTexte(message).replace(" ", "");
    }

    private static int lireEntier(String message, int min, int max) {
        while (true) {
            System.out.print(message);
            try {
                int n = Integer.parseInt(sc.nextLine().trim());
                if (n >= min && n <= max) {
                    return n;
                }
                System.out.println("Il faut un nombre entre " + min + " et " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Ce n'est pas un nombre entier.");
            }
        }
    }

    private static double lirePrix(String message) {
        while (true) {
            System.out.print(message);
            try {
                double prix = Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
                if (prix >= 0 && prix <= MAX) {
                    return prix;
                }
                System.out.println("Il faut un prix entre 0 et " + MAX + ".");
            } catch (NumberFormatException e) {
                System.out.println("Ce n'est pas un nombre.");
            }
        }
    }

    private static LocalDate lireDate(String message) {
        while (true) {
            System.out.print(message);
            try {
                return LocalDate.parse(sc.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Date invalide, le format est AAAA-MM-JJ.");
            }
        }
    }
}
