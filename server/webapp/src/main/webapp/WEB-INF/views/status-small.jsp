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
</head>
<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">Network Monitor</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="/make-survey">nowe badania</a></li>
            <li class="active"><a href="/status-small">urządzenia (mały widok)</a></li>
            <li><a href="/status">urządzenia (średni widok)</a></li>
            <li><a href="/all-controllers">kontrolery</a></li>
            <li><a href="/building">budynki</a></li>
            <li><a href="/logout">wyloguj</a></li>
        </ul>
    </div>
</nav>
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
