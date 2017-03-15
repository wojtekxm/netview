<%@ page import="zesp03.servlet.ActivateAccountServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Long tokenId = (Long) request.getAttribute(ActivateAccountServlet.ATTR_TOKEN_ID);
    String tokenValue = (String) request.getAttribute(ActivateAccountServlet.ATTR_TOKEN_VALUE);
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Aktywacja konta</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container">
    <h1>Aktywacja konta użytkownika</h1>
    <form action="/activate-account" method="post">
        <input type="hidden" name="<%= ActivateAccountServlet.POST_TOKEN_ID %>" value="<%= tokenId %>">
        <input type="hidden" name="<%= ActivateAccountServlet.POST_TOKEN_VALUE %>" value="<%= tokenValue %>">
        nazwa użytkownika<br>
        <input type="text" name="<%= ActivateAccountServlet.POST_USERNAME %>"><br><br>
        wybierz hasło<br>
        <input type="password" name="<%= ActivateAccountServlet.POST_PASSWORD1 %>"><br><br>
        powtórz hasło<br>
        <input type="password" name="<%= ActivateAccountServlet.POST_PASSWORD2 %>"><br><br>
        <button type="submit">Aktywuj konto</button>
    </form>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>