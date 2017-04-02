<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Budynki</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/style.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" style="margin-bottom: 50px;background-color: #2e302e;">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myDiv">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <div class="navbar-brand" title="Control your network">Network Monitor</div>
        </div>

        <div class="collapse navbar-collapse" id="myDiv">
            <ul class="nav navbar-nav" style="padding-right:3px;font-size: 16px;">
                <li><a style="background-color: #1d1d1d;" href="/"><span class="glyphicon glyphicon-home"></span></a></li>
                <li style="max-height:50px;"><a href="/make-survey">Nowe badanie</a></li>
                <li><a href="/all-controllers">Kontrolery</a></li>
                <li><a href="/all-users">Użytkownicy</a></li>
                <li><a href="/all-devices">Urządzenia</a></li>
                <li><a href="/unitsbuildings">Budynki</a></li>
            </ul>
            <form class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 150px!important;">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
                </div>
            </form>
            <ul class="nav navbar-nav navbar-right" style="padding-right:3px;font-size: 16px;">
                <li><a href="/account"><span class="glyphicon glyphicon-user"></span>  Mój profil</a></li>
                <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>

<div id="container">
    <div style="height: 10px;"></div>
        <div class="panel panel-default">
            <div class="panel-heading">
                szczegóły budynku
            </div>
            <table class="table table-bordered">
                <tr>
                    <td>ID</td>
                    <td><c:out value="${building.id}"/></td>
                </tr>
                <tr>
                    <td>Kod</td>
                    <td><c:out value="${building.code}"/></td>
                </tr>
                <tr>
                    <td>Nazwa</td>
                    <td><c:out value="${building.name}"/></td>
                </tr>
                <tr>
                    <td>Szerokość geograficzna</td>
                    <td><c:out value="${building.latitude}"/></td>
                </tr>
                <tr>
                    <td>Długość geograficzna</td>
                    <td><c:out value="${building.longitude}"/></td>
                </tr>
            </table>
        </div>

        <div>
          <form method="post" action="/api/remove-building">
            <input type="hidden" name="id" value="${building.id}">
            <input type="submit" value="Usuń" class="btn btn-danger glyphicon glyphicon-plus" role="button" style="width:150px;font-size:17px;" >

          </form>
        </div>
</div>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/jquery-3.1.1.min.js"></script>
</body>
</html>