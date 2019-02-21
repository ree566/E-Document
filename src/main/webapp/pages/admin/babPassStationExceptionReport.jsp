<%-- 
    Document   : babDetailInfo
    Created on : 2016/6/14, 下午 03:18:11
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />">
        <link rel="stylesheet" href="<c:url value="/webjars/jquery-ui-themes/1.12.1/redmond/jquery-ui.min.css" />" >
        <link rel="stylesheet" href="<c:url value="/css/bootstrap-datetimepicker.min.css"/>">
        <link rel="stylesheet" href="<c:url value="/webjars/datatables/1.10.16/css/jquery.dataTables.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/fixedHeader.dataTables.min.css"/>">
        <link rel="stylesheet" href="<c:url value="/css/buttons.dataTables.min.css"/>">
        <style>
            body{
                font-size: 16px;
                padding-top: 70px;
            }
            .wiget-ctrl{
                width: 98%;
                margin: 5px auto;
            }
            table th{
                text-align: center;
            }
            .alarm{
                color:red;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/js/jquery-ui-1.10.0.custom.min.js" />"></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>
        <script src="<c:url value="/js/bootstrap-datetimepicker.min.js" />"></script>
        <script src="<c:url value="/webjars/datatables/1.10.16/js/jquery.dataTables.min.js" /> "></script>
        <script src="<c:url value="/js/dataTables.fixedHeader.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/dataTables.buttons.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.flash.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/jszip.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/pdfmake.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/vfs_fonts.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.html5.min.js" />"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.print.min.js" />"></script>
        <script src="<c:url value="/js/urlParamGetter.js"/>"></script>
        <script src="<c:url value="/js/ajax-option-select-loader/babLineType.loader.js" />"></script>
        <script src="<c:url value="/js/jquery.fileDownload.js" />"></script>
        
        <script>
            lineTypeLoaderUrl = "<c:url value="/BabLineController/findLineType" />";
            var round_digit = 2;

            $(function () {
                setLineObject();
                initOptions($("#lineType"));

                var urlLineType = getQueryVariable("lineType");
                if (urlLineType == null) {
                    window.location.href = 'SysInfo';
                    return false;
                }

                var datePickerLockDays = 7;
                var momentFormatString = 'YYYY-MM-DD';
                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");
                var options = {
                    defaultDate: moment().subtract(1, 'days'),
                    useCurrent: true,
                    //locale: "zh-tw",
                    format: momentFormatString,
                    extraFormats: [momentFormatString]
                };
                var beginTimeObj = $('#fini').datetimepicker(options);
                var endTimeObj = $('#ffin').datetimepicker(options);

                var table;

                $("#send").click(function () {
                    var po = $('#po').val();
                    var modelName = $('#Model_name').val();
                    var lineType_id = $('#lineType').val();

                    var startDate = beginTimeObj.data("DateTimePicker").date();
                    var endDate = endTimeObj.data("DateTimePicker").date();

                    var poOrModelNameNotFill = (po == null || po.trim() == "") && (modelName == null || modelName.trim() == "");

                    if (poOrModelNameNotFill && endDate.diff(startDate, 'days') > datePickerLockDays) {
                        alert("日期過長, 請至少輸入工單或機種");
                        return false;
                    }

                    if (poOrModelNameNotFill && (startDate == null || endDate == null)) {
                        alert("請至少輸入工單機種或日期");
                        return false;
                    }

                    $("input").attr("disabled", true);
                    table = $("#table1").DataTable({
                        dom: 'Bfrtip',
                        buttons: [
                            'copy', 'excel', 'print'
                        ],
                        "processing": true,
                        "serverSide": false,
                        fixedHeader: {
                            headerOffset: 50
                        },
                        "ajax": {
                            "url": "<c:url value="/SqlViewController/findBabPassStationExceptionReport" />",
                            "type": "Get",
                            data: {
                                po: po,
                                modelName: modelName,
                                startDate: startDate == null ? null : startDate.format(momentFormatString),
                                endDate: endDate == null ? null : endDate.format(momentFormatString),
                                lineType_id: lineType_id
                            }
                        },

                        "columns": [
                            {data: "line_id", visible: false},
                            {data: "line_name"},
                            {data: "data_lost_cnt"},
                            {data: "zero_time_cnt"},
                            {data: "short_time_cnt"},
                            {data: "data_lost_cnt"},
                            {data: "zero_time_cnt"},
                            {data: "short_time_cnt"},
                            {data: "pcs_sum"},
                            {data: "total_qty"},
                            {data: "total_qty"}
                        ],
                        "columnDefs": [
                            {
                                "type": "html",
                                "targets": [5, 6, 7],
                                'render': function (data, type, full, meta) {
                                    return getPercent(data / full["pcs_sum"]);
                                }
                            },
                            {
                                "type": "html",
                                "targets": [10],
                                'render': function (data, type, full, meta) {
                                    return getPercent(full["pcs_sum"] / data);
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
                        filter: false,
                        info: false,
                        paginate: false,
                        destroy: true,
                        "initComplete": function (settings, json) {
                            $("input").removeAttr("disabled");
                        },
                        "order": [[1, "asc"]]
                    });
                });

                $.ajax({
                    type: "Get",
                    url: "<c:url value="/BabController/findAllModelName" />",
                    dataType: "json",
                    success: function (data) {
                        $("input#Model_name").autocomplete({
                            width: 300,
                            max: 10,
                            delay: 100,
                            minLength: 3,
                            autoFocus: true,
                            cacheLength: 1,
                            scroll: true,
                            highlight: false,
                            source: function (request, response) {
                                var term = $.trim(request.term);
                                var matcher = new RegExp('^' + $.ui.autocomplete.escapeRegex(term), "i");

                                response($.map(data, function (v, i) {
                                    var text = v;
                                    if (text && (!request.term || matcher.test(text))) {
                                        return {
                                            label: v,
                                            value: v
                                        };
                                    }
                                }));
                            }
                        });
                    }
                });

                $("#generateExcel").click(function () {
                    var btn = $(this);
                    var po = $('#po').val();
                    var modelName = $('#Model_name').val();
                    var lineType_id = $('#lineType').val();
                    var startDate = beginTimeObj.data("DateTimePicker").date();
                    var endDate = endTimeObj.data("DateTimePicker").date();


                    btn.attr("disabled", true);
                    $.fileDownload('<c:url value="/ExcelExportController/getBabPassStationExceptionReportDetails" />' + '?startDate=' + 
                            startDate.format(momentFormatString) + '&endDate=' + 
                            endDate.format(momentFormatString) + '&lineType_id=' + 
                            lineType_id + '&po=' + po + '&modelName=' + 
                            modelName, {
                        preparingMessageHtml: "We are preparing your report, please wait...",
                        failMessageHtml: "No reports generated. No Survey data is available.",
                        successCallback: function (url) {
                            btn.attr("disabled", false);
                        }
                        , failCallback: function (html, url) {
                            btn.attr("disabled", false);
                        }
                    });
                });

                $("#lineType").val($('#lineType option:contains(ASSY)').val());
            });

            function formatDate(dateString) {
                return moment(dateString).format('YYYY-MM-DD hh:mm:ss');
            }

            function getPercent(val) {
                return roundDecimal((val * 100), round_digit) + '%';
            }

            function roundDecimal(val, precision) {
                var size = Math.pow(10, precision);
                return Math.round(val * size) / size;
            }
        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />
        <div class="container form-inline">
            <div style="width:100%">
                <h3>Barcode異常統計查詢</h3>
                <h5>※工單、機種、日期若不指定請留空白</h5>
                <table id="leaveRequest" class="table">
                    <tr>
                        <td>
                            <div class="form-group form-inline">
                                <input type="text" id="po" placeholder="請輸入工單" />
                                <input type="text" id="Model_name" placeholder="請輸入機種" />
                                <select id="lineType"> 
                                    <option value="-1">請選擇線別</option>
                                </select>
                                日期:從
                                <div class='input-group date' id='beginTime'>
                                    <input type="text" id="fini" placeholder="請選擇起始時間"> 
                                </div> 
                                到 
                                <div class='input-group date' id='endTime'>
                                    <input type="text" id="ffin" placeholder="請選擇結束時間"> 
                                </div>
                                <input type="button" id="send" value="確定(Ok)">
                                <input type="button" id="generateExcel" value="Details">
                            </div>
                        </td>
                    </tr>
                </table>
                <div style="width:100%">
                    <h3>統計數量</h3>
                    <table id="table1" class="display" cellspacing="0" width="100%" style="text-align: center" >
                        <thead>
                            <tr>
                                <th>線別id</th>
                                <th>線別名稱</th>
                                <th>漏刷筆數</th>
                                <th>無資料筆數</th>
                                <th>連續過筆數</th>
                                <th>漏刷(%)</th>
                                <th>無資料(%)</th>
                                <th>連續過(%)</th>
                                <th>總數量</th>
                                <th>MES數量</th>
                                <th>導入率</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <hr />

                <div id="serverMsg"></div>
            </div>
        </div>
        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
