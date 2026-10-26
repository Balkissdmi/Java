package model;

import java.util.UUID;

public class Article {
    private UUID id = UUID.randomUUID();
    private String label;
    private String description;
    private Double qty;
    private Double prixAchat;
    private Double prixVente;
    private UUID categoryId;

    public Article() {}

    public Article(String label, String description, Double qty,
                   Double prixAchat, Double prixVente, UUID categoryId) {
        this.label = label;
        this.description = description;
        this.qty = qty;
        this.prixAchat = prixAchat;
        this.prixVente = prixVente;
        this.categoryId = categoryId;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getQty() { return qty; }
    public void setQty(Double qty) { this.qty = qty; }
    public Double getPrixAchat() { return prixAchat; }
    public void setPrixAchat(Double prixAchat) { this.prixAchat = prixAchat; }
    public Double getPrixVente() { return prixVente; }
    public void setPrixVente(Double prixVente) { this.prixVente = prixVente; }
    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }

    @Override
    public String toString() {
        return "Article{" + label + ", qty=" + qty + ", achat=" + prixAchat
             + ", vente=" + prixVente + ", cat=" + categoryId + "}";
    }
}
