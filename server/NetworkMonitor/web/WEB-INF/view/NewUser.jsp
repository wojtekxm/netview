<%@ page import="zesp03.data.row.UserRow" %>
<%@ page import="zesp03.filter.AuthenticationFilter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    UserRow loggedUser = (UserRow) request.getAttribute(AuthenticationFilter.ATTR_USERROW);
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title></title>
    <link rel="icon" href="/favicon.png">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
</head>
<body>
<div class="container" id="page">
    <p>zalogowany: <%= loggedUser.getName() %>
    </p>
    <a href="/">strona główna</a><br>
    <a href="/all-users">zobacz wszystkich użytkowników</a><br>
    <hr>
    <h4>Tworzenie nowego użytkownika</h4>
    <div id="settings" class="form-group">
        <label>
            <input type="radio" name="is_admin" value="false" checked>
            zwykły użytkownik
        </label>
        <br>
        <label>
            <input type="radio" name="is_admin" value="true">
            administrator
        </label>
        <br>
        <button type="button" id="button_create">Stwórz</button>
    </div>
    <div id="result"></div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script>
    $(document).ready(function () {
        $('#button_create').click(function () {
            var isAdmin = $('#page').find('input[name="is_admin"]:checked').val() === 'true';
            $('#result')
                .empty()
                .append(
                    $('<span></span>').text('Czekaj...')
                );
            $.ajax({
                type: 'post',
                url: '/api/create-new-user',
                dataType: 'json',
                data: {
                    "is-admin": isAdmin
                },
                success: function (data) {
                    if (data.success) {
                        $('#result')
                            .empty()
                            .append(
                                $('<p></p>').text('Stworzono nowego użytkownika o id ' + data.userId + '. By aktywować konto użyj poniższego linku:'),
                                $('<a></a>')
                                    .attr('href', data.activationURL)
                                    .text(data.activationURL)
                            );
                        $('#settings').hide(400);
                    }
                    else {
                        $('#result')
                            .empty()
                            .append(
                                $('<h1></h1>').text('ERROR')
                            );
                    }
                },
                error: function () {
                    $('#result')
                        .empty()
                        .append(
                            $('<h1></h1>').text('ERROR')
                        );
                }
            });
        });
    });
</script>
</body>
</html>