<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.Random" %>
<%@ page import="java.io.FileNotFoundException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Kacper
  Date: 2016-12-15
  Time: 17:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge; chrome=1"/>
    <title>Network Monitor</title>
    <link rel="stylesheet" href="/css/style.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>

</head>

<body>
<div id="all">
<div id="container">
    <div id="logo" >
        <a href="index.html"><img src="/images/logo2.jpg"></a>
    </div>
    <!--   	<div id="menu"></div> -->
    <div id="content">
        <ul id="accessPoints">
            <li> <div id="wydzial">Wszystkie kontrolery</div>
                <ul>
                    <jsp:useBean id="random" class="java.util.Random" scope="application" />
                    <c:forEach var="d" items="${devicesList}" varStatus="status">
                    <%--<c:forEach var="i" begin="0" end="400">--%>
                            <li id=${d}>${countList[status.index]}</li>
                    </c:forEach>
                </ul>
            </li>
        </ul>
    </div>
</div>
</div>
</body>
</html>