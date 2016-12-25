<%@ page import="zesp03.data.CheckInfo" %>
<%@ page import="zesp03.servlet.StatusSmall" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="zesp03.servlet.Details" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ArrayList<CheckInfo> attrList = (ArrayList<CheckInfo>)request.getAttribute(StatusSmall.ATTR_LIST);
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Network Monitor</title>
    <link rel="icon" href="favicon.png">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link rel="stylesheet" href="css/status-small.css">
</head>
<body>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="index.jsp">Network Monitor</a>
            </div>
            <ul class="nav navbar-nav">
                <li><a href="index.jsp">strona główna</a></li>
                <li><a href="make-survey">nowe badania</a></li>
                <li class="active"><a href="status-small">stan urządzeń (mały widok)</a></li>
            </ul>
        </div>
    </nav>
    <div class="container">
        <ul class="view-small"><%
            int sumActive = 0;
            int sumInactive = 0;
            int sumDisabled = 0;
            for(final CheckInfo info : attrList) {
                String c;
                if( info.survey().isEnabled() ) {
                    if( info.survey().getClientsSum() > 0 ) {
                        c = "square-green";
                        sumActive++;
                    }
                    else {
                        c = "square-red";
                        sumInactive++;
                    }
                }
                else {
                    c = "square-gray";
                    sumDisabled++;
                }
                final String h = "details?" + Details.PARAM_ID + "=" + info.device().getId();
        %><li class="<%= c %>"><a href="<%= h %>"></a></li
        ><% } %></ul>
        <hr>
        <div class="summary">
            <div class="square-green"></div> aktywne: <%= sumActive %><br>
            <div class="square-red"></div> nieaktywne: <%= sumInactive %><br>
            <div class="square-gray"></div> wyłączone: <%= sumDisabled %><br>
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"
        integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8=" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</body>
</html>
