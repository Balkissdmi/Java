package controler;

import config.CRUD;
import config.MyConnexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Mvt;

public class MvtDAO implements DAO<Mvt> {

    private final CRUD crud = new CRUD();

    private static final CRUD.RowMapper<Mvt> MAPPER = rs -> {
        Mvt m = new Mvt();
        m.setId(UUID.fromString(rs.getString("id")));
        m.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        m.setNote(rs.getString("note"));
        m.setType(Mvt.Type.valueOf(rs.getString("type")));
        m.setQty(rs.getDouble("qty"));
        m.setArticleId(UUID.fromString(rs.getString("article_id")));
        return m;
    };

    /**
     * Enregistre le mouvement ET ajuste le stock de l'article dans une seule transaction.
     * Une sortie superieure au stock disponible est refusee (rollback).
     */
    @Override
    public boolean insert(Mvt m) {
        if (m.getQty() == null || m.getQty() <= 0) return false;
        Connection c = MyConnexion.getConnection();
        boolean input = m.getType() == Mvt.Type.INPUT;
        String adjust = input
            ? "UPDATE article SET qty = qty + ? WHERE id = ?"
            : "UPDATE article SET qty = qty - ? WHERE id = ? AND qty >= ?";
        try {
            c.setAutoCommit(false);
            try (PreparedStatement p1 = c.prepareStatement(
                     "INSERT INTO mvt(id, date_time, note, type, qty, article_id) VALUES(?,?,?,?,?,?)");
                 PreparedStatement p2 = c.prepareStatement(adjust)) {
                p1.setString(1, CRUD.str(m.getId()));
                p1.setTimestamp(2, Timestamp.valueOf(m.getDateTime()));
                p1.setString(3, m.getNote());
                p1.setString(4, m.getType().name());
                p1.setDouble(5, m.getQty());
                p1.setString(6, CRUD.str(m.getArticleId()));
                p1.executeUpdate();

                p2.setDouble(1, m.getQty());
                p2.setString(2, CRUD.str(m.getArticleId()));
                if (!input) p2.setDouble(3, m.getQty());
                if (p2.executeUpdate() != 1) { // article inconnu ou stock insuffisant
                    c.rollback();
                    return false;
                }
                c.commit();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MvtDAO.class.getName()).log(Level.SEVERE, null, ex);
            try { c.rollback(); } catch (SQLException ignored) { }
            return false;
        } finally {
            try { c.setAutoCommit(true); } catch (SQLException ignored) { }
        }
    }

    /** Un mouvement est un historique : on ne le modifie pas. */
    @Override
    public boolean update(Mvt o, UUID id) {
        System.err.println("Un mouvement ne se modifie pas : creer un mouvement inverse.");
        return false;
    }

    /** Un mouvement est un historique : on ne le supprime pas. */
    @Override
    public boolean delete(UUID id) {
        System.err.println("Un mouvement ne se supprime pas : creer un mouvement inverse.");
        return false;
    }

    @Override
    public Mvt findById(UUID id) {
        ArrayList<Mvt> r = crud.executeSelect(
            "SELECT * FROM mvt WHERE id=?", MAPPER, CRUD.str(id));
        return r.isEmpty() ? null : r.get(0);
    }

    @Override
    public ArrayList<Mvt> select() {
        return crud.executeSelect("SELECT * FROM mvt ORDER BY date_time DESC", MAPPER);
    }

    public ArrayList<Mvt> selectByArticle(UUID articleId) {
        return crud.executeSelect(
            "SELECT * FROM mvt WHERE article_id=? ORDER BY date_time DESC",
            MAPPER, CRUD.str(articleId));
    }
}
