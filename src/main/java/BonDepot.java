import java.time.LocalDate;

public class BonDepot {
    private static int prochainNumero = 1;

    private int numero;
    private String telephone;
    private LocalDate dateEmission;
    private LigneDepot[] lignes;
    private int nbLignes;

    public BonDepot(String telephone) {
        this.numero = prochainNumero;
        prochainNumero++;
        this.telephone = telephone;
        this.dateEmission = LocalDate.now();
        this.lignes = new LigneDepot[20];
        this.nbLignes = 0;
    }

    // bon relu du fichier : le compteur doit depasser son numero
    public BonDepot(int numero, String telephone, LocalDate dateEmission) {
        this.numero = numero;
        this.telephone = telephone;
        this.dateEmission = dateEmission;
        this.lignes = new LigneDepot[20];
        this.nbLignes = 0;
        if (numero >= prochainNumero) {
            prochainNumero = numero + 1;
        }
    }

    public boolean ajouterLigne(String numeroArticle, int nbExemplaires) {
        if (nbLignes == lignes.length || nbExemplaires <= 0) {
            return false;
        }
        lignes[nbLignes] = new LigneDepot(numeroArticle, nbExemplaires);
        nbLignes++;
        return true;
    }

    public boolean contientArticle(String numeroArticle) {
        for (int i = 0; i < nbLignes; i++) {
            if (lignes[i].getNumero().equals(numeroArticle)) {
                return true;
            }
        }
        return false;
    }

    public void afficher() {
        System.out.println("Bon no " + numero + " du " + dateEmission + " | tel : " + telephone
                + " | " + nbLignes + " article(s)");
        for (int i = 0; i < nbLignes; i++) {
            System.out.println("    " + lignes[i]);
        }
    }

    public String versFichier() {
        String texte = numero + System.lineSeparator() + telephone + " : " + dateEmission + " : " + nbLignes;
        for (int i = 0; i < nbLignes; i++) {
            texte = texte + System.lineSeparator() + lignes[i].versFichier();
        }
        return texte;
    }

    public String getTelephone() {
        return telephone;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public LigneDepot getLigne(int i) {
        return lignes[i];
    }

    public int getNbLignes() {
        return nbLignes;
    }
}
