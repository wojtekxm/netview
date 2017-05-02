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
    <script src="/js/Chart.bundle.js" type="text/javascript"></script>
    <script src="/js/jquery-3.1.1.min.js" type="text/javascript"></script>
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
                <input type="radio" class="rad" id="default_chart" name="range" value="dzien" > Dzien </br>
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
                </form>
            </form>
        </div>
        <div id="control3"> Określenie badań z danej częstotliwości
            <form action="" class="forma">
                <input type="radio" class="rad" id="2400" name="frequency" value="1"> Częstotliwość 2,4Ghz</br>
                <input type="radio" class="rad" id="5000" name="frequency" value="2"> Częstotliwość 5Ghz</br>
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
    <button type="button" id="apply">Zatwierdz zmiany</button>
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
    .forma{ margin-bottom:5px; padding:10px;margin-top: -1px;  vertical-align: middle;}
    #wykresy {max-width: 100%;max-height:80%;  }
</style>

<script>
    $('#apply').hide();
    $(function () {$('#datetimepicker1').datetimepicker({format: 'DD-MM-YYYY HH:mm:ss'});});
    $(function () {$('#datetimepicker2').datetimepicker({format: 'DD-MM-YYYY HH:mm:ss'});});
    $('#default_chart').prop("checked", true);
    $('#control5').hide();
    $('#control2').hide();
    var id=<c:out value="${device.id}"/>;
    var range1=Math.round((Date.now() - 86400000)/1000);
    var range2=Math.round(Date.now()/1000);
    var ranged;
    var group=300;
    var frequency;
    var etykietka="Domyślny wykres dniowy";
    var type="original";
    var respons="true";
    var range_picker;

 // SPRAWDZANIE OBSLUGIWANYCH PASM CZESTOTLIWOSCI
        var request = new XMLHttpRequest();
        request.open('Get', '/api/device?id='+id);
        request.onload = function () {
            var jsondata = JSON.parse(request.responseText);
            if (jsondata.content.frequencySurvey['2400']==undefined &&
                jsondata.content.frequencySurvey['5000']!=undefined){console.log("TYLKO 5ghz");
                frequency=5000;$('#control3').hide();
                alert("Dostępne badania tylko dla częstotliwości 5ghz!");
            }

            if (jsondata.content.frequencySurvey['2400']!=undefined &&
                jsondata.content.frequencySurvey['5000']==undefined){console.log("TYLKO 2,4ghz");
                frequency=2400;$('#control3').hide();
                alert("Dostępne badania tylko dla częstotliwości 2,4ghz!");
            }

            if (jsondata.content.frequencySurvey['2400']!=undefined &&
                jsondata.content.frequencySurvey['5000']!=undefined){console.log("OBIE");
                frequency=2400;
                alert("Dostępne badania dla obu częstotliwości. Domyślna częstotliwość=2,4Ghz");
            }
            if (jsondata.content.frequencySurvey['2400']==undefined &&
                jsondata.content.frequencySurvey['5000']==undefined){console.log("Zadna");
                frequency=undefined;$
                ('#control1').hide();
                ('#control2').hide();
                ('#control3').hide();
                ('#control4').hide();
                ('#control5').hide();
                alert("BRAK BADAN!");
            }

        };
        request.send();

    setTimeout(function () {
        generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type);
    },1000);
    <%-------------------------------------------------------------------------------------------------------------------------------%>
    $(document).ready(function() {
        $('input[type=radio][name=range]').change(function() {
            if (this.value=='dzien') {
                $('#range_picker select').val(0);
                $('#control2').hide();
                $('#generate').show();
                $('#apply').hide();
                type="original"; console.log("Wartosc type:"+type);
                group=300; console.log("okres grupowania:" + group);
                etykietka="Wykres dzienny - badania oryginalne";
                range1=Math.round((Date.now() - 86400000)/1000); console.log("zakres1:" + convert(range1));
                range2=Math.round(Date.now()/1000); console.log("zakres2:" + convert(range2));
                type="original";
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type);
            }
            if (this.value=='tydzien') {
                $('#range_picker select').val(0);
                $('#control2').hide();
                $('#generate').show();
                $('#apply').hide();
                type="group";console.log("Wartosc type:"+type);
                group=10800; console.log("okres grupowania:" + group);
                etykietka="Wykres tygodniowy - grupowanie:3 godziny";
                range1=Math.round((Date.now() - 604800000)/1000); console.log("zakres1:" + convert(range1));
                range2=Math.round(Date.now()/1000); console.log("zakres2:" + convert(range2));
                type="group";
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type);
            }
            if (this.value=='miesiac') {
                $('#range_picker select').val(0);
                $('#control2').hide();
                $('#generate').show();
                $('#apply').hide();
                type="group";console.log("Wartosc type:"+type);
                group=86400; console.log("okres grupowania:" + group);
                etykietka="Wykres miesięczny - grupowanie:1 dzien";
                range1=Math.round((Date.now() - 2629743830)/1000); console.log("zakres1:" + convert(range1));
                range2=Math.round(Date.now()/1000); console.log("zakres2:" + convert(range2));
                type="group";
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type);
            }
            if (this.value=='rok') {
                $('#range_picker select').val(0);
                $('#control2').hide();
                $('#generate').show();
                $('#apply').hide();
                type="group";console.log("Wartosc type:"+type);
                group=2592000; console.log("okres grupowania:" + group);
                etykietka="Wykres roczny - grupowanie:1 miesiac";
                range1=Math.round((Date.now() - 31536000000)/1000); console.log("zakres1:" + convert(range1));
                range2=Math.round(Date.now()/1000); console.log("zakres2:" + convert(range2));
                type="group";
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type);
            }
            if (this.value=='custom') {
                $('#control2').show();
                $('#generate').hide();
                $('#apply').show();
            }
        });
    });

    <%-------------------------------------------------------------------------------------------------------------------------------%>
    $(document).ready(function() {
        $('input[type=radio][name=frequency]').change(function() {
            if (this.value=='1') {
                frequency=2400; console.log("Czestotliwosc:"+frequency);
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type);
            }
            if (this.value=='2') {
                frequency = 5000;
                console.log("Czestotliwosc:" + frequency);
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type);
            }
        });
    });
    <%-------------------------------------------------------------------------------------------------------------------------------%>
    $(document).ready(function() {
        $('input[type=radio][name=responsive]').change(function() {
            if (this.value=='1') {
                respons=true; console.log("Responsywny:"+respons);
                $('#wykresy').css('overflow', 'visible');
                $('#control5').hide();
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type);
            }
            if (this.value=='2') {
                respons=false;
                console.log("Responsywny:" + respons);
                $('#wykresy').css('overflow', 'scroll');
                $('#control5').show();
                generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type);
            }
        });
    });
    <%-------------------------------------------------------------------------------------------------------------------------------%>
    var gnr=document.getElementById("generate");
    gnr.addEventListener("click", function(){
        generateChart(mycanvas, <c:out value="${device.id}"/>,range1,range2,group,etykietka,frequency,type);
    });

    <%-------------------------------------------------------------------------------------------------------------------------------%>
    $(document).ready(function() {
        $(window).keydown(function(event){
            if(event.keyCode == 13) {event.preventDefault();return false;}});
    });

    <%-------------------------------------------------------------------------------------------------------------------------------%>
    function convert(time) {
        var DayName = ["niedziela", "poniedziałek", "wtorek", "sroda", "czwartek", "piątek", "sobota"];
        var MonthName = ["stycznia ", "lutego ", "marca ", "kwietnia ", "maja ", "czerwca ",
            "lipca ", "sierpnia ", "września ", "października ", "listopada ", "grudnia "];
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

    <%-------------------------------------------------------------------------------------------------------------------------------%>
    $('#apply').click(
        function () {
            tmp1=Math.round(Number($('#datetimepicker1').data('DateTimePicker').date())/1000);
            tmp2=Math.round(Number($('#datetimepicker2').data('DateTimePicker').date())/1000);
            ranged=Number($('#zakres').val());
            var temper=$('#range_picker').val();
            console.log("TEMPER="+temper);
            $('#range_picker select').val(0);
            if (temper==1){type="original"; range=0;}
            if (temper==2){type="group";range=Math.round((tmp2-tmp1)/100); console.log("zakres grupowania:"+range);}
            if (temper==3){type="group";range=300;}
            if (temper==4){type="group";range=1500;}
            if (temper==5){type="group";range=3600;}
            if (temper==6){type="group";range=10800;}
            if (temper==7){type="group";range=86400;}
            if (temper==8){type="group";range=604800;}
            if (temper==9){type="group";range=31536000;}
            etykietka=("Zakres od \n"+convert(tmp1)+"\n"+"do "+convert(tmp2));
            if (tmp1>=tmp2){alert("Wybrano niepoprawne parametry!!!!");}
            else{
                if (temper==1){
                    generateChart(mycanvas, <c:out value="${device.id}"/>,tmp1,tmp2,range,etykietka,frequency,'original',respons);
                }
                else{
                    generateChart(mycanvas, <c:out value="${device.id}"/>,tmp1,tmp2,range,etykietka,frequency,'group',respons);
                }
            }
        }
    );
<%-------------------------------------------------------------------------------------------------------------------------------%>
    function generateChart(mycanvas, id,timestamp,timestamp2,range,etykieta,frequency,type) {
        var tags = [];      //WSZYSTKO
        var values_avg = [];    //WSZYSTKO
        var values_min = [];    //WSZYSTKO
        var values_max = [];    //WSZYSTKO
        var request = new XMLHttpRequest();
        var szerokosctmp=Number($('#chartSize1').val())+"px";
        var wysokosctmp=Number($('#chartSize2').val())+"px";
            $('#mycanvas').remove();
            $('#wykresy').append('<canvas id="mycanvas"</canvas>');
            mycanvas=$('#mycanvas');
            $('#mycanvas').css({'width':szerokosctmp,'height':wysokosctmp});
            $('#mycanvas').css({'max-width':szerokosctmp,'max-height':wysokosctmp});
        mycanvas = document.querySelector('#mycanvas');

        var options = {tooltips: {mode: 'index'}, legend: {display: true},
            title: {display: true, text: etykieta},
            hover: {intersect: false, mode:'x'},
            label: {display: true},
            scales: {xAxes: [{display:true,barPercentage:1, autoSkip: true, ticks: {maxRotation: 0,maxTicksLimit: 2}}],
                yAxes: [{stacked: false, display: true, scaleLabel: {display: true, labelString: 'Ilosc klientów'},
                    ticks: {ticks: {autoSkip: false, fixedStepSize: 10, beginAtZero:true, Min: -5, suggestedMax: 100}}}]},
            maintainAspectRatio:false, responsive: respons, steppedLine: true, elements: {line: {tension: 0}}
        };
        var data2={showLine:false, labels: tags,
            datasets: [{label: "Ilosc klientów", data: values_avg, fill: false,
                borderColor:"rgba(255,155,0,1)",
                backgroundColor: "rgba(255,200,0,1)",
                pointBorderWidth: 1,
                hoverBackgroundColor:"rgba(0,0,0,1)"}]};
        var data = {
            showLine:false,
            labels: tags,
            datasets: [{
                label: "Minimalna ilosc",
                data: values_min,
                fill:false,
                borderColor:"rgba(255,0,0,1)",
                backgroundColor: "rgba(255,0,0,1)",
                hoverBackgroundColor:"rgba(0,0,0,1)"

            },{
                label: "Srednia ilosc",
                data: values_avg,
                fill: false,
                borderColor:"rgba(255,155,0,1)",
                backgroundColor: "rgba(255,200,0,1)",
                hoverBackgroundColor:"rgba(0,0,0,1)"

            },{
                label: "Maksymalna ilosc",
                data: values_max,
                fill: false,
                borderColor: "rgba(8, 95, 41,1)",
                backgroundColor: "rgba(8, 139, 41,1)",
                hoverBackgroundColor:"rgba(8, 139, 41,1)"}]};

        if (type=="original"){
            request.open('Get', '/api/surveys/original?device='+id+
                '&frequency='+frequency+
                '&start='+timestamp+
                '&end='+timestamp2
            );
            request.onload = function () {
                var jsondata = JSON.parse(request.responseText);
                //console.log(JSON.stringify(jsondata));
                var ilosc=Object.keys(jsondata.list).length;
                console.log("ilosc badan:"+ilosc);
                    for (i = 0; i < ilosc; i++) {
                        values_avg.push(Math.round(jsondata.list[i].clients));
                        tags.push(convert(Number(jsondata.list[i].timestamp)));
                    }
                var myFirstChart = Chart.Line(mycanvas, {data: data2, options: options});
            };
        }
        else{

            request.open('Get', '/api/surveys/avg-min-max?device='+id+
                '&frequency='+frequency+
                '&start='+timestamp+
                '&groupTime='+range+
                '&end='+timestamp2
            );
            request.onload = function () {
                var jsondata = JSON.parse(request.responseText);
                //console.log(JSON.stringify(jsondata));
                var ilosc=Object.keys(jsondata.list).length;
                console.log("ilosc badan:"+ilosc);
                    for (i = 0; i < ilosc; i++) {
                        values_avg.push(Math.round(jsondata.list[i].average));
                        values_min.push(jsondata.list[i].min);
                        values_max.push(jsondata.list[i].max);
                        tags.push(convert(Number(jsondata.list[i].timeStart)));
                        console.log("_____________________");
                }
                var myFirstChart = Chart.Line(mycanvas, {data: data, options: options});
            };}
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