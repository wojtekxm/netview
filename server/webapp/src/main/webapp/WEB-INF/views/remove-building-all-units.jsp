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
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <link rel="icon" href="/favicon.ico">
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
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">Lokalizacje<span class="caret"></span></a>
                    <ul class="dropdown-menu"  style="background-color: #080b08;">
                        <li><a href="/all-buildings">Budynki</a></li>
                        <li><a href="/all-units">Jednostki</a></li>
                        <li><a href="/unitsbuildings">Jedn. Bud.</a></li>
                    </ul>
                </li>
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

<div id="all" class="container-fluid">
    <div id="container">
        <div class="content">
            <div>
                <div id="wydzial"><div style="border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;"><span class="glyphicon glyphicon-th-list"></span> Kliknij jednostkę, z którą chcesz usunąć powiązanie :</div></div>
            </div>

            <div id="devices" class="panel panel-default" style="padding: 15px;">
                <div class="panel-heading" style="background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0;">
                    Powiązane jednostki:
                </div>

                <table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0;">

                    <tr>
                        <td>ID</td>
                        <td>Kod</td>
                        <td>Opis</td>
                    </tr>
                    <c:forEach items="${units}" var="unit">
                        <tr onclick="window.document.location='remove-building-units?id_building=${building.id}&id_unit=${unit.id}';">


                            <td> <c:out value="${unit.id}"/></td>
                            <td> <c:out value="${unit.code}"/></td>
                            <td> <c:out value="${unit.description}"/></td>


                        </tr>
                    </c:forEach>

                </table>


            </div>



        </div>
    </div>
</div>


<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>
