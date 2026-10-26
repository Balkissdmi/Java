package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Mvt {
    public enum Type { INPUT, OUTPUT }

    private UUID id = UUID.randomUUID();
    private LocalDateTime dateTime;
    private String note;
    private Type type;
    private Double qty;
    private UUID articleId;

    public Mvt() {}

    public Mvt(LocalDateTime dateTime, String note, Type type, Double qty, UUID articleId) {
        this.dateTime = dateTime;
        this.note = note;
        this.type = type;
        this.qty = qty;
        this.articleId = articleId;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public Double getQty() { return qty; }
    public void setQty(Double qty) { this.qty = qty; }
    public UUID getArticleId() { return articleId; }
    public void setArticleId(UUID articleId) { this.articleId = articleId; }

    @Override
    public String toString() {
        return "Mvt{" + dateTime + ", " + type + ", qty=" + qty + ", article=" + articleId + "}";
    }
}
