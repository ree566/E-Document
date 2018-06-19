<%-- 
    Document   : test
    Created on : 2015/11/20, 上午 11:36:50
    Author     : Wei.Cheng
https://datatables.net/forums/discussion/20388/trying-to-access-rowdata-in-render-function-with-ajax-datasource-getting-undefined
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ADMIN')"  var="isAdmin" />
<sec:authorize access="hasAnyRole('OPER_FQC', 'FQC_USER')"  var="isFqcOper" />
<sec:authorize access="isAuthenticated()"  var="isAuthenticated">
    <sec:authentication property="principal" var="user" />
    <sec:authentication property="principal.jobnumber" var="jobnumber" /> 
</sec:authorize>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../../images/favicon.ico"/>
        <link rel="stylesheet" href="<c:url value="/webjars/datatables/1.10.16/css/jquery.dataTables.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/bootstrap-datetimepicker.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/buttons.dataTables.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/select2.min.css" />">
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
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/datatables/1.10.16/js/jquery.dataTables.min.js" /> "></script>
        <script src="<c:url value="/webjars/jquery-blockui/2.70/jquery.blockUI.js" /> "></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>
        <script src="<c:url value="/js/param.check.js"/>"></script>
        <script src="<c:url value="/js/jquery-datatable-button/dataTables.buttons.min.js"/>"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.flash.min.js"/>"></script>
        <script src="<c:url value="/js/jquery-datatable-button/jszip.min.js"/>"></script>
        <script src="<c:url value="/js/jquery-datatable-button/pdfmake.min.js"/>"></script>
        <script src="<c:url value="/js/jquery-datatable-button/vfs_fonts.js"/>"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.html5.min.js"/>"></script>
        <script src="<c:url value="/js/jquery-datatable-button/buttons.print.min.js"/>"></script>
        <script src="<c:url value="/js/select2.min.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-datetimepicker.min.js"/>"></script>
        <script>
            $(function () {
                var table;

                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");

                initTable();

                $.getJSON('<c:url value="/json/fqcRemarkOptions.json" />', function (data) {
                    initFqcRemarkTemplate(data.fqcRemarkTemplate);
                });

                $("#search").click(initTable);

                var momentFormatString = 'YYYY-MM-DD';
                var options = {
                    defaultDate: moment(),
                    useCurrent: true,
                    maxDate: moment(),
                    format: momentFormatString,
                    extraFormats: [momentFormatString]
                };

                $('#fini, #ffin').datetimepicker(options);

                $("#fini").click(function () {
                    if ($("#ffin").val() == "") {
                        $("#ffin").val($(this).val());
                    }
                });

                $("body").on("click", ".remarkEdit", function () {
                    var data = table.row($(this).parents('tr')).data();
                    $("#myModalLabel").html("正在編輯欄位id: " + data.id + " 資料");
                    $("#remark").val(data.remark);
                    $("#currentID").val(data.id);
                    $('#addEditRemark').modal('show');
                });

                $("#updateRemark").click(function () {
                    var reg = /<(.|\n)*?>/g;
                    if (reg.test($('#remark').val()) == true) {
                        alert('HTML Tag are not allowed');
                        return false;
                    }
                    $.ajax({
                        method: "POST",
                        url: "<c:url value="/FqcController/updateRemark" />",
                        dataType: "html",
                        data: {
                            id: $("#currentID").val(),
                            remark: $("#remark").val()
                        },
                        success: function (response) {
                            if (response == "success") {
                                alert("Record update successfully.");
                                $('#addEditRemark').modal('hide');
                                table.ajax.reload();
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert(xhr.responseText);
                        }
                    });
                });
                
                $("#remark-template").change(function () {
                    var sel = $(this);
                    var selVal = sel.val();
                    var remarkWiget = $("#remark");
                    if (selVal != -1) {
                        var selText = sel.children(':selected').text();
                        remarkWiget.val("").val(selText);
                    } else {
                        remarkWiget.val("");
                    }
                });

                function initTable() {
                    table = $("#fqcRecord").DataTable({
                        dom: 'lf<"floatright"B>rtip',
                        buttons: [
                            {
                                extend: 'copyHtml5',
                                exportOptions: {
                                    columns: 'th:not(:last-child)'
                                }
                            },
                            {
                                extend: 'excelHtml5',
                                exportOptions: {
                                    columns: 'th:not(:last-child)'
                                }
                            },
                            {
                                extend: 'print',
                                exportOptions: {
                                    columns: 'th:not(:last-child)'
                                }
                            }
                        ],
                        "processing": true,
                        "serverSide": false,
                        fixedHeader: {
                            headerOffset: 50
                        },
                        "ajax": {
                            "url": "<c:url value="/FqcProducitvityHistoryController/findByDate" />",
                            "type": "GET",
                            data: {
                                startDate: $("#fini").val(),
                                endDate: $("#ffin").val()
                            }
                        },
                        "columns": [
                            {data: "id"},
                            {data: "jobnumber"},
                            {data: "po"},
                            {data: "modelName"},
                            {data: "standardTime"},
                            {data: "pcs"},
                            {data: "timeCost"},
                            {data: "productivity"},
                            {data: "fqcLineName"},
                            {data: "beginTime"},
                            {data: "lastUpdateTime"},
                            {data: "remark"},
                            {data: "id"}
                        ],
                        "columnDefs": [
                            {
                                "type": "html",
                                "targets": 7,
                                'render': function (data, type, full, meta) {
                                    var productividy = calcProductivity(full.standardTime, full.pcs, full.timeCost);
                                    var p = getPercent(productividy);
                                    return p + "%";
                                }
                            },
                            {
                                "type": "html",
                                "targets": [9, 10],
                                'render': function (data, type, full, meta) {
                                    return formatDate(data);
                                }
                            },
                            {
                                "type": "html",
                                "targets": 11,
                                'render': function (data, type, full, meta) {
                                    return data == null ? "n/a" : data;
                                }
                            },
                            {
                                "type": "html",
                                "targets": 12,
                                'render': function (data, type, full, meta) {
                                    if (${isAuthenticated}) {
                                        if (${!isFqcOper && !isAdmin}) {
                                            return "";
                                        } else if (${isFqcOper && !isAdmin} && full.jobnumber != '${jobnumber}') {
                                            return "";
                                        }
                                        return "<button class='btn btn-default btn-sm remarkEdit'><span class='glyphicon glyphicon-pencil'></span></button";
                                    } else {
                                        return "";
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
                        displayLength: 10,
                        lengthChange: true,
                        info: true,
                        paginate: true,
                        destroy: true,
                        stateSave: true,
                        "order": [[0, "desc"]]
                    });
                }

                function reloadTable() {
                    table.ajax.reload();
                }

                function formatDate(timeStamp) {
                    return moment(timeStamp).format("YYYY-MM-DD hh:mm");
                }

                function getPercent(val, precision) {
                    return roundDecimal((val * 100), precision == null ? 0 : precision);
                }

                function roundDecimal(val, precision) {
                    var size = Math.pow(10, precision);
                    return Math.round(val * size) / size;
                }

                function calcProductivity(standard, pcs, timeCost) {
                    if (standard == null || pcs == null || timeCost == null || standard == 0 || pcs == 0 || timeCost == 0) {
                        return 0;
                    }
                    var productivity = (standard * pcs) / timeCost;
                    return productivity;
                }

                function initFqcRemarkTemplate(templateOptions) {
                    var target = $("#remark-template");
                    for (var i = 0; i < templateOptions.length; i++) {
                        var option = templateOptions[i];
                        target.append("<option value='" + option.id + "'>" + option.val + "</option>");
                    }
                }

            });



        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />

        <div class="container">
            <div class="row">
                <div class="form-inline">
                    <label>篩選日期:從</label>
                    <div class='input-group date' id='beginTime'>
                        <input type="text" id="fini" placeholder="請選擇起始時間"> 
                    </div> 
                    <label>到</label>
                    <div class='input-group date' id='endTime'>
                        <input type="text" id="ffin" placeholder="請選擇結束時間"> 
                    </div>
                    <button id="search">確定</button>
                </div>
                <table id="fqcRecord" class="table table-bordered">
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>人員</th>
                            <th>工單</th>
                            <th>機種</th>
                            <th>標工</th>
                            <th>產出數量</th>
                            <th>時間</th>
                            <th>效率(%)</th>
                            <th>線別</th>
                            <th>開始</th>
                            <th>結束</th>
                            <th>備註</th>
                            <th>#</th>
                        </tr>
                    </thead>
                </table>
            </div>

            <!-- add/edit popup window -->
            <div class="modal fade" id="addEditRemark" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">                
                            <h4 class="modal-title" id="myModalLabel">Add/Edit Remark</h4>
                        </div>
                        <div class="modal-body">
                            <fieldset>
                                <!-- Form Name -->
                                <!-- Text input-->
                                <label class="control-label col-sm-3" for="standardTime">備註: </label>
                                <div class="input-group col-sm-9">
                                    <textarea type="number" id="remark" name="remark" class="input-xlarge form-control"></textarea>
                                </div><br />
                                <select id="remark-template">
                                    <option value="-1">使用範本</option>
                                </select>
                                <input type="hidden" id="currentID" value="" />                         
                            </fieldset>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" id="updateRemark">Save changes</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>    
            <!-- end popup window -->
        </div>
        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
