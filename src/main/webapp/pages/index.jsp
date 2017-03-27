<%-- 
    Document   : index
    Created on : 2017/3/6, 下午 02:38:11
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <c:set var="root" value="${pageContext.request.contextPath}/"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="${root}/images/favicon.ico"/>
        <link rel="stylesheet" type="text/css" media="screen" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" />
        <!--<link rel="stylesheet" type="text/css" media="screen" href="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/css/ui.jqgrid.css" />-->
        <link rel="stylesheet" type="text/css" media="screen" href="${root}/css/ui.jqgrid.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="${root}/css/ui.multiselect.css" />

        <!--<link rel="stylesheet" type="text/css" media="screen" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" />-->
        <style>
            /*            .ui-jqdialog {
                            display: none;
                            width: 300px;
                            position: absolute;
                            padding: .2em;
                            font-size: 11px;
                            overflow: visible;
                            left: 30% !important;
                            top: 40% !important;
                        }*/
        </style>
        <script src="https://code.jquery.com/jquery-1.12.4.min.js" 
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
                crossorigin="anonymous"
        ></script>
        <script
            src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"
            integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU="
            crossorigin="anonymous"
        ></script>
        <!--                <script src="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/js/i18n/grid.locale-tw.js"></script>
                        <script src="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/js/jquery.jqGrid.min.js"></script>
                        <script src="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/js/jquery.jqGrid.src.js"></script>-->
        <script src="${root}/js/i18n/grid.locale-tw.js"></script>
        <script src="${root}/js/jquery.jqGrid.min.js"></script>
        <script src="${root}/js/ui.multiselect.js"></script>
        <script src="${root}/js/urlParamGetter.js"></script>

        <script>
            $(function () {
                var lastsel, scrollPosition;
                var do_not_change_columns = ["id", "modified_Date", "CleanPanel_and_Assembly", "productionWT", "setupTime"];

                var grid = $("#list");
                var floorOption = getSelectOption("floor");
                var identitOption = getSelectOption("identit");

                var centerForm = function ($form) {
                    $form.closest('div.ui-jqdialog').position({
                        my: "center",
                        of: grid.closest('div.ui-jqgrid')
                    });
                };

                var customErrorTextFormat = function (response) {
                    return '<span class="ui-icon ui-icon-alert" ' +
                            'style="float:left; margin-right:.3em;"></span>' +
                            response.responseText;
                };

                grid.jqGrid({
                    url: '${root}/getSheetView.do',
                    datatype: "json",
                    mtype: 'POST',
                    colModel: [
                        {label: 'Model_id', name: 'modelId', hidden: true, key: true, editable: true, editoptions: {defaultValue: "0"}},
                        {label: 'Model', name: 'modelName', frozen: true, editable: true},
                        {label: 'TYPE', name: 'typeName', width: 100},
                        {label: 'ProductionWT', name: 'productionWT', width: 120},
                        {label: 'Total Module', name: 'totalModule', width: 100},
                        {label: 'Setup Time', name: 'setupTime', width: 100},
                        {label: 'CleanPanel', name: 'cleanPanel', width: 100},
                        {label: 'Assembly', name: 'assy', width: 100},
                        {label: 'T1', name: 't1', width: 60},
                        {label: 'T2', name: 't2', width: 60},
                        {label: 'T3', name: 't3', width: 60},
                        {label: 'T4', name: 't4', width: 60},
                        {label: 'Packing', name: 'packing', width: 100},
                        {label: 'Up_BI_RI', name: 'upBiRi', width: 100},
                        {label: 'Down_BI_RI', name: 'downBiRi', width: 100},
                        {label: 'BI Cost', name: 'biCost', width: 100},
                        {label: 'Vibration', name: 'vibration', width: 100},
                        {label: 'Hi-Pot/Leakage', name: 'hiPotLeakage', width: 100},
                        {label: 'Cold Boot', name: 'coldBoot', width: 100},
                        {label: 'Warm Boot', name: 'warmBoot', width: 100},
                        {label: 'ASS_T1', name: 'assyToT1', width: 100},
                        {label: 'T2_PACKING', name: 't2ToPacking', width: 100},
                        {label: 'Floor_id', name: 'floorId', editable: true},
                        {label: 'Floor', name: 'floorName', edittype: "select", editoptions: {value: floorOption}, width: 100, editable: true},
                        {label: 'Pending', name: 'pending', width: 100},
                        {label: 'Pending TIME', name: 'pendingTime', width: 100},
                        {label: 'BurnIn', name: 'burnIn', edittype: "select", editoptions: {value: "Y:Y;N:N"}, width: 100},
                        {label: 'B/I Time', name: 'biTime', width: 100},
                        {label: 'BI_Temperature', name: 'biTemperature', width: 100},
                        {label: 'SPEOwnerId', name: 'speOwnerId', hidden: true},
                        {label: 'SPE Owner', name: 'speOwnerName', edittype: "select", editoptions: {value: identitOption}, editable: true, width: 100},
                        {label: 'EEOwnerId', name: 'eeOwnerId', hidden: true},
                        {label: 'EE Owner', name: 'eeOwnerName', edittype: "select", editoptions: {value: identitOption}, editable: true, width: 100},
                        {label: 'QCOwnerId', name: 'qcOwnerId', hidden: true},
                        {label: 'QC Owner', name: 'qcOwnerName', edittype: "select", editoptions: {value: identitOption}, editable: true, width: 100},
                        {label: '組包SOP', name: 'assyPackingSop', width: 100},
                        {label: '測試SOP', name: 'testSop', width: 100},
                        {label: 'KEYPART_A', name: 'keypartA', width: 100},
                        {label: 'KEYPART_B', name: 'keypartB', width: 100},
                        {label: 'PRE-ASSY', name: 'preAssy', width: 100},
                        {label: 'BAB_FLOW', name: 'babFlow', width: 100},
                        {label: 'TEST_FLOW', name: 'testFlow', width: 100},
                        {label: 'PACKING_FLOW', name: 'packingFlow', width: 100},
                        {label: 'PART-LINK', name: 'partLink', width: 100},
                        {label: 'CE', name: 'ce', width: 60},
                        {label: 'UL', name: 'ul', width: 60},
                        {label: 'ROHS', name: 'rohs', width: 60},
                        {label: 'WEEE', name: 'weee', width: 60},
                        {label: 'Made in Taiwan', name: 'madeInTaiwan', width: 120},
                        {label: 'FCC', name: 'fcc', width: 60},
                        {label: 'EAC', name: 'eac', width: 60},
                        {label: 'N合1集合箱', name: 'nIn1CollectionBox', width: 100},
                        {label: '料號屬性值維護', name: 'partNoAttrMaintain', width: 100},
                        {label: '組裝排站人數', name: 'assyStations', width: 100},
                        {label: '包裝排站人數', name: 'packingStations', width: 100},
                        {label: '前置時間', name: 'assyLeadTime', width: 80},
                        {label: '看板工時', name: 'assyKanbanTime', width: 80},
                        {label: '前置時間', name: 'packingLeadTime', width: 80},
                        {label: '看板工時', name: 'packingKanbanTime', width: 80},
                        {label: 'CleanPanel+Assembly', name: 'cleanPanel_and_Assembly', width: 200},
                        {label: 'Modified_Date', width: 200, name: 'modified_Date', formatter: 'date', formatoptions: {srcformat: 'Y-m-d H:i:s A', newformat: 'Y-m-d H:i:s A'}}
                    ],
                    rowNum: 20,
                    rowList: [20, 50, 100],
                    pager: '#pager',
                    viewrecords: true,
                    shrinkToFit: false,
                    hidegrid: true,
                    prmNames: {id: "modelId"},
                    jsonReader: {
                        root: "rows",
                        page: "page",
                        total: "total",
                        records: "records",
                        repeatitems: false
                    },
                    onSelectRow: function (rowid, status, e) {
                        if (lastsel)
                            $("#list").jqGrid("restoreRow", lastsel);
                        if (rowid !== lastsel) {
                            scrollPosition = $("#list").closest(".ui-jqgrid-bdiv").scrollLeft();
                            $("#list").jqGrid("editRow", rowid, true, function () {
                                setTimeout(function () {
                                    $("input, select", e.target).focus();
                                }, 10);
                            });
                            setTimeout(function () {
                                $("#list").closest(".ui-jqgrid-bdiv").scrollLeft(scrollPosition);
                            }, 0);
                            lastsel = rowid;
                        } else {
                            lastsel = null;
                        }
                    },
                    afterSubmit: function () {
                        $(this).setGridParam({datatype: 'json', page: 1}).trigger('reloadGrid');
                        return [true];
                    },
                    navOptions: {reloadGridOptions: {fromServer: true}},
                    caption: "工時大表",
                    width: 1200,
                    height: 450,
//                    multiselect: true,
                    editurl: '${root}/updateSheet.do',
                    sortname: 'modelId', sortorder: 'desc'
                })
                        .jqGrid('navGrid', '#pager',
                                {edit: false, add: true, del: true, search: true},
                                {
                                    dataheight: 350,
                                    closeAfterEdit: false,
//                                    reloadAfterSubmit: true,
                                    errorTextFormat: customErrorTextFormat
                                },
                                {
                                    dataheight: 350,
                                    closeAfterAdd: true,
//                                    reloadAfterSubmit: true,
                                    errorTextFormat: customErrorTextFormat
                                },
                                {
                                    reloadAfterSubmit: true
                                },
                                {closeAfterSearch: false, reloadAfterSubmit: true}
                        )
                        .jqGrid('setGroupHeaders', {
                            useColSpanStyle: true,
                            groupHeaders: [
                                {startColumnName: 'ce', numberOfColumns: 7, titleText: '<em>外箱Label產品資訊 (1：要印   0：不印)</em>'},
                                {startColumnName: 'assyLeadTime', numberOfColumns: 2, titleText: '<em>組裝看板工時</em>'},
                                {startColumnName: 'packingLeadTime', numberOfColumns: 2, titleText: '<em>包裝看板工時</em>'}
                            ]
                        });

                var colModel = grid.jqGrid('getGridParam', 'colModel');
                var columnNames = [];

                for (var i = 0; i < colModel.length; i++) {
                    columnNames.push(colModel[i].name);
                }

//                var unitName = getQueryVariable("unit");
                var unitName = '${sessionScope.user.userType.name}';
                var modifyColumns = unitName == 'unlimited' ? columnNames : ((unitName == null || unitName == "") ? [] : getColumn(unitName));

                for (var i = 0; i < modifyColumns.length; i++) {
                    var canModifyColumn = modifyColumns[i];
                    grid.setColProp(canModifyColumn, {editable: true});
                }

                $("#list").jqGrid('setFrozenColumns');

                function getColumn(unit) {
                    var result;
                    $.ajax({
                        type: "Post",
                        url: "${root}/unitColumnServlet.do",
                        data: {
                            unit: unit
                        },
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

                function getSelectOption(columnName) {
                    var result;
                    $.ajax({
                        type: "GET",
                        url: "${root}/" + columnName + "Option.do",
//                        dataType: "text/plain",
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

            });
        </script>
    </head>
    <body>
        <div class="container">
            <div class="form-inline">
                <div>
                    <table class="table table-hover">
                        <tr>
                            <td>
                                jobnumber: <c:out value="${sessionScope.user.jobnumber}" /> / 
                                name: <c:out value="${sessionScope.user.jobnumber}" /> / 
                                unit: <c:out value="${sessionScope.user.userType.name}" /> /
                                floor: <c:out value="${sessionScope.user.floor.name}" />
                            </td>
                        </tr>
                    </table>
                </div>
                <div>
                    <input class="form-control" type="button" value="hideCol" id="hideCol" />
                    <input class="form-control" type="button" value="showCol" id="showCol" />
                </div>
                <div class="row">
                    <form action="${root}/logout.do" method="post">
                        <input class="form-control" type="submit" value="Logout" />
                    </form>
                </div>
            </div>
            <div class="row">
                <table id="list"></table> 
                <div id="pager"></div>
            </div>
        </div>
    </body>
</html>
