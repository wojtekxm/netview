<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Informacje o urządzeniu</title>
    <link rel="icon" href="/favicon.ico">
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/status-small.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.bundle.min.js"
            type="text/javascript"></script>
    <script
            src="https://code.jquery.com/jquery-3.2.0.min.js"
            integrity="sha256-JAW99MJVpJBGcbzEuXk4Az05s/XyDdBomFqNlM3ic+I="
            crossorigin="anonymous"></script>
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
                <li><a style="background-color: #1d1d1d;" href="/"><span class="glyphicon glyphicon-home"></span></a></li>
                <li style="max-height:50px;"><a href="/make-survey">Nowe badanie</a></li>
                <li><a href="/all-controllers">Kontrolery</a></li>
                <li><a href="/all-users">Użytkownicy</a></li>
                <li><a href="/all-devices">Urządzenia</a></li>
                <li><a href="/all-buildings">Budynki</a></li>
                <li><a href="/all-units">Jednostki</a></li>
                <li><a href="/unitsbuildings">Jedn. Bud.</a></li>
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
<div id="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            szczegóły urządzenia
        </div>
        <table class="table table-bordered">
            <tr>
                <td>nazwa</td>
                <td><c:out value="${device.name}"/></td>
            </tr>
            <tr>
                <td>opis</td>
                <td><c:out value="${device.description}"/></td>
            </tr>
            <tr>
                <td>nazwa kontrolera</td>
                <td><c:out value="${controller.name}"/></td>
            </tr>
            <tr>
                <td>IP kontrolera</td>
                <td><c:out value="${controller.ipv4}"/></td>
            </tr>
            <tr>
                <td>opis kontrolera</td>
                <td><c:out value="${controller.description}"/></td>
            </tr>
        </table>
    </div>


    <div id="wykresy">
        <canvas id="mycanvas" width="1000px" style="border:0 solid #bce8f1;"></canvas>
    </div>

    <br/>
    <button type="button" id="onclick1">Dzień</button>
    <button type="button" id="onclick2">Tydzień</button>
    <button type="button" id="onclick3">Miesiąc</button>
    <button type="button" id="onclick4">Kwartał</button>
    <button type="button" id="onclick5">Rok</button>



    <style>
        #mycanvas {
            /*background-image: url("/images/logooWhite.jpg");*/
            width: 100% !important;
            max-width: 5000px !important;
            height: auto !important;
            image-rendering: -moz-crisp-edges; /* Firefox */
            image-rendering: -o-crisp-edges; /* Opera */
            image-rendering: -webkit-optimize-contrast; /* Webkit (non-standard naming) */
            image-rendering: crisp-edges;
            -ms-interpolation-mode: nearest-neighbor; /* IE (non-standard property) */
        }
    </style>

    <script>

        var now = Date.now();
        var doba = now - 86400000;       // dzien
        var tydzien = now - 604800000;   // 7 dni
        var miesiac = now - 2629743830; //30 dni
        var kwartal = now - 7889231490; //90 dni
        var rok = now - 31536000000;   // 365 dni
        generateChart(mycanvas, <c:out value="${device.id}"/>,0,now,"Wykres kompletny");
        var btn1=document.getElementById("onclick1");
        btn1.addEventListener("click", function(){generateChart(mycanvas,<c:out value="${device.id}"/>,doba,now,"Wykres dobowy");});
        var btn2=document.getElementById("onclick2");
        btn2.addEventListener("click", function(){generateChart(mycanvas, <c:out value="${device.id}"/>,tydzien,now,"Wykres tygodniowy");});
        var btn3=document.getElementById("onclick3");
        btn3.addEventListener("click",function(){generateChart(mycanvas, <c:out value="${device.id}"/>,miesiac,now,"Wykres miesieczny");});
        var btn4=document.getElementById("onclick4");
        btn4.addEventListener("click", function(){generateChart(mycanvas, <c:out value="${device.id}"/>,kwartal,now,"Wykres kwartalny");});
        var btn5=document.getElementById("onclick5");
        btn5.addEventListener("click", function(){generateChart(mycanvas, <c:out value="${device.id}"/>,rok,now,"Wykres roczny");});

        function convert(time) {
            var temp = new Date(time * 1000);
            var Seconds = temp.getSeconds();
            var Minutes = temp.getMinutes();
            var Hours = temp.getHours();
            var WeekDay = temp.getDay();
            var Month = temp.getMonth();
            var Day = temp.getDate();
            var Year = temp.getFullYear();
            return (Hours > 9 ? '' : '0') + Hours + ":" +
                (Minutes > 9 ? '' : '0') + Minutes + ":" +
                (Seconds > 9 ? '' : '0') + Seconds + "    " +
                Day + " " + MonthName[Month] + " " + Year + " (" + DayName[WeekDay] + ") ";
        }

        var DayName = ["niedziela", "poniedziałek", "wtorek", "sroda", "czwartek", "piątek", "sobota"];
        var MonthName = ["stycznia ", "lutego ", "marca ", "kwietnia ", "maja ", "czerwca ",
            "lipca ", "sierpnia ", "września ", "października ", "listopada ", "grudnia "];
        var now = Date.now();
        var doba = now - 86400000;       // dzien
        var tydzien = now - 604800000;   // 7 dni
        var miesiac = now - 2629743830; //30 dni
        var kwartal = now - 7889231490; //90 dni
        var rok = now - 31536000000;   // 365 dni
        console.log("doba:" + doba);
        console.log("tydzien:" + tydzien);
        console.log("miesiac:" + miesiac);
        console.log("kwartal:" + kwartal);
        console.log("rok:" + rok);
        function generateChart(mycanvas, id,timestamp1,timestamp2,etykieta) {
            var request = new XMLHttpRequest();
            $('#mycanvas').remove();
            $('#wykresy').append('<canvas id="mycanvas" width="1000px" style="border:0 solid #bce8f1;"></canvas>');
            mycanvas = document.querySelector('#mycanvas');

            var tags = [];      //WSZYSTKO
            var values = [];    //WSZYSTKO
            var options = {
                tooltips: {mode: 'index'},
                legend: {display: false},
                title: {display: true, text: etykieta},
                hover: {intersect: false,
                    mode:'x'

                },
                label: {display: true},
                scales: {
                    xAxes: [{display: false}],

                    yAxes: [{
                        display: true,
                        scaleLabel: {
                            display: true,
                            labelString: 'Ilosc klientów'
                        },
                        ticks: {
                            ticks: {
                                fixedStepSize: 10,
                                suggestedMin: 0,
                                suggestedMax: 50
                            }
                        }}]
                },
                responsive: true,
                scaleLineColor: 'black',
                steppedLine: true,
                elements: {line: {tension: 0}}
            };

            var data = {
                labels: tags,
                datasets: [{
                    label: "Ilosc klientow urzadzenia",
                    data: values,
                    fill: false,
                    borderColor: "rgba(0,255,0,1)",
                    fillStyle: "rgba(0,255,0,1)",
                    borderJoinStyle: 'miter',
                    pointBorderColor: "rgba(0,0,0,0)",
                    pointBackgroundColor: "rgba(0,255,0,1)",
                    pointBorderWidth: 1,
                    pointHoverRadius: 4,
                    pointHoverBackgroundColor: "rgba(0, 0, 0, 1)",
                    pointHoverBorderColor: "rgb(0,0,0,0)",
                    pointHoverBorderWidth: 4,
                    pointRadius: 2,
                    tension: 0
                }
                ]
            };
            request.open('Get', '/api/chart?id=' + id);
            request.onload = function () {
                var jsondata = JSON.parse(request.responseText);
                timestamp1 = timestamp1 / 1000;
                timestamp2 = timestamp2 / 1000;
                console.log("timestamp1=" + timestamp1);
                console.log("timestamp2=" + timestamp2);
                for (i = 0; i < jsondata.length; i++) {
                    if (timestamp1 <= jsondata[i][1] && jsondata[i][1] <= timestamp2) {
                        tags.push(convert(jsondata[i][1]));
                        console.log(jsondata[i][1]);
                        values.push(jsondata[i][0]);
                        console.log(jsondata[i][0]);
                    }
                }
                var myFirstChart = Chart.Line(mycanvas, {data: data, options: options});

            };
            request.send();
        }
    </script>
    <script src="/js/jquery-3.1.1.min.js"></script>
    <script src="/js/bootstrap-3.3.7.min.js"></script>
</div>
</body>
</html>
