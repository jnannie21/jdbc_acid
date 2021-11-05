package dz.isolation.service;

import dz.isolation.dao.Dao;
import dz.isolation.dao.TeamDao;
import dz.isolation.model.Team;

import javax.servlet.http.HttpServletRequest;
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
     * Internal service's error.
     */
    private String errorMsg;

    /**
     * Returns Dao in a lazy way.
     * @return Teams Dao object.
     */
    private Dao<Team> getDao() {
        if (teamDao == null) {
            teamDao = new TeamDao(null);
        }
        return teamDao;
    }

    /**
     * Get all teams.
     * @return List of Team.
     */
    public List<Team> getAll() {
        return teamDao.getAll();
    }

    /**
     * Create from HttpServletRequest and insert to db new team.
     * @param req
     */
    public void insert(HttpServletRequest req) {
        try {
            Team team = new Team(
                    req.getParameter("color"),
                    Integer.parseInt(req.getParameter("points"))
            );
            getDao().insert(team);
        } catch (NumberFormatException e) {
            errorMsg = e.toString();
        }

    }

    /**
     * Update existing team.
     * @param req
     */
    public void update(HttpServletRequest req) {
        try {
            Team team = new Team(
                    Integer.parseInt(req.getParameter("id")),
                    req.getParameter("color"),
                    Integer.parseInt(req.getParameter("points"))
            );
            getDao().update(team);
        } catch (NumberFormatException e) {
            errorMsg = e.toString();
        }
    }

    /**
     * Delete team.
     * @param req
     */
    public void delete(HttpServletRequest req) {
        try {
            getDao().delete(Integer.parseInt(req.getParameter("id")));
        } catch (NumberFormatException e) {
            errorMsg = e.toString();
        }
    }

    /**
     * Get this service's internal error, if there is one, or Dao internal error.
     * @return error String or null.
     */
    public String getErrorMsg() {
        return errorMsg == null ? getDao().getErrorMsg() : errorMsg;
    }

    /**
     * Reset this and Dao object's internal errors.
     */
    public void resetError() {
        errorMsg = null;
        getDao().resetError();
    }
}
