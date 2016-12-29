<%@ page import="zesp03.servlet.MakeSurvey"
%><%@ page import="java.util.Locale"
%><%@ page contentType="text/html;charset=UTF-8" language="java"
%><%
    Double attrTime = (Double)request.getAttribute(MakeSurvey.ATTR_TIME);
    Integer attrRows = (Integer)request.getAttribute(MakeSurvey.ATTR_ROWS);
%><!DOCTYPE html>
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
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="index.jsp">Network Monitor</a>
            </div>
            <ul class="nav navbar-nav">
                <li class="active"><a href="make-survey">nowe badania</a></li>
                <li><a href="status-small">stan urządzeń (mały widok)</a></li>
            </ul>
            <hr>
        </div>
    </nav>
    <div class="container">
        <%
            if(attrTime != null && attrRows != null) {
        %>
        <p>
            Badanie zostało pomyślnie wykonane w czasie <%= String.format(Locale.US, "%.3f", attrTime) %>s.<br>
            Tabela device_survey zawiera teraz <%= attrRows %> rekordów.
        </p>
        <%
            }
        %>
        <p>Kliknij przycisk poniżej by wykonać nowe badanie sieci</p>
        <form method="post" action="make-survey">
            <input type="hidden" name="<%= MakeSurvey.PARAM_ACTION %>" value="<%= MakeSurvey.PARAM_ACTION_UPDATE %>">
            <input type="submit" value="Nowe badanie">
        </form>
    </div>
    <script src="/js/jquery-3.1.1.min.js"></script>
    <script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>
