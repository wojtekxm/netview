<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Informacje o użytkowniku</title>
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
                <li style="max-height:50px;"><a href="/make-survey">Nowe badanie</a></li>
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


<div id="all" class="container-fluid">
    <div id="container">
        <div class="content">
            <div style="height: 10px;"></div>
                <div>
                    <div id="wydzial"><div style="width: 100%;border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;"><span class="glyphicon glyphicon-arrow-right"></span> Informacje o użytkowniku: </div></div>
                </div>

                id: <c:out value="${user.id}"/><br>
                nazwa: <span id="user_name"></span><br>
                rola: <span id="user_role"></span><br>
                konto: <span id="user_state"></span><br>
                <button id="button_block" class="btn btn-danger" style="width:250px;">Zablokuj dostęp</button>
            </div>
        </div>
    </div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<%--<script>--%>
    <%--var my = {};--%>
    <%--my.selectedId = ${selected.id};--%>
    <%--$(document).ready( function() {--%>
        <%--$.ajax({--%>
            <%--type: 'get',--%>
            <%--url: '/api/user',--%>
            <%--data: {--%>
                <%--'id': my.selectedId--%>
            <%--},--%>
            <%--dataType: 'json',--%>
            <%--success: function(contentDtoOfUserDto) {--%>
                <%--var u = contentDtoOfUserDto.content;--%>
                <%--var role = 'zwykły użytkownik';--%>
                <%--if(u.role === 'ROOT')role = 'root';--%>
                <%--else if(u.role === 'ADMIN')role = 'administrator';--%>

                <%--var st = 'aktywne';--%>
                <%--if(u.blocked)st = 'zablokowane';--%>
                <%--else if(!u.activated)st = 'nieaktywne';--%>

                <%--$('#user_id').text(u.id);--%>
                <%--$('#user_name').text(u.name);--%>
                <%--$('#user_role').text(role);--%>
                <%--$('#user_state').text(st);--%>
                <%--if(!u.blocked) {--%>
                    <%--$('#button_block').show();--%>
                <%--}--%>
                <%--$('#result').show();--%>
                <%--$('#progress_area').hide();--%>
                <%--$('#button_block').click( function() {--%>
                    <%--$.ajax({--%>
                        <%--type: 'post',--%>
                        <%--url: '/api/user/block/' + my.selectedId,--%>
                        <%--dataType: 'json',--%>
                        <%--success: function(baseResultDto) {--%>
                            <%--if(baseResultDto.success) {--%>
                                <%--$('#user_state').text('zablokowane');--%>
                                <%--$('#button_block').hide(400);--%>
                                <%--$('#progress_area').hide();--%>
                            <%--}--%>
                            <%--else {--%>
                                <%--$('#progress_area').text('RESULT: FAILED');--%>
                                <%--$('#progress_area').show();--%>
                            <%--}--%>
                        <%--},--%>
                        <%--error: function() {--%>
                            <%--$('#progress_area').text('FAILED AJAX');--%>
                            <%--$('#progress_area').show();--%>
                        <%--}--%>
                    <%--});--%>
                <%--} );--%>
            <%--},--%>
            <%--error: function() {--%>
                <%--$('#progress_area').text('Nie udało się pobrać informacji!');--%>
                <%--$('#progress_area').show();--%>
            <%--}--%>
        <%--});--%>
    <%--} );--%>
<%--</script>--%>
</body>
</html>