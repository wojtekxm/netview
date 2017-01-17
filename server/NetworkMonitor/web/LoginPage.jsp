<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge; chrome=1"/>
    <title>Network Monitor</title>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/loginStyle.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>

</head>
<body>
<div id="container">
        <form action="deviceinfo">
            <div id="tittle">NETWORK-MONITOR</div><br><br>
            <div id="loginFields">
            <table style="width:50px;">
                <tr>
                    <td>Login:&nbsp;</td>
                    <td><input type="text" name="uname" style="z-index: 100; font-weight: bold;color:black;"></td>
                </tr>
                <tr>
                    <td>Hasło:&nbsp;</td>
                    <td><input type="password" name="pass" style="z-index: 100;font-weight: bold; color:black;"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="Zaloguj" style="width:100%;color:black;font-weight: bold;z-index: 100;"></td>
                </tr>
            </table>
            </div>
            <% String err = (String) session.getAttribute("error" );
                if(err != null)
                {
            %><div id="error"><%= err %></div><%
            }
            %>
        </form>
    <%
        session.removeAttribute("error");
    %>
</div>

</body>
</html>
