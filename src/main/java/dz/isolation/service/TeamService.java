package dz.isolation.service;

import dz.isolation.model.Student;
import dz.isolation.model.Team;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeamService {
    public static final String tableName = "team";

    private DataSource ds;
    private String errorMsg;

    public TeamService(DataSource ds) {
        try {
            if ((this.ds = ds) == null) {
                this.ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
            }
            createTable(this.ds);
        } catch (NamingException e) {
            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
        }
    }

    public void createTable(DataSource ds) {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
        ) {
            if (tableExists(conn, "team")) {
                return ;
            }
            String sql = "CREATE TABLE team" +
                    " (id SERIAL not NULL, " +
                    " color VARCHAR(255) NOT NULL UNIQUE, " +
                    " CHECK (color <> ''), " +
                    " points INTEGER NOT NULL, " +
                    " PRIMARY KEY (id))";

            stmt.executeUpdate(sql);
            System.out.println("Table team created...");
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
        Team team = new Team(
                "green",
                "10"
        );
        insert(team);
        team = new Team(
                "red",
                "15"
        );
        insert(team);
    }

    public List<Team> getAll() {
        List<Team> teams = new ArrayList<>();

        String sql = "select id, color, points from " +
                tableName +
                " order by id";
        try (Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Team team = new Team(
                        rs.getString("id"),
                        rs.getString("color"),
                        rs.getString("points")
                );
                teams.add(team);
            }
        } catch (SQLException e) {
            errorMsg = e.toString();
//            setErrorMsg(e.toString());
            e.printStackTrace();
        }
        return teams;
    }

    public void insert(Team team) {
//        String sql = "insert into team (color, points) values (" +
//                "'" + team.getColor() + "', " +
//                "'" + team.getPoints() + "' " +
//                ");";

        String sql = "insert into team (color, points) values (?, ?)";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, team.getColor());
            stmt.setInt(2, Integer.parseInt(team.getPoints()));
            stmt.executeUpdate();
            System.out.println("Team record inserted successfully");
        } catch (NumberFormatException | SQLException e) {
            errorMsg = e.toString();
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        String sql = "delete from team where id=" + id;
        try(Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.executeUpdate();
            System.out.println("Record " + id + " deleted successfully");
        } catch (SQLException e) {
            errorMsg = e.toString();
            e.printStackTrace();
        }
    }

    public void update(Team team) {
//        String sql = "update team set " +
//                "color=" +
//                "'" + team.getColor() + "', " +
//                "points=" +
//                "'" + team.getPoints() + "' " +
//                " where id=" +
//                "'" + team.getId() + "'";

        String sql = "update team set color=?, points=? where id=?";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, team.getColor());
            stmt.setInt(2, Integer.parseInt(team.getPoints()));
            stmt.setInt(3, Integer.parseInt(team.getId()));
            stmt.executeUpdate();
            System.out.println("Record " + team.getId() + " updated successfully");
        } catch (NumberFormatException | SQLException e) {
            errorMsg = e.toString();
            e.printStackTrace();
        }
    }

    public String getErrorMsg() {
        return errorMsg;
    }

//    public void setErrorMsg(String errorMsg) {
//        this.errorMsg = errorMsg;
//    }

    public void resetError() {
        errorMsg = null;
    }


}