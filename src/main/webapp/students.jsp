<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Students table</title>
</head>
<body>
    <h1>
        Available users
    </h1>
<%--    <table style="font-size: larger">--%>
<%--        <thead>--%>
<%--        <tr>--%>
<%--            <th>#</th>--%>
<%--            <th>First Name</th>--%>
<%--            <th>Last Name</th>--%>
<%--            <th>Username</th>--%>
<%--        </tr>--%>
<%--        </thead>--%>
<%--        <tbody>--%>

<%--        <%--%>
<%--            List<HashMap<String, String>> users = (List<HashMap<String, String>>)request.getAttribute("students");--%>
<%--            Iterator<HashMap<String, String>> it = users.iterator();--%>
<%--            while(it.hasNext()) {--%>
<%--                HashMap<String, String> student = it.next();--%>
<%--                out.println("<tr>");--%>
<%--                out.println("<td>" + student.get("id") + "</td>");--%>
<%--                out.println("<td>" + student.get("first_name") + "</td>");--%>
<%--                out.println("<td>" + student.get("last_name") + "</td>");--%>
<%--                out.println("<td>" + student.get("age") + "</td>");--%>
<%--                out.println(printChangeBtn(student.get("id")));--%>
<%--                out.println(printDeleteBtn(student.get("id")));--%>
<%--                out.println("</tr>");--%>
<%--            }--%>
<%--        %>--%>

<%--<form action="change_student" method="post">--%>
    <table style="font-size: larger">
        <thead>
            <tr>
                <th>#</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Username</th>
            </tr>
        </thead>

        <tbody>
        <%
            List<HashMap<String, String>> students = (List<HashMap<String, String>>)request.getAttribute("students");
            Iterator<HashMap<String, String>> it = students.iterator();
            for (it = students.iterator(); it.hasNext();)
            {
                HashMap<String, String> student = (HashMap<String, String>)it.next();
        %>
        <tr>
            <form action="change_student" method="post">
                <td><%=student.get("id") %> <input type="hidden" value="<%=student.get("id") %>" name="id"></td>
                <td><input type="text" value="<%=student.get("first_name") %>" name="first_name"/></td>
                <td><input type="text" value="<%=student.get("last_name") %>" name="last_name"/></td>
                <td><button type="submit">Change</button></td>
            </form>

            <form action="delete_student" method="post">
                <input type="hidden" value="<%=student.get("id") %>" name="id">
                <td><button type="submit">Delete</button></td>
            </form>
        </tr>
        <%} %>
        </tbody>
    </table>
<%--    <button type="submit">Change</button>--%>
<%--</form>--%>

<%--        <%!--%>
<%--            public String printChangeBtn(String id) {--%>
<%--                return "<td><form action=\"change_student\" method=\"post\">\n" +--%>
<%--                            "<button name=\"change\" value=\"" +--%>
<%--                            id +--%>
<%--                            "\">Change</button>\n" +--%>
<%--                        "</form></td>";--%>
<%--            }--%>

<%--            public String printDeleteBtn(String id) {--%>
<%--                return "<td><form action=\"delete_student\" method=\"post\">\n" +--%>
<%--                        "<button name=\"delete\" value=\"" +--%>
<%--                        id +--%>
<%--                        "\">Delete</button>\n" +--%>
<%--                        "</form></td>";--%>
<%--//                return "<td><button name=\"foo\" value=\"upvote\">Delete</button></td>";--%>
<%--            }--%>
<%--        %>--%>

<%--        </tbody>--%>
<%--    </table>--%>
</body>
</html>
