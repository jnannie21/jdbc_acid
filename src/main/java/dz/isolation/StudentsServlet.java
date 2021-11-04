package dz.isolation;

import dz.isolation.model.Student;
import dz.isolation.model.Team;
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
    private StudentService studentService;
    private TeamService teamService;

    @Override
    public void init() throws ServletException {
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
            studentService.changeStudent(req);
        } else if (req.getServletPath().equals("/delete_student")) {
            studentService.deleteStudent(req);
        } else if (req.getServletPath().equals("/add_student")) {
            studentService.addStudent(req);
        } else if (req.getServletPath().equals("/change_team")) {
            teamService.changeTeam(req);
        } else if (req.getServletPath().equals("/delete_team")) {
            teamService.deleteTeam(req);
        } else if (req.getServletPath().equals("/add_team")) {
            teamService.addTeam(req);
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
