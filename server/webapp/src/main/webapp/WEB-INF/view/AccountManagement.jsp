<%@ page import="zesp03.data.row.UserRow" %>
<%@ page import="zesp03.filter.AuthenticationFilter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    UserRow logged = (UserRow) request.getAttribute(AuthenticationFilter.ATTR_USERROW);
%>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>zarządzanie kontem</title>
    <link rel="icon" href="/favicon.png">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
</head>
<body>
<div class="container" id="page">
    <p>zalogowany: <%= logged.getName() %>
    </p>
    <a href="/">strona główna</a><br>
    <hr>
    <h4>Zmiana hasła</h4>
    <div id="form" class="form-group">
        stare hasło <input id="old_password" type="password"><br>
        nowe hasło <input id="new_password" type="password"><br>
        powtórz hasło <input id="repeat_password" type="password"><br>
        <button id="submit" type="button">Zmień</button>
    </div>
    <div id="result"></div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script>
    $(document).ready(function () {
        $('#submit').click(function () {
            $('#result > *').remove();
            $('#result').append(
                $('<div></div>').addClass('panel panel-info').append(
                    $('<div></div>').addClass('panel-heading').text('Czekaj...')
                )
            );
            $.ajax( {
                url: '/api/change-password',
                type: 'post',
                dataType: 'json',
                data: {
                    userId: <%= logged.getId() %>,
                    old: $('#old_password').val(),
                    desired: $('#new_password').val(),
                    repeat: $('#repeat_password').val()
                },
                success: function (response) {
                    $('#result > *').remove();
                    if(response.success) {
                        $('#result').append(
                            $('<div></div>').addClass('panel panel-success').append(
                                $('<div></div>').addClass('panel-heading').text('Hasło zostało zmienione.')
                            )
                        );
                    }
                    else {
                        var txt;
                        switch (response.reason) {
                            case 'INVALID_OLD_PASSWORD':
                                txt = 'Stare hasło się nie zgadza.';
                                break;
                            case 'PASSWORDS_DONT_MATCH':
                                txt = 'Hasła nie pasują do siebie.';
                                break;
                            default:
                                txt = 'Nie możesz wybrać takiego hasła.';
                        }
                        $('#result').append(
                            $('<div></div>').addClass('panel panel-danger').append(
                                $('<div></div>').addClass('panel-heading').text(txt)
                            )
                        );
                    }
                },
                error: function() {
                    $("#result > *").remove();
                    $('#result').append(
                        $('<div></div>').addClass('panel panel-danger').append(
                            $('<div></div>').addClass('panel-heading').text('Wystąpił problem. Spróbuj ponownie.')
                        )
                    );
                }
            } );
        });
    });
</script>
</body>
</html>