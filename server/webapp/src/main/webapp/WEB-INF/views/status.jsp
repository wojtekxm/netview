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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="/js/status.js"></script>
    <link rel="stylesheet" href="/css/style.css">
    <link href="css/simple-sidebar.css" rel="stylesheet">
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
                <li> <a href="#menu-toggle" id="menu-toggle">Filtry</a></li>
                <%--<li><a href="/all-units">Jednostki</a></li>--%>
                <%--<li><a href="/unitsbuildings">Jedn. Bud.</a></li>--%>
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

<div id="wrapper">
    <!-- Sidebar -->
    <div id="sidebar-wrapper">
        <ul class="sidebar-nav" style="background-color: #080b08;font-size: 16px;">
            <li class="sidebar-brand">
                <a href="#">
                    Filtrowanie
                </a>
            </li>
            <li>
                <a href="#">Kontrolery</a>
            </li>
            <li>
                <a href="#">Budynki</a>
            </li>
            <li>
                <a href="#">Stan</a>
            </li>
            <li>
                <a href="#">Najwięcej użytkowników</a>
            </li>
            <li>
                <a href="#">Najmniej użytkowników</a>
            </li>
            <li>
                <a href="#">Pozdrawiam</a>
            </li>
            <li>
                <a href="#">Kaceper</a>
            </li>
        </ul>
    </div>
    <!-- /#sidebar-wrapper -->


<div id="all" style="margin: 0">
    <div id="container">
        <div id="content">
            <ul class="view" style="z-index: 1000;top:0;">
                <li>
                    <div style="height: 10px;"></div>
                    <div id="wydzial"><div style="border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;"><span class="glyphicon glyphicon-th"></span> Wszystkie kontrolery</div></div>
                    <ul id="devices" class="panel panel-default" style="min-height:420px!important;padding: 4px;border: 1px solid #e0e0e0;list-style-type: none;"><div id="progress_area"></div></ul>
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
</div>

<script>
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
</script>

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

            success: function(listDtoOfCurrentDeviceStateDto) {
                if(!listDtoOfCurrentDeviceStateDto.success) {
                    err();
                    return;
                }
                devices = listDtoOfCurrentDeviceStateDto.list;
                e();
            },
            error: err
        });
        function err() {
            $('#progress_area').show();
        }
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
            var currentDeviceStateDto = devices[i];
            var state2400 = currentDeviceStateDto.frequencySurvey['2400'];
            var state5000 = currentDeviceStateDto.frequencySurvey['5000'];
            if(typeof state2400 === 'undefined') {
                var sum = 0;
                var isEnabled = false;
                var time = 0;
            }
            else {
                var sum = state2400.clients;
                var isEnabled = state2400.enabled;
                var time = state2400.timestamp;
            }
            var h = "/device?id=" + currentDeviceStateDto.id;
            var clazz= '';

            if (isEnabled == true) {
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
            } else if(isEnabled == false){
                clazz = "greyDiode";
                sum='-';
                off++;
            }
            var line = $('<li></li>').addClass(clazz)
                .attr('title', currentDeviceStateDto.name)
                .attr('data-toggle', 'tooltip')
                .append(
                    $('<a>'+sum+'</a>').attr('href', h).attr('style',style)
                );
            if(active+inactive+off != 0){
                $('#devices').append(line);
                $('#progress_area').hide(500);
            }else{
                $('#progress_area').show();
            }

            line.tooltip();
        }

        all = active+inactive+off;

        $('#countActive').text(active);
        $('#countInactive').text(inactive);
        $('#countOff').text(off);
        $('#countAll').text(all);

        var date = new Date(time*1000);
        var n = date.toLocaleString();
        $('#data').replaceWith('Ostatnie badanie sieci przeprowadzono:   ' + n);
    }

    $('#progress_area').show();

    allDevices();
    inter = setInterval('allDevices()', 30000);
</script>

<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });
</script>


</body>
</html>