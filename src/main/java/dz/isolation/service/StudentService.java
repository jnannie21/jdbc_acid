package dz.isolation.service;

import dz.isolation.model.Student;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentService {
    DataSource ds;
    public static final String tableName = "student";
    private String errorMsg;

    public StudentService() {
        try {
            ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
        } catch (NamingException e) {
            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
        }
    }

    public List<HashMap<String, String>> getStudents() {
        List<HashMap<String, String>> students = new ArrayList<>();

//        String sql = "select s.id, s.first_name, s.last_name, s.age, s.points, s.team_id, t.color, t.points as team_points from " +
//                tableName +
//                " as s inner join " +
//                TeamService.tableName +
//                " as t on s.team_id = t.id order by s.id";

                String sql = "select id, first_name, last_name, age, points, team_id from " +
                tableName +
                " order by id";

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
                student.put("team_id", rs.getString("team_id"));

//                ResultSetMetaData rsmd = rs.getMetaData();
//                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
//                    System.out.println(rsmd.getColumnName(i));
//                    System.out.println(rsmd.getColumnLabel(i));
//                }

//                student.put("team_color", rs.getString("color"));
//                student.put("team_points", rs.getString("team_points"));

                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public void addStudent(Student student) {
        String sql = "insert into student (first_name, last_name, age, points, team_id) values (" +
                "'" + student.getFirstName() + "', " +
                "'" + student.getLastName() + "', " +
                "'" + student.getAge() + "', " +
                "'" + student.getPoints() + "', " +
                "'" + student.getTeamId() + "'" +
                ");";
//        String sql = "insert into student (first_name, last_name, age, points, team_id) values (?, ?, ?, ?, ?)";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
//            setValuesInStatement(stmt, student);
            stmt.executeUpdate();
            System.out.println("Student record inserted successfully");
        } catch (SQLException e) {
            errorMsg = e.getMessage();
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
            errorMsg = e.getMessage();
            e.printStackTrace();
        }
    }

    public void updateStudent(Student student) {
        String sql = "update student set " +
                "first_name=" +
                "'" + student.getFirstName() + "', " +
                "last_name=" +
                "'" + student.getLastName() + "', " +
                "age=" +
                "'" + student.getAge() + "', " +
                "points=" +
                "'" + student.getPoints() + "', " +
                "team_id=" +
                "'" + student.getTeamId() + "'" +
                " where id=" +
                "'" + student.getId() + "'";

//        String sql = "update student set first_name=?, last_name=?, age=?, points=?, team_id=? where id=?";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
//            setValuesInStatement(stmt, student);
//            stmt.setInt(6, Integer.decode(student.getId()));

            stmt.executeUpdate();
            System.out.println("Record " + student.getId() + " updated successfully");
        } catch (SQLException e) {
            errorMsg = e.getMessage();
            e.printStackTrace();
        }
    }

//    private void setValuesInStatement(PreparedStatement stmt, Student student) throws SQLException {
//        stmt.setString(1, student.getFirstName());
//        stmt.setString(2, student.getLastName());
//        stmt.setInt(3, Integer.decode(student.getAge()));
//        stmt.setInt(4, Integer.decode(student.getPoints()));
//        stmt.setInt(5, Integer.decode(student.getTeamId()));
//    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void resetError() {
        errorMsg = null;
    }
}