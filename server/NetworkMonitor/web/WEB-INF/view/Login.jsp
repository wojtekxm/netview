<%@ page import="zesp03.servlet.Login"
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
%>
<%
    Boolean failed = (Boolean) request.getAttribute(Login.ATTR_FAILED);
    if (failed == null) failed = false;
    String error = request.getParameter(Login.GET_ERROR);
%><!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Network Monitor</title>
    <link rel="icon" href="/favicon.png">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
</head>
<body>
<div class="container"><%
    if (error != null) {
%>
    <h1>Musisz być zalogowany by wejść na tą stronę</h1><%
        }
        if (failed) {
    %>
    <h1>Nazwa użytkownika lub hasło nie poprawne, spróbuj jeszcze raz</h1><%
        }
    %>
    <form action="/login" method="post">
        <div>nazwa użytkownika</div>
        <input type="text" name="<%= Login.POST_USERNAME %>">
        <div>hasło</div>
        <input type="password" name="<%= Login.POST_PASSWORD %>">
        <br>
        <input type="submit" value="Zaloguj">
    </form>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>