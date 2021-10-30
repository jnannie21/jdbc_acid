<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Students table</title>
</head>
<body>
    <h1>
        Available students
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
        <%
            List<HashMap<String, String>> students = (List<HashMap<String, String>>)request.getAttribute("students");
            for (Iterator<HashMap<String, String>> it = students.iterator(); it.hasNext();)
            {
                HashMap<String, String> student = it.next();
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
        <%  } %>
        <tr>
            <form action="add_student" method="post">
                <td>n/a</td>
                <td><input type="text" value="" name="first_name"/></td>
                <td><input type="text" value="" name="last_name"/></td>
                <td><button type="submit">Add new</button></td>
            </form>
        </tr>
        </tbody>
    </table>
</body>
</html>
