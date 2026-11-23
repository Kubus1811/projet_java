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

    public int getNbExemplaires() {
        return nbExemplaires;
    }

    public String versFichier() {
        return nbExemplaires + " : " + numero;
    }

    @Override
    public String toString() {
        return nbExemplaires + " exemplaire(s) de " + numero;
    }
}
