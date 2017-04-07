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
        <link rel="stylesheet" type="text/css" media="screen" href="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/css/ui.jqgrid.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="${root}/css/ui.multiselect.css" />

        <style>
            .ui-jqgrid,
            .ui-jqgrid .ui-jqgrid-view,
            .ui-jqgrid .ui-jqgrid-pager,
            .ui-jqgrid .ui-pg-input {
                font-size: 15px;
            }
            .small-button {
                font-size: .8em !important;
            }
            .ui-button-text {
                font-size: inherit !important;
            } 
        </style>
        <script src="//code.jquery.com/jquery-1.12.4.min.js" 
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
                crossorigin="anonymous"
        ></script>
        <script
            src="//code.jquery.com/ui/1.12.1/jquery-ui.min.js"
            integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU="
            crossorigin="anonymous"
        ></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/js/i18n/grid.locale-tw.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/js/jquery.jqGrid.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/js/jquery.jqGrid.src.js"></script>
        <script src="${root}/js/ui.multiselect.js"></script>
        <script src="${root}/js/urlParamGetter.js"></script>

        <script>
            $(function () {
                $(".widget input[type=submit], .widget a, .widget button").button();

                var lastsel, scrollPosition;
                var do_not_change_columns = ["id", "modified_Date", "CleanPanel_and_Assembly", "productionWT", "setupTime"];
                var specialRelativeColumn = {SPE: ["type", "floor", "eeOwner", "speOwner", "qcOwner"], EE: [], IE: []};

                var search_string_options = {sopt: ['eq', 'ne', 'cn', 'bw', 'ew']};
                var search_decimal_options = {sopt: ['eq', 'lt', 'gt']};
                var search_date_options = {sopt: ['eq', 'ne']};

                var unitName = '${sessionScope.user.userType.name}';
                var modifyColumns = unitName == 'unlimited' ? columnNames : ((unitName == null || unitName == "") ? [] : getColumn());

                var grid = $("#list");

                if (unitName == 'SPE') {
                    var floorOption = getSelectOption("floor");
                    var identitOption = getSelectOption("identit");
                    var typeOption = getSelectOption("type");
                    var flowOption = getSelectOption("flow");
                }

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
                        {label: 'rowId', name: 'rowId', frozen: true, hidden: true, key: true, editable: true, editoptions: {defaultValue: "0"}},
                        {label: 'Model', name: 'modelName', frozen: true, editable: true, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: 'TYPE', name: 'typeName', edittype: "select", editoptions: {value: typeOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: 'ProductionWT', name: 'productionWT', width: 120, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Total Module', name: 'totalModule', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Setup Time', name: 'setupTime', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'CleanPanel', name: 'cleanPanel', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Assembly', name: 'assy', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'T1', name: 't1', width: 60, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'T2', name: 't2', width: 60, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'T3', name: 't3', width: 60, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'T4', name: 't4', width: 60, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Packing', name: 'packing', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Up_BI_RI', name: 'upBiRi', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Down_BI_RI', name: 'downBiRi', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'BI Cost', name: 'biCost', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Vibration', name: 'vibration', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Hi-Pot/Leakage', name: 'hiPotLeakage', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Cold Boot', name: 'coldBoot', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Warm Boot', name: 'warmBoot', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'ASS_T1', name: 'assyToT1', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'T2_PACKING', name: 't2ToPacking', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Floor', name: 'floorName', edittype: "select", editoptions: {value: floorOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: 'Pending', name: 'pending', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Pending TIME', name: 'pendingTime', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'BurnIn', name: 'burnIn', edittype: "select", editoptions: {value: "Y:Y;N:N"}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: 'B/I Time', name: 'biTime', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'BI_Temperature', name: 'biTemperature', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'SPE Owner', name: 'speOwnerName', edittype: "select", editoptions: {value: identitOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: 'EE Owner', name: 'eeOwnerName', edittype: "select", editoptions: {value: identitOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: 'QC Owner', name: 'qcOwnerName', edittype: "select", editoptions: {value: identitOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: '組包SOP', name: 'assyPackingSop', width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: '測試SOP', name: 'testSop', width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: 'KEYPART_A', name: 'keypartA', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'KEYPART_B', name: 'keypartB', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'PRE-ASSY', name: 'preAssy', width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: 'BAB_FLOW', name: 'babFlow', edittype: "select", editoptions: {value: flowOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: 'TEST_FLOW', name: 'testFlow', edittype: "select", editoptions: {value: flowOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: 'PACKING_FLOW', name: 'packingFlow', edittype: "select", editoptions: {value: flowOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: 'PART-LINK', name: 'partLink', width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: 'CE', name: 'ce', width: 60, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'UL', name: 'ul', width: 60, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'ROHS', name: 'rohs', width: 60, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'WEEE', name: 'weee', width: 60, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Made in Taiwan', name: 'madeInTaiwan', width: 120, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'FCC', name: 'fcc', width: 60, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'EAC', name: 'eac', width: 60, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'N合1集合箱', name: 'nInOneCollectionBox', width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: '料號屬性值維護', name: 'partNoAttributeMaintain', width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                        {label: '組裝排站人數', name: 'assyStation', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: '包裝排站人數', name: 'packingStation', width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: '前置時間', name: 'assyLeadTime', width: 80, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: '看板工時', name: 'assyKanbanTime', width: 80, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: '前置時間', name: 'packingLeadTime', width: 80, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: '看板工時', name: 'packingKanbanTime', width: 80, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'CleanPanel+Assembly', name: 'cleanPanel_and_Assembly', width: 200, searchrules: {required: true}, searchoptions: search_decimal_options},
                        {label: 'Modified_Date', width: 200, name: 'modified_Date', formatter: 'date', formatoptions: {srcformat: 'Y-m-d H:i:s A', newformat: 'Y-m-d H:i:s A'}, searchrules: {required: true}, searchoptions: search_date_options}
                    ],
                    rowNum: 20,
                    rowList: [20, 50, 100],
                    pager: '#pager',
                    viewrecords: true,
                    shrinkToFit: false,
                    hidegrid: true,
                    prmNames: {id: "rowId"},
                    stringResult: true,
                    jsonReader: {
                        root: "rows",
                        page: "page",
                        total: "total",
                        records: "records",
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
                    afterSubmit: function () {
                        $(this).jqGrid("setGridParam", {datatype: 'json'});
                        return [true];
                    },
                    navOptions: {reloadGridOptions: {fromServer: true}},
                    caption: "工時大表",
                    width: 1200,
                    height: 450,
                    multiselect: true,
                    multiboxonly: true,
                    editurl: '${root}/updateSheet.do',
                    sortname: 'modified_Date', sortorder: 'desc'
                })
                        .jqGrid('navGrid', '#pager',
                                {edit: true, add: true, del: true, search: true},
                                {
                                    dataheight: 350,
                                    width: 350,
                                    closeAfterEdit: false,
                                    reloadAfterSubmit: true,
                                    errorTextFormat: customErrorTextFormat
                                },
                                {
                                    dataheight: 350,
                                    width: 350,
                                    closeAfterAdd: true,
                                    reloadAfterSubmit: true,
                                    errorTextFormat: customErrorTextFormat
                                },
                                {
                                    reloadAfterSubmit: true
                                },
                                {
                                    sopt: ['eq', 'ne', 'lt', 'gt', 'cn', 'bw', 'ew'],
                                    closeAfterSearch: false,
                                    reloadAfterSubmit: true
                                }
                        )
                        .jqGrid('setGroupHeaders', {
                            useColSpanStyle: true,
                            groupHeaders: [
                                {startColumnName: 'ce', numberOfColumns: 7, titleText: '<em>外箱Label產品資訊 (1：要印   0：不印)</em>'},
                                {startColumnName: 'assyLeadTime', numberOfColumns: 2, titleText: '<em>組裝看板工時</em>'},
                                {startColumnName: 'packingLeadTime', numberOfColumns: 2, titleText: '<em>包裝看板工時</em>'}
                            ]
                        })
                        .navButtonAdd('#pager', {
                            caption: "Export to Excel",
                            buttonicon: "ui-icon-disk",
                            onClickButton: function () {
                                exportDataToExcel();
                            },
                            position: "last"
                        });

                var colModel = grid.jqGrid('getGridParam', 'colModel');
                var columnNames = [];

                console.log(modifyColumns);

                for (var i = 0; i < colModel.length; i++) {
//                    grid.setColProp(colModel[i].name, {editable: "hidden"});
                    columnNames.push(colModel[i].name);
                }

                for (var i = 0; i < modifyColumns.length; i++) {
                    var canModifyColumn = modifyColumns[i];
                    grid.setColProp(canModifyColumn, {editable: true, editoptions: {disabled: "disabled"}});
                }

                var specialColumns = specialRelativeColumn[unitName];

                for (var i = 0; i < specialColumns.length; i++) {
                    var columnName = specialColumns[i];
                    grid.setColProp(columnName + "Name", {editable: true});
                }

                grid.jqGrid('setFrozenColumns');

                function getColumn() {
                    var result;
                    $.ajax({
                        type: "Post",
                        url: "${root}/unitColumnServlet.do",
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
                
                function exportDataToExcel(){
                    alert("begin download...");
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
                <div class="widget">
                    <table>
                        <tr>
                            <td><button class="small-button" id="hideCol">hideCol</button></td>
                            <td><button class="small-button" id="showCol" >showCol</button></td>
                            <td>
                                <form action="${root}/logout.do" method="post">
                                    <input class="small-button" type="submit" value="Logout" />
                                </form>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="row">
                <table id="list"></table> 
                <div id="pager"></div>
            </div>
        </div>
    </body>
</html>
