<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Aktywacja konta</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container">
    <h1>Aktywacja konta użytkownika</h1>
    <form action="/api/activate-account" method="post">
        <input type="hidden" name="tid" value="${param.tid}">
        <input type="hidden" name="tv" value="${param.tv}">
        nazwa użytkownika<br>
        <input type="text" name="username"><br><br>
        wybierz hasło<br>
        <input type="password" name="password"><br><br>
        powtórz hasło<br>
        <input type="password" name="repeat"><br><br>
        <button type="submit">Aktywuj konto</button>
    </form>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>