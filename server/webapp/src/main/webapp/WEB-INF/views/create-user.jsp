<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>nowy użytkownik</title>
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
            <div id="tittle"><span class="glyphicon glyphicon-user"></span>
                Tworzenie nowego użytkownika</div>
        </div>
    </div>

    <div class="panel panel-default" id="content">
        <div>
            <a href="/all-users" class="btn btn-info" role="button">
                <span class="glyphicon glyphicon-backward"></span>
                Powrót do listy użytkowników
            </a>
        </div>
        <br>
        <div id="settings" class="form-group">
            <div>
                <a class="btn btn-success" id="button_create" role="button"><span class="glyphicon glyphicon-plus"></span> Dodaj</a>
            </div>
        </div>
        <div id="result"></div>

    </div>
</div>

<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script>
    $(document).ready(function () {
        $('#button_create').click(function () {
            $('#result')
                .empty()
                .append(
                    $('<span></span>').text('Czekaj...')
                );
            $.ajax({
                type: 'post',
                url: '/api/user/create',
                dataType: 'json',
                success: function (contentDtoOfUserCreatedDto) {
                    if (contentDtoOfUserCreatedDto.success) {
                        $('#result')
                            .empty()
                            .append(
                                $('<p></p>').text('Stworzono nowego użytkownika o id ' + contentDtoOfUserCreatedDto.content.userId + '. By aktywować konto użyj poniższego linku:'),
                                $('<a></a>')
                                    .attr('href', contentDtoOfUserCreatedDto.content.activationURL)
                                    .text(contentDtoOfUserCreatedDto.content.activationURL)
                            );
                        $('#settings').hide(400);
                    }
                    else {
                        $('#result')
                            .empty()
                            .append(
                                $('<h1></h1>').text('ERROR')
                            );
                    }
                },
                error: function () {
                    $('#result')
                        .empty()
                        .append(
                            $('<h1></h1>').text('ERROR')
                        );
                }
            });
        });
    });
</script>
</body>
</html>