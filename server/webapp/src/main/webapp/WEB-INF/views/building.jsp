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
                <li><a style="background-color: black;" href="/"><span class="glyphicon glyphicon-home"></span></a></li>
                <li style="max-height:50px;"><a href="/make-survey">Nowe badanie</a></li>
                <li><a href="/all-controllers">Kontrolery</a></li>
                <li><a href="/all-users">Użytkownicy</a></li>
                <li><a href="/all-devices">Urządzenia</a></li>
                <li><a href="/unitsbuildings">Budynki</a></li>
            </ul>
            <form method="get" action="/search" class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" name="query" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 150px!important;">
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
                <div id="wydzial"><div style="border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;"><span class="glyphicon glyphicon-arrow-right"></span> Informacje o budynku: </div></div>
            </div>
            <div id="devices" class="panel panel-default" style="padding: 15px;">
                <div class="panel-heading" style="background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0;">
                    Szczegóły budynku:
                </div>
                <c:url var="href" value="/modify-building?id=${building.id}"/>
                <table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0;">
                    <tr>
                        <td>Nazwa</td>
                        <td><c:out value="${building.name}"/></td>
                    </tr>
                    <tr>
                        <td>ID</td>
                        <td><c:out value="${building.id}"/></td>
                    </tr>
                    <tr>
                        <td>Kod</td>
                        <td><c:out value="${building.code}"/></td>
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
                <div>
                    <form method="post" action="/api/remove-building">
                            <span style="display: flex;position: relative;float: right;">
                            <span class="glyphicon glyphicon-trash" style="position: absolute;font-size:17px;color: white;top: 30%;left:35%;"></span>
                                <input type="hidden" name="id" value="${building.id}">
                                <input type="submit" value="Usuń" class="form-control btn btn-danger" role="button" style="float:right;height:38px;width:250px;font-size:17px;" >
                            </span>
                    </form>
                    <a href="${href}" class="btn btn-success" role="button" style="float:right;width:250px;font-size:17px;" ><span class="glyphicon glyphicon-wrench"></span> Modyfikuj</a>
                </div>

                <div class="panel-heading" style="margin-top: 70px;background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0;">
                    Powiązane jednostki:
                </div>

                <table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0;">

                    <tr>
                        <td>ID</td>
                        <td>Kod</td>
                        <td>Opis</td>
                    </tr>
                    <c:forEach items="${units}" var="unit">
                        <tr>

                            <td><c:out value="${unit.id}"/></td>
                            <td><c:out value="${unit.code}"/></td>
                            <td> <c:out value="${unit.description}"/></td>

                        </tr>
                    </c:forEach>

                </table>

                <div>

                    <a href="/remove-building-all-units?id=${building.id}" class="btn btn-danger" role="button" style="float:right;width:250px;font-size:17px;" ><span class="glyphicon glyphicon-trash"></span> Usuń powiązanie</a></div>
                <a href="/link-building-all-units?id=${building.id}" class="btn btn-success" role="button" style="float:right;width:250px;font-size:17px;"><span class="glyphicon glyphicon-plus"></span> Dodaj powiązanie</a>

            </div>

            <%--<div id="devices" class="panel panel-default" style="padding: 15px;">--%>
                <%--<div class="panel-heading" style="background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0;">--%>
                    <%--Powiązane jednostki:--%>
                <%--</div>--%>

                <%--<table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0;">--%>

                    <%--<tr>--%>
                        <%--<td>ID</td>--%>
                        <%--<td>Kod</td>--%>
                        <%--<td>Opis</td>--%>
                    <%--</tr>--%>
                    <%--<c:forEach items="${units}" var="unit">--%>
                        <%--<tr>--%>

                            <%--<td><c:out value="${unit.id}"/></td>--%>
                            <%--<td><c:out value="${unit.code}"/></td>--%>
                            <%--<td> <c:out value="${unit.description}"/></td>--%>

                        <%--</tr>--%>
                    <%--</c:forEach>--%>

                <%--</table>--%>

                <%--<div>--%>

                    <%--<a href="/remove-building-all-units?id=${building.id}" class="btn btn-danger" role="button" style="float:right;width:150px;font-size:17px;" >Usuń powiązanie</a></div>--%>
                <%--<a href="/link-building-all-units?id=${building.id}" class="btn btn-success" role="button" style="float:right;width:150px;font-size:17px;">Dodaj powiązanie</a>--%>

            <%--</div>--%>

        </div>
        </div>
    </div>

<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>

</body>
</html>
