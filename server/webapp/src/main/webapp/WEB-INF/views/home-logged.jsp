<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Strona główna</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/progress.css">
</head>
<body>
<div class="container">
    <h1>
        Zalogowany jako: <c:out value="${loggedUser.name}"/>
        <span class="label label-default"><c:choose
            ><c:when test="${loggedUser.role eq 'ROOT'}">root</c:when
            ><c:when test="${loggedUser.role eq 'NORMAL'}">zwykły użytkownik</c:when
        ></c:choose></span>
    </h1>
    <h1>Network Monitor</h1>
    <div class="row">
        <ul class="nav nav-pills nav-stacked">
            <li>
                <a href="/account">
                    zarządzaj kontem
                    <span class="label label-default">account</span>
                </a>
            </li>
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
                <a href="/create-controller">
                    nowy kontroler
                    <span class="label label-default">create-controller</span>
                </a>
            </li>
            <li>
                <a href="/all-controllers">
                    kontrolery
                    <span class="label label-default">all-controllers</span>
                </a>
            </li>
            <li>
                <a href="/unitsbuildings">
                    jed. bud.
                    <span class="label label-default">building</span>
                </a>
            </li>
            <li>
                <a href="/all-buildings">
                    budynki
                    <span class="label label-default">all-buildings</span>
                </a>
            </li>
            <li>
                <a href="/all-units">
                    jednostki
                    <span class="label label-default">all-units</span>
                </a>
            </li>
            <c:if test="${loggedUser.role.name() eq 'ROOT'}"><li>
                <a href="/all-users">
                    użytkownicy
                    <span class="label label-default">all-users</span>
                </a>
            </li></c:if>
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