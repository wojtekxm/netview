<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Jednostki</title>
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
            <li class="active"><a href="/buildings">budynki</a></li>
            <li><a href="/logout">wyloguj</a></li>
        </ul>
    </div>
</nav>

<div class="container">
    <div class="form-inline">
        <div class="form-group" style="margin-bottom: 10px;">
        <form method="post" action="/api/remove-unit">
            <input type="text" class="form-control" placeholder="Id jednostki"
                   required="required" name="id" style=" width: 30%;">

            <input type="submit" value="Usun jednostkę" class="btn btn-primary btn-default btn-lg active">

        </form>
    </div>
        <div class="form-group" style="margin-bottom: 10px;">
            <a href="/create-unit" type="submit" class=" btn btn-primary btn-default btn-lg active" >Dodaj jednostkę</a>

        </div>
    <div class="list-group ">
        <div class="row list-group-item list-group-item-heading list-group-item-success">
            <div class="col-md-1">Id</div>
            <div class="col-md-1">Kod</div>
            <div class="col-md-3">Opis</div>


        </div>

        <c:forEach var="u" items="${list}">
            <div class="row list-group-item list-group-item-info">
                <div class="col-md-1"><c:out value="${u.id}"/></div>
                <div class="col-md-1"><c:out value="${u.code}"/></div>
                <div class="col-md-3"><c:out value="${u.description}"/></div>

            </div>
        </c:forEach>
    </div>
</div>

<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/jquery-3.1.1.min.js"></script>

</body>
</html>