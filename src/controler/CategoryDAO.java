
package controler;

import config.CRUD;
import java.util.ArrayList;
import java.util.UUID;
import model.Category;

public class CategoryDAO implements DAO<Category> {

    private final CRUD crud = new CRUD();

    // Conversion d'une ligne SQL en objet Category
    private static final CRUD.RowMapper<Category> MAPPER = rs -> {
        Category c = new Category();

        c.setId(UUID.fromString(rs.getString("id")));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));

        // Le parent sera chargé séparément à partir de parent_id
        return c;
    };

    @Override
    public boolean insert(Category o) {

        if (o == null || o.getId() == null) {
            return false;
        }

        Category parent = o.getParent();

        // Une catégorie ne peut pas être son propre parent
        if (parent != null && o.getId().equals(parent.getId())) {
            return false;
        }

        // Vérifier que le parent existe dans la base
        if (parent != null && findById(parent.getId()) == null) {
            return false;
        }

        return crud.executeUpdate(
            "INSERT INTO category(id, name, description, parent_id) VALUES(?,?,?,?)",
            CRUD.str(o.getId()),
            o.getName(),
            o.getDescription(),
            parent == null ? null : CRUD.str(parent.getId())
        );
    }

    @Override
    public boolean update(Category o, UUID id) {

        if (o == null || id == null) {
            return false;
        }

        Category parent = o.getParent();

        if (parent != null) {

            // Une catégorie ne peut pas être son propre parent
            if (id.equals(parent.getId())) {
                return false;
            }

            // Le parent doit exister
            if (findById(parent.getId()) == null) {
                return false;
            }

            // Éviter les cycles dans la hiérarchie
            Category current = parent;

            while (current != null) {
                if (id.equals(current.getId())) {
                    return false;
                }
                current = current.getParent();
            }
        }

        return crud.executeUpdate(
            "UPDATE category SET name=?, description=?, parent_id=? WHERE id=?",
            o.getName(),
            o.getDescription(),
            parent == null ? null : CRUD.str(parent.getId()),
            CRUD.str(id)
        );
    }

    @Override
    public boolean delete(UUID id) {

        if (id == null) {
            return false;
        }

        // La suppression peut échouer si la catégorie possède
        // des sous-catégories, selon les contraintes SQL.
        return crud.executeUpdate(
            "DELETE FROM category WHERE id=?",
            CRUD.str(id)
        );
    }

    @Override
    public Category findById(UUID id) {

        if (id == null) {
            return null;
        }

        ArrayList<Category> result = crud.executeSelect(
            "SELECT * FROM category WHERE id=?",
            MAPPER,
            CRUD.str(id)
        );

        if (result.isEmpty()) {
            return null;
        }

        Category category = result.get(0);

        // Charger la catégorie parente
        ArrayList<Category> parents = crud.executeSelect(
            "SELECT * FROM category WHERE id = "
            + "(SELECT parent_id FROM category WHERE id = ?)",
            MAPPER,
            CRUD.str(id)
        );

        if (!parents.isEmpty()) {
            Category parent = parents.get(0);
            category.setParent(parent);
        }

        return category;
    }

    @Override
    public ArrayList<Category> select() {

        ArrayList<Category> categories = crud.executeSelect(
            "SELECT * FROM category ORDER BY name",
            MAPPER
        );

        // Charger les relations parent-enfants
        for (Category category : categories) {

            ArrayList<Category> parents = crud.executeSelect(
                "SELECT * FROM category WHERE id = "
                + "(SELECT parent_id FROM category WHERE id = ?)",
                MAPPER,
                CRUD.str(category.getId())
            );

            if (!parents.isEmpty()) {
                category.setParent(parents.get(0));
            }
        }

        return categories;
    }
}
