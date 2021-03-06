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
    <title>Lista urządzeń</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link rel="stylesheet" href="/css/notify.css">
    <link rel="stylesheet" href="/css/tabelka.css">
    <link rel="stylesheet" href="/css/style.css">
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
    <div class="on-loading"></div>
    <div class="panel panel-default on-loaded">
        <div class="panel-heading">
            <span class="glyphicon glyphicon-cog"></span> Urządzenia:
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
            <div id="tabelka_space"></div>
        </div>
    </div>
    <div class="panel panel-default on-loaded">
        <div class="panel-heading" style="font-size: 15px!important;">
            Usuwanie starych badań
        </div>
        <div class="panel-body">
            <div class="form-inline">
                <div class="radio">
                    <label>
                        <input type="radio" name="delete_type" value="all" checked>
                        Wszystkie (<span id="total_all">?</span>)
                    </label>
                </div>
            </div>
            <div class="form-inline">
                <div class="radio">
                    <label>
                        <input id="radio_delete_before" type="radio" name="delete_type" value="before"/>
                        Starsze niż
                    </label>
                    <div class='input-group date' id='datetimepicker1'>
                        <input id="input_calendar" type='text' class="form-control" disabled>
                        <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                    </div>
                    <span id="total_before"></span>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-offset-5 col-sm-7 clearfix">
                    <button id="btn_delete" class="btn btn-danger pull-left">
                        <span class="glyphicon glyphicon-trash"></span>
                        Usuń
                    </button>
                    <div id="delete_loading" class="pull-left progress-space"></div>
                </div>
            </div>
        </div>
    </div>
</div>


<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/moment-with-locales.min.js"></script>
<script src="/js/bootstrap-datetimepicker.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/tabelka.js"></script>
<script src="/js/notify.js"></script>
<script>
"use strict";
$(document).ready( function() {
    var $dateTimePicker, tabelkaSpace, btnDelete,
        spanTotalAll, spanTotalBefore, totalBeforeIsPending, totalBeforeChangedAgain;

    $dateTimePicker = $('#datetimepicker1');
    $dateTimePicker.datetimepicker({
        "locale": 'pl',
        "format": 'LLL'
    });
    tabelkaSpace = $('#tabelka_space');
    btnDelete = $('#btn_delete');
    spanTotalAll = $('#total_all');
    spanTotalBefore = $('#total_before');
    totalBeforeIsPending = false;
    totalBeforeChangedAgain = false;

    progress.loadParallel(
        [{
            "url" : '/api/device/details/all'
        }, {
            "url" : '/api/surveys/total/all/all'
        }],
        ['.on-loading'], ['.on-loaded'], [],
        function(responses) {
            var btnExamine;
            btnExamine = $('#btn_examine');
            fixDevices(responses[0].list, false);
            spanTotalAll.text(responses[1].content);
            refreshTotalBefore();
            btnExamine.click(function() {
                    btnExamine.prop('disabled', true);
                    progress.load(
                        [ {
                            "url" : '/api/surveys/examine/all',
                            "method" : 'post'
                        }, {
                            "url" : '/api/device/details/all'
                        }, {
                            "url" : '/api/surveys/total/all/all'
                        } ],
                        ['#examine_loading'], [], [],
                        function(responses) {
                            btnExamine.prop('disabled', false);
                            spanTotalAll.text(responses[2].content);
                            refreshTotalBefore();
                            fixDevices(responses[1].list, true);
                        },
                        function() {
                            btnExamine.prop('disabled', false);
                        }
                    );
                });
        }
    );

    btnDelete.click(function() {
        var radio, urlDelete, t;
        radio = $('input[type=radio][name=delete_type]:checked');
        if(radio.length < 1)return;
        if(radio.val() === 'all') {
            urlDelete = '/api/surveys/delete/all/all';
        }
        else {
            var t = getTimestampBeforeOrNull();
            if(t == null) {
                return;
            }
            urlDelete = '/api/surveys/delete/all/' + t;
        }
        btnDelete.prop('disabled', true);
        progress.load(
            [{
                "url" : urlDelete,
                "method" : 'post'
            }, {
                "url" : '/api/device/details/all'
            }, {
                "url" : '/api/surveys/total/all/all'
            }],
            ['#delete_loading'], [], [],
            function(responses) {
                spanTotalAll.text(responses[2].content);
                refreshTotalBefore();
                fixDevices(responses[1].list, true);
                btnDelete.prop('disabled', false);
                notify.success('Badania zostały usunięte');
            },
            function() {
                btnDelete.prop('disabled', false);
                notify.danger('Akcja się nie powiodła');
            }
        );
    });

    $dateTimePicker.on('dp.change', refreshTotalBefore);

    $('input[type=radio][name=delete_type]').change(function() {
        if( $('#radio_delete_before').prop('checked') ) {
            console.log('radio change before checked');
            $('#input_calendar').prop('disabled', false);
        }
        else {
            console.log('radio change before not checked');
            $('#input_calendar').prop('disabled', true);
        }
    });

    function fixDevices(listOfDeviceDetailsDto, replacingOld) {
        var builder = tabelka.builder('!')
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
            .column('kontroler', 'text', 'cmp_controller', 4, function(dev) {
                var cont = dev.controller;
                if(cont === null) {
                    dev.cmp_controller = '';
                    return '-';
                }
                else {
                    dev.cmp_controller = cont.name;
                    return $('<a></a>')
                        .attr('href', '/controller/' + cont.id)
                        .text(cont.name);
                }
            })
            .column('lokalizacja', 'text', 'cmp_location', 4, function(dev) {
                var b = dev.building;
                if(b === null) {
                    dev.cmp_location = '';
                    return '-';
                }
                else {
                    dev.cmp_location = b.name;
                    return $('<a></a>')
                        .attr('href', '/building/' + b.id)
                        .text(b.name);
                }
            });
        if(replacingOld) {
            tabelkaSpace.fadeOut(200, function() {
                builder.build('#tabelka_space', listOfDeviceDetailsDto);
                tabelkaSpace.fadeIn(200);
            });
        }
        else {
            builder.build('#tabelka_space', listOfDeviceDetailsDto);
        }
    }

    function refreshTotalBefore() {
        var t;
        t = getTimestampBeforeOrNull();
        if(t == null) {
            spanTotalBefore.text('');
            totalBeforeIsPending = false;
            totalBeforeChangedAgain = false;
            return;
        }
        if(totalBeforeIsPending) {
            totalBeforeChangedAgain = true;
            return;
        }
        totalBeforeIsPending = true;
        progress.loadGet(
            '/api/surveys/total/all/' + t,
            [], [], [],
            function(response) {
                spanTotalBefore.text('(' + response.content + ')');
                totalBeforeIsPending = false;
                if(totalBeforeChangedAgain) {
                    totalBeforeChangedAgain = false;
                    refreshTotalBefore();
                }
            }
        );
    }

    function getTimestampBeforeOrNull() {
        var d = $dateTimePicker.data('DateTimePicker').date();
        if(d === null) {
            console.log('$dateTimePicker.data("DateTimePicker").date() is null');
            return null;
        }
        return d.unix();
    }
} );
</script>
</body>
</html>