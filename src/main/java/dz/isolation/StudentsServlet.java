package dz.isolation;

import dz.isolation.model.LiquorType;
import dz.isolation.service.StudentService;

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

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        String liquorType = req.getParameter("Type");
//
//        LiquorType l = LiquorType.valueOf(liquorType);
//
//        List users = usersService.getUsers(l);
//
//        req.setAttribute("users", users);
//        RequestDispatcher view = req.getRequestDispatcher("students.jsp");
//        view.forward(req, resp);
//    }
}