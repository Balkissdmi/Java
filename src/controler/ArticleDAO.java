package controler;

import config.CRUD;
import java.util.ArrayList;
import java.util.UUID;
import model.Article;
import model.Supplier;

public class ArticleDAO implements DAO<Article> {

    private final CRUD crud = new CRUD();

    private static final CRUD.RowMapper<Article> MAPPER = rs -> {
        Article a = new Article();
        a.setId(UUID.fromString(rs.getString("id")));
        a.setLabel(rs.getString("label"));
        a.setDescription(rs.getString("description"));
        a.setQty(rs.getDouble("qty"));
        a.setPrixAchat(rs.getDouble("prix_achat"));
        a.setPrixVente(rs.getDouble("prix_vente"));
        a.setCategoryId(UUID.fromString(rs.getString("category_id")));
        return a;
    };

    @Override
    public boolean insert(Article o) {
        return crud.executeUpdate(
            "INSERT INTO article(id, label, description, qty, prix_achat, prix_vente, category_id)"
            + " VALUES(?,?,?,?,?,?,?)",
            CRUD.str(o.getId()), o.getLabel(), o.getDescription(), o.getQty(),
            o.getPrixAchat(), o.getPrixVente(), CRUD.str(o.getCategoryId()));
    }

    /** La quantite n'est volontairement pas modifiee ici : elle ne change que via Mvt. */
    @Override
    public boolean update(Article o, UUID id) {
        return crud.executeUpdate(
            "UPDATE article SET label=?, description=?, prix_achat=?, prix_vente=?, category_id=?"
            + " WHERE id=?",
            o.getLabel(), o.getDescription(), o.getPrixAchat(), o.getPrixVente(),
            CRUD.str(o.getCategoryId()), CRUD.str(id));
    }

    @Override
    public boolean delete(UUID id) {
        return crud.executeUpdate("DELETE FROM article WHERE id=?", CRUD.str(id));
    }

    @Override
    public Article findById(UUID id) {
        ArrayList<Article> r = crud.executeSelect(
            "SELECT * FROM article WHERE id=?", MAPPER, CRUD.str(id));
        return r.isEmpty() ? null : r.get(0);
    }

    @Override
    public ArrayList<Article> select() {
        return crud.executeSelect("SELECT * FROM article ORDER BY label", MAPPER);
    }

    // ---- relation plusieurs-a-plusieurs "fournir" ----

    public boolean addSupplier(UUID articleId, UUID supplierId) {
        return crud.executeUpdate(
            "INSERT INTO article_supplier(article_id, supplier_id) VALUES(?,?)",
            CRUD.str(articleId), CRUD.str(supplierId));
    }

    public boolean removeSupplier(UUID articleId, UUID supplierId) {
        return crud.executeUpdate(
            "DELETE FROM article_supplier WHERE article_id=? AND supplier_id=?",
            CRUD.str(articleId), CRUD.str(supplierId));
    }

    public ArrayList<Supplier> suppliersOf(UUID articleId) {
        return crud.executeSelect(
            "SELECT s.* FROM supplier s JOIN article_supplier x ON x.supplier_id = s.id"
            + " WHERE x.article_id=?",
            SupplierDAO.MAPPER, CRUD.str(articleId));
    }
}
