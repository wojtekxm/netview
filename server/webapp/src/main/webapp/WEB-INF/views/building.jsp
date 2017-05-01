<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="href" value="/modify-building?id=${id}"/>
<c:url var="action" value="/building/remove/${id}"/>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Informacje o budynku</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
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
<div class="container" style="margin-top:100px">
    <div id="main_progress">
        <div class="progress-loading"></div>
        <div class="progress-success">
            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Informacje o budynku</div>
                        <ul class="list-group">
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-xs-4">nazwa</div>
                                    <div class="col-xs-8" id="field_name"></div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-xs-4">kod</div>
                                    <div class="col-xs-8" id="field_code"></div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-xs-4">adres</div>
                                    <div class="col-xs-8" id="field_address"></div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <a href="${href}" class="btn btn-success pull-left" role="button">
                        <span class="glyphicon glyphicon-wrench"></span>
                        Zmień
                    </a>
                    <form class="pull-right" method="post" action="${action}">
                        <button type="submit" class="btn btn-danger pull-right" style="margin-bottom: 10px">
                            <span class="glyphicon glyphicon-trash"></span>
                            Usuń
                        </button>
                    </form>
                </div>
                <div class="col-md-6">
                    <div id="map" style="width: 100%; height: 300px; box-shadow: 0 0 10px -2px black"></div>
                </div>
            </div>
            <h4 style="margin-top: 50px">Jednostki organizacyjne powiązane z budynkiem</h4>
            <div id="tabelka_units"></div>
            <a href="/link-building-all-units?id=${id}" class="btn btn-success" role="button">
                <span class="glyphicon glyphicon-plus"></span>
                Połącz z jednostką organizacyjną...
            </a>
            <h4 style="margin-top: 50px">Kontrolery znajdujące się w budynku</h4>
            <div id="tabelka_controllers"></div>
        </div>
        <div class="progress-error">err</div>
    </div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/tabelka.js"></script>
<script>
var building, units, controllers, unitDefinitions, controllerDefinitions;
unitDefinitions = [
    {
        "label" : 'nazwa',
        "comparator" : util.comparatorText('description'),
        "extractor" : 'td_description'
    }, {
        "label" : 'kod',
        "comparator" : util.comparatorText('code'),
        "extractor" : 'td_code'
    }, {
        "label" : '',
        "comparator" : null,
        "extractor" : 'td_button',
        "optionalCssClass" : 'width1'
    }
];
function fixUnits() {
    var i, u;
    for(i = 0; i < units.length; i++) {
        u = units[i];
        u.td_description = $('<a></a>')
            .attr('href', '/unit/' + u.id)
            .text(u.description);
        u.td_code = $('<span></span>').text(u.code);
        u.td_button = $('<button class="btn btn-danger btn-xs"></button>')
            .click( {
                "unitId" : u.id
            }, function(event) {
                var i, buildingAndUnitDto;
                for(i = 0; i < units.length; i++) {
                    units[i].td_button.prop('disabled', true);
                }
                buildingAndUnitDto = {
                    "buildingId" : building.id,
                    "unitId" : event.data.unitId
                };
                progress.loadMany(
                    [ {
                        "url" : '/api/building/unlink-unit/',
                        "optionalPostData" : buildingAndUnitDto
                    }, {
                        "url" : '/api/building/units/' + building.id
                    } ],
                    '#main_progress',
                    function(responses) {
                        var i, tabelkaUnits;
                        for(i = 0; i < units.length; i++) {
                            units[i].td_button.prop('disabled', false);
                        }
                        units = responses[1].list;
                        fixUnits();
                        tabelkaUnits = $('#tabelka_units');
                        tabelkaUnits.empty();
                        tabelkaUnits.append(tabelka.create(units, unitDefinitions));
                    } );
            } ).append(
                $('<span class="glyphicon glyphicon-minus"></span>')
            );
    }
}
controllerDefinitions = [
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
    }
];
function fixControllers() {
    var i, cont;
    for(i = 0; i < controllers.length; i++) {
        cont = controllers[i];
        cont.td_name = $('<a></a>')
            .attr('href', '/controller/' + cont.id)
            .text(cont.name);
        cont.td_ipv4 = $('<span></span>').text(cont.ipv4);
        cont.td_description = $('<span></span>').text(cont.description);
        cont.td_community = $('<span></span>').text(cont.communityString);
    }
}

progress.load(
    'get',
    '/api/building/details/${id}',
    '#main_progress',
    function(contentDtoOfBuildingDetailsDto) {
        var fieldName, fieldCode, tabelkaUnits, tabelkaControllers;
        building = contentDtoOfBuildingDetailsDto.content.building;
        units = contentDtoOfBuildingDetailsDto.content.units;
        controllers = contentDtoOfBuildingDetailsDto.content.controllers;
        fieldName = $('#field_name');
        fieldName.text(building.name);
        fieldCode = $('#field_code');
        fieldCode.text(building.code);
        fieldAddress = $('#field_address');
        fieldAddress.append(
            building.street + ' ' + building.number,
            $('<br>'),
            building.postalCode + ' ' + building.city);
        fixUnits();
        tabelkaUnits = $('#tabelka_units');
        tabelkaUnits.append(tabelka.create(units, unitDefinitions));
        fixControllers();
        tabelkaControllers = $('#tabelka_controllers');
        tabelkaControllers.append(tabelka.create(controllers, controllerDefinitions));
        $('body').append(
            $('<script/>', {
                "src" : 'https://maps.googleapis.com/maps/api/js?key=AIzaSyCR0onCdpLDKtCPdd1h9Fpc3ENsrhPj_Q0&callback=initMap',
                "async" : 'async',
                "defer" : 'defer'
            })
        );
    }
);
function initMap() {
    var place = {
        lat: building.latitude,
        lng: building.longitude
    };
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 14,
        center: place
    });
    var marker = new google.maps.Marker({
        position: place,
        map: map
    });
}
</script>
</body>
</html>
