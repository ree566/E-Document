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
        <script src="../../js/urlParamGetter.js"></script>
        <script>
            var maxProductivity = 200;
            var table;
            $(document).ready(function () {

                var momentFormatString = 'YYYY-MM-DD';
                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");
                var options = {
                    useCurrent: true,
                    //locale: "zh-tw",
                    format: momentFormatString,
                    extraFormats: [momentFormatString]
                };
                
                //以免把時間區間定在六日沒有資料，此狀況會在moment是禮拜一時發生
                var prevDay = moment().add(-2, "days");
                if (prevDay.day() == 6 || prevDay.day() == 7) {
                    prevDay.add(-2, "days");
                }
                
                options.defaultDate = prevDay;
                $('#fini').datetimepicker(options);
                options.defaultDate = moment();
                $('#ffin').datetimepicker(options);

                $("#send").click(function () {

                    console.log("I'm clicked");

                    var startDate = $('#fini').val();
                    var endDate = $('#ffin').val();

                    getDetail(startDate, endDate);
                });

                $("body").on('click', '#testDetail tbody tr', function () {
                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                    } else {
                        $('#testDetail tr.selected').removeClass('selected');
                        $(this).addClass('selected');
                    }
                });

                var jobnumber = getQueryVariable("jobnumber");
                if (jobnumber != null) {
                    $("#send").trigger("click");
                    table.search(decodeURIComponent(jobnumber)).draw();
                }
            });

            function getDetail(startDate, endDate) {

                table = $("#testDetail").DataTable({
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
                        "url": "../../GetClosedInfo",
                        "type": "POST",
                        data: {
                            startDate: startDate,
                            endDate: endDate,
                            action: "getTest"
                        }
                    },
                    "columns": [
                        {data: "id", visible: false},
                        {data: "user_id"},
                        {data: "user_name"},
                        {data: "productivity"},
                        {data: "table_id"},
                        {data: "saveTime"}
                    ],
                    "columnDefs": [
                        {
                            "targets": 3,
                            'render': function (data, type, full, meta) {
                                var productivity = getPercent(data);
                                return (productivity > maxProductivity ? maxProductivity : productivity) + "%";
                            }
                        },
                        {
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
                    bAutoWidth: false,
                    displayLength: -1,
                    lengthChange: false,
                    filter: true,
                    info: true,
                    paginate: false,
                    destroy: true,
                    "initComplete": function (settings, json) {
                        $("#testDetail").show();
                    },
                    "order": [[5, "asc"], [1, "asc"]]
                });
            }

            function formatDate(dateString) {
                return dateString.substring(0, 16);
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
            <h3>測試線別資料擷取紀錄查詢</h3>
            <div>
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
            </div>
            <div>
                <table id="testDetail" class="display" cellspacing="0" width="100%" style="text-align: center" hidden>
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>人員工號</th>
                            <th>人員姓名</th>
                            <th>效率</th>
                            <th>桌次</th>
                            <th>儲存時間</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
