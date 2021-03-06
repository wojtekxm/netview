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
    <title>Kontroler <c:out value="${controller.name}"/></title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/notify.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link rel="stylesheet" href="/css/tabelka.css">
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
    <div class="panel panel-default">
        <div class="panel-heading">
            <span class="glyphicon glyphicon-arrow-right"></span> Informacje o kontrolerze:
        </div>
        <div class="panel-body">
            <div class="panel-heading" style="width: 100%;background-color: #fcfcfc; padding: 8px;font-size: 16px;border: 1px solid #e0e0e0;">
                Szczegóły kontrolera:
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
            <div class="clearfix" style="margin-top: 20px;">
                <a href="/modify-controller?id=${controller.id}" class="btn btn-success pull-left" role="button">
                    <span class="glyphicon glyphicon-wrench"></span> Zmień
                </a>
                <form class="pull-left" method="POST" action="/controller/remove/${controller.id}">
                    <button class="btn btn-danger" type="submit" style="margin-left: 15px;">
                        <span class="glyphicon glyphicon-trash"></span> Usuń
                    </button>
                </form>
            </div>

            <div class="on-loading progress-space-lg"></div>
            <div id="tabelka_devices" class="on-loaded" style="margin-top:30px;"></div>
        </div>
    </div>
    <div id="notify_layer" style="position: fixed; top: 100px;"></div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/tabelka.js"></script>
<script>
//TODO wczytywać podstawową tabelkę przez API
"use strict";
$(document).ready(function() {
    function fixDevices(listOfDeviceDetailsDto) {
        tabelka.builder('!')
            .searchGenerator(function(d) {
                return [d.name];
            })
            .column('nazwa', 'text', 'name', 4, function(dev) {
                return $('<a></a>')
                    .attr('href', '/device/' + dev.id)
                    .text(dev.name);
            })
            .deviceFrequency('2,4 GHz', 2400, 2)
            .deviceFrequency('5 GHz', 5000, 2)
            .column('lokalizacja', 'text', 'cmp_location', 8, function(dev) {
                var b = dev.building;
                if(b === null) {
                    dev.cmp_location = '';
                    return $('<span></span>').text('-');
                }
                else {
                    dev.cmp_location = b.name;
                    return $('<a></a>')
                        .attr('href', '/building/' + b.id)
                        .text(b.name);
                }
            })
            .buttonUnlink('usuń', function(deviceId) {
                return [ {
                    "url" : '/api/device/unlink-controller/' + deviceId,
                    "method" : 'post'
                }, {
                    "url" : '/api/controller/devices-details/' + ${controller.id}
                } ];
            }, function(responses) {
                fixDevices(responses[1].list);
            })
            .build('#tabelka_devices', listOfDeviceDetailsDto);
    }

    progress.loadGet(
        '/api/controller/devices-details/' + ${controller.id},
        ['.on-loading'], ['.on-loaded'], [],
        function(listDtoOfDeviceDetailsDto) {
            fixDevices(listDtoOfDeviceDetailsDto.list);
        },
        undefined,
        'md'
    );
});
</script>
</body>
</html>