<%@ page import="zesp03.data.row.UserRow" %>
<%@ page import="zesp03.filter.AuthenticationFilter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    session.setAttribute("style", "loggedStyleWhite");
    session.setAttribute("logo", "logooWhite");
    UserRow userRow = (UserRow) request.getAttribute(AuthenticationFilter.ATTR_USERROW);
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Strona główna</title>
    <link rel="icon" href="/favicon.png">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
</head>
<body>
<div class="container">
    <h1>
        Zalogowany jako: <%= userRow.getName() %> <span class="label label-default">administrator</span>
    </h1>
    <h1>Network Monitor</h1>
    <div class="row">
        <ul class="nav nav-pills nav-stacked">
            <li>
                <a href="/status">
                    stan urządzeń
                    <span class="label label-default">status</span>
                </a>
            </li>
            <li>
                <a href="/status-small">
                    stan urządzeń (pomniejszony widok)
                    <span class="label label-default">status-small</span>
                </a>
            </li>
            <li>
                <a href="/make-survey">
                    nowe badania sieci
                    <span class="label label-default">make-survey</span>
                </a>
            </li>
            <li>
                <a href="/all-controllers">
                    kontrolery
                    <span class="label label-default">all-controllers</span>
                </a>
            </li>
            <li>
                <a href="/add-controller">
                    nowy kontroler
                    <span class="label label-default">add-controller</span>
                </a>
            </li>
            <li>
                <a href="/logout">
                    wylogowanie
                    <span class="label label-default">logout</span>
                </a>
            </li>
        </ul>
    </div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
</body>
</html>