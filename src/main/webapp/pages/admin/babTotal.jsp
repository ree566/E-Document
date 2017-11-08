<%-- 
    Document   : chart
    Created on : 2016/2/16, 下午 02:26:52
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
        <link rel="stylesheet" href="../../css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="../../css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" href="../../css/jquery-ui.css">
        <link rel="stylesheet" href="../../css/buttons.dataTables.min.css">
        <style>
            body{
                font-size: 16px;
                padding-top: 70px;
            }
            table{
                width:100%;
            }
            .alarm{
                color:red;
            }
            .wiget-ctrl{
                width: 98%;
                margin: 5px auto;

            }
            #balanceCount{
                border:2px black solid;
                width:50%;
            }
        </style>
        <script src="../../js/charts.loader.js"></script>
        <script src="../../js/jquery-1.11.3.min.js"></script>
        <script src="../../js/jquery-ui-1.10.0.custom.min.js"></script>
        <script src="../../js/canvasjs.min.js"></script>
        <script src="../../js/jquery.dataTables.min.js"></script>
        <script src="../../js/dataTables.fnMultiFilter.js"></script>
        <script src="../../js/jquery.blockUI.js"></script>
        <script src="../../js/moment.js"></script>
        <script src="../../js/bootstrap-datetimepicker.min.js"></script>
        <script src="../../js/jquery.cookie.js"></script>
        <script src="../../js/alasql.min.js"></script> 
        <script src="../../js/jquery-datatable-button/dataTables.buttons.min.js"></script>
        <script src="../../js/jquery-datatable-button/buttons.flash.min.js"></script>
        <script src="../../js/jquery-datatable-button/jszip.min.js"></script>
        <script src="../../js/jquery-datatable-button/pdfmake.min.js"></script>
        <script src="../../js/jquery-datatable-button/vfs_fonts.js"></script>
        <script src="../../js/jquery-datatable-button/buttons.html5.min.js"></script>
        <script src="../../js/jquery-datatable-button/buttons.print.min.js"></script>
        <script src="../../js/param.check.js"></script>
        <script src="../../js/urlParamGetter.js"></script>
        <script src="../../js/jquery.fileDownload.js"></script>
        <script>
            var round_digit = 2;

            var table2;
            var autoReloadInterval;

            var lineTypeOptions = ["ASSY", "Packing"];

            var lineObject = {
                ASSY: [],
                Packing: []
            };

            var iframeUrl = "babMainSearch.jsp?t=1";

            function setLineObject() {
                $.ajax({
                    type: "GET",
                    url: "<c:url value="/GetLine" />",
                    data: {
                        sitefloor: $("#userSitefloorSelect").val()
                    },
                    dataType: "json",
                    async: false,
                    success: function (response) {
                        var lines = response;
                        for (var i = 0; i < lines.length; i++) {
                            var line = lines[i];
                            var lineName = line.name;
                            var lineType = line.linetype;
                            lineType == 'ASSY' ? lineObject.ASSY.push(lineName) : lineObject.Packing.push(lineName);
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showMsg(xhr.responseText);
                    }
                });
            }

            function initSelectOption() {
                $.getJSON("../../json/sitefloor.json", function (data) {
                    var sitefloors = data.sitefloors;
                    for (var i = 0, j = sitefloors.length; i < j; i++) {
                        var sitefloor = sitefloors[i].floor;
                        $("#sitefloor").append("<option value=" + sitefloor + ">" + sitefloor + "F</option>");

                    }
                });

                for (var i = 0; i < lineTypeOptions.length; i++) {
                    var value = lineTypeOptions[i];
                    $("#lineTypeFilter").append("<option value=" + value + ">" + value + "</option>");
                }
            }

            function getBAB() {
                table2 = $("#data2").DataTable({
                    "processing": false,
                    "serverSide": false,
                    "ajax": {
                        "url": "../../GetTotal",
                        "type": "POST",
                        "data": {"type": "type2"}
                    },
                    "columns": [
                        {},
                        {data: "TagName"},
                        {data: "T_Num"},
                        {data: "groupid"},
                        {data: "diff"},
                        {data: "ismax"}
                    ],
                    "columnDefs": [
                        {
                            "type": "html",
                            "targets": 0,
                            "data": "TagName",
                            'render': function (data, type, row) {
                                var ASSY = lineObject.ASSY;
                                var Packing = lineObject.Packing;
                                return $.inArray(data, ASSY) !== -1 ? 'ASSY' : ($.inArray(data, Packing) !== -1 ? 'Packing' : 'N/A');
                            }
                        },
                        {
                            "type": "html",
                            "targets": 3,
                            'render': function (data, type, row) {
                                return data == null ? "N/A" : data;
                            }
                        },
                        {
                            "type": "html",
                            "targets": 4,
                            'render': function (data, type, row) {
                                return data == null ? "N/A" : (data + '秒');
                            }
                        },
                        {
                            "type": "html",
                            "targets": 5,
                            'render': function (data, type, row) {
                                if (data == null) {
                                    return "N/A";
                                } else {
                                    return (data == true ? "<img src='../../images/red-light.jpg' width=20>" : "");
                                }
                            }
                        }
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: true,
                    displayLength: -1,
                    lengthChange: false,
//                    filter: false,
//                    info: false,
                    paginate: false,
                    "initComplete": function (settings, json) {
                        generateOnlineBabDetail(-1);

                        $("#lineTypeFilter").change(function () {
                            var filterVal = $(this).val();
                            table2.column(0).search(filterVal == -1 ? "" : filterVal).draw();
                            generateLineBalance(filterVal);
                        });
                    },
                    "order": [[1, "asc"], [2, "asc"]]
                });
            }

            function generateLineBalance(lineType) {
                if (lineType == "ASSY") {
                    generateOnlineBabDetail(lineObject.ASSY);
                } else if (lineType == "Packing") {
                    generateOnlineBabDetail(lineObject.Packing);
                } else {
                    generateOnlineBabDetail(-1);
                }
            }

            function generateOnlineBabDetail(filter) {
                $("#balanceCount").html("");
                var arr = table2.rows().data().toArray();
                if (arr == null || arr.length == 0) {
                    return false;
                }
                var res;
                if (filter.constructor === Array) {
                    res = alasql('SELECT PO, TagName, Model_name,\
                                          SUM(diff) / (COUNT(diff) * MAX(diff)) AS balance \
                                          FROM ? \
                                          WHERE TagName IN @(?)\
                                          GROUP BY PO, TagName, Model_name \
                                          ORDER BY TagName', [arr, filter]);
                } else {
                    res = alasql('SELECT PO, TagName, Model_name,\
                                          SUM(diff) / (COUNT(diff) * MAX(diff)) AS balance \
                                          FROM ? \
                                          GROUP BY PO, TagName, Model_name \
                                          ORDER BY TagName', [arr]);
                }

                //balance節省麻煩由alasql做前端計算

                for (var i = 0; i < res.length; i++) {
                    $("#balanceCount").append(
                            '<p>線別: ' + res[i].TagName +
                            ' 工單號碼: ' + res[i].PO +
                            ' 機種: ' + res[i].Model_name +
                            ' 最後一台平衡率: ' + getPercent(res[i].balance, round_digit) +
                            "</p>");
                }
            }

            function getPercent(val) {
                return roundDecimal((val * 100), round_digit) + '%';
            }

            function roundDecimal(val, precision) {
                var size = Math.pow(10, precision);
                return Math.round(val * size) / size;
            }

            function block() {
                $.blockUI({
                    css: {
                        border: 'none',
                        padding: '15px',
                        backgroundColor: '#000',
                        '-webkit-border-radius': '10px',
                        '-moz-border-radius': '10px',
                        opacity: .5,
                        color: '#fff'
                    },
                    fadeIn: 0
                    , overlayCSS: {
                        backgroundColor: '#FFFFFF',
                        opacity: .3
                    }
                });
            }

            function initDateTimePickerWiget() {
                var lockDays = 30;
                var momentFormatString = 'YYYY-MM-DD';
                var options = {
                    defaultDate: moment(),
                    useCurrent: true,
                    maxDate: moment(),
                    format: momentFormatString,
                    extraFormats: [momentFormatString]
                };
                var beginTimeObj = $('#fini').datetimepicker(options);
                var endTimeObj = $('#ffin').datetimepicker(options);

                beginTimeObj.on("dp.change", function (e) {
                    endTimeObj.data("DateTimePicker").minDate(e.date);
                    var beginDate = e.date;
                    var endDate = endTimeObj.data("DateTimePicker").date();
                    var dateDiff = endDate.diff(beginDate, 'days');
                    if (dateDiff > 30) {
                        endTimeObj.data("DateTimePicker").date(beginDate.add(lockDays, 'days'));
                    }
                });

                endTimeObj.on("dp.change", function (e) {
                    var beginDate = beginTimeObj.data("DateTimePicker").date();
                    var endDate = e.date;
                    var dateDiff = endDate.diff(beginDate, 'days');
                    if (dateDiff > 30) {
                        beginTimeObj.data("DateTimePicker").date(endDate.add(-lockDays, 'days'));
                    }
                });
            }

            function tableAjaxReload(tableObject) {
                tableObject.ajax.reload();
            }

            function showDialogMsg(msg) {
                $("#dialog-msg").html(msg);
            }

            function resizeIframe(obj) {
                obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
            }

            $(document).ready(function () {
                $('[data-toggle="tooltip"]').tooltip();
                var interval = null;
                var countdownnumber = 30 * 60;
                var diff = 12;

                setLineObject();

                getBAB();

                $("input, select").not(":checkbox").addClass("form-control");
                $(":button").addClass("btn btn-default");

                initSelectOption();

                var lineType = getQueryVariable("lineType");
                var babId = getQueryVariable("babId");
                if (lineType != null) {
                    $("#lineTypeFilter").val(lineType);
                    table2.column(0).search(lineType).draw();
                    generateLineBalance(lineType);
                    iframeUrl += "&lineType=" + lineType;
                }
                if (babId != null) {
                    iframeUrl += "&babId=" + babId;

                    setTimeout(function () {
                        window.location.hash = "#frame1";
                    }, 1000);
                }

                $(window).on("focus", function () {
                    autoReloadInterval = setInterval(function () {
                        table2.ajax.reload(function (json) {
                            generateLineBalance(lineType);
                        });
                    }, 10 * 1000);
                });

                $(window).on("blur", function () {
                    clearInterval(autoReloadInterval);
                });

                $("body").on("click", "#searchAvailableBAB, #send", function () {
                    block();
                });

                $.fn.dataTable.ext.errMode = function (settings, helpPage, message) {
                    console.log(message);
                };

                var lastHeight = 0, curHeight = 0, $frame = $('iframe:eq(0)');
                setInterval(function () {
                    curHeight = $frame.contents().find('body').height();
                    if (curHeight != lastHeight) {
                        $frame.css('height', (lastHeight = curHeight + 50) + 'px');
                    }
                }, 500);

                $("#frame1").attr("src", iframeUrl);
                
            });

        </script>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <!----->
        <div>
            <div class="wiget-ctrl form-inline">
                <div id="bab_currentStatus">
                    <h3>組裝包裝各站別狀態</h3>
                    <div style="width: 80%; background-color: #F5F5F5">
                        <div style="padding: 10px">
                            <label>Filter for:
                                <select id="lineTypeFilter">
                                    <option value="-1">all</option>
                                </select>
                            </label>
                            <table id="data2" class="display" cellspacing="0" width="100%" style="text-align: center">
                                <thead>
                                    <tr>
                                        <th>製程</th>
                                        <th>線別</th>
                                        <th>站別</th>
                                        <th>組別</th>
                                        <th>秒數</th>
                                        <th>亮燈</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
                <div id="balanceCount">
                </div>
            </div>

            <div>
                <iframe id="frame1" width="100%" frameborder="0" scrolling="no"></iframe> 
            </div>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
