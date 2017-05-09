<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Informacje o urządzeniu</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/status-small.css">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
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
        <div class="row">
            <div class="col-sm-2">
                <div id="settings_freq" class="box">
                    Pasmo
                    <br>

                    <input type="radio" id="radio_freq_2400" name="frequency" value="2400">
                    <label id="label_2400" for="radio_freq_2400">2,4 Ghz</label>
                    <br>

                    <input type="radio" id="radio_freq_5000" name="frequency" value="5000">
                    <label id="label_5000" for="radio_freq_5000">5 Ghz</label>
                    <br>
                    <button type="button" id="btn_refresh" class="btn btn-primary">Odswież wykres</button>
                </div>
            </div>
            <div class="col-sm-3">
                <div id="settings_chart" class="box">
                    Typ
                    <br>

                    <input type="radio" id="radio_chart_day" name="chart" value="day" checked>
                    <label for="radio_freq_5000">dzienny</label>
                    <br>

                    <input type="radio" id="radio_chart_week" name="chart" value="week">
                    <label for="radio_chart_week">tygodniowy</label>
                    <br>

                    <input type="radio" id="radio_chart_month" name="chart" value="month">
                    <label for="radio_chart_month">miesięczny</label>
                    <br>

                    <input type="radio" id="radio_chart_year" name="chart" value="year">
                    <label for="radio_chart_year">roczny</label>
                    <br>

                    <input type="radio" id="radio_chart_other" name="chart" value="other">
                    <label for="radio_chart_other">niestandardowy</label>
                    <br>
                </div><br>
            </div>
            <div class="col-sm-3">
                <div id="settings_calendar" class="box">
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
                    <br>
                    <br>
                    Agregacja badań
                    <select name="group" id="select_group" size="1">
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
            </div>
            <div class="col-sm-4">
                <div id="settings_size" class="box">
                    Rozmiar wykresu<br>
                    <input type="radio" id="radio_size_auto" name="size" value="auto" checked> automatyczny<br>
                    <input type="radio" id="radio_size_custom" name="size" value="custom"> inny<br>
                    szerokość (piksele)
                    <input type="text" id="custom_size_width" class="form-control" value="1000" disabled>
                    wysokość (piksele)
                    <input type="text" id="custom_size_height" class="form-control" value="500" disabled>
                </div>
            </div>
        </div>
        <div id="chart_loading" class="later"></div>
        <div id="chart_area" class="later">
            <canvas id="mycanvas" width="1000px" height="500px"></canvas>
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
    var device, chartLoading, chartArea, btnRefresh,
        radioFreq, radioChartDay, radioChartWeek, radioChartMonth, radioChartYear,
        radioChartOther, radioSizeAuto, radioSizeCustom, myCanvas, myChart,
        settingsFreq, settingsChart, settingsCalendar, settingsSize, selectGroup,
        customSizeWidth, customSizeHeight;
    device = {};
    chartLoading = $('#chart_loading');
    chartArea = $('#chart_area');
    btnRefresh = $('#btn_refresh');
    radioFreq = {};
    radioFreq[2400] = $('#radio_freq_2400');
    radioFreq[5000] = $('#radio_freq_5000');
    radioChartDay = $('#radio_chart_day');
    radioChartWeek = $('#radio_chart_week');
    radioChartMonth = $('#radio_chart_month');
    radioChartYear = $('#radio_chart_year');
    radioChartOther = $('#radio_chart_other');
    radioSizeAuto = $('#radio_size_auto');
    radioSizeCustom = $('#radio_size_custom');
    myCanvas = $('#mycanvas');
    settingsFreq = $('#settings_freq');
    settingsChart = $('#settings_chart');
    settingsCalendar = $('#settings_calendar');
    settingsSize = $('#settings_size');
    selectGroup = $('#select_group');
    customSizeWidth = $('#custom_size_width');
    customSizeHeight = $('#custom_size_height');
    $('#datetimepicker1').datetimepicker({
        "locale": 'pl',
        "format": 'LLL'
    });
    $('#datetimepicker2').datetimepicker({
        "locale": 'pl',
        "format": 'LLL'
    });
    settingsCalendar.hide();
    settingsSize.hide();

    progress.loadGet('/api/device/details/' + ${device.id},
        ['.on-loading'], ['.on-loaded'], [],
        function(contentDtoOfDeviceDetailsDto) {
            var arr, i, k;
            device = contentDtoOfDeviceDetailsDto.content;
            arr = [2400, 5000];
            for(i = 0; i < arr.length; i++) {
                k = arr[i];
                if(typeof device.frequency[k] !== 'undefined') {
                    radioFreq[k].prop('checked', true);
                    break;
                }
            }
            for(i = 0; i < arr.length; i++) {
                k = arr[i];
                if(typeof device.frequency[k] !== 'undefined') {
                    radioFreq[k].prop('disabled', false);
                }
                else {
                    radioFreq[k].prop('disabled', true);
                    $('#label_' + k).wrapInner('<s></s>');
                }
            }
            refresh();
            $('input[type=radio][name=frequency]').change(function() {
                console.log("$('input[type=radio][name=frequency]').change");
                refresh();
            });
            $('input[type=radio][name=chart]').change(function() {
                console.log("$('input[type=radio][name=chart]').change");
                if(radioChartOther.prop('checked')) {
                    console.log('checked');
                    settingsCalendar.fadeIn(500);
                    settingsSize.fadeIn(500);
                }
                else {
                    console.log('not');
                    settingsCalendar.fadeOut(500);
                    settingsSize.fadeOut(500);
                }
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

    btnRefresh.click(function() {
        refresh();
    });

    function refresh() {
        var mhz, isOriginal, t0, t1, url, groupTime;

        mhz = null;
        if(radioFreq[2400].prop('checked'))mhz = 2400;
        else if(radioFreq[5000].prop('checked'))mhz = 5000;
        if(mhz === null)return;//?

        isOriginal = false;
        if(!radioChartOther.prop('checked')) {
            t1 = Math.round(Date.now() / 1000);//TODO wyrównać do pełnej doby...
            if(radioChartDay.prop('checked')) {
                t0 = t1 - 60 * 60 * 24;
                isOriginal = true;
                groupTime = null;
            }
            else if(radioChartWeek.prop('checked')) {
                t0 = t1 - 60 * 60 * 24 * 7;//TODO tydzień
                groupTime = 60 * 60;
            }
            else if(radioChartMonth.prop('checked')) {
                t0 = t1 - 60 * 60 * 24 * 30;//TODO miesiąc
                groupTime = 60 * 60 * 6;
            }
            else {
                t0 = t1 - 60 * 60 * 24 * 365;//TODO rok
                groupTime = 60 * 60 * 24;
            }
        }
        else {
            t1 = getTimestampOrNull(2);
            t0 = getTimestampOrNull(1);
            if(t0 === null || t1 === null || t1 <= t0) {
                alert('wybierz poprawny przedział czasu');//!
            }
            groupTime = parseInt(selectGroup.val());
            if(groupTime === 0) {
                groupTime = null;
                isOriginal = true;
            }
        }
        if(isOriginal) {
            url = '/api/surveys/original?device=' + device.id +
                '&frequency=' + mhz +
                '&start=' + t0 +
                '&end=' + t1;
            progress.loadGet(url,
                [chartLoading], [chartArea], [],
                function (response) {
                    genOriginal(response.content.list);
                },
                undefined,
                'md'
            );
        }
        else {
            url = '/api/surveys/avg-min-max?device=' + device.id +
            '&frequency=' + mhz +
            '&start=' + t0 +
            '&groupTime=' + groupTime +
            '&end=' + t1;
            progress.loadGet(url,
                [chartLoading], [chartArea], [],
                function (response) {
                    genAMM(response.content.list);
                },
                undefined,
                'md'
            );
        }
    }

    function genAMM(surveys) {
        var data, options, values_min, values_avg, values_max, tags, i;
        /*myCanvas.remove();
        myCanvas = $('<canvas id="mycanvas"></canvas>');
        $('#wykresy').append(myCanvas);
        myCanvas.css({
            "width": parseInt($('#chartSize1').val()) + "px",
            "height": parseInt($('#chartSize2').val()) + "px"
        });*/
        if(typeof myChart !== 'undefined') {
            myChart.destroy();
            myChart = undefined;
        }
        values_min = [];
        values_max = [];
        values_avg = [];
        tags = [];
        options = {
            "tooltips": {
                "mode": 'index'
            },
            "legend": {
                "display": true
            },
            "title": {
                "display": true,
                "text": 'wykres'//TODO ?
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
                    "display": true,
                    "barPercentage": 1,
                    "autoSkip": true,
                    "ticks": {
                        "maxRotation": 0,
                        "maxTicksLimit": 2
                    }
                }],
                "yAxes": [{
                    "stacked": false,
                    "display": true,
                    "scaleLabel": {
                        "display": true,
                        "labelString": 'Ilosc klientów'
                    },
                    "ticks": {
                        "ticks": {
                            "autoSkip": false,
                            "fixedStepSize": 10,
                            "beginAtZero": true,
                            "Min": -5,
                            "suggestedMax": 100
                        }
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
            "showLine": false,
            "labels": tags,
            "datasets": [{
                "label": "Minimalna ilosc",
                "data": values_min,
                "fill": false,
                "borderColor": "rgba(255,0,0,1)",
                "backgroundColor": "rgba(255,0,0,1)",
                "hoverBackgroundColor": "rgba(0,0,0,1)"
            }, {
                "label": "Srednia ilosc",
                "data": values_avg,
                "fill": false,
                "borderColor": "rgba(255,155,0,1)",
                "backgroundColor": "rgba(255,200,0,1)",
                "hoverBackgroundColor": "rgba(0,0,0,1)"
            }, {
                "label": "Maksymalna ilosc",
                "data": values_max,
                "fill": false,
                "borderColor": "rgba(8, 95, 41,1)",
                "backgroundColor": "rgba(8, 139, 41,1)",
                "hoverBackgroundColor": "rgba(8, 139, 41,1)"
            }]
        };
        for(i = 0; i < surveys.length; i++) {
            values_avg.push(surveys[i].average);
            values_min.push(surveys[i].min);
            values_max.push(surveys[i].max);
            tags.push( convert(surveys[i].timeStart) );
        }
        myChart = Chart.Line(myCanvas, {
            "data": data,
            "options": options
        });
    }

    function genOriginal(surveys) {
        var data, options, values, tags, i;
        /*myCanvas.remove();
        myCanvas = $('<canvas id="mycanvas"></canvas>');
        $('#wykresy').append(myCanvas);
        myCanvas.css({
            "width": parseInt($('#chartSize1').val()) + "px",
            "height": parseInt($('#chartSize2').val()) + "px"
        });*/
        if(typeof myChart !== 'undefined') {
            myChart.destroy();
            myChart = undefined;
        }
        values = [];
        tags = [];
        options = {
            "tooltips": {
                "mode": 'index'
            },
            "legend": {
                "display": true
            },
            "title": {
                "display": true,
                "text": 'wykres'//TODO ?
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
                    "display": true,
                    "barPercentage": 1,
                    "autoSkip": true,
                    "ticks": {
                        "maxRotation": 0,
                        "maxTicksLimit": 2
                    }
                }],
                "yAxes": [{
                    "stacked": false,
                    "display": true,
                    "scaleLabel": {
                        "display": true,
                        "labelString": 'Ilosc klientów'
                    },
                    "ticks": {
                        "ticks": {
                            "autoSkip": false,
                            "fixedStepSize": 10,
                            "beginAtZero": true,
                            "Min": -5,
                            "suggestedMax": 100
                        }
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
            "showLine": false,
            "labels": tags,
            "datasets": [{
                "label": 'Ilosc klientów',
                "data": values,
                "fill": false,
                "borderColor": 'rgba(255,155,0,1)',
                "backgroundColor": 'rgba(255,200,0,1)',
                "pointBorderWidth": 1,
                "hoverBackgroundColor": 'rgba(0,0,0,1)'
            }]
        };
        for(i = 0; i < surveys.length; i++) {
            values.push( Math.round(surveys[i].clients) );
            tags.push( convert( Number(surveys[i].timestamp) ) );
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
});
</script>
</body>
</html>