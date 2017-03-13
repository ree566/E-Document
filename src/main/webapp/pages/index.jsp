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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../images/favicon.ico"/>
        <link rel="stylesheet" type="text/css" media="screen" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" />
        <!--<link rel="stylesheet" type="text/css" media="screen" href="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/css/ui.jqgrid.css" />-->
        <link rel="stylesheet" type="text/css" media="screen" href="../css/ui.jqgrid.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="../css/ui.multiselect.css" />

        <!--<link rel="stylesheet" type="text/css" media="screen" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" />-->
        <style>
            .ui-jqdialog {
                display: none;
                width: 300px;
                position: absolute;
                padding: .2em;
                font-size: 11px;
                overflow: visible;
                left: 30% !important;
                top: 40% !important;
            }
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
        <script src="../js/i18n/grid.locale-tw.js"></script>
        <script src="../js/jquery.jqGrid.min.js"></script>
        <script src="../js/ui.multiselect.js"></script>
        <script src="../js/urlParamGetter.js"></script>

        <script>
            $(function () {
                var lastsel, scrollPosition;
                var do_not_change_columns = ["Modified_Date"];

                var grid = $("#list");

                var centerForm = function ($form) {
                    $form.closest('div.ui-jqdialog').position({
                        my: "center",
                        of: grid.closest('div.ui-jqgrid')
                    });
                };

                grid.jqGrid({
                    url: '../SheetViewServlet',
//                    cacheUrlData: true,
                    datatype: "json",
                    mtype: 'POST',
//                    styleUI: 'Bootstrap',
                    colModel: [
                        {label: 'Model_id', name: 'Model_id', hidden: true, key: true, frozen: true, editoptions: {defaultValue: "0"}},
                        {label: 'Model', name: 'Model', frozen: true},
                        {label: 'TYPE', name: 'Type', width: 100},
                        {label: 'ProductionWT', name: 'ProductionWT', width: 120},
                        {label: 'Total Module', name: 'Total_Module', width: 100},
                        {label: 'Setup Time', name: 'SetupTime', width: 100},
                        {label: 'CleanPanel', name: 'CleanPanel', width: 100},
                        {label: 'Assembly', name: 'ASSY', width: 100},
                        {label: 'T1', name: 'T1', width: 60},
                        {label: 'T2', name: 'T2', width: 60},
                        {label: 'T3', name: 'T3', width: 60},
                        {label: 'T4', name: 'T4', width: 60},
                        {label: 'Packing', name: 'Packing', width: 100},
                        {label: 'Up_BI_RI', name: 'Up_BI_RI', width: 100},
                        {label: 'Down_BI_RI', name: 'Down_BI_RI', width: 100},
                        {label: 'BI Cost', name: 'BI_Cost', width: 100},
                        {label: 'Vibration', name: 'Vibration', width: 100},
                        {label: 'Hi-Pot/Leakage', name: 'Hi_Pot_Leakage', width: 100},
                        {label: 'Cold Boot', name: 'Cold_Boot', width: 100},
                        {label: 'Warm Boot', name: 'Warm_Boot', width: 100},
                        {label: 'ASS_T1', name: 'ASSY_to_T1', width: 100},
                        {label: 'T2_PACKING', name: 'T2_to_Packing', width: 100},
                        {label: 'Floor', name: 'Floor', width: 100},
                        {label: 'Pending', name: 'Pending', width: 100},
                        {label: 'Pending TIME', name: 'Pending_Time', width: 100},
                        {label: 'BurnIn', name: 'BurnIn', edittype: "select", editoptions: {value: "Y:Y;N:N"}, width: 100},
                        {label: 'B/I Time', name: 'BI_Time', width: 100},
                        {label: 'BI_Temperature', name: 'BI_Temperature', width: 100},
                        {label: 'SPE Owner', name: 'SPE_Owner', width: 100},
                        {label: 'EE Owner', name: 'EE_Owner', width: 100},
                        {label: 'QC Owner', name: 'QC_Owner', width: 100},
                        {label: '組包SOP', name: 'ASSY_Packing_SOP', width: 100},
                        {label: '測試SOP', name: 'Test_SOP', width: 100},
                        {label: 'KEYPART_A', name: 'Keypart_A', width: 100},
                        {label: 'KEYPART_B', name: 'Keypart_B', width: 100},
                        {label: 'PRE-ASSY', name: 'Pre_ASSY', width: 100},
                        {label: 'BAB_FLOW', name: 'BAB_Flow', width: 100},
                        {label: 'TEST_FLOW', name: 'Test_Flow', width: 100},
                        {label: 'PACKING_FLOW', name: 'Packing_Flow', width: 100},
                        {label: 'PART-LINK', name: 'Part_Link', width: 100},
                        {label: 'CE', name: 'CE', width: 60},
                        {label: 'UL', name: 'UL', width: 60},
                        {label: 'ROHS', name: 'ROHS', width: 60},
                        {label: 'WEEE', name: 'WEEE', width: 60},
                        {label: 'Made in Taiwan', name: 'Made_in_Taiwan', width: 120},
                        {label: 'FCC', name: 'FCC', width: 60},
                        {label: 'EAC', name: 'EAC', width: 60},
                        {label: 'N合1集合箱', name: 'N_in_1_collection_box', width: 100},
                        {label: '料號屬性值維護', name: 'PartNo_attr_maintain', width: 100},
                        {label: '組裝排站人數', name: 'ASSY_stations', width: 100},
                        {label: '包裝排站人數', name: 'Packing_stations', width: 100},
                        {label: '前置時間', name: 'ASSY_lead_time', width: 80},
                        {label: '看板工時', name: 'ASSY_kanban_time', width: 80},
                        {label: '前置時間', name: 'Packing_lead_time', width: 80},
                        {label: '看板工時', name: 'Packing_kanban_time', width: 80},
                        {label: 'CleanPanel+Assembly', name: 'CleanPanel_and_Assembly', width: 200},
                        {label: 'Modified_Date', width: 200, name: 'Modified_Date'}
                    ],
                    rowNum: 10,
                    rowList: [10, 20, 30],
                    pager: '#pager',
                    viewrecords: true,
                    shrinkToFit: false,
                    hidegrid: true,
                    jsonReader: {
                        repeatitems: false
                    },
//                    onSelectRow: function (rowid, status, e) {
//                        if (lastsel)
//                            $("#list").jqGrid("restoreRow", lastsel);
//                        if (rowid !== lastsel) {
//                            scrollPosition = $("#list").closest(".ui-jqgrid-bdiv").scrollLeft();
//                            $("#list").jqGrid("editRow", rowid, true, function () {
//                                setTimeout(function () {
//                                    $("input, select", e.target).focus();
//                                }, 10);
//                            });
//                            setTimeout(function () {
//                                $("#list").closest(".ui-jqgrid-bdiv").scrollLeft(scrollPosition);
//                            }, 0);
//                            lastsel = rowid;
//                        } else {
//                            lastsel = null;
//                        }
//                    },
//                    loadonce: true,
                    afterSubmit: function () {
                        $(this).setGridParam({datatype:'json', page:1}).trigger('reloadGrid');
                        return [true];
                    },
                    navOptions: {reloadGridOptions: {fromServer: true}},
                    caption: "工時大表",
                    width: 1200,
                    height: 450,
                    multiselect: true,
                    editurl: '../ReceiveSheetMoidfied'
                })
                        .jqGrid('navGrid', '#pager',
                                {edit: true, add: true, del: true, search: true},
                                {closeAfterEdit: true, reloadAfterSubmit: true},
                                {closeAfterAdd: true, reloadAfterSubmit: true},
                                {reloadAfterSubmit: true},
                                {closeAfterSearch: true, reloadAfterSubmit: true}
                        )
                        .jqGrid('setGroupHeaders', {
                            useColSpanStyle: true,
                            groupHeaders: [
                                {startColumnName: 'CE', numberOfColumns: 7, titleText: '<em>外箱Label產品資訊 (1：要印   0：不印)</em>'},
                                {startColumnName: 'ASSY_lead_time', numberOfColumns: 2, titleText: '<em>組裝看板工時</em>'},
                                {startColumnName: 'Packing_lead_time', numberOfColumns: 2, titleText: '<em>包裝看板工時</em>'}
                            ]
                        });

                var colModel = grid.jqGrid('getGridParam', 'colModel');
                var columnNames = [];

                for (var i = 0; i < colModel.length; i++) {
                    columnNames.push(colModel[i].name);
                }

                var unitName = getQueryVariable("unit");
                var modifyColumns = (unitName == null || unitName == "") ? [] : getColumn(unitName);

                if (modifyColumns.length != 0) {

                    modifyColumns.push("Model");
//                    modifyColumns.push("Model_id");

                    var difference = $(columnNames).not(modifyColumns).get();

                    for (var i = 0; i < modifyColumns.length; i++) {
                        if (do_not_change_columns.indexOf(modifyColumns[i]) == -1) {
                            grid.setColProp(modifyColumns[i], {editable: true});
                        }
                    }

                    for (var i = 0; i < do_not_change_columns.length; i++) {
                        difference.splice(difference.indexOf(do_not_change_columns[i]), 1);
                    }

                    $("#hideCol").on("click", function () {
                        grid.hideCol(difference);
                        resetFrozenColumn(grid);
                    });

                    $("#showCol").on("click", function () {
                        grid.showCol(difference);
                        resetFrozenColumn(grid);
                    });
                }


                $("#list").jqGrid('setFrozenColumns');

                function resetFrozenColumn(grid) {
                    grid.jqGrid("destroyFrozenColumns");
                    grid.jqGrid("setFrozenColumns");
                }

                function getColumn(unit) {
                    var result;
                    $.ajax({
                        type: "Post",
                        url: "../UnitColumnServlet",
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

            });
        </script>
    </head>
    <body>
        <div class="container">
            <div>
                <table>
                    <tr>
                        <td>
                            jobnumber: <c:out value="${sessionScope.jobnumber}" /> / 
                            name: <c:out value="${sessionScope.name}" /> / 
                            unit: <c:out value="${sessionScope.unit}" />
                        </td>
                    </tr>
                </table>
            </div>
            <div>
                <input type="button" value="hideCol" id="hideCol" />
                <input type="button" value="showCol" id="showCol" />
            </div>
            <div class="row">
                <form action="../LogoutServlet" method="post">
                    <input type="submit" value="Logout" />
                </form>
            </div>
            <div class="row">
                <table id="list"></table> 
                <div id="pager"></div>
            </div>
        </div>
    </body>
</html>
