<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dodawanie kontrolerów</title>
</head>
<body>
<p><font size="30">Dodawanie kontrolerów</font></p>
<form method="post" action="check">
    <p><b>Nazwa kontrolera:</b></p>
    <input type="text" name="name">
    <p><b>IPv4 kontrolera:</b></p>
    <input type="text" name="ipv4">
    <p><b>Komentarz(opcjonalne):</b></p>
    <input type="text" name="description">
    <p></p>
    <input type="submit" value="Dodaj">
    <a href="javascript:history.back();"><input type="button" value="Wstecz"></A>
</form><br>

</body>
</html>