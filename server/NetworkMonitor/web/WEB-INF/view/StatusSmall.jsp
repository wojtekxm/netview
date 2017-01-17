<%@ page import="zesp03.data.DeviceStatus"
%><%@ page import="zesp03.servlet.Details"
%>
<%@ page import="zesp03.servlet.StatusSmall"
%><%@ page import="java.util.List"
%><%@ page contentType="text/html;charset=UTF-8" language="java"
%><%
    List<DeviceStatus> states = (List<DeviceStatus>) request.getAttribute(StatusSmall.ATTR_STATES);
%><!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Network Monitor</title>
    <link rel="icon" href="/favicon.png">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/status-small.css">
</head>
<body>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="/index.jsp">Network Monitor</a>
            </div>
            <ul class="nav navbar-nav">
                <li><a href="/make-survey">nowe badania</a></li>
                <li class="active"><a href="/status-small">urządzenia (mały widok)</a></li>
                <li><a href="/deviceinfo">urządzenia (średni widok)</a></li>
                <li><a href="/ShowControllers">kontrolery</a></li>
                <li><a href="/logout">wyloguj</a></li>
            </ul>
        </div>
    </nav>
    <div class="container">
        <ul class="view-small"><%
            int sumActive = 0;
            int sumInactive = 0;
            int sumDisabled = 0;
            for (final DeviceStatus info : states) {
                String clazz;
                if( info.getSurvey().isEnabled() ) {
                    if( info.getSurvey().getClientsSum() > 0 ) {
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
                final String h = "/details?" + Details.PARAM_ID + "=" + info.getDevice().getId();
                String t = info.getDevice().getName();
                if( info.getDevice().getDescription() != null )t += "<br>opis: " + info.getDevice().getDescription();
                t += "<br>z: " + info.getController().getName();
        %><li class="<%= clazz %>" title="<%= t %>" data-toggle="tooltip" data-html="true"><a href="<%= h %>"></a></li
        ><% } %></ul>
        <hr>
        <div class="summary">
            <div class="square-green"></div> aktywne: <%= sumActive %><br>
            <div class="square-red"></div> nieaktywne: <%= sumInactive %><br>
            <div class="square-gray"></div> wyłączone: <%= sumDisabled %><br>
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
