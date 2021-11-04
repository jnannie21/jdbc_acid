package dz.isolation;

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
import java.util.List;

@WebServlet(
        name = "studentsservlet",
        urlPatterns = "/"
)
public class StudentsServlet extends HttpServlet {
    private Service studentService;
    private Service teamService;

    @Override
    public void init() {
        studentService = new StudentService();
        teamService = new TeamService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resetErrors();

        generateView(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resetErrors();

        routeRequest(req);

        generateView(req, resp);
    }

    private void routeRequest(HttpServletRequest req) {
        if (req.getServletPath().equals("/change_student")) {
            studentService.update(req);
        } else if (req.getServletPath().equals("/delete_student")) {
            studentService.delete(req);
        } else if (req.getServletPath().equals("/add_student")) {
            studentService.insert(req);
        } else if (req.getServletPath().equals("/change_team")) {
            teamService.update(req);
        } else if (req.getServletPath().equals("/delete_team")) {
            teamService.delete(req);
        } else if (req.getServletPath().equals("/add_team")) {
            teamService.insert(req);
        }
    }

    private void generateView(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setReqAttributes(req);
        RequestDispatcher view = req.getRequestDispatcher("students.jsp");
        view.forward(req, resp);
    }

    private void setReqAttributes(HttpServletRequest req) {
        List<Student> students = studentService.getAll();
        req.setAttribute("students", students);
        List<Team> teams = teamService.getAll();
        req.setAttribute("teams", teams);
        req.setAttribute("studentService", studentService);
        req.setAttribute("teamService", teamService);
    }

    private void resetErrors() {
        studentService.resetError();
        teamService.resetError();
    }

}
