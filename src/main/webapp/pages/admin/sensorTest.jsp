<%-- 
    Document   : sensorAdjust
    Created on : 2016/4/14, 下午 02:11:41
    Author     : Wei.Cheng
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />">
        <link rel="stylesheet" href="<c:url value="/webjars/datatables/1.10.16/css/jquery.dataTables.min.css" />">
        <style>
            #wigetCtrl{
                margin: 0px auto;
                width: 98%
            }
            body{
                font-size: 16px;
                padding-top: 70px;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>
        <script src="<c:url value="/webjars/datatables/1.10.16/js/jquery.dataTables.min.js" /> "></script>
        <script src="<c:url value="/js/alasql.min.js" /> "></script> 
        <script src="<c:url value="/webjars/jquery-blockui/2.70/jquery.blockUI.js" /> "></script>
        <script>
            $(function () {
                var timer;
                var cnt;
                var time_options = [1, 2, 3, 5, 10];
                var test_cnt_options = [2, 10, 15, 20, 50, 100];
                var testing_rec = [];
                var cntRemaining = $("#cnt-remaining"), timeRemaining = $("#time-remaining");
                var start_disabled_group = $("#start-testing, #period-options, #cnt-options, #tagName-options");
                var action_hint = $("#action-hint");
                var move_in = "<b class='text-success'>擋住</b>", move_off = "<b class='text-danger'>拿開</b>";
                var resultTable = $("#sensor-collect-result"), resultTable2 = $("#sensor-test-result"), tb1, tb2;
                var momentFormatString = 'YYYY-MM-DD HH:mm:ss';
                var tableDateFormat = "YY/MM/DD";
                var tableTimeFormat = "HH:mm:ss";
                var tableMilliSecondFormat = "SSS";
                var tagName;
                var tableFullTimeFormat = "HH:mm:ss.SSS";
                var beginTestText = $("#test-begin-text"), endTestText = $("#test-end-text");
                var cntPerform = $("#cnt-perform"),
                        matchesRecordPerform = $("#matches-record-perform"),
                        matchesRecordPercentPerform = $("#matches-record-percent-perform");
                var resultArea = $(".result");

                loadTimeOptions();
                loadCntOptions();
                loadSensorOptions();

                $("#start-testing").click(function () {
                    showTestBegin();
                    testing_rec.length = 0;
                    start_disabled_group.attr("disabled", true);
                    cnt = $("#cnt-options").val();
                    var period = $("#period-options").val();
                    var period_temp = period * cnt;
                    tagName = $("#tagName-options").val();

                    if (cnt && period && tagName) {
                        var c = 1;
                        timer = setInterval(function () {

                            var d = moment();
                            testing_rec.push({
                                tagName: tagName,
                                signal: c % 2,
                                logDate: d.format(tableDateFormat),
                                logTime: d.format(tableTimeFormat),
                                logMilliSecond: d.format(tableMilliSecondFormat),
                                datetime: d
                            });
                            cntRemaining.html(cnt - c);
                            timeRemaining.html(period_temp -= period);
                            action_hint.html(c % 2 == 0 ? move_off : move_in);

                            if (c++ == cnt) {
                                clearInterval(timer);
                                start_disabled_group.attr("disabled", false);
                                showTestingResult();
                                getCollectingResult();
                                showTestEnd();
                            }

                        }, period * 1000);
                    } else {
                        alert("Param error.");
                    }
                });

                $("#interrupt-testing").click(function () {
                    clearInterval(timer);
                    testing_rec.length = 0;
                    cntRemaining.html("");
                    timeRemaining.html("");
                    start_disabled_group.attr("disabled", false);
                    action_hint.html(move_off);
                    clearTestStatus();
                });

                $("#period-options, #cnt-options").on("change", function () {
                    cnt = $("#cnt-options").val();
                    var period = $("#period-options").val();
                    cntRemaining.html(cnt);
                    timeRemaining.html(period * cnt);
                    action_hint.html(move_off);
                }).trigger("change");

                $("#reload-collect-result").click(function () {
                    if (tb1) {
                        tb1.ajax.reload();
                    }
                });

                clearTestStatus();

                function loadTimeOptions() {
                    var sel = $("#period-options");
                    for (var i = 0, j = time_options.length; i < j; i++) {
                        var d = time_options[i];
                        sel.append("<option value='" + d + "'>" + d + " 秒</option>");
                    }
                }

                function loadCntOptions() {
                    var sel = $("#cnt-options");
                    for (var i = 0, j = test_cnt_options.length; i < j; i++) {
                        var d = test_cnt_options[i];
                        sel.append("<option value='" + d + "'>" + d + " 次</option>");
                    }
                }

                function loadSensorOptions() {
                    $.ajax({
                        url: "<c:url value="/SensorController/findAll" />",
                        method: 'GET',
                        dataType: 'json',
                        success: function (d) {
                            var arr = d;
                            if (arr != null) {
                                var sel = $("#tagName-options");
                                for (var i = 0, j = arr.length; i < j; i++) {
                                    var obj = arr[i];
                                    var id = obj.id;
                                    sel.append("<option value='" + id.orginTagName.name + "'>" +
                                            id.lampSysTagName.name + " (" + id.orginTagName.name + ")" +
                                            "</option>");
                                }
                                sel.val("5F_LB_2:DI_02");
                            }
                        }
                    });
                }

                function showTestingResult() {
                    tb2 = resultTable2.DataTable({
                        "aaData": testing_rec,
                        "columns": [
                            {data: "tagName"},
                            {data: "logDate"},
                            {data: "logTime"},
                            {data: "logMilliSecond"},
                            {data: "signal"}
                        ],
                        "columnDefs": [

                        ],
                        "oLanguage": {
                            "sLengthMenu": "顯示 _MENU_ 筆記錄",
                            "sZeroRecords": "無符合資料",
                            "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                        },
                        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
                        destroy: true,
                        stateSave: false
                    });
                }

                function getCollectingResult() {
                    var startDate = testing_rec[0].datetime, endDate = testing_rec[testing_rec.length - 1].datetime;
//                    startDate = startDate.subtract(900, 'seconds');
                    endDate = endDate.add(1, 'seconds');
                    tb1 = resultTable.DataTable({
                        "serverSide": false,
                        "ajax": {
                            "url": "<c:url value="/SensorController/findSensorData" />",
                            "type": "GET",
                            "data": {
                                tagName: tagName,
                                startDate: startDate == null ? null : startDate.format(momentFormatString),
                                endDate: endDate == null ? null : endDate.format(momentFormatString)
                            }
                        },
                        "columns": [
                            {data: "id"},
                            {data: "tagName"},
                            {data: "logDate"},
                            {data: "logTime"},
                            {data: "logMilliSecond"},
                            {data: "logValue"},
                            {data: "group"},
                            {data: "isused"},
                            {data: "diff"},
                            {data: "bab"},
                            {data: "id"}
                        ],
                        "columnDefs": [
                            {
                                "type": "html",
                                "targets": [0, 1, 2, 3, 4, 5, 6, 7, 8],
                                'render': function (data, type, full, meta) {
                                    return data == null ? "---" : data;
                                }
                            },
                            {
                                "type": "html",
                                "targets": [9],
                                'render': function (data, type, full, meta) {
                                    return data == null ? "---" : data.id;
                                }
                            },
                            {
                                "type": "html",
                                "targets": [10],
                                'render': function (data, type, full, meta) {
                                    var e = testing_rec.find(function (item, index, array) {
                                        var d1 = moment((item["logTime"] + "." + item["logMilliSecond"]), tableFullTimeFormat);
                                        var d2 = moment((full["logTime"] + "." + full["logMilliSecond"]), tableFullTimeFormat);
                                        return Math.abs(d1.diff(d2, 'milliseconds')) <= 200;
                                    });
                                    return e == null ? "X" : ("V" + " / " +
                                            moment(Math.abs(moment((e["logTime"] + "." + e["logMilliSecond"]), tableFullTimeFormat)
                                                    .diff(moment((full["logTime"] + "." + full["logMilliSecond"]), tableFullTimeFormat)), 'milliseconds'))
                                            .format("mm:ss.SSS"));
                                }
                            }
                        ],
                        "initComplete": function (settings, json) {
                            showTestResult();
                        },
                        "oLanguage": {
                            "sLengthMenu": "顯示 _MENU_ 筆記錄",
                            "sZeroRecords": "無符合資料",
                            "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                        },
                        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
                        order: [0, "desc"],
                        destroy: true,
                        stateSave: false
                    });
                }

                function showTestResult() {
                    var c = 0;
                    resultTable.find("tbody tr").each(function () {
                        var td = $(this).children(":eq(10)").html();
                        if (td && td != "X") {
                            c++;
                        }
                    });
                    cntPerform.html(cnt);
                    matchesRecordPerform.html(c);
                    matchesRecordPercentPerform.html(getPercent(c / cnt));
                }

                function showTestBegin() {
                    beginTestText.show();
                    endTestText.hide();
                    resultArea.block();
                }

                function showTestEnd() {
                    beginTestText.hide();
                    endTestText.show();
                    resultArea.unblock();
                }

                function clearTestStatus() {
                    beginTestText.hide();
                    endTestText.hide();
                    resultArea.unblock();
                }

                function getPercent(val) {
                    return roundDecimal((val * 100), 2) + '%';
                }

                function roundDecimal(val, precision) {
                    var size = Math.pow(10, precision);
                    return Math.round(val * size) / size;
                }
            });
        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />
        <div id="wigetCtrl" class="container">
            <div class="row">
                <h3>Sensor測試</h3>
                <div class="col col-md-6">
                    <table class="table table-bordered">
                        <tr>
                            <td>
                                <label for="tagName-options">請選擇感應器</label>
                            </td>
                            <td>
                                <select id="tagName-options" class="form-control"></select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="period-options">請選擇測試秒數間距</label>
                            </td>
                            <td>
                                <select id="period-options" class="form-control"></select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="period-options">請選擇測試次數</label>
                            </td>
                            <td>
                                <select id="cnt-options" class="form-control"></select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="button" id="start-testing" class="btn btn-default" value="開始測試" />
                                <input type="button" id="interrupt-testing" class="btn btn-default" value="中斷測試" />
                                <h5 class="text-danger" id="test-begin-text">測試中</h5>
                                <h5 class="text-success" id="test-end-text">測試完成</h5>
                            </td>
                            <td>
                                <h5>剩餘 <b id="cnt-remaining"></b> 次</h5>
                                <h5>剩餘 <b id="time-remaining"></b> 秒</h5>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="action-hint">請依照指示移動物體</label>
                            </td>
                            <td>
                                <h5>請 <b id="action-hint" class="text-danger"></b> 物體</h5>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="row result">
                <h3>測試時間</h3>
                <table id="sensor-test-result">
                    <thead>
                        <tr>
                            <th>tagName</th>
                            <th>logDate</th>
                            <th>logTime</th>
                            <th>logMilliSecond</th>
                            <th>logValue</th>
                        </tr>
                    </thead>
                </table>
            </div>
            <div class="row result">
                <h3>
                    蒐集結果
                    <button id="reload-collect-result" class="btn btn-default" />
                    <span class="glyphicon glyphicon-refresh" />
                    </button>
                </h3>
                <table id="sensor-collect-result">
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>tagName</th>
                            <th>logDate</th>
                            <th>logTime</th>
                            <th>logMilliSecond</th>
                            <th>logValue</th>
                            <th>group</th>
                            <th>isused</th>
                            <th>diff</th>
                            <th>bab_id</th>
                            <th>t</th>
                        </tr>
                    </thead>
                </table>
            </div>
            <div class="row result">
                <div class="col col-md-6">
                    <h3>Total</h3>
                    <table class="table table-bordered">
                        <tr>
                            <td>測試次數</td>
                            <td id="cnt-perform"></td>
                        </tr>
                        <tr>
                            <td>吻合次數(±200毫秒)</td>
                            <td id="matches-record-perform"></td>
                        </tr>
                        <tr>
                            <td>吻合百分比</td>
                            <td id="matches-record-percent-perform"></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
