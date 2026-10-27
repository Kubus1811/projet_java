import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
        return this.issn;
    }

    // Reductions selon anciennete et periodicite
    @Override
    public double calculerPrix(LocalDate date) {
        if (date == null || datePublication == null) {
            return this.prix;
        }

        double reduction = 0.0;
        String p = periodicite.toLowerCase();

        if (p.startsWith("hebdo")) {
            long semaines = datePublication.until(date, ChronoUnit.WEEKS);
            if (semaines >= 4) {
                reduction = 0.75;
            } else if (semaines >= 2) {
                reduction = 0.50;
            }
        } else if (p.startsWith("mensuel")) {
            long mois = datePublication.until(date, ChronoUnit.MONTHS);
            if (mois >= 4) {
                reduction = 0.75;
            } else if (mois >= 2) {
                reduction = 0.50;
            }
        } else if (p.startsWith("trimestr")) {
            long mois = datePublication.until(date, ChronoUnit.MONTHS);
            long trimestres = mois / 3;
            if (trimestres >= 4) {
                reduction = 0.75;
            } else if (trimestres >= 2) {
                reduction = 0.50;
            }
        }

        return this.prix * (1.0 - reduction);
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    @Override
    public String toString() {
        return "[Magazine] ISSN: " + issn +
               " | Titre: " + description +
               " | Periodicite: " + periodicite +
               " | Date publication: " + datePublication +
               " | Prix initial: " + String.format("%.2f", prix) + " euros" +
               " | Prix actuel: " + String.format("%.2f", calculerPrix()) + " euros" +
               " | Exemplaires: " + nbExemplaires;
    }
}
