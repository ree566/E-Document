<%-- 
    Document   : drag
    Created on : 2016/4/19, 上午 11:55:47
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>5F狀態平面圖 - ${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="images/favicon.ico"/>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <style>
            .draggable { 
                width: 25px; 
                height: 25px; 
                padding: 0.5em; 
                float: left;
                /*background-color: red;*/
                margin: 0px;
                cursor: default;
                text-align: center;
            }
            img{
                border:2px green solid;
            }
            #generateArea{
                height: 20px;
            }
            .alarm{
                background-color: #0066FF;
                /*color: white;*/
            }
            .normal{
                background-color: greenyellow;
                /*color: white;*/
            }
            .abnormal{
                background-color: yellow;
            }
            .offLine{
                background-color: white;
            }
            #goback{
                cursor: pointer;
                color: blue;
            }
            .lineTitle{
                padding: 0 auto; 
                width: 40px; 
                height: 40px; 
                background-color: white;
                font-size: 32px;
                float: left;
                border:5px green solid;
                cursor: default;
            }
            .clearWiget{
                clear: both;
            }
            #mapGroup{
                width: 1200px;
                height: 500px;
                background-image: url(images/totalMap_5f_1.png);
                background-repeat: no-repeat;
                -o-background-size: 100% 100%, auto;
                -moz-background-size: 100% 100%, auto;
                -webkit-background-size: 100% 100%, auto;
                background-size: 100% 100%, auto;
                background-position:center center;
                border:5px red solid;
            }
            /*            body {
                            padding-top: 70px;
                             Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. 
                        }*/
            #wigetCtrl{
            }
            .modal.fade.ui-draggable-dragging {
                -moz-transition: none;
                -o-transition: none;
                -webkit-transition: none;
                transition: none;
            }
            #wigetInfo{
                border-bottom: 5px red solid;
                border-right: 5px red solid;
                background-color: white;
                width: 25%; 
                overflow: hidden;
                padding:0 auto;
            }
            #titleArea>div, #testArea>div, #babArea>div{
                position: absolute;
            }
            .divCustomBg{
                background-size: 100% 100%, auto;
                background-repeat: no-repeat;
            }
            .ui-helper {
                /*width: 100% !important;*/
                float: left;
            }
            .blub-empty{
                background-image: url(images/blub-icon/Gray_Light_Icon.png);
                /*background-color: red;*/
            }
            .blub-normal{
                background-image: url(images/blub-icon/Green_Light_Icon.png);
            }
            .blub-alarm{
                background-image: url(images/blub-icon/Blue_Light_Icon.png);
            }
            .blub-abnormal{
                background-image: url(images/blub-icon/Yellow_Light_Icon.png);
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="js/reconnecting-websocket.min.js"></script>
        <script src="js/jquery.fullscreen-min.js"></script>
        <script>
            var sitefloor = 5;

            $(function () {

                var pXa = 0;
                var pYa = 350;

                var titleGroup = [
                    {lineName: "L1", x: 1090, y: -100},
                    {lineName: "LA", x: 1090, y: -200},
                    {lineName: "LB", x: 1090, y: -260},
                    {lineName: "LH", x: 380, y: -200},
                    {lineName: "LG", x: 380, y: -260},
                    {lineName: "LF", x: 380, y: -320}
                ];

                var testGroup = [
                    {people: 4, x: 600, y: -320}, // group 21-24
                    {people: 5, x: 600, y: -250}, // group 16-20
                    {people: 7, x: 530, y: -200}, // group 9-15
                    {people: 4, x: 630, y: -120}, // group 5-8
                    {people: 4, x: 630, y: -80} // group 1-4
                ];

                var babGroup = [
                    {people: 4, x: 930, y: -100, lineName: "L1"}, // group 1-4
                    {people: 4, x: 930, y: -200, lineName: "LA"}, // group 21-24
                    {people: 4, x: 930, y: -260, lineName: "LB"}, // group 16-20
                    {people: 3, x: 250, y: -190, lineName: "LF"}, // group 9-15
                    {people: 3, x: 250, y: -240, lineName: "LG"}, // group 5-8
                    {people: 3, x: 250, y: -300, lineName: "LH"} // group 1-4
                ];

                for (var i = 0; i < titleGroup.length; i++) {
                    var groupStatus = titleGroup[i];
                    $("#titleArea").append("<div></div>");
                    $("#titleArea>div")
                            .eq(i)
                            .addClass("titleWiget")
                            .html(groupStatus.lineName)
                            .css({left: groupStatus.x + pXa, top: groupStatus.y + pYa});
                }

                for (var i = 0; i < testGroup.length; i++) {
                    $("#testArea").append("<div></div>");
                    var groupStatus = testGroup[i];
                    for (var j = 0, k = groupStatus.people; j < k; j++) {
                        $("#testArea>div")
                                .eq(i)
                                .append("<div></div>")
                                .addClass("testWiget")
                                .css({left: groupStatus.x + pXa, top: groupStatus.y + pYa});
                    }
                }

                for (var i = 0; i < babGroup.length; i++) {
                    $("#babArea").append("<div></div>");
                    var groupStatus = babGroup[i];
                    for (var j = 0, k = groupStatus.people; j < k; j++) {
                        $("#babArea>div")
                                .eq(i)
                                .append("<div></div>")
                                .addClass("babWiget")
                                .attr("id", groupStatus.lineName)
                                .css({left: groupStatus.x + pXa, top: groupStatus.y + pYa});
                    }
                }

//                $(".titleWiget, .testWiget, .babWiget").after("<div class='clearWiget'></div>");

                var testChildElement = $("#testArea>.testWiget div");
                var babChildElement = $("#babArea>.babWiget div");

                testObjectInit();
                babObjectInit();


                $("#titleArea>div").not(".clearWiget").addClass("lineTitle");

                var dragableWiget = $("#titleArea>div, #testArea>div, #babArea>div");
//                dragableWiget.after("<div class='clearWiget'></div>");

//                dragableWiget.not(".clearWiget").addClass("ui-helper").draggable({
//                    drag: function (e) {
////                        return false;
//                    }
//                });

                $('[data-toggle="tooltip"]').tooltip();

                $("#fullBtn").click(function () {
                    $("#wigetCtrl").fullScreen(true);
                });

                function initWiget(obj) {
                    obj.addClass("blub-empty").removeClass("blub-alarm blub-normal blub-abnormal").removeAttr("title");
                }

                function testObjectInit() {
                    var object = $("#testArea>.testWiget div");
                    var loopCount = object.length;
                    object.each(function () {
                        $(this).attr({"id": "draggable" + loopCount + "_" + sitefloor + "f", "data-toggle": "tooltip"})
                                .addClass("draggable blub-empty divCustomBg")
                                .html(loopCount);
                        loopCount--;
                    });
                }

                function babObjectInit() {
                    $(".babWiget").each(function () {
                        var lineName = $(this).attr("id");
                        var childAmount = $(this).children().length;
                        $(this).children().each(function () {
                            $(this).attr({"id": (lineName + "_" + childAmount), "data-toggle": "tooltip"})
                                    .addClass("draggable blub-empty divCustomBg")
                                    .html(childAmount);
                            childAmount--;
                        });
                    });
                }

                function testDataToWiget(testObject) {
                    initWiget(testChildElement);
                    if (testObject != null) {
                        var testData = testObject.data;
                        if (testData != null) {
                            for (var k = 0, l = testData.length; k < l; k++) {
                                var people = testData[k];
                                var alarmSignal = people.isalarm;
                                var signalClass;
                                switch (alarmSignal) {
                                    case 0:
                                        signalClass = "blub-normal";
                                        break;
                                    case 1:
                                        signalClass = "blub-alarm";
                                        break;
                                    case 2:
                                        signalClass = "blub-abnormal";
                                        break;
                                }
                                $(".testWiget #draggable" + people.table + "_" + people.sitefloor + "f")
                                        .removeClass("blub-empty")
                                        .addClass(signalClass)
                                        .attr("title", people.name);
                            }
                        }
                    }
                }

                function babDataToWiget(babObject) {
//                    console.log(babObject);
                    initWiget(babChildElement);
                    if (babObject != null) {
                        var babData = babObject.data;
                        if (babData != null) {
                            for (var k = 0, l = babData.length; k < l; k++) {
                                var people = babData[k];
                                $("#babArea #" + people.TagName + " #" + people.TagName + "_" + people.T_Num)
                                        .removeClass("blub-empty")
                                        .addClass((people.ismax ? "blub-alarm" : "blub-normal"))
                                        .attr("title", "Time:" + people.diff + "秒");
                            }
                        }
                    }
                }

                var hostname = window.location.host;//Get the host ipaddress to link to the server.
//                var hostname = "172.20.131.52:8080";
                //--------------websocket functions
                //websocket will reconnect by reconnecting-websocket.min.js when client or server is disconnect
                var ws = new ReconnectingWebSocket("ws://" + hostname + "/CalculatorWSApplication/echo2");
                ws.timeoutInterval = 3000;
                ws.onopen = function () {

                };

                //Get the server message and transform into table.
                ws.onmessage = function (message) {
                    var jsonArray = $.parseJSON(message.data);
                    if (jsonArray.length != 0) {
                        testDataToWiget(jsonArray[0]);
                        babDataToWiget(jsonArray[1]);
                    }
                };

                ws.onerror = function (event) {
                    console.log("error");
                    console.log(event.data);
                };

                //generate the unnormal close event hint
                ws.onclose = function (event) {
                    var reason;
                    if (event.code == 1000)
                        reason = "Normal closure, meaning that the purpose for which the connection was established has been fulfilled.";
                    else if (event.code == 1001)
                        reason = "An endpoint is \"going away\", such as a server going down or a browser having navigated away from a page.";
                    else if (event.code == 1002)
                        reason = "An endpoint is terminating the connection due to a protocol error";
                    else if (event.code == 1003)
                        reason = "An endpoint is terminating the connection because it has received a type of data it cannot accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary message).";
                    else if (event.code == 1004)
                        reason = "Reserved. The specific meaning might be defined in the future.";
                    else if (event.code == 1005)
                        reason = "No status code was actually present.";
                    else if (event.code == 1006)
                        reason = "The connection was closed abnormally, e.g., without sending or receiving a Close control frame";
                    else if (event.code == 1007)
                        reason = "An endpoint is terminating the connection because it has received data within a message that was not consistent with the type of the message (e.g., non-UTF-8 [http://tools.ietf.org/html/rfc3629] data within a text message).";
                    else if (event.code == 1008)
                        reason = "An endpoint is terminating the connection because it has received a message that \"violates its policy\". This reason is given either if there is no other sutible reason, or if there is a need to hide specific details about the policy.";
                    else if (event.code == 1009)
                        reason = "An endpoint is terminating the connection because it has received a message that is too big for it to process.";
                    else if (event.code == 1010) // Note that this status code is not used by the server, because it can fail the WebSocket handshake instead.
                        reason = "An endpoint (client) is terminating the connection because it has expected the server to negotiate one or more extension, but the server didn't return them in the response message of the WebSocket handshake. <br /> Specifically, the extensions that are needed are: " + event.reason;
                    else if (event.code == 1011)
                        reason = "A server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.";
                    else if (event.code == 1015)
                        reason = "The connection was closed due to a failure to perform a TLS handshake (e.g., the server certificate can't be verified).";
                    else
                        reason = "Unknown reason";
                    console.log("The connection was closed for reason: " + reason);
                };

                function postToServer() {
                }

                function closeConnect() {
                    ws.close();
                    console.log("websocket connection is now end");
                }
//-----------------------
            });
        </script>
    </head>
    <body style="cursor: auto;">
        <!--<button id="fullBtn">Full</button>-->
        <div id="wigetCtrl">
            <div id="mapGroup">
                <div id="wigetInfo">
                    <label for="empty" style="float:left">空</label>
                    <div class="draggable blub-empty divCustomBg"></div>

                    <label for="normalSign" style="float:left">正常</label>
                    <div class="draggable blub-normal divCustomBg"></div>

                    <label for="normalSign" style="float:left">警告</label>
                    <div class="draggable blub-alarm divCustomBg"></div>

                    <label for="normalSign" style="float:left">異常</label>
                    <div class="draggable blub-abnormal divCustomBg"></div>
                </div>
                <!--<div class="clearWiget" /></div>-->

                <div id="titleArea"></div>
                <!--<div class="clearWiget" /></div>-->

                <div id="testArea"></div>
                <!--<div class="clearWiget" /></div>-->

                <div id="babArea"></div>
                <!--<div class="clearWiget"></div>-->
            </div>
        </div>
        <hr />

        <!--若您對 CSS 語法還不太會使用，就把 <div> 上的 position:absolute; 拿掉就好了~
        
        
        若您想要使用 CSS 語法就必須使用兩層 <div>
        
        第一層 <div> 寫上 position:relative;
        
        第二層 <div> 寫上 position:absolute;-->
        <div class="clearWiget" />
    </body>
</html>
