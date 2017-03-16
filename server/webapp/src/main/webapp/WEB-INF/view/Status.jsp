<%@ page import="zesp03.data.row.UserRow" %>
<%@ page import="zesp03.dto.DeviceStateDto" %>
<%@ page import="zesp03.filter.AuthenticationFilter" %>
<%@ page import="zesp03.servlet.DeviceServlet" %>
<%@ page import="zesp03.servlet.StatusServlet" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    List<DeviceStateDto> list = (List<DeviceStateDto>) request.getAttribute(StatusServlet.allDevicesString);
    System.out.println("Number of devices: "+list.size());
    UserRow userRow = (UserRow) request.getAttribute(AuthenticationFilter.ATTR_USERROW);
//    int surveyTime=list.get(0).getLastSurveyTimestamp();
//    Long longSurveyTime = new Long(surveyTime);
//    longSurveyTime=longSurveyTime*1000;
%>
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
    <link rel="stylesheet" href="/css/style.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
</head>
<body>
<%
    if(session.getAttribute("username")==null)
    {
        response.sendRedirect("LoginPage.jsp");
    }
%>
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <ul class="nav nav-pills pull-left" style="padding-top: 3px;border-radius: 10px;padding-left:7px;font-size: 17px;">
            <li role="presentation" class="active"><a href="/"><span class="glyphicon glyphicon-home"></span>  Strona główna</a></li>
        </ul>
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav nav-pills pull-left" style="padding-top: 3px;font-size: 17px;display:block;margin-left:80px;">
                <li role="presentation" style="max-height:50px;"><a href="/make-survey">Nowe badanie</a></li>
                <li role="presentation"><a href="/all-controllers">Kontrolery</a></li>
                <li role="presentation"><a href="/all-users">Użytkownicy</a></li>
                <li role="presentation"><a href="/api/all-devices">Urządzenia</a></li>
                <li role="presentation"><a href="/building">Budynki</a></li>
                <li role="presentation">
                    <form class="navbar-form nav-pills" style="padding-top: 2px;margin-top:2px;">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Szukaj...">
                    </div>
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
                </form>
                </li>
            </ul>
            <ul class="nav nav-pills pull-right" style="padding-top: 3px;padding-right:3px;font-size: 17px;display: inline;">
                <li role="presentation"><a href="/account"><span class="glyphicon glyphicon-user"></span>  Mój profil</a></li>
                <li role="presentation"><a href="/logout"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>
<div id="all" class="container-fluid">
    <div id="container">
        <div class="welcome">
            <div class="tittle"><img src="/images/icon.ico" style="padding-bottom: 5px;"> &nbsp; NETWORK-MONITOR</div>
            <div class="user">zalogowany: <%= userRow.getName() %>
            </div>
            <div class="logo"><img src="/images/logooWhite.jpg"></div>
        </div>
        <div id="content">
            <ul class="view" style="z-index: 1000;top:0;">
                <li>
                    <div id="wydzial"><div style="border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;"><span class="glyphicon glyphicon-th"></span> Wszystkie kontrolery</div></div>
                    <ul id="devices" class="panel panel-default" style="padding: 4px;border: 1px solid #e0e0e0;list-style-type: none;">
                        <%
                            int sumActive = 0;
                            int sumInactive = 0;
                            int sumDisabled = 0;
                            for (final DeviceStateDto info : list) {
                                int sumUsers = info.getClientsSum();
                                String clazz;
                                if (info.isEnabled()) {
                                    if (info.getClientsSum() > 0 && info.getClientsSum() <= 10) {
                                        clazz = "greenDiode1";
                                        sumActive++;
                                    } else if (info.getClientsSum() > 10 && info.getClientsSum() <= 30) {
                                        clazz = "greenDiode2";
                                        sumActive++;
                                    } else if (info.getClientsSum() > 30 && info.getClientsSum() <= 47) {
                                        clazz = "greenDiode3";
                                        sumActive++;
                                    } else if (info.getClientsSum() > 47) {
                                        clazz = "greenDiode4";
                                        sumActive++;
                                    }
                                    else {
                                        clazz = "redDiode";
                                        sumInactive++;
                                    }
                                }
                                else {
                                    clazz = "greyDiode";
                                    sumDisabled++;
                                }

                                final String h = "/device?" + DeviceServlet.GET_ID + "=" + info.getId();
                                String t = info.getName();
                                if(info.isEnabled()){
                                    if (info.getDescription() != null) t += "<br>opis: " + info.getDescription();
                                        %><li class="<%= clazz %>" title="<%= t %>" data-toggle="tooltip" data-html="true"><a href="<%= h %>" style="text-decoration: none; color: white;"><%= sumUsers %></a></li
                                        ><%
                                    }else{
                                        if (info.getDescription() != null) t += "<br>opis: " + info.getDescription();
                                        %><li class="<%= clazz %>" title="<%= t %>" data-toggle="tooltip" data-html="true"><a href="<%= h %>" style="text-decoration: none; color: white;">-</a></li
                                        ><%
                                    }
                        }
                        %>
                    </ul>
                </li>
            </ul>
        </div>

        <div class="panel panel-default" style="border-radius: 10px;">
            <div class="panel-heading">
                <div id="data"></div><h3 class="panel-title" style="font-size: 17px;color:black; padding-top: 12px;"><span class="glyphicon glyphicon-th-large"></span> Pokaż tylko urządzenia ( Kliknij w wybrany stan ) :</h3>
            </div>
            <div class="panel-body">
                <div class="btn-group btn-group-justified" role="group" aria-label="...">
                    <div class="btn-group" role="group" onclick="onlyGreen()">
                        <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display:table-cell; font-size:18px;"><div id="greenDiode"></div><span id="countActive">&emsp;aktywne: &nbsp;<%= sumActive %>&emsp;</span></div></button>
                    </div>
                    <div class="btn-group" role="group" onclick="onlyRed()">
                        <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display:table-cell; font-size:18px;"><div id="redDiode"></div><span id="countInactive">&emsp;nieaktywne: &nbsp;<%= sumInactive %>&emsp;</span></div></button>
                    </div>
                    <div class="btn-group" role="group" onclick="onlyGrey()">
                        <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display: table-cell; font-size:18px;"><div id="greyDiode"></div><span id="countOff">&emsp;wyłączone: &nbsp;<%= sumDisabled %>&emsp;</span></div></button>
                    </div>
                    <div class="btn-group" role="group" onclick="allDevices();inter = setInterval('allDevices()', 10000);">
                        <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display: table-cell; font-size:18px;"><div></div><span class="glyphicon glyphicon-equalizer"></span><span id="countAll">&emsp;Wszystkie: &nbsp;<%= sumDisabled + sumActive + sumInactive %>&emsp;</span></div></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>



<script>
    var devices = new Array();
    var inter;

    function allDevices()
    {
        $.ajax({
            type: 'GET',
            url: '/api/all-devices',
            dataType: 'json',

            success: function(data) {
                devices = data;
                e();
            },
        });
    }

    function e(){
        var active=0;
        var inactive=0;
        var off=0;
        var all=0;
        var line = '';

        $("#devices li").remove();

        for(var i = 0; i< devices.length; i++){
            var h = "/device?id=" + devices[i].id;
            var t = devices[i].name;
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
            line += '<li class="' + clazz + '" title="' + t + '" data-toggle="tooltip" data-html="true"><a href="' + h + '" style="list-style-type: none;color:white;text-decoration:none;">' + sum + '</a></li>';
        }

        all = active+inactive+off;

        document.getElementById('countActive').innerHTML='<span>&emsp;aktywne: &nbsp;'+active+'&emsp;</span>';
        document.getElementById('countInactive').innerHTML='<span>&emsp;nieaktywne: &nbsp;'+inactive+'&emsp;</span>';
        document.getElementById('countOff').innerHTML='<span>&emsp;wyłączone: &nbsp;'+off+'&emsp;</span>';
        document.getElementById('countAll').innerHTML='<span>&emsp;wszystkie: &nbsp;'+all+'&emsp;</span>';

        $("#devices").html(line);

        $(function () {
            $('[data-toggle="tooltip"]').tooltip();
        })
    };

    allDevices();
    inter = setInterval('allDevices()', 10000);

</script>

<script>
    function loadDate() {
        var devices = new Array();

        $.ajax({
            type: 'GET',
            url: '/api/all-devices',
            dataType: 'json',

            success: function(data) {
                devices=data;
                var date = new Date(devices[0].lastSurveyTimestamp*1000);
                var n = date.toLocaleString();
                $('#data').html('Ostatnie badanie sieci przeprowadzono:     ' + n);
            }
        });
    }

    loadDate();
    setInterval('loadDate()', 10000);
</script>

<script>
    function onlyGreen()
    {
        $.ajax({
            type: 'GET',
            url: '/api/all-devices',
            dataType: 'json',

            success: function(data) {
                devices = data;
                green();
            },
        });
    }


    function green(){
        clearInterval(inter);
        var line = '';
        var active=0;
        var inactive=0;
        var off=0;
        var all=0;

        $("#devices li").remove();

        for(var i = 0; i< devices.length; i++){
            var h = "/device?id=" + devices[i].id;
            var t = devices[i].name;
            var clazz= '';
            var sum = devices[i].clientsSum;

                if (devices[i].enabled == true) {
                    if (sum > 0 && sum <= 10) {
                        clazz = "greenDiode1";
                        active++;
                        line += '<li class="' + clazz + '" title="' + t + '" data-toggle="tooltip" data-html="true"><a href="' + h + '" style="list-style-type: none;color:white;text-decoration:none;">' + sum + '</a></li>';
                    } else if (sum > 10 && sum <= 30) {
                        clazz = "greenDiode2";
                        active++;
                        line += '<li class="' + clazz + '" title="' + t + '" data-toggle="tooltip" data-html="true"><a href="' + h + '" style="list-style-type: none;color:white;text-decoration:none;">' + sum + '</a></li>';
                    } else if (sum > 30 && sum <= 47) {
                        clazz = "greenDiode3";
                        active++;
                        line += '<li class="' + clazz + '" title="' + t + '" data-toggle="tooltip" data-html="true"><a href="' + h + '" style="list-style-type: none;color:white;text-decoration:none;">' + sum + '</a></li>';
                    } else if (sum > 47) {
                        clazz = "greenDiode4";
                        active++;
                        line += '<li class="' + clazz + '" title="' + t + '" data-toggle="tooltip" data-html="true"><a href="' + h + '" style="list-style-type: none;color:white;text-decoration:none;">' + sum + '</a></li>';
                    } else if (sum == 0) {
                        clazz = "redDiode";
                        inactive++;
                    }
                } else if(devices[i].enabled == false){
                    clazz = "greyDiode";
                    sum='-';
                    off++;
                }
        }

        all = active+inactive+off;

        document.getElementById('countActive').innerHTML='<span>&emsp;aktywne: &nbsp;'+active+'&emsp;</span>';
        document.getElementById('countInactive').innerHTML='<span>&emsp;nieaktywne: &nbsp;'+inactive+'&emsp;</span>';
        document.getElementById('countOff').innerHTML='<span>&emsp;wyłączone: &nbsp;'+off+'&emsp;</span>';
        document.getElementById('countAll').innerHTML='<span>&emsp;wszystkie: &nbsp;'+all+'&emsp;</span>';

        $("#devices").html(line);

        $(function () {
            $('[data-toggle="tooltip"]').tooltip();
        })
    };
</script>

<script>
    function onlyRed()
    {
        $.ajax({
            type: 'GET',
            url: '/api/all-devices',
            dataType: 'json',

            success: function(data) {
                devices = data;
                red();
            },
        });
    }


    function red(){
        clearInterval(inter);
        var line = '';
        var active=0;
        var inactive=0;
        var off=0;
        var all=0;

        $("#devices li").remove();

        for(var i = 0; i< devices.length; i++){
            var h = "/device?id=" + devices[i].id;
            var t = devices[i].name;
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
                    line += '<li class="' + clazz + '" title="' + t + '" data-toggle="tooltip" data-html="true"><a href="' + h + '" style="list-style-type: none;color:white;text-decoration:none;">' + sum + '</a></li>';
                }
            } else if(devices[i].enabled == false){
                clazz = "greyDiode";
                sum='-';
                off++;
            }
        }

        all = active+inactive+off;

        document.getElementById('countActive').innerHTML='<span>&emsp;aktywne: &nbsp;'+active+'&emsp;</span>';
        document.getElementById('countInactive').innerHTML='<span>&emsp;nieaktywne: &nbsp;'+inactive+'&emsp;</span>';
        document.getElementById('countOff').innerHTML='<span>&emsp;wyłączone: &nbsp;'+off+'&emsp;</span>';
        document.getElementById('countAll').innerHTML='<span>&emsp;wszystkie: &nbsp;'+all+'&emsp;</span>';

        $("#devices").html(line);

        $(function () {
            $('[data-toggle="tooltip"]').tooltip();
        })
    };
</script>

<script>
    function onlyGrey()
    {
        $.ajax({
            type: 'GET',
            url: '/api/all-devices',
            dataType: 'json',

            success: function(data) {
                devices = data;
                grey();
            },
        });
    }


    function grey(){
        clearInterval(inter);
        var active=0;
        var inactive=0;
        var off=0;
        var all=0;
        var line = '';

        $("#devices li").remove();

        for(var i = 0; i< devices.length; i++){
            var h = "/device?id=" + devices[i].id;
            var t = devices[i].name;
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
                line += '<li class="' + clazz + '" title="' + t + '" data-toggle="tooltip" data-html="true"><a href="' + h + '" style="list-style-type: none;color:white;text-decoration:none;">' + sum + '</a></li>';
            }
        }

        all = active+inactive+off;

        document.getElementById('countActive').innerHTML='<span>&emsp;aktywne: &nbsp;'+active+'&emsp;</span>';
        document.getElementById('countInactive').innerHTML='<span>&emsp;nieaktywne: &nbsp;'+inactive+'&emsp;</span>';
        document.getElementById('countOff').innerHTML='<span>&emsp;wyłączone: &nbsp;'+off+'&emsp;</span>';
        document.getElementById('countAll').innerHTML='<span>&emsp;wszystkie: &nbsp;'+all+'&emsp;</span>';

        $("#devices").html(line);

        $(function () {
            $('[data-toggle="tooltip"]').tooltip();
        })
    };
</script>

<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    })
</script>


</body>
</html>