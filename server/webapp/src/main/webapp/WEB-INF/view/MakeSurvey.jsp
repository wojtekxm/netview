<%@ page import="zesp03.servlet.MakeSurveyServlet" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Double time = (Double) request.getAttribute(MakeSurveyServlet.ATTR_TIME);
    Long rows = (Long) request.getAttribute(MakeSurveyServlet.ATTR_ROWS);
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Nowe badanie</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
</head>
<body>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="/">Network Monitor</a>
            </div>
            <ul class="nav navbar-nav">
                <li class="active"><a href="/make-survey">nowe badania</a></li>
                <li><a href="/status-small">urządzenia (mały widok)</a></li>
                <li><a href="/status">urządzenia (średni widok)</a></li>
                <li><a href="/all-controllers">kontrolery</a></li>
                <li><a href="/building">budynki</a></li>
                <li><a href="/logout">wyloguj</a></li>
            </ul>
            <hr>
        </div>
    </nav>
    <div class="container">
        <%
            if (time != null && rows != null) {
        %>
        <p>
            Badanie zostało pomyślnie wykonane w czasie <%= String.format(Locale.US, "%.3f", time) %>s.<br>
            Tabela device_survey zawiera teraz <%= rows %> rekordów.
        </p>
        <%
            }
        %>
        <p>Kliknij przycisk poniżej by wykonać nowe badanie sieci</p>
        <form method="post" action="make-survey">
            <input type="hidden" name="<%= MakeSurveyServlet.POST_UPDATE %>" value="1">
            <input type="submit" value="Nowe badanie">
        </form>
    </div>
    <script src="/js/jquery-3.1.1.min.js"></script>
    <script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>
