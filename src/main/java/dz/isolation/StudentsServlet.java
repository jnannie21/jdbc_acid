package dz.isolation;

import dz.isolation.exception.DaoException;
import dz.isolation.model.Student;
import dz.isolation.model.Team;
import dz.isolation.service.Service;
import dz.isolation.service.StudentService;
import dz.isolation.service.TeamService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for simple web application.
 * @author Dmitry Polushkin
 */
@WebServlet(
        name = "studentsservlet",
        urlPatterns = "/"
)
public class StudentsServlet extends HttpServlet {
    /**
     * Service for getting students data from db.
     */
    private Service studentService;

    /**
     * Service for getting teams data from db.
     */
    private Service teamService;

    /**
     * Creates services.
     */
    @Override
    public void init() {
        studentService = new StudentService();
        teamService = new TeamService();
    }

    /**
     * Returns all tables in one view.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        generateView(req, resp);
    }

    /**
     * Routes and returns view depending on url.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        routeRequest(req);

        generateView(req, resp);
    }

    /**
     * Routing to service action.
     * @param req
     */
    private void routeRequest(HttpServletRequest req) {
        try {
            if (req.getServletPath().equals("/update_student")) {
                studentService.update(req);
            } else if (req.getServletPath().equals("/delete_student")) {
                studentService.delete(req);
            } else if (req.getServletPath().equals("/insert_student")) {
                studentService.insert(req);
            } else if (req.getServletPath().equals("/update_team")) {
                teamService.update(req);
            } else if (req.getServletPath().equals("/delete_team")) {
                teamService.delete(req);
            } else if (req.getServletPath().equals("/insert_team")) {
                teamService.insert(req);
            }
        } catch (NumberFormatException | SQLException | DaoException e) {
            setErrorMsg(req, e);
        }
    }

    private void setErrorMsg(HttpServletRequest req, Exception e) {
        req.setAttribute("error", e.toString());
    }

    /**
     * Forwards a request from a servlet to view.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void generateView(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            setReqAttributes(req);
        } catch (SQLException e) {
            setErrorMsg(req, e);
        }

        RequestDispatcher view = req.getRequestDispatcher("students.jsp");
        view.forward(req, resp);
    }

    /**
     * Setting attributes in HttpServletRequest.
     * @param req
     */
    private void setReqAttributes(HttpServletRequest req) throws SQLException {
        List<Student> students = studentService.getAll();
        req.setAttribute("students", students);
        List<Team> teams = teamService.getAll();
        req.setAttribute("teams", teams);
    }
}
