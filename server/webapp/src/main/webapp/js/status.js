




function loadDate() {
    var devices = new Array();

    $.ajax({
        type: 'GET',
        url: '/api/all-devices',
        dataType: 'json',

        success: function(data) {
            devices=data;
            var date = new Date(devices[0].lastSurveyTimestamp*1000);
            var n = date.toLocaleString();
            $('#data').html('Ostatnie badanie sieci przeprowadzono:     ' + n);
        }
    });
}

var interGreen;

function onlyGreen()
{
    $.ajax({
        type: 'GET',
        url: '/api/all-devices',
        dataType: 'json',

        success: function(data) {
            devices = data;
            green();
        },
    });
}


function green(){
    clearInterval(inter);
    clearInterval(interRed);
    clearInterval(interGrey);
    var line = '';
    var active=0;
    var inactive=0;
    var off=0;
    var all=0;

    $("#devices li").remove();

    for(var i = 0; i< devices.length; i++){
        var h = "/device?id=" + devices[i].id;
        var t = devices[i].name;
        var clazz= '';
        var sum = devices[i].clientsSum;

        if (devices[i].enabled == true) {
            if (sum > 0 && sum <= 10) {
                clazz = "greenDiode1";
                active++;
                line += '<li class="' + clazz + '" title="' + t + '" data-toggle="tooltip" data-html="true"><a href="' + h + '" style="list-style-type: none;color:white;text-decoration:none;">' + sum + '</a></li>';
            } else if (sum > 10 && sum <= 30) {
                clazz = "greenDiode2";
                active++;
                line += '<li class="' + clazz + '" title="' + t + '" data-toggle="tooltip" data-html="true"><a href="' + h + '" style="list-style-type: none;color:white;text-decoration:none;">' + sum + '</a></li>';
            } else if (sum > 30 && sum <= 47) {
                clazz = "greenDiode3";
                active++;
                line += '<li class="' + clazz + '" title="' + t + '" data-toggle="tooltip" data-html="true"><a href="' + h + '" style="list-style-type: none;color:white;text-decoration:none;">' + sum + '</a></li>';
            } else if (sum > 47) {
                clazz = "greenDiode4";
                active++;
                line += '<li class="' + clazz + '" title="' + t + '" data-toggle="tooltip" data-html="true"><a href="' + h + '" style="list-style-type: none;color:white;text-decoration:none;">' + sum + '</a></li>';
            } else if (sum == 0) {
                clazz = "redDiode";
                inactive++;
            }
        } else if(devices[i].enabled == false){
            clazz = "greyDiode";
            sum='-';
            off++;
        }
    }

    all = active+inactive+off;

    document.getElementById('countActive').innerHTML='<span>&emsp;aktywne: &nbsp;'+active+'&emsp;</span>';
    document.getElementById('countInactive').innerHTML='<span>&emsp;nieaktywne: &nbsp;'+inactive+'&emsp;</span>';
    document.getElementById('countOff').innerHTML='<span>&emsp;wyłączone: &nbsp;'+off+'&emsp;</span>';
    document.getElementById('countAll').innerHTML='<span>&emsp;wszystkie: &nbsp;'+all+'&emsp;</span>';

    $("#devices").html(line);

    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    })
    loadDate();
};

var interRed;

function onlyRed()
{
    $.ajax({
        type: 'GET',
        url: '/api/all-devices',
        dataType: 'json',

        success: function(data) {
            devices = data;
            red();
        },
    });
}


function red(){
    clearInterval(interGreen);
    clearInterval(interGrey);
    clearInterval(inter);
    var line = '';
    var active=0;
    var inactive=0;
    var off=0;
    var all=0;

    $("#devices li").remove();

    for(var i = 0; i< devices.length; i++){
        var h = "/device?id=" + devices[i].id;
        var t = devices[i].name;
        var clazz= '';
        var sum = devices[i].clientsSum;

        if (devices[i].enabled == true) {
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
                line += '<li class="' + clazz + '" title="' + t + '" data-toggle="tooltip" data-html="true"><a href="' + h + '" style="list-style-type: none;color:white;text-decoration:none;">' + sum + '</a></li>';
            }
        } else if(devices[i].enabled == false){
            clazz = "greyDiode";
            sum='-';
            off++;
        }
    }

    all = active+inactive+off;

    document.getElementById('countActive').innerHTML='<span>&emsp;aktywne: &nbsp;'+active+'&emsp;</span>';
    document.getElementById('countInactive').innerHTML='<span>&emsp;nieaktywne: &nbsp;'+inactive+'&emsp;</span>';
    document.getElementById('countOff').innerHTML='<span>&emsp;wyłączone: &nbsp;'+off+'&emsp;</span>';
    document.getElementById('countAll').innerHTML='<span>&emsp;wszystkie: &nbsp;'+all+'&emsp;</span>';

    $("#devices").html(line);

    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    })
    loadDate();
};

var interGrey;

function onlyGrey()
{
    $.ajax({
        type: 'GET',
        url: '/api/all-devices',
        dataType: 'json',

        success: function(data) {
            devices = data;
            grey();
        },
    });
}


function grey(){
    clearInterval(inter);
    clearInterval(interGreen);
    clearInterval(interRed);
    var active=0;
    var inactive=0;
    var off=0;
    var all=0;
    var line = '';

    $("#devices li").remove();

    for(var i = 0; i< devices.length; i++){
        var h = "/device?id=" + devices[i].id;
        var t = devices[i].name;
        var clazz= '';
        var sum = devices[i].clientsSum;

        if (devices[i].enabled == true) {
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
        } else if(devices[i].enabled == false){
            clazz = "greyDiode";
            sum='-';
            off++;
            line += '<li class="' + clazz + '" title="' + t + '" data-toggle="tooltip" data-html="true"><a href="' + h + '" style="list-style-type: none;color:white;text-decoration:none;">' + sum + '</a></li>';
        }
    }

    all = active+inactive+off;

    document.getElementById('countActive').innerHTML='<span>&emsp;aktywne: &nbsp;'+active+'&emsp;</span>';
    document.getElementById('countInactive').innerHTML='<span>&emsp;nieaktywne: &nbsp;'+inactive+'&emsp;</span>';
    document.getElementById('countOff').innerHTML='<span>&emsp;wyłączone: &nbsp;'+off+'&emsp;</span>';
    document.getElementById('countAll').innerHTML='<span>&emsp;wszystkie: &nbsp;'+all+'&emsp;</span>';

    $("#devices").html(line);

    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    })
    loadDate();
};

$(function () {
    $('[data-toggle="tooltip"]').tooltip();
})
