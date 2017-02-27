<%@ page import="zesp03.servlet.AddControllerServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/AddControllers.css">
    <title>Dodawanie kontrolera</title>
</head>
<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/"><b>Network Monitor</b></a>
        </div>
    </div>
</nav>
<div id="strona">
    <div id="kontent">
        <div class="form-group">
            <form method="post" action="/add-controller">
                <input type="text" class="form-control" placeholder="Nazwa Kontrolera" onkeyup="nospaces(this)"
                       required="required" name="<%= AddControllerServlet.POST_NAME %>">
                <input type="text" class="form-control" placeholder="Adres IPv4" onkeyup="nospaces(this)"
                       required="required" name="<%= AddControllerServlet.POST_IP %>">
                <input type="text" class="form-control" placeholder="Komentarz (opcjonalne)" onkeyup="nospaces(this)"
                       name="<%= AddControllerServlet.POST_DESCRIPTION %>">
                <input type="submit" value="Dodaj kontroler" class="btn btn-primary btn-default btn-lg active">
            </form>
            <br>
        </div>
    </div>
</div>
</body>
</html>
