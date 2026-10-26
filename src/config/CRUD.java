package config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CRUD {

    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    public static String str(UUID id) {
        return id == null ? null : id.toString();
    }

    /** INSERT, UPDATE et DELETE, avec parametres (?) pour eviter l'injection SQL. */
    public boolean executeUpdate(String sql, Object... params) {
        try (PreparedStatement ps = MyConnexion.getConnection().prepareStatement(sql)) {
            bind(ps, params);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /** SELECT : le ResultSet est lu et ferme ici, pas de fuite de ressources. */
    public <T> ArrayList<T> executeSelect(String sql, RowMapper<T> mapper, Object... params) {
        ArrayList<T> list = new ArrayList<>();
        try (PreparedStatement ps = MyConnexion.getConnection().prepareStatement(sql)) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapper.map(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private void bind(PreparedStatement ps, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }
}
