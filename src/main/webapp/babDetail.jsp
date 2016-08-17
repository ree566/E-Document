<%-- 
    Document   : babDetailInfo
    Created on : 2016/6/14, 下午 03:18:11
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="images/favicon.ico"/>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <link rel="stylesheet" href="css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" href="css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="css/fixedHeader.dataTables.min.css">
        <link rel="stylesheet" href="//cdn.datatables.net/buttons/1.2.1/css/buttons.dataTables.min.css">
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
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="js/moment.js"></script>
        <script src="js/bootstrap-datetimepicker.min.js"></script>
        <script src="js/jquery.dataTables.min.js"></script>
        <script src="js/dataTables.fixedHeader.min.js"></script>
        <script src="//cdn.datatables.net/buttons/1.2.1/js/dataTables.buttons.min.js"></script>
        <script src="//cdn.datatables.net/buttons/1.2.1/js/buttons.flash.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>
        <script src="//cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/pdfmake.min.js"></script>
        <script src="//cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/vfs_fonts.js"></script>
        <script src="//cdn.datatables.net/buttons/1.2.1/js/buttons.html5.min.js"></script>
        <script src="//cdn.datatables.net/buttons/1.2.1/js/buttons.print.min.js"></script>

        <script>
            var round_digit = 2;

            function getDetail(startDate, endDate) {
                $("#babDetail").DataTable({
                    dom: 'Bfrtip',
                    buttons: [
                        'copy', 'csv', 'excel', 'pdf', 'print'
                    ],
                    "processing": true,
                    "serverSide": false,
                    fixedHeader: {
                        headerOffset: 50
                    },
                    "ajax": {
                        "url": "GetCloseBABInfo",
                        "type": "POST",
                        data: {
                            startDate: startDate,
                            endDate: endDate
                        }
                    },
                    "columns": [
                        {data: "id", visible: false},
                        {data: "PO", width: "50"},
                        {data: "Model_name", width: "50"},
                        {data: "lineName"},
                        {data: "people"},
                        {data: "failPercent"},
                        {data: "total"},
                        {},
                        {data: "btime"}
                    ],
                    "columnDefs": [
                        {
                            "targets": 7,
                            'render': function (data, type, full, meta) {
                                return getPercent(full.failPercent / full.total);
                            }
                        },
                        {
                            "targets": 8,
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
                    filter: true,
                    info: true,
                    paginate: false,
                    destroy: true,
                    "initComplete": function (settings, json) {
                        $("#babDetail").show();
                    },
                    "order": [[8, "desc"]]
                });
            }

            function formatDate(dateString) {
                return dateString.substring(0, 16);
            }

            function getPercent(val) {
                return roundDecimal((val * 100), round_digit);
            }

            function roundDecimal(val, precision) {
                var size = Math.pow(10, precision);
                return Math.round(val * size) / size;
            }

            $(function () {
                var momentFormatString = 'YYYY-MM-DD';
                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");
                var options = {
                    defaultDate: moment(),
                    useCurrent: true,
                    //locale: "zh-tw",
                    format: momentFormatString,
                    extraFormats: [momentFormatString],
                    disabledHours: [0, 1, 2, 3, 4, 5, 6, 7, 18, 19, 20, 21, 22, 23, 24]
                };
                var beginTimeObj = $('#fini').datetimepicker(options);
                var endTimeObj = $('#ffin').datetimepicker(options);

                var table;

                $("#send").click(function () {

                    console.log("I'm clicked");

                    var startDate = $('#fini').val();
                    var endDate = $('#ffin').val();

                    getDetail(startDate, endDate);
                });

                $("body").on('click', '#babDetail tbody tr', function () {
                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                    } else {
                        $('#babDetail tr.selected').removeClass('selected');
                        $(this).addClass('selected');
                    }
                });
            });
        </script>
    </head>
    <body>
        <jsp:include page="admin-header.jsp" />
        <div class="wiget-ctrl form-inline">
            <div style="width:100%">
                <h3>工單明細查詢</h3>
                <table id="leaveRequest" class="table">
                    <tr>
                        <td>
                            <div class="form-group form-inline">
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
                    <p class="alarm">※只會顯示"已完結工單"的資料。</p>
                    <table id="babDetail" class="display" cellspacing="0" width="100%" style="text-align: center" hidden>
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>工單</th>
                                <th>機種</th>
                                <th>線別</th>
                                <th>人數</th>
                                <th>未達標準</th>
                                <th>生產總數</th>
                                <th>亮燈頻率(%)</th>
                                <th>投入時間</th>
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
