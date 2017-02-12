<%@ page import="zesp03.data.row.ControllerRow" %>
<%@ page import="zesp03.data.row.DeviceRow" %>
<%@ page import="zesp03.data.row.DeviceSurveyRow" %>
<%@ page import="zesp03.servlet.DeviceServlet" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    DeviceRow device = (DeviceRow) request.getAttribute(DeviceServlet.ATTR_DEVICE);
    ControllerRow controller = (ControllerRow) request.getAttribute(DeviceServlet.ATTR_CONTROLLER);
    List<DeviceSurveyRow> selectedSurveys = (List<DeviceSurveyRow>) request.getAttribute(DeviceServlet.ATTR_SELECTED_SURVEYS);
    Integer totalSurveys = (Integer) request.getAttribute(DeviceServlet.ATTR_TOTAL_SURVEYS);
    Integer historyLimit = (Integer) request.getAttribute(DeviceServlet.ATTR_HISTORY_LIMIT);
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Informacje o urządzeniu</title>
    <link rel="icon" href="/favicon.png">
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
            <li><a href="/status-small">urządzenia (mały widok)</a></li>
            <li><a href="/status">urządzenia (średni widok)</a></li>
            <li><a href="/all-controllers">kontrolery</a></li>
            <li><a href="/logout">wyloguj</a></li>
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
                <td><%= device.getName() %>
                </td>
            </tr>
            <tr>
                <td>opis</td>
                <td><%= device.getDescription() != null ? device.getDescription() : "<em>(brak)</em>" %>
                </td>
            </tr>
            <tr>
                <td>nazwa kontrolera</td>
                <td><%= controller.getName() %>
                </td>
            </tr>
            <tr>
                <td>IP kontrolera</td>
                <td><%= controller.getIpv4() %>
                </td>
            </tr>
            <tr>
                <td>opis kontrolera</td>
                <td><%= controller.getDescription() != null ? controller.getDescription() : "<em>(brak)</em>" %>
                </td>
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
                for (final DeviceSurveyRow survey : selectedSurveys) {
                    final Instant instant = Instant.ofEpochSecond(survey.getTimestamp());
                    final ZoneId zone = ZoneId.systemDefault();
                    final LocalDateTime ld = LocalDateTime.ofInstant(instant, zone);
                    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    final String time = ld.format(formatter);
            %>
            <tr>
                <td><%= n++ %>
                </td>
                <td><%= time %>
                </td>
                <td><%= survey.isEnabled() ? "tak" : "nie" %>
                </td>
                <td><%= survey.getClientsSum() %>
                </td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
        <div class="panel-footer">
            Łącznie jest <%= totalSurveys %> wyników.
            Liczba wyników na stronę
            <form method="get" action="/device" style="display: inline"><%
                final ArrayList<Integer> limits = new ArrayList<>();
                limits.add(10);
                limits.add(20);
                limits.add(50);
                limits.add(100);
                limits.add(200);
                limits.add(500);
                limits.add(1000);
                if (!limits.contains(historyLimit))
                    limits.add(historyLimit);
                Collections.sort(limits);
            %>
                <input type="hidden" name="id" value="<%= device.getId() %>">
                <select name="<%= DeviceServlet.GET_HISTORY_LIMIT %>"><%
                    for (Integer i : limits) {
                %>
                    <option value="<%= i %>" <%= i.equals(historyLimit) ? "selected" : "" %>><%= i %>
                    </option>
                    <%
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