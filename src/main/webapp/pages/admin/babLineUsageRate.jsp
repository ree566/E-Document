<%-- 
    Document   : babDetailInfo
    Created on : 2016/6/14, 下午 03:18:11
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<sec:authorize access="isAuthenticated()"  var="isAuthenticated" />
<sec:authorize access="hasRole('BACKDOOR_4876_')"  var="isBackDoor4876" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />">
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
            .title, .subTitle{
                display: inline !important; 
            }
            .subTitle{
                color: red;
            }
            table td{
                text-align: center;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
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
        <script src="<c:url value="/js/param.check.js" />"></script>
        <script src="<c:url value="/js/countermeasure.js" />"></script>
        <script src="<c:url value="/js/pivot.min.js" />"></script>
        <script src="<c:url value="/js/jquery_pivot.js" />"></script>

        <script>
            var table;
            var worktimeInDay = 470;
            var lockDays = 45;
            $(function () {

                var lineObj = initLineObject();
                var momentFormatString = 'YYYY-MM-DD';
                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");

                var options = {
                    defaultDate: moment().subtract(1, "days"),
                    useCurrent: true,
                    //locale: "zh-tw",
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
                    if (dateDiff > lockDays) {
                        endTimeObj.data("DateTimePicker").date(beginDate.add(lockDays, 'days'));
                    }
                });

                endTimeObj.on("dp.change", function (e) {
                    var beginDate = beginTimeObj.data("DateTimePicker").date();
                    var endDate = e.date;
                    var dateDiff = endDate.diff(beginDate, 'days');
                    if (dateDiff > lockDays) {
                        beginTimeObj.data("DateTimePicker").date(endDate.add(-lockDays, 'days'));
                    }
                });

                $(":text").keyup(function () {
                    $(this).val($(this).val().toUpperCase());
                });

                var fields = [
                    // filterable fields
                    {
                        name: 'lineId', type: 'string', filterable: false, pseudo: true,
                        pseudoFunction: function (row) {
                            return lineObj[row.lineId.toString() + "_"].name;
                        }
                    },
                    {
                        name: 'date', type: 'string', filterable: false, pseudo: true, columnLabelable: true,
                        pseudoFunction: function (row) {
                            return row.date;
                        }
                    },
                    {
                        name: 'suspend', type: 'integer', rowLabelable: false, summarizable: 'sum', displayFunction: function (value) {
                            return worktimeInDay - value;
                        }
                    },
                    {
                        name: 'balance', type: 'integer', rowLabelable: false, pseudo: true,
                        pseudoFunction: function (row) {
                            return 0;
                        },
                        summarizable: 'sum'
                    }
                ];

                $("#send").click(function () {
                    getDetail(fields);
                });

            });

            function getDetail(fields) {
                $("#send").attr("disabled", true);

                $.ajax({
                    type: "Get",
                    url: "<c:url value="/BabController/findBabTimeGapPerLine" />",
                    data: {
                        startDate: $('#fini').val(),
                        endDate: $('#ffin').val()
                    },
                    dataType: "json",
                    success: function (response) {
                        var result = response.data;
                        var arr = [];
                        var field = ["lineId", "date", "suspend"];
                        arr.push(field);
                        for (var i = 0; i < result.length; i++) {
                            var row = result[i];
                            var subArr = [
                                row.lineId,
                                row.dateString,
                                row.totalGapsTimeInDay
                            ];
                            arr.push(subArr);
                        }
                        setupPivot({
                            json: arr,
                            fields: fields,
                            rowLabels: ["date"],
                            columnLabels: ["lineId"],
                            summaries: ["suspend"]
                        });
                        $("#send").removeAttr("disabled");
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.responseText);
                    }
                });

            }

            function initLineObject() {
                var result = [];
                $.ajax({
                    type: "GET",
                    url: "<c:url value="/BabLineController/findAll" />",
                    dataType: "json",
                    async: false,
                    success: function (response) {
                        for (var i = 0; i < response.length; i++) {
                            var o = response[i];
                            result[o.id.toString() + "_"] = o;
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showMsg(xhr.responseText);
                    }
                });
                return result;
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

            function setupPivot(input) {
                input.callbacks = {afterUpdateResults: function () {
                        //tfoot not found issus, need to add this line 
                        $('#results > table').addClass("table-hover");
                        $('#results > table').append("<tfoot id='foot'></tfoot>");

                        $('#results > table').DataTable({
                            dom: 'Bfrtip',
                            buttons: [
                                'copy',
                                {
                                    extend: 'excelHtml5',
                                    footer: true
                                },
                                'print'
                            ],
                            fixedHeader: {
                                headerOffset: 50
                            },
                            "oLanguage": {
                                "sLengthMenu": "顯示 _MENU_ 筆記錄",
                                "sZeroRecords": "無符合資料",
                                "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                            },
                            displayLength: -1,
                            filter: false,
                            paginate: false,
                            "footerCallback": function (row, data, start, end, display) {
                                var api = this.api();
                                var rcnt = 0;
                                var sum3Total = 0;
                                api.columns().every(function () {
                                    var o = this;
                                    var sum = o
                                            .data()
                                            .reduce(function (a, b) {
                                                var x = parseFloat(a) || 0;
                                                var y = parseFloat(b) || 0;
                                                return x + y;
                                            }, 0);

                                    var sum2 = worktimeInDay * o.data().count();

                                    var sum3 = getPercent(sum / sum2);
                                    if (rcnt == 0) {
                                        $("#foot").append('<tr><td style="background:#a1eaed;color:black; text-align: center;"><b>實際時間</b></td></tr>')
                                                .append('<tr><td style="background:#a1eaed;color:black; text-align: center;"><b>應執行時間</b></td></tr>')
                                                .append('<tr><td style="background:#a1eaed;color:black; text-align: center;"><b>百分比</b></td></tr>');
                                    } else {
                                        sum3Total = sum3Total + (sum / sum2);
                                        $("#foot tr:eq(0)").append('<td style="background:#a1eaed;color:black; text-align: center;"><b>' + sum + '</b></td>');
                                        $("#foot tr:eq(1)").append('<td style="background:#a1eaed;color:black; text-align: center;"><b>' + sum2 + '</b></td>');
                                        $("#foot tr:eq(2)").append('<td style="background:#a1eaed;color:black; text-align: center;"><b>' + sum3 + '</b></td>');
                                    }
                                    rcnt++;
                                });
                                $("#foot").append('<tr><td><b>Total百分比</b></td></tr>');
                                $("#foot tr:eq(3)").append('<td colspan=' + (rcnt - 1) + '><b>' + getPercent(sum3Total / (rcnt - 1)) + '</b></td>');
                            }
                        });

                    }};
                $('#pivot-table').pivot_display('setup', input);
            }

        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />
        <div class="container-fluid">
            <div>
                <h3 class="title">線體使用率查詢</h3>
                <h5 class="subTitle">※最多顯示30天紀錄</h5>
            </div>
            <div class="row form-inline">
                <div class="col form-group">
                    <label for="beginTime">日期: 從</label>
                    <div class='input-group date' id='beginTime'>
                        <input type="text" id="fini" placeholder="請選擇起始時間"> 
                    </div> 
                </div>
                <div class="col form-group">
                    <label for="endTime"> 到 </label>
                    <div class='input-group date' id='endTime'>
                        <input type="text" id="ffin" placeholder="請選擇結束時間"> 
                    </div>
                </div>
                <div class="col form-group">
                    <input type="button" id="send" value="確定(Ok)">
                </div>
            </div>

            <div class="row">
                <table id="BabDetail" class="table table-striped" cellspacing="0" width="100%" style="text-align: center" hidden>
                </table>
            </div>

            <div class="row">
                <!--                <table id="results" class="table table-striped" cellspacing="0" width="100%">
                                </table>-->
                <div id="results"></div>
            </div>
        </div>

        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
