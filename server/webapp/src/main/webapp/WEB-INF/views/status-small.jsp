<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<!-- kill me pls -->
<div style="margin-top:100px"></div>
<!-- I'm not a solution -->

<div class="container">
    <div style="height: 10px;"></div>
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
    <em>urządzenia nie wspierające 2,4 GHz są tutaj wyświetlane jak wyłączone</em>
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
            url: '/api/device/info/all',
            dataType: 'json',
            success: function(listDtoOfCurrentDeviceStateDto) {
                if(!listDtoOfCurrentDeviceStateDto.success) {
                    err();
                    return;
                }
                var i;
                var sumActive = 0;
                var sumInactive = 0;
                var sumDisabled = 0;
                var arr = listDtoOfCurrentDeviceStateDto.list;
                for(i = 0; i < arr.length; i++) {
                    var currentDeviceStateDto = arr[i];
                    var sur2400 = currentDeviceStateDto.frequencySurvey['2400'];
                    if(typeof sur2400 === 'undefined') {
                        sur2400 = {
                            "timestamp": 0,
                            "clients": 0,
                            "enabled": false
                        };
                    }
                    var clazz;
                    if(sur2400.enabled) {
                        if(sur2400.clients > 0) {
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
                    var href = '/device/' + currentDeviceStateDto.id;
                    var li = $('<li></li>').addClass(clazz)
                        .attr('title', currentDeviceStateDto.name)
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
            error: err
        } );
        function err() {
            $('#progress_area').text('Wystąpił problem');
        }
    } );
</script>
</body>
</html>
