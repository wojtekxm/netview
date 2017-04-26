<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dodawanie budynku</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
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
                <li><a style="background-color: black;" href="/"><span class="glyphicon glyphicon-home"></span></a></li>
                <li><a href="/all-controllers">Kontrolery</a></li>
                <li><a href="/all-users">Użytkownicy</a></li>
                <li><a href="/all-devices">Urządzenia</a></li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">Lokalizacje<span class="caret"></span></a>
                    <ul class="dropdown-menu"  style="background-color: #080b08;">
                        <li><a href="/all-buildings">Budynki</a></li>
                        <li><a href="/all-units">Jednostki</a></li>
                        <li><a href="/unitsbuildings">Jedn. Bud.</a></li>
                    </ul>
                </li>
            </ul>
            <form method="get" action="/search" class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" name="query" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 180px!important;">
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

<div id="strona">
    <div id="kontent">
        <div class="form-group">
            <form method="post" action="/api/building/create">
                <input type="text" class="form-control" placeholder="Nazwa Budynku"
                       id="new_name" required="required" name="name">
                <input type="text" class="form-control" placeholder="Kod budynku"
                       id="new_code" required="required" name="code">
                <input type="text" class="form-control" placeholder="Ulica"
                       id="new_street" name="street">
                <input type="text" class="form-control" placeholder="Miasto"
                       id="new_city" name="city">
                <input type="text" class="form-control" placeholder="Kod pocztowy"
                       id="new_postalCode" name="postalCode">
                <input type="text" class="form-control" placeholder="Numer"
                       id="new_number" name="number">
                <input type="text" class="form-control" placeholder="Szerokosc geograficzna"
                       id="new_latitude" required="required" name="latitude">
                <input type="text" class="form-control" placeholder="Wysokosc geograficzna"
                       id="new_longitude" required="required" name="longitude" style="margin-bottom: inherit;">
                <input type="submit" value="Dodaj budynek" id="btn_submit" class="btn btn-primary btn-default btn-lg active">
                <div id="change_progress" style="min-height:38px; min-width:60px">
                    <div class="progress-loading"></div>
                </div>
            </form>

            <div class="form-group">
                <div class="col-sm-12">
                    <div id="result_success"></div>
                    <div id="result_error"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/notify.js"></script>

<script>

    $(document).ready(function () {
        var btnSubmit = $('#btn_submit');
        btnSubmit.click(function () {
            btnSubmit.prop('disabled', true);
            var createBuildingDto = {
                "name": $('#new_name').val(),
                "code": $('#new_code').val(),
                "street": $('#new_street').val(),
                "city": $('#new_city').val(),
                "postalCode": $('#new_postalCode').val(),
                "number": $('#new_number').val(),
                "latitude": $('#new_latitude').val(),
                "longitude": $('#new_longitude').val()

            };
            progress.load(
                'post',
                '/api/building/create',
                '#change_progress',
                function( response ) {
                    btnSubmit.prop( 'disabled', false );
                    notify.success( '#result_success', 'Budynek został dodany.' );
                },
                function( response ) {
                    btnSubmit.prop( 'disabled', false );
                    notify.danger( '#result_error', 'Nie udało się dodać budynku.'); //(response == null ||  response.error == null ||  response.error == '' ) ? 'Błąd operacji' : response.error  );
                },
                createBuildingDto
            );

        });
    });


</script>
</body>
</html>