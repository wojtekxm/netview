<%@ page import="zesp03.data.DeviceData" %>
<%@ page import="zesp03.servlet.DeviceServlet" %>
<%@ page import="zesp03.servlet.StatusSmallServlet" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<DeviceData> list = (List<DeviceData>) request.getAttribute(StatusSmallServlet.ATTR_LIST);
    Double time = (Double) request.getAttribute(StatusSmallServlet.ATTR_TIME);
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Status sieci (minimalistyczny)</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/status-small.css">
</head>
<body>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="/">Network Monitor</a>
            </div>
            <ul class="nav navbar-nav">
                <li><a href="/make-survey">nowe badania</a></li>
                <li class="active"><a href="/status-small">urządzenia (mały widok)</a></li>
                <li><a href="/status">urządzenia (średni widok)</a></li>
                <li><a href="/all-controllers">kontrolery</a></li>
                <li><a href="/building">budynki</a></li>
                <li><a href="/logout">wyloguj</a></li>
            </ul>
        </div>
    </nav>
    <div class="container">
        <ul class="view-small"><%
            int sumActive = 0;
            int sumInactive = 0;
            int sumDisabled = 0;
            for (final DeviceData d : list) {
                String clazz;
                if (d.isEnabled()) {
                    if (d.getClientsSum() > 0) {
                        clazz = "square-green";
                        sumActive++;
                    }
                    else {
                        clazz = "square-red";
                        sumInactive++;
                    }
                }
                else {
                    clazz = "square-gray";
                    sumDisabled++;
                }
                final String h = "/device?" + DeviceServlet.GET_ID + "=" + d.getId();
                String t = d.getName();
                if( d.getDescription() != null )t += "<br>opis: " + d.getDescription();
        %><li class="<%= clazz %>" title="<%= t %>" data-toggle="tooltip" data-html="true"><a href="<%= h %>"></a></li
        ><% } %></ul>
        <hr>
        <div class="summary">
            <div class="square-green"></div> aktywne: <%= sumActive %><br>
            <div class="square-red"></div> nieaktywne: <%= sumInactive %><br>
            <div class="square-gray"></div> wyłączone: <%= sumDisabled %><br>
            <small>sprawdzanie stanów zajęło <%= String.format("%.3f", time) %> sek.</small>
            <br>
        </div>
    </div>
    <script src="/js/jquery-3.1.1.min.js"></script>
    <script src="/js/bootstrap-3.3.7.min.js"></script>
    <script>
        $(function() {
            $('[data-toggle="tooltip"]').tooltip();
        })
    </script>
</body>
</html>
