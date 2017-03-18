<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Logowanie</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/loginStyle.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet'
          type='text/css'>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
</head>
<body>
<div class="container">
    <c:if test="${param.error != null}"><h1 class="error">
        Musisz być zalogowany by wejść na tą stronę
    </h1></c:if>
    <div class="tittle">NETWORK-MONITOR</div>
    <br><br>
    <form action="/login" method="post">
        <div class="loginFields">
            <table style="width:50px;">
                <tr>
                    <td>Login:&nbsp;</td>
                    <td><input type="text" name="username"
                               style="z-index: 100; font-weight: bold;color:black;"></td>
                </tr>
                <tr>
                    <td>Hasło:&nbsp;</td>
                    <td><input type="password" name="password"
                               style="z-index: 100;font-weight: bold; color:black;"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="Zaloguj"
                               style="width:100%;color:black;font-weight: bold;z-index: 100;"></td>
                </tr>
            </table>
        </div>
    </form>
    <c:if test="${!empty failed and failed}"><h1 class="error">
        Podano zły login lub hasło
    </h1></c:if>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>