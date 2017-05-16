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
                <span class="glyphicon glyphicon-inbox"></span> Kontrolery:
            </div>
            <div class="panel-body">
                <div>
                    <div class="pull-left on-loaded" style="margin-bottom: 15px;">
                        <button id="btn_examine" class="btn btn-primary pull-left" type="button">
                            <span class="glyphicon glyphicon-refresh"></span>
                            zbadaj wszystkie
                        </button>
                        <div class = "pull-right" style="height: 35px; margin:0px!important;">
                            <div id="examine_loading" class="progress-space"></div>
                        </div>
                    </div>
                </div>
                <div>
                    <div id="tabelka_space"></div>
                    <div>
                        <a href="/create-controller" class="btn btn-success" role="button" style="width: 200px;margin-bottom:5px;">
                            <span class="glyphicon glyphicon-plus"></span>
                            Nowy kontroler
                        </a>
                    </div>
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
<script src="/js/notify.js"></script>
<script>
"use strict";
$(document).ready( function() {
    var tabelkaSpace, btnExamine;
    tabelkaSpace = $('#tabelka_space');
    btnExamine = $('#btn_examine');

    progress.loadGet(
        '/api/controller/details/all',
        ['.on-loading'], ['.on-loaded'], [],
        function(listDtoOfControllerDetailsDto) {
            fixControllers(listDtoOfControllerDetailsDto.list, false);
            btnExamine.click(function() {
                    btnExamine.prop('disabled', true);
                    progress.load(
                        [ {
                            "url" : '/api/surveys/examine/all',
                            "method" : 'post'
                        }, {
                            "url" : '/api/controller/details/all'
                        } ],
                        ['#examine_loading'], [], [],
                        function(responses) {
                            btnExamine.prop('disabled', false);
                            fixControllers(responses[1].list, true);
                        },
                        function() {
                            btnExamine.prop('disabled', false);
                        }
                    );
            });
    } );

    function fixControllers(listOfControllerDetailsDto, replacingOld) {
        var builder = tabelka.builder('!')
            .column('nazwa', 'text', 'name', 2, function(cont) {
                return $('<a></a>')
                    .attr('href', '/controller/' + cont.id)
                    .text(cont.name);
            })
            .column('opis', 'text', 'description', 2, 'description')
            .column('IP', 'text', 'ipv4', 2, function(cont) {
                return $('<samp></samp>').text(cont.ipv4);
            })
            .column('community', 'text', 'communityString', 2, function(cont) {
                return $('<samp></samp>').text(cont.communityString);
            })
            .column('prawdziwy', 'number', 'cmp_fake', 2, function(cont) {
                if(cont.fake) {
                    cont.cmp_fake = 1;
                    return $('<span></span>').append(
                        $('<span class="glyphicon glyphicon-unchecked" style="color:#d9534f"></span>'),
                        ' nie'
                    );
                }
                else {
                    cont.cmp_fake = 0;
                    return $('<span></span>').append(
                        $('<span class="glyphicon glyphicon-check" style="color:#5cb85c"></span>'),
                        ' tak'
                    );
                }
            })
            .column('urządzenia', 'number', 'numberOfDevices', 2, 'numberOfDevices')
            .column('lokalizacja', 'text', 'cmp_location', 4, function(cont) {
                var b = cont.building;
                if(b === null) {
                    cont.cmp_location = '';
                    return '-';
                }
                else {
                    cont.cmp_location = b.name;
                    return $('<a></a>')
                        .attr('href', '/building/' + b.id)
                        .text(b.name);
                }
            });
        if(replacingOld) {
            tabelkaSpace.fadeOut(200, function() {
                builder.build('#tabelka_space', listOfControllerDetailsDto);
                tabelkaSpace.fadeIn(200);
            });
        }
        else {
            builder.build('#tabelka_space', listOfControllerDetailsDto);
        }
    }
} );
</script>
</body>
</html>
