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
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
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
                <li><a href="/all-buildings">Budynki</a></li>
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

<div id="all" class="container-fluid">
    <div id="container">
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
                <input id="time_start" type="datetime-local" class="form-control" required>
            </div>
            <div class="col-sm-6">
                przewidywany czas ostatniego badania
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-12 text-center">
                <button id="submit" type="button" class="btn btn-primary">Generuj</button>
            </div>
        </div>
        <div id="progress" class="row">
        </div>
    </div>
</div>

<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/progress.js"></script>
<script>
    $(document).ready(function () {
        var submitButton = $('#submit');
        var progressArea = $('#progress');
        submitButton.click(function () {
            var obj = {};
            obj.deviceId = $('#device_id').val();
            if(obj.deviceId === '')return;
            obj.frequencyMhz = $('#frequency_mhz').val();
            if(obj.frequencyMhz === '')return;
            obj.timeStart = 1490000000;//!
            if(obj.timeStart === '')return;
            obj.minInterval = $('#min_interval').val();
            if(obj.minInterval === '')return;
            obj.maxInterval = $('#max_interval').val();
            if(obj.maxInterval === '')return;
            obj.maxClients = $('#max_clients').val();
            if(obj.maxClients === '')return;
            obj.numberOfSurveys = $('#number_of_surveys').val();
            if(obj.numberOfSurveys === '')return;
            submitButton.prop('disabled', true);
            progressArea.empty();
            progressArea.append( createProgressCircle() );
            $.ajax( {
                "url": '/api/import/fake-surveys',
                "type": 'post',
                "dataType": 'json',
                "contentType": 'application/json',
                "data": JSON.stringify(obj),
                "success": function (baseResultDto) {
                    progressArea.empty();
                    submitButton.prop('disabled', false);
                    if(baseResultDto.success) {
                        progressArea.append(
                            $('<div></div>').addClass('panel panel-success').append(
                                $('<div></div>').addClass('panel-heading').append(
                                    'Sukces ',
                                    $('<a></a>')
                                        .attr('href', '/device?id=' + obj.deviceId)
                                        .text('sprawdź urządzenie')
                                )
                            )
                        );
                    }
                    else {
                        progressArea.append(
                            $('<div></div>').addClass('panel panel-danger').append(
                                $('<div></div>').addClass('panel-heading').text('Error')
                            )
                        );
                    }
                },
                "error": function() {
                    progressArea.empty();
                    submitButton.prop('disabled', false);
                    progressArea.append(
                        $('<div></div>').addClass('panel panel-danger').append(
                            $('<div></div>').addClass('panel-heading').text('Error')
                        )
                    );
                }
            } );
        });
    });
</script>
</body>
</html>