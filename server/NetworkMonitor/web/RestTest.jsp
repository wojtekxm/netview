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
    <div>
        <div class="form-group">
            <div class="radio">
                <label>
                    <input type="radio" id="radio_all" name="selection" value="all">
                    wszystkie
                </label>
            </div>
        </div>
        <div class="form-group">
            <div class="radio">
                <label>
                    <input type="radio" id="radio_id_only" name="selection" value="id_only" checked>
                    tylko z wybranym id
                </label>
            </div>
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
    $("#button_device").click(function () {
        $("#result > *").remove();
        $("#result").append(
            $("<div></div>").addClass('panel panel-info').append(
                $('<div></div>').addClass('panel-heading').text('szukanie urządzenia ...')
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
                    $("<div></div>").addClass('panel panel-success').append(
                        $('<div></div>').addClass('panel-heading').text('Urządzenie ' + data.name),
                        $('<table></table>').addClass('table table-condensed table-hover').append(
                            $('<tbody></tbody>').append(
                                $('<tr></tr>').append(
                                    $('<td></td>').text('id'),
                                    $('<td></td>').text(data.id)
                                ),
                                $('<tr></tr>').append(
                                    $('<td></td>').text('nazwa'),
                                    $('<td></td>').text(data.name)
                                ),
                                $('<tr></tr>').append(
                                    $('<td></td>').text('opis'),
                                    $('<td></td>').text(data.description)
                                ),
                                $('<tr></tr>').append(
                                    $('<td></td>').text('znany'),
                                    $('<td></td>').text(data.known ? 'tak' : 'nie')
                                ),
                                $('<tr></tr>').append(
                                    $('<td></td>').text('id kontrolera'),
                                    $('<td></td>').text(data.controllerId)
                                ),
                                $('<tr></tr>').append(
                                    $('<td></td>').text('uruchomione'),
                                    $('<td></td>').text(data.enabled ? 'tak' : 'nie')
                                ),
                                $('<tr></tr>').append(
                                    $('<td></td>').text('aktualna liczba klientów'),
                                    $('<td></td>').text(data.clientsSum)
                                )
                            )
                        )
                    )
                );
            },
            error: function () {
                $("#result > *").remove();
                $('#result').append(
                    $('<div></div>').addClass('panel panel-danger').append(
                        $('<div></div>').addClass('panel-heading').text('nie znaleziono takiego urządzenia')
                    )
                );
            }
        });
    });
    $("#button_controller").click(function () {
        $("#result > *").remove();
        $("#result").append(
            $("<div></div>").addClass('panel panel-info').append(
                $('<div></div>').addClass('panel-heading').text('szukanie kontrolera ...')
            )
        );
        var argId = $('#textfield').val();
        $.ajax({
            type: 'GET',
            url: '/api/controller',
            dataType: 'json',
            data: {
                id: argId
            },
            success: function (data) {
                $("#result > *").remove();
                $("#result").append(
                    $("<div></div>").addClass('panel panel-success').append(
                        $('<div></div>').addClass('panel-heading').text('Kontroler ' + data.name),
                        $('<table></table>').addClass('table table-condensed table-hover').append(
                            $('<tbody></tbody>').append(
                                $('<tr></tr>').append(
                                    $('<td></td>').text('id'),
                                    $('<td></td>').text(data.id)
                                ),
                                $('<tr></tr>').append(
                                    $('<td></td>').text('nazwa'),
                                    $('<td></td>').text(data.name)
                                ),
                                $('<tr></tr>').append(
                                    $('<td></td>').text('adres IP'),
                                    $('<td></td>').text(data.ipv4)
                                ),
                                $('<tr></tr>').append(
                                    $('<td></td>').text('opis'),
                                    $('<td></td>').text(data.description)
                                )
                            )
                        )
                    )
                );
            },
            error: function () {
                $("#result > *").remove();
                $('#result').append(
                    $('<div></div>').addClass('panel panel-danger').append(
                        $('<div></div>').addClass('panel-heading').text('nie znaleziono takiego kontrolera')
                    )
                );
            }
        });
    });
</script>
</body>
</html>