package dz.isolation;

import dz.isolation.model.Student;
import dz.isolation.service.StudentService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@WebServlet(
        name = "studentsservlet",
        urlPatterns = "/"
)
public class StudentsServlet extends HttpServlet {
    private StudentService studentService;
    private DataSource ds;

    @Override
    public void init() throws ServletException {
        super.init();
        studentService = new StudentService();
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
        System.out.println("req.getParameter()");
        System.out.println(req.getParameter("id"));
        System.out.println(req.getParameter("first_name"));
        System.out.println(req.getParameter("last_name"));
        System.out.println("req.getServletPath()");
        System.out.println(req.getServletPath());
        System.out.println();

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
        List students = studentService.getStudents();
        req.setAttribute("students", students);
        RequestDispatcher view = req.getRequestDispatcher("students.jsp");
        view.forward(req, resp);
    }

    private void addStudent(HttpServletRequest req) {
        Student student = new Student(
                req.getParameter("first_name"),
                req.getParameter("last_name")
        );
        studentService.addStudent(student);
    }

    private void changeStudent(HttpServletRequest req) {
        Student student = new Student(
                req.getParameter("id"),
                req.getParameter("first_name"),
                req.getParameter("last_name")
        );
        studentService.updateStudent(student);
    }

    private void deleteStudent(HttpServletRequest req) {
        studentService.deleteStudent(req.getParameter("id"));
    }
}