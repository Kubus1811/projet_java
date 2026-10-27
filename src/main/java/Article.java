import java.time.LocalDate;

public abstract class Article {
    protected String description;
    protected double prix;
    protected int nbExemplaires;

    public Article(String description, double prix, int nbExemplaires) {
        this.description = description;
        this.prix = prix;
        this.nbExemplaires = nbExemplaires;
    }

    // Retourne l'identifiant (ISBN ou ISSN)
    public abstract String getNumero();

    // Compare l'ordre lexicographique des numeros
    public boolean placerApres(Article a) {
        if (a == null) {
            return false;
        }
        return this.getNumero().compareTo(a.getNumero()) > 0;
    }

    public void ajouter(int quantite) {
        if (quantite > 0) {
            this.nbExemplaires += quantite;
        }
    }

    public boolean retirer(int quantite) {
        if (quantite > 0 && this.nbExemplaires >= quantite) {
            this.nbExemplaires -= quantite;
            return true;
        }
        return false;
    }

    public abstract double calculerPrix(LocalDate date);

    public double calculerPrix() {
        return calculerPrix(LocalDate.now());
    }

    public abstract String versFichier();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getNbExemplaires() {
        return nbExemplaires;
    }

    public void setNbExemplaires(int nbExemplaires) {
        this.nbExemplaires = nbExemplaires;
    }

    @Override
    public String toString() {
        return "Description: " + description +
               " | Prix initial: " + String.format("%.2f", prix) + " euros" +
               " | Exemplaires: " + nbExemplaires;
    }
}
