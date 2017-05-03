<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>zarządzanie kontem</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
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

<div class="container">
    <div style="height: 100px;"></div>
    <div class="clearfix">
        <h4 class="pull-left">Zarządzanie kontem</h4>
    </div>
    <div class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-3 control-label">Aktualne hasło</label>
            <div class="col-sm-6">
                <input id="actual_password" type="password" class="form-control">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">Nowe hasło</label>
            <div class="col-sm-6">
                <input id="new_password" type="password" class="form-control">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">Powtórz nowe hasło</label>
            <div class="col-sm-6">
                <input id="repeat_password" type="password" class="form-control">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-5 col-sm-7 clearfix">
                <button id="btn_submit" type="button" class="btn btn-primary pull-left">Zapisz</button>
                <div class="pull-left" style="min-height:45px; min-width:60px">
                    <div id="change_loading" class="later"></div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-12">
                <div id="result_success"></div>
                <div id="result_error"></div>
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
        var changePasswordDto = {
            "old": $('#actual_password').val(),
            "desired": $('#new_password').val(),
            "repeat": $('#repeat_password').val()
        };
        progress.load(
            'post',
            '/api/change-password',
            ['#change_loading'], [], [],
            function(contentDtoOfAccessDto) {
                btnSubmit.prop('disabled', false);
                notify.success('#result_success', 'Hasło zostało zmienione');
            },
            function() {
                btnSubmit.prop('disabled', false);
                notify.danger('#result_error', 'Nie udało się zmienić hasła');
            },
            changePasswordDto
        );
    });
});
</script>
</body>
</html>