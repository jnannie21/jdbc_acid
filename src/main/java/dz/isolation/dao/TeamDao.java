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
    public static final String TABLE_NAME = "team";

    /**
     * Queries.
     */
    public static final String SELECT_ALL = "select id, color, points from " + TABLE_NAME + " order by id";
    public static final String INSERT = "insert into team (color, points) values (?, ?)";
    public static final String DELETE = "delete from team where id=?";
    public static final String UPDATE = "update team set color=?, points=? where id=?";

    /**
     * DataSource object.
     */
    private DataSource ds;

    /**
     * Default constructor.
     */
    public TeamDao() {
        try {
            this.ds = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres");
        } catch (NamingException e) {
            throw new IllegalStateException("jdbc/postgres is missing in JNDI!", e);
        }
    }

    /**
     * Constructor which accepts DataSource as source of data.
     * @param ds data source.
     */
    public TeamDao(DataSource ds) {
        this.ds = ds;
    }

    /**
     * Get all records as Team objects in List.
     * @return List of Team s
     */
    public List<Team> getAll() throws SQLException {
        List<Team> teams = new ArrayList<>();

        try (Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
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
        }
        return teams;
    }

    /**
     * Insert new record in team table.
     * @param team Team entity to insert.
     */
    public void insert(Team team) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT);
        ) {
            stmt.setString(1, team.getColor());
            stmt.setInt(2, team.getPoints());
            stmt.executeUpdate();
            System.out.println("Team record inserted successfully");
        }
    }

    /**
     * Delete record by id.
     * @param id id of record.
     */
    public void delete(int id) throws SQLException {
        try (Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE);
        ) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Record " + id + " deleted successfully");
        }
    }

    /**
     * Update record in table.
     * @param team record to update.
     */
    public void update(Team team) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE);
        ) {
            stmt.setString(1, team.getColor());
            stmt.setInt(2, team.getPoints());
            stmt.setInt(3, team.getId());
            stmt.executeUpdate();
            System.out.println("Record " + team.getId() + " updated successfully");
        }
    }
}