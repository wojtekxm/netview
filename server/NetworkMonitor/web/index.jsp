<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Network Monitor</title>
    <link rel="icon" href="favicon.png">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
</head>
<body>
    <div class="container">
        <h1>Network Monitor</h1>
        <div class="row">
            <ul class="nav nav-pills nav-stacked">
                <li><a href="index.jsp">strona główna (index.jsp)</a></li>
                <li><a href="make-survey">robienie nowych badań (make-survey)</a></li>
                <li><a href="status-small">stan wszystkich urządzeń, pomniejszony widok (status-small)</a></li>
                <li><a href="showDevices">stan wszystkich urządzeń, tabelka (showDevices)</a></li>
                <li><a href="Controllers.jsp">dodawanie kontrolerów (Controllers.jsp)</a></li>
                <li><a href="ShowControllersServlet">usuwanie kontrolerów (ShowControllersServlet)</a></li>
                <li><a href="AddController">??? (AddController)</a></li><br><br>
                <li><a href="deviceinfo">LOGOWANIE, stan wszystkich urządzeń, standardowy widok (deviceinfo)</a></li>
            </ul>
        </div>
    </div>
    <script src="/js/jquery-3.1.1.min.js"></script>
    <script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>