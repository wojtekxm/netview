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
            <span class="glyphicon glyphicon-arrow-right"></span> Informacje o użytkowniku:
        </div>
        <div class="panel-body">
            <div class="panel-heading" style="width: 100%;background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0;">
                Szczegóły użytkownika:
            </div>
            <table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0;">
                <tr>
                    <td>Nazwa</td>
                    <td><c:out value="${selected.name}"/></td>
                </tr>
                <tr>
                    <td>ID</td>
                    <td><c:out value="${selected.id}"/></td>
                </tr>
                <tr>
                    <td>Rola</td>
                    <td>
                        <c:choose>
                            <c:when test="${selected.role == 'NORMAL'}"><c:out value="zwykły użytkownik"/></c:when>
                            <c:when test="${selected.role == 'ROOT'}"><c:out value="root"/></c:when>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td>Stan konta</td>
                    <td id="user_state">
                        <c:choose>
                            <c:when test="${selected.blocked}"><c:out value="zablokowane"/></c:when>
                            <c:when test="${!selected.activated}"><c:out value="nieaktywne"/></c:when>
                            <c:otherwise><c:out value="aktywne"/></c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
            <div class="clearfix">
                <button id="button_block" class="btn btn-danger" style="display:none;float:left;font-size:17px;"><span class="glyphicon glyphicon-exclamation-sign"></span> Zablokuj</button>
            </div>
        </div>
    </div>
    <div id="notify_layer" style="position: fixed; top: 100px;"></div>
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
            success: function(contentDtoOfUserDto) {
                var u = contentDtoOfUserDto.content;

                var st = 'aktywne';
                if(u.blocked)st = 'zablokowane';
                else if(!u.activated)st = 'nieaktywne';

                if(!u.blocked) {
                    $('#button_block').show();
                }
                $('#devices').show();
                $('#progress_area').hide();
                $('#button_block').click( function() {
                    $.ajax({
                        type: 'post',
                        url: '/api/user/block/' + my.selectedId,
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