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

    public TeamService() {
        try {
            ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
        } catch (NamingException e) {
            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
        }
    }

    public List<HashMap<String, String>> getTeams() {
        List<HashMap<String, String>> teams = new ArrayList<>();

//        String sql = "select * from " + tableName;
        String sql = "select s.id, s.first_name, s.last_name, s.age, s.points, s.team_id, t.color, t.points as team_points from " +
                tableName +
                " as s inner join " +
                TeamService.tableName +
                " as t on s.team_id = t.id order by s.id";
        try(Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                HashMap<String, String> team = new HashMap<>();
                team.put("id", rs.getString("id"));
                team.put("color", rs.getString("color"));
                team.put("team_points", rs.getString("points"));
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

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.executeUpdate();
            System.out.println("Team record inserted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}