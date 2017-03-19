<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/AddControllers.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <title>Dodawanie kontrolera</title>
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
<div class="welcome">
    <div class="tittleStatic"><img src="/images/icon.ico" style="padding-bottom: 5px;"> &nbsp; NETWORK-MONITOR</div>
    <div class="userStatic">zalogowany: <c:out value="${loggedUser.name}"/>
    </div>
    <div class="logo"><img src="/images/logooWhite.jpg"></div>
</div>
<div id="strona">
    <div id="kontent">
        <div class="form-group">
            <form method="post" action="/api/create-controller">
                <input type="text" class="form-control" placeholder="Nazwa Kontrolera"
                       required="required" name="name">
                <input type="text" class="form-control" placeholder="Adres IPv4"
                       required="required" name="ipv4">
                <input type="text" class="form-control" placeholder="Komentarz (opcjonalne)"
                       name="description">
                <input type="submit" value="Dodaj kontroler" class="btn btn-primary btn-default btn-lg active">
            </form>
            <br>
        </div>
    </div>
</div>
</body>
</html>