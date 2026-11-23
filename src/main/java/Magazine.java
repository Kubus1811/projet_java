import java.time.LocalDate;

public class Magazine extends Article {
    private String issn;
    private String periodicite;
    private LocalDate datePublication;

    public Magazine(String description, double prix, int nbExemplaires, String issn,
                    String periodicite, LocalDate datePublication) {
        super(description, prix, nbExemplaires);
        this.issn = issn;
        this.periodicite = periodicite;
        this.datePublication = datePublication;
    }

    @Override
    public String getNumero() {
        return issn;
    }

    @Override
    public double calculerPrix(LocalDate date) {
        LocalDate limite50;
        LocalDate limite75;
        if (periodicite.equals("hebdomadaire")) {
            limite50 = datePublication.plusWeeks(2);
            limite75 = datePublication.plusWeeks(4);
        } else if (periodicite.equals("mensuel")) {
            limite50 = datePublication.plusMonths(2);
            limite75 = datePublication.plusMonths(4);
        } else if (periodicite.equals("trimestriel")) {
            // 1 trimestre = 3 mois
            limite50 = datePublication.plusMonths(6);
            limite75 = datePublication.plusMonths(12);
        } else {
            return prix;
        }
        if (date.isAfter(limite75)) {
            return prix / 4;
        }
        if (date.isAfter(limite50)) {
            return prix / 2;
        }
        return prix;
    }

    @Override
    public String versFichier() {
        return super.versFichier() + " : " + periodicite + " : " + datePublication;
    }

    @Override
    public String toString() {
        return "ISSN " + issn + " | " + super.toString() + " | " + periodicite + " du " + datePublication;
    }
}
