<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge; chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Status sieci</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/bootstrap-toggle.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/bootstrap-multiselect.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/notify.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' type='text/css'>
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
                <ul class="nav navbar-nav" style="padding-right:3px;font-size: 16px;">
                    <li><a style="background-color: black;padding-left:25px;padding-right: 20px;" href="/"><span class="glyphicon glyphicon-home"></span> &nbsp;NetView &nbsp;</a></li>
                    <c:if test="${loggedUser.role eq 'ROOT'}">  <li><a href="/all-controllers">Kontrolery</a></li>
                        <li><a href="/all-users">Użytkownicy</a></li>
                        <li><a href="/all-devices">Urządzenia</a></li>
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Lokalizacje<span class="caret"></span></a>
                            <ul class="dropdown-menu"  style="background-color: #080b08;">
                                <li><a href="/all-buildings">Budynki</a></li>
                                <li><a href="/all-units">Jednostki</a></li>
                            </ul>
                        </li>
                    </c:if>
                </ul>
            </ul>
            <c:if test="${loggedUser.role eq 'ROOT'}">  <form method="get" action="/search" class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" name="query" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 150px!important;">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
                </div>
            </form></c:if>
            <ul class="nav navbar-nav navbar-right" style="padding-right:3px;font-size: 16px;">
                <c:if test="${loggedUser.role eq 'ROOT'}">  <li style="margin-left: 0px!important;"><a href="/settings"><span class="glyphicon glyphicon-wrench"></span>  Ustawienia</a></li></c:if>
                <li><a href="/account"><span class="glyphicon glyphicon-user"></span>  <c:out value="${loggedUser.name}"/></a></li>
                <li><a href="/logout" style="margin-right: 10px;"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container" style="margin-top:80px">
    <div id="main_loading" class="later"></div>
    <div id="main_success" class="later">
        <div class="panel panel-default">
            <div class="panel-heading">
                <span class="glyphicon glyphicon-cog"></span> Aktualny stan urządzeń:
            </div>
            <div class="panel-body">
                <div class="buttons">
                    <div style="border: 1px solid gainsboro;padding:8px;border-radius: 3px;">
                        <button id="filtersToggle" class="btn btn-primary" type="button" data-toggle="collapse" data-target="#filters">
                            <span class="glyphicon glyphicon-arrow-down" style="margin: 0;padding: 0;"></span> Filtrowanie
                        </button>
                        <button id="back" type="button" class="btn btn-success" onclick="resetFilters();"><span class="glyphicon glyphicon-refresh"></span> Zresetuj filtry</button>
                        <input type="checkbox" id="toggleFrequency" data-toggle="toggleFrequency" data-on="5 GHz" data-off="2,4 GHz" data-onstyle="danger" data-offstyle="warning" data-width="100">
                        <div class = "pull-right" style="height: 35px; margin-right:5px;">
                            <div id="examine_loading" class="progress-space"></div>
                        </div>
                    </div>
                    <div id="filters" class="collapse">
                        <div style="margin-top: 15px!important; border: 1px solid gainsboro;padding:8px;border-radius: 3px;">
                            <select id="stan" multiple="multiple">
                                <option type="checkbox" class="s" value="active" selected="true">Aktywne</option>
                                <option type="checkbox" class="s" value="inactive" selected="true">Niektywne</option>
                                <option type="checkbox" class="s" value="off" selected="true">Wyłączone</option>
                            </select>
                            <select id="kontrolery" multiple="multiple"></select>
                            <select id="budynki" multiple="multiple"></select>
                            &nbsp;&nbsp;
                            <button id="filters_commit" type="button" class="btn btn-info" style="width: 150px;"><span class='glyphicon glyphicon-ok'></span> Filtruj &nbsp;</button>
                            <br>
                            <div style="border-top: 1px solid gainsboro; padding-top:10px; margin-top: 10px;">
                                <button id="top_15" type="button" class="btn btn-default" style="">15 najlepszych urządzeń</button>
                                <button id="worst_15" type="button" class="btn btn-default" style="margin-right: 4px;">15 najgorszych urządzeń</button>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="form-group">
                    <div id="result_error"></div>
                </div>
                <div id="result_error_main"></div>

                <ul class="view" style="margin:0px!important;z-index: 1000;top:0;">
                    <li>
                        <ul id="devices"></ul>
                    </li>
                </ul>

            </div>
        </div>


        <div class="panel panel-default">
            <div class="panel-heading" style="text-align: center;font-size: 15px!important;background-color: #2b2d2b;color:white;">
                <span class='glyphicon glyphicon-time' style="display: inline;"></span><div id="data_tittle" style="display:inline;margin-left: 6px;"></div> &nbsp;&nbsp;&nbsp; <div id="data" style="display:inline;"></div>
            </div>
            <div class="panel-body">
                <div style="text-align: center;height: 100%;">
                    <label><span class="label label-info" id="countAll" style="font-size: 16px;font-weight: normal"></span></label>
                    <label><span class="label label-success" id="countActive" style="font-size: 16px;font-weight: normal"></span></label>
                    <label><span class="label label-danger" id="countInactive" style="font-size: 16px;font-weight: normal"></span></label>
                    <label><span class="label label-default" id="countOff" style="font-size: 16px;font-weight: normal"></span></label>
                </div>
            </div>
        </div>
    </div>
    <div id="notify_layer" style="position: fixed; top: 100px;"></div>
</div>

<script type="text/javascript" src="/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="/js/bootstrap-3.3.7.min.js"></script>
<script type="text/javascript" src="/js/bootstrap-toggle.min.js"></script>
<script type="text/javascript" src="/js/bootstrap-multiselect.js"></script>
<script type="text/javascript" src="/js/status.js"></script>
<script type="text/javascript" src="/js/progress.js"></script>
<script src="/js/notify.js"></script>


<script type="text/javascript">
    function resetFilters(){
        btnWorst.prop('disabled', false);
        btnTop.prop('disabled', false);
        btnResetFilter.prop('disabled', true);
        $('option', $('#stan')).each(function(element) {
            $(this).removeAttr('selected').prop('selected', true);
        });
        $('#stan').multiselect('refresh');
        $('option', $('#kontrolery')).each(function(element) {
            $(this).removeAttr('selected').prop('selected', true);
        });
        $('#kontrolery').multiselect('refresh');
        $('option', $('#budynki')).each(function(element) {
            $(this).removeAttr('selected').prop('selected', true);
        });
        $('#budynki').multiselect('refresh');
        filterChoice = "filter";
        clearInterval(inter);
        getFilteredDevices();
        inter = setInterval('getFilteredDevices()', 30000);
    }
</script>

<script type="text/javascript">
    var inter;
    var filterChoice = "filter";
    var frequency = "2400";
    var option = "";
    var ifFilter = true;

    $('#top_15').click(function(){
        btnTop.prop('disabled', true);
        btnWorst.prop('disabled', false);
        clearInterval(inter);
        filterChoice = "top";
        option = "best";
        topDevices(option);
        inter = setInterval('topDevices(option)', 30000);
    });
    $('#worst_15').click(function(){
        btnWorst.prop('disabled', true);
        btnTop.prop('disabled', false);
        clearInterval(inter);
        filterChoice = "worst";
        option = "worst";
        topDevices(option);
        inter = setInterval('topDevices(option)', 30000);
    });

    function displayDevice(currentDeviceStateDto){

        var style='list-style-type: none;color:white;text-decoration:none;';

        var state2400 = currentDeviceStateDto.frequencySurvey['2400'];
        var state5000 = currentDeviceStateDto.frequencySurvey['5000'];

        if(frequency == "2400"){
            if(typeof state2400 === 'undefined') {
                return;
            }
            else {
                var sum = state2400.clients;
                var isEnabled = state2400.enabled;
            }
        }else if(frequency == "5000"){
            if(typeof state5000 === 'undefined') {
                return;
            }
            else {
                var sum = state5000.clients;
                var isEnabled = state5000.enabled;
            }
        }
        var h = "/device/" + currentDeviceStateDto.id;
        var clazz= '';

        if (isEnabled == true) {
            if (sum > 0 && sum <= 10) {
                clazz = "greenDiode1";
            } else if (sum > 10 && sum <= 30) {
                clazz = "greenDiode2";
            } else if (sum > 30 && sum <= 47) {
                clazz = "greenDiode3";
            } else if (sum > 47) {
                clazz = "greenDiode4";
            } else if (sum == 0) {
                clazz = "redDiode";
            }
        } else if(isEnabled == false){
            clazz = "redDiode";
            sum="-";
        }
        var line = $('<li></li>').addClass(clazz)
            .attr('title', currentDeviceStateDto.name)
            .attr('data-toggle', 'tooltip')
            .append(
                $('<a>'+sum+'</a>').attr('href', h).attr('style',style)
            );

        $('#devices').append(line);

        line.tooltip();
    }

    function topDevices(option) {
        $('[data-toggle="tooltip"]').tooltip('destroy');
        progress.loadGet(
            '/api/device/info/all',
            ['#examine_loading'], [], [],
            function(listDtoOfCurrentDeviceStateDto) {
                devices = listDtoOfCurrentDeviceStateDto.list;
                var freq2400 = new Array();
                var freq5000 = new Array();
                var time = 0;

                for(var i = 0; i< devices.length; i++){
                    var currentDeviceStateDto = devices[i];
                    var state2400 = currentDeviceStateDto.frequencySurvey['2400'];
                    var state5000 = currentDeviceStateDto.frequencySurvey['5000'];
                    if(frequency == "2400"){
                        if(typeof state2400 === 'undefined') {
                            continue;
                        }
                        else if(state2400.enabled == true){
                            time = state2400.timestamp;
                            freq2400.push(currentDeviceStateDto);
                        }
                    }else if(frequency == "5000"){
                        if(typeof state5000 === 'undefined') {
                            continue;
                        }
                        else if(state5000.enabled == true){
                            time = state5000.timestamp;
                            freq5000.push(currentDeviceStateDto);
                        }
                    }
                }

                if(frequency == "2400"){
                    if(option == "worst") {
                        freq2400.sort(function (a, b) {
                            return a.frequencySurvey['2400'].clients - b.frequencySurvey['2400'].clients;
                        });
                    }else if(option == "best") {
                        freq2400.sort(function (a, b) {
                            return b.frequencySurvey['2400'].clients - a.frequencySurvey['2400'].clients;
                        });
                    }
                }else if(frequency == "5000"){
                    if(option == "worst") {
                        freq5000.sort(function (a, b) {
                            return a.frequencySurvey['5000'].clients - b.frequencySurvey['5000'].clients;
                        });
                    }else if(option == "best") {
                        freq5000.sort(function (a, b) {
                            return b.frequencySurvey['5000'].clients - a.frequencySurvey['5000'].clients;
                        });
                    }
                }

                $('#devices li').remove();

                if(frequency == "2400"){
                    for(var i = 0; i < 15; i++){
                        displayDevice(freq2400[i]);
                    }
                }else if(frequency == "5000"){
                    for(var i = 0; i < 15; i++){
                        displayDevice(freq5000[i]);
                    }
                }

                if(option == "best"){
                    $('#countActive').css("display", "inline");
                    $('#countActive').text("15 urządzeń z największą liczbą użytkowników");
                    $('#countInactive').css("display", "none");
                    $('#countOff').css("display", "none");
                    $('#countAll').css("display", "none");
                }else if(option == "worst"){
                    $('#countActive').css("display", "none");
                    $('#countInactive').css("display", "inline");
                    $('#countInactive').text("15 urządzeń z najmniejszą liczbą użytkowników");
                    $('#countOff').css("display", "none");
                    $('#countAll').css("display", "none");
                }

                var date = new Date(time*1000);
                var n = date.toLocaleString();

                if(n == "Invalid Date"){
                    n = "";
                    $('#data').text(n);
                    $('#data_tittle').text("Nie przeprowadzono jeszcze żadnych badań");
                    btnFiltersToggle.prop('disabled', true);
                    btnResetFilter.prop('disabled', true);
                    $('#toggleFrequency').bootstrapToggle('disable');
                }else {
                    $('#data').text(n);
                    $('#data_tittle').text("Ostatnie badanie przeprowadzono:");
                }
            }
        );
    }
</script>

<script type="text/javascript">
    var btnFiltersToggle = $('#filtersToggle')
    var btnFilter = $('#filters_commit');
    var btnResetFilter = $('#back');
    var btnTop = $('#top_15');
    var btnWorst = $('#worst_15');

    function getFilteredDevices() {
        $('[data-toggle="tooltip"]').tooltip('destroy');
        progress.loadGet(
            '/api/device/info/all',
            ['#examine_loading'], [], [],
            function(listDtoOfCurrentDeviceStateDto) {
                devices = listDtoOfCurrentDeviceStateDto.list;
                filter();

                btnResetFilter.prop('disabled', false);
                btnFilter.prop('disabled', false);
            }
        );
    }


    function filter(){
        var value = "";

        var states = new Array();
        var stateId = "";
        $('.s :checkbox:checked').each(function(){
            stateId = $(this).attr('value');
            states.push(stateId);
        });


        var controllers = new Array();
        var controllerId = "";
        $('.c :checkbox:checked').each(function(){
            controllerId = $(this).attr('value');
            controllers.push(controllerId);
        });

        var buildings = new Array();
        var buildingId = "";
        $('.b :checkbox:checked').each(function(){
            buildingId = $(this).attr('value');
            buildings.push(buildingId);

        });


        if(states.length == 0){
            if(ifFilter == true){
                notify.danger('Nie wybrano żadnego stanu');
                clearInterval(inter);
                return;
            }
        }
        if(controllers.length == 0){
            if(ifFilter == true) {
                notify.danger('Nie wybrano żadnego kontrolera');
                clearInterval(inter);
                return;
            }
        }
        if(buildings.length == 0){
            if(ifFilter == true) {
                notify.danger('Nie wybrano żadnego budynku');
                clearInterval(inter);
                return;
            }
        }


        var active = 0;
        var inactive = 0;
        var off = 0;
        var all = 0;
        var ac = 0;
        var inac = 0;
        var of = 0;
        var al = 0;
        var sum = 0;
        var isEnabled = true;
        var time = 0;
        var style = 'list-style-type: none;color:white;text-decoration:none;';
        var clazz = '';
        var h = '';
        var currentDeviceStateDto = null;
        var state2400 = null;
        var state5000 = null;
        var allLines = $();



        var resultDevices = new Array();
        for(var j=0; j<devices.length; j++){
            if(controllers.length == controllersSize){
                resultDevices.push(devices[j]);
            }else{
                for(var s = 0; s < controllers.length; s++){
                    if (devices[j].controllerId !== null) {
                        if (controllers[s] == devices[j].controllerId.toString()) {
                            resultDevices.push(devices[j]);
                        }
                    }
                }
            }
        }

        var resultDevices2 = new Array();
        for(var j = 0; j < resultDevices.length; j++){
            if(buildings.length == buildingsSize){
                resultDevices2.push(resultDevices[j]);
            }else{
                for(var s = 0; s < buildings.length; s++){
                    if (resultDevices[j].buildingId !== null) {
                        if (buildings[s] == resultDevices[j].buildingId.toString()) {
                            resultDevices2.push(resultDevices[j]);
                        }
                    }
                }
            }
        }

        for(var j=0;j<resultDevices2.length;j++){
            currentDeviceStateDto = resultDevices2[j];
            state2400 = currentDeviceStateDto.frequencySurvey['2400'];
            state5000 = currentDeviceStateDto.frequencySurvey['5000'];
            if (frequency == "2400") {
                if (typeof state2400 === 'undefined') {
                    continue;
                }
                if(state2400 === null){
                    isEnabled = false;
                }
                else {
                    sum = state2400.clients;
                    isEnabled = state2400.enabled;
                    time = state2400.timestamp;
                }
            } else if (frequency == "5000") {
                if (typeof state5000 === 'undefined') {
                    continue;
                }
                if(state5000 === null){
                    isEnabled = false;
                }
                else {
                    sum = state5000.clients;
                    isEnabled = state5000.enabled;
                    time = state5000.timestamp;
                }
            }

            for(var s=0;s<states.length;s++){
                active = 0;
                inactive = 0;
                off = 0;

                style = 'list-style-type: none;color:white;text-decoration:none;';

                h = "/device/" + currentDeviceStateDto.id;

                if (isEnabled == true) {
                    if (states[s] == 'active') {
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
                        }
                    } else if (states[s] == 'inactive') {
                        if (sum == 0) {
                            clazz = "redDiode";
                            inactive++;
                        }
                    }
                } else if (isEnabled == false) {
                    if (states[s] == 'off') {
                        clazz = "greyDiode";
                        sum = '-';
                        off++;
                    }
                }
                if (active + inactive + off != 0) {
                    if (isEnabled == true) {
                        if (sum > 0) {
                            ac++;
                        } else if (sum == 0) {
                            inac++;
                        }
                    } else if (isEnabled == false) {
                        of++;
                    }
                    var line = $('<li></li>').addClass(clazz)
                        .attr('title', currentDeviceStateDto.name)
                        .attr('data-toggle', 'tooltip')
                        .append(
                            $('<a>' + sum + '</a>').attr('href', h).attr('style', style)
                        );
                    line.tooltip();
                    allLines = allLines.add(line);
                }
            }
        }

        $("#devices li").remove();
        $('#devices').append(allLines);

        al = ac + inac + of;

        if(al == 0){
            notify.danger('Brak wyników dla podanej konfiguracji');
            clearInterval(inter);
            return;
        }

        var date = new Date(time*1000);
        var n = date.toLocaleString();

        if(n == "Invalid Date"){
            n = "";
            $('#data').text(n);
            $('#data_tittle').text("Nie przeprowadzono jeszcze żadnych badań");
            btnFiltersToggle.prop('disabled', true);
            btnResetFilter.prop('disabled', true);
            $('#toggleFrequency').bootstrapToggle('disable');
        }else {
            $('#data').text(n);
            $('#data_tittle').text("Ostatnie badanie przeprowadzono:");
            $('#countActive').css("display", "inline");
            $('#countInactive').css("display", "inline");
            $('#countOff').css("display", "inline");
            $('#countAll').css("display", "inline");
            $('#countActive').text(' aktywne: ' + ac + ' ');
            $('#countInactive').text('nieaktywne: ' + inac);
            $('#countOff').text('wyłączone: ' + of);
            $('#countAll').text('wszystkie: ' + al);
        }
    }

    $('#filters_commit').click(function(){
        btnWorst.prop('disabled', false);
        btnTop.prop('disabled', false);
        btnFilter.prop('disabled', true);
        ifFilter = true;
        filterChoice = "filter";
        clearInterval(inter);
        getFilteredDevices();
        inter = setInterval('getFilteredDevices()', 30000);
    })
</script>

<script type="text/javascript">
    function compare(b1, b2) {
        var x = b1.name;
        if (x === null) x = '';
        var y = b2.name;
        if (y === null) y = '';
        return x.localeCompare(y);
    }

    var buildingsSize = 0;
    var controllersSize = 0;

    $(document).ready(function(){
        var buildings = new Array();
        var controllers = new Array();


        $.ajax({
            type: 'GET',
            url: '/api/building/info/all',
            dataType: 'json',

            success: function(listDtoOfBuildingDto){
                if(!listDtoOfBuildingDto.success){
                    return;
                }
                buildings = listDtoOfBuildingDto.list;
                buildings.sort(compare);

                $.each(buildings, function(index, item){
                    var opt = $('<option></option>', {
                        type: 'checkbox',
                        value: item.id,
                        text: item.name,
                        class: 'b',
                        selected: true
                    });
                    opt.appendTo(b);
                    b.multiselect('rebuild');
                })
                buildingsSize = buildings.length;
            }
        });

        $.ajax({
            type: 'GET',
            url: '/api/controller/details/all',
            dataType: 'json',

            success: function(listDtoOfControllerDetailsDto){
                if(!listDtoOfControllerDetailsDto.success){
                    return;
                }
                controllers = listDtoOfControllerDetailsDto.list;
                controllers.sort(compare);

                $.each(controllers, function(index, item){
                    var opt = $('<option></option>', {
                        type: 'checkbox',
                        value: item.id,
                        text: item.name,
                        class: 'c',
                        selected: true
                    });
                    opt.appendTo(c);
                    c.multiselect('rebuild');
                })
                controllersSize = controllers.length;
            }
        });


        $('#stan').multiselect({
            includeSelectAllOption: true,
            nonSelectedText:'Stan urządzeń',
            allSelectedText: 'Stan urządzeń',
            enableCaseInsensitiveFiltering: true,
            maxHeight: 300
        });
        var b = $('#budynki').multiselect({
            enableFiltering: true,
            includeSelectAllOption: true,
            nonSelectedText:'Budynki',
            allSelectedText: 'Budynki',
            enableCaseInsensitiveFiltering: true,
            maxHeight: 300
        });

        var c = $('#kontrolery').multiselect({
            enableFiltering: true,
            includeSelectAllOption: true,
            nonSelectedText:'Kontrolery',
            allSelectedText: 'Kontrolery',
            enableCaseInsensitiveFiltering: true,
            maxHeight: 300
        });
    })
</script>


<script type="text/javascript">

    $(document).ready( function(){
        progress.loadGet(
            '/api/device/info/all',
            ['#main_loading'], ['#main_success'], [],
            function(listDtoOfCurrentDeviceStateDto) {
                devices = listDtoOfCurrentDeviceStateDto.list;
                e();
            }, undefined, 'md'
        );
        inter = setInterval('allDevices()', 30000);
    });


    function allDevices()
    {
        $('[data-toggle="tooltip"]').tooltip('destroy');
        progress.loadGet(
            '/api/device/info/all',
            ['#examine_loading'], [], [],
            function(listDtoOfCurrentDeviceStateDto) {
                devices = listDtoOfCurrentDeviceStateDto.list;
                e();
            }, undefined, 'md'
        );
    }


    function e(){
        clearInterval(interGreen);
        clearInterval(interRed);
        clearInterval(interGrey);
        var active=0;
        var inactive=0;
        var off=0;
        var all=0;
        var style='text-align: center!important;';
        var allLines = $();


        for(var i = 0; i< devices.length; i++){
            var currentDeviceStateDto = devices[i];
            var state2400 = currentDeviceStateDto.frequencySurvey['2400'];
            var state5000 = currentDeviceStateDto.frequencySurvey['5000'];
            if(frequency == "2400"){
                if(typeof state2400 === 'undefined') {
                    continue;
                }
                if(state2400 === null){
                    var isEnabled = false;
                }
                else {
                    var sum = state2400.clients;
                    var isEnabled = state2400.enabled;
                    var time = state2400.timestamp;
                }
            }else if(frequency == "5000"){
                if(typeof state5000 === 'undefined') {
                    continue;
                }
                if(state5000 === null){
                    var isEnabled = false;
                }
                else {
                    var sum = state5000.clients;
                    var isEnabled = state5000.enabled;
                    var time = state5000.timestamp;
                }
            }
            var h = "/device/" + currentDeviceStateDto.id;
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
                    $('<a>'+sum+'</a>').attr('href', h).attr('style', style)
                );
            allLines = allLines.add(line);
            if(active+inactive+off != 0){
                var date = new Date(time*1000);
                var n = date.toLocaleString();

                if(n == "Invalid Date"){
                    n = "";
                    $('#data').text(n);
                    $('#data_tittle').text("Nie przeprowadzono jeszcze żadnych badań");
                    btnFiltersToggle.prop('disabled', true);
                    btnResetFilter.prop('disabled', true);
                    $('#toggleFrequency').bootstrapToggle('disable');
                }
            }

            line.tooltip();
        }
        $('#devices li').remove();
        $('#devices').append(allLines);

        all = active+inactive+off;



        var date = new Date(time*1000);
        var n = date.toLocaleString();

        if(n == "Invalid Date"){
            n = "";
            $('#data').text(n);
            $('#data_tittle').text("Nie przeprowadzono jeszcze żadnych badań");
            btnFiltersToggle.prop('disabled', true);
            btnResetFilter.prop('disabled', true);
            $('#toggleFrequency').bootstrapToggle('disable');
        }else {
            $('#data').text(n);
            $('#data_tittle').text("Ostatnie badanie przeprowadzono:");
            $('#countActive').text(' aktywne: ' + active + ' ');
            $('#countInactive').text('nieaktywne: ' + inactive);
            $('#countOff').text('wyłączone: ' + off);
            $('#countAll').text('wszystkie: ' + all);
        }
    }
</script>

<script type="text/javascript">
    $(function() {
        $('#toggleFrequency').change(function() {
            ifFilter = false;
            if(frequency == "2400"){
                frequency = "5000";
            }else if(frequency == "5000"){
                frequency = "2400";
            }

            if(filterChoice == "filter"){
                clearInterval(inter);
                getFilteredDevices();
                inter = setInterval('getFilteredDevices()', 30000);
            }else if(filterChoice == "top"){
                btnWorst.prop('disabled', false);
                clearInterval(inter);
                option = "best";
                topDevices(option);
                inter = setInterval('topDevices(option)', 30000);
            }else if(filterChoice == "worst"){
                btnTop.prop('disabled', false);
                clearInterval(inter);
                option = "worst";
                topDevices(option);
                inter = setInterval('topDevices(option)', 30000);
            }

        })
    })
</script>

<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });
</script>


</body>
</html>
