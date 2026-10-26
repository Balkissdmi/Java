package controler;

import config.CRUD;
import java.util.ArrayList;
import java.util.UUID;
import model.Supplier;

public class SupplierDAO implements DAO<Supplier> {

    private final CRUD crud = new CRUD();

    static final CRUD.RowMapper<Supplier> MAPPER = rs -> {
        Supplier s = new Supplier();
        s.setId(UUID.fromString(rs.getString("id")));
        s.setFullname(rs.getString("fullname"));
        s.setEmail(rs.getString("email"));
        s.setTel(rs.getString("tel"));
        s.setVat(rs.getString("vat"));
        return s;
    };

    @Override
    public boolean insert(Supplier o) {
        return crud.executeUpdate(
            "INSERT INTO supplier(id, fullname, email, tel, vat) VALUES(?,?,?,?,?)",
            CRUD.str(o.getId()), o.getFullname(), o.getEmail(), o.getTel(), o.getVat());
    }

    @Override
    public boolean update(Supplier o, UUID id) {
        return crud.executeUpdate(
            "UPDATE supplier SET fullname=?, email=?, tel=?, vat=? WHERE id=?",
            o.getFullname(), o.getEmail(), o.getTel(), o.getVat(), CRUD.str(id));
    }

    @Override
    public boolean delete(UUID id) {
        return crud.executeUpdate("DELETE FROM supplier WHERE id=?", CRUD.str(id));
    }

    @Override
    public Supplier findById(UUID id) {
        ArrayList<Supplier> r = crud.executeSelect(
            "SELECT * FROM supplier WHERE id=?", MAPPER, CRUD.str(id));
        return r.isEmpty() ? null : r.get(0);
    }

    @Override
    public ArrayList<Supplier> select() {
        return crud.executeSelect("SELECT * FROM supplier ORDER BY fullname", MAPPER);
    }
}
