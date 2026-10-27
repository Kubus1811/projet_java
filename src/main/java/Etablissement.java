import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    // Insertion triée par ordre lexicographique selon ISBN/ISSN
    public void insererArticleTrie(Article a) {
        if (a == null) return;

        // Si l'article existe déjà, on incrémente son stock
        Article existant = rechercher(a.getNumero());
        if (existant != null) {
            existant.ajouter(a.getNbExemplaires());
            return;
        }

        if (nbArticles >= articles.length) {
            articles = Arrays.copyOf(articles, articles.length * 2);
        }

        // On cherche l'emplacement avec placerApres
        int index = 0;
        while (index < nbArticles && a.placerApres(articles[index])) {
            index++;
        }

        // Décalage des éléments
        for (int i = nbArticles; i > index; i--) {
            articles[i] = articles[i - 1];
        }

        articles[index] = a;
        nbArticles++;
    }

    // Ajout d'un livre
    public void ajouter(String description, double prix, int nbExemplaires, String isbn, int nbPages) {
        Livre l = new Livre(description, prix, nbExemplaires, isbn, nbPages);
        insererArticleTrie(l);
    }

    // Ajout d'un manuel scolaire
    public void ajouter(String description, double prix, int nbExemplaires, String isbn, int nbPages,
                        String matiere, String niveauScolaire) {
        Manuel m = new Manuel(description, prix, nbExemplaires, isbn, nbPages, matiere, niveauScolaire);
        insererArticleTrie(m);
    }

    // Ajout d'un magazine
    public void ajouter(String description, double prix, int nbExemplaires, String issn,
                        String periodicite, LocalDate datePublication) {
        Magazine mag = new Magazine(description, prix, nbExemplaires, issn, periodicite, datePublication);
        insererArticleTrie(mag);
    }

    // Recherche par référence ISBN ou ISSN
    public Article rechercher(String numero) {
        if (numero == null) return null;
        for (int i = 0; i < nbArticles; i++) {
            if (articles[i].getNumero().equalsIgnoreCase(numero.trim())) {
                return articles[i];
            }
        }
        return null;
    }

    // Augmentation de stock
    public boolean ajouter(String numero, int quantite) {
        Article a = rechercher(numero);
        if (a != null && quantite > 0) {
            a.ajouter(quantite);
            return true;
        }
        return false;
    }

    // Diminution de stock
    public boolean retirer(String numero, int quantite) {
        Article a = rechercher(numero);
        if (a != null) {
            return a.retirer(quantite);
        }
        return false;
    }

    // Insertion triée des bons par date d'émission (le plus récent en dernier)
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

    // Enregistrement d'un bon de dépôt et mise à jour du stock
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

    // Affichage des articles rangés par nombre d'exemplaires croissant
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

        // Tri à bulles classique
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
            System.out.printf("%d. [%s] %s | Stock: %d | Prix: %.2f euros%n",
                    (i + 1), a.getNumero(), a.getDescription(), a.getNbExemplaires(), a.calculerPrix());
        }
    }

    // Affichage des bons d'un client
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
            System.out.println("Aucun bon trouve pour ce numero.");
        }
    }

    // Affichage des bons pour un article sur une periode
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

    // Sauvegarde des articles dans un fichier texte
    public void versFichierArticles(String nomFichier) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(nomFichier);
            for (int i = 0; i < nbArticles; i++) {
                fw.write(articles[i].versFichier());
                if (i < nbArticles - 1) {
                    fw.write(System.lineSeparator());
                }
            }
            System.out.println("Articles sauvegardes dans " + nomFichier);
        } catch (IOException e) {
            System.out.println("Erreur sauvegarde articles : " + e.getMessage());
        } finally {
            if (fw != null) {
                try { fw.close(); } catch (IOException e) {}
            }
        }
    }

    // Chargement des articles depuis un fichier texte
    public void depuisFichierArticles(String nomFichier) {
        File f = new File(nomFichier);
        if (!f.exists()) return;

        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String num;
            while ((num = br.readLine()) != null) {
                num = num.trim();
                if (num.isEmpty()) continue;

                String ligne = br.readLine();
                if (ligne == null) break;

                String[] tokens = ligne.split(" : ");
                if (tokens.length == 5) {
                    // Magazine
                    String desc = tokens[0];
                    double prix = Double.parseDouble(tokens[1]);
                    int stock = Integer.parseInt(tokens[2]);
                    String per = tokens[3];
                    LocalDate date = LocalDate.parse(tokens[4]);
                    ajouter(desc, prix, stock, num, per, date);
                } else if (tokens.length == 6) {
                    // Manuel
                    String desc = tokens[0];
                    double prix = Double.parseDouble(tokens[1]);
                    int stock = Integer.parseInt(tokens[2]);
                    int pages = Integer.parseInt(tokens[3]);
                    String mat = tokens[4];
                    String niv = tokens[5];
                    ajouter(desc, prix, stock, num, pages, mat, niv);
                } else if (tokens.length == 4) {
                    // Livre
                    String desc = tokens[0];
                    double prix = Double.parseDouble(tokens[1]);
                    int stock = Integer.parseInt(tokens[2]);
                    int pages = Integer.parseInt(tokens[3]);
                    ajouter(desc, prix, stock, num, pages);
                }
            }
            System.out.println("Articles charges depuis " + nomFichier);
        } catch (Exception e) {
            System.out.println("Erreur chargement articles : " + e.getMessage());
        } finally {
            try {
                if (br != null) br.close();
                if (fr != null) fr.close();
            } catch (IOException e) {}
        }
    }

    // Sauvegarde des bons de depot dans un fichier texte
    public void versFichierDepots(String nomFichier) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(nomFichier);
            for (int i = 0; i < nbBonsDepot; i++) {
                fw.write(bonsDepot[i].versFichier());
                if (i < nbBonsDepot - 1) {
                    fw.write(System.lineSeparator());
                }
            }
            System.out.println("Bons de depot sauvegardes dans " + nomFichier);
        } catch (IOException e) {
            System.out.println("Erreur sauvegarde bons : " + e.getMessage());
        } finally {
            if (fw != null) {
                try { fw.close(); } catch (IOException e) {}
            }
        }
    }

    // Chargement des bons de depot depuis un fichier texte
    public void depuisFichierDepots(String nomFichier) {
        File f = new File(nomFichier);
        if (!f.exists()) return;

        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String ligneNum;
            while ((ligneNum = br.readLine()) != null) {
                ligneNum = ligneNum.trim();
                if (ligneNum.isEmpty()) continue;

                int num = Integer.parseInt(ligneNum);
                String entete = br.readLine();
                if (entete == null) break;

                String[] pHeader = entete.split(" : ");
                String tel = pHeader[0];
                LocalDate date = LocalDate.parse(pHeader[1]);
                int nbLignes = Integer.parseInt(pHeader[2]);

                BonDepot bon = new BonDepot(num, tel, date);
                for (int i = 0; i < nbLignes; i++) {
                    String ligneArt = br.readLine();
                    if (ligneArt == null) break;
                    String[] pArt = ligneArt.split(" : ");
                    int qte = Integer.parseInt(pArt[0]);
                    String ref = pArt[1];
                    bon.ajouterLigne(ref, qte);
                }
                insererBonTrie(bon);
            }
            System.out.println("Bons de depot charges depuis " + nomFichier);
        } catch (Exception e) {
            System.out.println("Erreur chargement dépôts : " + e.getMessage());
        } finally {
            try {
                if (br != null) br.close();
                if (fr != null) fr.close();
            } catch (IOException e) {}
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
