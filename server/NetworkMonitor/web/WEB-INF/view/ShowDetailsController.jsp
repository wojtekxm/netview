<%--
  Created by IntelliJ IDEA.
  User: Berent
  Date: 2017-01-17
  Time: 01:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="zesp03.entity.Controller" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    Controller controller = ( Controller )request.getAttribute( "controller" );
%>

<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Network Monitor</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
</head>

<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="index.jsp">Network Monitor</a>
        </div>
        <ul class="nav navbar-nav">
            <li class="active"><a href="">Menu</a></li>
            <li><a href="">kolejna zakladka</a></li>
        </ul>
        <hr>
    </div>
</nav>

<div class="container">
    <div class="list-group ">
        <div class="row">
            <a href="ShowControllers" class="btn btn-primary btn-default btn-lg active" role="button">Wróc</a>
            <form method="post" action="RemoveController?id=<%= controller.getId() %>">
                <input type="submit" value="Usuń" class="btn btn-primary btn-default btn-lg active" role="button">
            </form>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading">
                szczegóły urządzenia
            </div>
            <table class="table table-bordered">
                <tr>
                    <td>ID</td>
                    <td><%= controller.getId() %></td>
                </tr>
                <tr>
                    <td>Nazwa</td>
                    <td><%= controller.getName() %></td>
                </tr>
                <tr>
                    <td>IP</td>
                    <td><%= controller.getIpv4() %></td>
                </tr>
                <tr>
                    <td>Opis</td>
                    <td><%= ( controller.getDescription() != null && !controller.getDescription().isEmpty() ) ? controller.getDescription() : "(brak)" %></td>
                </tr>
            </table>
        </div>
    </div>
</div>

<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/jquery-3.1.1.min.js"></script>
</body>
</html>