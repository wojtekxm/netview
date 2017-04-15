<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>RestTest</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/status-small.css">
</head>
<body>
<div class="container">
    <hr>
    <div id="settings">
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
            <input type="text" class="form-control" id="textfield" title="identyfikator">
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
        var selection = $('#settings').find('input[name=selection]:checked').val();
        if (selection === 'id_only') {
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
                            $('<table></table>').addClass('table table-bordered table-condensed table-hover').append(
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
                                        $('<td></td>').append(
                                            data.description === null ?
                                                $('<em></em>').text('(brak)') :
                                                $('<span></span>').text(data.description)
                                        )
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
                            $('<div></div>').addClass('panel-heading').text('wystąpił problem przy szukaniu urządzenia')
                        )
                    );
                },
                statusCode: {
                    404: function () {
                        $("#result > *").remove();
                        $('#result').append(
                            $('<div></div>').addClass('panel panel-danger').append(
                                $('<div></div>').addClass('panel-heading').text('urządzenie o id=' + argId + ' nie istnieje')
                            )
                        );
                    }
                }
            });
        }
        else if (selection === 'all') {
            $("#result").append(
                $("<div></div>").addClass('panel panel-info').append(
                    $('<div></div>').addClass('panel-heading').text('szukanie urządzeń ...')
                )
            );
            $.ajax({
                type: 'GET',
                url: '/api/all-devices',
                dataType: 'json',
                success: function (listDtoOfDeviceStateDto) {
                    if(!listDtoOfDeviceStateDto.success) {
                        err();
                        return;
                    }
                    $("#result > *").remove();
                    var i;
                    for (i = 0; i < listDtoOfDeviceStateDto.list.length; i++) {
                        var device = listDtoOfDeviceStateDto.list[i];
                        $("#result").append(
                            $("<div></div>").addClass('panel panel-success').append(
                                $('<div></div>').addClass('panel-heading').text('Urządzenie ' + device.name),
                                $('<table></table>').addClass('table table-bordered table-condensed table-hover').append(
                                    $('<tbody></tbody>').append(
                                        $('<tr></tr>').append(
                                            $('<td></td>').text('id'),
                                            $('<td></td>').text(device.id)
                                        ),
                                        $('<tr></tr>').append(
                                            $('<td></td>').text('nazwa'),
                                            $('<td></td>').text(device.name)
                                        ),
                                        $('<tr></tr>').append(
                                            $('<td></td>').text('opis'),
                                            $('<td></td>').append(
                                                device.description === null ?
                                                    $('<em></em>').text('(brak)') :
                                                    $('<span></span>').text(device.description)
                                            )
                                        ),
                                        $('<tr></tr>').append(
                                            $('<td></td>').text('znany'),
                                            $('<td></td>').text(device.known ? 'tak' : 'nie')
                                        ),
                                        $('<tr></tr>').append(
                                            $('<td></td>').text('id kontrolera'),
                                            $('<td></td>').text(device.controllerId)
                                        ),
                                        $('<tr></tr>').append(
                                            $('<td></td>').text('uruchomione'),
                                            $('<td></td>').text(device.enabled ? 'tak' : 'nie')
                                        ),
                                        $('<tr></tr>').append(
                                            $('<td></td>').text('aktualna liczba klientów'),
                                            $('<td></td>').text(device.clientsSum)
                                        )
                                    )
                                )
                            )
                        );
                    }
                },
                error: err
            });
            function err() {
                $("#result > *").remove();
                $('#result').append(
                    $('<div></div>').addClass('panel panel-danger').append(
                        $('<div></div>').addClass('panel-heading').text('wystąpił problem przy szukaniu urządzeń')
                    )
                );
            }
        }
    });
    $("#button_controller").click(function () {
        $("#result > *").remove();
        var selection = $('#settings').find('input[name=selection]:checked').val();
        if (selection === 'id_only') {
            $("#result").append(
                $("<div></div>").addClass('panel panel-info').append(
                    $('<div></div>').addClass('panel-heading').text('szukanie kontrolera ...')
                )
            );
            var argId = $('#textfield').val();
            $.ajax({
                type: 'GET',
                url: '/api/controller/' + argId,
                dataType: 'json',
                success: function (data) {
                    $("#result > *").remove();
                    $("#result").append(
                        $("<div></div>").addClass('panel panel-success').append(
                            $('<div></div>').addClass('panel-heading').text('Kontroler ' + data.name),
                            $('<table></table>').addClass('table table-bordered table-condensed table-hover').append(
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
                                        $('<td></td>').append(
                                            data.description === null ?
                                                $('<em></em>').text('(brak)') :
                                                $('<span></span>').text(data.description)
                                        )
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
                            $('<div></div>').addClass('panel-heading').text('wystąpił problem przy szukaniu kontrolera')
                        )
                    );
                },
                statusCode: {
                    404: function () {
                        $("#result > *").remove();
                        $('#result').append(
                            $('<div></div>').addClass('panel panel-danger').append(
                                $('<div></div>').addClass('panel-heading').text('kontroler o id=' + argId + ' nie istnieje')
                            )
                        );
                    }
                }
            });
        }
        else if (selection === 'all') {
            $("#result").append(
                $("<div></div>").addClass('panel panel-info').append(
                    $('<div></div>').addClass('panel-heading').text('szukanie kontrolerów ...')
                )
            );
            $.ajax({
                type: 'GET',
                url: '/api/controller/all',
                dataType: 'json',
                success: function (data) {
                    $("#result > *").remove();
                    var i;
                    for (i = 0; i < data.length; i++) {
                        var controller = data[i];
                        $("#result").append(
                            $("<div></div>").addClass('panel panel-success').append(
                                $('<div></div>').addClass('panel-heading').text('Kontroler ' + controller.name),
                                $('<table></table>').addClass('table table-bordered table-condensed table-hover').append(
                                    $('<tbody></tbody>').append(
                                        $('<tr></tr>').append(
                                            $('<td></td>').text('id'),
                                            $('<td></td>').text(controller.id)
                                        ),
                                        $('<tr></tr>').append(
                                            $('<td></td>').text('nazwa'),
                                            $('<td></td>').text(controller.name)
                                        ),
                                        $('<tr></tr>').append(
                                            $('<td></td>').text('adres IP'),
                                            $('<td></td>').text(controller.ipv4)
                                        ),
                                        $('<tr></tr>').append(
                                            $('<td></td>').text('opis'),
                                            $('<td></td>').append(
                                                controller.description === null ?
                                                    $('<em></em>').text('(brak)') :
                                                    $('<span></span>').text(controller.description)
                                            )
                                        )
                                    )
                                )
                            )
                        );
                    }
                },
                error: function () {
                    $("#result > *").remove();
                    $('#result').append(
                        $('<div></div>').addClass('panel panel-danger').append(
                            $('<div></div>').addClass('panel-heading').text('wystąpił problem przy szukaniu kontrolerów')
                        )
                    );
                }
            });
        }
    });
</script>
</body>
</html>