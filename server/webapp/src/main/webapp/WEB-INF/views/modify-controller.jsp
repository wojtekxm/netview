<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Modyfikuj kontroler</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/progress.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" style="background-color: #080b08;">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myDiv">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>

        <div class="collapse navbar-collapse" id="myDiv">
            <ul class="nav navbar-nav" style="padding-right:3px;font-size: 16px;">
                <ul class="nav navbar-nav" style="padding-right:3px;font-size: 16px;">
                    <li><a style="background-color: black;padding-left:25px;padding-right: 20px;" href="/"><span class="glyphicon glyphicon-home"></span> &nbsp;NetView &nbsp;</a></li>
                    <c:if test="${loggedUser.role eq 'ROOT'}">  <li><a href="/all-controllers">Kontrolery</a></li>
                    <li><a href="/all-users">Użytkownicy</a></li>
                    <li><a href="/all-devices">Urządzenia</a></li>
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">Lokalizacje<span class="caret"></span></a>
                        <ul class="dropdown-menu"  style="background-color: #080b08;">
                            <li><a href="/all-buildings">Budynki</a></li>
                            <li><a href="/all-units">Jednostki</a></li></c:if>
                        </ul>
                </ul>
                </li>
            </ul>
            <c:if test="${loggedUser.role eq 'ROOT'}">  <form method="get" action="/search" class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" name="query" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 150px!important;">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
                </div>
            </form></c:if>
            <ul class="nav navbar-nav navbar-right" style="padding-right:3px;font-size: 16px;">
                <c:if test="${loggedUser.role eq 'ROOT'}">  <li><a href="/settings"><span class="glyphicon glyphicon-wrench"></span>  Ustawienia</a></li></c:if>
                <li><a href="/account"><span class="glyphicon glyphicon-user"></span>  <c:out value="${loggedUser.name}"/></a></li>
                <li><a href="/logout" style="margin-right: 10px;"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <div style="height: 80px;"></div>
    <div class="panel panel-default" id="header" style="margin-bottom: 15px!important;">
        <div class="panel-body">
            <div id="tittle"><span class="glyphicon glyphicon-inbox"></span> <c:out value="${controller.name}"/>: </div>
            <%--<form method="post" action="/api/controller/accept-modify-controller" id="form1"></form>--%>
        </div>
    </div>

    <div class="panel panel-default" id="content">

        <div class="panel-heading" style="background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0; margin-bottom: inherit;">
            Modyfikuj kontroler:
        </div>

        <table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0;margin-bottom: inherit;">
            <tr>
                <input id="id" type="hidden" name="id" value="${controller.id}" />
            </tr>

            <tr>
                <td ><font color="red" ><b>*</b></font> Nazwa:</td>
                <td><input  id="new_name" type="text" name="name" value="${controller.name}" style="width: 30%;" />
            </tr>

            <tr>
                <td ><font color="red" ><b>*</b></font> IPv4:</td>
                <td><input type="text" id="new_ipv4" name="ipv4" value="${controller.ipv4}" style="width: 30%;" />
            </tr>

            <tr>
                <td>Opis:</td>
                <td><input  type="text" id="new_description" name="description" value="${controller.description}" style="width: 30%;" />
            </tr>
            <tr>
                <td>Community String:</td>
                <td><input  type="text" id="new_communityString" name="communityString" value="${controller.communityString}" style="width: 30%;" />
            </tr>
            <tr>
                <td></td>
                <td>
                    <label>
                        <input id="new_fake" name="fake" type="checkbox"
                               <c:if test="${ controller.fake == false}">checked="checked"</c:if> >
                        Prawdziwy
                    </label>
                </td>
            </tr>
            <tr>
                <td>Budynek:</td>
                <td>
            <%--<label for="new_building">Wybierz budynek:</label>--%>

                    <select class="form-control" id="new_building"name="buildingId" data-width="auto" >

                        <option value="0"> </option>

                         <c:forEach items="${list}" var="building" >

                             <option <c:if test="${ controller.buildingId == building.id}">
                             selected = "selected"   </c:if>   value="${building.id}"  >
                                <c:out value="${building.name}"/>
                            </option>

                        </c:forEach>
                    </select>
                </td>
            </tr>

                </table>

        <div style="margin-bottom: 60px;">

            <a href="/controller/${controller.id}" class="btn btn-info" role="button" style="float:left;width:180px;font-size:17px;margin-right: 10px;" ><span class="glyphicon glyphicon-backward"></span> Powrót</a>
            <span style="display: flex;position: relative;float: left;">
                        <span class="glyphicon glyphicon-ok" style="position: absolute;font-size:17px;color: white;top: 30%; left:15%;"></span>
                        <input type="submit" value="Zatwierdź" class="btn btn-success" id="btn_submit" role="button" style="float:left;width:180px;font-size:17px;" >
                        <div class="pull-left progress-space">
                    <div id="change_loading" class="later"></div>
                </div>
                    </span>

        </div>

    </div>
    <div class="form-group">
        <div class="col-sm-12">
            <div id="result_success"></div>
            <div id="result_error"></div>
        </div>
    </div>
</div>

<%--<div id="all" class="container-fluid">--%>
    <%--<div id="container">--%>
        <%--<div class="content">--%>
            <%--<div style="height: 10px;"></div>--%>
            <%--<div>--%>
                <%--<div id="wydzial"><div style="width: 100%;border-bottom: 1px solid #e0e0e0;padding-bottom: 3px;"><span class="glyphicon glyphicon-arrow-right"></span> <c:out value="${controller.name}"/>: </div></div>--%>
                <%--<form method="post" action="/api/controller/accept-modify-controller" id="form1"></form>--%>
            <%--</div>--%>

            <%--<div id="devices" class="panel panel-default" style="padding: 15px;">--%>
                <%--<div class="panel-heading" style="background-color: #fcfcfc; padding: 15px;font-size: 16px;border: 1px solid #e0e0e0; margin-bottom: inherit;">--%>
                    <%--Modyfikuj kontroler:--%>
                <%--</div>--%>

                <%--<table class="table table-responsive" style="background-color: white!important;border: 1px solid #e0e0e0;margin-bottom: inherit;">--%>
                    <%--<tr>--%>
                        <%--<input form="form1" id="id" type="hidden" name="id" value="${controller.id}" />--%>
                    <%--</tr>--%>

                    <%--<tr>--%>
                        <%--<td style="color:red">*Nazwa</td>--%>
                        <%--<td><input form="form1" id="new_name" type="text" name="name" value="${controller.name}" style="width: 30%;" />--%>
                    <%--</tr>--%>

                    <%--<tr>--%>
                        <%--<td style="color:red">*IPv4</td>--%>
                        <%--<td><input form="form1" type="text" id="new_ipv4" name="ipv4" value="${controller.ipv4}" style="width: 30%;" />--%>
                    <%--</tr>--%>

                    <%--<tr>--%>
                        <%--<td>Opis</td>--%>
                        <%--<td><input form="form1" type="text" id="new_description" name="description" value="${controller.description}" style="width: 30%;" />--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                        <%--<td>Community String</td>--%>
                        <%--<td><input form="form1" type="text" id="new_communityString" name="communityString" value="${controller.communityString}" style="width: 30%;" />--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                        <%--<td>--%>
                            <%--<label>--%>
                                <%--<input id="new_fake" name="fake" form="form1" type="checkbox"--%>
                                       <%--<c:if test="${ controller.fake == false}">checked="checked"</c:if> >--%>
                                <%--Prawdziwy--%>
                            <%--</label>--%>
                        <%--</td>--%>
                    <%--</tr>--%>

                    <%--<label for="new_building">Wybierz budynek:</label>--%>

                    <%--<select form="form1" class="form-control" id="new_building"name="buildingId">--%>
                        <%--<option></option>--%>
                        <%--<c:forEach items="${list}" var="building" >--%>
                            <%--<option value="${building.id}" >--%>
                                <%--<c:out value="${building.name}"/>--%>
                            <%--</option>--%>
                        <%--</c:forEach>--%>
                    <%--</select>--%>
                <%--</table>--%>
                <%--<div>--%>

                    <%--<a href="/controller/${controller.id}" class="btn btn-info" role="button" style="float:left;width:180px;font-size:17px;margin-right: 10px;" ><span class="glyphicon glyphicon-backward"></span> Powrót</a>--%>
                    <%--<span style="display: flex;position: relative;float: left;">--%>
                        <%--<span class="glyphicon glyphicon-ok" style="position: absolute;font-size:17px;color: white;top: 30%;left:15%;"></span>--%>
                        <%--<input form="form1" type="submit" value="Zatwierdź" class="btn btn-success" id="btn_submit" role="button" style="float:left;width:180px;font-size:17px;" >--%>
                        <%--<div class="pull-left progress-space">--%>
                    <%--<div id="change_loading" class="later"></div>--%>
                <%--</div>--%>
                    <%--</span>--%>

                <%--</div>--%>
            <%--</div>--%>
            <%--<div class="form-group">--%>
                <%--<div class="col-sm-12">--%>
                    <%--<div id="result_success"></div>--%>
                    <%--<div id="result_error"></div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/notify.js"></script>
<script>
"use strict";
$(document).ready(function () {
    var btnSubmit = $('#btn_submit');
    btnSubmit.click(function () {
        btnSubmit.prop('disabled', true);
        var controllerDto = {
            "id": $('#id').val(),
            "name": $('#new_name').val(),
            "ipv4": $('#new_ipv4').val(),
            "description": $('#new_description').val(),
            "communityString": $('#new_communityString').val(),
            "fake" : !( $('#new_fake')[0].checked ),
            "buildingId": $('#new_building').val()

        };
        progress.load(
            [{
                "url" : '/api/controller/accept-modify-controller',
                "method" : 'post',
                "postData" : controllerDto
            }],
            ['#change_loading'], [], [],
            function() {
                btnSubmit.prop('disabled', false);
                notify.success('#result_success', 'Dane zostały zmienione.');
            },
            function() {
                btnSubmit.prop('disabled', false);
                notify.danger('#result_error', 'Nie udało się zmienić danych.');
            }
        );
    });
});
</script>
</body>
</html>