package dz.isolation.service;

import dz.isolation.dao.TeamDao;
import dz.isolation.model.Team;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class TeamService implements Service<Team> {
    private TeamDao teamDao;
    private String errorMsg;

    public TeamService() {
        teamDao = new TeamDao(null);
    }

    public List<Team> getAll() {
        return teamDao.getAll();
    }

    public void insert(HttpServletRequest req) {
        try {
            Team team = new Team(
                    req.getParameter("color"),
                    Integer.parseInt(req.getParameter("points"))
            );
            teamDao.insert(team);
        } catch (NumberFormatException e) {
            errorMsg = e.toString();
        }

    }

    public void update(HttpServletRequest req) {
        try {
            Team team = new Team(
                    Integer.parseInt(req.getParameter("id")),
                    req.getParameter("color"),
                    Integer.parseInt(req.getParameter("points"))
            );
            teamDao.update(team);
        } catch (NumberFormatException e) {
            errorMsg = e.toString();
        }
    }

    public void delete(HttpServletRequest req) {
        try {
            teamDao.delete(Integer.parseInt(req.getParameter("id")));
        } catch (NumberFormatException e) {
            errorMsg = e.toString();
        }
    }

    public String getErrorMsg() {
        return errorMsg == null ? teamDao.getErrorMsg() : errorMsg;
    }

    public void resetError() {
        errorMsg = null;
        teamDao.resetError();
    }
}
