package dz.isolation.dao;

import dz.isolation.model.Team;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao class for operations with team table.
 */
public class TeamDao implements Dao<Team> {
    /**
     * Table name.
     */
    public static final String tableName = "team";

    /**
     * DataSource object.
     */
    private DataSource ds;

    /**
     * Internal error message.
     */
    private String errorMsg;

    /**
     * Constructor which accepts DataSource as source of data, or null.
     * If argument is null then jndi lookup used for getting data source.
     * @param ds data source or null.
     */
    public TeamDao(DataSource ds) {
        try {
            if ((this.ds = ds) == null) {
                this.ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres");
            }
        } catch (NamingException e) {
            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
        }
    }

    /**
     * Get all records as Team objects in List.
     * @return List of Team s
     */
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

    /**
     * Insert new record in team table.
     * @param team Team entity to insert.
     */
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

    /**
     * Delete record by id.
     * @param id id of record.
     */
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

    /**
     * Update record in table.
     * @param team record to update.
     */
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

    /**
     * Get internal error message.
     * @return error message.
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Set internal message to null.
     */
    public void resetError() {
        errorMsg = null;
    }
}