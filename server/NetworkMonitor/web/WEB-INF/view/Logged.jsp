<%@ page import="java.util.ArrayList" %>
<%@ page import="zesp03.data.CheckInfo" %>
<%@ page import="zesp03.servlet.Details" %>
<%@ page import="zesp03.servlet.DeviceInfo" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    ArrayList<CheckInfo> allDevices = (ArrayList<CheckInfo>)request.getAttribute(DeviceInfo.allDevicesString);
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge; chrome=1"/>
    <title>Network Monitor</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/loggedStyle.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>

</head>

<body>
<div style="height:60px;">
    <div class="nav">
        <ol>
            <li><a href="index.jsp" class="aNav">Strona główna</a></li>
            <li><a href="make-survey" class="aNav">Nowe badanie</a></li>
            <li><a href="status-small" class="aNav">Mały widok</a></li>
        </ol>
    </div>
</div>
<div id="all">
<div id="container">
    <div class="welcome">
        <div class="tittle">NETWORK-MONITOR</div>
        <div class="logo"><img src="/images/logoo.jpg"></div>
    </div>
        <!--   	<div id="menu"></div> -->
    <div id="content">
        <ul style="z-index: 1000;top:0;">
            <li> <div id="wydzial">Wszystkie kontrolery</div>
                <ul style="padding: 5px;">
                    <%
                    int sumActive = 0;
                    int sumInactive = 0;
                    int sumDisabled = 0;
                    for(final CheckInfo info : allDevices) {
                        int sumUsers=info.survey().getClientsSum();
                        String c;
                        if( info.survey().isEnabled() ) {
                            if( info.survey().getClientsSum() > 0 ) {
                                c = "greenDiode";
                                sumActive++;
                            }
                            else {
                                c = "redDiode";
                                sumInactive++;
                            }
                        }
                        else {
                            c = "greyDiode";
                            sumDisabled++;
                        }

                        final String h = "details?" + Details.PARAM_ID + "=" + info.device().getId();
                        String t = info.device().getName();
                        if( info.device().getDescription() != null )t += "<br>opis: " + info.device().getDescription();
                        t += "<br>z: " + info.controller().getName();
                    %><li class="<%= c %>" title="<%= t %>" data-toggle="tooltip" data-html="true"><a href="<%= h %>" style="text-decoration: none; color: white;"><%= sumUsers %></a></li
                    ><% } %>

                </ul>
            </li>
            <div class="summary" style="font-size:20px;width:100%;padding: 8px;display: table;margin-right: auto;margin-left: auto;background-color: black;">
                <div style="display:table-cell;"><div id="greenDiode"></div> &emsp;aktywne: &nbsp;<%= sumActive %>&emsp;</div>
                <div style="display:table-cell;"><div id="redDiode"></div> &emsp;nieaktywne: &nbsp;<%= sumInactive %>&emsp;</div>
                <div style="display: table-cell;"><div id="greyDiode"></div> &emsp;wyłączone: &nbsp;<%= sumDisabled %>&emsp;</div>
            </div>
        </ul>
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