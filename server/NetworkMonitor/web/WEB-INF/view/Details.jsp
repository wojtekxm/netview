<%@ page import="zesp03.servlet.Details"
%><%@ page import="zesp03.data.ControllerRow"
%><%@ page import="zesp03.data.DeviceRow"
%><%@ page import="zesp03.data.SurveyRow"
%><%@ page import="java.util.ArrayList"
%><%@ page import="java.time.Instant"
%><%@ page import="java.time.LocalDateTime"
%><%@ page import="java.time.ZoneId"
%><%@ page import="java.time.format.DateTimeFormatter"
%><%@ page import="java.util.Collections"
%><%@ page contentType="text/html;charset=UTF-8" language="java"
%><%
    ControllerRow attrController = (ControllerRow)request.getAttribute(Details.ATTR_CONTROLLER);
    DeviceRow attrDevice = (DeviceRow)request.getAttribute(Details.ATTR_DEVICE);
    ArrayList<SurveyRow> attrSurveyList = (ArrayList<SurveyRow>)request.getAttribute(Details.ATTR_SURVEY_LIST);
    int attrSurveysNumber = (Integer)request.getAttribute(Details.ATTR_SURVEYS_NUMBER);
    int attrHistoryLimit = (Integer)request.getAttribute(Details.ATTR_HISTORY_LIMIT);
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
                <li><a href="/status-small">stan urządzeń (mały widok)</a></li>
            </ul>
        </div>
    </nav>
    <div class="container">
        <div class="panel panel-default">
            <div class="panel-heading">
                szczegóły urządzenia
            </div>
            <table class="table table-bordered">
                <tr>
                    <td>nazwa</td>
                    <td><%= attrDevice.getName() %></td>
                </tr>
                <tr>
                    <td>opis</td>
                    <td><%= attrDevice.getDescription() != null ? attrDevice.getDescription() : "(brak)" %></td>
                </tr>
                <tr>
                    <td>nazwa kontrolera</td>
                    <td><%= attrController.getName() %></td>
                </tr>
                <tr>
                    <td>IP kontrolera</td>
                    <td><%= attrController.getIPv4() %></td>
                </tr>
                <tr>
                    <td>opis kontrolera</td>
                    <td><%= attrController.getDescription() != null ? attrController.getDescription() : "(brak)" %></td>
                </tr>
            </table>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">
                historia badań
            </div>
            <table class="table table-condensed table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>data</th>
                    <th>uruchomione</th>
                    <th>liczba klientów</th>
                </tr>
                </thead>
                <tbody><%
                    int n = 1;
                    for(final SurveyRow row : attrSurveyList) {
                        final Instant instant = Instant.ofEpochSecond( row.getTimestamp() );
                        final ZoneId zone = ZoneId.systemDefault();
                        final LocalDateTime ld = LocalDateTime.ofInstant(instant, zone);
                        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        final String time = ld.format(formatter);
                %>
                <tr>
                    <td><%= n++ %></td>
                    <td><%= time %></td>
                    <td><%= row.isEnabled() ? "tak" : "nie" %></td>
                    <td><%= row.getClientsSum() %></td>
                </tr><%
                    }
                %>
                </tbody>
            </table>
            <div class="panel-footer">
                Łącznie jest <%= attrSurveysNumber %> wyników.
                Liczba wyników na stronę
                <form method="get" action="/details" style="display: inline"><%
                    final ArrayList<Integer> limits = new ArrayList<>();
                    limits.add(10);
                    limits.add(20);
                    limits.add(50);
                    limits.add(100);
                    limits.add(200);
                    limits.add(500);
                    limits.add(1000);
                    if( ! limits.contains(attrHistoryLimit) )
                        limits.add(attrHistoryLimit);
                    Collections.sort(limits);
                    %>
                    <input type="hidden" name="id" value="<%= attrDevice.getId() %>">
                    <select name="<%= Details.PARAM_HISTORY_LIMIT %>"><%
                        for(Integer i : limits) {
                        %>
                        <option value="<%= i %>" <%= i.equals(attrHistoryLimit) ? "selected" : "" %>><%= i %></option><%
                        }
                        %>
                    </select>
                    <input type="submit" value="Wyświetl">
                </form>
            </div>
        </div>
    </div>
    <script src="/js/jquery-3.1.1.min.js"></script>
    <script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>
