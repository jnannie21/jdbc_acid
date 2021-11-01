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
import java.io.PrintWriter;
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
        studentService = new StudentService();
        teamService = new TeamService();
        createTables();
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
        List<HashMap<String, String>> students = studentService.getStudents();
        req.setAttribute("students", students);
        List<HashMap<String, String>> teams = teamService.getTeams();
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

    private void createTables() throws ServletException {
        try {
            DataSource ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
            createTeamTable(ds);
            createStudentTable(ds);
        } catch (NamingException e) {
            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
        }
    }

    private void createStudentTable(DataSource ds) {
        try(Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
        ) {
            if (tableExists(conn, "student")) {
                return ;
            }
            String sql = "CREATE TABLE student" +
                    " (id SERIAL NOT NULL, " +
                    " first_name VARCHAR(255) NOT NULL, " +
                    " CHECK (first_name <> ''), " +
                    " last_name VARCHAR(255) NOT NULL, " +
                    " CHECK (last_name <> ''), " +
                    " age INTEGER NOT NULL, " +
                    " CHECK (age <> 0), " +
                    " points INTEGER NOT NULL, " +
                    " team_id INTEGER not NULL, " +
                    " PRIMARY KEY (id), " +
                    " CONSTRAINT fk_team " +
                    " FOREIGN KEY(team_id) " +
                    " REFERENCES team(id)) ";
            stmt.executeUpdate(sql);
            System.out.println("Table student created...");
            populateStudentTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateStudentTable() {
        Student student = new Student(
                "user1_first_name",
                "user1_last_name",
                "33",
                "10",
                "1"
        );
        studentService.addStudent(student);
        student = new Student(
                "user2_first_name",
                "user2_last_name",
                "25",
                "15",
                "1"
        );
        studentService.addStudent(student);
    }

    private void createTeamTable(DataSource ds) {
        try (Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
        ) {
            if (tableExists(conn, "team")) {
                return ;
            }
            String sql = "CREATE TABLE team" +
                    " (id SERIAL not NULL, " +
                    " color VARCHAR(255) NOT NULL UNIQUE, " +
                    " CHECK (color <> ''), " +
                    " points INTEGER NOT NULL, " +
                    " PRIMARY KEY (id))";

            stmt.executeUpdate(sql);
            System.out.println("Table team created...");
            populateTeamTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateTeamTable() {
        Team team = new Team(
                "red",
                "10"
        );
        teamService.addTeam(team);
        team = new Team(
                "green",
                "20"
        );
        teamService.addTeam(team);
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet resultSet = meta.getTables(
                null,
                null,
                tableName,
                new String[] {"TABLE"}
        );
        return resultSet.next();
    }

    private void resetErrors() {
        studentService.resetError();
        teamService.resetError();
    }

}
