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
                    " color VARCHAR(255), " +
                    " points INTEGER, " +
                    " PRIMARY KEY (id))";

            stmt.executeUpdate(sql);
            System.out.println("Table " + tableName + " created...");
            populateTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateTable() {
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

    public List<HashMap<String, String>> getTeams() {
        List<HashMap<String, String>> teams = new ArrayList<>();

        String sql = "select * from " + tableName;
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