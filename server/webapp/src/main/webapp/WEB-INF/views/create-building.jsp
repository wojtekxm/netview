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
                        <li><a href="/unitsbuildings">Jedn. Bud.</a></li>
                    </ul>
                </li>
                <%--<li> <a href="#menu-toggle" id="menu-toggle">Filtry</a></li>--%>
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

<div style="margin-top:100px"></div>

<div id="strona">
    <div id="kontent">

            <form method="post" action="/api/building/create" class="form-horizontal">

                    <div class="form-group">
                        <label for="new_name" class="col-sm-2 control-label">Nazwa budynku:</label>
                        <div class="col-sm-3">
                <input type="text" class="form-control" placeholder="Wprowadź nazwę budynku"
                       id="new_name" required="required" name="name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="new_code" class="col-sm-2 control-label">Kod budynku:</label>
                        <div class="col-sm-3">
                <input type="text" class="form-control" placeholder="Wprowadź kod budynku"
                       id="new_code" required="required" name="code">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="new_street" class="col-sm-2 control-label">Ulica:</label>
                        <div class="col-sm-3">
                        <input type="text" class="form-control" placeholder="Wprowadź nazwę ulicy"
                           id="new_street" name="street">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="new_city" class="col-sm-2 control-label">Miasto:</label>
                        <div class="col-sm-3">
                    <input type="text" class="form-control" placeholder="Wprowadź nazwę miasta"
                           id="new_city" name="city">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="new_postalCode" class="col-sm-2 control-label">Kod pocztowy:</label>
                        <div class="col-sm-3">
                    <input type="text" class="form-control" placeholder="Wprowadź kod pocztowy"
                           id="new_postalCode" name="postalCode">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="new_number" class="col-sm-2 control-label">Numer:</label>
                        <div class="col-sm-3">
                        <input type="text" class="form-control" placeholder="Wprowadź numer"
                           id="new_number" name="number">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="new_latitude" class="col-sm-2 control-label">Szerokość geograficzna:</label>
                        <div class="col-sm-3">
                        <input type="text" class="form-control" placeholder="Wprowadź szerokosc geograficzną"
                           id="new_latitude" required="required" name="latitude">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="new_longitude" class="col-sm-2 control-label">Długość geograficzna:</label>
                        <div class="col-sm-3">
                        <input type="text" class="form-control" placeholder="Wprowadź długość geograficzną"
                           id="new_longitude" required="required" name="longitude">
                        </div>
                    </div>

                    <div id="change_progress" style="min-height:38px; min-width:60px">
                        <div class="progress-loading"></div>
                <%--<input type="submit" value="Dodaj budynek" id="btn_submit" class="btn btn-primary btn-default btn-lg active">--%>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <span style="display: flex;position: relative;float: left;">
                        <span class="glyphicon glyphicon-ok" style="position: absolute;font-size:17px;color: white;top: 30%;left:15%;"></span>
                        <input type="submit" value="Dodaj budynek" id="btn_submit" class="btn btn-success" role="button" style="width: 200px;"></div>
                        </span>
                    </div>
                    </div>
             </form>

    </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <div id="result_success"></div>
                    <div id="result_error"></div>
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