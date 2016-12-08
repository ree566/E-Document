<%-- 
    Document   : test
    Created on : 2015/11/20, 上午 11:36:50
    Author     : Wei.Cheng
https://datatables.net/forums/discussion/20388/trying-to-access-rowdata-in-render-function-with-ajax-datasource-getting-undefined
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <script src="../../js/urlParamGetter.js"></script>
        <script>
            var maxProductivity = 200;
            var table;
            $(document).ready(function () {

                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");

                $("#send").click(function () {
                    getDetail();
                });

                $("body").on('click', '#cellHistoryDetail tbody tr', function () {
                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                    } else {
                        $('#cellHistoryDetail tr.selected').removeClass('selected');
                        $(this).addClass('selected');
                    }
                });
            });

            function getDetail() {

                table = $("#cellHistoryDetail").DataTable({
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
                        "url": "../../CellRecordServlet",
                        "type": "Get",
                        data: {
                            PO: $("#PO").val(),
                            minPcs: $("#minPcs").val(),
                            maxPcs: $("#maxPcs").val()
                        }
                    },
                    "columns": [
                        {data: "barcode"},
                        {data: "PO"},
                        {data: "linename"},
                        {data: "diffTime"},
                        {data: "diff"},
                        {data: "standard"},
                        {data: "PERCENT"},
                        {data: "beginTime"},
                        {data: "endtime"}
                    ],
                    "columnDefs": [
                        {
                            "targets": 4,
                            'render': function (data, type, full, meta) {
                                return roundDecimal(data, 2);
                            }
                        },
                        {
                            "targets": 6,
                            'render': function (data, type, full, meta) {
                                var percent = getPercent(data);
                                return percent + "%";
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
                    displayLength: -1,
                    lengthChange: false,
                    filter: false,
                    info: true,
                    paginate: false,
                    destroy: true,
                    "initComplete": function (settings, json) {
                        $("#cellHistoryDetail").show();
                    },
                    "order": [[8, "asc"]]
                });
            }

            function formatDate(dateString) {
                return dateString.substring(0, 19);
            }

            function getPercent(val) {
                return roundDecimal((val * 100), 0);
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
            <h3>Cell桌資料擷取紀錄查詢</h3>
            <div class="container form-inline">
                <div>
                    <label for="PO">Please insert your PO:</label>
                    <input type="text" id="PO" placeholder="PO insert here">
                    <label for="amount">MinPcs:</label>
                    <input type="number" id="minPcs" min="1" placeholder="不設定下限請留空" />
                    <label for="amount">MaxPcs:</label>
                    <input type="number" id="maxPcs" min="1" placeholder="不設定上限請留空" />
                    <input type="button" id="send" value="send" />
                </div>
            </div>
            <div>
                <table id="cellHistoryDetail" class="display" cellspacing="0" width="100%" style="text-align: center" hidden>
                    <thead>
                        <tr>
                            <th>barcode</th>
                            <th>PO</th>
                            <th>line</th>
                            <th>time spent(Full)</th>
                            <th>time spent(Min)</th>
                            <th>standard(Min)</th>
                            <th>spent percent</th>
                            <th>begin time</th>
                            <th>end time</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
