<%@ page import="zesp03.data.row.UserRow" %>
<%@ page import="zesp03.filter.AuthenticationFilter" %>
<%@ page import="zesp03.servlet.BlockPasswordServlet" %>
<%@ page import="zesp03.servlet.UserServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    UserRow loggedUser = (UserRow) request.getAttribute(AuthenticationFilter.ATTR_USERDATA);
    UserRow selectedUser = (UserRow) request.getAttribute(UserServlet.ATTR_USERDATA);
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Informacje o użytkowniku</title>
    <link rel="icon" href="/favicon.png">
</head>
<body>
zalogowany: <%= loggedUser.getName() %><br><br>
informacje o użytkowniku:<br>
id: <%= selectedUser.getId() %><br>
nazwa: <%= selectedUser.getName() %><br>
rola: <%= selectedUser.isAdmin() ? "administrator" : "zwykły użytkownik" %><br>
<%
    if (selectedUser.getSecret() == null) {
%>konto zablokowane<br>
<%
} else {
%>konto aktywne<br>
<form action="/block-password" method="post">
    <input type="hidden" name="<%= BlockPasswordServlet.POST_ID %>" value="<%= selectedUser.getId() %>">
    <button type="submit">Zablokuj dostęp</button>
</form>
<%
    }
%>
</body>
</html>
