<%@ page import="zesp03.entity.Controller"
%>
<%@ page import="java.util.List"
%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%>
<%
    List< Controller > controllers = ( List< Controller > )request.getAttribute( "controllers" );
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
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/index.jsp">Network Monitor</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="/make-survey">nowe badania</a></li>
            <li><a href="/status-small">urządzenia (mały widok)</a></li>
            <li><a href="/deviceinfo">urządzenia (średni widok)</a></li>
            <li class="active"><a href="/ShowControllers">kontrolery</a></li>
            <li><a href="/logout">wyloguj</a></li>
        </ul>
    </div>
</nav>

<div class="container">
    <div class="list-group ">
        <div class="row">
            <a href="Controllers.jsp" class="btn btn-primary btn-default btn-lg active" role="button">Stwórz nowy kontroler</a>
        </div>

        <% for( int i = 0; i < controllers.size(); ++i ) { %>
        <div class="row">
            <a href="ShowDetailsController?id=<%= controllers.get( i ).getId() %>" class="col-md-4  list-group-item list-group-item-info" >
                <%= controllers.get( i ).getName() %>
                <%= controllers.get( i ).getIpv4() %>
                <%= ( controllers.get( i ).getDescription() != null && !controllers.get( i ).getDescription().isEmpty() ) ? controllers.get( i ).getDescription() : "null" %>
            </a>
        </div>
        <% } %>
    </div>
</div>

<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/jquery-3.1.1.min.js"></script>

</body>
</html>