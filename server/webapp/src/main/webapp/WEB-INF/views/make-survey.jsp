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
    <link rel="stylesheet" href="/css/style.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" style="margin-bottom: 50px;background-color: #2e302e;">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myDiv">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <div class="navbar-brand" title="Control your network">Network Monitor</div>
        </div>

        <div class="collapse navbar-collapse" id="myDiv">
            <ul class="nav navbar-nav" style="padding-right:3px;font-size: 16px;">
                <li><a style="background-color: black;" href="/"><span class="glyphicon glyphicon-home"></span></a></li>
                <li style="max-height:50px;"><a href="/make-survey">Nowe badanie</a></li>
                <li><a href="/all-controllers">Kontrolery</a></li>
                <li><a href="/all-users">Użytkownicy</a></li>
                <li><a href="/all-devices">Urządzenia</a></li>
                <li><a href="/all-buildings">Budynki</a></li>
                <%--<li><a href="/all-units">Jednostki</a></li>--%>
                <%--<li><a href="/unitsbuildings">Jedn. Bud.</a></li>--%>
            </ul>
            <form class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 150px!important;">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
                </div>
            </form>
            <ul class="nav navbar-nav navbar-right" style="padding-right:3px;font-size: 16px;">
                <li><a href="/account"><span class="glyphicon glyphicon-user"></span>  Mój profil</a></li>
                <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>

<!-- kill me pls -->
<%--<div style="margin-top:100px"></div>--%>
<!-- I'm not a solution -->

<div id="container">
    <div style="height: 10px;"></div>
    <div id="progress_area">
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
                            success: function(contentDtoOfExamineResultDto) {
                                if(contentDtoOfExamineResultDto.success) {
                                    span.removeClass('glyphicon glyphicon-exclamation-sign glyphicon-hourglass glyphicon-ok-circle');
                                    span.addClass('glyphicon glyphicon-ok-circle');
                                    small.text(contentDtoOfExamineResultDto.queryTime.toFixed(2) + ' sek.');
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
