<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%--<%@ page language="java" contentType="text/html; charset=UTF-8" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html lang="pl">--%>
<%--<head>--%>
    <%--<meta charset="utf-8">--%>
    <%--<meta http-equiv="X-UA-Compatible" content="IE=edge">--%>
    <%--<meta name="viewport" content="width=device-width, initial-scale=1">--%>
    <%--<title>Logowanie</title>--%>
    <%--<link rel="icon" href="/favicon.ico">--%>
    <%--<link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">--%>
    <%--<link rel="stylesheet" href="/css/loginStyle.css">--%>
    <%--<link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet'--%>
          <%--type='text/css'>--%>
<%--</head>--%>
<%--<body>--%>
<%--<div class="container">--%>
    <%--<c:if test="${param.error != null}"><h1 class="error">--%>
        <%--Musisz być zalogowany by wejść na tą stronę--%>
    <%--</h1></c:if>--%>
    <%--<div class="tittle">NETWORK-MONITOR</div>--%>
    <%--<br><br>--%>
    <%--<form action="/login" method="post">--%>
        <%--<div class="loginFields">--%>
            <%--<table style="width:50px;">--%>
                <%--<tr>--%>
                    <%--<td>Login:&nbsp;</td>--%>
                    <%--<td><input type="text" name="username"--%>
                               <%--style="z-index: 100; font-weight: bold;color:black;"></td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td>Hasło:&nbsp;</td>--%>
                    <%--<td><input type="password" name="password"--%>
                               <%--style="z-index: 100;font-weight: bold; color:black;"></td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td></td>--%>
                    <%--<td><input type="submit" value="Zaloguj"--%>
                               <%--style="width:100%;color:black;font-weight: bold;z-index: 100;"></td>--%>
                <%--</tr>--%>
            <%--</table>--%>
        <%--</div>--%>
    <%--</form>--%>
    <%--<c:if test="${!empty failed and failed}"><h1 class="error">--%>
        <%--Podano zły login lub hasło--%>
    <%--</h1></c:if>--%>
<%--</div>--%>
<%--<script src="/js/jquery-3.1.1.min.js"></script>--%>
<%--<script src="/js/bootstrap-3.3.7.min.js"></script>--%>
<%--</body>--%>
<%--</html>--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pl">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Network Monitor</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/loginStyle.css">
    <link rel="stylesheet" href="/css/metisMenu.min.css" media="screen">
    <link rel="stylesheet" href="/css/sb-admin-2.css" media="screen">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>

</head>

<body>

<div class="container">
    <c:if test="${param.error != null}"><h1 class="error">
        Musisz być zalogowany by wejść na tą stronę
    </h1></c:if>
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="login-panel panel panel-default">
                <div class="panel-heading" style="background-color: #121612;color:#cdd6cd;">
                    <h3 class="panel-title" style="font-size:17px !important;">Logowanie</h3>
                </div>
                <div class="panel-body">
                    <form action="/login" method="post" role="form">
                        <fieldset>
                            <div class="form-group has-feedback">
                                <div class="input-group">
                                    <div class="input-group-addon">
                                        <i class="glyphicon glyphicon-user"></i>
                                    </div>
                                    <input class="form-control" placeholder="Nazwa użytkownika..." name="username" type="text" autofocus>
                                </div>
                            </div>
                            <div class="form-group has-feedback">
                                <div class="input-group">
                                    <div class="input-group-addon">
                                        <i class="glyphicon glyphicon-lock"></i>
                                    </div>
                                    <input class="form-control" placeholder="Hasło..." name="password" type="password" value="">
                                </div>
                            </div>
                            <div class="checkbox">
                                <label>
                                    <input name="remember" type="checkbox" value="Remember Me">Zapamiętaj mnie
                                </label>
                            </div>
                            <%--<input class="btn btn-lg btn-success btn-block" type="submit" value="Zaloguj">--%>
                            <button type="submit" class="btn btn-lg btn-success btn-block">
                                <span class="glyphicon glyphicon-log-in"></span> &nbsp;&nbsp;Zaloguj
                            </button>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <c:if test="${!empty failed and failed}"><h1 class="error">
        Podano zły login lub hasło
    </h1></c:if>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="/js/metisMenu.min.js"></script>
<script src="/js/sb-admin-2.js"></script>

</body>

</html>
