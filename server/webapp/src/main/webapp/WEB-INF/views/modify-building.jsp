<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Budynki</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/notify.css">
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
<div class="container">
    <div style="height: 80px;"></div>
    <div class="panel panel-default" id="header" style="margin-bottom: 15px!important;">
        <div class="panel-body">
            <div id="tittle"><span class="glyphicon glyphicon-home"></span> <c:out value="${building.name}"/>: </div>
            <%--<form method="post" action="/api/accept-modify-building" id="form1"></form>--%>
        </div>
    </div>

    <div class="panel panel-default" id="content">

        <div class="panel-heading" style="background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0;">
            Modyfikuj budynek:
        </div>

        <table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0; margin-bottom: inherit;">
            <tr>
                <input type="hidden" id="id" name="id" value="${building.id}" />
            </tr>
            <tr>
                <td><font color="red" ><b>*</b></font> Kod:</td>
                <td><input type="text" id="new_code" name="code" value="${building.code}" style="width: 30%;"/></td>
            </tr>

            <tr>
                <td ><font color="red" ><b>*</b></font> Nazwa:</td>
                <td><input type="text" id="new_name" name="name" value="${building.name}" style="width: 30%;" />
            </tr>

            <tr>
                <td>Ulica:</td>
                <td><input type="text" id="new_street" name="street" value="${building.street}" style="width: 30%;" />
            </tr>

            <tr>
                <td>Numer:</td>
                <td><input type="text" id="new_number" name="number" value="${building.number}" style="width: 30%;" />
            </tr>

            <tr>
                <td>Kod pocztowy:</td>
                <td><input  type="text" id="new_postalCode" name="postalCode" value="${building.postalCode}" style="width: 30%;" />
            </tr>

            <tr>
                <td>Miasto:</td>
                <td><input  type="text" id="new_city" name="city" value="${building.city}" style="width: 30%;" />
            </tr>

            <tr>
                <td><font color="red" ><b>*</b></font> Szerokość geograficzna:</td>
                <td><input  type="text" id="new_latitude" name="latitude" value="${building.latitude}" style="width: 30%;"/></td>
            </tr>

            <tr>
                <td ><font color="red" ><b>*</b></font> Długość geograficzna:</td>
                <td><input  type="text" id="new_longitude" name="longitude" value="${building.longitude}" style="width: 30%;" /></td>

            </tr>
        </table>
        <div style="height: 40px;">
            <a href="/building/${building.id}" class="btn btn-info" role="button" style="float:left;width:180px;font-size:17px; margin-right: 10px;" ><span class="glyphicon glyphicon-backward"></span> Wróć</a>
            <span style="display: flex;position: relative;float: left;">
                        <span class="glyphicon glyphicon-ok" style="position: absolute;font-size:17px;color: white;top: 30%;left:19%;"></span>

                        <input id="btn_submit" type="submit" value="Zatwierdź" class="btn btn-success" role="button" style="float:left;width:180px;font-size:17px;" >
                    </span>
            <div id="change_loading" class="later progress-space"></div>
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
        var BuildingDto = {
            "id": $('#id').val(),
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
            [{
                "url" : '/api/accept-modify-building',
                "method" : 'post',
                "postData" : BuildingDto
            }],
            ['#change_loading'], [], [],
            function(BuildingDto) {
                btnSubmit.prop('disabled', false);
                notify.success('Udało się zmienić dane.');
            },
            function() {
                btnSubmit.prop('disabled', false);
                notify.danger('Nie udało się zmienić danych.');
            }
        );
    });
});
</script>
</body>
</html>