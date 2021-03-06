/*
 This file is part of the NetView open source project
 Copyright (c) 2017 NetView authors
 Licensed under The MIT License
 */
"use strict";
var devices = [];
var interGreen;

function onlyGreen()
{
    $('[data-toggle="tooltip"]').tooltip('destroy');
    $.ajax({
        type: 'GET',
        url: '/api/device/info/all',
        dataType: 'json',

        success: function(listDtoOfCurrentDeviceStateDto) {
            if(!listDtoOfCurrentDeviceStateDto.success) {
                err();
                return;
            }
            devices = listDtoOfCurrentDeviceStateDto.list;
            green();
        },
        error: err
    });
    function err() {
        $('#progress_area').show();
    }
}


function green(){
    clearInterval(inter);
    clearInterval(interRed);
    clearInterval(interGrey);
    var active=0;
    var inactive=0;
    var off=0;
    var all=0;
    var style='list-style-type: none;color:white;text-decoration:none;';


    for(var i = 0; i< devices.length; i++){
        var currentDeviceStateDto = devices[i];
        var state2400 = currentDeviceStateDto.frequencySurvey['2400'];
        var state5000 = currentDeviceStateDto.frequencySurvey['5000'];
        if(frequency == "2400"){
            if(typeof state2400 === 'undefined') {
                continue;
            }
            else {
                var sum = state2400.clients;
                var isEnabled = state2400.enabled;
                var time = state2400.timestamp;
            }
        }else if(frequency == "5000"){
            if(typeof state5000 === 'undefined') {
                continue;
            }
            else {
                var sum = state5000.clients;
                var isEnabled = state5000.enabled;
                var time = state5000.timestamp;
            }
        }

        var h = "/device/" + currentDeviceStateDto.id;
        var clazz= '';

        if (isEnabled == true) {
            if (sum > 0 && sum <= 10) {
                clazz = "greenDiode1";
                active++;
                var line = $('<li></li>').addClass(clazz)
                    .attr('title', currentDeviceStateDto.name)
                    .attr('data-toggle', 'tooltip')
                    .append(
                        $('<a>'+sum+'</a>').attr('href', h).attr('style',style)
                    );
                $('#devices').append(line);
                line.tooltip();
            } else if (sum > 10 && sum <= 30) {
                clazz = "greenDiode2";
                active++;
                var line = $('<li></li>').addClass(clazz)
                    .attr('title', currentDeviceStateDto.name)
                    .attr('data-toggle', 'tooltip')
                    .append(
                        $('<a>'+sum+'</a>').attr('href', h).attr('style',style)
                    );
                $('#devices').append(line);
                line.tooltip();
            } else if (sum > 30 && sum <= 47) {
                clazz = "greenDiode3";
                active++;
                var line = $('<li></li>').addClass(clazz)
                    .attr('title', currentDeviceStateDto.name)
                    .attr('data-toggle', 'tooltip')
                    .append(
                        $('<a>'+sum+'</a>').attr('href', h).attr('style',style)
                    );
                $('#devices').append(line);
                line.tooltip();
            } else if (sum > 47) {
                clazz = "greenDiode4";
                active++;
                var line = $('<li></li>').addClass(clazz)
                    .attr('title', currentDeviceStateDto.name)
                    .attr('data-toggle', 'tooltip')
                    .append(
                        $('<a>'+sum+'</a>').attr('href', h).attr('style',style)
                    );
                $('#devices').append(line);
                line.tooltip();
            } else if (sum == 0) {
                clazz = "redDiode";
                inactive++;
            }
        } else if(isEnabled == false){
            clazz = "greyDiode";
            sum='-';
            off++;
        }
    }

    all = active+inactive+off;

    $('#countActive').text('aktywne: ' + active);
    $('#countInactive').text('nieaktywne: ' + inactive);
    $('#countOff').text('wyłączone: ' + off);
    $('#countAll').text('wszystkie: ' + all);

    $('#progress_area').hide();


    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });

    var date = new Date(time*1000);
    var n = date.toLocaleString();
    $('#data').replaceWith('Ostatnie badanie sieci przeprowadzono:   ' + n);
};

var interRed;

function onlyRed()
{
    $('[data-toggle="tooltip"]').tooltip('destroy');
    $.ajax({
        type: 'GET',
        url: '/api/device/info/all',
        dataType: 'json',

        success: function(listDtoOfCurrentDeviceStateDto) {
            if(!listDtoOfCurrentDeviceStateDto.success) {
                err();
                return;
            }
            devices = listDtoOfCurrentDeviceStateDto.list;
            red();
        },
        error: err
    });
    function err() {
        $('#progress_area').show();
    }
}


function red(){
    clearInterval(interGreen);
    clearInterval(interGrey);
    clearInterval(inter);
    var active=0;
    var inactive=0;
    var off=0;
    var all=0;
    var style='list-style-type: none;color:white;text-decoration:none;';


    for(var i = 0; i< devices.length; i++){
        var currentDeviceStateDto = devices[i];
        var state2400 = currentDeviceStateDto.frequencySurvey['2400'];
        var state5000 = currentDeviceStateDto.frequencySurvey['5000'];
        if(frequency == "2400"){
            if(typeof state2400 === 'undefined') {
                continue;
            }
            else {
                var sum = state2400.clients;
                var isEnabled = state2400.enabled;
                var time = state2400.timestamp;
            }
        }else if(frequency == "5000"){
            if(typeof state5000 === 'undefined') {
                continue;
            }
            else {
                var sum = state5000.clients;
                var isEnabled = state5000.enabled;
                var time = state5000.timestamp;
            }
        }

        var h = "/device/" + currentDeviceStateDto.id;
        var clazz= '';

        if (isEnabled == true) {
            if (sum > 0 && sum <= 10) {
                clazz = "greenDiode1";
                active++;
            } else if (sum > 10 && sum <= 30) {
                clazz = "greenDiode2";
                active++;
            } else if (sum > 30 && sum <= 47) {
                clazz = "greenDiode3";
                active++;
            } else if (sum > 47) {
                clazz = "greenDiode4";
                active++;
            } else if (sum == 0) {
                clazz = "redDiode";
                inactive++;
                var line = $('<li></li>').addClass(clazz)
                    .attr('title', currentDeviceStateDto.name)
                    .attr('data-toggle', 'tooltip')
                    .append(
                        $('<a>'+sum+'</a>').attr('href', h).attr('style',style)
                    );
                $('#devices').append(line);
                line.tooltip();
            }
        } else if(isEnabled == false){
            clazz = "greyDiode";
            sum='-';
            off++;
        }
    }

    all = active+inactive+off;

    $('#countActive').text('aktywne: ' + active);
    $('#countInactive').text('nieaktywne: ' + inactive);
    $('#countOff').text('wyłączone: ' + off);
    $('#countAll').text('wszystkie: ' + all);

    $('#progress_area').hide();

    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });

    var date = new Date(time*1000);
    var n = date.toLocaleString();
    $('#data').replaceWith('Ostatnie badanie sieci przeprowadzono:   ' + n);
};

var interGrey;

function onlyGrey()
{
    $('[data-toggle="tooltip"]').tooltip('destroy');
    $.ajax({
        type: 'GET',
        url: '/api/device/info/all',
        dataType: 'json',

        success: function(listDtoOfCurrentDeviceStateDto) {
            if(!listDtoOfCurrentDeviceStateDto.success) {
                err();
                return;
            }
            devices = listDtoOfCurrentDeviceStateDto.list;
            grey();
        },
        error: err
    });
    function err() {
        $('#progress_area').show();
    }
}


function grey(){
    clearInterval(inter);
    clearInterval(interGreen);
    clearInterval(interRed);
    var active=0;
    var inactive=0;
    var off=0;
    var all=0;
    var style='list-style-type: none;color:white;text-decoration:none;';


    for(var i = 0; i< devices.length; i++){
        var currentDeviceStateDto = devices[i];
        var state2400 = currentDeviceStateDto.frequencySurvey['2400'];
        var state5000 = currentDeviceStateDto.frequencySurvey['5000'];
        if(frequency == "2400"){
            if(typeof state2400 === 'undefined') {
                continue;
            }
            else {
                var sum = state2400.clients;
                var isEnabled = state2400.enabled;
                var time = state2400.timestamp;
            }
        }else if(frequency == "5000"){
            if(typeof state5000 === 'undefined') {
                continue;
            }
            else {
                var sum = state5000.clients;
                var isEnabled = state5000.enabled;
                var time = state5000.timestamp;
            }
        }

        var h = "/device/" + currentDeviceStateDto.id;
        var clazz= '';

        if (isEnabled == true) {
            if (sum > 0 && sum <= 10) {
                clazz = "greenDiode1";
                active++;
            } else if (sum > 10 && sum <= 30) {
                clazz = "greenDiode2";
                active++;
            } else if (sum > 30 && sum <= 47) {
                clazz = "greenDiode3";
                active++;
            } else if (sum > 47) {
                clazz = "greenDiode4";
                active++;
            } else if (sum == 0) {
                clazz = "redDiode";
                inactive++;
            }
        } else if(isEnabled == false){
            clazz = "greyDiode";
            sum='-';
            off++;
            var line = $('<li></li>').addClass(clazz)
                .attr('title', currentDeviceStateDto.name)
                .attr('data-toggle', 'tooltip')
                .append(
                    $('<a>'+sum+'</a>').attr('href', h).attr('style',style)
                );
            $('#devices').append(line);
            line.tooltip();
        }
    }

    all = active+inactive+off;

    $('#countActive').text('aktywne: ' + active);
    $('#countInactive').text('nieaktywne: ' + inactive);
    $('#countOff').text('wyłączone: ' + off);
    $('#countAll').text('wszystkie: ' + all);

    $('#progress_area').hide();

    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });

    var date = new Date(time*1000);
    var n = date.toLocaleString();
    $('#data').replaceWith('Ostatnie badanie sieci przeprowadzono:   ' + n);
};

