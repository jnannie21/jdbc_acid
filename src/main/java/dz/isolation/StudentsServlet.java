package dz.isolation;

import dz.isolation.model.Student;
import dz.isolation.model.Team;
import dz.isolation.service.StudentService;
import dz.isolation.service.TeamService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;

@WebServlet(
        name = "studentsservlet",
        urlPatterns = "/"
)
public class StudentsServlet extends HttpServlet {
    private StudentService studentService;
    private TeamService teamService;

    @Override
    public void init() throws ServletException {
        studentService = new StudentService(null);
        teamService = new TeamService(null);
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
            changeStudent(req);
        } else if (req.getServletPath().equals("/delete_student")) {
            deleteStudent(req);
        } else if (req.getServletPath().equals("/add_student")) {
            addStudent(req);
        } else if (req.getServletPath().equals("/change_team")) {
            changeTeam(req);
        } else if (req.getServletPath().equals("/delete_team")) {
            deleteTeam(req);
        } else if (req.getServletPath().equals("/add_team")) {
            addTeam(req);
        }
    }

    private void generateView(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setReqAttributes(req);
        RequestDispatcher view = req.getRequestDispatcher("students.jsp");
        view.forward(req, resp);
    }

    private void setReqAttributes(HttpServletRequest req) {
        List<Student> students = studentService.getStudents();
        req.setAttribute("students", students);
        List<Team> teams = teamService.getTeams();
        req.setAttribute("teams", teams);
        req.setAttribute("studentService", studentService);
        req.setAttribute("teamService", teamService);
    }

    private void addStudent(HttpServletRequest req) {
        Student student = new Student(
                req.getParameter("first_name"),
                req.getParameter("last_name"),
                req.getParameter("age"),
                req.getParameter("points"),
                req.getParameter("team_id")
        );
        studentService.addStudent(student);
    }

    private void changeStudent(HttpServletRequest req) {
        Student student = new Student(
                req.getParameter("id"),
                req.getParameter("first_name"),
                req.getParameter("last_name"),
                req.getParameter("age"),
                req.getParameter("points"),
                req.getParameter("team_id")
        );
        studentService.updateStudent(student);
    }

    private void deleteStudent(HttpServletRequest req) {
        studentService.deleteStudent(req.getParameter("id"));
    }

    private void addTeam(HttpServletRequest req) {
        Team team = new Team(
                req.getParameter("color"),
                req.getParameter("points")
        );
        teamService.addTeam(team);
    }

    private void changeTeam(HttpServletRequest req) {
        Team team = new Team(
                req.getParameter("id"),
                req.getParameter("color"),
                req.getParameter("points")
        );
        teamService.updateTeam(team);
    }

    private void deleteTeam(HttpServletRequest req) {
        teamService.deleteTeam(req.getParameter("id"));
    }

//    private void createTables() throws ServletException {
//        try {
//            DataSource ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
//            createTeamTable(ds);
//            createStudentTable(ds);
//        } catch (NamingException e) {
//            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
//        }
//    }

    private void resetErrors() {
        studentService.resetError();
        teamService.resetError();
    }

}
