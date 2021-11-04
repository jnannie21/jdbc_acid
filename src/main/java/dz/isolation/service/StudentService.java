package dz.isolation.service;

import dz.isolation.dao.StudentDao;
import dz.isolation.dao.TeamDao;
import dz.isolation.model.Student;
import dz.isolation.model.Team;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class StudentService {
    private StudentDao studentDao;

    public StudentService() {
        System.out.println("here we are");
        studentDao = new StudentDao(null);
    }

    public List<Student> getAll() {
        return studentDao.getAll();
    }

    public void addStudent(HttpServletRequest req) {
        Student student = new Student(
                req.getParameter("first_name"),
                req.getParameter("last_name"),
                req.getParameter("age"),
                req.getParameter("points"),
                req.getParameter("team_id")
        );
        studentDao.insert(student);
    }

    public void changeStudent(HttpServletRequest req) {
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

    public void deleteStudent(HttpServletRequest req) {
        studentDao.delete(req.getParameter("id"));
    }

    public String getErrorMsg() {
        return studentDao.getErrorMsg();
    }

    public void resetError() {
        studentDao.resetError();
    }
}
