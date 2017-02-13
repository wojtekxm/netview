<%@ page import="zesp03.data.row.UserRow" %>
<%@ page import="zesp03.filter.AuthenticationFilter" %>
<%@ page import="zesp03.servlet.AllUsersServlet" %>
<%@ page import="zesp03.servlet.UserServlet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    UserRow loggedUser = (UserRow) request.getAttribute(AuthenticationFilter.ATTR_USERROW);
    ArrayList<UserRow> allUsers = (ArrayList<UserRow>) request.getAttribute(AllUsersServlet.ATTR_USERS);
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Użytkownicy</title>
    <link rel="icon" href="/favicon.png">
</head>
<body>
zalogowany: <%= loggedUser.getName() %><br><br>
lista użytkowników:<br>
<%
    for (UserRow u : allUsers) {
        String href = "/user?" + UserServlet.GET_ID + "=" + u.getId();
        String label = u.getName() != null ? u.getName() : "[" + u.getId() + " - konto nieaktywne]";
%><a href="<%= href %>"><%= label %>
</a><br>
<%
    }
%>
<hr>
<form action="/create-new-user" method="post">
    <button type="submit">Stwórz nowego użytkownika</button>
</form>
</body>
</html>
