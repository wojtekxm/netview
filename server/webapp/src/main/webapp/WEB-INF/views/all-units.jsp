<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Jednostki organizacyjne</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link rel="stylesheet" href="/css/notify.css">
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
                <ul class="nav navbar-nav" style="padding-right:3px;font-size: 16px;">
                    <li><a style="background-color: black;padding-left:25px;padding-right: 20px;" href="/"><span class="glyphicon glyphicon-home"></span> &nbsp;NetView &nbsp;</a></li>
                    <c:if test="${loggedUser.role eq 'ROOT'}">  <li><a href="/all-controllers">Kontrolery</a></li>
                    <li><a href="/all-users">Użytkownicy</a></li>
                    <li><a href="/all-devices">Urządzenia</a></li>
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">Lokalizacje<span class="caret"></span></a>
                        <ul class="dropdown-menu"  style="background-color: #080b08;">
                            <li><a href="/all-buildings">Budynki</a></li>
                            <li><a href="/all-units">Jednostki</a></li></c:if>
                        </ul>
                </ul>
                </li>
            </ul>
            <c:if test="${loggedUser.role eq 'ROOT'}">  <form method="get" action="/search" class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" name="query" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 150px!important;">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
                </div>
            </form></c:if>
            <ul class="nav navbar-nav navbar-right" style="padding-right:3px;font-size: 16px;">
                <c:if test="${loggedUser.role eq 'ROOT'}">  <li><a href="/settings"><span class="glyphicon glyphicon-wrench"></span>  Ustawienia</a></li></c:if>
                <li><a href="/account"><span class="glyphicon glyphicon-user"></span>  <c:out value="${loggedUser.name}"/></a></li>
                <li><a href="/logout" style="margin-right: 10px;"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <div style="height: 80px;"></div>
    <div class="panel panel-default" id="header" style="margin-bottom: 15px!important;">
        <div class="panel-body">
            <div style="font-size: 17px; display: inline-block;"><span class="glyphicon glyphicon-home"></span> Jednostki organizacyjne:</div>
        </div>
    </div>
    <div id="main_loading" class="later"></div>
    <div id="main_success" class="later">
        <div id="tabelka_space"></div>
        <div>
            <a href="/create-unit" class="btn btn-success" role="button" style="margin-bottom:20px;">
                <span class="glyphicon glyphicon-plus"></span>
                Dodaj nową jednostkę organizacyjną
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
$(document).ready( function() {
    var units, columnDefinitions;
    units = [];
    columnDefinitions = [
        {
            "label" : 'nazwa',
            "comparator" : util.comparatorText('description'),
            "extractor" : 'td_description',
            "cssClass" : 'width-12'
        }, {
            "label" : 'kod',
            "comparator" : util.comparatorText('code'),
            "extractor" : 'td_code',
            "cssClass" : 'width-4'
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
        }
    }

    progress.loadGet(
        '/api/unit/all',
        ['#main_loading'], ['#main_success'], [],
        function(listDtoOfUnitDto) {
            units = listDtoOfUnitDto.list;
            fixUnits();
            $('#tabelka_space').append(
                tabelka.create(units, columnDefinitions)
            );
        }
    );
} );
</script>
</body>
</html>