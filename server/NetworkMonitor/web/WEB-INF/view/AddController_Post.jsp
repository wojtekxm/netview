<%@ page import="zesp03.data.row.ControllerRow" %>
<%@ page import="zesp03.servlet.AddControllerServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ControllerRow controllerRow = (ControllerRow) request.getAttribute(AddControllerServlet.ATTR_CONTROLLERDATA);
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
nazwa: <%= controllerRow.getName() %><br>
adres IP: <%= controllerRow.getIpv4() %><br>
opis: <%= controllerRow.getDescription() == null ? "(brak)" : controllerRow.getDescription() %><br>
id: <%= controllerRow.getId() %><br>
</body>
</html>
