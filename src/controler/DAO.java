package controler;

import java.util.ArrayList;
import java.util.UUID;

public interface DAO<T> {
    boolean insert(T o);
    boolean update(T o, UUID id);
    boolean delete(UUID id);
    T findById(UUID id);
    ArrayList<T> select();
}
