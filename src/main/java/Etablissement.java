import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

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
        System.out.println(nom + " : " + nbArticles + " article(s), " + nbBonsDepot + " bon(s) de depot");
        for (int i = 0; i < nbArticles; i++) {
            System.out.println(articles[i]);
        }
        for (int i = 0; i < nbBonsDepot; i++) {
            bonsDepot[i].afficher();
        }
    }

    public Article rechercher(String numero) {
        for (int i = 0; i < nbArticles; i++) {
            if (articles[i].getNumero().equals(numero)) {
                return articles[i];
            }
        }
        return null;
    }

    private boolean insererArticle(Article a) {
        if (nbArticles == articles.length || rechercher(a.getNumero()) != null) {
            return false;
        }
        // on decale vers la droite les articles qui doivent etre apres le nouveau
        int i = nbArticles;
        while (i > 0 && articles[i - 1].placerApres(a)) {
            articles[i] = articles[i - 1];
            i--;
        }
        articles[i] = a;
        nbArticles++;
        return true;
    }

    public boolean ajouter(String description, double prix, int nbExemplaires, String isbn, int nbPages) {
        return insererArticle(new Livre(description, prix, nbExemplaires, isbn, nbPages));
    }

    public boolean ajouter(String description, double prix, int nbExemplaires, String isbn, int nbPages,
                           String matiere, String niveauScolaire) {
        return insererArticle(new Manuel(description, prix, nbExemplaires, isbn, nbPages,
                matiere, niveauScolaire));
    }

    public boolean ajouter(String description, double prix, int nbExemplaires, String issn,
                           String periodicite, LocalDate datePublication) {
        return insererArticle(new Magazine(description, prix, nbExemplaires, issn,
                periodicite, datePublication));
    }

    public boolean ajouter(String numero, int quantite) {
        Article a = rechercher(numero);
        if (a == null) {
            return false;
        }
        return a.ajouter(quantite);
    }

    public boolean retirer(String numero, int quantite) {
        Article a = rechercher(numero);
        if (a == null) {
            return false;
        }
        return a.retirer(quantite);
    }

    private boolean insererBon(BonDepot bon) {
        if (nbBonsDepot == bonsDepot.length) {
            return false;
        }
        int i = nbBonsDepot;
        while (i > 0 && bonsDepot[i - 1].getDateEmission().isAfter(bon.getDateEmission())) {
            bonsDepot[i] = bonsDepot[i - 1];
            i--;
        }
        bonsDepot[i] = bon;
        nbBonsDepot++;
        return true;
    }

    public boolean ajouter(BonDepot bon) {
        if (!insererBon(bon)) {
            return false;
        }
        for (int i = 0; i < bon.getNbLignes(); i++) {
            LigneDepot ligne = bon.getLigne(i);
            ajouter(ligne.getNumero(), ligne.getNbExemplaires());
        }
        return true;
    }

    public void lister() {
        System.out.println("Articles par stock croissant :");
        // tri a bulles sur une copie, pour ne pas casser le tri par numero
        Article[] copie = new Article[nbArticles];
        for (int i = 0; i < nbArticles; i++) {
            copie[i] = articles[i];
        }
        for (int i = 0; i < nbArticles - 1; i++) {
            for (int j = 0; j < nbArticles - 1 - i; j++) {
                if (copie[j].getNbExemplaires() > copie[j + 1].getNbExemplaires()) {
                    Article temp = copie[j];
                    copie[j] = copie[j + 1];
                    copie[j + 1] = temp;
                }
            }
        }
        for (int i = 0; i < nbArticles; i++) {
            System.out.println(copie[i] + " | prix du jour : "
                    + copie[i].calculerPrix(LocalDate.now()) + " euros");
        }
    }

    public void lister(String telephone) {
        System.out.println("Bons du client " + telephone + " :");
        for (int i = 0; i < nbBonsDepot; i++) {
            if (bonsDepot[i].getTelephone().equals(telephone)) {
                bonsDepot[i].afficher();
            }
        }
    }

    public void lister(String numero, LocalDate debut, LocalDate fin) {
        System.out.println("Bons avec l'article " + numero + " du " + debut + " au " + fin + " :");
        for (int i = 0; i < nbBonsDepot; i++) {
            LocalDate date = bonsDepot[i].getDateEmission();
            // les deux dates limites sont incluses
            if (bonsDepot[i].contientArticle(numero) && !date.isBefore(debut) && !date.isAfter(fin)) {
                bonsDepot[i].afficher();
            }
        }
    }

    public void versFichierArticles(String nomFichier) {
        try {
            FileWriter fichier = new FileWriter(nomFichier);
            for (int i = 0; i < nbArticles; i++) {
                // saut de ligne entre les articles seulement, pas a la fin du fichier
                if (i > 0) {
                    fichier.write(System.lineSeparator());
                }
                fichier.write(articles[i].versFichier());
            }
            fichier.close();
        } catch (IOException e) {
            System.out.println("Impossible d'ecrire dans " + nomFichier);
        }
    }

    public void depuisFichierArticles(String nomFichier) {
        try {
            BufferedReader fichier = new BufferedReader(new FileReader(nomFichier));
            String numero = fichier.readLine();
            while (numero != null && !numero.equals("")) {
                String[] infos = fichier.readLine().split(" : ");
                String description = infos[0];
                double prix = Double.parseDouble(infos[1]);
                int nbExemplaires = Integer.parseInt(infos[2]);
                // le nombre d'infos donne le type : 4 = livre, 6 = manuel, 5 = magazine
                if (infos.length == 4) {
                    ajouter(description, prix, nbExemplaires, numero, Integer.parseInt(infos[3]));
                } else if (infos.length == 6) {
                    ajouter(description, prix, nbExemplaires, numero, Integer.parseInt(infos[3]),
                            infos[4], infos[5]);
                } else {
                    ajouter(description, prix, nbExemplaires, numero, infos[3], LocalDate.parse(infos[4]));
                }
                numero = fichier.readLine();
            }
            fichier.close();
        } catch (IOException e) {
            System.out.println("Pas de fichier " + nomFichier);
        }
    }

    public void versFichierDepots(String nomFichier) {
        try {
            FileWriter fichier = new FileWriter(nomFichier);
            for (int i = 0; i < nbBonsDepot; i++) {
                if (i > 0) {
                    fichier.write(System.lineSeparator());
                }
                fichier.write(bonsDepot[i].versFichier());
            }
            fichier.close();
        } catch (IOException e) {
            System.out.println("Impossible d'ecrire dans " + nomFichier);
        }
    }

    public void depuisFichierDepots(String nomFichier) {
        try {
            BufferedReader fichier = new BufferedReader(new FileReader(nomFichier));
            String numeroBon = fichier.readLine();
            while (numeroBon != null && !numeroBon.equals("")) {
                String[] infos = fichier.readLine().split(" : ");
                BonDepot bon = new BonDepot(Integer.parseInt(numeroBon), infos[0],
                        LocalDate.parse(infos[1]));
                int nbLignes = Integer.parseInt(infos[2]);
                for (int i = 0; i < nbLignes; i++) {
                    // dans le fichier : nombre d'exemplaires puis numero
                    String[] infosLigne = fichier.readLine().split(" : ");
                    bon.ajouterLigne(infosLigne[1], Integer.parseInt(infosLigne[0]));
                }
                // insererBon et pas ajouter : le stock du fichier articles compte deja ces depots
                insererBon(bon);
                numeroBon = fichier.readLine();
            }
            fichier.close();
        } catch (IOException e) {
            System.out.println("Pas de fichier " + nomFichier);
        }
    }
}
