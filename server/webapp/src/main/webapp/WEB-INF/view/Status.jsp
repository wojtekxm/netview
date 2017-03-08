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
    int surveyTime=list.get(0).getLastSurveyTimestamp();
    Long longSurveyTime = new Long(surveyTime);
    longSurveyTime=longSurveyTime*1000;
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge; chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Status sieci</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="//code.angularjs.org/1.2.20/angular.js"></script>
    <link rel="stylesheet" href="/css/style.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
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
    <div class="container-fluid">
        <ul class="nav nav-pills" style="padding-top: 3px;font-size: 17px;position: absolute;width: 100%;left: 0;text-align: center;margin:0 auto;">
            <li role="presentation"><a href="/make-survey">Nowe badanie</a></li>
            <li role="presentation"><a href="/status-small">Mały widok</a></li>
            <li role="presentation"><a href="/all-controllers">Kontrolery</a></li>
            <li role="presentation"><a href="/all-users">Użytkownicy</a></li>
            <li role="presentation"><a href="/api/all-devices">Urządzenia</a></li>
            <form class="navbar-form nav-pills" style="padding-top: 2px;margin-top:2px;">
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="Szukaj...">
                </div>
                <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
            </form>
        </ul>
        <ul class="nav nav-pills pull-left" style="padding-top: 3px;border-radius: 10px;padding-left:7px;font-size: 17px;">
            <li role="presentation" class="active"><a href="/"><span class="glyphicon glyphicon-home"></span>  Strona główna</a></li>
        </ul>
        <ul class="nav nav-pills pull-right" style="padding-top: 3px;padding-right:3px;font-size: 17px;">
            <li role="presentation"><a href="#"><span class="glyphicon glyphicon-user"></span>  Mój profil</a></li>
            <li role="presentation"><a href="/logout"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
        </ul>
    </div>
</nav>
<div id="all" class="container-fluid">
    <div id="container">
        <div class="welcome">
            <div class="tittle"><img src="/images/icon.ico" style="padding-bottom: 5px;"> &nbsp; NETWORK-MONITOR</div>
            <div class="user">zalogowany: <%= userRow.getName() %>
            </div>
            <div class="logo"><img src="/images/logooWhite.jpg"></div>
        </div>
        <div id="content">
            <ul class="view" style="z-index: 1000;top:0;">
                <li>
                    <div id="wydzial"><div style="border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;"><span class="glyphicon glyphicon-th"></span> Wszystkie kontrolery</div></div>
                    <ul id="devices" style="padding: 4px;border: 1px solid #e0e0e0;">
                        <%
                            int sumActive = 0;
                            int sumInactive = 0;
                            int sumDisabled = 0;
                            for (final DeviceData info : list) {
                                int sumUsers = info.getClientsSum();
                                String clazz;
                                if (info.isEnabled()) {
                                    if (info.getClientsSum() > 0 && info.getClientsSum() <= 10) {
                                        clazz = "greenDiode1";
                                        sumActive++;
                                    } else if (info.getClientsSum() > 10 && info.getClientsSum() <= 30) {
                                        clazz = "greenDiode2";
                                        sumActive++;
                                    } else if (info.getClientsSum() > 30 && info.getClientsSum() <= 47) {
                                        clazz = "greenDiode3";
                                        sumActive++;
                                    } else if (info.getClientsSum() > 47) {
                                        clazz = "greenDiode4";
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

                                final String h = "/device?" + DeviceServlet.GET_ID + "=" + info.getId();
                                String t = info.getName();
                                if(info.isEnabled()){
                                    if (info.getDescription() != null) t += "<br>opis: " + info.getDescription();
                                        %><li class="<%= clazz %>" title="<%= t %>" data-toggle="tooltip" data-html="true"><a href="<%= h %>" style="text-decoration: none; color: white;"><%= sumUsers %></a></li
                                        ><%
                                    }else{
                                        if (info.getDescription() != null) t += "<br>opis: " + info.getDescription();
                                        %><li class="<%= clazz %>" title="<%= t %>" data-toggle="tooltip" data-html="true"><a href="<%= h %>" style="text-decoration: none; color: white;">-</a></li
                                        ><%
                                    }
                        }
                        %>
                    </ul>
                </li>
            </ul>
        </div>

        <div class="panel panel-default" style="border-radius: 10px;">
            <div class="panel-heading">
                <div id="data"></div><h3 class="panel-title" style="font-size: 17px;color:black; padding-top: 12px;"><span class="glyphicon glyphicon-th-large"></span> Pokaż tylko urządzenia ( Kliknij w wybrany stan ) :</h3>
            </div>
            <div class="panel-body">
                <div class="btn-group btn-group-justified" role="group" aria-label="...">
                    <div class="btn-group" role="group" onclick="onlyGreen()">
                        <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display:table-cell; font-size:18px;"><div id="greenDiode"></div> &emsp;aktywne: &nbsp;<%= sumActive %>&emsp;</div></button>
                    </div>
                    <div class="btn-group" role="group" onclick="onlyRed()">
                        <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display:table-cell; font-size:18px;"><div id="redDiode"></div> &emsp;nieaktywne: &nbsp;<%= sumInactive %>&emsp;</div></button>
                    </div>
                    <div class="btn-group" role="group" onclick="onlyGrey()">
                        <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display: table-cell; font-size:18px;"><div id="greyDiode"></div> &emsp;wyłączone: &nbsp;<%= sumDisabled %>&emsp;</div></button>
                    </div>
                    <div class="btn-group" role="group" onclick="allColors()">
                        <button type="button" class="btn btn-default" style="border-radius: 10px;"><div style="display: table-cell; font-size:18px;"><div></div><span class="glyphicon glyphicon-equalizer"></span>&emsp;Wszystkie: &nbsp;<%= sumDisabled + sumActive + sumInactive %>&emsp;</div></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>



<%--<script>--%>
    <%--function auto_load(){--%>
        <%--$.ajax({--%>
            <%--url: "/api/device",--%>
            <%--cache: false,--%>
            <%--success: function(data){--%>
                <%--$("#devices").html(data);--%>
            <%--}--%>
        <%--});--%>
    <%--}--%>

    <%--$(document).ready(function(){--%>

        <%--auto_load(); //Call auto_load() function when DOM is Ready--%>

    <%--});--%>

    <%--//Refresh auto_load() function after 10000 milliseconds--%>
    <%--setInterval(auto_load,10000);--%>
<%--</script>--%>

<script>
    function onlyGreen(){
        $( ".view > li > ul" ).empty();
        <%
        for (DeviceData d : list) {
            int sumUsers=d.getClientsSum();
            String clazz;
            if(d.isEnabled()){
                if(d.getClientsSum() > 0){
                    clazz="greenDiode2";
                    if( sumUsers > 0 && sumUsers <= 10 ){
                       clazz="greenDiode1";
                       sumActive++;
                    }else if( sumUsers > 10 && sumUsers <= 30 ){
                       clazz="greenDiode2";
                       sumActive++;
                    }
                    else if( sumUsers > 30 && sumUsers <= 47 ){
                       clazz="greenDiode3";
                       sumActive++;
                    }
                    else if( sumUsers > 47){
                       clazz="greenDiode4";
                       sumActive++;
                    }
        %>
        $( ".view > li > ul" ).append( "<li class='<%= clazz %>' title='<%= d.getName() %>' data-toggle='tooltip' data-html='true'><a href='/device?=<%= d.getId() %>' style='text-decoration: none; color: white;'><%= d.getClientsSum() %></a></li>").val();
        $(function () {
            $('[data-toggle="tooltip"]').tooltip();
        })
        <%
                }
            }
        }
        %>
    }
</script>

<script>
    function onlyRed(){
        $( ".view > li > ul" ).empty();
        <%
        for (DeviceData d : list) {
            String clazz;
            if(d.isEnabled()){
                if(d.getClientsSum() == 0){
                    clazz="redDiode";
        %>
        $( ".view > li > ul" ).append( "<li class='<%= clazz %>' title='<%= d.getName() %>' data-toggle='tooltip' data-html='true'><a href='/device?=<%= d.getId() %>' style='text-decoration: none; color: white;'><%= d.getClientsSum() %></a></li>").val();
        $(function () {
            $('[data-toggle="tooltip"]').tooltip();
        })
        <%
                }
            }
        }
        %>
    }
</script>

<script>
    function onlyGrey(){
        $( ".view > li > ul" ).empty();
        <%
        for (DeviceData d : list) {
            String clazz;
            if(!d.isEnabled()){
                clazz="greyDiode";
        %>
        $( ".view > li > ul" ).append( "<li class='<%= clazz %>' title='<%= d.getName() %>' data-toggle='tooltip' data-html='true'><a href='/device?=<%= d.getId() %>' style='text-decoration: none; color: white;'>-</a></li>").val();
        $(function () {
            $('[data-toggle="tooltip"]').tooltip();
        })
        <%
            }
        }
        %>
    }
</script>

<script>
    function allColors(){
        $( ".view > li > ul" ).empty();
        <%
        sumInactive=0;
        sumActive=0;
        sumDisabled=0;
        for (DeviceData d : list) {
            int sumUsers=d.getClientsSum();
            String clazz;
            if(d.isEnabled()){
                if(d.getClientsSum() > 0){
                    clazz="greenDiode2";
                    if( sumUsers > 0 && sumUsers <= 10 ){
                       clazz="greenDiode1";
                       sumActive++;
                    }else if( sumUsers > 10 && sumUsers <= 30 ){
                       clazz="greenDiode2";
                       sumActive++;
                    }
                    else if( sumUsers > 30 && sumUsers <= 47 ){
                       clazz="greenDiode3";
                       sumActive++;
                    }
                    else if( sumUsers > 47){
                       clazz="greenDiode4";
                       sumActive++;
                    }
                }else{
                    clazz="redDiode";
                    sumInactive++;
                }
            }else{
                clazz="greyDiode";
                sumDisabled++;
            }
            if(d.isEnabled()){
        %>
        $( ".view > li > ul" ).append( "<li class='<%= clazz %>' title='<%= d.getName() %>' data-toggle='tooltip' data-html='true'><a href='/device?=<%= d.getId() %>' style='text-decoration: none; color: white;'><%= d.getClientsSum() %></a></li>").val();
        $(function () {
            $('[data-toggle="tooltip"]').tooltip();
        })
        <%
            }else{
            %>
        $( ".view > li > ul" ).append( "<li class='<%= clazz %>' title='<%= d.getName() %>' data-toggle='tooltip' data-html='true'><a href='/device?=<%= d.getId() %>' style='text-decoration: none; color: white;'>-</a></li>").val();
        $(function () {
            $('[data-toggle="tooltip"]').tooltip();
        })
        <%
            }
        }
        %>
    }
</script>

<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    })
</script>

<script>
    window.onload = function() {
        var date = new Date(<%= longSurveyTime %>);
        var n = date.toLocaleString();
        $( "#data" ).append("Ostatnie badanie sieci przeprowadzono: &emsp; " + n).val();
    }
</script>

</body>
</html>