package dz.isolation;

import dz.isolation.model.LiquorType;
import dz.isolation.service.UsersService;

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
        name = "usersservlet",
        urlPatterns = "/"
)
public class UsersServlet extends HttpServlet {
    private UsersService usersService;
    private DataSource ds;

    @Override
    public void init() throws ServletException {
        super.init();
        usersService = new UsersService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List users = usersService.getUsers();

        req.setAttribute("users", users);
        RequestDispatcher view = req.getRequestDispatcher("users.jsp");
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
//        RequestDispatcher view = req.getRequestDispatcher("users.jsp");
//        view.forward(req, resp);
//    }
}