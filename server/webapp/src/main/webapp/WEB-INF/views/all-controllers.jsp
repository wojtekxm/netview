<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Kontrolery</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link rel="stylesheet" href="/css/tabelka.css">
    <link rel="stylesheet" href="/css/style.css">
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
                        <li><a href="/unitsbuildings">Jedn. Bud.</a></li>
                    </ul>
                </li>
                <%--<li> <a href="#menu-toggle" id="menu-toggle">Filtry</a></li>--%>
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
<div class="container">
    <div style="height: 100px;"></div>
    <h4 class="pull-left">Kontrolery</h4>
    <div class="pull-right on-loaded">
        <button id="btn_examine" class="btn btn-primary pull-right" type="button">
            <span class="glyphicon glyphicon-refresh"></span>
            zbadaj wszystkie
        </button>
        <div class="pull-right" style="min-height:45px; min-width:60px">
            <span id="examine_loading"></span>
        </div>
    </div>
    <div class="on-loading"></div>
    <div class="on-loaded">
        <div id="tabelka_space"></div>
        <div>
            <a href="/create-controller" class="btn btn-success" role="button" style="width: 200px;">
                <span class="glyphicon glyphicon-plus"></span>
                Dodaj nowy kontroler
            </a>
        </div>
    </div>
    <div id="notify_layer" style="position: fixed; top: 100px;"></div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/tabelka.js"></script>
<script src="/js/notify.js"></script>
<script>
"use strict";
$(document).ready( function() {
    var controllers, columnDefinitions;
    controllers = [];
    columnDefinitions = [
        {
            "label" : 'nazwa',
            "comparator" : util.comparatorText('name'),
            "extractor" : 'td_name'
        }, {
            "label" : 'IP',
            "comparator" : util.comparatorText('ipv4'),
            "extractor" : 'td_ipv4'
        }, {
            "label" : 'opis',
            "comparator" : util.comparatorText('description'),
            "extractor" : 'td_description'
        }, {
            "label" : 'community string',
            "comparator" : util.comparatorText('communityString'),
            "extractor" : 'td_community'
        }, {
            "label" : 'liczba urządzeń',
            "comparator" : util.comparatorNumber('numberOfDevices'),
            "extractor" : 'td_devices'
        }, {
            "label" : 'lokalizacja',
            "comparator" : util.comparatorText('cmp_location'),
            "extractor" : 'td_location'
        }
    ];

    function fixControllers() {
        var i, cont, building;
        for(i = 0; i < controllers.length; i++) {
            cont = controllers[i];
            building = cont.building;

            cont.td_name = $('<a></a>')
                .attr('href', '/controller/' + cont.id)
                .text(cont.name);
            cont.td_ipv4 = $('<span></span>').text(cont.ipv4);
            cont.td_description = $('<span></span>').text(cont.description);
            cont.td_community = $('<span></span>').text(cont.communityString);
            cont.td_devices = $('<span></span>').text(cont.numberOfDevices);

            if(building === null) {
                cont.cmp_location = null;
                cont.td_location = $('<span></span>').text('-');
            }
            else {
                cont.cmp_location = building.name;
                cont.td_location = $('<a></a>')
                    .attr('href', '/building/' + building.id)
                    .text(building.name);
            }
        }
    }

    progress.load(
        'get',
        '/api/controller/details/all',
        ['.on-loading'], ['.on-loaded'], [],
        function(listDtoOfControllerDetailsDto) {
            var btnExamine, tabelkaSpace;
            btnExamine = $('#btn_examine');
            tabelkaSpace = $('#tabelka_space');
            controllers = listDtoOfControllerDetailsDto.list;
            fixControllers();
            tabelkaSpace.append(
                tabelka.create(controllers, columnDefinitions)
            );
            btnExamine.click(
                function() {
                    btnExamine.prop('disabled', true);
                    progress.loadMany(
                        [ {
                            "url" : '/api/surveys/examine/all',
                            "optionalPostData" : false
                        }, {
                            "url" : '/api/controller/details/all'
                        } ],
                        ['#examine_loading'], [], [],
                        function(responses) {
                            btnExamine.prop('disabled', false);
                            controllers = responses[1].list;
                            fixControllers();
                            tabelkaSpace.fadeOut(200, function() {
                                tabelkaSpace.empty();
                                tabelkaSpace.append(
                                    tabelka.create(controllers, columnDefinitions)
                                );
                                tabelkaSpace.fadeIn(200);
                            });
                        },
                        function() {
                            btnExamine.prop('disabled', false);
                        }
                    );
                }
            );
    } );
} );
</script>
</body>
</html>
