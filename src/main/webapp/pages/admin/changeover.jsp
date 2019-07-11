<%-- 
    Document   : changeover
    Created on : 2017/3/28, 下午 02:38:22
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../../images/favicon.ico"/>
        <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/tooltipster.bundle.min.css"/>" >
        <style>
            .table td, .table th {
                text-align: center;   
            }
            a.done{
                color: grey;
            }
            a.no-record, #log>.error{
                color: red;
            }
            a.process, #log>.active{
                color: green;
            }
            #log {
                background: white;
                margin: 0;
                padding: 0.5em 0.5em 0.5em 0.5em;
                position: fixed;
                left: 0px;
                bottom: 0px;
                overflow: auto;
                height: 100px;
                width: 600px;
                border-width: 3px;
                border-style: solid;
                border-color: #FFAC55;
            }
            #tableArea{
                overflow: auto;
                height: 4000px;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js" />"></script>
        <script src="<c:url value="/js/urlParamGetter.js" />"></script>
        <script src="<c:url value="/js/tooltipster.bundle.min.js"/>"></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>

        <script>
            $(function () {
                var sitefloor = getQueryVariable("sitefloor");

                var msg = $("#sendMessage");
                var log = $("#log");

                if (sitefloor == null) {
                    alert("Sitefloor not found");
                    return;
                }

                var lineObj = getLine();

                for (var i = 0; i < lineObj.length; i++) {
                    var line = lineObj[i];
                    var tableString =
                            "<div class='col-md-2'><table id=tb_" +
                            line.id +
                            " class='table table-bordered'><thead></thead><tbody></tbody></table></div>";

                    $("#tableArea").append(tableString);
                    $("#tableArea" + " #tb_" + line.id).find("thead").append("<tr><th>" + line.name + "</th></tr>");
                }

                $("#sendMessage").keydown(function (event) {
                    if (event.which == 13) {
                        event.preventDefault();
                        sendMessage($(this).val());
                        $(this).val(null);
                    }
                });

                $("#sync").click(function () {
                    websocket.send("sync");
                });

                $("#chatClear").click(function () {
                    log.html("");
                });

                init();

                function appendLog(message, style) {
                    log.append("<div class='" + (style == null ? '' : style) + "'>" + message + "</div>");
                    log.scrollTop(log.prop("scrollHeight"));
                }

                function getLine() {
                    var obj;
                    $.ajax({
                        type: "Get",
                        url: "<c:url value="/BabLineController/findAll" />",
                        data: {
                            sitefloor: sitefloor
                        },
                        dataType: "json",
                        async: false,
                        success: function (response) {
                            obj = response;
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            console.log(xhr.responseText);
                        }
                    });
                    return obj;
                }

                function init() {
                    console.log(1);
                    var hostname = window.location.host;//Get the host ipaddress to link to the server.
                    //var hostname = "172.20.131.52:8080";

                    //websocket will reconnect by reconnecting-websocket.min.js when client or server is disconnect

                    var clientId = $("#clientId").val();

                    websocket = new WebSocket("ws://" + hostname + "/CalculatorWSApplication/echo6/" + "test");

                    websocket.onopen = function () {
                        appendLog("CONNECTED", "active");
                    };

                    websocket.onmessage = function (evt) {
                        var data = evt.data;

                        try {
                            
                            var jsonObject = JSON.parse(data);
                            var action = jsonObject.action;
                            var currentStatus = jsonObject.status;

                            appendLog("Receive data", "active");
                            
                            if (action == 'init') {
                                $(".table > tbody").html("");
                                for (var i = 0; i < lineObj.length; i++) {
                                    var line = lineObj[i];
                                    if (line.id in currentStatus) {
                                        var tb = $("#tb_" + line.id);
                                        var tbhead = tb.find("thead");
                                        var tbbody = tb.find("tbody");
                                        var jsonArray = currentStatus[line.id];

                                        var prevBab = null;

                                        for (var j = 0; j < jsonArray.length; j++) {
                                            var bab = jsonArray[j];

                                            var diff;
                                            if (prevBab != null) {
                                                if (prevBab.lastUpdateTime != null) {
                                                    var prevEndTime = moment(prevBab.lastUpdateTime);
                                                    var currentStartTime = moment(bab.beginTime);
                                                    diff = moment.utc(currentStartTime.diff(prevEndTime)).format("HH:mm:ss");
                                                }
                                            }

                                            if (j < jsonArray.length && j != 0) {
                                                tbbody.append(
                                                        "<tr><td><span class='" + (bab.isused == 0 ? "process_arrow" : "") + " glyphicon glyphicon-arrow-down'><strong>" +
                                                        (diff == null ? "" : diff) +
                                                        "</strong></span></td></tr>"
                                                        );
                                                diff = null;
                                            }

                                            tbbody.append("<tr><td><a id='data_" + bab.lineName + j + "' class='" + (bab.isused == 0 ? "process" : (bab.isused == -1 ? "no-record" : "done")) + "'>" + bab.modelName + "</a></td></tr>");
                                            var babStatus = loopObjectParams(bab);
                                            if (bab.lastUpdateTime != null) {
                                                var startTime = moment(bab.beginTime);
                                                var endTime = moment(bab.lastUpdateTime);
                                                babStatus += "<p>Time processed: " + moment.utc(endTime.diff(startTime)).format("HH:mm:ss") + "</p>";
                                            }

                                            $("#data_" + bab.lineName + j).tooltipster({trigger: "hover", side: "right", contentAsHTML: true, updateAnimation: null, 'content': babStatus});

                                            prevBab = bab;
                                        }

                                        var lastBab = jsonArray[jsonArray.length - 1];
                                        if (lastBab.isused != 0) {
                                            tbbody.append("<tr class='suspend_col'><td><a>閒置: </a><input type='hidden' class='interval_suspend_time' hidden value='" + lastBab.lastUpdateTime + "' /><a></a></td></tr>");
                                        }
                                    }
                                }


                            } else if (action == 'update') {
                                sendMessage("sync");
                            }

                            if ($(".interval_suspend_time").length != 0) {
                                setInterval(function () {
                                    $(".interval_suspend_time").each(function () {
                                        var suspendTime = moment($(this).val());

                                        var now = moment();
                                        $(this).next().html(moment.utc(now.diff(suspendTime)).format("HH:mm:ss"));
                                    });
                                }, 1000);
                            }

                        } catch (e) {
                            var messages = data.split('\n');
                            for (var i = 0; i < messages.length; i++) {
                                appendLog(messages[i]);
                            }
                        }

                    };

                    websocket.onerror = function (evt) {
                        appendLog("ERROR", "error");
                    };

                }

                function sendMessage(message) {
                    websocket.send(message);
                }

                function loopObjectParams(target) {
                    var str = "<p>PO: " + target.po + "</p>" +
                            "<p>ModelName: " + target.modelName + "</p>" +
                            "<p>line: " + target.line.id + "</p>" +
                            "<p>people: " + target.people + "</p>" +
                            "<p>startTime: " + target.beginTime + "</p>" +
                            "<p>endTime: " + target.lastUpdateTime + "</p>";
                    return str;
                }
            });

            window.onerror = function (msg, url, linenumber) {
                alert('Error message: ' + msg + '\nURL: ' + url + '\nLine Number: ' + linenumber);
                return true;
            };

            //window.addEventListener("load", init, false);
        </script>
    </head>
    <body>
        <input id="clientId" placeholder="id"/><input id="connect" type="button" value="connect">
        <input id ="sendMessage" placeholder="message" /><input type="button" id="sync" value="sync">
        <input type="button" id="chatClear" value="chatClear">
        <div id="output"></div>
        <div id="log"></div>
        <div id="tableArea">
        </div>
    </body>
</html>
