<%--
  Created by IntelliJ IDEA.
  User: Berent
  Date: 2016-12-21
  Time: 00:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="zesp03.data.ControllerRow" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    ArrayList<ControllerRow> controllers = (ArrayList<ControllerRow>)request.getSession().getAttribute("controllers");
%>

<!DOCTYPE html>
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
                    <%= controllers.get(i).getIPv4() %>
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