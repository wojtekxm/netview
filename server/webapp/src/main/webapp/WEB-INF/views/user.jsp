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
    <title>Informacje o użytkowniku</title>
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

<div class="container page">
    <div class="on-loading"></div>
    <div class="panel panel-default on-loaded">
        <div class="panel-heading">
            <span class="glyphicon glyphicon-user"></span>
            Informacje o użytkowniku
        </div>
        <div class="panel-body">
            <table class="table table-bordered">
                <colgroup>
                    <col style="width: 30%">
                    <col>
                </colgroup>
                <tr id="tr_name">
                    <th>nazwa</th>
                    <td id="user_name">?</td>
                </tr>
                <tr>
                    <th>rola</th>
                    <td id="tr_role">
                        <span id="span_role">?</span>
                        <button id="btn_role" class="btn">?</button>
                        <div class="progress-space-xs" style="display:inline-block">
                            <div id="role_loading"></div>
                        </div>
                    </td>
                </tr>
                <tr id="tr_activated">
                    <th>aktywowany</th>
                    <td id="user_activated">?</td>
                </tr>
                <tr id="tr_blocked">
                    <th>zablokowany</th>
                    <td>
                        <span id="span_blocked">?</span>
                        <button id="btn_blocked" class="btn">?</button>
                        <div class="progress-space-xs" style="display:inline-block">
                            <div id="block_loading"></div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th>stworzony</th>
                    <td id="user_created">?</td>
                </tr>
                <tr id="tr_last_access">
                    <th>ostatni dostęp</th>
                    <td id="user_last_access">?</td>
                </tr>
            </table>
            <div class="row">
                <div class="col-sm-6">
                    <button id="btn_password" class="btn btn-primary">
                        Zresetuj hasło
                    </button>
                    <div class="progress-space" style="display: inline-block">
                        <div id="password_loading"></div>
                    </div>
                </div>
                <div class="col-sm-6 clearfix">
                    <c:if test="${loggedUser.id != id}"
                    ><form class="pull-right" method="post" action="/user/remove/${id}" style="display: block">
                        <button class="btn btn-danger pull-right" type="submit">
                            <span class="glyphicon glyphicon-trash"></span>
                            Usuń
                        </button>
                    </form></c:if>
                </div>
            </div>
            <div id="reset_result"></div>
        </div>
    </div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/moment-with-locales.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/notify.js"></script>
<script>
"use strict";
$(document).ready(function() {
    var loggedId, selectedId, user, userName, spanRole, btnRole, btnBlocked, btnPassword, userActivated,
        userCreated, userLastAccess, spanBlocked, resetResult,
        trRole, trBlocked, trLastAccess, trName, trActivated, userInfo;
    loggedId = ${loggedUser.id};
    selectedId = ${id};
    userName = $('#user_name');
    userActivated = $('#user_activated');
    userCreated = $('#user_created');
    userLastAccess = $('#user_last_access');
    spanRole = $('#span_role');
    spanBlocked = $('#span_blocked');
    btnBlocked = $('#btn_blocked');
    btnRole = $('#btn_role');
    btnPassword = $('#btn_password');
    trRole = $('#tr_role');
    trBlocked = $('#tr_blocked');
    trLastAccess = $('#tr_last_access');
    trName = $('#tr_name');
    trActivated = $('#tr_activated');
    resetResult = $('#reset_result');

    progress.loadGet(
        '/api/user/info/' + selectedId,
        ['.on-loading'], ['.on-loaded'], [],
        function(contentDtoOfUserDto) {
            updateUser(contentDtoOfUserDto.content);
            btnBlocked.click(function () {
                var postUrl = '/api/user/';
                if(userInfo.blocked) {
                    postUrl += 'unlock/';
                }
                else {
                    postUrl += 'block/';
                }
                postUrl += selectedId;
                btnBlocked.prop('disabled', true);
                progress.load(
                    [{
                        "url": postUrl,
                        "method": 'post'
                    }, {
                        "url": '/api/user/info/' + selectedId
                    }],
                    ['#block_loading'], [], [],
                    function (responses) {
                        btnBlocked.prop('disabled', false);
                        updateUser(responses[1].content);
                    },
                    function () {
                        btnBlocked.prop('disabled', false);
                    }
                );
            });
            btnRole.click(function () {
                var postUrl = '/api/user/';
                if(userInfo.role === 'ROOT') {
                    postUrl += 'degrade/';
                }
                else {
                    postUrl += 'advance/';
                }
                postUrl += selectedId;
                btnRole.prop('disabled', true);
                progress.load(
                    [{
                        "url": postUrl,
                        "method": 'post'
                    }, {
                        "url": '/api/user/info/' + selectedId
                    }],
                    ['#role_loading'], [], [],
                    function (responses) {
                        btnRole.prop('disabled', false);
                        updateUser(responses[1].content);
                    },
                    function () {
                        btnRole.prop('disabled', false);
                    }
                );
            });
            btnPassword.click(function() {
                resetResult.empty();
                btnPassword.prop('disabled', true);
                progress.load(
                    [{
                        "url": '/api/user/begin-reset-password/' + selectedId,
                        "method": 'post'
                    }, {
                        "url": '/api/user/info/' + selectedId
                    }],
                    ['#password_loading'], [], [],
                    function (responses) {
                        var h4, a;
                        updateUser(responses[1].content);
                        h4 = $('<h4></h4>').text('Hasło można zresetować na stronie:');
                        a = $('<a></a>').attr('href', responses[0].content.resetURL)
                            .text(responses[0].content.resetURL);
                        h4.hide();
                        a.hide();
                        resetResult.append(h4, a);
                        h4.show(400);
                        a.show(400);
                    },
                    function () {
                        btnRole.prop('disabled', false);
                        notify.danger('Niepowodzenie');
                    }
                );
            });
        },
        function() {
            notify.danger('Niepowodzenie');
        }
    );

    function updateUser(userDto) {
        var when, diff, ago;
        userName.text(userDto.name);
        spanRole.text(userDto.role === 'ROOT' ? 'administrator' : 'zwykły użytkownik');
        trName.hide();
        trActivated.hide();
        trBlocked.hide();
        trLastAccess.hide();
        if(loggedId != selectedId) {
            trActivated.show();
            if(userDto.activated) {
                btnRole.show();
                trName.show();
                trLastAccess.show();
                trBlocked.show();
                userActivated.text('tak');
                if (userDto.blocked) {
                    spanBlocked.text('tak');
                    btnBlocked.removeClass('btn-danger');
                    btnBlocked.addClass('btn-success');
                    btnBlocked.text('odblokuj');
                }
                else {
                    spanBlocked.text('nie');
                    btnBlocked.removeClass('btn-success');
                    btnBlocked.addClass('btn-danger');
                    btnBlocked.text('zablokuj');
                }
            }
            else {
                btnRole.hide();
                userActivated.text('nie');
            }
            if(userDto.role === 'ROOT') {
                btnRole.removeClass('btn-success');
                btnRole.addClass('btn-danger');
                btnRole.text('zdegraduj do zwykłego');
            }
            else {
                btnRole.removeClass('btn-danger');
                btnRole.addClass('btn-success');
                btnRole.text('awansuj na administratora');
            }
        }
        else {
            btnRole.hide();
            trName.show();
            trLastAccess.show();
        }
        if(userDto.activated) {
            btnPassword.show();
        }
        else {
            btnPassword.hide();
        }
        when = moment(userDto.createdAt).format('DD.MM.YYYY hh:mm:ss');
        diff = new Date().getTime() - userDto.createdAt;
        ago = moment.duration(diff).locale('pl').humanize() + ' temu';
        userCreated.text(when + ' (' + ago + ')');
        when = moment(userDto.lastAccess).format('DD.MM.YYYY hh:mm:ss');
        diff = new Date().getTime() - userDto.lastAccess;
        ago = moment.duration(diff).locale('pl').humanize() + ' temu';
        userLastAccess.text(when + ' (' + ago + ')');
        userInfo = userDto;
    }
});
</script>
</body>
</html>