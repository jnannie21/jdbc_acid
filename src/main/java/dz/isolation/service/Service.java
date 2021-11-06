package dz.isolation.service;

import dz.isolation.exception.DaoException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public interface Service<T> {
    List<T> getAll() throws SQLException;
    void insert(HttpServletRequest req) throws NumberFormatException, SQLException, DaoException;
    void update(HttpServletRequest req) throws NumberFormatException, SQLException;
    void delete(HttpServletRequest req) throws NumberFormatException, SQLException;
}