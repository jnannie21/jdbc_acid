package dz.isolation.service;

import dz.isolation.model.Student;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentService {
    DataSource ds;
    private static final String tableName = "student";

    public StudentService() {
        try {
            ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
            new TeamService();
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
            String sql = "CREATE TABLE " +
                    tableName +
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
            System.out.println("Table " + tableName + " created...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<HashMap<String, String>> getStudents() {
        List<HashMap<String, String>> students = new ArrayList<>();

        String sql = "select * from " + tableName + " order by id";
        try(Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
        ) {
//            stmt.setString(1, tableName);
//            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                HashMap<String, String> student = new HashMap<>();
                student.put("id", rs.getString("id"));
                student.put("first_name", rs.getString("first_name"));
                student.put("last_name", rs.getString("last_name"));
                student.put("age", rs.getString("age"));
                student.put("points", rs.getString("points"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    private boolean tableExists(Connection conn) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet resultSet = meta.getTables(
                null,
                null,
                tableName,
                new String[] {"TABLE"}
        );
        return resultSet.next();
    }

    public void addStudent(Student student) {
//        INSERT INTO Product (type, model, maker)
//        VALUES ('PC', 1157, 'B');

        String sql = "insert into student (first_name, last_name, team_id) values (" +
                "'" + student.getFirstName() + "', " +
                "'" + student.getLastName() + "', " +
                1 +
                ");";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.executeUpdate();
            System.out.println("Record " + student.getId() + " inserted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(String id) {
        String sql = "delete from student where id=" + id;
        try(Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.executeUpdate();
            System.out.println("Record " + id + " deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(Student student) {
        String sql = "update student set first_name='" +
                student.getFirstName() +
                "', last_name='" +
                student.getLastName() +
                "' where id=" +
                student.getId() +
                ";";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.executeUpdate();
            System.out.println("Record " + student.getId() + " updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}