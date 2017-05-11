<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Generator sztucznych badań</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="/css/style.css">
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
            <ul class="nav navbar-nav" style="margin-left:10px;padding-right:3px;font-size: 16px;">
                <li><a style="background-color: black;padding-left:25px;padding-right: 20px;" href="/"><span class="glyphicon glyphicon-home"></span> &nbsp;NetView &nbsp;</a></li>
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
<div id="all" class="container-fluid">
    <div class="container">
        <h1>Generator sztucznych badań</h1>
        <div class="form-group row">
            <div class="col-sm-6">
                <label for="device_id">id urządzenia</label>
                <input id="device_id" type="number" class="form-control" required>
            </div>
            <div class="col-sm-6">
                <label for="frequency_mhz">częstotliwość [MHz]</label>
                <input id="frequency_mhz" type="number" class="form-control" value="2400" required>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-6">
                <label for="number_of_surveys">liczba badań</label>
                <input id="number_of_surveys" type="number" class="form-control" required>
            </div>
            <div class="col-sm-6">
                <label for="max_clients">maksymalna liczba klientów</label>
                <input id="max_clients" type="number" class="form-control" value="50" required>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-6">
                <label for="min_interval">minimalny odstęp badań w sekundach</label>
                <input id="min_interval" type="number" class="form-control" value="300" required>
            </div>
            <div class="col-sm-6">
                <label for="max_interval">maksymalny odstęp badań w sekundach</label>
                <input id="max_interval" type="number" class="form-control" value="300" required>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-6">
                <label for="time_start">czas początkowy</label>
                <div class='input-group date' id='datetimepicker1'>
                    <input id="time_start" type='text' class="form-control">
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>
            <div class="col-sm-6">
                <label>przewidywany czas ostatniego badania</label>
                <input id="estimated_time_end" type="text" class="form-control" disabled>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-12 text-center">
                <button id="submit" type="button" class="btn btn-primary">Generuj</button>
                <div id="loading" class="progress-space"></div>
            </div>
        </div>
        <div id="notify" class="row"></div>
    </div>
</div>

<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/moment-with-locales.min.js"></script>
<script src="/js/bootstrap-datetimepicker.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/notify.js"></script>
<script>
$(document).ready(function () {
    var $dateTimePicker = $('#datetimepicker1');
    var $submitButton = $('#submit');
    var $progressArea = $('#progress');
    var $minInterval = $('#min_interval');
    var $maxInterval = $('#max_interval');
    var $numberOfSurveys = $('#number_of_surveys');
    var $estimatedTimeEnd = $('#estimated_time_end');
    divNotify = $('#notify');
    $dateTimePicker.datetimepicker( {
        "locale": 'pl',
        "format": 'LLL'
    });
    $minInterval.on('change', updateEstimated);
    $maxInterval.on('change', updateEstimated);
    $numberOfSurveys.on('change', updateEstimated);
    $dateTimePicker.on('dp.change', updateEstimated);
    function updateEstimated() {
        var date = $dateTimePicker.data('DateTimePicker').date();
        if(date === null) {
            $estimatedTimeEnd.val('?');
            return;
        }
        var timeStart = date.unix();
        var interval0 = parseInt( $minInterval.val() );
        var interval1 = parseInt( $maxInterval.val() );
        var nos = parseInt( $numberOfSurveys.val() );
        if( isNaN(interval0) || isNaN(interval1) || isNaN(nos) ||
            (interval0 < 1) || (interval1 < 1) || (nos < 1) ) {
            $estimatedTimeEnd.val('?');
            return;
        }
        var ete = timeStart + (nos - 1) * (interval0 + interval1) / 2;
        var momentEnd = moment.unix(ete);
        momentEnd.locale('pl');
        $estimatedTimeEnd.val(momentEnd.format('LLL'));
    }

    $submitButton.click(function () {
        var obj = {};
        var date = $dateTimePicker.data('DateTimePicker').date();
        if(date === null)return;
        obj.timeStart = date.unix();
        obj.deviceId = parseInt( $('#device_id').val() );
        obj.frequencyMhz = parseInt( $('#frequency_mhz').val() );
        obj.minInterval = parseInt( $('#min_interval').val() );
        obj.maxInterval = parseInt( $('#max_interval').val() );
        obj.maxClients = parseInt( $('#max_clients').val() );
        obj.numberOfSurveys = parseInt( $('#number_of_surveys').val() );
        if( isNaN(obj.deviceId) )return;
        if( isNaN(obj.frequencyMhz) )return;
        if( isNaN(obj.minInterval) )return;
        if( isNaN(obj.maxInterval) )return;
        if( isNaN(obj.maxClients) )return;
        if( isNaN(obj.numberOfSurveys) )return;
        $submitButton.prop('disabled', true);
        progress.load(
            [{
                "url" : '/api/surveys/fake',
                "method" : 'post',
                "postData" : obj
            }],
            ['#loading'], [], [],
            function() {
                $submitButton.prop('disabled', false);
                divNotify.empty();
                divNotify.append(
                    $('<div></div>').addClass('panel panel-success').append(
                        $('<div></div>').addClass('panel-heading').append(
                            'Sukces ',
                            $('<a></a>')
                                .attr('href', '/device/' + obj.deviceId)
                                .text('sprawdź urządzenie')
                        )
                    )
                );
            },
            function() {
                $submitButton.prop('disabled', false);
                divNotify.empty();
                divNotify.append(
                    $('<div></div>').addClass('panel panel-danger').append(
                        $('<div></div>').addClass('panel-heading').text('Error')
                    )
                );
            }
        );
    });
});
</script>
</body>
</html>