<%@ page import="zesp03.data.row.UserRow" %>
<%@ page import="zesp03.filter.AuthenticationFilter" %>
<%@ page import="zesp03.servlet.AllUsersServlet" %>
<%@ page import="zesp03.servlet.UserServlet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    UserRow userRow = (UserRow) request.getAttribute(AuthenticationFilter.ATTR_USERROW);
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
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/style.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
</head>
<body>
<div class="container">
    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <ul class="nav nav-pills" style="padding-top: 3px;font-size: 17px;position: absolute;width: 100%;left: 0;text-align: center;margin:0 auto;">
                <li role="presentation"><a href="/make-survey">Nowe badanie</a></li>
                <li role="presentation"><a href="/status-small">Mały widok</a></li>
                <li role="presentation"><a href="/all-controllers">Kontrolery</a></li>
                <li role="presentation"><a href="/all-users">Użytkownicy</a></li>
                <li role="presentation"><a href="/all-devices">Urządzenia</a></li>
                <form class="navbar-form nav-pills" style="padding-top: 2px;margin-top:2px;">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
                </form>
            </ul>
            <ul class="nav nav-pills pull-left" style="padding-top: 3px;border-radius: 10px;padding-left:7px;font-size: 17px;">
                <li role="presentation" class="active"><a href="/"><span class="glyphicon glyphicon-home"></span>  Strona główna</a></li>
            </ul>
            <ul class="nav nav-pills pull-right" style="padding-top: 3px;padding-right:3px;font-size: 17px;">
                <li role="presentation"><a href="#"><span class="glyphicon glyphicon-user"></span>  Mój profil</a></li>
                <li role="presentation"><a href="/logout"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </nav>
    <div class="welcome">
        <div class="tittleStatic">NETWORK-MONITOR</div>
        <div class="userStatic">zalogowany: <%= userRow.getName() %>
        </div>
        <div class="logo"><img src="/images/logooWhite.jpg"></div>
    </div>
    zalogowany: <%= userRow.getName() %><br><br>
    lista użytkowników:<br>
    <%
        for (UserRow u : allUsers) {
            String href = "/user?" + UserServlet.GET_ID + "=" + u.getId();
            String label = u.isActivated() ? u.getName() : "[" + u.getId() + " - konto nieaktywne]";
    %><a href="<%= href %>"><%= label %>
</a><br>
    <%
        }
    %>
    <hr>
    <a href="/new-user">Tworzenie nowego użytkownika</a>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>