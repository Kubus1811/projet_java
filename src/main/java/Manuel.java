public class Manuel extends Livre {
    private String matiere;
    private String niveauScolaire;

    public Manuel(String description, double prix, int nbExemplaires, String isbn, int nbPages,
                  String matiere, String niveauScolaire) {
        super(description, prix, nbExemplaires, isbn, nbPages);
        this.matiere = matiere;
        this.niveauScolaire = niveauScolaire;
    }

    @Override
    public String versFichier() {
        return this.isbn + System.lineSeparator() +
               this.description + " : " + this.prix + " : " + this.nbExemplaires + " : " +
               this.nbPages + " : " + this.matiere + " : " + this.niveauScolaire;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getNiveauScolaire() {
        return niveauScolaire;
    }

    public void setNiveauScolaire(String niveauScolaire) {
        this.niveauScolaire = niveauScolaire;
    }

    @Override
    public String toString() {
        return "[Manuel Scolaire] ISBN: " + isbn +
               " | Titre: " + description +
               " | Matiere: " + matiere +
               " | Niveau: " + niveauScolaire +
               " | Prix initial: " + String.format("%.2f", prix) + " euros" +
               " | Prix actuel: " + String.format("%.2f", calculerPrix()) + " euros" +
               " | Exemplaires: " + nbExemplaires +
               " | Pages: " + nbPages;
    }
}
