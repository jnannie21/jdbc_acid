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
    DataSource ds;
    public static final String tableName = "team";
    private String errorMsg;

    public TeamService() {
        try {
            ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
        } catch (NamingException e) {
            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
        }
    }

    public List<HashMap<String, String>> getTeams() {
        List<HashMap<String, String>> teams = new ArrayList<>();

        String sql = "select id, color, points from " +
                tableName +
                " order by id";
        try (Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                HashMap<String, String> team = new HashMap<>();
                team.put("id", rs.getString("id"));
                team.put("color", rs.getString("color"));
                team.put("points", rs.getString("points"));
                teams.add(team);
            }
        } catch (SQLException e) {
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

    public void resetError() {
        errorMsg = null;
    }
}