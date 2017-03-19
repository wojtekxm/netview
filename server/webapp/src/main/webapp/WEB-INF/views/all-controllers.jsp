<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Kontrolery</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/style.css">
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
<div id="all" class="container-fluid">
    <div class="container">
        <div class="welcome">
            <div class="tittleStatic"><img src="/images/icon.ico" style="padding-bottom: 5px;"> &nbsp; NETWORK-MONITOR</div>
            <div class="userStatic">zalogowany: <c:out value="${loggedUser.name}"/>
            </div>
            <div class="logo"><img src="/images/logooWhite.jpg"></div>
        </div>
        <div class="content">
            <div>
                <div id="wydzial"><div style="border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;"><span class="glyphicon glyphicon-th-list"></span> Akutalna lista kontrolerów:</div></div>
            </div>
            <div class="panel panel-default" style="padding: 15px;">
                <c:forEach items="${list}" var="controller"><c:url var="href" value="/controller?id=${controller.id}"/>
                    <div>
                        <a href="${href}" class="list-group-item" style="max-width: 300px;">
                            <span class="glyphicon glyphicon-menu-right"></span>
                            <c:out value="${controller.name}"/>
                            <c:out value="${controller.ipv4}"/>
                            <c:out value="${controller.description}"/>
                        </a>
                    </div>
                </c:forEach>
                <div>
                    <a href="/create-controller" class="btn btn-success" role="button" style="width: 300px;font-size:17px;">
                        <span class="glyphicon glyphicon-plus"></span>
                        Dodaj nowy kontroler
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/jquery-3.1.1.min.js"></script>
</body>
</html>