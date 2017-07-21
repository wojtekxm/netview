<%--
This file is part of the NetView open source project
Copyright (c) 2017 NetView authors
Licensed under The MIT License
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Usuń powiązanie</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/notify.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/cookieconsent.min.css" media="screen">
    <script src="/js/cookieconsent.min.js"></script>
    <script>
        window.addEventListener("load", function(){
            window.cookieconsent.initialise({
                "palette": {
                    "popup": {
                        "background": "#3c404d",
                        "text": "#d6d6d6"
                    },
                    "button": {
                        "background": "#8bed4f"
                    }
                },
                "theme": "edgeless",
                "content": {
                    "message": "Ta strona wykorzystuje pliki cookies. Korzystanie z witryny oznacza zgodę na ich zapis lub odczyt wg ustawień przeglądarki.",
                    "dismiss": "OK",
                    "link": "O polityce cookies",
                    "href": "wszystkoociasteczkach.pl/polityka-cookies/"
                }
            })});
    </script>
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
                                <li><a href="/all-units">Jednostki</a></li>
                            </ul>
                        </li>
                    </c:if>
                </ul>
            </ul>
            <c:if test="${loggedUser.role eq 'ROOT'}">  <form method="get" action="/search" class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" name="query" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 150px!important;">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
                </div>
            </form></c:if>
            <ul class="nav navbar-nav navbar-right" style="padding-right:3px;font-size: 16px;">
                <c:if test="${loggedUser.role eq 'ROOT'}">  <li style="margin-left: 0px!important;"><a href="/settings"><span class="glyphicon glyphicon-wrench"></span>  Ustawienia</a></li></c:if>
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
            <div id="tittle"><span class="glyphicon glyphicon-home"></span> Kliknij jednostkę, z którą chcesz usunąć powiązanie :</div>
        </div>
    </div>

    <div class="panel panel-default" id="content">

        <div class="panel-heading" style="background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0;">
            Powiązane jednostki:
        </div>

        <table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0;">

            <tr>
                <td>Kod</td>
                <td>Opis</td>
            </tr>

            <c:forEach items="${units}" var="unit">
                <tr onclick="window.document.location='remove-building-units?id_building=${building.id}&id_unit=${unit.id}';">
                    <td> <c:out value="${unit.code}"/></td>
                    <td> <c:out value="${unit.description}"/></td>
                </tr>
            </c:forEach>
        </table>

    </div>
</div>
<%--<div id="all" class="container-fluid">--%>
    <%--<div id="container">--%>
        <%--<div class="content">--%>
            <%--<div>--%>
                <%--<div id="wydzial"><div style="width: 100%;border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;"><span class="glyphicon glyphicon-th-list"></span> Kliknij jednostkę, z którą chcesz usunąć powiązanie :</div></div>--%>
            <%--</div>--%>

            <%--<div id="devices" class="panel panel-default" style="padding: 15px;">--%>
                <%--<div class="panel-heading" style="background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0;">--%>
                    <%--Powiązane jednostki:--%>
                <%--</div>--%>

                <%--<table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0;">--%>

                    <%--<tr>--%>
                        <%--<td>Kod</td>--%>
                        <%--<td>Opis</td>--%>
                    <%--</tr>--%>

                    <%--<c:forEach items="${units}" var="unit">--%>
                    <%--<tr onclick="window.document.location='remove-building-units?id_building=${building.id}&id_unit=${unit.id}';">--%>
                         <%--<td> <c:out value="${unit.code}"/></td>--%>
                         <%--<td> <c:out value="${unit.description}"/></td>--%>
                    <%--</tr>--%>
                    <%--</c:forEach>--%>
                <%--</table>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>


<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>
