package dz.isolation.service;

import dz.isolation.dao.Dao;
import dz.isolation.dao.StudentDao;
import dz.isolation.model.Student;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

/**
 * Service for fetching students from db via Dao.
 */
public class StudentService implements Service<Student> {
    /**
     * Dao for accessing student table.
     */
    private Dao<Student> studentDao;

    /**
     * Default constructor.
     */
    public StudentService() {
        studentDao = new StudentDao();
    }

    /**
     * Get all students.
     * @return List of Student.
     */
    @Override
    public List<Student> getAll() throws SQLException {
        return studentDao.getAll();
    }

    /**
     * Create from HttpServletRequest and insert to db new student.
     * @param req
     */
    @Override
    public void insert(HttpServletRequest req) throws NumberFormatException, SQLException {
        Student student = new Student(
                req.getParameter("first_name"),
                req.getParameter("last_name"),
                Integer.parseInt(req.getParameter("age")),
                Integer.parseInt(req.getParameter("points")),
                Integer.parseInt(req.getParameter("team_id"))
        );
        studentDao.insert(student);
    }

    /**
     * Update existing student.
     * @param req
     */
    @Override
    public void update(HttpServletRequest req) throws NumberFormatException, SQLException {
        Student student = new Student(
                Integer.parseInt(req.getParameter("id")),
                req.getParameter("first_name"),
                req.getParameter("last_name"),
                Integer.parseInt(req.getParameter("age")),
                Integer.parseInt(req.getParameter("points")),
                Integer.parseInt(req.getParameter("team_id"))
        );
        studentDao.update(student);
    }

    /**
     * Delete student.
     * @param req
     */
    @Override
    public void delete(HttpServletRequest req) throws NumberFormatException, SQLException {
        studentDao.delete(Integer.parseInt(req.getParameter("id")));
    }
}
