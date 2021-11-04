package dz.isolation.dao;

import java.util.List;

public interface Dao<T> {
    List<T> getAll();
    void insert(T t);
    void update(T t);
    void delete(int id);
    String getErrorMsg();
    void resetError();
}