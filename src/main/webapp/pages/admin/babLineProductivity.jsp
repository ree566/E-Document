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

        <script>
            $(function () {

                initLineObject();
                var momentFormatString = 'YYYY-MM-DD';
                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");
                var options = {
                    defaultDate: moment(),
                    useCurrent: true,
                    //locale: "zh-tw",
                    format: momentFormatString,
                    extraFormats: [momentFormatString]
                };

                var beginTimeObj = $('#fini').datetimepicker(options);
                var endTimeObj = $('#ffin').datetimepicker(options);

                var table;

                $("#send").click(function () {
                    getDetail();
                });

                $.ajax({
                    type: "Get",
                    url: "<c:url value="/BabController/findAllModelName" />",
                    dataType: "json",
                    success: function (data) {
                        $("#modelName").autocomplete({
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

                var urlLineType = getQueryVariable("lineType");

                if (urlLineType != null) {
                    $("#lineType").val(urlLineType);
                }


            });

            function getDetail(modelName, lineType, startDate, endDate) {
                $("#send").attr("disabled", true);
                $("#BabDetail").DataTable({
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
                        "url": "<c:url value="/SqlViewController/findBabLineProductivity" />",
                        "type": "Get",
                        data: {
                            po: $("#po").val(),
                            modelName: $("#modelName").val(),
                            line_id: $("#line").val(),
                            startDate: $('#fini').val(),
                            endDate: $('#ffin').val()
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(xhr.responseText);
                        }
                    },
                    "columns": [
                        {data: "id"},
                        {data: "po"},
                        {data: "modelName"},
                        {data: "lineName"},
                        {data: "lineType"},
                        {data: "sitefloor"},
                        {data: "people"},
                        {data: "btime"},
                        {data: "lastUpdateTime"},
                        {data: "totalPcs"},
                        {data: "standardTime"},
                        {data: "timeCost"},
                        {data: "productivity"}
                    ],
                    "columnDefs": [
                        {
                            "type": "html",
                            "targets": [7, 8],
                            'render': function (data, type, full, meta) {
                                return formatDate(data);
                            }
                        },
                        {
                            "type": "html",
                            "targets": [12],
                            'render': function (data, type, full, meta) {
                                return getPercent(data);
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
                    lengthChange: true,
                    "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
                    filter: false,
                    info: true,
                    paginate: true,
                    pageLength: 10,
                    destroy: true,
                    "initComplete": function (settings, json) {
                        $("#send").attr("disabled", false);
                        $("#BabDetail").show();
                    },
                    "order": [[0, "asc"]]
                });
            }

            function initLineObject() {
                $.ajax({
                    type: "GET",
                    url: "<c:url value="/BabLineController/findAll" />",
                    dataType: "json",
                    success: function (response) {
                        var lineWiget = $("#line");
                        var arr = response;
                        for (var i = 0; i < arr.length; i++) {
                            var line = arr[i];
                            lineWiget.append("<option value=" + line.id + ">" + line.name + "</option>");
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showMsg(xhr.responseText);
                    }
                });
            }

            function formatDate(dateString) {
                return moment(dateString).format('YYYY-MM-DD HH:mm');
            }
            
            function getPercent(val) {
                return roundDecimal((val * 100), 2) + '%';
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
                <h3>線體效率查詢</h3>
                <table id="leaveRequest" class="table">
                    <tr>
                        <td>
                            <div class="form-group form-inline">
                                <input type="text" id="po" placeholder="工單"/>
                                <input type="text" id="modelName" placeholder="機種"/>
                                <select id="line">
                                    <option value="-1">請選擇線別</option>
                                </select>
                                <label for="beginTime">日期: 從</label>
                                <div class='input-group date' id='beginTime'>
                                    <input type="text" id="fini" placeholder="請選擇起始時間"> 
                                </div> 
                                <label for="endTime"> 到 </label>
                                <div class='input-group date' id='endTime'>
                                    <input type="text" id="ffin" placeholder="請選擇結束時間"> 
                                </div>
                                <input type="button" id="send" value="確定(Ok)">
                            </div>
                        </td>
                    </tr>
                </table>
                <div style="width:100%">
                    <h3>各站紀錄</h3>
                    <table id="BabDetail" class="table table-striped" cellspacing="0" width="100%" style="text-align: center" hidden>
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>工單</th>
                                <th>機種</th>
                                <th>線別</th>
                                <th>類別</th>
                                <th>樓層</th>
                                <th>人數</th>
                                <th>開始</th>
                                <th>結束</th>
                                <th>數量</th>
                                <th>標工</th>
                                <th>耗時</th>
                                <th>效率</th>
                            </tr>
                        </thead>
                    </table>
                </div>

                <div id="serverMsg"></div>
            </div>
        </div>
        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
