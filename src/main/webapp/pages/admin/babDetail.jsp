<%-- 
    Document   : babDetailInfo
    Created on : 2016/6/14, 下午 03:18:11
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="cDAO" class="com.advantech.model.CountermeasureDAO" scope="application" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../../images/favicon.ico"/>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <link rel="stylesheet" href="../../css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" href="../../css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="../../css/fixedHeader.dataTables.min.css">
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
            #babDetail th {
                max-width: 100px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }
            .dt-index {
                column-width: auto !important;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="../../js/moment.js"></script>
        <script src="../../js/bootstrap-datetimepicker.min.js"></script>
        <script src="../../js/jquery.dataTables.min.js"></script>
        <script src="../../js/dataTables.fixedHeader.min.js"></script>

        <script>
            var round_digit = 2;
            var table;
            var actionCodes;
            var checkBoxs;

            $(function () {
                actionCodes = getActionCode();
                checkBoxs = $("#actionCode > div").detach();
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

                $("#send").click(function () {

                    console.log("I'm clicked");

                    var startDate = $('#fini').val();
                    var endDate = $('#ffin').val();

                    table = getDetail(startDate, endDate);

                });

                $("body").on('click', '#babDetail tbody tr', function () {
                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                    } else {
                        $('#babDetail tr.selected').removeClass('selected');
                        $(this).addClass('selected');
                    }
                });

                $("#generateExcel").click(function () {
                    var startDate = $('#fini').val();
                    var endDate = $('#ffin').val();

                    window.location.href = '../../TestServlet?startDate=' + startDate + '&endDate=' + endDate;

                });

                $("body").on('click', '#babDetail input[type="button"]', function () {
                    console.log("click");

                    var selectData = table.row($(this).parents('tr')).data();

                    console.log(selectData);

                    editId = selectData.id;
//                    console.log(selectData.cm_id);
                    $("#myModal #titleMessage").html(
                            "號碼: " + editId +
                            " / 工單: " + selectData.PO +
                            " / 機種: " + selectData.Model_name +
                            " / 線別: " + selectData.lineName +
                            " / 時間: " + formatDate(selectData.btime)
                            );

                    getContermeasure(selectData.id);

                });

                $('#myModal').on('shown.bs.modal', function () {
                    $(this).find('.modal-dialog').css({width: '90%',
                        height: 'auto',
                        'max-height': '100%'});
                });
            });

            function getDetail(startDate, endDate) {
                var alarmPercentStandard = 0.3;
                var table = $("#babDetail").DataTable({
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
                            action: "getBab"
                        }
                    },
                    "columns": [
                        {data: "id", visible: false},
                        {data: "PO"},
                        {data: "Model_name"},
                        {data: "linetype"},
                        {data: "lineName"},
                        {data: "people"},
                        {data: "qty"},
                        {},
                        {data: "btime"},
                        {}
                    ],
                    "columnDefs": [
                        {
                            "targets": 7,
                            'render': function (data, type, row, meta) {
                                return getPercent(row.failPercent / row.total);
                            }
                        },
                        {
                            "targets": 8,
                            'render': function (data, type, row, meta) {
                                return formatDate(data);
                            }
                        },
                        {
                            "targets": 9,
                            'render': function (data, type, row) {
                                var isAboveApStandard = row.failPercent / row.total > alarmPercentStandard;
                                if(isAboveApStandard){
                                    return "<input type='button' class='btn btn-default' data-toggle='modal' data-target='#myModal' value='檢視詳細'>";
                                }else{
                                    return "達到標準";
                                }
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
                return table;
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

            function initCountermeasureDialog() {
                console.log("init dialog");
                $(".modal-body #errorCon, #responseUser").html("N/A");
                $("input[name='errorCode']").prop("checked", false);
                $('input[name="actionCode"]').prop("checked", false);
                $(" #responseUser").html("");
                $(".modal-body :checkbox").attr("disabled", true);
                setupCheckBox();
                showDialogMsg("");
            }

            function getContermeasure(BABid) {
                console.log("generate dialog");
                initCountermeasureDialog();

                $.ajax({
                    url: "../../CountermeasureServlet",
                    data: {
                        BABid: BABid,
                        action: "selectOne"
                    },
                    type: "POST",
                    dataType: 'json',
                    success: function (msg) {
                        var jsonData = msg;
                        var errorCodes = msg.errorCodes;
                        if (errorCodes != null) {
                            $(".modal-body #errorCon").html(jsonData.solution.replace(/(?:\r\n|\r|\n)/g, '<br />'));
                            var checkedErrorCodes = [], checkedActionCodes = [];

                            for (var i = 0; i < errorCodes.length; i++) {
                                var obj = errorCodes[i];
                                checkedErrorCodes.push(obj.ec_id);
                                checkedActionCodes.push(obj.ac_id);
                            }

                            setErrorCodeCheckBox(checkedErrorCodes);
                            setupCheckBox();
                            setActionCodeCheckBox(checkedActionCodes);

                            $(":checkbox").attr("disabled", true);

                            var editors = jsonData.editors;
                            for (var i = 0; i < editors.length; i++) {
                                var editor = editors[i].editor;
                                $(".modal-body #responseUser").append("<span class='label label-default'>#" + (editor == null ? 'N/A' : editor) + "</span> ");
                            }
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showDialogMsg(xhr.responseText);
                    }
                });
            }

            function setupCheckBox() {

                $("#actionCode").html("");
                var data = actionCodes.data;
                var array = $.map($('input[name="errorCode"]:checked'), function (c) {
                    return c.value;
                });

                for (var i = 0, j = array.length; i < j; i++) {
                    var id = array[i];
                    for (var k = 0, l = data.length; k < l; k++) {
                        if (data[k].ec_id == id) {
                            var checkboxObj = checkBoxs.clone();
                            checkboxObj.addClass("ec" + id);
                            checkboxObj.find(":checkbox").attr("value", data[k].id).after(data[k].name);
                            $("#actionCode").append(checkboxObj);
                        }
                    }
                }
            }

            function getActionCode() {
                var result;
                $.ajax({
                    url: "../../CountermeasureServlet",
                    data: {action: "getActionCode"},
                    type: "POST",
                    dataType: "json",
                    async: false,
                    success: function (response) {
                        result = response;
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.responseText);
                    }
                });
                return result;
            }

            function setErrorCodeCheckBox(errorCodes) {
                for (var i = 0; i < errorCodes.length; i++) {
                    $('input[name="errorCode"][value=' + errorCodes[i] + ']').prop("checked", true);
                }

            }

            function setActionCodeCheckBox(actionCodes) {
                for (var i = 0; i < actionCodes.length; i++) {
                    $('input[name="actionCode"][value=' + actionCodes[i] + ']').prop("checked", true);
                }
            }

            function tableAjaxReload(tableObject) {
                tableObject.ajax.reload();
            }

            function showDialogMsg(msg) {
                $("#dialog-msg").html(msg);
            }
        </script>
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 id="titleMessage" class="modal-title"></h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <table id="countermeasureTable" cellspacing="10" class="table table-bordered">
                                <tr>
                                    <td class="lab">Error Code</td>
                                    <td id="errorCode"> 
                                        <div class="checkbox">
                                            <c:forEach var="errorCode" items="${cDAO.getErrorCode()}">
                                                <label class="checkbox-inline">
                                                    <input type="checkbox" name="errorCode" value="${errorCode.id}">${errorCode.name}
                                                </label>
                                            </c:forEach>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lab">Action Code</td>
                                    <td id="actionCode">
                                        <div class="checkbox">
                                            <label class="checkbox-inline">
                                                <input type="checkbox" name="actionCode">
                                            </label>
                                        </div>
                                        <%--</c:forEach>--%>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lab">說明</td>
                                    <td id="errorCon">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lab">填寫人</td>
                                    <td id="responseUser">
                                    </td>
                                </tr>
                            </table>
                            <div id="dialog-msg" class="alarm"></div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <!--d-->
        <div class="wiget-ctrl form-inline">
            <div style="width:100%">
                <h3>工單明細查詢</h3>
                <table class="table">
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
                                <input type="button" id="generateExcel" value="產出excel">
                            </div>
                        </td>
                    </tr>
                </table>
                <div style="width:100%">
                    <h3>符合條件之工單列表</h3>
                    <p class="alarm">※只會顯示"已完結工單"的資料。</p>
                    <p class="alarm">※亮燈頻率只是"近似值"，並非代表"生產總數量"的亮燈頻率。</p>
                    <table id="babDetail" class="display" cellspacing="0" width="100%" style="text-align: center" hidden>
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>工單</th>
                                <th>機種</th>
                                <th>站別</th>
                                <th>線別</th>
                                <th>生產人數</th>
                                <th>生產總數量</th>
                                <th>亮燈頻率(%)</th>
                                <th>投入時間</th>
                                <th>異常對策</th>
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
