<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Informacje o użytkowniku</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <link rel="icon" href="/favicon.ico">
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
                <li><a href="/all-buildings">Budynki</a></li>
                <%--<li><a href="/all-units">Jednostki</a></li>--%>
                <%--<li><a href="/unitsbuildings">Jedn. Bud.</a></li>--%>
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

<!-- kill me pls -->
<div style="margin-top:100px"></div>
<!-- I'm not a solution -->

<div id="container" id="page">
    <div style="height: 10px;"></div>
    <div id="result" style="display:none">
        <h1>informacje o użytkowniku</h1>
        id: <span id="user_id"></span><br>
        nazwa: <span id="user_name"></span><br>
        rola: <span id="user_role"></span><br>
        konto: <span id="user_state"></span><br>
        <button id="button_block" type="button" style="display:none">Zablokuj dostęp</button>
    </div>
    <span id="progress_area">pobieranie informacji...</span>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script>
    var my = {};
    my.selectedId = ${selected.id};
    $(document).ready( function() {
        $.ajax({
            type: 'get',
            url: '/api/user',
            data: {
                'id': my.selectedId
            },
            dataType: 'json',
            success: function(userData) {
                var role = 'zwykły użytkownik';
                if(userData.role === 'ROOT')role = 'root';
                else if(userData.role === 'ADMIN')role = 'administrator';

                var st = 'aktywne';
                if(userData.blocked)st = 'zablokowane';
                else if(!userData.activated)st = 'nieaktywne';

                $('#user_id').text(userData.id);
                $('#user_name').text(userData.name);
                $('#user_role').text(role);
                $('#user_state').text(st);
                if(!userData.blocked) {
                    $('#button_block').show();
                }
                $('#result').show();
                $('#progress_area').hide();
                $('#button_block').click( function() {
                    $.ajax({
                        type: 'post',
                        url: '/api/block-user',
                        data: {
                            id: my.selectedId
                        },
                        dataType: 'json',
                        success: function(baseResultDto) {
                            if(baseResultDto.success) {
                                $('#user_state').text('zablokowane');
                                $('#button_block').hide(400);
                                $('#progress_area').hide();
                            }
                            else {
                                $('#progress_area').text('RESULT: FAILED');
                                $('#progress_area').show();
                            }
                        },
                        error: function() {
                            $('#progress_area').text('FAILED AJAX');
                            $('#progress_area').show();
                        }
                    });
                } );
            },
            error: function() {
                $('#progress_area').text('Nie udało się pobrać informacji!');
                $('#progress_area').show();
            }
        });
    } );
</script>
</body>
</html>