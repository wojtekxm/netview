<%@ page import="zesp03.data.DeviceData" %>
<%@ page import="zesp03.data.row.UserRow" %>
<%@ page import="zesp03.filter.AuthenticationFilter" %>
<%@ page import="zesp03.servlet.DeviceServlet" %>
<%@ page import="zesp03.servlet.StatusServlet" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    List<DeviceData> list = (List<DeviceData>) request.getAttribute(StatusServlet.allDevicesString);
    UserRow userRow = (UserRow) request.getAttribute(AuthenticationFilter.ATTR_USERROW);
    String style = (String) session.getAttribute("style");
    String logo = (String) session.getAttribute("logo");
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge; chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Status sieci</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/css/<%= style %>.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
</head>
<body>
<%
    if(session.getAttribute("username")==null)
    {
        response.sendRedirect("LoginPage.jsp");
    }
%>
    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="collapse navbar-collapse">
            <ul class="nav nav-pills" style="padding-top: 3px;font-size: 17px;position: absolute;width: 100%;left: 0;text-align: center;margin:0 auto;">
                <li role="presentation"><a href="/make-survey">Nowe badanie</a></li>
                <li role="presentation"><a href="/status-small">Mały widok</a></li>
                <li role="presentation"><a href="/all-controllers">Kontrolery</a></li>
                <li role="presentation"><a href="/building">Budynki</a></li>

            </ul>
            <ul class="nav nav-pills pull-left" style="padding-top: 3px;border-radius: 10px;padding-left:7px;font-size: 17px;">
                <li role="presentation" class="active"><a href="/">Strona główna</a></li>
            </ul>
            <ul class="nav nav-pills pull-right" style="padding-top: 3px;padding-right:3px;font-size: 17px;">
                <li role="presentation"><a href="#"><span class="glyphicon glyphicon-user"></span> Mój profil</a></li>
                <li role="presentation"><a href="#"><span class="glyphicon glyphicon-log-out"></span> Wyloguj</a></li>
            </ul>
        </div>
    </nav>
<div id="all" class="container-fluid">
<div id="container">
    <div class="welcome">
        <div class="tittle">NETWORK-MONITOR</div>
        <div class="user">zalogowany: <%= userRow.getName() %>
        </div>
        <div class="logo"><img src="/images/<%= logo %>.jpg"></div>
    </div>
    <div id="content">
        <ul class="view" style="z-index: 1000;top:0;">
            <li> <div id="wydzial">Wszystkie kontrolery</div>
                <ul style="padding: 4px;border: 1px solid #e8e8e8;">
                    <%
                    int sumActive = 0;
                    int sumInactive = 0;
                    int sumDisabled = 0;
                        for (final DeviceData d : list) {
                            int sumUsers = d.getClientsSum();
                        String clazz;
                            if (d.isEnabled()) {
                                if (d.getClientsSum() > 0) {
                                clazz = "greenDiode";
                                sumActive++;
                            }
                            else {
                                clazz = "redDiode";
                                sumInactive++;
                            }
                        }
                        else {
                            clazz = "greyDiode";
                            sumDisabled++;
                        }

                            final String h = "/device?" + DeviceServlet.GET_ID + "=" + d.getId();
                        String t = d.getName();
                        if( d.getDescription() != null )t += "<br>opis: " + d.getDescription();
                    %><li class="<%= clazz %>" title="<%= t %>" data-toggle="tooltip" data-html="true"><a href="<%= h %>" style="text-decoration: none; color: white;"><%= sumUsers %></a></li
                    ><% } %>

                </ul>
            </li>
        </ul>
    </div>
    <%--<div class="btn-group btn-group-justified" role="group" aria-label="...">--%>
        <%--<div class="btn-group" role="group">--%>
            <%--<button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display:table-cell; font-size:18px;"><div id="greenDiode"></div> &emsp;aktywne: &nbsp;<%= sumActive %>&emsp;</div></button>--%>
        <%--</div>--%>
        <%--<div class="btn-group" role="group">--%>
            <%--<button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display:table-cell; font-size:18px;"><div id="redDiode"></div> &emsp;nieaktywne: &nbsp;<%= sumInactive %>&emsp;</div></button>--%>
        <%--</div>--%>
        <%--<div class="btn-group" role="group">--%>
            <%--<button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display: table-cell; font-size:18px;"><div id="greyDiode"></div> &emsp;wyłączone: &nbsp;<%= sumDisabled %>&emsp;</div></button>--%>
        <%--</div>--%>
    <%--</div>--%>
    <div class="panel panel-default" style="border-radius: 10px;">
        <div class="panel-heading">
            <h3 class="panel-title">Statystyki:</h3>
        </div>
        <div class="panel-body">
            <div class="btn-group btn-group-justified" role="group" aria-label="...">
                <div class="btn-group" role="group">
                    <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display:table-cell; font-size:18px;"><div id="greenDiode"></div> &emsp;aktywne: &nbsp;<%= sumActive %>&emsp;</div></button>
                </div>
                <div class="btn-group" role="group">
                    <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display:table-cell; font-size:18px;"><div id="redDiode"></div> &emsp;nieaktywne: &nbsp;<%= sumInactive %>&emsp;</div></button>
                </div>
                <div class="btn-group" role="group">
                    <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display: table-cell; font-size:18px;"><div id="greyDiode"></div> &emsp;wyłączone: &nbsp;<%= sumDisabled %>&emsp;</div></button>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script>
    $(function() {
        $('[data-toggle="tooltip"]').tooltip();
    })
</script>
<%--<script>--%>
    <%--$(document).ready(function(){--%>
        <%--var NavY = $('.nav').offset().top;--%>

        <%--var stickyNav=function () {--%>
            <%--var ScrollY=$(window).scrollTop();--%>

            <%--if(ScrollY>=NavY){--%>
                <%--$('.nav').addClass('sticky');--%>
            <%--}else{--%>
                <%--$('.nav').removeClass('sticky');--%>
            <%--}--%>
        <%--};--%>
        <%--stickyNav();--%>

        <%--$(window).scroll(function(){--%>
            <%--stickyNav()--%>
    <%--});--%>
    <%--});--%>
<%--</script>--%>
</body>
</html>