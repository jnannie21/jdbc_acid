package dz.isolation.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface Service<T> {
    List<T> getAll();
    void insert(HttpServletRequest req);
    void update(HttpServletRequest req);
    void delete(HttpServletRequest req);
    String getErrorMsg();
    void resetError();
}