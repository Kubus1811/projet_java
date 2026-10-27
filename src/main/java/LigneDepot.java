public class LigneDepot {
    private String numero;
    private int nbExemplaires;

    public LigneDepot(String numero, int nbExemplaires) {
        this.numero = numero;
        this.nbExemplaires = nbExemplaires;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getNbExemplaires() {
        return nbExemplaires;
    }

    public void setNbExemplaires(int nbExemplaires) {
        this.nbExemplaires = nbExemplaires;
    }

    @Override
    public String toString() {
        return "Reference: " + numero + " | Nombre d'exemplaires: " + nbExemplaires;
    }
}
