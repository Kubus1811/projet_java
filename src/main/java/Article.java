import java.time.LocalDate;

public abstract class Article {
    private String description;
    protected double prix;
    private int nbExemplaires;

    public Article(String description, double prix, int nbExemplaires) {
        this.description = description;
        this.prix = prix;
        this.nbExemplaires = nbExemplaires;
    }

    public abstract String getNumero();

    public abstract double calculerPrix(LocalDate date);

    public boolean placerApres(Article a) {
        return getNumero().compareTo(a.getNumero()) > 0;
    }

    public boolean ajouter(int quantite) {
        if (quantite <= 0) {
            return false;
        }
        nbExemplaires = nbExemplaires + quantite;
        return true;
    }

    public boolean retirer(int quantite) {
        if (quantite <= 0 || quantite > nbExemplaires) {
            return false;
        }
        nbExemplaires = nbExemplaires - quantite;
        return true;
    }

    public int getNbExemplaires() {
        return nbExemplaires;
    }

    public String versFichier() {
        return getNumero() + System.lineSeparator() + description + " : " + prix + " : " + nbExemplaires;
    }

    @Override
    public String toString() {
        return description + " | prix : " + prix + " euros | stock : " + nbExemplaires;
    }
}
