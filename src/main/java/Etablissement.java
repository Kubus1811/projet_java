import java.time.LocalDate;
import java.util.Arrays;

public class Etablissement {
    private String nom;
    private Article[] articles;
    private int nbArticles;
    private BonDepot[] bonsDepot;
    private int nbBonsDepot;

    public Etablissement(String nom) {
        this.nom = nom;
        this.articles = new Article[100];
        this.nbArticles = 0;
        this.bonsDepot = new BonDepot[100];
        this.nbBonsDepot = 0;
    }

    public void afficher() {
        System.out.println("=== Boutique : " + nom + " ===");
        System.out.println("Articles en catalogue (" + nbArticles + ") :");
        if (nbArticles == 0) {
            System.out.println("  Aucun article.");
        } else {
            for (int i = 0; i < nbArticles; i++) {
                System.out.println("  " + (i + 1) + ". " + articles[i]);
            }
        }

        System.out.println("\nBons de depot emis (" + nbBonsDepot + ") :");
        if (nbBonsDepot == 0) {
            System.out.println("  Aucun bon de depot.");
        } else {
            for (int i = 0; i < nbBonsDepot; i++) {
                bonsDepot[i].afficher();
            }
        }
    }

    // Insertion triee par ordre lexicographique selon ISBN/ISSN (TP3 - Question 4)
    public void insererArticleTrie(Article a) {
        if (a == null) return;

        // Si l'article existe deja, on incremente son stock
        Article existant = rechercher(a.getNumero());
        if (existant != null) {
            existant.ajouter(a.getNbExemplaires());
            return;
        }

        if (nbArticles >= articles.length) {
            articles = Arrays.copyOf(articles, articles.length * 2);
        }

        // Recherche de l'emplacement avec placerApres
        int index = 0;
        while (index < nbArticles && a.placerApres(articles[index])) {
            index++;
        }

        // Decalage des elements
        for (int i = nbArticles; i > index; i--) {
            articles[i] = articles[i - 1];
        }

        articles[index] = a;
        nbArticles++;
    }

    // Ajout d'un livre (TP3 - Question 4.b)
    public void ajouter(String description, double prix, int nbExemplaires, String isbn, int nbPages) {
        Livre l = new Livre(description, prix, nbExemplaires, isbn, nbPages);
        insererArticleTrie(l);
    }

    // Ajout d'un manuel scolaire (TP3 - Question 4.b)
    public void ajouter(String description, double prix, int nbExemplaires, String isbn, int nbPages,
                        String matiere, String niveauScolaire) {
        Manuel m = new Manuel(description, prix, nbExemplaires, isbn, nbPages, matiere, niveauScolaire);
        insererArticleTrie(m);
    }

    // Ajout d'un magazine (TP3 - Question 4.b)
    public void ajouter(String description, double prix, int nbExemplaires, String issn,
                        String periodicite, LocalDate datePublication) {
        Magazine mag = new Magazine(description, prix, nbExemplaires, issn, periodicite, datePublication);
        insererArticleTrie(mag);
    }

    // Recherche par reference ISBN ou ISSN (TP3 - Question 4.c)
    public Article rechercher(String numero) {
        if (numero == null) return null;
        for (int i = 0; i < nbArticles; i++) {
            if (articles[i].getNumero().equalsIgnoreCase(numero.trim())) {
                return articles[i];
            }
        }
        return null;
    }

    // Augmentation de stock (TP3 - Question 4.c)
    public boolean ajouter(String numero, int quantite) {
        Article a = rechercher(numero);
        if (a != null && quantite > 0) {
            a.ajouter(quantite);
            return true;
        }
        return false;
    }

    // Diminution de stock (TP3 - Question 4.c)
    public boolean retirer(String numero, int quantite) {
        Article a = rechercher(numero);
        if (a != null) {
            return a.retirer(quantite);
        }
        return false;
    }

    // Insertion triee des bons par date d'emission (le plus recent en dernier) (TP3 - Question 5)
    public void insererBonTrie(BonDepot bon) {
        if (bon == null) return;

        if (nbBonsDepot >= bonsDepot.length) {
            bonsDepot = Arrays.copyOf(bonsDepot, bonsDepot.length * 2);
        }

        int index = 0;
        while (index < nbBonsDepot && !bon.getDateEmission().isBefore(bonsDepot[index].getDateEmission())) {
            index++;
        }

        for (int i = nbBonsDepot; i > index; i--) {
            bonsDepot[i] = bonsDepot[i - 1];
        }

        bonsDepot[index] = bon;
        nbBonsDepot++;
    }

    // Enregistrement d'un bon de depot et mise a jour du stock (TP3 - Question 5.b)
    public void ajouter(BonDepot bon) {
        if (bon == null) return;
        insererBonTrie(bon);

        for (int i = 0; i < bon.getNbLignes(); i++) {
            LigneDepot ligne = bon.getLignes()[i];
            Article a = rechercher(ligne.getNumero());
            if (a != null) {
                a.ajouter(ligne.getNbExemplaires());
            } else {
                System.out.println("Note : L'article " + ligne.getNumero() + " n'est pas encore au catalogue.");
            }
        }
    }

    // --- TP4 - Question 1 : Methodes de consultation et de tri ---

    // Affichage des articles ranges par nombre d'exemplaires croissant avec prix du jour (TP4 - Question 1.a)
    public void lister() {
        System.out.println("=== Articles par nombre d'exemplaires croissant (prix du jour) ===");
        if (nbArticles == 0) {
            System.out.println("Aucun article en stock.");
            return;
        }

        // Copie des articles pour ne pas perturber le tri principal
        Article[] copie = new Article[nbArticles];
        for (int i = 0; i < nbArticles; i++) {
            copie[i] = articles[i];
        }

        // Tri a bulles par quantite croissante
        for (int i = 0; i < nbArticles - 1; i++) {
            for (int j = 0; j < nbArticles - i - 1; j++) {
                if (copie[j].getNbExemplaires() > copie[j + 1].getNbExemplaires()) {
                    Article tmp = copie[j];
                    copie[j] = copie[j + 1];
                    copie[j + 1] = tmp;
                }
            }
        }

        for (int i = 0; i < nbArticles; i++) {
            Article a = copie[i];
            System.out.printf("%d. [%s] %s | Stock: %d | Prix du jour: %.2f euros%n",
                    (i + 1), a.getNumero(), a.getDescription(), a.getNbExemplaires(), a.calculerPrix());
        }
    }

    // Affichage des bons d'un client ranges par date (TP4 - Question 1.b)
    public void lister(String telephone) {
        System.out.println("=== Bons de depot du client " + telephone + " ===");
        int count = 0;
        for (int i = 0; i < nbBonsDepot; i++) {
            if (bonsDepot[i].getTelephone().equalsIgnoreCase(telephone.trim())) {
                bonsDepot[i].afficher();
                count++;
            }
        }
        if (count == 0) {
            System.out.println("Aucun bon trouve pour ce client.");
        }
    }

    // Affichage des bons pour un article donne sur une periode (TP4 - Question 1.c)
    public void lister(String numero, LocalDate dateDebut, LocalDate dateFin) {
        System.out.println("=== Bons pour l'article " + numero + " (" + dateDebut + " au " + dateFin + ") ===");
        int count = 0;
        for (int i = 0; i < nbBonsDepot; i++) {
            BonDepot b = bonsDepot[i];
            boolean dansPeriode = !b.getDateEmission().isBefore(dateDebut) && !b.getDateEmission().isAfter(dateFin);
            if (dansPeriode && b.contientArticle(numero)) {
                b.afficher();
                count++;
            }
        }
        if (count == 0) {
            System.out.println("Aucun bon correspondant a ces criteres.");
        }
    }

    public String getNom() {
        return nom;
    }

    public Article[] getArticles() {
        return articles;
    }

    public int getNbArticles() {
        return nbArticles;
    }

    public BonDepot[] getBonsDepot() {
        return bonsDepot;
    }

    public int getNbBonsDepot() {
        return nbBonsDepot;
    }
}
