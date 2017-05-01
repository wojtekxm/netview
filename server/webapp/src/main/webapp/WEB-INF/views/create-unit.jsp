<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dodawanie jednostki</title>
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
        <div class="form-group">

            <form method="post" action="/api/unit/create">
                <input type="text" class="form-control" placeholder="Kod jednostki"
                       id="new_code" required="required" name="code">
                <input type="description" class="form-control" placeholder="Opis"
                       id="new_description" required="required" name="description">
                <input type="submit" value="Dodaj jednostkę" id="btn_submit" class="btn btn-primary btn-default btn-lg active">
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
            var createUnitDto = {
                "code": $('#new_code').val(),
                "description": $('#new_description').val()

            };
            progress.load(
                'post',
                '/api/unit/create',
                '#change_progress',
                function(createUnitDto) {
                    btnSubmit.prop('disabled', false);
                    notify.success('#result_success', 'Jednostka została dodana.');
                },
                function() {
                    btnSubmit.prop('disabled', false);
                    notify.danger('#result_error', 'Nie udało się dodać jednostki.');
                },
                createUnitDto

            );
        });
    });
</script>
</body>
</html>