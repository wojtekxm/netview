<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dodawanie kontrolera</title>
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
                <li><a href="/account"><span class="glyphicon glyphicon-user"></span>  &nbsp;<c:out value="${loggedUser.name}"/></a></li>
                <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div style="height: 80px;"></div>
    <div class="panel panel-default">
        <div class="panel-body">
            <div id="tittle"><span class="glyphicon glyphicon-inbox"></span> Nowy kontroler</div>
        </div>
    </div>

    <div style="height: 10px;"></div>
    <div class="panel panel-default" id="content"><div></div>
        <form method="post" action="/api/controller/create" id="form1" class="form-horizontal">


            <div class="form-group">
                <label class="control-label col-sm-2 " for="new_name" >Nazwa kontrolera:</label>
                <div class="col-sm-3">
                    <input form="form1"type="text" class="form-control"  placeholder="Wprowadź nazwę kontrolera"
                           id="new_name" required="required" name="name">
                </div>
            </div>
            <div class="form-group">
                <label for="new_ipv4" class="control-label col-sm-2 ">Adres IPv4:</label>
                <div class="col-sm-3">
                    <input form="form1" type="text" class="form-control"  placeholder="Wprowadź Adres IPv4"
                           id="new_ipv4" required="required" name="ipv4">
                </div>
            </div>
            <div class="form-group">
                <label for="new_description" class="control-label col-sm-2 ">Komentarz:</label>
                <div class="col-sm-3">
                    <input form="form1" type="text" class="form-control"  placeholder="Wprowadź komentarz (opcjonalne)"
                           id="new_description" name="description">
                </div>
            </div>
            <div class="form-group">
                <label for="new_communityString" class="control-label col-sm-2 ">Community String:</label>
                <div class="col-sm-3">
                    <input form="form1" type="text" class="form-control"  placeholder="Wprowadź Community String"
                           id="new_communityString" name="communityString">
                </div>
            </div>


            <div class="form-group">
                <label for="new_building" class="control-label col-sm-2 ">Budynek:</label>
                <div class="col-sm-3">
                    <label for="new_building">Wybierz budynek:</label>

                    <select form="form1" class="form-control" id="new_building"name="buildingId">
                        <option></option>
                        <c:forEach items="${list}" var="building" >
                            <option value="${building.id}" >
                                <c:out value="${building.name}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <div class="checkbox">
                        <label>
                            <input form="form1" type="checkbox" <%--class="form-control"--%> id="new_fake" name="fake"> prawdziwy?</label>
                    </div>
                </div>
            </div>

            <div style="min-height:38px; min-width:60px">
                <div id="change_loading"></div>

                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <span style="display: flex;position: relative;float: left;">
                        <span class="glyphicon glyphicon-ok" style="position: absolute;font-size:17px;color: white;top: 30%;left:15%;"></span>
                        <input form="form1" type="submit" id="btn_submit" value="Dodaj kontroler" class="btn btn-success" role="button" style="width: 200px;"></div>
                    </span>
                </div>
            </div>
            <%--</div>--%>
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
"use strict";
$(document).ready(function () {
    var btnSubmit = $('#btn_submit');
    btnSubmit.click(function () {
        btnSubmit.prop('disabled', true);
        var createControllerDto = {
            "name": $('#new_name').val(),
            "ipv4": $('#new_ipv4').val(),
            "description": $('#new_description').val(),
            "communityString": $('#new_communityString').val(),
            "buildingId": $('#new_building').val(),
            "fake": $('#new_fake').val()
        };
        progress.load(
            'post',
            '/api/controller/create',
            ['#change_loading'], [], [],
            function(createControllerDto) {
                btnSubmit.prop('disabled', false);
                notify.success('#result_success', 'Kontroler został dodany.');
            },
            function() {
                btnSubmit.prop('disabled', false);
                notify.danger('#result_error', 'Nie udało się dodać kontrolera.');
            },
            createControllerDto
        );
    });
});
</script>
</body>
</html>