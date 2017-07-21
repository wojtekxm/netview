<%--
This file is part of the NetView open source project
Copyright (c) 2017 NetView authors
Licensed under The MIT License
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Użytkownicy</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link rel="stylesheet" href="/css/notify.css">
    <link rel="stylesheet" href="/css/tabelka.css">
    <link rel="stylesheet" href="/css/style.css">
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
<div class="container page">
    <div id="main_loading" class="on-loading"></div>
    <div class="panel panel-default on-loaded">
        <div class="panel-heading">
            <span class="glyphicon glyphicon-user"></span>
            Użytkownicy
        </div>
        <div class="panel-body">
            <div id="tabelka_users"></div>
            <button id="btn_new" class="btn btn-success" data-toggle="collapse" data-target="#form_create">
                <span class="glyphicon glyphicon-plus"></span>
                Nowy użytkownik
            </button>
            <div id="form_create" class="collapse">
                <hr>
                <div class="form-inline">
                    <label>
                        <input id="check_send" type="checkbox" name="send" value="yes" checked>
                        wyślij e-mail aktywacyjny
                    </label>
                    <input id="field_email" type="email" class="form-control">
                    <button id="btn_submit" class="btn btn-primary">
                        Stwórz
                    </button>
                    <div class="progress-space" style="display:inline-block">
                        <div id="create_loading"></div>
                    </div>
                </div>
                <div id="create_result"></div>
            </div>
        </div>
    </div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/moment-with-locales.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/notify.js"></script>
<script src="/js/tabelka.js"></script>
<script>
"use strict";
$(document).ready( function() {
    var fieldEmail, btnSubmit, createResult, checkSend;
    fieldEmail = $('#field_email');
    btnSubmit = $('#btn_submit');
    createResult = $('#create_result');
    checkSend = $('#check_send');

    function fixUsers(listOfUserDto) {
        tabelka.builder('!')
            .searchGenerator(function(u) {
                return [u.name];
            })
            .column('nazwa', 'text', 'cmp_name', 2, function(user) {
                if(user.name === null) {
                    user.cmp_name = '[' + user.id + ']';
                    return $('<a></a>')
                        .attr('href', '/user/' + user.id)
                        .text(user.cmp_name);
                }
                else {
                    user.cmp_name = user.name;
                    return $('<a></a>')
                        .attr('href', '/user/' + user.id)
                        .text(user.cmp_name);
                }
            })
            .column('rola', 'number', 'cmp_role', 2, function(user) {
                if(user.role === 'ROOT') {
                    user.cmp_role = 0;
                    return 'administrator';
                }
                else {
                    user.cmp_role = 1;
                    return 'zwykły';
                }
            })
            .column('aktywowany', 'number', 'cmp_activated', 2, function(user) {
                if(user.activated) {
                    user.cmp_activated = 1;
                    return $('<span></span>').append(
                        $('<span class="glyphicon glyphicon-check" style="color:#5cb85c"></span>'),
                        ' tak'
                    );
                }
                else {
                    user.cmp_activated = 0;
                    return $('<span></span>').append(
                        $('<span class="glyphicon glyphicon-unchecked" style="color:#d9534f"></span>'),
                        ' nie'
                    );
                }
            })
            .column('zablokowany', 'number', 'cmp_blocked', 2, function(user) {
                if(user.blocked) {
                    user.cmp_blocked = 1;
                    return $('<span></span>').append(
                        $('<span class="glyphicon glyphicon-check" style="color:#5cb85c"></span>'),
                        ' tak'
                    );
                }
                else {
                    user.cmp_blocked = 0;
                    return $('<span></span>').append(
                        $('<span class="glyphicon glyphicon-unchecked" style="color:#d9534f"></span>'),
                        ' nie'
                    );
                }
            })
            .column('utworzony', 'number', 'createdAt', 4, function(user) {
                var s = $('<span></span>');
                s.attr('title', moment(user.createdAt).format('DD.MM.YYYY hh:mm:ss') );
                var d = new Date().getTime() - user.createdAt;
                s.text(moment.duration(d).locale('pl').humanize() + ' temu');
                return s;
            })
            .column('ostatni dostęp', 'number', 'createdAt', 4, function(user) {
                var s = $('<span></span>');
                s.attr('title', moment(user.lastAccess).format('DD.MM.YYYY hh:mm:ss') );
                var d = new Date().getTime() - user.lastAccess;
                s.text(moment.duration(d).locale('pl').humanize() + ' temu');
                return s;
            })
            .build('#tabelka_users', listOfUserDto);
    }

    progress.loadGet(
        '/api/user/info/all',
        ['.on-loading'], ['.on-loaded'], [],
        function(listDtoOfUserDto) {
            fixUsers(listDtoOfUserDto.list);
        }
    );

    $('input[type=checkbox][name=send]').change(function() {
        if( $('input[type=checkbox][name=send]:checked').val() ) {
            fieldEmail.prop('disabled', false);
        }
        else {
            fieldEmail.prop('disabled', true);
        }
    });

    btnSubmit.click(function() {
        var createUserDto, email;
        btnSubmit.prop('disabled', true);
        createResult.empty();
        email = fieldEmail.val();
        createUserDto = {
            "sendEmail" : checkSend.prop('checked') ? email : null
        };
        console.log('createUserDto', createUserDto);
        progress.load(
            [{
                "url" : '/api/user/create',
                "method" : 'post',
                "postData" : createUserDto
            }, {
                "url" : '/api/user/info/all'
            }],
            ['#create_loading'], [], [],
            function(responses) {
                btnSubmit.prop('disabled', false);
                var h4 = $('<h4></h4>').text('Nowego użytkownika można aktywować po odwiedzeniu linku:');
                var a = $('<a></a>').attr('href', responses[0].content.activationURL)
                    .text(responses[0].content.activationURL);
                h4.hide();
                a.hide();
                createResult.append(h4, a);
                h4.show(400);
                a.show(400);
                fixUsers(responses[1].list);
                notify.success('Nowy użytkownik został stworzony');
            },
            function() {
                btnSubmit.prop('disabled', false);
                notify.danger('Niepowodzenie');
            }
        );
    });
} );
</script>
</body>
</html>