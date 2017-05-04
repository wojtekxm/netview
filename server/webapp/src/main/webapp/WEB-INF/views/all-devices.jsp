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
    <link rel="stylesheet" href="/css/tabelka.css">
    <link rel="stylesheet" href="/css/style.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <link rel="icon" href="/favicon.ico">
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
                <li><a href="/account"><span class="glyphicon glyphicon-user"></span>  &nbsp;<c:out value="${loggedUser.name}"/></a></li>
                <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <div style="height: 100px;"></div>
    <h4 class="pull-left">Urządzenia</h4>
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
        <div style="margin-top: 50px"></div>
        <div class="panel panel-default">
            <div class="panel-heading">Usuwanie starych badań</div>
            <div class="panel-body">
                <div class="form-inline"><div class="radio">
                    <label>
                        <input type="radio" name="delete_type" value="all" checked>
                        Wszystkie (<span id="total_all">?</span>)
                    </label>
                </div>
                </div>
                <div class="form-inline">
                    <div class="radio">
                        <label>
                            <input type="radio" name="delete_type" value="before"/>
                            Starsze niż
                        </label>
                        <div class='input-group date' id='datetimepicker1'>
                            <input type='text' class="form-control">
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
                        <div class="pull-left" style="min-height:45px; min-width:60px">
                            <div id="delete_loading"></div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div id="notify_deleted"></div>
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
    var $dateTimePicker, devices, columnDefinitions, tabelkaSpace, btnDelete,
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

    devices = [];
    columnDefinitions = [
        {
            "label" : 'nazwa',
            "comparator" : util.comparatorText('name'),
            "extractor" : 'td_name',
            "cssClass" : 'width-4'
        }, {
            "label" : 'opis',
            "comparator" : util.comparatorText('description'),
            "extractor" : 'td_description',
            "cssClass" : 'width-4'
        }, {
            "label" : '2,4 GHz',
            "comparator" : util.comparatorNumber('cmp_2400'),
            "extractor" : 'td_2400',
            "cssClass" : 'width-2'
        }, {
            "label" : '5 GHz',
            "comparator" : util.comparatorNumber('cmp_5000'),
            "extractor" : 'td_5000',
            "cssClass" : 'width-2'
        }, {
            "label" : 'kontroler',
            "comparator" : util.comparatorText('cmp_controller'),
            "extractor" : 'td_controller',
            "cssClass" : 'width-4'
        }
    ];

    function fixDevices() {
        var i, dev, controller, freq, survey;
        for(i = 0; i < devices.length; i++) {
            dev = devices[i];
            controller = dev.controller;
            freq = dev.frequency;

            dev.td_name = $('<a></a>')
                .attr('href', '/device/' + dev.id)
                .text(dev.name);

            dev.td_description = $('<span></span>').text(dev.description);

            dev.td_2400 = $('<span></span>');
            if(typeof freq['2400'] !== 'undefined') {
                survey = freq['2400'];
                if(survey !== null && survey.enabled) {
                    dev.cmp_2400 = survey.clients;
                    dev.td_2400.text(survey.clients);
                }
                else {
                    dev.cmp_2400 = -1;
                    dev.td_2400.text('wył.');
                }
            }
            else {
                dev.cmp_2400 = -2;
                dev.td_2400.text('-');
            }

            dev.td_5000 = $('<span></span>');
            if(typeof freq['5000'] !== 'undefined') {
                survey = freq['5000'];
                if(survey !== null && survey.enabled) {
                    dev.cmp_5000 = survey.clients;
                    dev.td_5000.text(survey.clients);
                }
                else {
                    dev.cmp_5000 = -1;
                    dev.td_5000.text('wył.');
                }
            }
            else {
                dev.cmp_5000 = -2;
                dev.td_5000.text('-');
            }

            if(controller === null) {
                dev.cmp_controller = null;
                dev.td_controller = $('<span></span>').text('-');
            }
            else {
                dev.cmp_controller = controller.name;
                dev.td_controller = $('<a></a>')
                    .attr('href', '/controller/' + controller.id)
                    .text(controller.name);
            }
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
        progress.load(
            'get',
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

    progress.loadMany(
        [{
            "url" : '/api/device/details/all'
        }, {
            "url" : '/api/surveys/total/all/all'
        }],
        ['.on-loading'], ['.on-loaded'], [],
        function(responses) {
            var btnExamine;
            btnExamine = $('#btn_examine');
            devices = responses[0].list;
            fixDevices();
            tabelkaSpace.append(
                tabelka.create(devices, columnDefinitions)
            );
            spanTotalAll.text(responses[1].content);
            refreshTotalBefore();
            btnExamine.click(function() {
                    btnExamine.prop('disabled', true);
                    progress.loadMany(
                        [ {
                            "url" : '/api/surveys/examine/all',
                            "optionalPostData" : false
                        }, {
                            "url" : '/api/device/details/all'
                        }, {
                            "url" : '/api/surveys/total/all/all'
                        } ],
                        ['#examine_loading'], [], [],
                        function(responses) {
                            btnExamine.prop('disabled', false);
                            devices = responses[1].list;
                            spanTotalAll.text(responses[2].content);
                            refreshTotalBefore();
                            fixDevices();
                            tabelkaSpace.fadeOut(200, function() {
                                tabelkaSpace.empty();
                                tabelkaSpace.append(
                                    tabelka.create(devices, columnDefinitions)
                                );
                                tabelkaSpace.fadeIn(200);
                            });
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
        progress.loadMany(
            [{
                "url" : urlDelete,
                "optionalPostData" : false
            }, {
                "url" : '/api/device/details/all'
            }, {
                "url" : '/api/surveys/total/all/all'
            }],
            ['#delete_loading'], [], [],
            function(responses) {
                devices = responses[1].list;
                spanTotalAll.text(responses[2].content);
                refreshTotalBefore();
                fixDevices();
                tabelkaSpace.fadeOut(200, function() {
                    tabelkaSpace.empty();
                    tabelkaSpace.append(
                        tabelka.create(devices, columnDefinitions)
                    );
                    tabelkaSpace.fadeIn(200);
                });
                btnDelete.prop('disabled', false);
                notify.success('#notify_deleted', 'Badania zostały usunięte');
            },
            function() {
                btnDelete.prop('disabled', false);
                notify.danger('#notify_deleted', 'Akcja się nie powiodła');
            }
        );
    });

    $dateTimePicker.on('dp.change', refreshTotalBefore);
} );
</script>
</body>
</html>