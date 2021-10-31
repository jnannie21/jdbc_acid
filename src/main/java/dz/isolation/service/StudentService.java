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
    public static final String tableName = "student";

    public StudentService() {
        try {
            ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
        } catch (NamingException e) {
            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
        }
    }

    public List<HashMap<String, String>> getStudents() {
        List<HashMap<String, String>> students = new ArrayList<>();

        String sql = "select s.id, s.first_name, s.last_name, s.age, s.points, s.team_id, t.color, t.points as team_points from " +
                tableName +
                " as s inner join " +
                TeamService.tableName +
                " as t on s.team_id = t.id order by s.id";

        try (Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                HashMap<String, String> student = new HashMap<>();
                student.put("id", rs.getString("id"));
                student.put("first_name", rs.getString("first_name"));
                student.put("last_name", rs.getString("last_name"));
                student.put("age", rs.getString("age"));
                student.put("points", rs.getString("points"));

//                ResultSetMetaData rsmd = rs.getMetaData();
//                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
//                    System.out.println(rsmd.getColumnName(i));
//                    System.out.println(rsmd.getColumnLabel(i));
//                }

                student.put("team_color", rs.getString("color"));
                student.put("team_points", rs.getString("team_points"));

                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public void addStudent(Student student) {
        addStudent(student, "1");
    }

    public void addStudent(Student student, String teamId) {
        String sql = "insert into student (first_name, last_name, team_id) values (" +
                "'" + student.getFirstName() + "', " +
                "'" + student.getLastName() + "', " +
                teamId +
                ");";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.executeUpdate();
            System.out.println("Team record inserted successfully");
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

//    public void updateStudent(Student student) {
//        updateStudent(student);
//    }

    public void updateStudent(Student student, String teamId) {
        String sql = "update student set first_name=?, last_name=? where id=?";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setInt(3, Integer.parseInt(student.getId()));
            stmt.executeUpdate();
            System.out.println("Record " + student.getId() + " updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}