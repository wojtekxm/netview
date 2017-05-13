<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Wyniki wyszukiwania</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/notify.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <link rel="icon" href="/favicon.ico">
</head>
<body>

<nav class="navbar navbar-inverse navbar-fixed-top" style="background-color: #080b08;">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myDiv">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>

        <div class="collapse navbar-collapse" id="myDiv">
            <ul class="nav navbar-nav" style="padding-right:3px;font-size: 16px;">
                <ul class="nav navbar-nav" style="padding-right:3px;font-size: 16px;">
                    <li><a style="background-color: black;padding-left:25px;padding-right: 20px;" href="/"><span class="glyphicon glyphicon-home"></span> &nbsp;NetView &nbsp;</a></li>
                    <c:if test="${loggedUser.role eq 'ROOT'}">  <li><a href="/all-controllers">Kontrolery</a></li>
                    <li><a href="/all-users">Użytkownicy</a></li>
                    <li><a href="/all-devices">Urządzenia</a></li>
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">Lokalizacje<span class="caret"></span></a>
                        <ul class="dropdown-menu"  style="background-color: #080b08;">
                            <li><a href="/all-buildings">Budynki</a></li>
                            <li><a href="/all-units">Jednostki</a></li></c:if>
                        </ul>
                </ul>
                </li>
            </ul>
            <c:if test="${loggedUser.role eq 'ROOT'}">  <form method="get" action="/search" class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" name="query" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 150px!important;">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
                </div>
            </form></c:if>
            <ul class="nav navbar-nav navbar-right" style="padding-right:3px;font-size: 16px;">
                <c:if test="${loggedUser.role eq 'ROOT'}">  <li><a href="/settings"><span class="glyphicon glyphicon-wrench"></span>  Ustawienia</a></li></c:if>
                <li><a href="/account"><span class="glyphicon glyphicon-user"></span>  <c:out value="${loggedUser.name}"/></a></li>
                <li><a href="/logout" style="margin-right: 10px;"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <div style="height: 80px;"></div>
    <div class="panel panel-default" id="header" style="margin-bottom: 15px!important;">
        <div class="panel-body">
            <div id="tittle"><span class="glyphicon glyphicon-wrench"></span> Wyniki dla wyszukiwania: <c:out value="${param.query}"/></div>
        </div>
    </div>

    <div class="panel panel-default" id="content">
        <h3>urządzenia</h3>
        <ol class="list">
            <c:forEach var="deviceDto" items="${search.devices}"><li><c:url var="h" value="/device/${deviceDto.id}"/>
                <a href="${h}"><c:out value="${deviceDto.name}"/></a>
            </li>
            </c:forEach>
        </ol>
        <hr>

        <h3>kontrolery</h3>
        <ol class="list">
            <c:forEach var="controllerDto" items="${search.controllers}"><li><c:url var="h" value="/controller/${controllerDto.id}"/>
                <a href="${h}"><c:out value="${controllerDto.name}"/></a>
            </li>
            </c:forEach>
        </ol>
        <hr>

        <h3>budynki</h3>
        <ol class="list">
            <c:forEach var="buildingDto" items="${search.buildings}"><li><c:url var="h" value="/building/${buildingDto.id}"/>
                <a href="${h}"><c:out value="${buildingDto.name}"/></a>
            </li>
            </c:forEach>
        </ol>
        <hr>

        <h3>jednostki</h3>
        <ol class="list">
            <c:forEach var="unitDto" items="${search.units}"><li>
                <a href="/unit?id=${unitDto.id}">[<c:out value="${unitDto.code}"/>] <c:out value="${unitDto.description}"/></a>
            </li>
            </c:forEach>
        </ol>
        <hr>

        <h3>użytkownicy</h3>
        <ol class="list">
            <c:forEach var="userDto" items="${search.users}"><li>
                <a href="/user?id=${userDto.id}"><c:out value="${userDto.name}"/></a>
            </li>
            </c:forEach>
        </ol>
        <hr>
    </div>
</div>

<%--<div id="all" class="container-fluid">--%>
    <%--<div id="container">--%>
        <%--<h1>Wyniki dla wyszukiwania: <mark><c:out value="${param.query}"/></mark></h1>--%>

        <%--<h3>urządzenia</h3>--%>
        <%--<ol class="list">--%>
            <%--<c:forEach var="deviceDto" items="${search.devices}"><li><c:url var="h" value="/device/${deviceDto.id}"/>--%>
                <%--<a href="${h}"><c:out value="${deviceDto.name}"/></a>--%>
            <%--</li>--%>
            <%--</c:forEach>--%>
        <%--</ol>--%>
        <%--<hr>--%>

        <%--<h3>kontrolery</h3>--%>
        <%--<ol class="list">--%>
            <%--<c:forEach var="controllerDto" items="${search.controllers}"><li><c:url var="h" value="/controller/${controllerDto.id}"/>--%>
                <%--<a href="${h}"><c:out value="${controllerDto.name}"/></a>--%>
            <%--</li>--%>
            <%--</c:forEach>--%>
        <%--</ol>--%>
        <%--<hr>--%>

        <%--<h3>budynki</h3>--%>
        <%--<ol class="list">--%>
            <%--<c:forEach var="buildingDto" items="${search.buildings}"><li><c:url var="h" value="/building/${buildingDto.id}"/>--%>
                <%--<a href="${h}"><c:out value="${buildingDto.name}"/></a>--%>
            <%--</li>--%>
            <%--</c:forEach>--%>
        <%--</ol>--%>
        <%--<hr>--%>

        <%--<h3>jednostki</h3>--%>
        <%--<ol class="list">--%>
            <%--<c:forEach var="unitDto" items="${search.units}"><li><c:url var="h" value="/unit?id=${unitDto.id}"/>--%>
                <%--<a href="${h}">[<c:out value="${unitDto.code}"/>] <c:out value="${unitDto.description}"/></a>--%>
            <%--</li>--%>
            <%--</c:forEach>--%>
        <%--</ol>--%>
        <%--<hr>--%>

        <%--<h3>użytkownicy</h3>--%>
        <%--<ol class="list">--%>
            <%--<c:forEach var="userDto" items="${search.users}"><li><c:url var="h" value="/user?id=${userDto.id}"/>--%>
                <%--<a href="${h}"><c:out value="${userDto.name}"/></a>--%>
            <%--</li>--%>
            <%--</c:forEach>--%>
        <%--</ol>--%>
        <%--<hr>--%>
    <%--</div>--%>
<%--</div>--%>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>