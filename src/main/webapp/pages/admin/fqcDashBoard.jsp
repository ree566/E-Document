<%-- 
    Document   : drag
    Created on : 2016/4/19, 上午 11:55:47
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.UUID" %>
<!DOCTYPE html>
<html>
    <head>
        <c:set var="userSitefloor" value="${param.sitefloor}" />
        <c:if test="${(userSitefloor == null) || (userSitefloor == '' || userSitefloor < 1 || userSitefloor > 7)}">
            <c:redirect url="/SysInfo" />
        </c:if>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${userSitefloor}FQC狀態 - ${initParam.pageTitle}</title>
        <link rel="stylesheet" href="<c:url value="/webjars/jquery-ui-themes/1.12.1/redmond/jquery-ui.min.css" />" >
        <link rel="stylesheet" href="<c:url value="/css/tooltipster.bundle.min.css"/>" >
        <link rel="stylesheet" href="<c:url value="/webjars/datatables/1.10.16/css/jquery.dataTables.min.css" />">
        <style>
            body{
                font-family: 微軟正黑體;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/js/jquery-ui-1.10.0.custom.min.js"/>"></script>
        <script src="<c:url value="/js/reconnecting-websocket.min.js"/>"></script>
        <script src="<c:url value="/js/jquery.fullscreen-min.js"/>"></script>
        <script src="<c:url value="/js/tooltipster.bundle.min.js"/>"></script>
        <script src="<c:url value="/js/totalMap-setting/${userSitefloor}f.js"/>"></script>
        <script src="<c:url value="/webjars/datatables/1.10.16/js/jquery.dataTables.min.js" /> "></script>
        <script>
            var maxProductivity = 200;

            $(function () {
                var table = $("#fqc-status").DataTable({
                    "processing": false,
                    "serverSide": false,
                    "columns": [
                        {data: "id"},
                        {data: "name"},
                        {data: "po"},
                        {data: "standardTime"},
                        {data: "currentPcs"},
                        {data: "firstPcsTimeCost"},
                        {data: "productivity"},
                        {data: "processLineName"}
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: true,
                    displayLength: -1,
                    lengthChange: false,
                    filter: true,
                    paginate: false
                });

                function refreshRowData(arr) {
                    var d = [];
                    if (arr != null && arr.length != 0 && arr[0] != null) {
                        var data = arr[0].data;
                        if (data != null && data.length != 0) {
                            for (var i = 0; i < data.length; i++) {
                                var fqcData = data[i];
                                var obj = {
                                    id: fqcData.history.fqc.id,
                                    name: fqcData.history.jobnumber,
                                    po: fqcData.history.fqc.po,
                                    standardTime: fqcData.standardTime,
                                    currentPcs: fqcData.currentPcs,
                                    firstPcsTimeCost: fqcData.history.fqc.firstPcsTimeCost,
                                    productivity: getPercent(fqcData.productivity),
                                    processLineName: fqcData.history.fqc.fqcLine.name
                                };
                                d[i] = obj;
                            }
                        }
                    }
                    addDataToTable(d);
                }

                function addDataToTable(data) {
                    console.log(data);
                    table.clear().draw();
                    if (data.length) {
                        table.rows.add(data).draw();
                    }
                }

                var hostname = window.location.host;//Get the host ipaddress to link to the server.
                //var hostname = "172.20.131.52:8080";
                //--------------websocket functions
                //websocket will reconnect by reconnecting-websocket.min.js when client or server is disconnect

                var ws4;

                var onopen = function () {

                };

                var onerror = function (event) {
                    console.log("error");
                    console.log(event.data);
                };

                //generate the unnormal close event hint
                var onclose = function (event) {
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

                ws4 = new ReconnectingWebSocket("ws://" + hostname + "/CalculatorWSApplication/echo4");
                setWebSocketClient(ws4);
                //Get the server message and transform into table.
                ws4.onmessage = function (message) {
                    var jsonArray = $.parseJSON(message.data);
                    refreshRowData(jsonArray);
                };

                function setWebSocketClient(webSocket) {
                    webSocket.timeoutInterval = 3000;
                    webSocket.onopen = onopen;
                    webSocket.onerror = onerror;
                    webSocket.onclose = onclose;
                }

                function postToServer() {
                }

                function closeConnect() {
                    ws4.close();
                    console.log("websocket connection is now end");
                }
//-----------------------

                function getPercent(val) {
                    return roundDecimal((val * 100), 2);
                }

                function roundDecimal(val, precision) {
                    var size = Math.pow(10, precision);
                    return Math.round(val * size) / size;
                }

            });
        </script>
    </head>
    <body style="cursor: auto;">
        <!--<button id="fullBtn">Full</button>-->
        <div id="wigetCtrl">
            <div>
                <h5><label>效率計算公式:</label>檢驗效率 = 標準工時 * N台 / (結束-開始) * N台 </h5>
                <div class="form-inline">
                    <label for="standardTime">標準工時:</label>
                    <input type="number" id="standardTime" class="form-control" placeholder="標準工時" />
                    <label for="standardTime">N台:</label>
                    <input type="number" id="nPcs" class="form-control" placeholder="N台" />
                    <label for="standardTime">結束-開始:</label>
                    <input type="number" id="firstPcsCost" class="form-control" placeholder="結束-開始" />
                    <label for="standardTime">效率:</label>
                    <input type="number" id="productivity" class="form-control" placeholder="效率" readonly="" disabled="" />
                </div>
            </div>
            <table id="fqc-status" class="table table-bordered display cell-border">
                <thead>
                    <tr>
                        <th>id</th>
                        <th>人員</th>
                        <th>工單</th>
                        <th>標工</th>
                        <th>產出數量</th>
                        <th>第一台時間</th>
                        <th>效率(%)</th>
                        <th>線別名稱</th>
                    </tr>
                </thead>
            </table>
        </div>
        <div class="clearWiget" />
    </body>
</html>
