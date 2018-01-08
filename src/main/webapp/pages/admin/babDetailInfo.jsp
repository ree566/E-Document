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
        <link rel="shortcut icon" href="../../images/favicon.ico"/>
        <link rel="stylesheet" href="../../css/jquery-ui.css">
        <link rel="stylesheet" href="../../css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" href="../../css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="../../css/fixedHeader.dataTables.min.css">
        <link rel="stylesheet" href="../../css/buttons.dataTables.min.css">
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
        <script src="../../js/jquery-1.11.3.min.js"></script>
        <script src="../../js/jquery-ui-1.10.0.custom.min.js"></script>
        <script src="../../js/moment.js"></script>
        <script src="../../js/bootstrap-datetimepicker.min.js"></script>
        <script src="../../js/jquery.dataTables.min.js"></script>
        <script src="../../js/dataTables.fixedHeader.min.js"></script>
        <script src="../../js/jquery-datatable-button/dataTables.buttons.min.js"></script>
        <script src="../../js/jquery-datatable-button/buttons.flash.min.js"></script>
        <script src="../../js/jquery-datatable-button/jszip.min.js"></script>
        <script src="../../js/jquery-datatable-button/pdfmake.min.js"></script>
        <script src="../../js/jquery-datatable-button/vfs_fonts.js"></script>
        <script src="../../js/jquery-datatable-button/buttons.html5.min.js"></script>
        <script src="../../js/jquery-datatable-button/buttons.print.min.js"></script>

        <script>
            $(function () {
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
                    var Model_name = $('#Model_name').val();
                    var startDate = $('#fini').val();
                    var endDate = $('#ffin').val();

                    if (Model_name == null || Model_name.trim() == "") {
                        alert("請輸入工單號碼");
                        return false;
                    }

                    table = $("#table1").DataTable({
                        "processing": false,
                        "serverSide": false,
                        "ajax": {
                            "url": "<c:url value="/BabController/findByModelAndDate" />",
                            "type": "Get",
                            data: {
                                modelName: Model_name,
                                startDate: startDate,
                                endDate: endDate
                            }
                        },

                        "columns": [
                            {data: "id", visible: false},
                            {data: "po", width: "50px"},
                            {data: "line.name", width: "50px"},
                            {data: "people", width: "50px"},
                            {data: "babStatus", width: "50px"},
                            {data: "beginTime", width: "50px"}
                        ],
                        "columnDefs": [
                            {
                                "type": "html",
                                "targets": 4,
                                'render': function (data, type, full, meta) {
                                    return data == null ? "PROCESSING" : data;
                                }
                            },
                            {
                                "type": "html",
                                "targets": 5,
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
                        bAutoWidth: true,
                        displayLength: -1,
                        lengthChange: false,
                        filter: false,
                        info: false,
                        paginate: false,
                        destroy: true,
                        "initComplete": function (settings, json) {
                            $("#table1").show();
                        },
                        "order": [[5, "desc"]]
                    });
                });

                $("body").on('dblclick', '#table1 tbody tr', function () {
                    var selectData = table.row(this).data();
                    var BABid = selectData.id;
                    var ModelName = selectData.modelName;
                    var babStatus = selectData.babStatus;

                    if (babStatus == "NO_RECORD") {
                        alert("此筆記錄無統計數據。");
                        return;
                    }

//                    block();

                    $("#Model_name").val(ModelName);

                    getDetail(BABid, babStatus);

                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                    } else {
                        table.$('tr.selected').removeClass('selected');
                        $(this).addClass('selected');
                    }
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
            });

            function getDetail(BABid, babStatus) {
                $("#BabDetail").DataTable({
                    dom: 'Bfrtip',
                    buttons: [
                        'copy', 'excel', 'print'
                    ],
                    "processing": false,
                    "serverSide": false,
                    fixedHeader: {
                        headerOffset: 50
                    },
                    "ajax": {
                        "url": "<c:url value="/BabController/findPcsDetail" />",
                        "type": "Get",
                        data: {
                            id: BABid,
                            babStatus: babStatus
                        }
                    },
                    "columns": [
                        {data: "bab.id", visible: false},
                        {data: "tagName"},
                        {data: "groupid"},
                        {data: "diff"},
                        {data: "lastUpdateTime"}
                    ],
                    "columnDefs": [
                        {
                            "type": "html",
                            "targets": 4,
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
                    bAutoWidth: true,
                    displayLength: -1,
                    lengthChange: false,
                    filter: false,
                    info: false,
                    paginate: false,
                    destroy: true,
                    "initComplete": function (settings, json) {
                        $("#BabDetail").show();
                    },
                    "order": [[2, "asc"], [1, "asc"]]
                });
            }

            function formatDate(dateString) {
                return moment(dateString).format('YYYY-MM-DD hh:mm');
            }
        </script>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div class="container form-inline">
            <div style="width:100%">
                <h3>工單明細查詢</h3>
                <table id="leaveRequest" class="table">
                    <tr>
                        <td>
                            <div class="form-group form-inline">
                                <input type="text" id="Model_name" placeholder="請輸入機種" />
                                日期:從
                                <div class='input-group date' id='beginTime'>
                                    <input type="text" id="fini" placeholder="請選擇起始時間"> 
                                </div> 
                                到 
                                <div class='input-group date' id='endTime'>
                                    <input type="text" id="ffin" placeholder="請選擇結束時間"> 
                                </div>
                                <input type="button" id="send" value="確定(Ok)">
                            </div>
                        </td>
                    </tr>
                </table>
                <div style="width:100%">
                    <h3>符合條件之工單列表</h3>
                    <p class="alarm">※雙擊表格內的內容可直接於下方帶出資料。</p>
                    <table id="table1" class="display" cellspacing="0" width="100%" style="text-align: center" hidden>
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>工單</th>
                                <th>線別</th>
                                <th>人數</th>
                                <th>狀態</th>
                                <th>投入時間</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <hr />
                <div style="width:100%">
                    <h3>各站紀錄</h3>
                    <table id="BabDetail" class="table table-striped" cellspacing="0" width="100%" style="text-align: center" hidden>
                        <thead>
                            <tr>
                                <th>BABid</th>
                                <th>感應器名稱(站別  )</th>
                                <th>組別</th>
                                <th>花費時間(秒)</th>
                                <th>過感應器時間</th>
                            </tr>
                        </thead>
                    </table>
                </div>

                <div id="serverMsg"></div>
            </div>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
