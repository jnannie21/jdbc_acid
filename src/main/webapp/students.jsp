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
<%--        <tr>--%>
<%--            <td>1</td>--%>
<%--            <td>Mark</td>--%>
<%--            <td>Tompson</td>--%>
<%--            <td>the_mark7</td>--%>
<%--            <td><button>Change</button></td>--%>
<%--            <td><button>Delete</button></td>--%>
<%--        </tr>--%>

        <%
            List<HashMap<String, String>> users = (List<HashMap<String, String>>)request.getAttribute("students");
            Iterator<HashMap<String, String>> it = users.iterator();
            while(it.hasNext()) {
                HashMap<String, String> student = it.next();
                out.println("<tr>");
                out.println("<td>" + student.get("id") + "</td>");
                out.println("<td>" + student.get("first_name") + "</td>");
                out.println("<td>" + student.get("last_name") + "</td>");
                out.println("<td>" + student.get("age") + "</td>");
                out.println(printChangeBtn(student.get("id")));
                out.println(printDeleteBtn(student.get("id")));
                out.println("</tr>");
            }
        %>

        <%!
            public String printChangeBtn(String id) {
                return "<td><form action=\"change_student\" method=\"post\">\n" +
                            "<button name=\"change\" value=\"" +
                            id +
                            "\">Change</button>\n" +
                        "</form></td>";
            }

            public String printDeleteBtn(String id) {
                return "<td><form action=\"delete_student\" method=\"post\">\n" +
                        "<button name=\"delete\" value=\"" +
                        id +
                        "\">Delete</button>\n" +
                        "</form></td>";
//                return "<td><button name=\"foo\" value=\"upvote\">Delete</button></td>";
            }
        %>

        </tbody>
    </table>
</body>
</html>
