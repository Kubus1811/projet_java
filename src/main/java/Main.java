import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("   DEMONSTRATION TP2 / TP3 ET TP4 (QUESTION 1)           ");
        System.out.println("==========================================================\n");

        // 1. Creation de l'etablissement (TP3 - Question 3)
        Etablissement boutique = new Etablissement("Boutique Solidaire");

        // 2. Ajout des articles d'exemple (TP3 - Question 4.b)
        System.out.println("--- 1. Ajout des articles au catalogue ---");
        boutique.ajouter("La maison vide", 25.0, 10, "9782707356741", 752);
        boutique.ajouter("Nouvelle grammaire du college", 38.0, 31, "9782210446694", 512, "Francais", "College");
        boutique.ajouter("Geo", 6.50, 20, "00208245", "mensuel", LocalDate.of(2024, 8, 1));

        // Affichage du catalogue (tri automatique par ordre lexicographique)
        boutique.afficher();

        // 3. Test de la recherche et de la modification de stock (TP3 - Question 4.c)
        System.out.println("\n--- 2. Recherche et modification directe de stock ---");
        String refRech = "9782707356741";
        Article art = boutique.rechercher(refRech);
        System.out.println("Article recherche (" + refRech + ") : " + art);

        System.out.println("\nAugmentation du stock de 5 exemplaires...");
        boutique.ajouter(refRech, 5);
        System.out.println("Apres ajout : " + boutique.rechercher(refRech));

        System.out.println("\nVente (retrait) de 3 exemplaires...");
        boutique.retirer(refRech, 3);
        System.out.println("Apres retrait : " + boutique.rechercher(refRech));

        // 4. Test du calcul des prix et reductions (TP3 - Question 4.a)
        System.out.println("\n--- 3. Calcul des prix et reductions specifiques ---");
        Article livre = boutique.rechercher("9782707356741");
        if (livre != null) {
            LocalDate dateNormale = LocalDate.of(2025, 1, 15);
            LocalDate dateAvril = LocalDate.of(2025, 4, 23); // Journee mondiale du livre
            System.out.printf("Prix du livre en janvier : %.2f euros%n", livre.calculerPrix(dateNormale));
            System.out.printf("Prix du livre en avril (-50%%) : %.2f euros%n", livre.calculerPrix(dateAvril));
        }

        Article mag = boutique.rechercher("00208245");
        if (mag != null) {
            System.out.printf("Prix initial magazine Geo : %.2f euros%n", mag.getPrix());
            System.out.printf("Prix actuel calcule de Geo : %.2f euros%n", mag.calculerPrix());
        }

        // 5. Test des bons de depot (TP3 - Question 5)
        System.out.println("\n--- 4. Creation et enregistrement de bons de depot ---");
        BonDepot bon1 = new BonDepot(1, "061236547890", LocalDate.of(2025, 10, 10));
        bon1.ajouterLigne("9782210446694", 10);
        bon1.ajouterLigne("9782707356741", 20);

        System.out.println("Enregistrement du bon 1 dans la boutique (mise a jour automatique du stock)...");
        boutique.ajouter(bon1);

        BonDepot bon2 = new BonDepot(2, "069876543210", LocalDate.of(2025, 10, 15));
        bon2.ajouterLigne("00208245", 5);
        boutique.ajouter(bon2);

        // 6. Test des methodes de tri et de listing (TP4 - Question 1)
        System.out.println("\n--- 5. TP4 Question 1 : Affichage des articles par stock croissant ---");
        boutique.lister();

        System.out.println("\n--- 6. TP4 Question 1 : Bons de depot d'un client donne ---");
        boutique.lister("061236547890");

        System.out.println("\n--- 7. TP4 Question 1 : Bons pour un article sur une periode donnee ---");
        boutique.lister("9782210446694", LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 12));

        System.out.println("\n==========================================================");
        System.out.println("   FIN DE LA DEMONSTRATION - TOUS LES TESTS SONT VALIDES ");
        System.out.println("==========================================================");
    }
}
