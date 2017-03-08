<%@ page import="zesp03.servlet.MakeSurveyServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Double time = (Double) request.getAttribute(MakeSurveyServlet.ATTR_TIME);
    Long rows = (Long) request.getAttribute(MakeSurveyServlet.ATTR_ROWS);
%>
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
        <div id="loading">
            <em>pobieranie listy kontrolerów...</em>
        </div>
        <div id="box" style="display: none">
            <ul id="list_controllers" class="list-group"></ul>
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
                        $('#list_controllers').append(
                          $('<li></li>')
                              .addClass('list-group-item row')
                              .append(
                                  $('<div></div>')
                                      .addClass('col-sm-2')
                                      .append(
                                          $('<input type="checkbox">')
                                              .prop('checked', true)
                                      ),
                                  $('<div></div>')
                                      .addClass('col-sm-4')
                                      .append(
                                          $('<strong></strong>').text(listOfControllerRow[i].name)
                                      ),
                                  $('<div></div>')
                                      .addClass('col-sm-6')
                                      .append(
                                          $('<span></span>')
                                      )
                              )
                              .data('controller_id', listOfControllerRow[i].id)
                        );
                    }
                    $('#btn_examine').click( function() {
                        $('#list_controllers > li').each( function() {
                            var li = $(this);
                            var resultField = li.find('span:nth-of-type(1)');
                            var checkbox = li.find('input');
                            resultField.text('');
                            if(! checkbox.is(':checked') ) {
                                return;
                            }
                            resultField.text('badanie...');
                            $.ajax( {
                                type: 'post',
                                url: '/api/examine',
                                dataType: 'json',
                                data: {
                                    id: li.data('controller_id')
                                },
                                success: function(examineResultDto) {
                                    if(examineResultDto.success) {
                                        resultField.text('OK, ' +
                                            examineResultDto.timeElapsed.toFixed(2) + ' s, ' +
                                            examineResultDto.updatedDevices + ' zaktualizowanych urządzeń');
                                    }
                                    else {
                                        resultField.text('ERROR (server)');
                                    }
                                },
                                error: function() {
                                    resultField.text('ERROR (HTTP)');
                                }
                            } );
                        } );
                    } );
                    $('#loading').hide(400);
                    $('#box').show(400);
                },
                error: function() {
                    $('#loading').text('Fatal error');
                }
            } );
        } );
    </script>
</body>
</html>
