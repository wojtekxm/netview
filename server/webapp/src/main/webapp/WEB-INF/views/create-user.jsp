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
                <li><a style="background-color: #1d1d1d;" href="/"><span class="glyphicon glyphicon-home"></span></a></li>
                <li style="max-height:50px;"><a href="/make-survey">Nowe badanie</a></li>
                <li><a href="/all-controllers">Kontrolery</a></li>
                <li><a href="/all-users">Użytkownicy</a></li>
                <li><a href="/all-devices">Urządzenia</a></li>
                <li><a href="/building">Budynki</a></li>
            </ul>
            <form class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 150px!important;">
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
<div id="container" id="page">
    <div class="content">
        <div>
            <div id="wydzial">
                <div style="border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;">
                    <span class="glyphicon glyphicon-plus"></span>
                    Tworzenie nowego użytkownika
                </div>
            </div>
        </div>
        <div class="panel panel-default" style="padding: 15px;">
            <div>
                <a href="/all-users" class="btn btn-info" role="button" style="width: 300px;font-size:17px;">
                    <span class="glyphicon glyphicon-backward"></span>
                    Wszyscy użytkownicy
                </a>
            </div>
            <br>
            <br>
            <div id="settings" class="form-group">
                <div class="radio">
                    <label style="font-size:17px;">
                        <input type="radio" name="is_admin" value="false" checked>
                        zwykły użytkownik
                    </label>
                </div>
                <div class="radio">
                    <label style="font-size:17px;">
                        <input type="radio" name="is_admin" value="true">
                        administrator
                    </label>
                </div>
                <br>
                <div>
                    <a class="btn btn-success" id="button_create" role="button" style="width: 300px;font-size:17px;"><span class="glyphicon glyphicon-plus"></span> Dodaj</a>
                </div>
            </div>
            <div id="result"></div>
        </div>
    </div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script>
    $(document).ready(function () {
        $('#button_create').click(function () {
            var isAdmin = $('#page').find('input[name="is_admin"]:checked').val() === 'true';
            $('#result')
                .empty()
                .append(
                    $('<span></span>').text('Czekaj...')
                );
            $.ajax({
                type: 'post',
                url: '/api/create-user',
                dataType: 'json',
                data: {
                    "is-admin": isAdmin
                },
                success: function (data) {
                    if (data.success) {
                        $('#result')
                            .empty()
                            .append(
                                $('<p></p>').text('Stworzono nowego użytkownika o id ' + data.userId + '. By aktywować konto użyj poniższego linku:'),
                                $('<a></a>')
                                    .attr('href', data.activationURL)
                                    .text(data.activationURL)
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