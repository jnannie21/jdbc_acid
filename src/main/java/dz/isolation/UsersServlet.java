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
        urlPatterns = "/users"
)
public class UsersServlet extends HttpServlet {
    private UsersService usersService;
    private DataSource ds;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
            usersService = new UsersService(ds);
            usersService.createTable();
        } catch (NamingException e) {
            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String liquorType = req.getParameter("Type");

        LiquorType l = LiquorType.valueOf(liquorType);

        List liquorBrands = usersService.getAvailableBrands(l);

        req.setAttribute("brands", liquorBrands);
        RequestDispatcher view = req.getRequestDispatcher("result.jsp");
        view.forward(req, resp);
    }
}