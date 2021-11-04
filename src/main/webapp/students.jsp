<%@ page import="java.util.*" %>
<%@ page import="dz.isolation.dao.StudentDao" %>
<%@ page import="dz.isolation.dao.TeamDao" %>
<%@ page import="dz.isolation.model.Team" %>
<%@ page import="dz.isolation.model.Student" %>
<%@ page import="dz.isolation.dao.StudentDao" %>
<%@ page import="dz.isolation.dao.TeamDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Students and teams table</title>
</head>
<body>
    <div style="font-size: larger; color: brown">
            <%
                StudentDao studentDao = (StudentDao) request.getAttribute("studentDao");
                System.out.println("studentDao");
                System.out.println(studentDao);
                TeamDao teamDao = (TeamDao) request.getAttribute("teamDao");
                String studentServiceError = studentDao.getErrorMsg();
                String teamServiceError = teamDao.getErrorMsg();

                if (studentServiceError != null) {
                    out.print(studentServiceError);
                }
                if (teamServiceError != null) {
                    out.print(teamServiceError);
                }
            %>
    </div>
    <h1>
        Available students
    </h1>
    <table style="font-size: larger">
        <thead>
            <tr>
                <th>id</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Age</th>
                <th>Points</th>
                <th>Team_id</th>
                <th></th>
            </tr>
        </thead>

        <tbody>
        <%
            List<Student> students = (List<Student>)request.getAttribute("students");
            for (Iterator<Student> it = students.iterator(); it.hasNext();)
            {
                Student student = it.next();
        %>
                <tr>
                    <form action="change_student" method="post">
                        <td><%=student.getId() %> <input type="hidden" value="<%=student.getId() %>" name="id"></td>
                        <td><input type="text" value="<%=student.getFirstName() %>" name="first_name"/></td>
                        <td><input type="text" value="<%=student.getLastName() %>" name="last_name"/></td>
                        <td><input type="text" value="<%=student.getAge() %>" name="age"/></td>
                        <td><input type="text" value="<%=student.getPoints() %>" name="points"/></td>
                        <td><input type="text" value="<%=student.getTeamId() %>" name="team_id"/></td>
                        <td><button type="submit">Change</button></td>
                    </form>

                    <form action="delete_student" method="post">
                        <input type="hidden" value="<%=student.getId() %>" name="id">
                        <td><button type="submit">Delete</button></td>
                    </form>
                </tr>
        <%  } %>
        <tr>
            <form action="add_student" method="post">
                <td>n/a</td>
                <td><input type="text" value="" name="first_name"/></td>
                <td><input type="text" value="" name="last_name"/></td>
                <td><input type="text" value="" name="age"/></td>
                <td><input type="text" value="" name="points"/></td>
                <td><input type="text" value="" name="team_id"/></td>
                <td><button type="submit">Add new</button></td>
            </form>
        </tr>
        </tbody>
    </table>

    <h1>
        Available teams
    </h1>
    <table style="font-size: larger">
        <thead>
        <tr>
            <th>id</th>
            <th>Color</th>
            <th>Points</th>
        </tr>
        </thead>

        <tbody>
        <%
            List<Team> teams = (List<Team>)request.getAttribute("teams");
            for (Iterator<Team> it = teams.iterator(); it.hasNext();)
            {
                Team team = it.next();
        %>
        <tr>
            <form action="change_team" method="post" name="change_team<%=team.getId() %>">
                <td><%=team.getId() %> <input type="hidden" value="<%=team.getId() %>" name="id"></td>
                <td><input type="text" value="<%=team.getColor() %>" name="color"/></td>
                <td><input type="text" value="<%=team.getPoints() %>" name="points"/></td>
                <td><button type="submit" name="submit_change_team">Change</button></td>
            </form>

            <form action="delete_team" method="post">
                <input type="hidden" value="<%=team.getId() %>" name="id">
                <td><button type="submit">Delete</button></td>
            </form>
        </tr>
        <%  } %>
        <tr>
            <form action="add_team" method="post">
                <td>n/a</td>
                <td><input type="text" value="" name="color"/></td>
                <td><input type="text" value="" name="points"/></td>
                <td><button type="submit">Add new</button></td>
            </form>
        </tr>
        </tbody>
    </table>
</body>
</html>
