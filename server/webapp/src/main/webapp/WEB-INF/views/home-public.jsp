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
    <link rel="stylesheet" href="/css/progress.css">
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
            <div class="login-panel panel panel-default" style="margin-bottom: 0px; padding-bottom: 0px;">
                <div class="panel-heading" style="background-color: #121612;color:#cdd6cd;">
                    <h3 class="panel-title" style="font-size:17px !important;">Network-Monitor</h3>
                </div>
                <div class="panel-body" style="display: none;">
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
                <div class="panel-footer" style="cursor: pointer;">
                    Logowanie
                </div>
            </div>
        </div>
    </div>
    <c:if test="${!empty failed and failed}"><h1 class="error">
        Podano zły login lub hasło
    </h1></c:if>
</div>

<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="/js/metisMenu.min.js"></script>
<script src="/js/sb-admin-2.js"></script>
<script>
    $(document).ready(function(){
        $(".panel-footer").click(function(){
            $(".panel-body").slideToggle("slow");
        });
    });
</script>
</body>

</html>
