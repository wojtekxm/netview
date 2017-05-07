<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Modyfikacja jednostki</title>
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


<div id="all" class="container-fluid">
    <div id="container">
        <div class="content">
            <div style="height: 10px;"></div>
            <div>
                <div id="wydzial"><div style="width: 100%;border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;"><span class="glyphicon glyphicon-arrow-right"></span> <c:out value="${unit.description}"/>: </div></div>
                <form method="post" action="/api/accept-modify-unit" id="form2"></form>
            </div>

            <div id="devices" class="panel panel-default" style="padding: 15px;">
                <div class="panel-heading" style="background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0;">
                    Modyfikuj jednostkę:
                </div>

                <table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0;margin-bottom: inherit;">

                    <tr>

                        <input form="form2" id="id" type="hidden" name="id" value="${unit.id}" />

                    </tr>
                    <tr>
                        <td style="color:red" >*Kod</td>
                        <td ><input form="form2" id="new_code" type="text" name="code" value="${unit.code}" style="width: 30%;"/></td>
                    </tr>

                    <tr>
                        <td  style="color:red">*Nazwa</td>
                        <td ><input form="form2" id="new_description" type="text" name="description" value="${unit.description}" style="width: 30%;"/>
                    </tr>

                </table>
                <div>
                    <a href="/unit/${unit.id}" class="btn btn-info" role="button" style="float:left;width:180px;font-size:17px; margin-right: 10px;" ><span class="glyphicon glyphicon-backward"></span> Wróć</a>
                    <span style="display: flex;position: relative;float: left;">
                        <span class="glyphicon glyphicon-ok" style="position: absolute;font-size:17px;color: white;top: 30%;left:15%;"></span>
                        <input form="form2" type="submit" value=" Zatwierdź" class="btn btn-success"  id="btn_submit" role="button" style="float:left;width:180px;font-size:17px;" >
                         <div style="min-height:38px; min-width:60px">
                    <div id="change_loading" class="later"></div>
                         </div>
                    </span>
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
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/notify.js"></script>

<script>
"use strict";
$(document).ready(function () {
    var btnSubmit = $('#btn_submit');
    btnSubmit.click(function () {
        btnSubmit.prop('disabled', true);
        var unitDto = {
            "id": $('#id').val(),
            "code": $('#new_code').val(),
            "description": $('#new_description').val()

        };
        progress.load(
            'post',
            '/api/accept-modify-unit',
            ['#change_loading'], [], [],
            function(unitDto) {
                btnSubmit.prop('disabled', false);
                notify.success('#result_success', 'Dane zostały zmienione.');
            },
            function() {
                btnSubmit.prop('disabled', false);
                notify.danger('#result_error', 'Nie udało się zmienić danych.');
            },
            unitDto

        );
    });
});
</script>
</body>
</html>