<%@ page import="zesp03.data.row.UserRow" %>
<%@ page import="zesp03.filter.AuthenticationFilter" %>
<%@ page import="zesp03.servlet.BlockPasswordServlet" %>
<%@ page import="zesp03.servlet.UserServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    UserRow loggedUser = (UserRow) request.getAttribute(AuthenticationFilter.ATTR_USERROW);
    UserRow selectedUser = (UserRow) request.getAttribute(UserServlet.ATTR_USERROW);
    String role;
    switch (selectedUser.getRole()) {
        case ROOT:
            role = "root";
            break;
        case ADMIN:
            role = "administrator";
            break;
        default:
            role = "zwykły użytkownik";
    }
    String status = "konto aktywne";
    if (selectedUser.isBlocked()) status = "konto zablokowane";
    if (!selectedUser.isActivated()) status = "konto nieaktywowane";
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Informacje o użytkowniku</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
</head>
<body>
<div class="container" id="page">

    zalogowany: <%= loggedUser.getName() %><br><br>
    informacje o użytkowniku:<br>
    id: <%= selectedUser.getId() %><br>
    nazwa: <%= selectedUser.getName() %><br>
    rola: <%= role %><br>
    <%= status %><br>
    <%
        if (!selectedUser.isBlocked()) {
    %>
    <form action="/block-password" method="post">
        <input type="hidden" name="<%= BlockPasswordServlet.POST_ID %>" value="<%= selectedUser.getId() %>">
        <button type="submit">Zablokuj dostęp</button>
    </form>
    <%
        }
    %>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>