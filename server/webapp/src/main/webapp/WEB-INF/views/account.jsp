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
                    <input type="text" name="query" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 180px!important;">
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
                <div id="wydzial">
                    <div style="border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;">
                        <span class="glyphicon glyphicon-user"></span>
                        Zalogowany: <c:out value="${loggedUser.name}"/>
                    </div>
                </div>
            </div>
            <div id="devices" class="panel panel-default" style="padding: 15px;">
                <div class="panel-heading" style="background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0;">
                    Zmiana hasła
                </div>
                <table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0;">
                    <tr>
                        <td>Stare hasło:</td>
                        <td><input id="old_password" type="password"></td>
                    </tr>
                    <tr>
                        <td>Nowe hasło:</td>
                        <td><input id="new_password" type="password"></td>
                    </tr>
                    <tr>
                        <td>Potwierdź nowe hasło:</td>
                        <td><input id="repeat_password" type="password"></td>
                    </tr>
                </table>
                <button id="submit" class="btn btn-success" style="float:left; width:180px;"><span class="glyphicon glyphicon-ok"></span> Zmień</button>
                <div id="result" style="margin-top: 35px;"></div>
            </div>
        </div>
    </div>
</div>


<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script>
    $(document).ready(function () {
        $('#submit').click(function () {
            $('#result > *').remove();
            $('#result').append(
                $('<div></div>').addClass('panel panel-info').append(
                    $('<div></div>').addClass('panel-heading').text('Czekaj...')
                )
            );
            $.ajax( {
                url: '/api/change-password',
                type: 'post',
                dataType: 'json',
                data: {
                    userId: ${loggedUser.id},
                    old: $('#old_password').val(),
                    desired: $('#new_password').val(),
                    repeat: $('#repeat_password').val()
                },
                success: function (ContentDtoOfPasswordChangedDto) {
                    $('#result > *').remove();
                    if(ContentDtoOfPasswordChangedDto.success) {
                        $('#result').append(
                            $('<div></div>').addClass('panel panel-success').append(
                                $('<div></div>').addClass('panel-heading').text('Hasło zostało zmienione.')
                            )
                        );
                    }
                    else {
                        var txt = 'Nie udało się zmienić hasła';
                        $('#result').append(
                            $('<div></div>').addClass('panel panel-danger').append(
                                $('<div></div>').addClass('panel-heading').text(txt)
                            )
                        );
                    }
                },
                error: function() {
                    $("#result > *").remove();
                    $('#result').append(
                        $('<div></div>').addClass('panel panel-danger').append(
                            $('<div></div>').addClass('panel-heading').text('Wystąpił problem. Spróbuj ponownie.')
                        )
                    );
                }
            } );
        });
    });
</script>
</body>
</html>