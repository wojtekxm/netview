<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Budynki</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link rel="stylesheet" href="/css/notify.css">
    <link rel="stylesheet" href="/css/tabelka.css">
    <link rel="stylesheet" href="/css/style.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
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

<div class="container" style="margin-top:80px">
    <div class="on-loading"></div>
    <div class="on-loaded">
        <div class="panel panel-default">
            <div class="panel-heading">
                <span class="glyphicon glyphicon-home"></span> Budynki:
            </div>
            <div class="panel-body">
                <div id="tabelka_buildings"></div>
                <div>
                    <a href="/create-building" class="btn btn-success" role="button">
                        <span class="glyphicon glyphicon-plus"></span>
                        Nowy budynek
                    </a>
                </div>
            </div>
        </div>
    </div>
    <div id="notify_layer" style="position: fixed; top: 100px;"></div>
</div>

<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/tabelka.js"></script>
<script>
"use strict";
$(document).ready( function() {
    function fixBuildings(listOfBuildingDto) {
        tabelka.builder('!')
            .column('nazwa', 'text', 'name', 7, function(building) {
                return $('<a></a>')
                    .attr('href', '/building/' + building.id)
                    .text(building.name);
            })
            .column('kod', 'text', 'code', 3, function(building) {
                return $('<span></span>').text(building.code);
            })
            .column('ulica', 'text', '_s', 3, function(b) {
                b._s = '';
                if(b.street !== null) {
                    b._s += b.street;
                }
                if(b.number !== null) {
                    b._s += ' ' + b.number;
                }
                return $('<span></span>').text(b._s);
            })
            .column('miasto', 'text', 'city', 3, function(b) {
                return $('<span></span>').text(b.city);
            })
            .build('#tabelka_buildings', listOfBuildingDto);
    }

    progress.loadGet(
        '/api/building/info/all',
        ['.on-loading'], ['.on-loaded'], [],
        function(listDtoOfBuildingDto) {
            fixBuildings(listDtoOfBuildingDto.list);
        }
    );
} );
</script>
</body>
</html>