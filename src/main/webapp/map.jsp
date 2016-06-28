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
        <title>${initParam.pageTitle}</title>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <style>
            .draggable { 
                width: 10px; 
                height: 10px; 
                padding: 0.5em; 
                float: left;
                background-color: white;
                border:2px green dashed;
                margin: 2px 2px;
                border-radius:99em;
                cursor: default;
            }
            img{
                border:2px green dashed;
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
                border:2px green dashed;
                cursor: default;
            }
            .clearWiget{
                clear: both;
            }
            #mapGroup{
                width: 1280px;
                height: 1024px;
                margin: 0px auto;
            }
/*            body {
                padding-top: 70px;
                 Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. 
            }*/
            #wigetCtrl{
                margin: 0px auto;
                width: 98%;
            }
            .modal.fade.ui-draggable-dragging {
                -moz-transition: none;
                -o-transition: none;
                -webkit-transition: none;
                transition: none;
            }
        </style>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="js/reconnecting-websocket.min.js"></script>
        <script>

            $(function () {

                var testGroup = [
                    {people: 4, x: 281, y: -569}, // group 21-24
                    {people: 5, x: 111, y: -489}, // group 16-20
                    {people: 7, x: -126, y: -441}, // group 9-15
                    {people: 4, x: -258, y: -349}, // group 5-8
                    {people: 4, x: -393, y: -300} // group 1-4
                ];

                var babGroup = [
                    {people: 4, x: 813, y: -359, lineName: "L1"}, // group 1-4
                    {people: 4, x: 705, y: -489, lineName: "LA"}, // group 21-24
                    {people: 4, x: 564, y: -535, lineName: "LB"}, // group 16-20
                    {people: 3, x: -227, y: -614, lineName: "LF"}, // group 9-15
                    {people: 3, x: -334, y: -528, lineName: "LG"}, // group 5-8
                    {people: 3, x: -436, y: -481, lineName: "LH"} // group 1-4
                ];

                var titleGroup = [
                    {lineName: "L1", x: 961, y: -318},
                    {lineName: "LA", x: 941, y: -446},
                    {lineName: "LB", x: 897, y: -502},
                    {lineName: "LH", x: 162, y: -433},
                    {lineName: "LG", x: 116, y: -491},
                    {lineName: "LF", x: 73, y: -576}
                ];

                for (var i = 0; i < titleGroup.length; i++) {
                    var groupStatus = titleGroup[i];
                    $("#titleArea").append("<div></div>");
                    $("#titleArea>div")
                            .eq(i)
                            .addClass("titleWiget")
                            .css({left: groupStatus.x, top: groupStatus.y})
                            .html(groupStatus.lineName);
                }

                for (var i = 0; i < testGroup.length; i++) {
                    $("#testArea").append("<div></div>");
                    var groupStatus = testGroup[i];
                    for (var j = 0, k = groupStatus.people; j < k; j++) {
                        $("#testArea>div")
                                .eq(i)
                                .append("<div></div>")
                                .addClass("testWiget")
                                .css({left: groupStatus.x, top: groupStatus.y});
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
                                .css({left: groupStatus.x, top: groupStatus.y})
                                .attr("id", groupStatus.lineName);
                    }
                }

                var testChildElement = $("#testArea>.testWiget div");
                var babChildElement = $("#babArea>.babWiget div");

                testObjectInit();
                babObjectInit();

                $("#testArea>div, #babArea>div").draggable({
                    drag: function (e) {
                        return false;
                    }
                });
                $("#titleArea>div").addClass("lineTitle").draggable({
                    drag: function (e) {
                        return false;
                    }
                });

                $('[data-toggle="tooltip"]').tooltip();


                function initWiget(obj) {
                    obj.addClass("offLine").removeClass("alarm normal abnormal").removeAttr("title");
                }

                function testObjectInit() {
                    var object = $("#testArea>.testWiget div");
                    var loopCount = object.length;
                    object.each(function () {
                        $(this).attr("id", "draggable" + loopCount)
                                .attr("data-toggle", "tooltip")
                                .addClass("draggable")
                                .html(loopCount);
                        loopCount--;
                    });
                }

                function babObjectInit() {
                    $(".babWiget").each(function () {
                        var lineName = $(this).attr("id");
                        var childAmount = $(this).children().length;
                        $(this).children().each(function () {
                            $(this).attr("id", (lineName + childAmount))
                                    .attr("data-toggle", "tooltip")
                                    .addClass("draggable")
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
                                        signalClass = "normal";
                                        break;
                                    case 1:
                                        signalClass = "alarm";
                                        break;
                                    case 2:
                                        signalClass = "abnormal";
                                        break;
                                }
                                $(".testWiget #draggable" + people.table)
                                        .removeClass("offLine")
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
                                $("#babArea #" + people.TagName + " #" + people.TagName + people.T_Num)
                                        .removeClass("offLine")
                                        .addClass((people.ismax ? "alarm" : "normal"))
                                        .attr("title", "Time:" + people.diff + "秒");
                            }
                        }
                    }
                }

                var hostname = window.location.host;//Get the host ipaddress to link to the server.
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
        <div id="wigetCtrl">
            <div id="wigetInfo">
                <label for="empty" style="float:left">空</label>
                <div id="empty" class="draggable"></div>
                <label for="normalSign" style="float:left">正常</label>
                <div id="normalSign" class="draggable normal"></div>
                <label for="normalSign" style="float:left">警告</label>
                <div id="alarmSign" class="draggable alarm"></div>
                <label for="normalSign" style="float:left">異常</label>
                <div id="abnormalSign" class="draggable abnormal"></div>
            </div>

            <div id="mapGroup">
                <div class="clearWiget"></div>
                <img src="images/totalMap.png" alt="Img Test" width="1100px" height="600px"/>
                <div id="titleArea"></div>
                <div id="testArea"></div>
                <div class="clearWiget"></div>
                <div id="babArea"></div>
                <div class="clearWiget"></div>
            </div>
        </div>
        <hr />
        <jsp:include page="footer.jsp" />
    </body>
</html>
