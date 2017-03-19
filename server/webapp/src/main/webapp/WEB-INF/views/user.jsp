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