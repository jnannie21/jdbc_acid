package dz.isolation.service;

import dz.isolation.dao.Dao;
import dz.isolation.dao.TeamDao;
import dz.isolation.exception.DaoException;
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
    private Dao<Team> teamDao;

    /**
     * Default constructor.
     */
    public TeamService() {
        teamDao = new TeamDao();
    }

    /**
     * Get all teams.
     * @return List of Team.
     */
    public List<Team> getAll() throws SQLException {
        return teamDao.getAll();
    }

    /**
     * Create from HttpServletRequest and insert to db new team.
     * @param req
     */
    public void insert(HttpServletRequest req)
            throws NumberFormatException, SQLException, DaoException {
        Team team = new Team(
                req.getParameter("color"),
                Integer.parseInt(req.getParameter("points"))
        );
        teamDao.insert(team);
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
        teamDao.update(team);
    }

    /**
     * Delete team.
     * @param req
     */
    public void delete(HttpServletRequest req) throws NumberFormatException, SQLException {
        teamDao.delete(Integer.parseInt(req.getParameter("id")));
    }
}
