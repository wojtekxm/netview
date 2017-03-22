<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge; chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Status sieci</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <script src="//code.angularjs.org/1.2.20/angular.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="/js/status.js"></script>
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
                <div class="form-group" style="display:flex;">
                    <input type="text" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 150px!important;">
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

<div id="all">
    <div id="container">
        <%--<div class="welcome">--%>
            <%--<div class="tittle"><img src="/images/icon.ico" style="padding-bottom: 5px;"> &nbsp; NETWORK-MONITOR</div>--%>
            <%--<div class="user">zalogowany: <c:out value="${loggedUser.name}"/>--%>
            <%--</div>--%>
            <%--<div class="logo"><img src="/images/logooWhite.jpg"></div>--%>
        <%--</div>--%>
        <div id="content">
            <ul class="view" style="z-index: 1000;top:0;">
                <li>
                    <div id="wydzial"><div style="border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;"><span class="glyphicon glyphicon-th"></span> Wszystkie kontrolery</div></div>
                    <ul id="devices" class="panel panel-default" style="padding: 4px;border: 1px solid #e0e0e0;list-style-type: none;"><div id="progress_area"></div></ul>
                </li>
            </ul>
        </div>

        <div class="panel panel-default" style="border-radius: 10px;">
            <div class="panel-heading">
                <div id="data"></div><h3 class="panel-title" style="font-size: 17px;color:black; padding-top: 12px;"><span class="glyphicon glyphicon-th-large"></span> Pokaż tylko urządzenia ( Kliknij w wybrany stan ) :</h3>
            </div>
            <div class="panel-body">
                <div class="btn-group btn-group-justified" role="group" aria-label="...">
                    <div class="btn-group" role="group" onclick="onlyGreen();interGreen = setInterval('onlyGreen()', 10000);">
                        <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display:table-cell; font-size:18px;"><div id="greenDiode"></div>&emsp;aktywne: &nbsp;&emsp;<span id="countActive"></span></div></button>
                    </div>
                    <div class="btn-group" role="group" onclick="onlyRed();interRed = setInterval('onlyRed()', 10000);">
                        <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display:table-cell; font-size:18px;"><div id="redDiode"></div>&emsp;nieaktywne: &nbsp;&emsp;<span id="countInactive"></span></div></button>
                    </div>
                    <div class="btn-group" role="group" onclick="onlyGrey();interGrey = setInterval('onlyGrey()', 10000);">
                        <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display: table-cell; font-size:18px;"><div id="greyDiode"></div>&emsp;wyłączone: &nbsp;&emsp;<span id="countOff"></span></div></button>
                    </div>
                    <div class="btn-group" role="group" onclick="allDevices();inter = setInterval('allDevices()', 10000);">
                        <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display: table-cell; font-size:18px;"><div></div><span class="glyphicon glyphicon-equalizer"></span>&emsp;Wszystkie: &nbsp;&emsp;<span id="countAll"></span></div></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    var devices = new Array();
    var inter;

    function allDevices()
    {
        $('[data-toggle="tooltip"]').tooltip('destroy');
        $.ajax({
            type: 'GET',
            url: '/api/all-devices',
            dataType: 'json',

            success: function(data) {
                devices = data;
                e();
            },
            error: function() {
                $('#progress_area').text('Wystąpił problem');
            }
        });
    }

    function e(){
        clearInterval(interGreen);
        clearInterval(interRed);
        clearInterval(interGrey);
        var active=0;
        var inactive=0;
        var off=0;
        var all=0;
        var style='list-style-type: none;color:white;text-decoration:none;';

        $('#devices li').remove();

        for(var i = 0; i< devices.length; i++){
            var h = "/device?id=" + devices[i].id;
            var clazz= '';
            var sum = devices[i].clientsSum;

            if (devices[i].enabled == true) {
                if (sum > 0 && sum <= 10) {
                    clazz = "greenDiode1";
                    active++;
                } else if (sum > 10 && sum <= 30) {
                    clazz = "greenDiode2";
                    active++;
                } else if (sum > 30 && sum <= 47) {
                    clazz = "greenDiode3";
                    active++;
                } else if (sum > 47) {
                    clazz = "greenDiode4";
                    active++;
                } else if (sum == 0) {
                    clazz = "redDiode";
                    inactive++;
                }
            } else if(devices[i].enabled == false){
                clazz = "greyDiode";
                sum='-';
                off++;
            }
            var line = $('<li></li>').addClass(clazz)
                .attr('title', devices[i].name)
                .attr('data-toggle', 'tooltip')
                .append(
                    $('<a>'+sum+'</a>').attr('href', h).attr('style',style)
                );
            $('#devices').append(line);

            line.tooltip();
        }


//        $("#devices").fadeOut('slow');
//        $("#devices").fadeIn('slow');

        all = active+inactive+off;

        $('#countActive').text(active);
        $('#countInactive').text(inactive);
        $('#countOff').text(off);
        $('#countAll').text(all);

        $('#progress_area').hide();

        loadDate();
    }

    $('#progress_area').attr('style','font-size:18px;').text('Pobieranie danych...');

    allDevices();
    $("#devices").fadeIn('slow');
    inter = setInterval('allDevices()', 10000);
</script>

<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });
</script>


</body>
</html>