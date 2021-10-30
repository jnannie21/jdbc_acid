package dz.isolation.service;

import dz.isolation.model.LiquorType;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsersService {
    DataSource ds;

    public UsersService(DataSource dataSource) {
        this.ds = dataSource;
    }

    public void createTable() {
        try(Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
        ) {
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
}