package dz.isolation.service;

import dz.isolation.dao.Dao;
import dz.isolation.dao.StudentDao;
import dz.isolation.model.Student;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class StudentService implements Service<Student> {
    private Dao<Student> studentDao;
    private String errorMsg;

    private Dao<Student> getDao() {
        if (studentDao == null) {
            studentDao = new StudentDao(null);
        }
        return studentDao;
    }

    public List<Student> getAll() {
        return getDao().getAll();
    }

    public void insert(HttpServletRequest req) {
        try {
            Student student = new Student(
                    req.getParameter("first_name"),
                    req.getParameter("last_name"),
                    Integer.parseInt(req.getParameter("age")),
                    Integer.parseInt(req.getParameter("points")),
                    Integer.parseInt(req.getParameter("team_id"))
            );
            getDao().insert(student);
        } catch (NumberFormatException e) {
            errorMsg = e.toString();
        }
    }

    public void update(HttpServletRequest req) {
        try {
            Student student = new Student(
                    Integer.parseInt(req.getParameter("id")),
                    req.getParameter("first_name"),
                    req.getParameter("last_name"),
                    Integer.parseInt(req.getParameter("age")),
                    Integer.parseInt(req.getParameter("points")),
                    Integer.parseInt(req.getParameter("team_id"))
            );
            getDao().update(student);
        } catch (NumberFormatException e) {
            errorMsg = e.toString();
        }
    }

    public void delete(HttpServletRequest req) {
        try {
            getDao().delete(Integer.parseInt(req.getParameter("id")));
        } catch (NumberFormatException e) {
            errorMsg = e.toString();
        }
    }

    public String getErrorMsg() {
        return errorMsg == null ? getDao().getErrorMsg() : errorMsg;
    }

    public void resetError() {
        errorMsg = null;
        getDao().resetError();
    }
}
