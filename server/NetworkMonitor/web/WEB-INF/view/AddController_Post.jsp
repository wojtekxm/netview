<%@ page import="zesp03.data.ControllerData" %>
<%@ page import="zesp03.servlet.AddControllerServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ControllerData controllerData = (ControllerData) request.getAttribute(AddControllerServlet.ATTR_CONTROLLERDATA);
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dodawanie kontrolera</title>
    <link rel="icon" href="/favicon.png">
</head>
<body>
Dodano nowy kontroler<br>
nazwa: <%= controllerData.getName() %><br>
adres IP: <%= controllerData.getIpv4() %><br>
opis: <%= controllerData.getDescription() == null ? "(brak)" : controllerData.getDescription() %><br>
id: <%= controllerData.getId() %><br>
</body>
</html>
