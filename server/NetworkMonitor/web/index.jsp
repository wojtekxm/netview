<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    session.setAttribute("style", "loggedStyleBlack");
    session.setAttribute("logo", "logoo");
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Network Monitor</title>
    <link rel="icon" href="/favicon.png">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
</head>
<body>
    <div class="container">
        <h1>Network Monitor</h1>
        <div class="row">
            <ul class="nav nav-pills nav-stacked">
                <li>
                    <a href="/index.jsp">
                        strona główna
                        <span class="label label-default">index.jsp</span>
                    </a>
                </li>
                <li>
                    <a href="/make-survey">
                        nowe badania sieci
                        <span class="label label-default">make-survey</span>
                    </a>
                </li>
                <li>
                    <a href="/status-small">
                        stan urządzeń (pomniejszony widok)
                        <span class="label label-default">status-small</span>
                    </a>
                </li>
                <li>
                    <a href="/deviceinfo">
                        stan urządzeń (standardowy widok)
                        <span class="label label-default">deviceinfo</span>
                    </a>
                </li>
                <li>
                    <a href="/ShowControllers">
                        kontrolery
                        <span class="label label-default">ShowControllers</span>
                    </a>
                </li>
                <li>
                    <a href="/Controllers.jsp">
                        nowy kontroler
                        <span class="label label-default">Controllers.jsp</span>
                    </a>
                </li>
                <li>
                    <a href="/all-users">
                        użytkownicy
                        <span class="label label-default">all-users</span>
                    </a>
                </li>
                <li>
                    <a href="/login">
                        logowanie
                        <span class="label label-default">login</span>
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