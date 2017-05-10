<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Access Point <c:out value="${device.name}"/></title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/status-small.css">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <style>
        .box {
            width: 100%;
            padding: 5px;
            height: 200px;
        }
        body {
            background-color: rgb(217, 224, 231);
        }
    </style>
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
                <li><a style="background-color: black;margin-left:10px;padding-left:25px;padding-right: 20px;" href="/"><span class="glyphicon glyphicon-home"></span> &nbsp;NetView &nbsp;</a></li>
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
                <li><a href="/logout" style="margin-right: 10px;"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <div style="margin-top:80px"></div>
    <div class="on-loading progress-space"></div>
    <div class="on-loaded">
        <div class="panel panel-default">
            <div class="panel-heading clearfix">
                <h4 class="pull-left" style="margin-bottom: 0">Informacje o urządzeniu</h4>
                <form class="pull-right" method="post" action="/device/remove/${device.id}">
                    <button class="btn btn-sm btn-danger" type="submit">
                        <span class="glyphicon glyphicon-trash"></span>
                        Usuń
                    </button>
                </form>
            </div>
            <table class="table table-bordered">
                <tr>
                    <th>nazwa</th>
                    <th>2,4 GHz</th>
                    <th>5 GHz</th>
                    <th>kontroler</th>
                    <th>lokalizacja</th>
                    <th>liczba badań</th>
                </tr>
                <tr>
                    <td id="td_name"></td>
                    <td id="td_2400"></td>
                    <td id="td_5000"></td>
                    <td id="td_controller"></td>
                    <td id="td_location"></td>
                    <td id="td_surveys"></td>
                </tr>
            </table>
        </div>
        <div id="panel-chart" class="panel panel-default">
            <div class="panel-heading">
                <div class="btn-group btn-group-justified" data-toggle="buttons">
                    <label class="btn btn-primary active">
                        <input type="radio" id="radio_chart_day" name="chart" value="day" autocomplete="off" checked>
                        dzienny
                    </label>
                    <label class="btn btn-primary">
                        <input type="radio" id="radio_chart_week" name="chart" value="week" autocomplete="off">
                        tygodniowy
                    </label>
                    <label class="btn btn-primary">
                        <input type="radio" id="radio_chart_month" name="chart" value="month" autocomplete="off">
                        miesięczny
                    </label>
                    <label class="btn btn-primary">
                        <input type="radio" id="radio_chart_year" name="chart" value="year" autocomplete="off">
                        roczny
                    </label>
                    <label class="btn btn-primary">
                        <input type="radio" id="radio_chart_other" name="chart" value="other" autocomplete="off">
                        niestandardowy
                    </label>
                </div>
                <div id="settings_advanced" class="collapse in">
                    <div class="row">
                        <div class="col-sm-3">
                                początek<br>
                                <div class='input-group date' id='datetimepicker1'>
                                    <input id="time_start" type='text'  data-date-end-date="0d" class="form-control">
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                                koniec<br>
                                <div class='input-group date' id='datetimepicker2'>
                                    <input id="time_end" type='text'  data-date-end-date="0d" class="form-control">
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                        </div>
                        <div class="col-sm-3">
                            Agregacja badań
                            <br>
                            <select class="form-control" name="group" id="select_group" size="1">
                                <option value="0">brak</option>
                                <option value="300">5 minut</option>
                                <option value="900">15 minut</option>
                                <option value="3600">godzina</option>
                                <option value="10800">3 godziny</option>
                                <option value="86400">dzień</option>
                                <option value="604800">7 dni</option>
                                <option value="2592000">30 dni</option>
                                <option value="31536000">365 dni</option>
                            </select>
                        </div>
                        <div class="col-sm-3">
                            Rozmiar wykresu<br>
                            <label>
                                <input type="radio" id="radio_size_auto" name="size" value="auto" checked> automatyczny
                            </label>
                            <br>
                            <label>
                                <input type="radio" id="radio_size_custom" name="size" value="custom"> inny
                            </label>
                            <br>
                        </div>
                        <div class="col-sm-3">
                                szerokość (piksele)
                                <input type="text" id="custom_size_width" class="form-control" value="1000" disabled>
                                wysokość (piksele)
                                <input type="text" id="custom_size_height" class="form-control" value="400" disabled>
                        </div>
                    </div>
                    <div class="text-center">
                            <button type="button" id="btn_cancel" class="btn btn-default">Schowaj</button>
                            <button type="button" id="btn_custom_apply" class="btn btn-primary">Zastosuj</button>
                    </div>
                </div>
            </div>
            <div class="panel-body">
                <div id="chart_loading" class="later" style="height:400px"></div>
                <div id="chart_area" class="later" style="overflow: auto">
                    <canvas id="mycanvas" width="1000px" height="400px"></canvas>
                </div>
            </div>
            <div class="panel-footer clearfix">
                <div class="row">
                    <div class="col-xs-4 clearfix">
                        <button id="btn_past" type="button" class="btn btn-primary pull-left">
                            <span class="glyphicon glyphicon-arrow-left"></span>
                        </button>
                    </div>
                    <div class="col-xs-4 text-center">
                        <div class="btn-group" data-toggle="buttons">
                            <label id="label_freq_2400" class="btn btn-primary">
                                <input type="radio" id="radio_freq_2400" name="frequency" value="2400" autocomplete="off">
                                2,4 Ghz
                            </label>
                            <label id="label_freq_5000" class="btn btn-primary">
                                <input type="radio" id="radio_freq_5000" name="frequency" value="5000" autocomplete="off">
                                5 Ghz
                            </label>
                        </div>
                    </div>
                    <div class="col-xs-4 clearfix">
                        <button id="btn_future" type="button" class="btn btn-primary pull-right">
                            <span class="glyphicon glyphicon-arrow-right"></span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/moment-with-locales.min.js"></script>
<script src="/js/bootstrap-datetimepicker.min.js"></script>
<script src="/js/Chart.js"></script>
<script src="/js/progress.js"></script>
<script>
"use strict";
$(document).ready(function() {
    var device, chartLoading, chartArea, btnCancel, btnCustomApply,
        radioFreq, radioChartDay, radioChartWeek, radioChartMonth, radioChartYear,
        radioChartOther, radioSizeAuto, radioSizeCustom, myCanvas, myChart,
        settingsAdvanced, selectGroup,
        customSizeWidth, customSizeHeight, labFreq,
        tdName, tdFreq, tdController, tdLocation, tdSurveys, btnPast, btnFuture,
        arrFreqs, currentFreq, presentedChart, timeJump;
    device = {};
    chartLoading = $('#chart_loading');
    chartArea = $('#chart_area');
    btnCancel = $('#btn_cancel');
    btnCustomApply = $('#btn_custom_apply');
    radioFreq = {};
    radioFreq[2400] = $('#radio_freq_2400');
    radioFreq[5000] = $('#radio_freq_5000');
    labFreq = {
        "2400" : $('#label_freq_2400'),
        "5000" : $('#label_freq_5000')
    };
    radioChartDay = $('#radio_chart_day');
    radioChartWeek = $('#radio_chart_week');
    radioChartMonth = $('#radio_chart_month');
    radioChartYear = $('#radio_chart_year');
    radioChartOther = $('#radio_chart_other');
    radioSizeAuto = $('#radio_size_auto');
    radioSizeCustom = $('#radio_size_custom');
    myCanvas = $('#mycanvas');
    settingsAdvanced = $('#settings_advanced');
    selectGroup = $('#select_group');
    customSizeWidth = $('#custom_size_width');
    customSizeHeight = $('#custom_size_height');
    tdName = $('#td_name');
    tdFreq = {
        "2400" : $('#td_2400'),
        "5000" : $('#td_5000')
    };
    tdController = $('#td_controller');
    tdLocation = $('#td_location');
    tdSurveys = $('#td_surveys');
    btnPast = $('#btn_past');
    btnFuture = $('#btn_future');
    arrFreqs = [2400, 5000];
    timeJump = 0;
    moment.locale('pl');
    Chart.defaults.global.tooltips.caretSize = 8;
    Chart.defaults.global.hover.animationDuration = 100;
    $('#datetimepicker1').datetimepicker({
        "locale": 'pl',
        "format": 'LLL'
    });
    $('#datetimepicker2').datetimepicker({
        "locale": 'pl',
        "format": 'LLL'
    });
    settingsAdvanced.collapse();

    progress.loadParallel(
        [ {
            "url" : '/api/device/details/' + ${device.id}
        }, {
            "url" : '/api/surveys/total/' + ${device.id} + '/all'
        } ],
        ['.on-loading'], ['.on-loaded'], [],
        function(responses) {
            var arr, i, k, tmp, firstMhz;
            device = responses[0].content;
            tdName.text(device.name);
            if(device.controller === null) {
                tdController.text('-');
            }
            else {
                tdController.append(
                    $('<a></a>').attr('href', '/controller/' + device.controller.id)
                        .text(device.controller.name)
                );
            }
            if(device.building === null) {
                tdLocation.text('-');
            }
            else {
                tdLocation.append(
                    $('<a></a>').attr('href', '/building/' + device.building.id)
                        .text(device.building.name)
                );
            }
            tdSurveys.text(responses[1].content);

            for(i = 0; i < arrFreqs.length; i++) {
                k = arrFreqs[i];
                if(typeof device.frequency[k] !== 'undefined') {
                    tmp = device.frequency[k];
                    tdFreq[k].append(
                        $('<span class="glyphicon glyphicon-check" style="color:#5cb85c"></span>')
                    );
                    if(tmp === null || tmp.enabled === false) {
                        tdFreq[k].append(
                            ' (wyłączone)'
                        );
                    }
                    else if(tmp.clients < 1) {
                        tdFreq[k].append(
                            ' (0 klientów)'
                        );
                    }
                    else {
                        tdFreq[k].append(
                            ' (' + tmp.clients + ' klientów)'
                        );
                    }
                }
                else {
                    radioFreq[k].prop('disabled', true);
                    labFreq[k].addClass('disabled');
                    labFreq[k].prop('disabled', true);
                    tdFreq[k].append(
                        $('<span class="glyphicon glyphicon-unchecked" style="color:#d9534f"></span>'),
                        ' nie wspiera'
                    );
                    //$('#label_freq_' + k).wrapInner('<s></s>');
                }
            }
            firstMhz = null;
            for(i = 0; i < arrFreqs.length; i++) {
                k = arrFreqs[i];
                if(typeof device.frequency[k] !== 'undefined') {
                    radioFreq[k].prop('checked', true);
                    labFreq[k].addClass('active');
                    firstMhz = k;
                    break;
                }
            }
            for(i = 0; i < arrFreqs.length; i++) {//!
                k = arrFreqs[i];
                if(typeof device.frequency[k] === 'undefined') {
                    labFreq[k].detach();
                }
            }//!
            if(firstMhz === null) {
                $('#panel-chart').hide();
                return;
            }
            refresh();
            $('input[type=radio][name=chart]').change(function() {
                timeJump = 0;
                var val = $('input[type=radio][name=chart]:checked').val();
                console.log("$('input[type=radio][name=chart]:checked').val()", val);
                if(val === 'other') {
                    settingsAdvanced.collapse('show');
                }
                else {
                    settingsAdvanced.collapse('hide');
                    refresh();
                }
            });
            btnCancel.click(function() {
                settingsAdvanced.collapse('hide');
            });
            btnPast.click(function() {
                timeJump--;
                refresh();
            });
            btnFuture.click(function() {
                timeJump++;
                refresh();
            })
            $('input[type=radio][name=frequency]').change(function() {
                console.log(
                    "$('input[type=radio][name=frequency]'):checked",
                    $('input[type=radio][name=frequency]:checked').val()
                );
                refresh();
            });
            $('input[type=radio][name=size]').change(function() {
                if(radioSizeAuto.prop('checked')) {
                    customSizeWidth.prop('disabled', true);
                    customSizeHeight.prop('disabled', true);
                }
                else {
                    customSizeWidth.prop('disabled', false);
                    customSizeHeight.prop('disabled', false);
                }
            });
        },
        undefined,
        'md'
    );

    btnCustomApply.click(function() {
        timeJump = 0;
        refresh();
    });

    function checkRefresh() {
        //... sprawdź czy w ogóle coś się zmieniło ...
        refresh();
    }

    function refresh() {
        var mhz, isOriginal, t0, t1, timeSpan, url, groupTime, targetChart;

        mhz = null;
        if(radioFreq[2400].prop('checked'))mhz = 2400;
        else if(radioFreq[5000].prop('checked'))mhz = 5000;
        if(mhz === null)return;//?

        targetChart = $('input[type=radio][name=chart]:checked').val();
        isOriginal = false;
        if(targetChart === 'other') {
            t1 = getTimestampOrNull(2);
            t0 = getTimestampOrNull(1);
            if(t0 === null || t1 === null || t1 <= t0) {
                alert('wybierz poprawny przedział czasu');//!
                return;
            }
            groupTime = parseInt(selectGroup.val());
            if(groupTime === 0) {
                groupTime = null;
                isOriginal = true;
            }
        }
        else {
            t1 = Math.round(Date.now() / 1000);//TODO wyrównać do pełnej doby...
            if(targetChart === 'day') {
                t0 = t1 - 60 * 60 * 24;
                isOriginal = true;
                groupTime = null;
            }
            else if(targetChart === 'week') {
                t0 = t1 - 60 * 60 * 24 * 7;//TODO tydzień
                groupTime = 60 * 60;
            }
            else if(targetChart === 'month') {
                t0 = t1 - 60 * 60 * 24 * 30;//TODO miesiąc
                groupTime = 60 * 60 * 4;
            }
            else {
                t0 = t1 - 60 * 60 * 24 * 365;//TODO rok
                groupTime = 60 * 60 * 24;
            }
        }
        timeSpan = t1 - t0;
        t1 += timeSpan * timeJump;
        t0 += timeSpan * timeJump;
        if(isOriginal) {
            url = '/api/surveys/original?device=' + device.id +
                '&frequency=' + mhz +
                '&start=' + t0 +
                '&end=' + t1;
            progress.loadGet(url,
                [chartLoading], [chartArea], [],
                function (response) {
                    genOriginal(response.content.list, response.content.before, response.content.after, t0, t1);
                    presentedChart = targetChart;
                    setCalendar(t0, t1);
                    if(targetChart === 'other') {
                        timeJump = 0;
                    }
                },
                undefined,
                'md'
            );
        }
        else {
            url = '/api/surveys/avg-min-max?device=' + device.id +
            '&frequency=' + mhz +
            '&start=' + (t0 - groupTime) +
            '&groupTime=' + groupTime +
            '&end=' + (t1 + groupTime);
            progress.loadGet(url,
                [chartLoading], [chartArea], [],
                function (response) {
                    genAMM(response.list, t0, t1);
                    presentedChart = targetChart;
                    setCalendar(t0, t1);
                    if(targetChart === 'other') {
                        timeJump = 0;
                    }
                },
                undefined,
                'md'
            );
        }
    }

    function genAMM(surveysExtended, t0, t1) {
        var data, options, points_min, points_avg, points_max, i, last;
        prepareCanvas();
        points_min = [];
        points_max = [];
        points_avg = [];
        options = {
            "tooltips": {
                "mode": 'index'
            },
            "legend": {
                "display": true
            },
            "title": {
                "display": false,
                "text": ''
            },
            "hover": {
                "intersect": true,
                "mode": 'x'
            },
            "label": {
                "display": true
            },
            "scales": {
                "xAxes": [{
                    "type" : 'time',
                    "time" : {
                        "minUnit" : 'minute',
                        "tooltipFormat" : 'dddd, D MMMM YYYY, h:mm:ss',
                        "displayFormats" : {
                            "minute" : 'HH:mm',
                            "hour" : 'HH:mm',
                            "day" : 'dddd',
                            "week" : 'DD.MM',
                            "month" : 'DD.MM'
                        },
                        "min" : t0 * 1000,
                        "max" : t1 * 1000
                    }
                }],
                "yAxes": [{
                    "stacked": false,
                    "display": true,
                    "scaleLabel": {
                        "display": true,
                        "labelString": 'liczba klientów'
                    },
                    "ticks": {
                        "autoSkip": false,
                        "fixedStepSize": 10,
                        "beginAtZero": true,
                        "Min": -5,
                        "suggestedMax": 50
                    }
                }]
            },
            "maintainAspectRatio": false,
            "responsive": radioSizeAuto.prop('checked'),
            "steppedLine": true,
            "elements": {
                "line": {
                    "tension": 0
                }
            }
        };
        data = {
            "datasets": [{
                "label": "minimalnie",
                "data": points_min,
                "fill": true,
                "backgroundColor": '#ffee00',
                "borderColor": '#ffcc00',
                "pointBackgroundColor": '#ffee00',
                "pointBorderColor": '#ffcc00',
                "pointHoverBackgroundColor": '#ffee00',
                "pointHoverBorderColor": '#ffcc00',
                "borderWidth" : 1,
                "pointBorderWidth" : 1,
                "pointHoverBorderWidth" : 1,
                "borderJoinStyle" : 'round',
                "pointRadius" : 0,
                "pointHoverRadius" : 5,
                "pointHitRadius" : 5
            }, {
                "label": "średnio",
                "data": points_avg,
                "fill": true,
                "backgroundColor": '#ff6600',
                "borderColor": '#e62e00',
                "pointBackgroundColor": '#ff6600',
                "pointBorderColor": '#e62e00',
                "pointHoverBackgroundColor": '#ff6600',
                "pointHoverBorderColor": '#e62e00',
                "borderWidth" : 1,
                "pointBorderWidth" : 1,
                "pointHoverBorderWidth" : 1,
                "borderJoinStyle" : 'round',
                "pointRadius" : 0,
                "pointHoverRadius" : 5,
                "pointHitRadius" : 5
            }, {
                "label": "maksymalnie",
                "data": points_max,
                "fill": true,
                "backgroundColor": '#cc0000',
                "borderColor": '#800000',
                "pointBackgroundColor": '#cc0000',
                "pointBorderColor": '#800000',
                "pointHoverBackgroundColor": '#cc0000',
                "pointHoverBorderColor": '#800000',
                "borderWidth" : 1,
                "pointBorderWidth" : 1,
                "pointHoverBorderWidth" : 1,
                "borderJoinStyle" : 'round',
                "pointRadius" : 0,
                "pointHoverRadius" : 5,
                "pointHitRadius" : 5
            }]
        };
        for(i = 0; i < surveysExtended.length; i++) {
            last = surveysExtended[i];
            points_avg.push({
                "y" : Math.round(last.average),
                "x" : last.timeStart * 1000
            });
            points_min.push({
                "y" : last.min,
                "x" : last.timeStart * 1000
            });
            points_max.push({
                "y" : last.max,
                "x" : last.timeStart * 1000
            });
        }
        if(surveysExtended.length > 0) {
            last = surveysExtended[surveysExtended.length-1];
            points_avg.push({
                "y" : Math.round(last.average),
                "x" : last.timeEnd * 1000
            });
            points_min.push({
                "y" : last.min,
                "x" : last.timeEnd * 1000
            });
            points_max.push({
                "y" : last.max,
                "x" : last.timeEnd * 1000
            });
        }
        myChart = Chart.Line(myCanvas, {
            "data": data,
            "options": options
        });
    }

    function genOriginal(surveys, before, after, t0, t1) {
        var data, options, points, i, last, ctx, grad;
        prepareCanvas();
        ctx = myCanvas.get()[0].getContext('2d', {
            "alpha": true
        });
        grad = ctx.createLinearGradient(0, 400, 0, 0);
        grad.addColorStop(0.0, 'rgba(3,169,244,0.6)');
        grad.addColorStop(1.0, 'rgba(3,169,244,0.6)');
        points = [];
        options = {
            "tooltips": {
                "mode": 'index'
            },
            "legend": {
                "display": true
            },
            "title": {
                "display": false,
                "text": ''
            },
            "hover": {
                "intersect": false,
                "mode": 'x'
            },
            "label": {
                "display": true
            },
            "scales": {
                "xAxes": [{
                    "type" : 'time',
                    "time" : {
                        "minUnit" : 'minute',
                        "tooltipFormat" : 'dddd, D MMMM YYYY, h:mm:ss',
                        "displayFormats" : {
                            "minute" : 'HH:mm',
                            "hour" : 'HH:mm',
                            "day" : 'dddd',
                            "week" : 'DD.MM',
                            "month" : 'DD.MM'
                        },
                        "min" : t0 * 1000,
                        "max" : t1 * 1000
                    }
                    /*"type" : 'linear',
                    "position": 'bottom',
                    "display": true,
                    "barPercentage": 1,
                    "autoSkip": true,
                    "ticks": {
                        "maxRotation": 0,
                        "maxTicksLimit": 2
                    }*/
                }],
                "yAxes": [{
                    "stacked": false,
                    "display": true,
                    "scaleLabel": {
                        "display": true,
                        "labelString": 'liczba klientów'
                    },
                    "ticks": {
                        "autoSkip": false,
                        "fixedStepSize": 10,
                        "beginAtZero": true,
                        "Min": -5,
                        "suggestedMax": 50
                    }
                }]
            },
            "maintainAspectRatio": false,
            "responsive": radioSizeAuto.prop('checked'),
            "steppedLine": true,
            "elements": {
                "line": {
                    "tension": 0
                }
            }
        };
        data = {
            "datasets": [{
                "label": 'liczba klientów',
                "data": points,
                "fill": true,
                "backgroundColor": grad,
                "borderColor": 'rgb(2, 139, 202)',
                "pointBackgroundColor": grad,
                "pointBorderColor": 'rgb(2, 139, 202)',
                "pointHoverBackgroundColor": grad,
                "pointHoverBorderColor": 'rgb(2, 139, 202)',
                "borderWidth" : 1,
                "pointBorderWidth" : 1,
                "pointHoverBorderWidth" : 1,
                "borderJoinStyle" : 'round',
                "pointRadius" : 2,
                "pointHoverRadius" : 7,
                "pointHitRadius" : 7
            }]
        };
        if(before !== null) {
            points.push({
                "y" : before.clients,
                "x" : before.timestamp * 1000
            });
        }
        for(i = 0; i < surveys.length; i++) {
            points.push({
                "y" : surveys[i].clients,
                "x" : surveys[i].timestamp * 1000
            });
        }
        if(after !== null) {
            points.push({
                "y" : after.clients,
                "x" : after.timestamp * 1000
            });
        }
        else if(points.length > 0) {
            last = points[points.length - 1];
            points.push({
                "y" : last.y,
                "x" : moment().valueOf()
            });
        }
        myChart = Chart.Line(myCanvas, {
            "data": data,
            "options": options
        });
    }

    function convert(time) {
        var DayName = ["niedziela", "poniedziałek", "wtorek", "sroda", "czwartek", "piątek", "sobota"];
        var MonthName = ["stycznia ", "lutego ", "marca ", "kwietnia ", "maja ", "czerwca ",
            "lipca ", "sierpnia ", "września ", "października ", "listopada ", "grudnia "];
        time = time * 1000;
        var temp = new Date(time);
        var Seconds = temp.getSeconds();
        var Minutes = temp.getMinutes();
        var Hours = temp.getHours();
        var WeekDay = temp.getDay();
        var Month = temp.getMonth();
        var Day = temp.getDate();
        var Year = temp.getFullYear();
        return (Hours > 9 ? '' : '0') + Hours + ":" +
            (Minutes > 9 ? '' : '0') + Minutes + ":" +
            (Seconds > 9 ? '' : '0') + Seconds + "    " +
            Day + " " + MonthName[Month] + " " + Year + " (" + DayName[WeekDay] + ") ";
    }

    function getTimestampOrNull(oneOrTwo) {
        var d = $('#datetimepicker' + oneOrTwo).data('DateTimePicker').date();
        if(d === null) {
            return null;
        }
        return d.unix();
    }

    function setCalendar(t0, t1) {
        $('#datetimepicker1').data('DateTimePicker').date( moment.unix(t0) );
        $('#datetimepicker2').data('DateTimePicker').date( moment.unix(t1) );
    }

    function prepareCanvas() {
        var w, h;
        if(typeof myChart !== 'undefined') {
            myChart.destroy();
            myChart = undefined;
        }
        chartArea.empty();
        myCanvas = $('<canvas id="mycanvas"></canvas>');
        if( radioSizeCustom.prop('checked') && radioChartOther.prop('checked') ) {
            w = parseInt( $('#custom_size_width').val() );
            h = parseInt( $('#custom_size_height').val() );
            if(isNaN(w) || isNaN(h)) {
                if(isNaN(w)) {
                    alert('Podaj poprawną szerokość');
                }
                else {
                    alert('Podaj poprawną wysokość');
                }
                if(isNaN(w)) {
                    w = 1000;
                    $('#custom_size_width').val(w);
                }
                if(isNaN(h)) {
                    h = 400;
                    $('#custom_size_height').val(h)
                }
            }
            w = Math.round(w);
            h = Math.round(h);
        }
        else {
            w = 1000;
            h = 400;
        }
        myCanvas.attr('width', w)
            .attr('height', h);
        chartLoading.css({
            "height" : h + 'px'
        });
        chartArea.append(myCanvas);
    }
});
</script>
</body>
</html>