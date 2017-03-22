<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Status sieci (minimalistyczny)</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/status-small.css">
    <link rel="stylesheet" href="/css/style.css">
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
                <li><a style="background-color: #1d1d1d;" href="/"><span class="glyphicon glyphicon-home"></span></a></li>
                <li style="max-height:50px;"><a href="/make-survey">Nowe badanie</a></li>
                <li><a href="/all-controllers">Kontrolery</a></li>
                <li><a href="/all-users">Użytkownicy</a></li>
                <li><a href="/all-devices">Urządzenia</a></li>
                <li><a href="/building">Budynki</a></li>
            </ul>
            <form class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="Szukaj..." style="max-width: 200px!important;">
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

<!-- kill me pls -->
<div style="margin-top:100px"></div>
<!-- I'm not a solution -->

<div class="container">
    <div id="result" style="display:none">
        <ul class="view-small" id="list"></ul>
        <hr>
        <div class="summary">
            <div class="square-green"></div> aktywne: <span id="sum_active"></span><br>
            <div class="square-red"></div> nieaktywne: <span id="sum_inactive"></span><br>
            <div class="square-gray"></div> wyłączone: <span id="sum_disabled"></span><br>
        </div>
    </div>
    <div id="progress_area">pobieranie informacji...</div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script>
    $(function() {
        $('[data-toggle="tooltip"]').tooltip();
    });
    $(document).ready( function() {
        $.ajax( {
            type: 'get',
            url: '/api/all-devices',
            dataType: 'json',
            success: function(listOfDeviceStateDto) {
                var i;
                var sumActive = 0;
                var sumInactive = 0;
                var sumDisabled = 0;
                for(i = 0; i < listOfDeviceStateDto.length; i++) {
                    var deviceStateDto = listOfDeviceStateDto[i];
                    var clazz;
                    if(deviceStateDto.enabled) {
                        if(deviceStateDto.clientsSum > 0) {
                            sumActive++;
                            clazz = 'square-green';
                        }
                        else {
                            sumInactive++;
                            clazz = 'square-red';
                        }
                    }
                    else {
                        sumDisabled++;
                        clazz = 'square-gray';
                    }
                    var href = '/device?id=' + deviceStateDto.id;
                    var li = $('<li></li>').addClass(clazz)
                        .attr('title', deviceStateDto.name)
                        .attr('data-toggle', 'tooltip')
                        .append(
                            $('<a></a>').attr('href', href)
                        );
                    $('#list').append(li);
                    li.tooltip();
                }
                $('#sum_active').text(sumActive);
                $('#sum_inactive').text(sumInactive);
                $('#sum_disabled').text(sumDisabled);
                $('#result').show();
                $('#progress_area').hide();
            },
            error: function() {
                $('#progress_area').text('Wystąpił problem');
            }
        } );
    } );
</script>
</body>
</html>
