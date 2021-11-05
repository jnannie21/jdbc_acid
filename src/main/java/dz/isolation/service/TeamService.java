package dz.isolation.service;

import dz.isolation.dao.Dao;
import dz.isolation.dao.TeamDao;
import dz.isolation.model.Team;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

/**
 * Service for fetching teams from db via Dao.
 */
public class TeamService implements Service<Team> {
    /**
     * Dao for accessing team table.
     */
    private TeamDao teamDao;

    /**
     * Returns Dao in a lazy way.
     * @return Teams Dao object.
     */
    private Dao<Team> getDao() {
        if (teamDao == null) {
            teamDao = new TeamDao();
        }
        return teamDao;
    }

    /**
     * Get all teams.
     * @return List of Team.
     */
    public List<Team> getAll() throws SQLException {
        return getDao().getAll();
    }

    /**
     * Create from HttpServletRequest and insert to db new team.
     * @param req
     */
    public void insert(HttpServletRequest req) throws NumberFormatException, SQLException {
        Team team = new Team(
                req.getParameter("color"),
                Integer.parseInt(req.getParameter("points"))
        );
        getDao().insert(team);
    }

    /**
     * Update existing team.
     * @param req
     */
    public void update(HttpServletRequest req) throws NumberFormatException, SQLException {
        Team team = new Team(
                Integer.parseInt(req.getParameter("id")),
                req.getParameter("color"),
                Integer.parseInt(req.getParameter("points"))
        );
        getDao().update(team);
    }

    /**
     * Delete team.
     * @param req
     */
    public void delete(HttpServletRequest req) throws NumberFormatException, SQLException {
        getDao().delete(Integer.parseInt(req.getParameter("id")));
    }
}
