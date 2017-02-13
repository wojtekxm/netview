<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>RestTest</title>
    <link rel="icon" href="/favicon.png">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/status-small.css">
</head>
<body>
<div class="container">
    <hr>
    <div class="form-inline">
        <div class="form-group">
            <label for="textfield">id</label>
            <input type="text" class="form-control" id="textfield">
        </div>
        <button class="btn btn-info" type="submit" id="button_controller">Kontroler</button>
        <button class="btn btn-info" type="submit" id="button_device">Urządzenie</button>
    </div>
    <hr>
    <div id="result"></div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script>
    $("#button_controller").click(function () {
        $("#result > *").remove();
        $("#result").append(
            $("<div></div>").addClass('panel panel-default').append(
                $('<div></div>').addClass('panel-body').text('pobieranie...')
            )
        );
        var argId = $('#textfield').val();
        $.ajax({
            type: 'GET',
            url: '/api/device',
            dataType: 'json',
            data: {
                id: argId
            },
            success: function (data) {
                $("#result > *").remove();
                $("#result").append(
                    $("<div></div>").addClass('panel panel-default').append(
                        $('<div></div>').addClass('panel-heading').text('Urządzenie ' + data.name),
                        $('<div></div>').addClass('panel-body').append(
                            $('<ul></ul>').addClass('list-group').append(
                                $("<li></li>").addClass('list-group-item').text('id = ' + data.id),
                                $("<li></li>").addClass('list-group-item').text('nazwa = ' + data.name),
                                $("<li></li>").addClass('list-group-item').text('opis = ' + data.description),
                                $('<li></li>').addClass('list-group-item').text('znany = ' + data.known),
                                $("<li></li>").addClass('list-group-item').text('id kontrolera = ' + data.controllerId),
                                $("<li></li>").addClass('list-group-item').text('uruchomione = ' + data.enabled),
                                $("<li></li>").addClass('list-group-item').text('aktualna liczba klientów = ' + data.clientsSum)
                            )
                        )
                    )
                );
            },
            error: function () {
                $("#result > *").remove();
                $('#result').append(
                    $('<div></div>').addClass('panel panel-danger').append(
                        $('<div></div>').addClass('panel-heading').text('error'),
                        $('<div></div>').addClass('panel-body').text('nie ma takiego urządzenia')
                    )
                );
            }
        });
    });
</script>
</body>
</html>