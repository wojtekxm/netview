<%--
This file is part of the NetView open source project
Copyright (c) 2017 NetView authors
Licensed under The MIT License
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Jednostka <c:out value="${unit.description}"/></title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/notify.css">
    <link rel="stylesheet" href="/css/tabelka.css">
    <link rel="stylesheet" href="/css/progress.css">
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
                    <div class="panel-heading">Informacje o jednostce</div>
                    <div class="panel-body">
                        <ul class="list-group" >
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-xs-4">kod</div>
                                    <div class="col-xs-8" id="field_code"></div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-xs-4">Nazwa</div>
                                    <div class="col-xs-8" id="field_description"></div>
                                </div>
                            </li>
                        </ul>
                        <div>
                            <a href="/modify-unit?id=${unit.id}" class="btn btn-success" role="button" style="float:left; margin-right: 15px;" ><span class="glyphicon glyphicon-wrench"></span> Zmień</a>
                            <form class="pull-left" method="post" action="/unit/remove/${unit.id}">
                                <button type="submit" class="btn btn-danger ">
                                    <span class="glyphicon glyphicon-trash"></span>
                                    Usuń
                                </button>
                            </form>
                    </div>
                </div>
                </div>
                <div class="panel panel-default on-loaded">
                    <div class="panel-heading" style="margin-top:40px; font-size: 15px!important;">
                        Budynki powiązane z jednostką
                    </div>
                    <div class="panel-body">
                        <div id="tabelka_buildings"></div>
                        <a href="/link-unit-all-buildings?id=${unit.id}" class="btn btn-success" role="button">
                            <span class="glyphicon glyphicon-plus"></span>
                            Połącz z budynkiem ...
                        </a>
                    </div>
                </div>
    </div>
</div>

<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/tabelka.js"></script>
<script>
"use strict";
$(document).ready(function() {
    var unitId = ${unit.id};
    var unit;

    function fixBuildings(listOfBuildingDto) {
        tabelka.builder('!')
            .searchGenerator(function(b) {
                return [b.name, b.code, b.street, b.city];
            })
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
            .buttonUnlink('usuń', function(buildingId) {
                var buildingAndUnitDto = {
                    "unitId" : unit.id,
                    "buildingId" : buildingId
                };
                return [ {
                    "url" : '/api/building/unlink-unit/',
                    "method" : 'post',
                    "postData" : buildingAndUnitDto
                }, {
                    "url" : '/api/unit/buildings/' + unit.id
                } ];
            }, function(responses) {
                fixBuildings(responses[1].list);
            })
            .build('#tabelka_buildings', listOfBuildingDto);
    }

    progress.loadGet(
        '/api/unit/details/' + unitId,
        ['.on-loading'], ['.on-loaded'], [],
        function(contentDtoOfUnitDetailsDto) {
            unit = contentDtoOfUnitDetailsDto.content.unit;
            $('#field_description').text(unit.description);
            $('#field_code').text(unit.code);
            fixBuildings(contentDtoOfUnitDetailsDto.content.buildings);
        }
    );
});
</script>
</body>
</html>
