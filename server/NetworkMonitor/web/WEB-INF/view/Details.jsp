<%@ page import="zesp03.servlet.Details" %>
<%@ page import="zesp03.data.ControllerRow" %>
<%@ page import="zesp03.data.DeviceRow" %>
<%@ page import="zesp03.data.SurveyRow" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ControllerRow attrController = (ControllerRow)request.getAttribute(Details.ATTR_CONTROLLER);
    DeviceRow attrDevice = (DeviceRow)request.getAttribute(Details.ATTR_DEVICE);
    ArrayList<SurveyRow> attrSurveys = (ArrayList<SurveyRow>)request.getAttribute(Details.ATTR_SURVEYS);
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
                    for(final SurveyRow row : attrSurveys) {
                        final Instant instant = Instant.ofEpochSecond( row.getTimestamp() );
                        final ZoneId zone = ZoneId.systemDefault();
                        final LocalDateTime ld = LocalDateTime.ofInstant(instant, zone);
                        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy kk:mm:ss");
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
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"
        integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8=" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</body>
</html>
