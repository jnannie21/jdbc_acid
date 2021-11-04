package dz.isolation.dao;

import dz.isolation.model.Student;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao implements Dao<Student> {
    public static final String tableName = "student";

    private DataSource ds;
    private String errorMsg;

    public StudentDao(DataSource ds) {
        try {
            if ((this.ds = ds) == null) {
                this.ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
            }
        } catch (NamingException e) {
            errorMsg = e.toString();
            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
        }
    }

    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();

                String sql = "select id, first_name, last_name, age, points, team_id from " +
                tableName +
                " order by id";

        try (Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getInt("points"),
                        rs.getInt("team_id")
                );

                students.add(student);
            }
        } catch (SQLException e) {
            errorMsg = e.toString();
            e.printStackTrace();
        }
        return students;
    }

    public void insert(Student student) {
        String sql = "insert into student (first_name, last_name, age, points, team_id) values (?, ?, ?, ?, ?)";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            setValuesInStatement(stmt, student);

            stmt.executeUpdate();
            System.out.println("Student record inserted successfully");
        } catch (NumberFormatException | SQLException e) {
            errorMsg = e.toString();
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "delete from student where id=?";
        try(Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Record " + id + " deleted successfully");
        } catch (NumberFormatException | SQLException e) {
            errorMsg = e.toString();
            e.printStackTrace();
        }
    }

    public void update(Student student) {
        String sql = "update student set first_name=?, last_name=?, age=?, points=?, team_id=? where id=?";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            setValuesInStatement(stmt, student);
            stmt.setInt(6, student.getId());

            stmt.executeUpdate();
            System.out.println("Record " + student.getId() + " updated successfully");
        } catch (NumberFormatException | SQLException e) {
            errorMsg = e.toString();
            e.printStackTrace();
        }
    }

    private void setValuesInStatement(PreparedStatement stmt, Student student)
            throws SQLException, NumberFormatException {
        stmt.setString(1, student.getFirstName());
        stmt.setString(2, student.getLastName());
        stmt.setInt(3, student.getAge());
        stmt.setInt(4, student.getPoints());
        stmt.setInt(5, student.getTeamId());
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void resetError() {
        errorMsg = null;
    }
}