package dz.isolation.dao;

import dz.isolation.model.Team;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDao implements Dao<Team> {
    public static final String tableName = "team";

    private DataSource ds;
    private String errorMsg;

    public TeamDao(DataSource ds) {
        try {
            if ((this.ds = ds) == null) {
                this.ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres");
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
            errorMsg = e.toString();
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
                10
        );
        insert(team);
        team = new Team(
                "red",
                15
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
                        rs.getInt("id"),
                        rs.getString("color"),
                        rs.getInt("points")
                );
                teams.add(team);
            }
        } catch (SQLException e) {
            errorMsg = e.toString();
            e.printStackTrace();
        }
        return teams;
    }

    public void insert(Team team) {
        String sql = "insert into team (color, points) values (?, ?)";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, team.getColor());
            stmt.setInt(2, team.getPoints());
            stmt.executeUpdate();
            System.out.println("Team record inserted successfully");
        } catch (SQLException e) {
            errorMsg = e.toString();
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "delete from team where id=?";
        try(Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Record " + id + " deleted successfully");
        } catch (SQLException e) {
            errorMsg = e.toString();
            e.printStackTrace();
        }
    }

    public void update(Team team) {
        String sql = "update team set color=?, points=? where id=?";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, team.getColor());
            stmt.setInt(2, team.getPoints());
            stmt.setInt(3, team.getId());
            stmt.executeUpdate();
            System.out.println("Record " + team.getId() + " updated successfully");
        } catch (SQLException e) {
            errorMsg = e.toString();
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