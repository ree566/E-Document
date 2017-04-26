<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}/"/>
<style>
    /*    .ui-jqgrid,
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
        th.ui-th-column div {
            height:auto !important;
        }
        .ui-th-column>div,
        .ui-jqgrid-btable .jqgrow>td {
            word-wrap: break-word;  IE 5.5+ and CSS3 
            white-space: pre-wrap;  CSS3 
            white-space: -moz-pre-wrap;  Mozilla, since 1999 
            white-space: -pre-wrap;  Opera 4-6 
            white-space: -o-pre-wrap;  Opera 7 
            overflow: hidden;
            vertical-align: middle;
        }*/
</style>

<script src="${root}js/urlParamGetter.js"></script>
<script src="${root}js/column-name-setting.js"></script>
<script src="${root}js/jqgrid-custom-select-option-reader.js"></script>

<script>
    $(function () {
        var lastsel, scrollPosition;

        var unitName = '${sessionScope.user.userType.name}';
        var modifyColumns = (unitName == null || unitName == "") ? [] : getColumn();
        var grid = $("#list");

        //Set param into jqgrid-custom-select-option-reader.js and get option by param selectOptions
        //You can get the floor select options and it's formatter function
        //ex: floor selector -> floor and floor_func
        setSelectOptions({
            rootUrl: "${root}",
            columnInfo: [
                {name: "floor", isNullable: false},
                {name: "identit", nameprefix: "spe_", isNullable: false, dataToServer: "SPE"},
                {name: "identit", nameprefix: "ee_", isNullable: false, dataToServer: "EE"},
                {name: "identit", nameprefix: "qc_", isNullable: false, dataToServer: "QC"},
                {name: "type", isNullable: false},
                {name: "flow", isNullable: true},
                {name: "preAssy", isNullable: true},
                {name: "pending", isNullable: false}
            ]
        });

        grid.jqGrid({
            url: '${root}getSheetView.do',
            datatype: 'json',
            mtype: 'POST',
//            guiStyle: "bootstrap",
            colModel: [
                {label: 'id', name: "id", width: 60, frozen: true, hidden: false, key: true, search: false, editable: true, editoptions: {readonly: 'readonly', disabled: true, defaultValue: "0"}},
                {label: 'Model', name: "modelName", frozen: true, editable: true, searchrules: {required: true}, searchoptions: search_string_options, editrules: {required: true}, formoptions: required_form_options},
                {label: 'TYPE', name: "type.id", edittype: "select", editoptions: {value: selectOptions["type"]}, formatter: selectOptions["type_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'ProductionWT', name: "productionWt", width: 120, searchrules: number_search_rule, searchoptions: search_decimal_options},
                {label: 'Total Module', name: "totalModule", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}},
                {label: 'Setup Time', name: "setupTime", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                {label: 'CleanPanel', name: "cleanPanel", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Assembly', name: "assy", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'T1', name: "t1", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'T2', name: "t2", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'T3', name: "t3", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'T4', name: "t4", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Packing', name: "packing", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Up_BI_RI', name: "upBiRi", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Down_BI_RI', name: "downBiRi", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'BI Cost', name: "biCost", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Vibration', name: "vibration", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Hi-Pot/Leakage', name: "hiPotLeakage", width: 120, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Cold Boot', name: "coldBoot", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Warm Boot', name: "warmBoot", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'ASS_T1', name: "assyToT1", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                {label: 'T2_PACKING', name: "t2ToPacking", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                {label: 'Floor', name: "floor.id", edittype: "select", editoptions: {value: selectOptions["floor"]}, width: 100, searchrules: {required: true}, searchoptions: search_string_options, formatter: selectOptions["floor_func"]},
                {label: 'Pending', name: "pending.id", edittype: "select", editoptions: {value: selectOptions["pending"]}, formatter: selectOptions["pending_func"], width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                {label: 'Pending TIME', name: "pendingTime", width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {required: true, number: true}, editoptions: {defaultValue: '0'}, formoptions: required_form_options},
                {label: 'BurnIn', name: "burnIn", edittype: "select", editoptions: {value: "Y:Y;N:N"}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'B/I Time', name: "biTime", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {required: true, number: true}, editoptions: {defaultValue: '0'}, formoptions: required_form_options},
                {label: 'BI_Temperature', name: "biTemperature", width: 120, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {required: true, number: true}, editoptions: {defaultValue: '0'}, formoptions: required_form_options},
                {label: 'SPE Owner', name: "identitBySpeOwnerId.id", edittype: "select", editoptions: {value: selectOptions["spe_identit"]}, formatter: selectOptions["spe_identit_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'EE Owner', name: "identitByEeOwnerId.id", edittype: "select", editoptions: {value: selectOptions["ee_identit"]}, formatter: selectOptions["ee_identit_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'QC Owner', name: "identitByQcOwnerId.id", edittype: "select", editoptions: {value: selectOptions["qc_identit"]}, formatter: selectOptions["qc_identit_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: '組包SOP', name: "assyPackingSop", width: 100, searchrules: {required: true}, searchoptions: search_string_options, edittype: "textarea", editoptions: {maxlength: 500}},
                {label: '測試SOP', name: "testSop", width: 100, searchrules: {required: true}, searchoptions: search_string_options, edittype: "textarea", editoptions: {maxlength: 500}},
                {label: 'KEYPART_A', name: "keypartA", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'KEYPART_B', name: "keypartB", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'PRE-ASSY', name: "preAssy.id", edittype: "select", editoptions: {value: selectOptions["preAssy"]}, formatter: selectOptions["preAssy_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'BAB_FLOW', name: "flowByBabFlowId.id", edittype: "select", editoptions: {value: selectOptions["flow"]}, formatter: selectOptions["flow_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'TEST_FLOW', name: "flowByTestFlowId.id", edittype: "select", editoptions: {value: selectOptions["flow"]}, formatter: selectOptions["flow_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'PACKING_FLOW', name: "flowByPackingFlowId.id", edittype: "select", editoptions: {value: selectOptions["flow"]}, formatter: selectOptions["flow_func"], width: 140, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'PART-LINK', name: "partLink", edittype: "select", editoptions: {value: ":empty;Y:Y;N:N"}, width: 100, searchrules: number_search_rule, searchoptions: search_string_options},
                {label: 'CE', name: "ce", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'UL', name: "ul", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'ROHS', name: "rohs", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'WEEE', name: "weee", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'Made in Taiwan', name: "madeInTaiwan", width: 120, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'FCC', name: "fcc", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'EAC', name: "eac", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'N合1集合箱', name: "nInOneCollectionBox", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: '料號屬性值維護', name: "partNoAttributeMaintain", edittype: "select", editoptions: {value: "Y:Y;N:N"}, width: 120, searchrules: {required: true}, searchoptions: search_string_options},
                {label: '組裝排站人數', name: "assyStation", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                {label: '包裝排站人數', name: "packingStation", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                {label: '前置時間', name: "assyLeadTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {elmsuffix: '(組裝)'}, editoptions: {defaultValue: '0'}},
                {label: '看板工時', name: "assyKanbanTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {elmsuffix: '(組裝)'}},
                {label: '前置時間', name: "packingLeadTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {elmsuffix: '(包裝)'}, editoptions: {defaultValue: '0'}},
                {label: '看板工時', name: "packingKanbanTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {elmsuffix: '(包裝)'}},
                {label: 'CleanPanel+Assembly', name: "cleanPanelAndAssembly", width: 200, searchrules: number_search_rule, searchoptions: search_decimal_options},
                {label: 'Modified_Date', width: 200, name: "modifiedDate", index: "modifiedDate", formatter: 'date', formatoptions: {srcformat: 'Y-m-d H:i:s A', newformat: 'Y-m-d H:i:s A'}, stype: 'text', searchrules: date_search_rule, searchoptions: search_date_options, align: 'center'}
            ],
            rowNum: 20,
            rowList: [20, 50, 100],
            pager: '#pager',
            viewrecords: true,
            autowidth: true,
            shrinkToFit: false,
            hidegrid: true,
            stringResult: true,
            gridview: true,
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false
            },
//                    onSelectRow: function (rowid, status, e) {
//                        if (lastsel)
//                            grid.jqGrid("restoreRow", lastsel);
//                        if (rowid !== lastsel) {
//                            scrollPosition = grid.closest(".ui-jqgrid-bdiv").scrollLeft();
//                            grid.jqGrid("editRow", rowid, true, function () {
//                                setTimeout(function () {
//                                    $("input, select", e.target).focus();
//                                }, 10);
//                            });
//                            setTimeout(function () {
//                                grid.closest(".ui-jqgrid-bdiv").scrollLeft(scrollPosition);
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
            height: 450,
            editurl: '${root}updateSheet.do',
            sortname: 'id', sortorder: 'desc',
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Ajax Error occurred\n"
                        + "\nstatus is: " + xhr.status
                        + "\nthrownError is: " + thrownError
                        + "\najaxOptions is: " + ajaxOptions
                        );
            }
        })
                .jqGrid('navGrid', '#pager',
                        {edit: true, add: true, del: true, search: true},
                        {
                            jqModal: true,
                            dataheight: 350,
                            width: 450,
                            closeAfterEdit: false,
                            reloadAfterSubmit: true,
                            errorTextFormat: customErrorTextFormat,
                            recreateForm: true,
                            beforeShowForm: greyout,
                            closeOnEscape: true,
                            zIndex: 9999,
                            bottominfo: "Fields marked with (*) are required"
                        },
                        {
                            jqModal: true,
                            dataheight: 350,
                            width: 450,
                            closeAfterAdd: false,
                            reloadAfterSubmit: true,
                            errorTextFormat: customErrorTextFormat,
                            recreateForm: true,
                            beforeShowForm: greyout,
                            closeOnEscape: true,
                            zIndex: 9999,
                            bottominfo: "Fields marked with (*) are required"
                        },
                        {
                            zIndex: 9999,
                            reloadAfterSubmit: true
                        },
                        {
                            zIndex: 9999,
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
                        grid.jqGrid('excelExport', {"url": "${root}generateExcel.do"});
                    },
                    position: "last"
                });

        var columns = grid.jqGrid('getGridParam', 'colModel');
        var columnNames = [];

        for (var i = 0; i < columns.length; i++) {
            var obj = columns[i];
            columnNames[i] = obj.name;
        }

        //Remove not change column
        columnNames = $.grep(columnNames, function (value) {
            return $.inArray(value, do_not_change_columns) == -1;
        });

        //Separate readyonly column and editable column
//        var editableColumns = modifyColumns.length == 1 && modifyColumns[0] == -1 ? columnNames : modifyColumns;
//        var readonlyColumns = $(columnNames).not(editableColumns).get();

        var editableColumns = columnNames;
        var readonlyColumns = [];

        if (editableColumns.length != 0) {
            for (var i = 0; i < editableColumns.length; i++) {
                var editableColumn = editableColumns[i];
                grid.setColProp(editableColumn, {editable: true});
            }

            for (var i = 0; i < readonlyColumns.length; i++) {
                var readonlyColumn = readonlyColumns[i];
                grid.setColProp(readonlyColumn, {editable: true, editoptions: {readonly: 'readonly', disabled: true}});
            }
        }

        grid.jqGrid('setFrozenColumns');

        $(window).bind('resize', function () {

            setTimeout(function () {
                grid.jqGrid("setGridWidth", $('#worktime-content').width());
            }, 1000);

        }).trigger('resize');

        function getColumn() {
            var result;
            $.ajax({
                type: "Post",
                url: "${root}unitColumn.do",
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
<div id="worktime-content">
    <table id="list"></table> 
    <div id="pager"></div>
</div>