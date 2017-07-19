<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Budynek <c:out value="${building.name}"/></title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css" media="screen">
    <link rel="stylesheet" href="/css/progress.css">
    <link rel="stylesheet" href="/css/notify.css">
    <link rel="stylesheet" href="/css/tabelka.css">
    <link rel="stylesheet" href="/css/style.css">
    <link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="/css/cookieconsent.min.css" media="screen">
    <script src="/js/cookieconsent.min.js"></script>
    <script>
        window.addEventListener("load", function(){
            window.cookieconsent.initialise({
                "palette": {
                    "popup": {
                        "background": "#3c404d",
                        "text": "#d6d6d6"
                    },
                    "button": {
                        "background": "#8bed4f"
                    }
                },
                "theme": "edgeless",
                "content": {
                    "message": "Ta strona wykorzystuje pliki cookies. Korzystanie z witryny oznacza zgodę na ich zapis lub odczyt wg ustawień przeglądarki.",
                    "dismiss": "OK",
                    "link": "O polityce cookies",
                    "href": "wszystkoociasteczkach.pl/polityka-cookies/"
                }
            })});
    </script>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" style="background-color: #080b08;">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myDiv">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>

        <div class="collapse navbar-collapse" id="myDiv">
            <ul class="nav navbar-nav" style="padding-right:3px;font-size: 16px;">
                <ul class="nav navbar-nav" style="padding-right:3px;font-size: 16px;">
                    <li><a style="background-color: black;padding-left:25px;padding-right: 20px;" href="/"><span class="glyphicon glyphicon-home"></span> &nbsp;NetView &nbsp;</a></li>
                    <c:if test="${loggedUser.role eq 'ROOT'}">  <li><a href="/all-controllers">Kontrolery</a></li>
                        <li><a href="/all-users">Użytkownicy</a></li>
                        <li><a href="/all-devices">Urządzenia</a></li>
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Lokalizacje<span class="caret"></span></a>
                            <ul class="dropdown-menu"  style="background-color: #080b08;">
                                <li><a href="/all-buildings">Budynki</a></li>
                                <li><a href="/all-units">Jednostki</a></li>
                            </ul>
                        </li>
                    </c:if>
                </ul>
            </ul>
            <c:if test="${loggedUser.role eq 'ROOT'}">  <form method="get" action="/search" class="navbar-form navbar-nav" style="margin-right:5px;font-size: 16px;">
                <div class="form-group" style="display:flex;">
                    <input type="text" name="query" class="form-control" placeholder="Szukaj..." style="margin-right:4px;max-width: 150px!important;">
                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
                </div>
            </form></c:if>
            <ul class="nav navbar-nav navbar-right" style="padding-right:3px;font-size: 16px;">
                <c:if test="${loggedUser.role eq 'ROOT'}">  <li style="margin-left: 0px!important;"><a href="/settings"><span class="glyphicon glyphicon-wrench"></span>  Ustawienia</a></li></c:if>
                <li><a href="/account"><span class="glyphicon glyphicon-user"></span>  <c:out value="${loggedUser.name}"/></a></li>
                <li><a href="/logout" style="margin-right: 10px;"><span class="glyphicon glyphicon-log-out"></span>  Wyloguj</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container" style="margin-top:80px">
    <div class="on-loading"></div>
    <div class="on-loaded">
                <div class="row">
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading"><span class="glyphicon glyphicon-home"></span> Informacje o budynku</div>
                            <div class="panel-body">
                                <ul class="list-group">
                                    <li class="list-group-item">
                                        <div class="row">
                                            <div class="col-xs-4">nazwa</div>
                                            <div class="col-xs-8" id="field_name"></div>
                                        </div>
                                    </li>
                                    <li class="list-group-item">
                                        <div class="row">
                                            <div class="col-xs-4">kod</div>
                                            <div class="col-xs-8" id="field_code"></div>
                                        </div>
                                    </li>
                                    <li class="list-group-item">
                                        <div class="row">
                                            <div class="col-xs-4">adres</div>
                                            <div class="col-xs-8" id="field_address"></div>
                                        </div>
                                    </li>
                                </ul>
                                <a href="/modify-building?id=${building.id}" class="btn btn-success pull-left" role="button">
                                    <span class="glyphicon glyphicon-wrench"></span>
                                    Zmień
                                </a>
                                <form method="post" action="/building/remove/${building.id}">
                                    <button type="submit" class="btn btn-danger" style="margin-left: 15px">
                                        <span class="glyphicon glyphicon-trash"></span>
                                        Usuń
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div id="map" style="width: 100%; height: 275px; box-shadow: 0 0 10px -2px black"></div>
                    </div>
                </div>

                <div class="panel panel-default on-loaded">
                    <div class="panel-heading" style="margin-top:20px; font-size: 15px!important;">
                        Jednostki organizacyjne powiązane z budynkiem
                    </div>
                    <div class="panel-body">
                        <div id="tabelka_units"></div>
                        <a href="/link-building-all-units?id=${building.id}" class="btn btn-success" role="button">
                            <span class="glyphicon glyphicon-plus"></span>
                            Połącz z jednostką organizacyjną...
                        </a>
                    </div>
                </div>

                <div class="panel panel-default on-loaded">
                    <div class="panel-heading" style="margin-top:40px; font-size: 15px!important;">
                        Kontrolery znajdujące się w budynku
                    </div>
                    <div class="panel-body">
                        <div id="tabelka_controllers"></div>
                    </div>
                </div>

                <div class="panel panel-default on-loaded">
                    <div class="panel-heading" style="margin-top:40px; font-size: 15px!important;">
                        Urządzenia znajdujące się w budynku
                    </div>
                    <div class="panel-body">
                        <div id="tabelka_devices"></div>
                        <button class="btn btn-success" data-toggle="modal" data-target="#modalLinkDevices">
                            <span class="glyphicon glyphicon-plus"></span>
                            Przenieś urządzenie
                        </button>
                    </div>
                </div>
    </div>
</div>
<div id="modalLinkDevices" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">Wybierz urządzenia które zostaną przeniesione do budynku</h4>
            </div>
            <div class="modal-body">
                <div id="loading_modal_devices" class="progress-space"></div>
                <div id="tabelka_modal_devices" style="overflow: auto"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">Zamknij</button>
            </div>
        </div>
    </div>
</div>
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap-3.3.7.min.js"></script>
<script src="/js/progress.js"></script>
<script src="/js/tabelka.js"></script>
<script>
"use strict";
var building;
$(document).ready(function(){
    var buildingId = ${building.id};
    var tabelkaDevices, tabelkaModalDevices, modalLinkDevices, currentDevices, mapDevicesId;
    tabelkaDevices = null;
    tabelkaModalDevices = null;
    modalLinkDevices = $('#modalLinkDevices');
    currentDevices = [];
    mapDevicesId = {};

    progress.loadParallel(
        [{
            "url" : '/api/building/info/' + buildingId
        }, {
            "url" : '/api/building/units/' + buildingId
        }, {
            "url" : '/api/building/controllers-details/' + buildingId
        }, {
            "url" : '/api/building/devices-details/' + buildingId
        }],
        ['.on-loading'], ['.on-loaded'], [],
        function(responses) {
            building = responses[0].content;
            $('#field_name').text(building.name);
            $('#field_code').text(building.code);
            $('#field_address').append(
                building.street + ' ' + building.number,
                $('<br>'),
                building.postalCode + ' ' + building.city);

            fixUnits(responses[1].list);
            fixControllers(responses[2].list);
            fixDevices(responses[3].list);

            $('body').append(
                $('<script/>', {
                    "src" : 'https://maps.googleapis.com/maps/api/js?key=AIzaSyCR0onCdpLDKtCPdd1h9Fpc3ENsrhPj_Q0&callback=initMap',
                    "async" : 'async',
                    "defer" : 'defer'
                })
            );
        }, undefined, 'md'
    );

    modalLinkDevices.on('show.bs.modal', function() {
        progress.loadGet(
            '/api/device/details/all',
            ['#loading_modal_devices'], ['#tabelka_modal_devices'], [],
            function(response) {
                fixModalDevices(response.list);
            }
        )
    });

    function fixUnits(listOfUnitDto) {
        tabelka.builder()
            .searchGenerator(function(u) {
                return [u.description, u.code];
            })
            .column('nazwa', 'text', 'description', 12, function(unit) {
                    return $('<a></a>')
                        .attr('href', '/unit/' + unit.id)
                        .text(unit.description);
            })
            .column('kod', 'text', 'code', 4, function(unit) {
                    return $('<span></span>').text(unit.code);
            })
            .buttonUnlink('usuń', function(unitId) {
                return [ {
                    "url" : '/api/building/unlink-unit/',
                    "method" : 'post',
                    "postData" : {
                        "buildingId" : building.id,
                        "unitId" : unitId
                    }
                }, {
                    "url" : '/api/building/units/' + building.id
                } ];
            }, function(responses) {
                fixUnits(responses[1].list);
            })
            .build('#tabelka_units', listOfUnitDto);
    }

    function fixControllers(listOfControllerDetailsDto) {
        tabelka.builder('!')
            .searchGenerator(function(c) {
                return [c.name, c.ipv4];
            })
            .column('nazwa', 'text', 'name', 3, function(cont) {
                return $('<a></a>')
                    .attr('href', '/controller/' + cont.id)
                    .text(cont.name);
            })
            .column('opis', 'text', 'description', 3, 'description')
            .column('IP', 'text', 'ipv4', 3, function(cont) {
                return $('<samp></samp>').text(cont.ipv4);
            })
            .column('community', 'text', 'communityString', 3, function(cont) {
                return $('<samp></samp>').text(cont.communityString);
            })
            .column('prawdziwy', 'number', 'cmp_fake', 2, function(cont) {
                if(cont.fake) {
                    cont.cmp_fake = 0;
                    return $('<span></span>').append(
                        $('<span class="glyphicon glyphicon-check" style="color:#5cb85c"></span>'),
                        ' tak'
                    );
                }
                else {
                    cont.cmp_fake = 1;
                    return $('<span></span>').append(
                        $('<span class="glyphicon glyphicon-unchecked" style="color:#d9534f"></span>'),
                        ' nie'
                    );
                }
            })
            .column('urządzenia', 'number', 'numberOfDevices', 2, 'numberOfDevices')
            .buttonUnlink('usuń', function(controllerId) {
                return [ {
                    "url" : '/api/controller/unlink-building/' + controllerId,
                    "method" : 'post'
                }, {
                    "url" : '/api/building/controllers-details/' + building.id
                } ];
            }, function(responses) {
                fixControllers(responses[1].list);
            })
            .build('#tabelka_controllers', listOfControllerDetailsDto);
    }

    function fixDevices(listOfDeviceDetailsDto) {
        var i;
        if(tabelkaDevices === null) {
            tabelkaDevices = tabelka.builder()
                .searchGenerator(function(d) {
                    return [d.name];
                })
                .column('nazwa', 'text', 'name', 6, function (dev) {
                    return $('<a></a>')
                        .attr('href', '/device/' + dev.id)
                        .text(dev.name);
                })
                .deviceFrequency('2,4 GHz', 2400, 2)
                .deviceFrequency('5 GHz', 5000, 2)
                .column('kontroler', 'text', 'cmp_controller', 6, function (dev) {
                    var con = dev.controller;
                    dev.cmp_controller = null;
                    if (con !== null) {
                        dev.cmp_controller = con.name;
                        return $('<a></a>')
                            .attr('href', '/controller/' + con.id)
                            .text(con.name);
                    }
                    return $('<span></span>').text('-');
                })
                .buttonUnlink('usuń', function (deviceId) {
                    return [{
                        "url": '/api/device/unlink-building/' + deviceId,
                        "method": 'post'
                    }, {
                        "url": '/api/building/devices-details/' + building.id
                    }];
                }, function (responses) {
                    fixDevices(responses[1].list);
                })
                .build('#tabelka_devices', listOfDeviceDetailsDto);
        }
        else {
            tabelkaDevices.resetData(listOfDeviceDetailsDto);
        }
        currentDevices = listOfDeviceDetailsDto;
        mapDevicesId = {};
        for(i = 0; i < currentDevices.length; i++) {
            mapDevicesId[currentDevices[i].id] = true;
        }
    }

    function fixModalDevices(listOfDeviceDetailsDto) {
        if(tabelkaModalDevices === null) {
            tabelkaModalDevices = tabelka.builder()
                .searchGenerator(function(d) {
                    return [d.name];
                })
                .column('nazwa', 'text', 'name', 6, function (dev) {
                    return $('<a></a>')
                        .attr('href', '/device/' + dev.id)
                        .text(dev.name);
                })
                .deviceFrequency('2,4 GHz', 2400, 2)
                .deviceFrequency('5 GHz', 5000, 2)
                .column('kontroler', 'text', 'cmp_controller', 6, function (dev) {
                    var con = dev.controller;
                    dev.cmp_controller = null;
                    if (con !== null) {
                        dev.cmp_controller = con.name;
                        return $('<a></a>')
                            .attr('href', '/controller/' + con.id)
                            .text(con.name);
                    }
                    return $('<span></span>').text('-');
                })
                .special('', 0, function (dev) {
                    dev.button_span = $('<span class="glyphicon glyphicon-plus"></span>');
                    dev.button = $('<button class="btn btn-success btn-xs pull-right"></button>');
                    if (hasDevice(dev.id)) {
                        dev.button.removeClass('btn-success');
                        dev.button.addClass('btn-danger');
                        dev.button_span.removeClass('glyphicon-plus');
                        dev.button_span.addClass('glyphicon-minus');
                    }
                    dev.button.click({
                        "deviceId": dev.id
                    }, function (event) {
                        var i, devId, url;
                        devId = event.data.deviceId;
                        for (i = 0; i < listOfDeviceDetailsDto.length; i++) {
                            listOfDeviceDetailsDto[i].button.prop('disabled', true);
                        }
                        if (hasDevice(devId)) {
                            url = '/api/device/unlink-building/' + devId;
                        }
                        else {
                            url = '/api/device/link-building/' + devId + '/' + building.id;
                        }
                        progress.load(
                            [{
                                "url": url,
                                "method": 'post'
                            }, {
                                "url": '/api/building/devices-details/' + building.id
                            }, {
                                "url": '/api/device/details/all'
                            }],
                            ['#loading_link_device_' + devId], [], [],
                            function (responses) {
                                fixDevices(responses[1].list);
                                fixModalDevices(responses[2].list);
                                var i;
                                for (i = 0; i < listOfDeviceDetailsDto.length; i++) {
                                    listOfDeviceDetailsDto[i].button.prop('disabled', false);
                                }
                            }, undefined, 'xs');
                    }).append(
                        dev.button_span
                    );
                    return $('<div class="clearfix" style="min-width:50px"></div>').append(
                        dev.button,
                        $('<div class="progress-space-xs pull-right"></div>')
                            .attr('id', 'loading_link_device_' + dev.id)
                    );
                })
                .build('#tabelka_modal_devices', listOfDeviceDetailsDto);
        }
        else {
            tabelkaModalDevices.resetData(listOfDeviceDetailsDto);
        }
    }

    function hasDevice(id) {
        return (typeof mapDevicesId[id] !== 'undefined') &&
            (mapDevicesId[id] === true);
    }
});
function initMap() {
    var place = {
        lat: building.latitude,
        lng: building.longitude
    };
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 14,
        center: place
    });
    var marker = new google.maps.Marker({
        position: place,
        map: map
    });
}
</script>
</body>
</html>
