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
            var tableMap = new Map();
            var userMap = new Map();
            var urlLineType;
            $(function () {
                urlLineType = getQueryVariable("lineType");
                
                //Find all user and perform user select options
                //Let user create station

                var momentFormatString = 'YYYY-MM-DD';
                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");

                $(":text").keyup(function () {
                    $(this).val($(this).val().toUpperCase());
                });

                var options = {
                    defaultDate: moment(),
                    useCurrent: true,
                    //locale: "zh-tw",
                    format: momentFormatString,
                    extraFormats: [momentFormatString]
                };

                $('#fini').datetimepicker(options);

                setUserOptions();
                $("#send").click(getDetail);

                //hook up event for edit record buttons
                $(document).on('click', '.editButton', function (event) { //any element with the class EditButton will be handled here
                    var table = tableMap.get($(this).parents("table").attr("id"));
                    var data = table.row($(this).parents('tr')).data();

                    $("#editRemark .currentID").val(data.id);
                    $("#editRemark .user_id").val(data.user.id);
                    $("#editRemark .currentLineID").val(data.line.id);
                    $("#editRemark .station").val(data.station);

                    $('#editRemark').modal('show');
                });

                $(document).on('click', '#addRemarkButton', function (event) { //any element with the class EditButton will be handled here

                    var user_id = $("#addRemark .user_id").val();
                    var line_id = $("#addRemark .currentLineID").val();
                    var station = $("#addRemark .station").val();

                    $.ajax({
                        method: "POST",
                        url: "<c:url value="/LineUserReferenceController/insert" />",
                        dataType: "html",
                        data: {
                            "line.id": line_id,
                            "user.id": user_id,
                            "station": station,
                            onboardDate: $("#fini").val()
                        },
                        success: function (response) {
                            if (response == "success") {
                                $('#addRemark').modal('hide');
                                var table = tableMap.get("tb" + line_id);
                                table.ajax.reload();
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(xhr.responseText);
                        }
                    });
                });

                $(document).on('click', '#editRemarkButton', function (event) { //any element with the class EditButton will be handled here

                    var id = $("#editRemark .currentID").val();
                    var line_id = $("#editRemark .currentLineID").val();
                    var user_id = $("#editRemark .user_id").val();
                    var station = $("#editRemark .station").val();

                    $.ajax({
                        method: "POST",
                        url: "<c:url value="/LineUserReferenceController/update" />",
                        dataType: "html",
                        data: {
                            "id": id,
                            "line.id": line_id,
                            "user.id": user_id,
                            "station": station,
                            onboardDate: $("#fini").val()
                        },
                        success: function (response) {
                            if (response == "success") {
                                $('#editRemark').modal('hide');
                                var table = tableMap.get("tb" + line_id);
                                table.ajax.reload();
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(xhr.responseText);
                        }
                    });

                });

                $(document).on('click', '.delButton', function (event) { //any element with the class EditButton will be handled here
                    if (!confirm("Delete selected row?")) {
                        return false;
                    }

                    var table = tableMap.get($(this).parents("table").attr("id"));
                    var data = table.row($(this).parents('tr')).data();

                    $.ajax({
                        type: "POST",
                        url: "<c:url value="/LineUserReferenceController/delete" />",
                        data: {
                            "id": data.id,
                            "user.id": data.user.id,
                            "line.id": data.line.id,
                            "station": data.station
                        },
                        dataType: "html",
                        success: function (response) {
                            table.ajax.reload();
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            showMsg(xhr.responseText);
                        }
                    });
                });

            });

            var initCnt = 0;

            function getDetail() {
                $("#send").attr("disabled", "disabled");
                var area = $("#tableArea");
                var table = area.find("table").eq(0).clone();
                var lineTypeIds = urlLineType == "ASSY" ? [1, 2] : [3];
                $.ajax({
                    type: "GET",
                    url: "<c:url value="/BabLineController/findByLineType" />",
                    dataType: "json",
                    data: {
                        lineType_id: lineTypeIds
                    },
                    success: function (response) {
                        var arr = response;
                        for (var i = 0; i < arr.length; i++) {
                            var o = arr[i];
                            var cloneTb;
                            if ($('#tb' + o.id).length == 0) {
                                cloneTb = table.clone().prop('id', 'tb' + o.id);
                                area.append("<h3>線別: " + o.name + "</h3>");
                                area.append(cloneTb);
                                area.append("<hr />");
                            } else {
                                cloneTb = $('#tb' + o.id);
                            }
                            initTable(cloneTb, o.id);
                        }
                        $("#send").removeAttr("disabled");
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showMsg(xhr.responseText);
                    }
                });
            }

            function initTable(tb, d) {
                var t;
                t = tb.DataTable({
                    dom: 'Bfrtip',
                    "buttons": [
                        {
                            "text": 'Add',
                            "attr": {
                                "data-toggle": "modal",
                                "data-target": "#addRemark"
                            },
                            "action": function (e, dt, node, config) {
                                clearForm($("#addRemark"));
                                $("#addRemark .currentLineID").val(d);
                                $('#addRemark').modal('toggle');
                            }
                        }
                    ],
                    "ajax": {
                        "url": "<c:url value="/LineUserReferenceController/findByLineAndDate" />",
                        "type": "Get",
                        data: {
                            id: d,
                            onboardDate: $("#fini").val()
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(xhr.responseText);
                        }
                    },
                    "columns": [
                        {data: "id", title: "id", visible: false},
                        {data: "line.id", title: "line_id", visible: false},
                        {data: "user.id", title: "user_id"},
                        {data: "station", title: "station"},
                        {
                            "data": "id",
                            "width": "20%",
                            "title": "action",
                            "render": function (data, type, full, meta) { //this column is redefinied to show the action buttons
                                return '<div class="btn-toolbar">' +
                                        '<button class="btn btn-sm btn-primary editButton">Edit</button>' +
                                        '<button class="btn btn-sm btn-danger delButton">Delete</button>' +
                                        '</div>';
                            }
                        }
                    ],
                    "columnDefs": [
                        {
                            "type": "html",
                            "targets": [2],
                            'render': function (data, type, full, meta) {
                                return userMap.has(data) ? userMap.get(data) : data;
                            }
                        }
                    ],
                    "order": [[3, "asc"]],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    displayLength: -1,
                    "processing": true,
                    "initComplete": function (settings, json) {

                    },
                    filter: false,
                    destroy: true,
                    paginate: false,
                    info: false
                });
                tableMap.set(tb.attr("id"), t);
            }

            function clearForm(dialog) {//blank the add/edit popup form
                dialog.find(":text, textarea, input[type='number']").val("");
                dialog.find("#currentID, #currentLineID, #currentID2").val(0);
            }

            function setUserOptions() {
                var role;
                console.log();
                if (urlLineType == "ASSY") {
                    role = ["PREASSY", "ASSY"];
                } else {
                    role = ["PACKING"];
                }
                $.ajax({
                    type: "GET",
                    url: "<c:url value="/LineUserReferenceController/findUserByRole" />",
                    data: {
                        userRole: role
                    },
                    dataType: "json",
                    async: false,
                    success: function (response) {
                        var obj = response.data;
                        var target = $("#addRemark .user_id, #editRemark .user_id");
                        for (var i = 0; i < obj.length; i++) {
                            var o = obj[i];
                            target.append("<option value=" + o.id + ">" + o.username + "</option>");
                            userMap.set(o.id, o.username);
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
        <div class="container">
            <!-- add/edit popup window -->
            <div class="modal fade" id="addRemark" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">                
                            <h4 class="modal-title" id="myModalLabel">Add/Edit Remark</h4>
                        </div>
                        <div class="modal-body">
                            <fieldset>
                                <!-- Form Name -->
                                <!-- Text input-->
                                <label class="control-label col-sm-3" for="user_id">人員: </label>
                                <div class="input-group col-sm-9">
                                    <!--<input id="modelName" name="modelName" type="text" class="input-xlarge form-control">-->
                                    <select class="user_id" name="user_id" class="input-xlarge form-control">
                                    </select>
                                </div><br />

                                <label class="control-label col-sm-3" for="user_id">站別: </label>
                                <div class="input-group col-sm-9">
                                    <input class="station" name="station" type="text" class="input-xlarge form-control">
                                </div>                     
                                <input type="hidden" class="currentLineID" value="" />                         
                                <input type="hidden" class="currentID" value="" />                         
                            </fieldset>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" id="addRemarkButton">Save changes</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>    
            <!-- end popup window -->

            <!-- add/edit popup window -->
            <div class="modal fade" id="editRemark" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">                
                            <h4 class="modal-title" id="myModalLabel">Add/Edit Remark</h4>
                        </div>
                        <div class="modal-body">
                            <fieldset>
                                <!-- Form Name -->
                                <!-- Text input-->
                                <label class="control-label col-sm-3" for="user_id">人員: </label>
                                <div class="input-group col-sm-9">
                                    <!--<input id="modelName" name="modelName" type="text" class="input-xlarge form-control">-->
                                    <select class="user_id" name="user_id" class="input-xlarge form-control">
                                    </select>
                                </div><br />

                                <label class="control-label col-sm-3" for="user_id">站別: </label>
                                <div class="input-group col-sm-9">
                                    <input class="station" name="station" type="text" class="input-xlarge form-control" readonly="">
                                </div>
                                <input type="hidden" class="currentID" value="" />                         
                                <input type="hidden" class="currentLineID" value="" />                         
                            </fieldset>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" id="editRemarkButton">Save changes</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>    
            <!-- end popup window -->

            <div class="row">
                <h3 class="title">設定各線人員</h3>
            </div>

            <div class="row form-inline">
                <div class="col form-group">
                    <label for="beginTime">日期: 從</label>
                    <div class='input-group date' id='onboardDate'>
                        <input type="text" id="fini" placeholder="請選擇時間"> 
                    </div> 
                </div>
                <div class="col form-group">
                    <input type="button" id="send" value="確定(Ok)">
                </div>
            </div>

            <div id="tableArea" class="row">
                <table class="table"></table>
            </div>
        </div>
        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
