import java.time.LocalDate;

public class Tests {
    private static int nbErreurs = 0;

    public static void main(String[] args) {
        Livre livre = new Livre("La maison vide", 25.0, 10, "9782707356741", 752);
        verifier("livre en janvier : plein tarif", livre.calculerPrix(LocalDate.of(2026, 1, 15)) == 25.0);
        verifier("livre en avril : moitie prix", livre.calculerPrix(LocalDate.of(2026, 4, 23)) == 12.5);

        Manuel manuel = new Manuel("Nouvelle grammaire du college", 38.0, 31, "9782210446694", 512,
                "Francais", "College");
        verifier("manuel en avril : moitie prix", manuel.calculerPrix(LocalDate.of(2026, 4, 1)) == 19.0);

        // pour chaque periodicite : le jour du seuil (pas de reduction) puis le lendemain
        LocalDate sortie = LocalDate.of(2026, 1, 10);
        Magazine hebdo = new Magazine("Le Point", 8.0, 12, "02424002", "hebdomadaire", sortie);
        verifier("hebdomadaire a 2 semaines pile", hebdo.calculerPrix(LocalDate.of(2026, 1, 24)) == 8.0);
        verifier("hebdomadaire a 2 semaines et 1 jour", hebdo.calculerPrix(LocalDate.of(2026, 1, 25)) == 4.0);
        verifier("hebdomadaire a 4 semaines et 1 jour", hebdo.calculerPrix(LocalDate.of(2026, 2, 8)) == 2.0);
        Magazine mensuel = new Magazine("Geo", 8.0, 20, "00208245", "mensuel", sortie);
        verifier("mensuel a 2 mois pile", mensuel.calculerPrix(LocalDate.of(2026, 3, 10)) == 8.0);
        verifier("mensuel a 2 mois et 1 jour", mensuel.calculerPrix(LocalDate.of(2026, 3, 11)) == 4.0);
        verifier("mensuel a 4 mois et 1 jour", mensuel.calculerPrix(LocalDate.of(2026, 5, 11)) == 2.0);
        Magazine revue = new Magazine("Revue", 8.0, 5, "01234567", "trimestriel", sortie);
        verifier("trimestriel a 6 mois pile", revue.calculerPrix(LocalDate.of(2026, 7, 10)) == 8.0);
        verifier("trimestriel a 6 mois et 1 jour", revue.calculerPrix(LocalDate.of(2026, 7, 11)) == 4.0);
        verifier("trimestriel a 12 mois et 1 jour", revue.calculerPrix(LocalDate.of(2027, 1, 11)) == 2.0);

        verifier("retrait de 4 sur 10", livre.retirer(4) && livre.getNbExemplaires() == 6);
        verifier("retrait de 7 sur 6 refuse", !livre.retirer(7) && livre.getNbExemplaires() == 6);
        verifier("ajout de 0 refuse", !livre.ajouter(0));

        Etablissement boutique = new Etablissement("Boutique de test");
        boutique.ajouter("La maison vide", 25.0, 10, "9782707356741", 752);
        boutique.ajouter("Geo", 6.5, 20, "00208245", "mensuel", LocalDate.of(2024, 8, 1));
        boutique.ajouter("Nouvelle grammaire du college", 38.0, 31, "9782210446694", 512,
                "Francais", "College");
        verifier("numero deja utilise refuse", !boutique.ajouter("Copie", 1.0, 1, "00208245", 10));
        verifier("recherche d'un article connu", boutique.rechercher("9782210446694") != null);
        verifier("recherche d'un article inconnu", boutique.rechercher("123") == null);
        verifier("stock d'un article inconnu refuse", !boutique.ajouter("123", 5));

        BonDepot bon = new BonDepot("0612345678");
        bon.ajouterLigne("00208245", 5);
        verifier("ligne de 0 exemplaire refusee", !bon.ajouterLigne("9782707356741", 0));
        boutique.ajouter(bon);
        verifier("stock apres le depot : 20 + 5", boutique.rechercher("00208245").getNbExemplaires() == 25);

        // les articles doivent sortir tries par numero, pas dans l'ordre d'ajout
        boutique.afficher();
        System.out.println(nbErreurs + " erreur(s)");
    }

    private static void verifier(String nom, boolean resultat) {
        if (resultat) {
            System.out.println("OK      " + nom);
        } else {
            System.out.println("ERREUR  " + nom);
            nbErreurs++;
        }
    }
}
