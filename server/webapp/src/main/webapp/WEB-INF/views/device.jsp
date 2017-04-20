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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.bundle.js" type="text/javascript"></script>
    <script
            src="https://code.jquery.com/jquery-3.2.0.min.js"
            integrity="sha256-JAW99MJVpJBGcbzEuXk4Az05s/XyDdBomFqNlM3ic+I="
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/css/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="/css/progress.css"><link href='https://fonts.googleapis.com/css?family=Lato|Josefin+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
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
    <div id="control">
        <div id="control1"> Zakres wyswietlanych danych
            <form action="" class="forma">
                <input type="radio" class="rad" name="range" value="dzien"> Dzien </br>
                <input type="radio" class="rad" name="range" value="tydzien"> Tydzien</br>
                <input type="radio" class="rad" name="range" value="miesiac"> Miesiac</br>
                <input type="radio" class="rad" name="range" value="rok"> Rok</br>
                <input type="radio" class="rad" name="range" value="custom"> Ustawienia niestandardowe</br>
            </form></br>
        </div>
        <div id="control2"> Ustawienia niestandardowe
            <form action="" class="forma">
                <div class='input-group date' id='datetimepicker1' width="100px">
                    <input id="time_start" type='text'  data-date-end-date="0d" class="form-control"> Od
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
                </br>
                <div class='input-group date' id='datetimepicker2' width="100px">
                    <input id="time_end" type='text'  data-date-end-date="0d" class="form-control"> Do
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </form>
            </br>
                <form name="testform">
                    <select name="range" id="range_picker" size="1">
                        <option value=1>Badania oryginalne</option>
                        <option selected value=2>Grupowanie sugerowane</option>
                        <option value=3>5 minut</option>
                        <option value=4>15 minut</option>
                        <option value=5>godzina</option>
                        <option value=6>3 godziny</option>
                        <option value=7>dzień</option>
                        <option value=8>tydzień</option>
                        <option value=9>miesiąc</option>
                        <option value=10>rok</option>
                    </select>
                </br>
                    <label for="range_picker">Zakres grupowania badań</label>
                    </br></br>
                </form>
            </br>
                <button type="button" id="apply">Zatwierdz zmiany</button>
            </form>
        </div>
        <div id="control3"> Określenie badań z danej częstotliwości
            <form action="" class="forma">
                <input type="radio" class="rad" name="frequency" value="1"> Częstotliwość 2,4Ghz</br>
                <input type="radio" class="rad" name="frequency" value="2"> Częstotliwość 5Ghz</br>
            </form>
        </div>
        <div id="control4"> Rodzaj wykresu
            <form action="" class="forma">
                <input type="radio" class="rad" name="responsive" value="1"> Wykres responsywny</br>
                <input type="radio" class="rad" name="responsive" value="2"> Wykres z suwakiem</br>
            </form></br>
        </div>
        <div id="control5"> Rozmiary wykresu
            <form action="" class="forma">
                <input type="text" width="20" id="chartSize1" value="1000"> Szerokość wykresu (w px)
                </br>
                <input type="text" mwidth="20" id="chartSize2" value="500"> Wysokość wykresu (w px)
            </form></br>
        </div>
    </div>
    <button type="button" id="generate">Odswież wykres</button>
    <script src="/js/jquery-3.1.1.min.js"></script>
    <script src="/js/bootstrap-3.3.7.min.js"></script>
    <script src="/js/moment-with-locales.min.js"></script>
    <script src="/js/bootstrap-datetimepicker.min.js"></script>
    <script src="/js/progress.js"></script>
    <br/>
    <div id="wykresy">
    <canvas id="mycanvas" width="1000px" height="100px"></canvas>
    </div></br>
</div>



<style>
    .input-group{max-width: 200px;}
    #control{  display: flex; justify-content: space-between;  padding: 20px;  }
    #control1{display:flex;flex-direction: column;}
    #control2{display:flex;flex-direction: column;}
    #control3{display:flex;flex-direction: column;}
    #control4{display:flex;flex-direction: column;}
    .forma{ margin-bottom:5px; padding:10px;margin-top: -1px;
        vertical-align: middle;}

    #wykresy {max-width: 100%;max-height:80%;  }
</style>

<script>
    $(function () {$('#datetimepicker1').datetimepicker({format: 'DD-MM-YYYY HH:mm:ss'});});
    $(function () {$('#datetimepicker2').datetimepicker({format: 'DD-MM-YYYY HH:mm:ss'});});

    $('#control5').hide();
    $('#control2').hide();
    var now = Date.now();
    var teraz=Math.round(now/1000);
    var doba = Math.round((now - 86400000)/1000);       // dzien
    var tydzien = Math.round((now - 604800000)/1000);   // 7 dni
    var miesiac = Math.round((now - 2629743830)/1000); //30 dni
    var kwartal = Math.round((now - 7889231490)/1000); //90 dni
    var rok = Math.round((now - 31536000000)/1000);   // 365 dni
    var range1=doba;
    var range2=teraz;
    var ranged;
    var group=300;
    var frequency=2400;
    var etykietka="DEFAULT";
    var type="Line";
    var api;
    var respons="true";
    var range_picker;
    var timestamp1,timestamp2;

    $(document).ready(function() {
        $('input[type=radio][name=range]').change(function() {
            if (this.value=='dzien') {
                $('#range_picker select').val(0);
                $('#control2').hide();
                api="original"; console.log("Wartosc api:"+api);
                group=300; console.log("okres grupowania:" + group);
                etykietka="Wykres dzienny - badania oryginalne";
                range1=Math.round((Date.now() - 86400000)/1000); console.log("zakres1:" + convert(range1));
                range2=Math.round(Date.now()/1000); console.log("zakres2:" + convert(range2));
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type,respons);
                console.log("_____________________________________________________________");
            }
            if (this.value=='tydzien') {
                $('#range_picker select').val(0);
                $('#control2').hide();
                api="group";console.log("Wartosc api:"+api);
                group=10800; console.log("okres grupowania:" + group);
                etykietka="Wykres tygodniowy - grupowanie:3 godziny";
                range1=Math.round((Date.now() - 604800000)/1000); console.log("zakres1:" + convert(range1));
                range2=Math.round(Date.now()/1000); console.log("zakres2:" + convert(range2));
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type,respons);
                console.log("_____________________________________________________________");
            }
            if (this.value=='miesiac') {
                $('#range_picker select').val(0);
                $('#control2').hide();
                api="group";console.log("Wartosc api:"+api);
                group=86400; console.log("okres grupowania:" + group);
                etykietka="Wykres miesięczny - grupowanie:1 dzien";
                range1=Math.round((Date.now() - 2629743830)/1000); console.log("zakres1:" + convert(range1));
                range2=Math.round(Date.now()/1000); console.log("zakres2:" + convert(range2));
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type,respons);
                console.log("_____________________________________________________________");
            }
            if (this.value=='rok') {
                $('#range_picker select').val(0);
                $('#control2').hide();
                api="group";console.log("Wartosc api:"+api);
                group=2592000; console.log("okres grupowania:" + group);
                etykietka="Wykres roczny - grupowanie:1 miesiac";
                range1=Math.round((Date.now() - 31536000000)/1000); console.log("zakres1:" + convert(range1));
                range2=Math.round(Date.now()/1000); console.log("zakres2:" + convert(range2));
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type,respons);
                console.log("_____________________________________________________________");
            }
            if (this.value=='custom') {
                $('#control2').show();
            }
        });
    });


    $(document).ready(function() {
        $('input[type=radio][name=frequency]').change(function() {
            if (this.value=='1') {
                frequency=2400; console.log("Czestotliwosc:"+frequency);
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type,respons);
            }
            if (this.value=='2') {
                frequency = 5000;
                console.log("Czestotliwosc:" + frequency);
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type,respons);
            }
        });
    });

    $(document).ready(function() {
        $('input[type=radio][name=responsive]').change(function() {
            if (this.value=='1') {
                respons=true; console.log("Responsywny:"+respons);
                $('#wykresy').css('overflow', 'visible');
                $('#control5').hide();
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type,respons);
            }
            if (this.value=='2') {
                respons=false;
                console.log("Responsywny:" + respons);
                $('#wykresy').css('overflow', 'scroll');
                $('#control5').show();
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type,respons);
            }
        });
    });

    var gnr=document.getElementById("generate");
    gnr.addEventListener("click", function(){
            generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type,respons);
        }
    );

    $(document).ready(function() {
        $(window).keydown(function(event){
            if(event.keyCode == 13) {
                event.preventDefault();
                return false;
            }
        });
    });


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


var temp;
    $('#apply').click(
        function () {
            tmp1=Math.round(Number($('#datetimepicker1').data('DateTimePicker').date())/1000);
            tmp2=Math.round(Number($('#datetimepicker2').data('DateTimePicker').date())/1000);
            ranged=Number($('#zakres').val());
            console.log("data1 z kalendarza:"+tmp1);
            console.log("data2 z kalendarza:"+tmp2);
            temp=Number(Math.round((tmp2-tmp1)/50));
            etykietka=("Zakres od \n"+convert(tmp1)+"do \n"+convert(tmp2));
            if (tmp1>=tmp2){alert("Wybrano niepoprawne parametry!!!!");}
            else{
                generateChart(mycanvas, <c:out value="${device.id}"/>,tmp1,tmp2,temp,etykietka,frequency,type,respons);
            }

        }
    );

    function generateChart(mycanvas, id,timestamp,timestamp2,range,etykieta,frequency,type) {
        var temper=$('#range_picker').val();
        console.log("TEMPER="+temper);
        if (temper==1){api="original"};
        if (temper==2){api="group";range=(range2-range1)/100;}
        if (temper==3){api="group";range=300;}
        if (temper==4){api="group";range=1500;}
        if (temper==5){api="group";range=3600;}
        if (temper==6){api="group";range=10800;}
        if (temper==7){api="group";range=86400;}
        if (temper==8){api="group";range=604800;}
        if (temper==9){api="group";range=31536000;}
        temper=0;
        $('#range_picker select').val(0);
        console.log("TEMPER="+ $('#range_picker select').val());
        console.log("RANGE:"+range);
        var request = new XMLHttpRequest();
        var szerokosctmp=Number($('#chartSize1').val())+"px";
        var wysokosctmp=Number($('#chartSize2').val())+"px";
        console.log("WYSOKOSC:"+wysokosctmp);
        console.log("SZEROKOSC:"+szerokosctmp);
            $('#mycanvas').remove();
            $('#wykresy').append('<canvas id="mycanvas"</canvas>');
            mycanvas=$('#mycanvas');
            $('#mycanvas').css({'width':szerokosctmp,'height':wysokosctmp});
            $('#mycanvas').css({'max-width':szerokosctmp,'max-height':wysokosctmp});

        mycanvas = document.querySelector('#mycanvas');
        var tags = [];      //WSZYSTKO
        var values_avg = [];    //WSZYSTKO
        var values_min = [];    //WSZYSTKO
        var values_max = [];    //WSZYSTKO
        var options = {tooltips: {mode: 'index'}, legend: {display: true},
            title: {display: true, text: etykieta},
            hover: {intersect: false, mode:'x'},
            label: {display: true},
            scales: {xAxes: [{display:true,barPercentage:1, autoSkip: false, ticks: {maxRotation: 30}}],
                yAxes: [{stacked: false, display: true, scaleLabel: {display: true, labelString: 'Ilosc klientów'},
                    ticks: {ticks: {autoSkip: false, fixedStepSize: 10, beginAtZero:true, Min: -5, suggestedMax: 100}}}]},
            maintainAspectRatio:false, responsive: respons, steppedLine: true, elements: {line: {tension: 0}}
        };
        var data2={
            showLine:false,
            labels: tags,
            datasets: [{
                label: "Ilosc klientów",
                data: values_avg,
                fill: false,
                borderColor:"rgba(255,155,0,1)",
                backgroundColor: "rgba(255,200,0,1)",
                borderWidth: 0,
                hoverBackgroundColor:"rgba(0,0,0,1)"
            }]
        };

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

        if (api=="original"){
            var request2 = new XMLHttpRequest();
            request.open('Get', '/api/surveys/original?device='+id+
                '&frequency='+frequency+
                '&start='+timestamp+
                '&end='+timestamp2
            );
            var avg_tmp,tags_tmp;
            console.log("ZAKRES TIMESTAMPU:"+(range2-range1));
            request.onload = function () {
                var jsondata = JSON.parse(request.responseText);
                //console.log(JSON.stringify(jsondata));
                var ilosc=Object.keys(jsondata.list).length;
                    for (i = 0; i < ilosc; i++) {
                        avg_tmp = jsondata.list[i].clients;
                        values_avg.push(Math.round(avg_tmp));
                        tags.push(convert(Number(jsondata.list[i].timestamp)));
                    }

                var myFirstChart = Chart.Line(mycanvas, {data: data2, options: options});
            };
        }
        else{
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
                //console.log(JSON.stringify(jsondata));
                var ilosc=Object.keys(jsondata.list).length;
                    for (i = 0; i < ilosc; i++) {
                        avg_tmp = jsondata.list[i].average;
                        min_tmp = jsondata.list[i].min;
                        max_tmp = jsondata.list[i].max;
                        values_avg.push(Math.round(avg_tmp));
                        values_min.push(min_tmp);
                        values_max.push(max_tmp);
                        tags.push(convert(Number(jsondata.list[i].timeStart)));
                        <%--console.log(i+" AVERAGE:"+avg_tmp);
                    console.log(i+" MIN:"+min_tmp);
                    console.log(i+" MAX:"+max_tmp);
                    console.log(convert(Number(jsondata.list[i].timeStart)));
                    console.log("______________________________");--%>

                }
                var myFirstChart = Chart.Line(mycanvas, {data: data, options: options});
            };

        }
        request.send();


    }
</script>


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