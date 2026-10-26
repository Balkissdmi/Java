
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Category {

    private UUID id = UUID.randomUUID();
    private String name;
    private String description;

    // Relation réflexive : catégorie parente
    private Category parent;

    // Relation réflexive : liste des sous-catégories
    private List<Category> subCategories = new ArrayList<>();

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getParent() {
        return parent;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    // Définir le parent et maintenir les deux côtés de la relation
    public void setParent(Category parent) {

        // Éviter de se définir soi-même comme parent
        if (parent == this) {
            throw new IllegalArgumentException(
                "Une catégorie ne peut pas être son propre parent."
            );
        }

        // Éviter les cycles dans la hiérarchie
        Category current = parent;
        while (current != null) {
            if (current == this) {
                throw new IllegalArgumentException(
                    "Cette relation créerait un cycle."
                );
            }
            current = current.getParent();
        }

        // Retirer la catégorie de l'ancienne liste de sous-catégories
        if (this.parent != null) {
            this.parent.getSubCategories().remove(this);
        }

        this.parent = parent;

        // Ajouter la catégorie à la nouvelle liste de sous-catégories
        if (parent != null && !parent.getSubCategories().contains(this)) {
            parent.getSubCategories().add(this);
        }
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent=" +
                (parent != null ? parent.getName() : "aucun") +
                '}';
    }
}
