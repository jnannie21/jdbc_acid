package dz.isolation.service;

import dz.isolation.dao.Dao;
import dz.isolation.dao.StudentDao;
import dz.isolation.model.Student;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class StudentService implements Service<Student> {
    private Dao studentDao;

    public StudentService() {
        studentDao = new StudentDao(null);
    }

    public List<Student> getAll() {
        return studentDao.getAll();
    }

    public void insert(HttpServletRequest req) {
        Student student = new Student(
                req.getParameter("first_name"),
                req.getParameter("last_name"),
                req.getParameter("age"),
                req.getParameter("points"),
                req.getParameter("team_id")
        );
        studentDao.insert(student);
    }

    public void update(HttpServletRequest req) {
        Student student = new Student(
                req.getParameter("id"),
                req.getParameter("first_name"),
                req.getParameter("last_name"),
                req.getParameter("age"),
                req.getParameter("points"),
                req.getParameter("team_id")
        );
        studentDao.update(student);
    }

    public void delete(HttpServletRequest req) {
        studentDao.delete(req.getParameter("id"));
    }

    public String getErrorMsg() {
        return studentDao.getErrorMsg();
    }

    public void resetError() {
        studentDao.resetError();
    }
}
