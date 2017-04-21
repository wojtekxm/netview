<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Modyfikuj kontroler</title>
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
            <form method="get" action="/search" class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" name="query" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 250px!important;">
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
            <div style="height: 10px;"></div>
            <div>
                <div id="wydzial"><div style="width: 100%;border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;"><span class="glyphicon glyphicon-arrow-right"></span> <c:out value="${controller.name}"/>: </div></div>
                <form method="post" action="/api/controller/accept-modify-controller" id="form1"></form>
            </div>

            <div id="devices" class="panel panel-default" style="padding: 15px;">
                <div class="panel-heading" style="background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0;">
                    Modyfikuj kontroler:
                </div>

                <table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0;">
                    <tr>
                        <input form="form1" type="hidden" name="id" value="${controller.id}" />
                    </tr>

                    <tr>
                        <td>Nazwa</td>
                        <td><input form="form1" type="text" name="name" value="${controller.name}" style="width: 30%;" />
                    </tr>

                    <tr>
                        <td>IP</td>
                        <td><input form="form1" type="text" name="ipv4" value="${controller.ipv4}" style="width: 30%;" />
                    </tr>

                    <tr>
                        <td>Opis</td>
                        <td><input form="form1" type="text" name="description" value="${controller.description}" style="width: 30%;" />
                    </tr>
                    <tr>
                        <td>Community String</td>
                        <td><input form="form1" type="text" name="communityString" value="${controller.communityString}" style="width: 30%;" />
                    </tr>


                    <label for="sel1">Wybierz budynek:</label>

                    <select form="form1" class="form-control" id="sel1"name="buildingId">
                        <option></option>
                        <c:forEach items="${list}" var="building" >
                            <option value="${building.id}" >
                                <c:out value="${building.name}"/>
                            </option>
                        </c:forEach>
                    </select>
                </table>
                <div>
                    <a href="/controller/?id=${controller.id}" class="btn btn-info" role="button" style="float:left;width:180px;font-size:17px;" ><span class="glyphicon glyphicon-backward"></span> Powrót</a>
                    <span style="display: flex;position: relative;float: left;">
                        <span class="glyphicon glyphicon-ok" style="position: absolute;font-size:17px;color: white;top: 30%;left:20%;"></span>
                        <input form="form1" type="submit" value="Zatwierdź" class="btn btn-success" role="button" style="float:left;width:180px;font-size:17px;" >
                    </span>

                </div>
            </div>

        </div>
    </div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>