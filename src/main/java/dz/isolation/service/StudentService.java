package dz.isolation.service;

import dz.isolation.dao.Dao;
import dz.isolation.dao.StudentDao;
import dz.isolation.model.Student;

import javax.servlet.http.HttpServletRequest;
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
     * Internal service's error.
     */
    private String errorMsg;

    /**
     * Returns Dao in a lazy way.
     * @return Students Dao object.
     */
    private Dao<Student> getDao() {
        if (studentDao == null) {
            studentDao = new StudentDao(null);
        }
        return studentDao;
    }

    /**
     * Get all students.
     * @return List of Student.
     */
    @Override
    public List<Student> getAll() {
        return getDao().getAll();
    }

    /**
     * Create from HttpServletRequest and insert to db new student.
     * @param req
     */
    @Override
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

    /**
     * Update existing student.
     * @param req
     */
    @Override
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

    /**
     * Delete student.
     * @param req
     */
    @Override
    public void delete(HttpServletRequest req) {
        try {
            getDao().delete(Integer.parseInt(req.getParameter("id")));
        } catch (NumberFormatException e) {
            errorMsg = e.toString();
        }
    }

    /**
     * Get this service's internal error, if there is one, or Dao internal error.
     * @return error String or null.
     */
    @Override
    public String getErrorMsg() {
        return errorMsg == null ? getDao().getErrorMsg() : errorMsg;
    }

    /**
     * Reset this and Dao object's internal errors.
     */
    @Override
    public void resetError() {
        errorMsg = null;
        getDao().resetError();
    }
}
