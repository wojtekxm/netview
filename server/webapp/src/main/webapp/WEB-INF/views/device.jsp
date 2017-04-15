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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.bundle.js"
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
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">Lokalizacje<span class="caret"></span></a>
                    <ul class="dropdown-menu"  style="background-color: #080b08;">
                        <li><a href="/all-buildings">Budynki</a></li>
                        <li><a href="/all-units">Jednostki</a></li>
                        <li><a href="/unitsbuildings">Jedn. Bud.</a></li>
                    </ul>
                </li>
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
    <br/> <br/> <br/>
    <div id="control">
        <form action="">
            <input type="radio" name="range" value="dzien"> Dzien
            <input type="radio" name="range" value="tydzien"> Tydzien
            <input type="radio" name="range" value="miesiac"> Miesiac
            <input type="radio" name="range" value="kwartal"> Kwartal
            <input type="radio" name="range" value="rok"> Rok
        </form></br>
        <form action="">
            <input type="radio" name="group" value="1"> 5 minut
            <input type="radio" name="group" value="2"> 30 minut
            <input type="radio" name="group" value="3"> 3 godziny
            <input type="radio" name="group" value="4"> dzień
            <input type="radio" name="group" value="5"> tydzień
            <input type="radio" name="group" value="6"> miesiąc
            <input type="radio" name="group" value="7"> rok
        </form></br>
        <form action="">
            <input type="radio" name="type" value="1"> Wykres liniowy
            <input type="radio" name="type" value="2"> Wykres słupkowy
        </form></br>
        <form action="">
            <input type="radio" name="frequency" value="1"> Częstotliwość 2,4Ghz
            <input type="radio" name="frequency" value="2"> Częstotliwość 5Ghz
        </form></br>
        <button type="button" id="generate">Generuj wykres</button>
        <button type="button" id="onclick1">Dzień</button>

    </div>

    <br/>
    <div id="wykresy">
        <canvas id="mycanvas" width="1000px" height="500px" style="border:0 solid #bce8f1;"></canvas>
    </div>


</div>





    <style>
        <%-- #mycanvas {
             width: 100% !important;
             max-width: 5000px !important;
             height: auto !important;
             image-rendering: -moz-crisp-edges; /* Firefox */
             image-rendering: -o-crisp-edges; /* Opera */
             image-rendering: -webkit-optimize-contrast; /* Webkit (non-standard naming) */
             image-rendering: crisp-edges;
             -ms-interpolation-mode: nearest-neighbor; /* IE (non-standard property) */
         } --%>
        #wykresy{
            max-height: 500px;
            max-width: 100%;
            overflow:scroll;

        }
     </style>

    <script>
        var range1=0;
        var range2=0;
        var group=0;
        var frequency=0;
        var type=0;
        var now = Date.now();
        var teraz=Math.round(now/1000);
        var doba = Math.round((now - 86400000)/1000);       // dzien
        var tydzien = Math.round((now - 604800000)/1000);   // 7 dni
        var miesiac = Math.round((now - 2629743830)/1000); //30 dni
        var kwartal = Math.round((now - 7889231490)/1000); //90 dni
        var rok = Math.round((now - 31536000000)/1000);   // 365 dni

        $(document).ready(function() {
            $('input[type=radio][name=range]').change(function() {
                if (this.value=='dzien') {
                    range1=Math.round((Date.now() - 86400000)/1000); console.log("zakres1:" + convert(range1));
                    range2=Math.round(Date.now()/1000); console.log("zakres2:" + convert(range2));
                }
                if (this.value=='tydzien') {
                    range1=Math.round((Date.now() - 604800000)/1000); console.log("zakres1:" + convert(range1));
                    range2=Math.round(Date.now()/1000); console.log("zakres2:" + convert(range2));
                }
                if (this.value=='miesiac') {
                    range1=Math.round((Date.now() - 2629743830)/1000); console.log("zakres1:" + convert(range1));
                    range2=Math.round(Date.now()/1000); console.log("zakres2:" + convert(range2));
                }
                if (this.value=='kwartal') {
                    range1=Math.round((Date.now() - 7889231490)/1000); console.log("zakres1:" + convert(range1));
                    range2=Math.round(Date.now()/1000); console.log("zakres2:" + convert(range2));
                }
                if (this.value=='rok') {
                    range1=Math.round((Date.now() - 31536000000)/1000); console.log("zakres1:" + convert(range1));
                    range2=Math.round(Date.now()/1000); console.log("zakres2:" + convert(range2));
                }
            });
        });

        $(document).ready(function() {
            $('input[type=radio][name=group]').change(function() {
                if (this.value=='1') {
                    group=300; console.log("okres grupowania:" + group);
                }
                if (this.value=='2') {
                    group=1800; console.log("okres grupowania:" + group);
                }
                if (this.value=='3') {
                    group=10800; console.log("okres grupowania:" + group);
                }
                if (this.value=='4') {
                    group=86400; console.log("okres grupowania:" + group);
                }
                if (this.value=='5') {
                    group=604800; console.log("okres grupowania:" + group);
                }
                if (this.value=='6') {
                    group=2592000; console.log("okres grupowania:" + group);
                }
                if (this.value=='7') {
                    group=31536000; console.log("okres grupowania:" + group);
                }


            });
        });

        $(document).ready(function() {
            $('input[type=radio][name=type]').change(function() {
                if (this.value=='1') {
                    type="Line"; console.log("Typ wykresu:"+type);
                }
                if (this.value=='2') {
                    type="Bar"; console.log("Typ wykresu:"+type);
                }

            });
        });

        $(document).ready(function() {
            $('input[type=radio][name=frequency]').change(function() {
                if (this.value=='1') {
                    frequency=2400; console.log("Czestotliwosc:"+frequency);
                }
                if (this.value=='2') {
                    frequency = 5000;
                    console.log("Czestotliwosc:" + frequency);
                }
            });
        });


        var gnr=document.getElementById("generate");
        gnr.addEventListener("click", function(){
            generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,'TEST',frequency,type);
        }
        );
        generateChart(mycanvas,  <c:out value="${device.id}"/>,doba,teraz,300,'Wykres dobowy 2400 SWIEZY',2400);
        var btn1=document.getElementById("onclick1");
        btn1.addEventListener("click", function(){generateChart(mycanvas,  <c:out value="${device.id}"/>,doba,teraz,300,'Wykres ' +
            'dzienny pasmo 2,4GHz grupowanie:5min',2400);});
        function convert(time) {
            time=time*1000;
            var temp = new Date(time);
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
        console.log("teraz:" + teraz);
        console.log("doba:" + doba);
        console.log("tydzien:" + tydzien);
        console.log("miesiac:" + miesiac);
        console.log("kwartal:" + kwartal);
        console.log("rok:" + rok);
        function generateChart(mycanvas, id,timestamp,timestamp2,range,etykieta,frequency,type) {
            var request = new XMLHttpRequest();
            $('#mycanvas').remove();
            $('#wykresy').append('<canvas id="mycanvas" width="3000px" height="500px" style="border:0 solid #bce8f1;"></canvas>');
            mycanvas = document.querySelector('#mycanvas');
            var tags = [];      //WSZYSTKO
            var values_avg = [];    //WSZYSTKO
            var values_min = [];    //WSZYSTKO
            var values_max = [];    //WSZYSTKO
            var options = {
                tooltips: {mode: 'index'},
                legend: {display: true},
                title: {display: true, text: etykieta},
                hover: {intersect: false,
                    mode:'x'

                },
                label: {display: true},
                scales: {
                    xAxes: [{
                        display:true,
                        barPercentage:1,
                        autoSkip: false,
                        ticks: {
                            maxRotation: 70 // angle in degrees
                        }

                    }],

                    yAxes: [{
                        stacked: false,
                        display: true,
                        scaleLabel: {
                            display: true,
                            labelString: 'Ilosc klientów'
                        }
                        ,
                        ticks: {
                            ticks: {
                                autoSkip: false,
                                fixedStepSize: 10,
                                beginAtZero:true,
                                Min: -5,
                                suggestedMax: 100
                            }
                        }}]
                },
                responsive: false,
                steppedLine: true,
                elements: {line: {tension: 0}}
            };

            var ctx = mycanvas.getContext("2d");
            var gradient = ctx.createLinearGradient(300, 0, 300, 600);
            gradient.addColorStop(0, 'black');
            gradient.addColorStop(0.25, 'red');
            gradient.addColorStop(0.5, 'orange');
            gradient.addColorStop(0.75, 'yellow');
            gradient.addColorStop(1, 'green');

            var data = {
                showLine:false,
                labels: tags,
                datasets: [{
                    label: "Minimalna ilosc",
                    data: values_min,
                    fill:false,
                    borderColor:"rgba(255,0,0,1)",
                    backgroundColor: "rgba(255,0,0,1)",
                    borderWidth: 0,
                    pointWidth:0,
                    pointBorderWidth:0,
                    hoverBackgroundColor:"rgba(0,0,0,1)"

                },{
                    label: "Srednia ilosc",
                    data: values_avg,
                    fill: false,
                    borderColor:"rgba(255,155,0,1)",
                    backgroundColor: "rgba(255,200,0,1)",
                    borderWidth: 0,
                    hoverBackgroundColor:"rgba(0,0,0,1)"

                },{
                    label: "Maksymalna ilosc",
                    data: values_max,
                    fill: false,
                    borderColor: "rgba(8, 95, 41,1)",
                    backgroundColor: "rgba(8, 139, 41,1)",
                    hoverBackgroundColor:"rgba(8, 139, 41,1)"
                }

                ]
            };
            var request = new XMLHttpRequest();
            request.open('Get', '/api/surveys/avg-min-max?device='+id+
                '&frequency='+frequency+
                '&start='+timestamp+
                '&groupTime='+range+
                '&end='+timestamp2
            );
            var min_tmp,avg_tmp,max_tmp,tags_tmp;
            request.onload = function () {
                var jsondata = JSON.parse(request.responseText);
                var ilosc=Object.keys(jsondata.list).length;
                console.log("ILOSC:"+ilosc);
                console.log("Czas wczytania:"+jsondata.queryTime);
                //console.log(Math.round(jsondata.list[1].average)); TESTOWO
                //console.log(Math.round(jsondata.list[0].min)); TESTOWO
                console.log(JSON.stringify(jsondata));

                for (i = 0; i < ilosc; i++) {
                    avg_tmp=jsondata.list[i].average;
                    min_tmp=jsondata.list[i].min;
                    max_tmp=jsondata.list[i].max;
                    console.log(i+" AVERAGE:"+avg_tmp);
                    values_avg.push(Math.round(avg_tmp));
                    console.log(i+" MIN:"+min_tmp);
                    values_min.push(min_tmp);
                    console.log(i+" MAX:"+max_tmp);
                    values_max.push(max_tmp);
                    console.log(convert(Number(jsondata.list[i].timeStart)));
                    tags.push(convert(Number(jsondata.list[i].timeStart)));
                    console.log("______________________________");
                }

                    var myFirstChart = Chart.Line(mycanvas, {data: data, options: options});

            };
            request.send();
            console.log(doba);
        }
    </script>
    <script src="/js/jquery-3.1.1.min.js"></script>
    <script src="/js/bootstrap-3.3.7.min.js"></script>

</body>
</html>

<%-- <div class="panel panel-default">
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
 --%>