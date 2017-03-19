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
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <ul class="nav nav-pills" style="padding-top: 3px;font-size: 17px;position: absolute;width: 100%;left: 0;text-align: center;margin:0 auto;">
            <li role="presentation"><a href="/make-survey">Nowe badanie</a></li>
            <li role="presentation"><a href="/status-small">Mały widok</a></li>
            <li role="presentation"><a href="/all-controllers">Kontrolery</a></li>
            <li role="presentation"><a href="/all-users">Użytkownicy</a></li>
            <li role="presentation"><a href="/all-devices">Urządzenia</a></li>
            <form class="navbar-form nav-pills" style="padding-top: 2px;margin-top:2px;">
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="Search">
                </div>
                <button type="submit" class="btn btn-default">Submit</button>
            </form>
        </ul>
        <ul class="nav nav-pills pull-left" style="padding-top: 3px;border-radius: 10px;padding-left:7px;font-size: 17px;">
            <li role="presentation" class="active"><a href="/"><span class="glyphicon glyphicon-home"></span>  Strona główna</a></li>
        </ul>
        <ul class="nav nav-pills pull-right" style="padding-top: 3px;padding-right:3px;font-size: 17px;">
            <li role="presentation"><a href="#"><span class="glyphicon glyphicon-user"></span>  Mój profil</a></li>
            <li role="presentation"><a href="/logout"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
        </ul>
    </div>
</nav>
<div class="container" id="page">
    <div class="welcome">
        <div class="tittleStatic"><img src="/images/icon.ico" style="padding-bottom: 5px;"> &nbsp; NETWORK-MONITOR</div>
        <div class="userStatic">zalogowany: <c:out value="${loggedUser.name}"/>
        </div>
        <div class="logo"><img src="/images/logooWhite.jpg"></div>
    </div>
    <a href="/">strona główna</a><br>
    <a href="/all-users">zobacz wszystkich użytkowników</a><br>
    <hr>
    <h4>Tworzenie nowego użytkownika</h4>
    <div id="settings" class="form-group">
        <label>
            <input type="radio" name="is_admin" value="false" checked>
            zwykły użytkownik
        </label>
        <br>
        <label>
            <input type="radio" name="is_admin" value="true">
            administrator
        </label>
        <br>
        <button type="button" id="button_create">Stwórz</button>
    </div>
    <div id="result"></div>
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