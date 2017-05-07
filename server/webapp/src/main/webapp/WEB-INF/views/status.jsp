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
    <link rel="stylesheet" href="/css/bootstrap-toggle.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/bootstrap-multiselect.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/style.css">
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
                <li><a href="/settings"><span class="glyphicon glyphicon-wrench"></span>  Ustawienia</a></li>
                <li><a href="/account"><span class="glyphicon glyphicon-user"></span>  <c:out value="${loggedUser.name}"/></a></li>
                <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div style="height: 80px;"></div>
    <div class="panel panel-default">
        <div class="panel-body" style="background-color: #f8fafe;">
            <div style="font-size: 17px; display: inline-block;"><span class="glyphicon glyphicon-cog"></span> Aktualny stan urządzeń:</div>
        </div>
    </div>
    <div style="margin-bottom: 5px;">
        <input type="checkbox" id="toggleFrequency" data-toggle="toggleFrequency" data-on="5 GHz" data-off="2,4 GHz" data-onstyle="danger" data-offstyle="warning">
        <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#filters">
            <span class="glyphicon glyphicon-arrow-down" style="margin: 0;padding: 0;"></span> Filtrowanie
        </button>
        <button id="back" type="button" class="btn btn-success" onclick="resetFilters();"><span class="glyphicon glyphicon-refresh"></span> Zresetuj filtry</button>
    </div>
    <div id="filters" class="collapse">
        <div class="panel panel-default">
            <div class="panel-body">
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
                <div style="border-top: 1px ridge gainsboro; padding-top:10px; margin-top: 10px;">
                    <button id="top_15" type="button" class="btn btn-default" style="">15 najlepszych urządzeń</button>
                    <button id="worst_15" type="button" class="btn btn-default" style="margin-right: 4px;">15 najgorszych urządzeń</button>
                </div>
                <div class="form-group" style="margin-top: 20px;">
                    <div id="result_success"></div>
                    <div id="result_error"></div>
                </div>
            </div>
        </div>
    </div>

    <div style="height: 10px;"></div>
    <ul class="view" style="z-index: 1000;top:0;">
        <li>
            <ul id="devices" class="panel panel-default" style="padding: 4px;border: 1px solid #e0e0e0;list-style-type: none;"><div id="progress_area"></div></ul>
        </li>
    </ul>

    <div class="panel panel-default">
        <div class="panel-heading">
            <span class='glyphicon glyphicon-time'></span><div id="data_tittle"></div> &nbsp;&nbsp;<div id="data"></div>
        </div>
        <div class="panel-body">
            <div id="countActive" style="display: inline;"></div><br>
            <div id="countInactive" style="display: inline;"></div><br>
            <div id="countOff" style="display: inline;"></div><br>
            <div id="countAll" style="display: inline;"></div><br>
        </div>
    </div>
</div>

<script type="text/javascript" src="/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="/js/bootstrap-3.3.7.min.js"></script>
<script type="text/javascript" src="/js/bootstrap-toggle.min.js"></script>
<script type="text/javascript" src="/js/bootstrap-multiselect.js"></script>
<script type="text/javascript" src="/js/status.js"></script>
<script type="text/javascript" src="/js/progress.js"></script>
<script src="/js/notify.js"></script>

<%--<script>--%>
    <%--$(document).ready(function(){--%>
        <%--$.ajax({--%>
            <%--type: 'GET',--%>
            <%--url: '/api/login',--%>
            <%--dataType: 'json',--%>

            <%--success: function() {--%>
                <%--alert("");--%>
            <%--},--%>
            <%--error: err--%>
        <%--});--%>
    <%--});--%>
<%--</script>--%>

<script type="text/javascript">
    function resetFilters(){
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
        filter();
    }
</script>


<script type="text/javascript">
    var filterChoice = "filter";
    var frequency = "2400";
    var option = "";
    var ifFilter = true;

    $('#top_15').click(function(){
        filterChoice = "top";
        option = "best";
        topDevices(option);
    });
    $('#worst_15').click(function(){
        filterChoice = "worst";
        option = "worst";
        topDevices(option);
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
            $('#progress_area').remove();

        line.tooltip();
    }

    function topDevices(option){
        $.ajax({
            type: 'GET',
            url: '/api/device/info/all',
            dataType: 'json',

            success: function(listDtoOfCurrentDeviceStateDto) {
                if(!listDtoOfCurrentDeviceStateDto.success) {
                    err();
                    return;
                }
                devices = listDtoOfCurrentDeviceStateDto.list;
                var freq2400 = new Array();
                var freq5000 = new Array();

                for(var i = 0; i< devices.length; i++){
                    var currentDeviceStateDto = devices[i];
                    var state2400 = currentDeviceStateDto.frequencySurvey['2400'];
                    var state5000 = currentDeviceStateDto.frequencySurvey['5000'];
                    if(frequency == "2400"){
                        if(typeof state2400 === 'undefined') {
                            continue;
                        }
                        else if(state2400.enabled == true){
                            freq2400.push(currentDeviceStateDto);
                        }
                    }else if(frequency == "5000"){
                        if(typeof state5000 === 'undefined') {
                            continue;
                        }
                        else if(state5000.enabled == true){
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
                    $('#countActive').text("15 urządzeń z największą liczbą użytkowników");
                    $('#countInactive').css("display", "none");
                    $('#countOff').css("display", "none");
                    $('#countAll').css("display", "none");
                }else if(option == "worst"){
                    $('#countActive').text("15 urządzeń z najmniejszą liczbą użytkowników");
                    $('#countInactive').css("display", "none");
                    $('#countOff').css("display", "none");
                    $('#countAll').css("display", "none");
                }



            },
            error: function(){
                $('#data').remove();
                $('#data_tittle').replaceWith(" &nbsp;Wystąpił błąd podczas pobierania danych");
            }
        });
    }
</script>



<script type="text/javascript">
    function filter(){
        clearInterval(inter);
        var value = "";

        $("#devices li").remove();

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
                notify.danger('#result_error', 'Nie wybrano żadnego stanu');
            }
        }

        if(controllers.length == 0){
            if(ifFilter == true) {
                notify.danger('#result_error', 'Nie wybrano żadnego kontrolera');
            }
        }

        if(buildings.length == 0){
            if(ifFilter == true) {
                notify.danger('#result_error', 'Nie wybrano żadnego budynku');
            }
        }

        var ac = 0;
        var inac = 0;
        var of = 0;
        var al = 0;



        for(var j=0;j<devices.length;j++){
            var currentDeviceStateDto = devices[j];
            var state2400 = currentDeviceStateDto.frequencySurvey['2400'];
            var state5000 = currentDeviceStateDto.frequencySurvey['5000'];
            if (frequency == "2400") {
                if (typeof state2400 === 'undefined') {
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
            } else if (frequency == "5000") {
                if (typeof state5000 === 'undefined') {
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


            for(var i=0;i<controllers.length;i++){
                for(var k=0;k<states.length;k++) {
                    if (controllers[i] == devices[j].controllerId.toString()) {
                        var active = 0;
                        var inactive = 0;
                        var off = 0;
                        var all = 0;
                        var style = 'list-style-type: none;color:white;text-decoration:none;';

                        var h = "/device?id=" + currentDeviceStateDto.id;
                        var clazz = '';

                        if (isEnabled == true) {
                            if(states[k] == 'active') {
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
                            }else if(states[k] == 'inactive'){
                                if (sum == 0) {
                                    clazz = "redDiode";
                                    inactive++;
                                }
                            }
                        } else if (isEnabled == false) {
                            if(states[k] == 'off'){
                                clazz = "greyDiode";
                                sum = '-';
                                off++;
                            }
                        }
                        var line = $('<li></li>').addClass(clazz)
                            .attr('title', currentDeviceStateDto.name)
                            .attr('data-toggle', 'tooltip')
                            .append(
                                $('<a>' + sum + '</a>').attr('href', h).attr('style', style)
                            );

                        if (active + inactive + off != 0) {
                            if(isEnabled == true){
                                if(sum > 0){
                                    ac++;
                                }else if(sum == 0){
                                    inac++;
                                }
                            }else if(isEnabled == false){
                                of++;
                            }
                            $('#devices').append(line);
                            $('#progress_area').remove();
                        }

                        line.tooltip();

                    }
                }
            }
        }

        al = ac+inac+of;


        var date = new Date(time*1000);
        var n = date.toLocaleString();

        if(n == "Invalid Date"){
            n = "";
            setTimeout(function(){
                $('#data').replaceWith(n);
                $('#data_tittle').replaceWith(" &nbsp;Nie przeprowadzono jeszcze żadnych badań");
            }, 5000);
        }else {
            $('#data').replaceWith(n);
            $('#data_tittle').replaceWith(" &nbsp;Ostatnie badanie przeprowadzono:");
            $('#countInactive').css("display", "inline");
            $('#countOff').css("display", "inline");
            $('#countAll').css("display", "inline");
            $('#countActive').text('aktywne: ' + ac);
            $('#countInactive').text('nieaktywne: ' + inac);
            $('#countOff').text('wyłączone: ' + of);
            $('#countAll').text('wszystkie: ' + al);
        }


//        if(value == "" && controllerId == "" && stateId == ""){
//            inter = setInterval('allDevices()', 30000);
//            allDevices();
//        }
    }

    $('#filters_commit').click(function(){
        ifFilter = true;
        filterChoice = "filter";
        filter();
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

    $(document).ready(function(){
        var devices = new Array();
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
    $("#filters-button").click(function(e) {
        e.preventDefault();
        $(".filters").toggleClass("toggled");
    });
</script>

<script type="text/javascript">
    var frequency = "2400";
    var clicked = "all";
    var inter;

    function err() {
        $('#progress_area').show();
        setTimeout(function()
        {
            $('#progress_area').remove();
        }, 5000);
    }


    function allDevices()
    {
        $('[data-toggle="tooltip"]').tooltip('destroy');
        $.ajax({
            type: 'GET',
            url: '/api/device/info/all',
            dataType: 'json',

            success: function(listDtoOfCurrentDeviceStateDto) {
                if(!listDtoOfCurrentDeviceStateDto.success) {
                    err();
                    return;
                }
                devices = listDtoOfCurrentDeviceStateDto.list;
                e();
            },
            error: function(){
                $('#data').remove();
                $('#data_tittle').replaceWith(" &nbsp;Wystąpił błąd podczas pobierania danych");
            }
        });
    }

    function e(){
        clicked = "all";
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
                    $('<a>'+sum+'</a>').attr('href', h).attr('style',style)
                );
            if(active+inactive+off != 0){
                $('#devices').append(line);
                $('#progress_area').remove();

                var date = new Date(time*1000);
                var n = date.toLocaleString();

                if(n == "Invalid Date"){
                    n = "";
                    $('#data').replaceWith(n);
                    $('#data_tittle').replaceWith(" &nbsp;Nie przeprowadzono jeszcze żadnych badań");
                }
            }else{
                err();
            }

            line.tooltip();
        }

        all = active+inactive+off;



        var date = new Date(time*1000);
        var n = date.toLocaleString();

        if(n == "Invalid Date"){
            n = "";
            setTimeout(function(){
                $('#data').replaceWith(n);
                $('#data_tittle').replaceWith(" &nbsp;Nie przeprowadzono jeszcze żadnych badań");
            }, 4000);
        }else {
            $('#data').replaceWith(n);
            $('#data_tittle').replaceWith(" &nbsp;Ostatnie badanie przeprowadzono:");
            $('#countActive').text('aktywne: ' + active);
            $('#countInactive').text('nieaktywne: ' + inactive);
            $('#countOff').text('wyłączone: ' + off);
            $('#countAll').text('wszystkie: ' + all);
        }
    }

    err();

    allDevices();
    inter = setInterval('allDevices()', 30000);
</script>

<script type="text/javascript">
    $(function() {
        $('#toggleFrequency').change(function() {
            ifFilter = false;
            $('#devices li').remove();
            if(frequency == "2400"){
                frequency = "5000";
            }else if(frequency == "5000"){
                frequency = "2400";
            }

            if(filterChoice == "filter"){
                filter();
            }else if(filterChoice == "top"){
                option = "best";
                topDevices(option);
            }else if(filterChoice == "worst"){
                option = "worst";
                topDevices(option);
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