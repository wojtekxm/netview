<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="lub" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Budynki</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/style.css">
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
            <li class="active"><a href="/all-controllers">budynki</a></li>
            <li><a href="/logout">wyloguj</a></li>
        </ul>
    </div>
</nav>

<div class="container">
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

        <c:forEach var="lub" items="list">
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

<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/jquery-3.1.1.min.js"></script>

</body>
</html>