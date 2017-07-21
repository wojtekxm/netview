<%--
This file is part of the NetView open source project
Copyright (c) 2017 NetView authors
Licensed under The MIT License
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Ustawienia</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/progress.css">
    <link rel="stylesheet" href="/css/tabelka.css">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/notify.css">
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

<div class="container page">
    <form action="/settings" method="POST">
        <div class="panel panel-default">
            <div class="panel-heading">
                <span class="glyphicon glyphicon-wrench"></span> Ustawienia
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="examineInterval">
                                Okres badań
                                <small>sekundy</small>
                            </label>
                            <input id="examineInterval" type="number" name="examineInterval" class="form-control" value="${examineInterval}">
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="databaseCleaningInterval">
                                Okres czyszczenia bazy
                                <small>sekundy</small>
                            </label>
                            <input id="databaseCleaningInterval" type="number" name="databaseCleaningInterval" class="form-control" value="${databaseCleaningInterval}">
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="tokenAccessExpiration">
                                Maksymalny czas nieaktywności użytkownika
                                <small>minuty</small>
                            </label>
                            <input id="tokenAccessExpiration" type="number" name="tokenAccessExpiration" class="form-control" value="${tokenAccessExpiration}">
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="tokenActivateExpiraton">
                                Czas ważności tokenów aktywacji konta
                                <small>minuty</small>
                            </label>
                            <input id="tokenActivateExpiraton" type="number" name="tokenActivateExpiraton" class="form-control" value="${tokenActivateExpiraton}">
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="tokenPasswordExpiration">
                                Czas ważności tokenów do resetowania hasła
                                <small>minuty</small>
                            </label>
                            <input id="tokenPasswordExpiration" type="number" name="tokenPasswordExpiration" class="form-control" value="${tokenPasswordExpiration}">
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="serverDelay">
                                Opóźnienie API
                                <small>na potrzeby testów aplikacji, milisekundy</small>
                            </label>
                            <input id="serverDelay" type="number" name="serverDelay" class="form-control" value="${serverDelay}">
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="adminMailUsername">
                                Adres e-mail administratora
                            </label>
                            <input id="adminMailUsername" type="email" name="adminMailUsername" class="form-control" value="${adminMailUsername}">
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>
                                Hasło do konta e-mail administratora
                            </label>
                            <input type="password" class="form-control" value="" placeholder="Można ustawić tylko w pliku konfiguracyjnym" disabled>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="adminMailSmtpHost">
                                Adres SMTP serwera poczty
                            </label>
                            <input id="adminMailSmtpHost" type="text" name="adminMailSmtpHost" class="form-control" value="${adminMailSmtpHost}">
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="adminMailSmtpPort">
                                Port SMTP na serwerze poczty
                            </label>
                            <input id="adminMailSmtpPort" type="number" name="adminMailSmtpPort" class="form-control" value="${adminMailSmtpPort}">
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-12 text-center">
                        <input class="btn btn-primary" type="submit" value="Zapisz">
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/notify.js"></script>
<script>
    <c:if test="${success eq true}">
    notify.success('Zapisane', true);
    </c:if>
    <c:if test="${success eq false}">
    notify.danger('Niepowodzenie. Pole "${errorField}" zawiera błąd.');
    </c:if>
</script>
</body>
</html>