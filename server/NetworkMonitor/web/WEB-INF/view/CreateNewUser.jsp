<%@ page import="zesp03.data.UserData" %>
<%@ page import="zesp03.filter.AuthenticationFilter" %>
<%@ page import="zesp03.servlet.ActivateAccountServlet" %>
<%@ page import="zesp03.servlet.CreateNewUserServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    UserData loggedUser = (UserData) request.getAttribute(AuthenticationFilter.ATTR_USERDATA);
    Long tokenId = (Long) request.getAttribute(CreateNewUserServlet.ATTR_TOKEN_ID);
    String tokenValue = (String) request.getAttribute(CreateNewUserServlet.ATTR_TOKEN_VALUE);
    //TODO...
    String link = new StringBuilder()
            .append("http://")
            .append(request.getServerName())
            .append(request.getServerPort() != 80 ? ":" + request.getServerPort() : "")
            .append("/activate-account?")
            .append(ActivateAccountServlet.GET_TOKEN_ID)
            .append("=")
            .append(tokenId.toString())
            .append("&")
            .append(ActivateAccountServlet.GET_TOKEN_VALUE)
            .append("=")
            .append(tokenValue)
            .toString();
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Nowy użytkownik</title>
    <link rel="icon" href="/favicon.png">
</head>
<body>
stworzono nowego użytkownika, użyj tego linku by aktywować konto i ustawić hasło<br>
<a href="<%= link %>"><%= link %>
</a>
</body>
</html>
