<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Lista urządzeń</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/progress.css">
    <link rel="stylesheet" href="/css/style.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <link rel="icon" href="/favicon.ico">
</head>
<body>

<nav class="navbar navbar-inverse navbar-fixed-top" style="margin-bottom: 50px;background-color: #2e302e;">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myDiv">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <div class="navbar-brand" title="Control your network">Network Monitor</div>
        </div>

        <div class="collapse navbar-collapse" id="myDiv">
            <ul class="nav navbar-nav" style="padding-right:3px;font-size: 16px;">
                <li><a style="background-color: black;" href="/"><span class="glyphicon glyphicon-home"></span></a></li>
                <li style="max-height:50px;"><a href="/make-survey">Nowe badanie</a></li>
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
    <div class="row">
        <div class="col-sm-12">
            <h4 class="pull-left">Urządzenia</h4>
            <div class="pull-right">
                <button id="btn_examine" class="btn btn-primary pull-right" type="button">
                    <span class="glyphicon glyphicon-refresh"></span>
                    zaktualizuj wszystkie
                </button>
                <div id="examine_progress" class="pull-right" style="min-height:45px; min-width:60px">
                    <span class="progress-loading"></span>
                    <span class="progress-success"></span>
                    <span class="progress-error"></span>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div id="main_progress" class="col-sm-12">
            <div class="progress-loading"></div>
            <div class="progress-success">
                <div id="main_success"></div>
                <div class="row">
                    <div class="col-sm-12">
                        <a href="/create-device" class="btn btn-success" role="button">
                            <span class="glyphicon glyphicon-plus"></span>
                            Dodaj nowe urządzenie
                        </a>
                    </div>
                </div>
            </div>
            <div class="progress-error"></div>
        </div>
    </div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/tabelka.js"></script>
<script>
"use strict";
(function() {
    var devices, columnDefinitions, currentTabelka;
    currentTabelka = null;
    devices = [];
    columnDefinitions = [
        {
            "label" : 'nazwa',
            "comparator" : util.comparatorText('name'),
            "extractor" : function(deviceDetailsDto) {
                return $('<a></a>')
                    .attr('href', '/device?id=' + encodeURI(deviceDetailsDto.id))
                    .text(deviceDetailsDto.name);
            }
        }, {
            "label" : 'opis',
            "comparator" : util.comparatorText('description'),
            "extractor" : function(deviceDetailsDto) {
                return $('<span></span>').text(deviceDetailsDto.description);
            }
        }, {
            "label" : '2,4 GHz',
            "comparator" : util.comparatorNumber('_2400'),
            "extractor" : function(deviceDetailsDto) {
                var freq, survey, span;
                span = $('<span></span>');
                freq = deviceDetailsDto.frequency;
                if(typeof freq['2400'] !== 'undefined') {
                    survey = freq['2400'];
                    if(survey === null || !survey.enabled) {
                        span.text('wył.');
                    }
                    else {
                        span.text(survey.clients);
                    }
                }
                else {
                    span.text('-');
                }
                return span;
            }
        }, {
            "label" : 'kontroler',
            "comparator" : util.comparatorText('_controllerName'),
            "extractor" : function(deviceDetailsDto) {
                var c = deviceDetailsDto.controller;
                return $('<a></a>')
                    .attr('href', '/controller?id=' + encodeURI(c.id))
                    .text(c.name);
            }
        }
    ];

    function fixDevices() {
        var i, dev, controller, freq, survey;
        for(i = 0; i < devices.length; i++) {
            dev = devices[i];
            controller = dev.controller;
            freq = dev.frequency;
            if(controller === null) {
                dev._controllerName = null;
            }
            else {
                dev._controllerName = controller.name;
            }
            if(typeof freq['2400'] !== 'undefined') {
                survey = freq['2400'];
                if(survey !== null && survey.enabled) {
                    dev._2400 = survey.clients;
                }
                else {
                    dev._2400 = -1;
                }
            }
            else {
                dev._2400 = -2;
            }
        }
    }

    $(document).ready( function() {
        progress.load(
            'get',
            '/api/device/details/all',
            '#main_progress',
            function(listDtoOfDeviceDetailsDto) {
                var btnExamine, mainProgress, mainSuccess;
                btnExamine = $('#btn_examine');
                mainProgress = $('#main_progress');
                mainSuccess = $('#main_success');
                devices = listDtoOfDeviceDetailsDto.list;
                fixDevices();
                currentTabelka = tabelka.create(devices, columnDefinitions);
                mainSuccess.append(currentTabelka);
                btnExamine.click(
                    function() {
                        btnExamine.prop('disabled', true);
                        progress.loadMany(
                            [ {
                                "url" : '/api/examine-all',
                                "optionalPostData" : false
                            }, {
                                "url" : '/api/device/details/all'
                            } ],
                            '#examine_progress',
                            function(responses) {
                                btnExamine.prop('disabled', false);
                                devices = responses[1].list;
                                fixDevices();
                                mainSuccess.fadeOut(200, function() {
                                    mainSuccess.empty();
                                    currentTabelka = tabelka.create(devices, columnDefinitions)
                                    mainSuccess.append(currentTabelka);
                                    mainSuccess.fadeIn(200);
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
})();
</script>
</body>
</html>