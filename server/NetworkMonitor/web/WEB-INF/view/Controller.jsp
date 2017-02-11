<%@ page import="zesp03.data.ControllerData" %>
<%@ page import="zesp03.servlet.ControllerServlet" %>
<%@ page import="zesp03.servlet.RemoveControllerServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    ControllerData controllerData = (ControllerData) request.getAttribute(ControllerServlet.ATTR_CONTROLLERDATA);
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Informacje o kontrolerze</title>
    <link rel="icon" href="/favicon.png">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
</head>
<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">Network Monitor</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="/make-survey">nowe badania</a></li>
            <li><a href="/status-small">urządzenia (mały widok)</a></li>
            <li><a href="/status">urządzenia (średni widok)</a></li>
            <li><a href="/all-controllers">kontrolery</a></li>
            <li><a href="/logout">wyloguj</a></li>
        </ul>
    </div>
</nav>

<div class="container">
    <div class="list-group ">
        <div class="row">
            <form method="post" action="/remove-controller">
                <input type="hidden" name="<%= RemoveControllerServlet.POST_ID %>"
                       value="<%= controllerData.getId() %>">
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
                    <td><%= controllerData.getId() %>
                    </td>
                </tr>
                <tr>
                    <td>Nazwa</td>
                    <td><%= controllerData.getName() %>
                    </td>
                </tr>
                <tr>
                    <td>IP</td>
                    <td><%= controllerData.getIpv4() %>
                    </td>
                </tr>
                <tr>
                    <td>Opis</td>
                    <td><%= controllerData.getDescription() != null ? controllerData.getDescription() : "<em>(brak)</em>" %>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>

<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/jquery-3.1.1.min.js"></script>
</body>
</html>