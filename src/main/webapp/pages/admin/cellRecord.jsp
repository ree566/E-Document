<%-- 
    Document   : test
    Created on : 2015/11/20, 上午 11:36:50
    Author     : Wei.Cheng
https://datatables.net/forums/discussion/20388/trying-to-access-rowdata-in-render-function-with-ajax-datasource-getting-undefined
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../../images/favicon.ico"/>
        <link rel="stylesheet" href="../../css/jquery-ui.css">
        <link rel="stylesheet" href="../../css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" href="../../css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="../../css/fixedHeader.dataTables.min.css">
        <link rel="stylesheet" href="../../css/buttons.dataTables.min.css">
        <style>
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
            table th{
                text-align: center;
            }
            .alarm{
                color: red;
            }
            #cellhistoryFilter td{
                padding: 5px;
            }
        </style>
        <script src="../../js/jquery-1.11.3.min.js"></script>
        <script src="../../js/jquery-ui-1.10.0.custom.min.js"></script>
        <script src="../../js/jquery.dataTables.min.js"></script>
        <script src="../../js/dataTables.fixedHeader.min.js"></script>
        <script src="../../js/jquery-datatable-button/dataTables.buttons.min.js"></script>
        <script src="../../js/jquery-datatable-button/buttons.flash.min.js"></script>
        <script src="../../js/jquery-datatable-button/jszip.min.js"></script>
        <script src="../../js/jquery-datatable-button/pdfmake.min.js"></script>
        <script src="../../js/jquery-datatable-button/vfs_fonts.js"></script>
        <script src="../../js/jquery-datatable-button/buttons.html5.min.js"></script>
        <script src="../../js/jquery-datatable-button/buttons.print.min.js"></script>
        <script src="../../js/moment.js"></script>
        <script src="../../js/bootstrap-datetimepicker.min.js"></script>
        <script src="../../js/urlParamGetter.js"></script>
        <script src="../../js/jquery.fileDownload.js"></script>
        <script>
            var maxProductivity = 200;
            var availHistoryTable;
            var table;
            $(document).ready(function () {

                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");

                var lines = getLine();
                for (var i = 0; i < lines.length; i++) {
                    setLineOptions(lines[i]);
                }

                $("#send").click(function () {
                    getDetail();
                });

                $("#cellHistoryResearch").click(function () {
                    getAllCell();
                });

                $("body").on('click', '#cellHistoryDetail tbody tr', function () {
                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                    } else {
                        $('#cellHistoryDetail tr.selected').removeClass('selected');
                        $(this).addClass('selected');
                    }
                });

                $("body").on('dblclick', '#cellHistory tbody tr', function () {
                    var selectData = availHistoryTable.row(this).data();
                    $("#reset").trigger("click");
                    $("#PO").val(selectData.PO);
                    $("#lineId").val(selectData.lineId);
                    $("#type").val(selectData.type);
                    $("#send").trigger("click");

                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                    } else {
                        availHistoryTable.$('tr.selected').removeClass('selected');
                        $(this).addClass('selected');
                    }

                    $("html, body").animate({scrollTop: $('#cellHistoryDetail').offset().top}, 500);
                });

                var momentFormatString = 'YYYY-MM-DD';
                var options = {
                    defaultDate: moment(),
                    useCurrent: true,
                    maxDate: moment(),
                    format: momentFormatString,
                    extraFormats: [momentFormatString]
                };

                var options2 = {
                    useCurrent: true,
                    maxDate: moment(),
                    format: momentFormatString,
                    extraFormats: [momentFormatString]
                };

                $('#fini, #ffin').datetimepicker(options);

                $('#fini_1, #ffin_1').datetimepicker(options2);

                $("#fini").click(function () {
                    if ($("#ffin").val() == "") {
                        $("#ffin").val($(this).val());
                    }
                });

                $("#fini_1").click(function () {
                    if ($("#ffin_1").val() == "") {
                        $("#ffin_1").val($(this).val());
                    }
                });
                getAllCell();
            });

            function setLineOptions(line) {
                $("#lineId").append("<option value=" + line.id + " " + (line.lock == 1 ? "disabled style='opacity:0.5'" : "") + ">線別 " + line.name + "</option>");
            }

            function getLine() {
                var result;
                $.ajax({
                    type: "Get",
                    url: "<c:url value="/CellLineServlet/findAll" />",
                    dataType: "json",
                    async: false,
                    success: function (response) {
                        result = response;
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showMsg(xhr.responseText);
                    }
                });
                return result;
            }

            function getAllCell() {
                availHistoryTable = $("#cellHistory").DataTable({
                    dom: 'lf<"floatright"B>rtip',
                    buttons: [
                        {
                            extend: 'copyHtml5',
                            exportOptions: {
                                columns: ':visible'
                            }
                        },
                        {
                            extend: 'excelHtml5',
                            exportOptions: {
                                columns: ':visible'
                            }
                        },
                        {
                            extend: 'print',
                            exportOptions: {
                                columns: ':visible'
                            }
                        }
                    ],
                    "processing": true,
                    "serverSide": false,
                    fixedHeader: {
                        headerOffset: 50
                    },
                    "ajax": {
                        "url": "<c:url value="/CellController/findByDate" />",
                        "type": "GET",
                        data: {
                            action: "getHistory",
                            startDate: $("#fini").val(),
                            endDate: $("#ffin").val()
                        }
                    },
                    "columns": [
                        {data: "id", visible: false},
                        {data: "line.id", visible: false},
                        {data: "po"},
                        {data: "modelName"},
                        {data: "line.name"},
                        {data: "lineType.name"},
                        {data: "failPercent"},
                        {data: "total"},
                        {data: "isused"},
                        {data: "btime"},
                        {data: "lastUpdateTime"}
                    ],
                    "columnDefs": [
                        {
                            "targets": 6,
                            'render': function (data, type, full, meta) {
                                return ((data * 100 < 1 && data !== 0) ? getPercent(data, 2) : getPercent(data, 1)) + '%';
                            }
                        },
                        {
                            "targets": 8,
                            'render': function (data, type, full, meta) {
                                return data == null ? "進行中" : "已完結";
                            }
                        },
                        {
                            "targets": [9, 10],
                            'render': function (data, type, full, meta) {
                                return data == null ? "N/A" : formatDate(data);
                            }
                        }
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: false,
                    displayLength: 10,
                    lengthChange: true,
                    info: true,
                    paginate: true,
                    destroy: true,
                    "initComplete": function (settings, json) {
                        $("#cellHistory").show();
                    },
                    "order": [[8, "desc"]]
                });
            }

            function getDetail() {

                table = $("#cellHistoryDetail").DataTable({
                    dom: 'lf<"floatright"B>rtip',
                    buttons: [
                        {
                            extend: 'copyHtml5',
                            exportOptions: {
                                columns: ':visible'
                            }
                        },
                        {
                            text: 'Excel',
                            action: function (e, dt, node, config) {
                                var arr = [];
                                var data = dt.rows({filter: 'applied'}).data();

                                data.each(function (value, index) {
                                    arr[index] = value;
                                });

                                var headers = [];
                                $("#cellHistoryDetail th").each(function () {
                                    headers.push($(this).html());
                                });

                                $.fileDownload("../../CellRecordServlet", {
                                    preparingMessageHtml: "We are preparing your report, please wait...",
                                    failMessageHtml: "There was a problem generating your report, please try again.",
                                    httpMethod: "Post",
                                    data: {
                                        PO: $("#PO").val(),
                                        type: $("#type").val(),
                                        lineId: $("#lineId").val(),
                                        minPcs: $("#minPcs").val(),
                                        maxPcs: $("#maxPcs").val(),
                                        startDate: $("#fini_1").val(),
                                        endDate: $("#ffin_1").val(),
                                        action: "FILE_DOWNLOAD",
                                        columnHeader: headers
                                    }
                                });
                                e.preventDefault();

                            }
                        },
                        {
                            extend: 'print',
                            exportOptions: {
                                columns: ':visible'
                            }
                        }
                    ],
                    "processing": true,
                    "serverSide": false,
                    fixedHeader: {
                        headerOffset: 50
                    },
                    "ajax": {
                        "url": "../../CellRecordServlet",
                        "type": "Post",
                        data: {
                            PO: $("#PO").val(),
                            type: $("#type").val(),
                            lineId: $("#lineId").val(),
                            minPcs: $("#minPcs").val(),
                            maxPcs: $("#maxPcs").val(),
                            startDate: $("#fini_1").val(),
                            endDate: $("#ffin_1").val(),
                            action: "SELECT"
                        }
                    },
                    "columns": [
                        {data: "PO"},
                        {data: "Model_name"},
                        {data: "linename"},
                        {data: "diff"},
                        {data: "standard"},
                        {data: "PERCENT"},
                        {data: "pass"},
                        {data: "beginTime"},
                        {data: "endtime"},
                        {data: "beginUser"},
                        {data: "endUser"}
                    ],
                    "columnDefs": [
                        {
                            "targets": 3,
                            'render': function (data, type, full, meta) {
                                return roundDecimal(data, 2);
                            }
                        },
                        {
                            "targets": 5,
                            'render': function (data, type, full, meta) {
                                var percent = (data * 100 < 1 ? getPercent(data, 2) : getPercent(data, 1));
                                return percent + "%";
                            }
                        },
                        {
                            "targets": 6,
                            'render': function (data, type, full, meta) {
                                return data == 1 ? "是" : "否";
                            }
                        },
                        {
                            "targets": [7, 8],
                            'render': function (data, type, full, meta) {
                                return formatDate(data);
                            }
                        }
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: false,
                    displayLength: 10,
                    lengthChange: true,
                    info: true,
                    paginate: true,
                    destroy: true,
//                    stateSave: true,
                    "initComplete": function (settings, json) {
                        $("#cellHistoryDetail").show();
                    },
                    "order": [[1, "asc"], [8, "asc"]]
                });
            }

            function formatDate(dateString) {
                return dateString.substring(0, 16);
            }

            function getPercent(val, precision) {
                return roundDecimal((val * 100), precision == null ? 0 : precision);
            }

            function roundDecimal(val, precision) {
                var size = Math.pow(10, precision);
                return Math.round(val * size) / size;
            }

        </script>
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <div class="container">
            <div class="row">
                <h5 class="alarm">※雙擊表格內容可查看工單完整資訊</h5>
                <h3>Cell桌各線別投入過工單列表</h3>
                <div class="form-inline">
                    <label>篩選日期:從</label>
                    <div class='input-group date' id='beginTime'>
                        <input type="text" id="fini" placeholder="請選擇起始時間"> 
                    </div> 
                    <label>到</label>
                    <div class='input-group date' id='endTime'>
                        <input type="text" id="ffin" placeholder="請選擇結束時間"> 
                    </div>
                    <button id="cellHistoryResearch">確定</button>
                </div>
                <table id="cellHistory" class="display" cellspacing="0" width="100%" style="text-align: center" hidden>
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>lineId</th>
                            <th>工單</th>
                            <th>機種</th>
                            <th>線別</th>
                            <th>類別</th>
                            <th>亮燈頻率</th>
                            <th>投入數量</th>
                            <th>狀態</th>
                            <th>開始時間</th>
                            <th>結束時間</th>
                        </tr>
                    </thead>
                </table>
            </div>
            <div class="row">
                <h3>Cell桌資料擷取紀錄查詢</h3>
                <form class="form-inline">
                    <table id="cellhistoryFilter">
                        <tr>
                            <td colspan="2"><span>請選擇篩選條件(選填)</span></td>
                        </tr>
                        <tr>
                            <td><label>PO</label></td>
                            <td><input type="text" id="PO" placeholder="Please insert your PO"></td>
                        </tr>
                        <tr>
                            <td><label>線別</label></td>
                            <td>
                                <select id="lineId">
                                    <option value="">請選擇線別</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td><label>類別</label></td>
                            <td>
                                <select id="type">
                                    <option value="">請選擇類別</option>
                                    <option value="BAB">Bab</option>
                                    <option value="TEST">TEST</option>
                                    <option value="PKG">Packing</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td><label>Pcs</label></td>
                            <td>
                                <input type="number" id="minPcs" min="1" placeholder="不設定下限請留空" />
                                <input type="number" id="maxPcs" min="1" placeholder="不設定上限請留空" />
                            </td>
                        </tr>
                        <tr>
                            <td><label>結束日期</label></td>
                            <td>
                                <div class='input-group date' id='beginTime'>
                                    <input type="text" id="fini_1" placeholder="請選擇起始時間"> 
                                </div> 
                                <label>到</label>
                                <div class='input-group date' id='endTime'>
                                    <input type="text" id="ffin_1" placeholder="請選擇結束時間"> 
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input type="button" id="send" value="確定" />
                                <input class="form-control" type="reset" id="reset" value="reset" />
                            </td>
                        </tr>
                    </table>
                </form>
                <div>
                    <table id="cellHistoryDetail" class="display" cellspacing="0" width="100%" style="text-align: center" hidden>
                        <thead>
                            <tr>
                                <th>工單</th>
                                <th>機種</th>
                                <th>線別</th>
                                <th>耗時(Min)</th>
                                <th>標工(Min)</th>
                                <th>百分比</th>
                                <th>合格</th>
                                <th>開始時間</th>
                                <th>結束時間</th>
                                <th>開始人員</th>
                                <th>結束人員</th>
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
