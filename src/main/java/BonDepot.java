import java.time.LocalDate;
import java.util.Arrays;

public class BonDepot {
    private static int compteurNumero = 1;

    private int numero;
    private String telephone;
    private LocalDate dateEmission;
    private LigneDepot[] lignes;
    private int nbLignes;

    public BonDepot(String telephone) {
        this(compteurNumero++, telephone, LocalDate.now());
    }

    public BonDepot(String telephone, LocalDate dateEmission) {
        this(compteurNumero++, telephone, dateEmission);
    }

    // Constructeur complet avec numero (utilise notamment pour initialiser un bon precis)
    public BonDepot(int numero, String telephone, LocalDate dateEmission) {
        this.numero = numero;
        this.telephone = telephone;
        this.dateEmission = dateEmission;
        this.lignes = new LigneDepot[20];
        this.nbLignes = 0;
        if (numero >= compteurNumero) {
            compteurNumero = numero + 1;
        }
    }

    public void ajouterLigne(String numero, int nbExemplaires) {
        if (numero == null || nbExemplaires <= 0) {
            return;
        }
        // Si la reference existe deja dans le bon, on cumule les exemplaires
        for (int i = 0; i < nbLignes; i++) {
            if (lignes[i].getNumero().equalsIgnoreCase(numero.trim())) {
                lignes[i].setNbExemplaires(lignes[i].getNbExemplaires() + nbExemplaires);
                return;
            }
        }
        // Agrandir le tableau si besoin
        if (nbLignes >= lignes.length) {
            lignes = Arrays.copyOf(lignes, lignes.length * 2);
        }
        lignes[nbLignes++] = new LigneDepot(numero.trim(), nbExemplaires);
    }

    public boolean contientArticle(String ref) {
        if (ref == null) return false;
        for (int i = 0; i < nbLignes; i++) {
            if (lignes[i].getNumero().equalsIgnoreCase(ref.trim())) {
                return true;
            }
        }
        return false;
    }

    public int getNombreTotalExemplaires() {
        int total = 0;
        for (int i = 0; i < nbLignes; i++) {
            total += lignes[i].getNbExemplaires();
        }
        return total;
    }

    public void afficher() {
        System.out.println(this.toString());
    }

    public int getNumero() {
        return numero;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public LigneDepot[] getLignes() {
        return lignes;
    }

    public int getNbLignes() {
        return nbLignes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bon de depot no ").append(numero).append("\n");
        sb.append("Client (tel) : ").append(telephone).append("\n");
        sb.append("Date         : ").append(dateEmission).append("\n");
        sb.append("Nb articles  : ").append(nbLignes).append(" reference(s) (").append(getNombreTotalExemplaires()).append(" ex. au total)\n");
        for (int i = 0; i < nbLignes; i++) {
            sb.append(" - ").append(lignes[i].toString()).append("\n");
        }
        return sb.toString();
    }
}
