<%--
  Created by IntelliJ IDEA.
  User: Berent
  Date: 2016-12-21
  Time: 00:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="zesp03.entity.Controller" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    List< Controller > controllers = ( List< Controller > )request.getAttribute( "controllers" );
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