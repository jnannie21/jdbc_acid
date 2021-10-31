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
//    private DataSource ds;

    @Override
    public void init() throws ServletException {
        super.init();
        createTables();
        studentService = new StudentService();
        teamService = new TeamService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List students = studentService.getStudents();

        req.setAttribute("students", students);
        RequestDispatcher view = req.getRequestDispatcher("students.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
//        System.out.println("req.getParameter()");
//        System.out.println(req.getParameter("id"));
//        System.out.println(req.getParameter("first_name"));
//        System.out.println(req.getParameter("last_name"));
//        System.out.println("req.getServletPath()");
//        System.out.println(req.getServletPath());
//        System.out.println();

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
        }
    }

    private void generateView(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<HashMap<String, String>> students = studentService.getStudents();
        req.setAttribute("students", students);
        List<HashMap<String, String>> teams = teamService.getTeams();
        req.setAttribute("teams", teams);
        RequestDispatcher view = req.getRequestDispatcher("students.jsp");
        view.forward(req, resp);
    }

    private void addStudent(HttpServletRequest req) {
        Student student = new Student(
                req.getParameter("first_name"),
                req.getParameter("last_name"),
                req.getParameter("age"),
                req.getParameter("points")
        );
        studentService.addStudent(student, req.getParameter("team_id"));
    }

    private void changeStudent(HttpServletRequest req) {
        Student student = new Student(
                req.getParameter("id"),
                req.getParameter("first_name"),
                req.getParameter("last_name"),
                req.getParameter("age"),
                req.getParameter("points")
        );
        studentService.updateStudent(student, req.getParameter("team_id"));
    }

    private void deleteStudent(HttpServletRequest req) {
        studentService.deleteStudent(req.getParameter("id"));
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
            if (tableStudentExists(conn)) {
                return ;
            }
            String sql = "CREATE TABLE student" +
                    " (id SERIAL not NULL, " +
                    " first_name VARCHAR(255), " +
                    " last_name VARCHAR(255), " +
                    " age INTEGER, " +
                    " points INTEGER, " +
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

    private boolean tableStudentExists(Connection conn) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet resultSet = meta.getTables(
                null,
                null,
                "student",
                new String[] {"TABLE"}
        );
        return resultSet.next();
    }

    private void populateStudentTable() {
        Student student = new Student(
                "user1_first_name",
                "user1_last_name",
                "33",
                "10"
        );
        studentService.addStudent(student);
        student = new Student(
                "user2_first_name",
                "user2_last_name",
                "25",
                "15"
        );
        studentService.addStudent(student);
    }

    private void createTeamTable(DataSource ds) {
        try(Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
        ) {
            if (tableTeamExists(conn)) {
                return ;
            }
            String sql = "CREATE TABLE team" +
                    " (id SERIAL not NULL, " +
                    " color VARCHAR(255), " +
                    " points INTEGER, " +
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

    private boolean tableTeamExists(Connection conn) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet resultSet = meta.getTables(
                null,
                null,
                "team",
                new String[] {"TABLE"}
        );
        return resultSet.next();
    }
}
