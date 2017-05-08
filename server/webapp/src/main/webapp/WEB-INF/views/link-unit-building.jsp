<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Budynki</title>
    <link rel="icon" href="/favicon.ico">
    <script src="/js/jquery-3.1.1.min.js"></script>
    <script src="/js/bootstrap-3.3.7.min.js"></script>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
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
                <li><a style="background-color: black;" href="/"><span class="glyphicon glyphicon-home"></span> &nbsp;Network Monitor</a></li>
                <li><a href="/all-controllers">Kontrolery</a></li>
                <li><a href="/all-users">Użytkownicy</a></li>
                <li><a href="/all-devices">Urządzenia</a></li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">Lokalizacje<span class="caret"></span></a>
                    <ul class="dropdown-menu"  style="background-color: #080b08;">
                        <li><a href="/all-buildings">Budynki</a></li>
                        <li><a href="/all-units">Jednostki</a></li>
                    </ul>
                </li>
            </ul>
            <form method="get" action="/search" class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" name="query" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 150px!important;">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
                </div>
            </form>
            <ul class="nav navbar-nav navbar-right" style="padding-right:3px;font-size: 16px;">
                <li><a href="/settings"><span class="glyphicon glyphicon-wrench"></span>  Ustawienia</a></li>
                <li><a href="/account"><span class="glyphicon glyphicon-user"></span>  <c:out value="${loggedUser.name}"/></a></li>
                <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div style="height: 80px;"></div>
    <div class="panel panel-default" id="content">

        <div class="list-group ">
            <div class="row list-group-item list-group-item-heading list-group-item-success">
                <div class="col-md-1">Id</div>
                <div class="col-md-1">Id - Budynki</div>
                <div class="col-md-1">Kod</div>
                <div class="col-md-3">Nazwa</div>
                <div class="col-md-1">Szer. Geo</div>
                <div class="col-md-1">Dł. Geo</div>
                <div class="col-md-1">Id - Jednostki</div>
                <div class="col-md-1">Kod</div>
                <div class="col-md-2">Opis</div>


            </div>

            <c:forEach var="lub" items="${list}">
                <div class="row list-group-item list-group-item-info">
                    <div class="col-md-1"><c:out value="${lub.id}"/></div>
                    <div class="col-md-1"><c:out value="${lub.building.id}"/></div>
                    <div class="col-md-1"><c:out value="${lub.building.code}"/></div>
                    <div class="col-md-3"><c:out value="${lub.building.name}"/></div>
                    <div class="col-md-1"><c:out value="${lub.building.latitude}"/></div>
                    <div class="col-md-1"><c:out value="${lub.building.longitude}"/></div>

                    <div class="col-md-1"><c:out value="${lub.unit.id}"/></div>
                    <div class="col-md-1"><c:out value="${lub.unit.code}"/></div>
                    <div class="col-md-2"><c:out value="${lub.unit.description}"/></div>

                </div>
            </c:forEach>
        </div>

    </div>
</div>

<%--<div id="all" class="container-fluid">--%>
    <%--<div id="container">--%>
        <%--<div class="list-group ">--%>
            <%--<div class="row list-group-item list-group-item-heading list-group-item-success">--%>
                <%--<div class="col-md-1">Id</div>--%>
                <%--<div class="col-md-1">Id - Budynki</div>--%>
                <%--<div class="col-md-1">Kod</div>--%>
                <%--<div class="col-md-3">Nazwa</div>--%>
                <%--<div class="col-md-1">Szer. Geo</div>--%>
                <%--<div class="col-md-1">Dł. Geo</div>--%>
                <%--<div class="col-md-1">Id - Jednostki</div>--%>
                <%--<div class="col-md-1">Kod</div>--%>
                <%--<div class="col-md-2">Opis</div>--%>


            <%--</div>--%>

            <%--<c:forEach var="lub" items="${list}">--%>
            <%--<div class="row list-group-item list-group-item-info">--%>
                <%--<div class="col-md-1"><c:out value="${lub.id}"/></div>--%>
                <%--<div class="col-md-1"><c:out value="${lub.building.id}"/></div>--%>
                <%--<div class="col-md-1"><c:out value="${lub.building.code}"/></div>--%>
                <%--<div class="col-md-3"><c:out value="${lub.building.name}"/></div>--%>
                <%--<div class="col-md-1"><c:out value="${lub.building.latitude}"/></div>--%>
                <%--<div class="col-md-1"><c:out value="${lub.building.longitude}"/></div>--%>

                <%--<div class="col-md-1"><c:out value="${lub.unit.id}"/></div>--%>
                <%--<div class="col-md-1"><c:out value="${lub.unit.code}"/></div>--%>
                <%--<div class="col-md-2"><c:out value="${lub.unit.description}"/></div>--%>

            <%--</div>--%>
            <%--</c:forEach>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>

<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>

</body>
</html>