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
            createTeamTable(this.ds);
        } catch (NamingException e) {
            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
        }
    }

    public void createTeamTable(DataSource ds) {
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
            populateTeamTable();
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

    private void populateTeamTable() {
        Team team = new Team(
                "red",
                "10"
        );
        addTeam(team);
        team = new Team(
                "green",
                "20"
        );
        addTeam(team);
    }

    public List<Team> getTeams() {
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
            setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        return teams;
    }

    public void addTeam(Team team) {
        String sql = "insert into team (color, points) values (" +
                "'" + team.getColor() + "', " +
                "'" + team.getPoints() + "' " +
                ");";

//        String sql = "insert into team (color, points) values (?, ?)";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
//            stmt.setString(1, team.getColor());
//            String points = team.getPoints();
//            stmt.setInt(2, points.length() > 0 ? Integer.parseInt(points) : 0);
            stmt.executeUpdate();
            System.out.println("Team record inserted successfully");
        } catch (SQLException e) {
            errorMsg = e.getMessage();
            e.printStackTrace();
        }
    }

    public void deleteTeam(String id) {
        String sql = "delete from team where id=" + id;
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

    public void updateTeam(Team team) {
        String sql = "update team set " +
                "color=" +
                "'" + team.getColor() + "', " +
                "points=" +
                "'" + team.getPoints() + "' " +
                " where id=" +
                "'" + team.getId() + "'";

//        String sql = "update team set color=?, points=? where id=?";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
//            stmt.setString(1, team.getColor());
//            Integer points = team.getPoints().length() > 0 ? Integer.decode(team.getPoints()) : null;
//            stmt.setObject(2, points);
//            Integer id = team.getId().length() > 0 ? Integer.decode(team.getId()) : null;
//            stmt.setObject(3, id);
            stmt.executeUpdate();
            System.out.println("Record " + team.getId() + " updated successfully");
        } catch (SQLException e) {
            errorMsg = e.getMessage();
            e.printStackTrace();
        }
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