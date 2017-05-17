<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Resetowanie hasła</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
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
<div class="container page">
    <div id="before" class="panel panel-default">
        <div class="panel-heading">
            Resetowanie hasła
        </div>
        <div class="panel-body">
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label">wybierz hasło</label>
                    <div class="col-sm-6">
                        <input id="password" type="password" class="form-control">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">powtórz hasło</label>
                    <div class="col-sm-6">
                        <input id="password_repeat" type="password" class="form-control">
                    </div>
                </div>
                <div class="form-group text-center">
                    <button id="btn_submit" class="btn btn-primary">
                        Zapisz
                    </button>
                    <div class="progress-space" style="display:inline-block">
                        <div id="reset_loading"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="after" class="panel panel-default later">
        <div class="panel-body">
            <h4>Hasło zostało zresetowane, możesz się <a href="/login">zalogować.</a></h4>
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
        var btnSubmit, divBefore, divAfter;
        divBefore = $('#before');
        divAfter = $('#after');
        btnSubmit = $('#btn_submit');
        btnSubmit.click(function() {
            var resetPasswordDto;
            resetPasswordDto = {
                "tokenId" : ${param.tid},
                "tokenValue" : '${param.tv}',
                "desired" : $('#password').val(),
                "repeat" : $('#password_repeat').val()
            };
            progress.load(
                [{
                    "url" : '/api/user/finish-reset-password',
                    "method" : 'post',
                    "postData" : resetPasswordDto
                }],
                ['#reset_loading'], [], [],
                function() {
                    divBefore.fadeOut(500, function() {
                        divAfter.fadeIn(500);
                    });
                }, function() {
                    notify.danger('Niepowodzenie');
                }
            );
        });
    });
</script>
</body>
</html>