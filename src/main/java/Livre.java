import java.time.LocalDate;

public class Livre extends Article {
    protected String isbn;
    protected int nbPages;

    public Livre(String description, double prix, int nbExemplaires, String isbn, int nbPages) {
        super(description, prix, nbExemplaires);
        this.isbn = isbn;
        this.nbPages = nbPages;
    }

    @Override
    public String getNumero() {
        return this.isbn;
    }

    // Reduction de 50% au mois d'avril
    @Override
    public double calculerPrix(LocalDate date) {
        if (date != null && date.getMonthValue() == 4) {
            return this.prix * 0.5;
        }
        return this.prix;
    }

    @Override
    public String versFichier() {
        return this.isbn + System.lineSeparator() +
               this.description + " : " + this.prix + " : " + this.nbExemplaires + " : " + this.nbPages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getNbPages() {
        return nbPages;
    }

    public void setNbPages(int nbPages) {
        this.nbPages = nbPages;
    }

    @Override
    public String toString() {
        return "[Livre] ISBN: " + isbn +
               " | Titre: " + description +
               " | Prix initial: " + String.format("%.2f", prix) + " euros" +
               " | Prix actuel: " + String.format("%.2f", calculerPrix()) + " euros" +
               " | Exemplaires: " + nbExemplaires +
               " | Pages: " + nbPages;
    }
}
