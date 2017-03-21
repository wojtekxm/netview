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
            $('#data').replaceWith('Ostatnie badanie sieci przeprowadzono:   ' + n);
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
    var active=0;
    var inactive=0;
    var off=0;
    var all=0;
    var style='list-style-type: none;color:white;text-decoration:none;';

    $("#devices li").remove();

    for(var i = 0; i< devices.length; i++){
        var h = "/device?id=" + devices[i].id;
        var clazz= '';
        var sum = devices[i].clientsSum;

        if (devices[i].enabled == true) {
            if (sum > 0 && sum <= 10) {
                clazz = "greenDiode1";
                active++;
                var line = $('<li></li>').addClass(clazz)
                    .attr('title', devices[i].name)
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
                    .attr('title', devices[i].name)
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
                    .attr('title', devices[i].name)
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
                    .attr('title', devices[i].name)
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
        } else if(devices[i].enabled == false){
            clazz = "greyDiode";
            sum='-';
            off++;
        }
    }

    all = active+inactive+off;

    $('#countActive').text(active);
    $('#countInactive').text(inactive);
    $('#countOff').text(off);
    $('#countAll').text(all);

    $('#progress_area').hide();


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
    var active=0;
    var inactive=0;
    var off=0;
    var all=0;
    var style='list-style-type: none;color:white;text-decoration:none;';

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
                var line = $('<li></li>').addClass(clazz)
                    .attr('title', devices[i].name)
                    .attr('data-toggle', 'tooltip')
                    .append(
                        $('<a>'+sum+'</a>').attr('href', h).attr('style',style)
                    );
                $('#devices').append(line);
                line.tooltip();
            }
        } else if(devices[i].enabled == false){
            clazz = "greyDiode";
            sum='-';
            off++;
        }
    }

    all = active+inactive+off;

    $('#countActive').text(active);
    $('#countInactive').text(inactive);
    $('#countOff').text(off);
    $('#countAll').text(all);

    $('#progress_area').hide();

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
    var style='list-style-type: none;color:white;text-decoration:none;';

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
            var line = $('<li></li>').addClass(clazz)
                .attr('title', devices[i].name)
                .attr('data-toggle', 'tooltip')
                .append(
                    $('<a>'+sum+'</a>').attr('href', h).attr('style',style)
                );
            $('#devices').append(line);
            line.tooltip();
        }
    }

    all = active+inactive+off;

    $('#countActive').text(active);
    $('#countInactive').text(inactive);
    $('#countOff').text(off);
    $('#countAll').text(all);

    $('#progress_area').hide();

    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    })
    loadDate();
};

