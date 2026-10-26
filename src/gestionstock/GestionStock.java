package gestionstock;

import controler.ArticleDAO;
import controler.CategoryDAO;
import controler.MvtDAO;
import controler.SupplierDAO;
import java.time.LocalDateTime;
import model.Article;
import model.Category;
import model.Mvt;
import model.Supplier;

public class GestionStock {

    public static void main(String[] args) {

        CategoryDAO cd = new CategoryDAO();
        ArticleDAO ad = new ArticleDAO();
        SupplierDAO sd = new SupplierDAO();
        MvtDAO md = new MvtDAO();

        // =====================================
        // 1. Création des catégories
        // =====================================

        Category alimentaire =
            new Category("Alimentaire", "Produits alimentaires");

        Category boissons =
            new Category("Boissons", "Eaux et jus");
        boissons.setParent(alimentaire);

        Category eaux =
            new Category("Eaux", "Toutes les catégories d'eau");
        eaux.setParent(boissons);

        Category eauMinerale =
            new Category("Eau minérale", "Eau minérale naturelle");
        eauMinerale.setParent(eaux);

        Category eauGazeuse =
            new Category("Eau gazeuse", "Eau gazeuse");
        eauGazeuse.setParent(eaux);

        Category jus =
            new Category("Jus", "Jus de fruits");
        jus.setParent(boissons);

        Category fruits =
            new Category("Fruits", "Fruits frais");
        fruits.setParent(alimentaire);

        Category pommes =
            new Category("Pommes", "Différentes variétés de pommes");
        pommes.setParent(fruits);

        Category pommesRouges =
            new Category("Pommes rouges", "Pommes rouges fraîches");
        pommesRouges.setParent(pommes);

        // =====================================
        // 2. Enregistrement des catégories
        // =====================================

        Category[] categories = {
            alimentaire,
            boissons,
            eaux,
            eauMinerale,
            eauGazeuse,
            jus,
            fruits,
            pommes,
            pommesRouges
        };

        for (Category c : categories) {
            System.out.println(
                "Insertion de " + c.getName() + " : " + cd.insert(c)
            );
        }

        // =====================================
        // 3. Affichage de toute la hiérarchie
        // =====================================

        System.out.println("\n===== HIÉRARCHIE DES CATÉGORIES =====");

        afficherCategories(alimentaire, 0);

        // =====================================
        // 4. Création d'un article
        // =====================================

        Article a = new Article(
            "Eau 1L",
            "Eau minérale en bouteille de 1 litre",
            0.0,
            0.450,
            0.700,
            eauMinerale.getId()
        );

        System.out.println("\nArticle : " + ad.insert(a));

        // =====================================
        // 5. Création d'un fournisseur
        // =====================================

        Supplier s = new Supplier(
            "Ali Ben Salah",
            "ali@example.tn",
            "20123456",
            "1234567/A"
        );

        System.out.println("Fournisseur : " + sd.insert(s));

        System.out.println("Lien article-fournisseur : "
            + ad.addSupplier(a.getId(), s.getId()));

        // =====================================
        // 6. Mouvements de stock
        // =====================================

        System.out.println("Entrée 100 : "
            + md.insert(new Mvt(
                LocalDateTime.now(),
                "Réception",
                Mvt.Type.INPUT,
                100.0,
                a.getId()
            )));

        System.out.println("Sortie 30 : "
            + md.insert(new Mvt(
                LocalDateTime.now(),
                "Vente",
                Mvt.Type.OUTPUT,
                30.0,
                a.getId()
            )));

        System.out.println("Sortie 500 (doit échouer) : "
            + md.insert(new Mvt(
                LocalDateTime.now(),
                "Vente",
                Mvt.Type.OUTPUT,
                500.0,
                a.getId()
            )));

        // =====================================
        // 7. Affichage des résultats
        // =====================================

        System.out.println("\nArticle trouvé : "
            + ad.findById(a.getId()));

        System.out.println("Fournisseurs de l'article : "
            + ad.suppliersOf(a.getId()));

        System.out.println("Mouvements de l'article : "
            + md.selectByArticle(a.getId()));
    }

    // Affichage récursif des catégories et sous-catégories
    public static void afficherCategories(Category categorie, int niveau) {

        // Indentation selon le niveau hiérarchique
        for (int i = 0; i < niveau; i++) {
            System.out.print("    ");
        }

        System.out.println("- " + categorie.getName());

        // Afficher récursivement toutes les sous-catégories
        for (Category sousCategorie : categorie.getSubCategories()) {
            afficherCategories(sousCategorie, niveau + 1);
        }
    }
}
