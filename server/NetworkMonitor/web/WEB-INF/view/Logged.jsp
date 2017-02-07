<%@page import="zesp03.data.DeviceStatus"
%>
<%@ page import="zesp03.data.UserData"
%>
<%@ page import="zesp03.filter.AuthenticationFilter"
%>
<%@ page import="zesp03.servlet.Details"
%><%@ page import="zesp03.servlet.DeviceInfo"
%><%@ page import="java.util.List"
%><%@ page language="java" contentType="text/html; charset=UTF-8"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%><%
    List<DeviceStatus> list = (List<DeviceStatus>) request.getAttribute(DeviceInfo.allDevicesString);
    UserData userData = (UserData) request.getAttribute(AuthenticationFilter.ATTR_USERDATA);
    String style = (String) session.getAttribute("style");
    String logo = (String) session.getAttribute("logo");
%><!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge; chrome=1"/>
    <title>Network Monitor</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
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
<div style="height:60px;">
    <div class="nav">
        <ol>
            <li><a href="/index.jsp" class="aNav">Strona główna</a></li>
            <li><a href="/make-survey" class="aNav">Nowe badanie</a></li>
            <li><a href="/status-small" class="aNav">Mały widok</a></li>
            <li><a href="style" class="aNav">Zmień styl</a></li>
            <li><a href="/ShowControllers" class="aNav">Kontrolery</a></li>
            <li><a href="/logout" class="aNav">Wyloguj</a></li>
        </ol>
    </div>
</div>
<div id="all" class="container-fluid">
<div id="container">
    <div class="welcome">
        <div class="tittle">NETWORK-MONITOR</div>
        <div class="user">zalogowany: <%= userData.getName() %>
        </div>
        <div class="logo"><img src="/images/<%= logo %>.jpg"></div>
    </div>
    <div id="content">
        <ul style="z-index: 1000;top:0;">
            <li> <div id="wydzial">Wszystkie kontrolery</div>
                <ul style="padding: 5px;">
                    <%
                    int sumActive = 0;
                    int sumInactive = 0;
                    int sumDisabled = 0;
                        for (final DeviceStatus info : list) {
                        int sumUsers = info.getSurvey().getClientsSum();
                        String clazz;
                        if( info.getSurvey().isEnabled() ) {
                            if( info.getSurvey().getClientsSum() > 0 ) {
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

                        final String h = "/details?" + Details.PARAM_ID + "=" + info.getDevice().getId();
                        String t = info.getDevice().getName();
                        if( info.getDevice().getDescription() != null )t += "<br>opis: " + info.getDevice().getDescription();
                        t += "<br>z: " + info.getController().getName();
                    %><li class="<%= clazz %>" title="<%= t %>" data-toggle="tooltip" data-html="true"><a href="<%= h %>" style="text-decoration: none; color: white;"><%= sumUsers %></a></li
                    ><% } %>

                </ul>
            </li>
        </ul>
    </div>
    <div class="summary">
        <div style="display:table-cell;"><div id="greenDiode"></div> &emsp;aktywne: &nbsp;<%= sumActive %>&emsp;</div>
        <div style="display:table-cell;"><div id="redDiode"></div> &emsp;nieaktywne: &nbsp;<%= sumInactive %>&emsp;</div>
        <div style="display: table-cell;"><div id="greyDiode"></div> &emsp;wyłączone: &nbsp;<%= sumDisabled %>&emsp;</div>
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
<script>
    $(document).ready(function(){
        var NavY = $('.nav').offset().top;

        var stickyNav=function () {
            var ScrollY=$(window).scrollTop();

            if(ScrollY>=NavY){
                $('.nav').addClass('sticky');
            }else{
                $('.nav').removeClass('sticky');
            }
        };
        stickyNav();

        $(window).scroll(function(){
            stickyNav()
    });
    });
</script>
</body>
</html>