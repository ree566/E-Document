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
        <script src="<c:url value="/js/ajax-option-select-loader/floor.loader.js" />"></script>
        <script src="<c:url value="/js/ajax-option-select-loader/babLineType.loader.js" />"></script>
        <script src="<c:url value="/js/jquery.fileDownload.js" />"></script>

        <script>
            lineTypeLoaderUrl = "<c:url value="/BabLineController/findLineType" />";
            floorLoaderUrl = "<c:url value="/FloorController/findAll" />";
            var round_digit = 2;

            $(function () {
                setLineObject();
                initOptions($("#lineType"));
                initFloorOptions($("#sitefloor"));

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

                var generateExcel = function () {
                    var btn = $("#send");
                    var floor_id = $('#sitefloor').val();
                    var lineType_id = $('#lineType').val();
                    var startDate = beginTimeObj.data("DateTimePicker").date();
                    var endDate = endTimeObj.data("DateTimePicker").date();

                    if (endDate.diff(startDate, 'days') > datePickerLockDays) {
                        alert("日期過長, 請重新設定查詢時間 <= " + datePickerLockDays);
                        return false;
                    }

                    if (startDate == null || endDate == null) {
                        alert("請輸入日期");
                        return false;
                    }

                    btn.attr("disabled", true);
                    $.fileDownload('<c:url value="/ExcelExportController/getBabPreAssyDetail" />' + '?startDate=' +
                            startDate.format(momentFormatString) + '&endDate=' +
                            endDate.format(momentFormatString) + '&lineType_id=' +
                            lineType_id + '&floor_id=' + floor_id, {
                                preparingMessageHtml: "We are preparing your report, please wait...",
                                failMessageHtml: "No reports generated. No Survey data is available.",
                                successCallback: function (url) {
                                    btn.attr("disabled", false);
                                },
                                failCallback: function (html, url) {
                                    btn.attr("disabled", false);
                                }
                            });
                };

                $("#send").click(generateExcel);

                $("#lineType").val(-1);
                $("#sitefloor").val(-1);
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
                <h3>前置資料查詢</h3>
                <table id="" class="table">
                    <tr>
                        <td>
                            <div class="form-group form-inline">
                                <select id="sitefloor"> 
                                    <option value="-2">請選擇樓層</option>
									<option value="-1">All</option>
                                </select>
                                <select id="lineType"> 
                                    <option value="-2">請選擇製程</option>
									<option value="-1">All</option>
                                </select>
                                日期:從
                                <div class='input-group date' id='beginTime'>
                                    <input type="text" id="fini" placeholder="請選擇起始時間"> 
                                </div> 
                                到 
                                <div class='input-group date' id='endTime'>
                                    <input type="text" id="ffin" placeholder="請選擇結束時間"> 
                                </div>
                                <input type="button" id="send" value="下載(Download)">
                            </div>
                        </td>
                    </tr>
                </table>
                <div style="width:100%">

                </div>
                <hr />

                <div id="serverMsg"></div>
            </div>
        </div>
        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
