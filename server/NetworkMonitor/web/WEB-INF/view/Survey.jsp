<%@ page import="zesp03.servlet.Survey" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Double attrTime = (Double)request.getAttribute(Survey.ATTR_TIME);
    Integer attrRows = (Integer)request.getAttribute(Survey.ATTR_ROWS);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Network Monitor</title>
    <link rel="icon" href="favicon.png">
</head>
<body>
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
    <form method="post" action="survey">
        <input type="hidden" name="<%= Survey.PARAM_ACTION %>" value="<%= Survey.PARAM_ACTION_UPDATE %>">
        <input type="submit" value="Nowe badanie">
    </form><br>
    <a href="index.jsp">index.jsp</a><br>
</body>
</html>
