<%--
This file is part of the NetView open source project
Copyright (c) 2017 NetView authors
Licensed under The MIT License
--%>
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
    <link rel="stylesheet" href="/css/notify.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="/css/cookieconsent.min.css" media="screen">
    <script src="/js/cookieconsent.min.js"></script>
    <script>
        window.addEventListener("load", function(){
            window.cookieconsent.initialise({
                "palette": {
                    "popup": {
                        "background": "#3c404d",
                        "text": "#d6d6d6"
                    },
                    "button": {
                        "background": "#8bed4f"
                    }
                },
                "theme": "edgeless",
                "content": {
                    "message": "Ta strona wykorzystuje pliki cookies. Korzystanie z witryny oznacza zgodę na ich zapis lub odczyt wg ustawień przeglądarki.",
                    "dismiss": "OK",
                    "link": "O polityce cookies",
                    "href": "wszystkoociasteczkach.pl/polityka-cookies/"
                }
            })});
    </script>
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
    <div class="panel panel-default">
        <div class="panel-heading">
            <span class="glyphicon glyphicon-inbox"></span> Nowy kontroler
        </div>
        <div class="panel-body">
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="control-label col-sm-2 " for="new_name" ><font color="red" ><b>*</b></font> Nazwa kontrolera:</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control"  placeholder="Wprowadź nazwę kontrolera"
                               id="new_name" required="required" name="name">
                    </div>
                </div>
                <div class="form-group">
                    <label for="new_ipv4" class="control-label col-sm-2 "><font color="red" ><b>*</b></font> Adres IPv4:</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control"  placeholder="Wprowadź Adres IPv4"
                               id="new_ipv4" required="required" name="ipv4">
                    </div>
                </div>
                <div class="form-group">
                    <label for="new_description" class="control-label col-sm-2 ">Komentarz:</label>
                    <div class="col-sm-3">
                        <input  type="text" class="form-control"  placeholder="Wprowadź komentarz (opcjonalne)"
                                id="new_description" name="description">
                    </div>
                </div>
                <div class="form-group">
                    <label for="new_communityString" class="control-label col-sm-2 ">Community String:</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control"  placeholder="Wprowadź Community String"
                               id="new_communityString" name="communityString">
                    </div>
                </div>


                <div class="form-group">
                    <label for="new_building" class="control-label col-sm-2 ">Budynek:</label>
                    <div class="col-sm-3">
                        <label for="new_building">Wybierz budynek:</label>

                        <select  class="form-control" id="new_building"name="buildingId">
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
                                <input  type="checkbox" <%--value="false" class="form-control"--%> id="new_fake" name="fake"> Prawdziwy </label>
                        </div>
                    </div>
                </div>

                <div class="change_loading" class="progress-space"></div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                    <span style="display: flex;position: relative;float: left;">
                    <span class="glyphicon glyphicon-ok" style="position: absolute;font-size:17px;color: white;top: 30%;left:15%;"></span>
                        <input  type="submit" id="btn_submit" value="Dodaj kontroler" class="btn btn-success" role="button" style="width: 200px;">
                    </span>
                    </div>
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
        var createControllerDto = {
            "name": $('#new_name').val(),
            "ipv4": $('#new_ipv4').val(),
            "description": $('#new_description').val(),
            "communityString": $('#new_communityString').val(),
            "buildingId": $('#new_building').val(),
            "fake" : !( $('#new_fake')[0].checked )
        };
        progress.load(
            [{
                "url" : '/api/controller/create',
                "method" : 'post',
                "postData" : createControllerDto
            }],
            ['#change_loading'], [], [],
            function() {
                btnSubmit.prop('disabled', false);
                notify.success('Kontroler został dodany.');
            },
            function() {
                btnSubmit.prop('disabled', false);
                notify.danger('Nie udało się dodać kontrolera.');
            }
        );
    });
});
</script>
</body>
</html>