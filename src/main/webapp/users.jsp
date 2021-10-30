<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users table</title>
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
            List<HashMap<String, String>> users = (List<HashMap<String, String>>)request.getAttribute("users");
//            System.out.println(users);
            Iterator<HashMap<String, String>> it = users.iterator();
            while(it.hasNext()){
                HashMap<String, String> user = it.next();
                out.println("<tr>");
                out.println("<td>" + user.get("id") + "</td>");
                out.println("<td>" + user.get("first_name") + "</td>");
                out.println("<td>" + user.get("last_name") + "</td>");
                out.println("<td>" + user.get("age") + "</td>");
                out.println("<td><button>Change</button></td>");
                out.println("<td><button>Delete</button></td>");
                out.println("</tr>");
            }
        %>

        </tbody>
    </table>
</body>
</html>
