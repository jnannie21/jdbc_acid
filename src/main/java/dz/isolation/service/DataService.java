package dz.isolation.service;

import dz.isolation.model.Team;

import java.util.HashMap;
import java.util.List;

public interface DataService {
    public List<HashMap<String, String>> getAll();
    public void addOne(Object entity);
    public void deleteById(String id);
    public void update(Object entity);
    public String getErrorMsg();
    public void resetError();
}
