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
            .title, .subTitle{
                display: inline !important; 
            }
            .subTitle{
                color: red;
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
        <script src="<c:url value="/js/param.check.js" />"></script>
        <script src="<c:url value="/js/ajax-option-select-loader/babLineType.loader.js" />"></script>
        <script src="<c:url value="/js/countermeasure.js" />"></script>

        <script>
            var table;
            lineTypeLoaderUrl = "<c:url value="/BabLineController/findLineType" />";
            $(function () {
                setLineObject();
                initOptions($("#lineType"));
                
                initCountermeasureDialog({
                    queryUrl: "<c:url value="/CountermeasureController/findByBab" />",
                    saveUrl: "<c:url value="/CountermeasureController/update" />",
                    actionCodeQueryUrl: null,
                    errorCodeQueryUrl: null
                }, table, "Bab_Best_LineProductivity_Sop_Record", '${isAuthenticated ? user.jobnumber : null}');

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

                var lockDays = 180;
                beginTimeObj.on("dp.change", function (e) {
                    endTimeObj.data("DateTimePicker").minDate(e.date);
                    var beginDate = e.date;
                    var endDate = endTimeObj.data("DateTimePicker").date();
                    var dateDiff = endDate.diff(beginDate, 'days');
                    if (dateDiff > 30) {
                        endTimeObj.data("DateTimePicker").date(beginDate.add(lockDays, 'days'));
                    }
                });

                endTimeObj.on("dp.change", function (e) {
                    var beginDate = beginTimeObj.data("DateTimePicker").date();
                    var endDate = e.date;
                    var dateDiff = endDate.diff(beginDate, 'days');
                    if (dateDiff > 30) {
                        beginTimeObj.data("DateTimePicker").date(endDate.add(-lockDays, 'days'));
                    }
                });

                $(":text").keyup(function () {
                    $(this).val($(this).val().toUpperCase());
                });

                var urlLineType = getQueryVariable("lineType");

                if (urlLineType != null) {
                    $("#lineType").val($('#lineType option:contains(' + urlLineType + ')').val());
                }

                $("#send").click(getDetail);
                
                if (${!isAuthenticated}) {
                    $("#editCountermeasure").off("click").attr("disabled", true);
                    $("#countermeasureEditHint").show();
                }
            });

            function getDetail() {
                $("#send").attr("disabled", true);
                table = $("#BabDetail").DataTable({
                    "processing": true,
                    "serverSide": false,
                    fixedHeader: {
                        headerOffset: 50
                    },
                    "ajax": {
                        "url": "<c:url value="/SqlViewController/findBabBestLineBalanceRecord" />",
                        "type": "Get",
                        data: {
                            lineType_id: $("#lineType").val(),
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
                        {data: "people"},
                        {data: "line_name"},
                        {data: "btime"},
                        {data: "failPcs"},
                        {data: "totalPcs"},
                        {data: "balance"},
                        {data: "sopRecord_id"}
                    ],
                    "columnDefs": [
                        {
                            "type": "html",
                            "targets": [5],
                            'render': function (data, type, full, meta) {
                                return data == null ? 'n/a' : formatDate(data);
                            }
                        },
                        {
                            "type": "html",
                            "targets": [9],
                            'render': function (data, type, full, meta) {
                                if (data) {
                                    return "<input type='button' class='cm-detail btn btn-info btn-sm' data-toggle= 'modal' data-target='#myModal' value='檢視詳細' />";
                                } else {
                                    return "<input type='button' class='cm-detail btn btn-danger btn-sm' data-toggle= 'modal' data-target='#myModal' value='檢視詳細' />";
                                }
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
                    "order": [[1, "asc"]]
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

            function roundDecimal(val, precision) {
                var size = Math.pow(10, precision);
                return Math.round(val * size) / size;
            }
        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />

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
                                    <td class="lab">說明</td>
                                    <td id="errorCon" >
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lab">SOP資訊</td>
                                    <td>
                                        <label id="sopHint" hidden="">※請輸入完整Sop名稱及頁數:</label>
                                        <div id="sop"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lab">最後修改人員</td>
                                    <td id="responseUser" >
                                    </td>
                                </tr>
                            </table>
                            <div id="dialog-msg" class="alarm"></div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <label id="countermeasureEditHint" for="editCountermeasure" hidden="">請<a href="<c:url value="/login" /> " target='_parent'>登入</a>做異常回覆</label>
                        <button type="button" id="editCountermeasure" class="btn btn-default" >Edit</button>
                        <button type="button" id="saveCountermeasure" class="btn btn-default">Save</button>
                        <button type="button" id="undoContent" class="btn btn-default">Undo</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>

        <div class="container">
            <div>
                <h3 class="title">機種最佳線平衡sop查詢</h3>
            </div>
            <div class="row form-inline">

                <div class="col form-group">
                    <select id="lineType" class="form-control">
                        <option value="-1">請選擇製程</option>
                    </select>
                </div>

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
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>工單</th>
                            <th>機種</th>
                            <th>人數</th>
                            <th>線別</th>
                            <th>開始</th>
                            <th>failPcs</th>
                            <th>totalPcs</th>
                            <th>balance</th>
                            <th>sopRecord_id</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>

        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
