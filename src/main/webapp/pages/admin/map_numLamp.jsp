<%-- 
    Document   : drag
    Created on : 2016/4/19, 上午 11:55:47
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <c:set var="userSitefloor" value="${param.sitefloor}" />
        <c:if test="${(userSitefloor == null) || (userSitefloor == '' || userSitefloor < 1 || userSitefloor > 7)}">
            <c:redirect url="/SysInfo" />
        </c:if>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${userSitefloor}F狀態平面圖 - ${initParam.pageTitle}</title>
        <link rel="stylesheet" href="../../css/jquery-ui.css">
        <link rel="stylesheet" href="../../css/tooltipster.bundle.min.css">
        <style>
            body{
                font-family: 微軟正黑體;
            }
            .draggable { 
                float: left;
                /*background-color: red;*/
                margin: 0px;
                cursor: default;
                text-align: center;
            }

            #babArea .draggable{
                width: 25px; 
                height: 25px; 
                padding: 1.0em; 
                font-size: 36px;
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
                background-image: url(../../images/totalMap_${userSitefloor}f.png);
                background-repeat: no-repeat;
                -o-background-size: 100% 100%, auto;
                -moz-background-size: 100% 100%, auto;
                -webkit-background-size: 100% 100%, auto;
                background-size: 100% 100%, auto;
                background-position:center center;
                border:5px red solid;
                /*讓最外層div不要隨視窗變動而改變(不然裏頭的子div會跑掉)*/
                position: absolute; 
            }
            /*            body {
                            padding-top: 70px;
                             Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. 
                        }*/
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
            }

            #titleArea>div, #babArea>div{
                position: absolute;
            }

            .titleWiget{
                cursor: pointer;
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
                background-image: url(../../images/blub-icon/Gray_Light_Icon.png);
                /*background-color: red;*/
            }

            .blub-normal{
                background-image: url(../../images/blub-icon/Green_Light_Icon.png);
                cursor: pointer;
            }

            .blub-alarm{
                background-image: url(../../images/blub-icon/Blue_Light_Icon.png);
                cursor: pointer;
            }

            .blub-abnormal{
                background-image: url(../../images/blub-icon/Yellow_Light_Icon.png);
            }

            #tooltipTrig{
                position: fixed;
                bottom: 0px;
            }

            .suggestMsg{
                background-color: white;
                color: red;
                margin: 5px;
                padding: 5px;
            }

            #wigetInfo > p{
                font-size: 24px;
            }
            .stretch_it{
                white-space: nowrap;
            }
            .justify{
                text-align: justify;
            }
        </style>
        <script src="../../js/jquery-1.11.3.min.js"></script>
        <script src="../../js/jquery-ui-1.10.0.custom.min.js"></script>
        <script src="../../js/reconnecting-websocket.min.js"></script>
        <script src="../../js/tooltipster.bundle.min.js"></script>
        <script src="../../js/numLamp-setting/${userSitefloor}f.js"></script>
        <script src="../../js/stretch-text.js"></script>

        <script>

            $(function () {

                var pXa = -0;
                var pYa = -0;

                for (var i = 0; i < titleGroup.length; i++) {
                    var groupStatus = titleGroup[i];
                    $("#titleArea").append("<div></div>");
                    $("#titleArea>div")
                            .eq(i)
                            .attr("id", groupStatus.lineName + "_title")
                            .addClass("titleWiget")
                            .html(groupStatus.lineName)
                            .css({left: groupStatus.x + pXa, top: groupStatus.y + pYa});
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

                var babChildElement = $("#babArea>.babWiget div");

                babObjectInit();

                $("#titleArea>div").not(".clearWiget").addClass("lineTitle");

                var dragableWiget = $("#titleArea>div, #babArea>div");
//                dragableWiget.after("<div class='clearWiget'></div>");

//                dragableWiget.not(".clearWiget").addClass("ui-helper").draggable({
//                    drag: function (e) {
////                        return false;
//                    }
//                });
                $('#wigetInfo > p').strech_text();

                function initWiget(obj) {
                    obj.addClass("blub-empty").removeClass("blub-alarm blub-normal blub-abnormal").html(0);
                    obj.parent().tooltipster('content', "empty");
                }

                function babObjectInit() {
                    $(".babWiget").each(function () {
                        var lineName = $(this).attr("id");
                        var childAmount = $(this).children().length;
                        $(this).children().each(function () {
                            $(this).attr({"id": (lineName + "_" + childAmount)})
                                    .addClass("draggable blub-empty divCustomBg")
                                    .html(0);
                            childAmount--;
                        });
                        $("#" + lineName)
                                .attr({title: "empty"})
                                .tooltipster({trigger: "click", side: "right", contentAsHTML: true, updateAnimation: null});
                    });
                }

                function babDataToWiget(babObject) {
                    $("#messageBoxArea > div").html("");
                    initWiget(babChildElement);
                    if (babObject != null) {
                        var babData = babObject;
                        if (babData != null) {
                            for (var k = 0, l = babGroup.length; k < l; k++) {
                                var lineName = babGroup[k].lineName;
                                var details = babObject[lineName];
                                if (details != null) {
                                    var suggestPeople = details.suggestTestPeople;
                                    var obj = $("#babArea #" + lineName.trim() + " #" + lineName.trim() + "_" + 1);//統一寫到數字燈1
                                    if (obj.length) {
                                        obj.removeClass("blub-empty")
                                                .addClass((suggestPeople == null ? "blub-abnormal" : "blub-normal"))
                                                .html(suggestPeople);
                                        var messageArray = details.message;
                                        var message = "PO: " + details.PO + "<br/>ModelName: " + details.model_name + "<br/>";
                                        if (details.quantity != null) {
                                            message += "Current piece: " + details.quantity + " pcs<br/>";
                                        }
                                        for (var i = 0; i < messageArray.length; i++) {
                                            message += messageArray[i] + "<br/>";
                                        }
                                        message += "<div class='suggestMsg'>" + lineName.trim() + " Suggestion station Action: " + details.suggestTestPeople + "</div>";
                                        $("#" + lineName.trim()).tooltipster('content', message);
                                    }
                                }
                            }
                        }
                    }
                }

                var hostname = window.location.host;//Get the host ipaddress to link to the server.
//                var hostname = "172.20.131.52:8080";
                //--------------websocket functions
                //websocket will reconnect by reconnecting-websocket.min.js when client or server is disconnect
                var ws = new ReconnectingWebSocket("ws://" + hostname + "/CalculatorWSApplication/echo3");
                ws.timeoutInterval = 3000;
                ws.onopen = function () {

                };

                //Get the server message and transform into table.
                ws.onmessage = function (message) {
                    var jsonArray = $.parseJSON(message.data);
                    if (jsonArray.length != 0) {
                        babDataToWiget(jsonArray);
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
            <div id="mapGroup">
                <div id="wigetInfo">
                    <p class="stretch">數字燈</p>
                </div>
                <div id="titleArea"></div>
                <div id="babArea"></div>
            </div>
        </div>
        <div class="clearWiget" />
    </body>
</html>
