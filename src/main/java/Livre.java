import java.time.LocalDate;

public class Livre extends Article {
    private String isbn;
    private int nbPages;

    public Livre(String description, double prix, int nbExemplaires, String isbn, int nbPages) {
        super(description, prix, nbExemplaires);
        this.isbn = isbn;
        this.nbPages = nbPages;
    }

    @Override
    public String getNumero() {
        return isbn;
    }

    @Override
    public double calculerPrix(LocalDate date) {
        if (date.getMonthValue() == 4) {
            return prix / 2;
        }
        return prix;
    }

    @Override
    public String versFichier() {
        return super.versFichier() + " : " + nbPages;
    }

    @Override
    public String toString() {
        return "ISBN " + isbn + " | " + super.toString() + " | " + nbPages + " pages";
    }
}
