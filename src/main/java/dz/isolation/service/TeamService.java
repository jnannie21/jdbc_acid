package dz.isolation.service;

import dz.isolation.dao.StudentDao;
import dz.isolation.dao.TeamDao;
import dz.isolation.model.Student;
import dz.isolation.model.Team;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class TeamService {
    private TeamDao teamDao;

    public TeamService() {
        teamDao = new TeamDao(null);
    }

    public List<Team> getAll() {
        return teamDao.getAll();
    }

    public void addTeam(HttpServletRequest req) {
        Team team = new Team(
                req.getParameter("color"),
                req.getParameter("points")
        );
        teamDao.insert(team);
    }

    public void changeTeam(HttpServletRequest req) {
        Team team = new Team(
                req.getParameter("id"),
                req.getParameter("color"),
                req.getParameter("points")
        );
        teamDao.update(team);
    }

    public void deleteTeam(HttpServletRequest req) {
        teamDao.delete(req.getParameter("id"));
    }

    public String getErrorMsg() {
        return teamDao.getErrorMsg();
    }

    public void resetError() {
        teamDao.resetError();
    }
}
