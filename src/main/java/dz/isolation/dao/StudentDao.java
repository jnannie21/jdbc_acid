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
            new TeamDao(ds).createTable(this.ds);
            createTable(this.ds);
        } catch (NamingException e) {
            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
        }
    }

    public void createTable(DataSource ds) {
        try(Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
        ) {
            if (tableExists(conn, "student")) {
                return ;
            }
            String sql = "CREATE TABLE student" +
                    " (id SERIAL NOT NULL, " +
                    " first_name VARCHAR(255) NOT NULL, " +
                    " CHECK (first_name <> ''), " +
                    " last_name VARCHAR(255) NOT NULL, " +
                    " CHECK (last_name <> ''), " +
                    " age INTEGER NOT NULL, " +
                    " CHECK (age > 0), " +
                    " points INTEGER NOT NULL, " +
                    " team_id INTEGER NOT NULL, " +
                    " PRIMARY KEY (id), " +
                    " CONSTRAINT fk_team " +
                    " FOREIGN KEY(team_id) " +
                    " REFERENCES team(id)) ";
            stmt.executeUpdate(sql);
            System.out.println("Table student created...");
            populateTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet resultSet = meta.getTables(
                null,
                null,
                tableName,
                new String[] {"TABLE"}
        );
        return resultSet.next();
    }

    private void populateTable() {
        Student student = new Student(
                "dima",
                "dimin",
                "33",
                "10",
                "1"
        );
        insert(student);
        student = new Student(
                "vasia",
                "vasin",
                "44",
                "22",
                "2"
        );
        insert(student);
    }

    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();

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
                Student student = new Student(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("age"),
                        rs.getString("points"),
                        rs.getString("team_id")
                );

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
            setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    public void insert(Student student) {
//        String sql = "insert into student (first_name, last_name, age, points, team_id) values (" +
//                "'" + student.getFirstName() + "', " +
//                "'" + student.getLastName() + "', " +
//                "'" + student.getAge() + "', " +
//                "'" + student.getPoints() + "', " +
//                "'" + student.getTeamId() + "'" +
//                ");";
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

    public void delete(String id) {
        String sql = "delete from student where id=?";
        try(Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
            System.out.println("Record " + id + " deleted successfully");
        } catch (NumberFormatException | SQLException e) {
            errorMsg = e.toString();
            e.printStackTrace();
        }
    }

    public void update(Student student) {
//        String sql = "update student set " +
//                "first_name=" +
//                "'" + student.getFirstName() + "', " +
//                "last_name=" +
//                "'" + student.getLastName() + "', " +
//                "age=" +
//                "'" + student.getAge() + "', " +
//                "points=" +
//                "'" + student.getPoints() + "', " +
//                "team_id=" +
//                "'" + student.getTeamId() + "'" +
//                " where id=" +
//                "'" + student.getId() + "'";

        String sql = "update student set first_name=?, last_name=?, age=?, points=?, team_id=? where id=?";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            setValuesInStatement(stmt, student);
            stmt.setInt(6, Integer.parseInt(student.getId()));

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
        stmt.setInt(3, Integer.parseInt(student.getAge()));
        stmt.setInt(4, Integer.parseInt(student.getPoints()));
        stmt.setInt(5, Integer.parseInt(student.getTeamId()));
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void resetError() {
        errorMsg = null;
    }
}