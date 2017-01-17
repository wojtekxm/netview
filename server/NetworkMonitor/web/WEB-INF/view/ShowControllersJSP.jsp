<%@ page import="zesp03.entity.Controller"
%>
<%@ page import="java.util.List"
%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%>
<%
    List<Controller> controllers = (List<Controller>)request.getAttribute("controllers");
%><!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <title>Network Monitor</title>
</head>
<body>
    <form method="POST">
        <table>
            <tr>
                <th>Nazwa</th>
                <th>Adres IP</th>
                <th>Opis</th>
                <th>Usunac</th>
            </tr>
            <% %>
            <% for (int i = 1; i < controllers.size(); ++i) { %>
            <tr>
                <td>
                    <%= controllers.get(i).getName() %>
                </td>
                <td>
                    <%= controllers.get(i).getIpv4() %>
                </td>
                <td>
                    <%= controllers.get(i).getDescription() %>
                </td>
                <td>
                    <input type="checkbox" name="ids-to-delete" value="<%= controllers.get(i).getId()%>"/>
                </td>
            </tr>
            <% } %>
            <% %>
        </table>
        <input type="submit" value="Usun"/>
        <a href="javascript:history.back();"><input type="button" value="Wstecz"></a>
    </form>

</body>
</html>