<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Nowe badanie</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
</head>
<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">Network Monitor</a>
        </div>
        <ul class="nav navbar-nav">
            <li class="active"><a href="/make-survey">nowe badania</a></li>
            <li><a href="/status-small">urządzenia (mały widok)</a></li>
            <li><a href="/status">urządzenia (średni widok)</a></li>
            <li><a href="/all-controllers">kontrolery</a></li>
            <li><a href="/building">budynki</a></li>
            <li><a href="/logout">wyloguj</a></li>
        </ul>
        <hr>
    </div>
</nav>
<div class="container">
    <div id="progress_area">
        pobieranie informacji...
    </div>
    <div id="box" style="display: none">
        <ul id="list_controllers" class="list-group"></ul>
        <button id="btn_all"     type="button">zaznacz wszystkie</button>
        <button id="btn_none"    type="button">odznacz wszystkie</button>
        <button id="btn_inverse" type="button">odwróć zaznaczenie</button><br>
        <button id="btn_examine" type="button">zbadaj</button>
    </div>

</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script>
    $(document).ready( function() {
        $.ajax( {
            type: 'get',
            url: '/api/all-controllers',
            dataType: 'json',
            success: function(listOfControllerRow) {
                var i;
                for(i = 0; i < listOfControllerRow.length; i++) {
                    var controllerRow = listOfControllerRow[i];
                    $('#list_controllers').append(
                        $('<li></li>')
                            .addClass('list-group-item row')
                            .append(
                                $('<div></div>')
                                    .addClass('col-sm-1')
                                    .append(
                                        $('<input type="checkbox">')
                                            .prop('checked', true)
                                    ),
                                $('<div></div>')
                                    .addClass('col-sm-2')
                                    .append(
                                        $('<a></a>').text(controllerRow.name)
                                            .attr('href', '/controller?id=' + controllerRow.id)
                                    ),
                                $('<div></div>')
                                    .addClass('col-sm-3')
                                    .append(
                                        $('<span></span>').text(controllerRow.ipv4)
                                    ),
                                $('<div></div>')
                                    .addClass('col-sm-6')
                                    .append(
                                        '<span></span>',
                                        ' ',
                                        '<small></small>'
                                    )
                            )
                            .data('controller_id', controllerRow.id)
                    );
                }
                $('#btn_all').click( function() {
                    $('#list_controllers').find('> li').each( function() {
                        $(this).find('input').prop('checked', true);
                    } );
                } );
                $('#btn_none').click( function() {
                    $('#list_controllers').find('> li').each( function() {
                        $(this).find('input').prop('checked', false);
                    } );
                } );
                $('#btn_inverse').click( function() {
                    $('#list_controllers').find('> li').each( function() {
                        var checkbox = $(this).find('input');
                        if( checkbox.prop('checked') ) {
                            checkbox.prop('checked', false);
                        }
                        else {
                            checkbox.prop('checked', true);
                        }
                    } );
                } );
                $('#btn_examine').click( function() {
                    $('#list_controllers').find('> li').each( function() {
                        var li = $(this);
                        var div4 = li.children('div:nth-of-type(4)');
                        var span = div4.children('span:nth-of-type(1)');
                        var small = div4.children('small:nth-of-type(1)');
                        var checkbox = li.find('input');
                        span.removeClass('glyphicon glyphicon-exclamation-sign glyphicon-hourglass glyphicon-ok-circle');
                        small.text('');
                        if(! checkbox.is(':checked') ) {
                            return;
                        }
                        span.addClass('glyphicon glyphicon-hourglass');
                        $.ajax( {
                            type: 'post',
                            url: '/api/examine',
                            dataType: 'json',
                            data: {
                                id: li.data('controller_id')
                            },
                            success: function(examineResultDto) {
                                if(examineResultDto.success) {
                                    span.removeClass('glyphicon glyphicon-exclamation-sign glyphicon-hourglass glyphicon-ok-circle');
                                    span.addClass('glyphicon glyphicon-ok-circle');
                                    small.text(examineResultDto.timeElapsed.toFixed(2) + ' sek.');
                                }
                                else {
                                    span.removeClass('glyphicon glyphicon-exclamation-sign glyphicon-hourglass glyphicon-ok-circle');
                                    span.addClass('glyphicon glyphicon-exclamation-sign');
                                }
                            },
                            error: function() {
                                span.removeClass('glyphicon glyphicon-exclamation-sign glyphicon-hourglass glyphicon-ok-circle');
                                span.addClass('glyphicon glyphicon-exclamation-sign');
                            }
                        } );
                    } );
                } );
                $('#progress_area').hide();
                $('#box').show(400);
            },
            error: function() {
                $('#progress_area').text('Wystąpił problem');
            }
        } );
    } );
</script>
</body>
</html>