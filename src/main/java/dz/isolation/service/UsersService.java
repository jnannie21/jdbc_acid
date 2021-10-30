package dz.isolation.service;

import dz.isolation.model.LiquorType;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsersService {
    DataSource ds;

    public UsersService() {
        try {
            ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
            createTable();
        } catch (NamingException e) {
            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
        }
    }

    public void createTable() {
        try(Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
        ) {
            if (tableExists(conn)) {
                return ;
            }
            String sql = "CREATE TABLE USERS " +
                    "(id INTEGER not NULL, " +
                    " first_name VARCHAR(255), " +
                    " last_name VARCHAR(255), " +
                    " age INTEGER, " +
                    " points INTEGER, " +
                    " PRIMARY KEY ( id ))";

            stmt.executeUpdate(sql);
            System.out.println("Table created...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<HashMap<String, String>> getUsers() {
        List<HashMap<String, String>> users = new ArrayList<>();

        String sql = "select * from users";
        try(Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                HashMap<String, String> user = new HashMap<>();
                user.put("id", rs.getString("id"));
                user.put("first_name", rs.getString("first_name"));
                user.put("last_name", rs.getString("last_name"));
                user.put("age", rs.getString("age"));
                user.put("points", rs.getString("points"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private boolean tableExists(Connection conn) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet resultSet = meta.getTables(
                null,
                null,
                "users",
                new String[] {"TABLE"}
        );
        return resultSet.next();
    }
}