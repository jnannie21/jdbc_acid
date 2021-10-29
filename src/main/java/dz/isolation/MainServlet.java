package dz.isolation;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MainServlet extends HttpServlet {
//    static final String DB_URL = "jdbc:postgres://postgres/postgres";
//    static final String USER = "postgres_user";
//    static final String PASS = "postgres_pass";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.print("<h1>Hello, this is my first servlet changed!!!</h1>");

        try {
            InitialContext cxt = new InitialContext();
            if ( cxt == null ) {
                throw new Exception("Uh oh -- no context!");
            }
            DataSource ds = (DataSource) cxt.lookup( "java:/comp/env/jdbc/postgres" );
            if ( ds == null ) {
                throw new Exception("Data source not found!");
            }

            createTable(ds, out);

            out.print("connection established");
        } catch (Exception e) {
            out.print(e);
        }
    }

    private void createTable(DataSource ds, PrintWriter out) {
        try(Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
        ) {
            String sql = "CREATE TABLE REGISTRATION " +
                    "(id INTEGER not NULL, " +
                    " first VARCHAR(255), " +
                    " last VARCHAR(255), " +
                    " age INTEGER, " +
                    " PRIMARY KEY ( id ))";

            stmt.executeUpdate(sql);
            out.print("Created table in given database...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
