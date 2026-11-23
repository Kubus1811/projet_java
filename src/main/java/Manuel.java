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
        return super.versFichier() + " : " + matiere + " : " + niveauScolaire;
    }

    @Override
    public String toString() {
        return super.toString() + " | " + matiere + " | " + niveauScolaire;
    }
}
