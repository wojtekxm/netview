<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="actionRemove" value="/controller/remove/${controller.id}"/>
<c:url var="hrefModify" value="/modify-controller?id=${controller.id}"/>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Kontroler</title>
    <link rel="icon" href="/favicon.ico">
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
                <li><a href="/account"><span class="glyphicon glyphicon-user"></span>  &nbsp;<c:out value="${loggedUser.name}"/></a></li>
                <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div style="height: 80px;"></div>
    <div class="panel panel-default">
        <div class="panel-body">
            <div id="tittle"><span class="glyphicon glyphicon-arrow-right"></span> Informacje o kontrolerze: </div>
        </div>
    </div>

    <div style="height: 10px;"></div>
    <div class="panel panel-default" id="content"><div></div>
        <div class="panel-heading" style="width: 100%;background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0;">
            Szczegóły urządzenia:
        </div>

        <table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0; margin-bottom: inherit">
            <tr>
                <td>Nazwa</td>
                <td><c:out value="${controller.name}"/></td>
            </tr>
            <tr>
                <td>IP</td>
                <td><c:out value="${controller.ipv4}"/></td>
            </tr>
            <tr>
                <td>Opis</td>
                <td><c:out value="${controller.description}"/></td>
            </tr>
            <tr>
                <td>Community String</td>
                <td><c:out value="${controller.communityString}"/></td>
            </tr>


            <c:if test="${ controller.building != null && controller.building.name != ''}">

               <tr>
                    <td>Budynek</td>
                    <td><a href="/building/${controller.building.id}"><c:out value="${controller.building.name}"/></a> </td>
                </tr>
            </c:if>
            <c:if test="${ controller.fake == false}">
            <tr>
                <td>Rodzaj kontrolera </td>
                <td>Prawdziwy</td>
            </tr>
            </c:if>
            <c:if test="${ controller.fake == true}">
                <tr>
                    <td>Rodzaj kontrolera </td>
                    <td>Sztuczny</td>
                </tr>
            </c:if>

        </table>
        <div class="clearfix">
            <form class="pull-left" method="POST" action="${actionRemove}">
                <button class="btn btn-danger" type="submit" style="margin-right: 10px;">
                    <span class="glyphicon glyphicon-trash"></span> Usuń
                </button>
            </form>
            <a href="${hrefModify}" class="btn btn-success pull-left" role="button">
                <span class="glyphicon glyphicon-wrench"></span> Zmień
            </a>
        </div>
    </div>
</div>

<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/progress.js"></script>

</body>
</html>