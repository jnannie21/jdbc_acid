package dz.isolation.dao;

import dz.isolation.exception.DaoException;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    List<T> getAll() throws SQLException;
    void insert(T t) throws SQLException, DaoException;
    void update(T t) throws SQLException;
    void delete(int id) throws SQLException;
}