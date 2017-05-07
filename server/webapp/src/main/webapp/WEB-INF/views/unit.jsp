<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="href" value="/modify-unit?id=${id}"/>
<c:url var="action" value="/unit/remove/${id}"/>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Jednostki</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/tabelka.css">
    <link rel="stylesheet" href="/css/progress.css">
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
                    </ul>
                </li>
            </ul>
            <form method="get" action="/search" class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" name="query" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 150px!important;">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
                </div>
            </form>
            <ul class="nav navbar-nav navbar-right" style="padding-right:3px;font-size: 16px;">
                <li><a href="/settings"><span class="glyphicon glyphicon-wrench"></span>  Ustawienia</a></li>
                <li><a href="/account"><span class="glyphicon glyphicon-user"></span>  <c:out value="${loggedUser.name}"/></a></li>
                <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container" style="margin-top:100px">
    <div class="on-loading"></div>
    <div class="on-loaded">
        <div class="row">
            <%--<div class="col-md-6">--%>
                <div class="panel panel-default">
                    <div class="panel-heading">Informacje o jednostce</div>
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
                <%--</div>--%>
                    <div>
                        <a href="${href}" class="btn btn-success" role="button" style="float:left; margin-right: 10px;margin-top: 10px;" ><span class="glyphicon glyphicon-wrench"></span> Zmień</a>
                        <form class="pull-left" method="post" action="${action}">
                            <button type="submit" class="btn btn-danger " style="margin-top: 10px;">
                                <span class="glyphicon glyphicon-trash"></span>
                                Usuń
                            </button>
                        </form>

                    </div>
                </div>
                <h4 style="margin-top: 80px">Budynki powiązane z jednostką</h4>
                <div id="tabelka_buildings"></div>
                <a href="/link-unit-all-buildings?id=${id}" class="btn btn-success" role="button">
                    <span class="glyphicon glyphicon-plus"></span>
                    Połącz z budynkiem ...
                </a>
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
    var unit;

    function fixBuildings(listOfBuildingDto) {
        tabelka.builder()
            .column('nazwa', 'text', 'name', 12, function(building) {
                return $('<a></a>')
                    .attr('href', '/building/' + building.id)
                    .text(building.name);
            })
            .column('kod', 'text', 'code', 3, function(building) {
                return $('<span></span>').text(building.code);
            })
            .special('', 1, function(building) {
                building.button = $('<button class="btn btn-danger btn-xs pull-right"></button>');
                building.button.click( {
                    "buildingId" : building.id
                }, function(event) {
                    var i, buildingAndUnitDto;
                    for(i = 0; i < listOfBuildingDto.length; i++) {
                        listOfBuildingDto[i].button.prop('disabled', true);
                    }
                    buildingAndUnitDto = {
                        "unitId" : unit.id,
                        "buildingId" : event.data.buildingId
                    };
                    progress.load(
                        [ {
                            "url" : '/api/building/unlink-unit/',
                            "method" : 'post',
                            "postData" : buildingAndUnitDto
                        }, {
                            "url" : '/api/unit/buildings/' + unit.id
                        } ],
                        [], [], [], //TODO...
                        function(responses) {
                            var i, tabelkaBuildings;
                            for(i = 0; i < listOfBuildingDto.length; i++) {
                                listOfBuildingDto[i].button.prop('disabled', false);
                            }
                            fixBuildings(responses[1].list);
                        } );
                } ).append(
                    $('<span class="glyphicon glyphicon-minus"></span>')
                );
                return $('<div class="clearfix"></div>').append(
                    building.button,
                    $('<div class="progress-space-xs pull-right"></div>')
                        .attr('id', 'loading_unlink_building_' + building.id)
                );
            })
            .build('#tabelka_buildings', listOfBuildingDto);
    }

    progress.loadGet(
        '/api/unit/details/${id}',
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
