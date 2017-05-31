<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authentication var="user" property="principal" />
<sec:authorize access="hasRole('ADMIN') or hasRole('USER')"  var="isAdmin" />
<style>
    .permission-hint{
        color: red;
    }
    .ui-jqgrid .ui-jqdialog {
        color: red;
    }

    .DataTD .ui-widget-content{
        width: 60%;
    }

    .CaptionTD{
        width: 30%;
    }
</style>

<script src="<c:url value="/js/urlParamGetter.js" />"></script>
<script src="<c:url value="/js/column-name-setting.js" />"></script>
<script src="<c:url value="/js/jqgrid-custom-select-option-reader.js" />"></script>
<script src="<c:url value="/js/jqgrid-custom-validator.js" />"></script>

<script>
    $(function () {
        var scrollPosition = 0;

        var unitName = '${user.unit.name}';
        var modifyColumns = [];
//        var modifyColumns = (unitName == null || unitName == "") ? [] : getColumn();
        var grid = $("#list");

        //Set param into jqgrid-custom-select-option-reader.js and get option by param selectOptions
        //You can get the floor select options and it's formatter function
        //ex: floor selector -> floor and floor_func
        setSelectOptions({
            rootUrl: "<c:url value="/" />",
            columnInfo: [
                {name: "floor", isNullable: false},
                {name: "user", nameprefix: "spe_", isNullable: false, dataToServer: "SPE"},
                {name: "user", nameprefix: "ee_", isNullable: false, dataToServer: "EE"},
                {name: "user", nameprefix: "qc_", isNullable: false, dataToServer: "QC"},
                {name: "type", isNullable: false},
                {name: "flow", nameprefix: "bab_", isNullable: false, dataToServer: "1"},
                {name: "flow", nameprefix: "test_", isNullable: true, dataToServer: "3"},
                {name: "flow", nameprefix: "pkg_", isNullable: true, dataToServer: "2"},
                {name: "preAssy", isNullable: true},
                {name: "pending", isNullable: false}
            ]
        });

        var burnIn_select_event = [
            {
                type: 'change',
                fn: function (e) {
                    var selectOption = $('option:selected', this).text();
                    var defaultValue = {
                        BI: 40,
                        RI: 0,
                        N: 0
                    };
                    $('input#biTemperature, input#biTime').val(defaultValue[selectOption]);
                }
            }
        ];

        var pending_select_event = [
            {
                type: 'change',
                fn: function (e) {
                    var selectOption = $('option:selected', this).text();
                    $('input#pendingTime').val(selectOption == 'N' ? 0 : '');
                }
            }
        ];

        var babFlow_select_event = [
            {
                type: 'change', fn: function (e) {
                    $.get('<c:url value="/SelectOption/flow-byParent/" />' + $(this).val(), function (data) {
                        var sel2 = $("#flowByTestFlowId\\.id");
//                        var selVal = sel2.val();
                        sel2.html("");
                        sel2.append("<option role='option' value=0>empty</option>");
                        for (var i = 0; i < data.length; i++) {
                            sel2.append("<option role='option' value=" + data[i].id + ">" + data[i].name + "</option>");
                        }
//                        sel2.val(selVal);
                    });
                }
            }
        ];

        var testFlowInit = function (form) {
            setTimeout(function () {
                // do here all what you need (like alert('yey');)
                $("#flowByBabFlowId\\.id").trigger("change");
            }, 50);
            greyout(form);
        };

        var flowVerify = function (postdata, formid) {
            cleanEditForm();

            var babArr = selectOptions["bab_flow"],
                    testArr = selectOptions["test_flow"],
                    pkgArr = selectOptions["pkg_flow"];
            var babFlowName = babArr[postdata["flowByBabFlowId.id"]],
                    testFlowName = testArr[postdata["flowByTestFlowId.id"]],
                    pkgFlowName = pkgArr[postdata["flowByPackingFlowId.id"]];
            var babCheckLogic = flow_check_logic.BAB,
                    testCheckLogic = flow_check_logic.TEST,
                    pkgCheckLogic = flow_check_logic.PKG;

            var babCheckMessage = flowCheck(babCheckLogic, babFlowName, postdata);
            var testCheckMessage = flowCheck(testCheckLogic, testFlowName, postdata);
            var pkgCheckMessage = flowCheck(pkgCheckLogic, pkgFlowName, postdata);

            var totalAlert = babCheckMessage.concat(testCheckMessage).concat(pkgCheckMessage);
            errorTextFormatF(totalAlert); //field // code
            return totalAlert.length == 0 ? [true, "saved"] : [false, "There are some errors in the entered data. Hover over the error icons for details."];

        };

        function flowCheck(logicArr, flowName, formObj) {
            var validationErrors = [];
            for (var i = 0; i < logicArr.length; i++) {
                var logic = logicArr[i];
                var keyword = logic.keyword;
                for (var j = 0; j < keyword.length; j++) {
                    if (flowName.indexOf(keyword[j]) > -1) {
                        var checkCol = logic.checkColumn;
                        for (var k = 0; k < checkCol.length; k++) {
                            var colName = checkCol[k];
                            if (!logic.prmValid(formObj[colName])) {
                                var err = {};
                                err.field = colName;
                                err.code = logic.message;
                                validationErrors.push(err);
                            }
                        }
                    }
                }
            }
            return validationErrors;
        }

        grid.jqGrid({
            url: '<c:url value="/Worktime/read" />',
            datatype: 'json',
            mtype: 'GET',
//            guiStyle: "bootstrap",
            autoencode: true,
            colModel: [
                {label: 'id', name: "id", width: 60, frozen: true, hidden: true, key: true, search: false, editable: true, editrules: {edithidden: true}, editoptions: {readonly: 'readonly', disabled: true, defaultValue: "0"}},
                {label: 'Model', name: "modelName", frozen: true, editable: true, searchrules: {required: true}, searchoptions: search_string_options, editrules: {required: true}, formoptions: required_form_options},
                {label: 'TYPE', name: "type.id", edittype: "select", editoptions: {value: selectOptions["type"]}, formatter: selectOptions["type_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["type"], sopt: ['eq']}},
                {label: 'ProductionWT', name: "productionWt", width: 120, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: formula_hint, editrules: {number: true}},
                {label: 'Total Module', name: "totalModule", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Setup Time', name: "setupTime", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: formula_hint, editrules: {number: true}},
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
                {label: 'ASS_T1', name: "assyToT1", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: formula_hint, editrules: {number: true}},
                {label: 'T2_PACKING', name: "t2ToPacking", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: formula_hint, editrules: {number: true}},
                {label: 'Floor', name: "floor.id", edittype: "select", editoptions: {value: selectOptions["floor"]}, width: 100, formatter: selectOptions["floor_func"], searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["floor"], sopt: ['eq']}},
                {label: 'Pending', name: "pending.id", edittype: "select", editoptions: {value: selectOptions["pending"], dataEvents: pending_select_event}, formatter: selectOptions["pending_func"], width: 100, searchrules: number_search_rule, stype: "select", searchoptions: {value: selectOptions["pending"], sopt: ['eq']}},
                {label: 'Pending TIME', name: "pendingTime", width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {required: true, number: true}, formoptions: required_form_options},
                {label: 'BurnIn', name: "burnIn", edittype: "select", editoptions: {value: "N:N;BI:BI;RI:RI", dataEvents: burnIn_select_event}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'B/I Time', name: "biTime", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {required: true, number: true}, editoptions: {defaultValue: '0'}, formoptions: required_form_options},
                {label: 'BI_Temperature', name: "biTemperature", width: 120, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {required: true, number: true}, editoptions: {defaultValue: '0'}, formoptions: required_form_options},
                {label: 'SPE Owner', name: "userBySpeOwnerId.id", edittype: "select", editoptions: {value: selectOptions["spe_user"]}, formatter: selectOptions["spe_user_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["spe_user"], sopt: ['eq']}},
                {label: 'EE Owner', name: "userByEeOwnerId.id", edittype: "select", editoptions: {value: selectOptions["ee_user"]}, formatter: selectOptions["ee_user_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["ee_user"], sopt: ['eq']}},
                {label: 'QC Owner', name: "userByQcOwnerId.id", edittype: "select", editoptions: {value: selectOptions["qc_user"]}, formatter: selectOptions["qc_user_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["qc_user"], sopt: ['eq']}},
                {label: '組包SOP', name: "assyPackingSop", width: 100, search: false, edittype: "textarea", editoptions: {maxlength: 500}},
                {label: '測試SOP', name: "testSop", width: 100, search: false, edittype: "textarea", editoptions: {maxlength: 500}},
                {label: 'KEYPART_A', name: "keypartA", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'KEYPART_B', name: "keypartB", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'PRE-ASSY', name: "preAssy.id", edittype: "select", editoptions: {value: selectOptions["preAssy"]}, formatter: selectOptions["preAssy_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["preAssy"], sopt: ['eq']}},
                {label: 'BAB_FLOW', name: "flowByBabFlowId.id", edittype: "select", editoptions: {value: selectOptions["bab_flow"], dataEvents: babFlow_select_event, defaultValue: "111"}, formatter: selectOptions["bab_flow_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["bab_flow"], sopt: ['eq']}},
                {label: 'TEST_FLOW', name: "flowByTestFlowId.id", edittype: "select", editoptions: {value: selectOptions["test_flow"]}, formatter: selectOptions["test_flow_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["test_flow"], sopt: ['eq']}},
                {label: 'PACKING_FLOW', name: "flowByPackingFlowId.id", edittype: "select", editoptions: {value: selectOptions["pkg_flow"]}, formatter: selectOptions["pkg_flow_func"], width: 140, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["pkg_flow"], sopt: ['eq']}},
                {label: 'PART-LINK', name: "partLink", edittype: "select", editoptions: {value: ":empty;Y:Y;N:N"}, width: 100, searchrules: number_search_rule, searchoptions: search_string_options},
                {label: 'CE', name: "ce", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'UL', name: "ul", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'ROHS', name: "rohs", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'WEEE', name: "weee", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'Made in Taiwan', name: "madeInTaiwan", width: 120, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'FCC', name: "fcc", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'EAC', name: "eac", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'N合1集合箱', name: "nsInOneCollectionBox", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: '料號屬性值維護', name: "partNoAttributeMaintain", edittype: "select", editoptions: {value: "Y:Y;N:N"}, width: 120, searchrules: {required: true}, searchoptions: search_string_options},
                {label: '組裝排站人數', name: "assyStation", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: formula_hint, editrules: {integer: true}},
                {label: '包裝排站人數', name: "packingStation", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: formula_hint, editrules: {integer: true}},
                {label: '前置時間', name: "assyLeadTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {elmsuffix: '(組裝)'}, editoptions: {defaultValue: '0'}},
                {label: '看板工時', name: "assyKanbanTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {elmsuffix: '(組裝)(F)'}},
                {label: '前置時間', name: "packingLeadTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {elmsuffix: '(包裝)'}, editoptions: {defaultValue: '0'}},
                {label: '看板工時', name: "packingKanbanTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {elmsuffix: '(包裝)(F)'}},
                {label: 'CleanPanel+Assembly', name: "cleanPanelAndAssembly", width: 200, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}},
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
            sortname: 'id', sortorder: 'desc',
            onSelectRow: function () {
                scrollPosition = grid.closest(".ui-jqgrid-bdiv").scrollTop();
            },
            gridComplete: function () {
                grid.closest(".ui-jqgrid-bdiv").scrollTop(scrollPosition);
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Ajax Error occurred\n"
                        + "\nstatus is: " + xhr.status
                        + "\nthrownError is: " + thrownError
                        + "\najaxOptions is: " + ajaxOptions
                        );
            }
        })
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
                        grid.jqGrid('excelExport', {"url": "<c:url value="/Worktime/excel" />"});
                    },
                    position: "last"
                });

        grid.jqGrid('navGrid', '#pager',
                {edit: ${isAdmin}, add: ${isAdmin}, del: ${isAdmin}, search: true},
                {
                    url: '<c:url value="/Worktime/update" />',
                    dataheight: 350,
                    width: 450,
                    closeAfterEdit: false,
                    reloadAfterSubmit: true,
                    errorTextFormat: errorTextFormatF,
                    beforeSubmit: flowVerify,
                    afterclickPgButtons: function (whichbutton, formid, rowid) {
                        $("#flowByBabFlowId\\.id").trigger("change");
//                        var rowData = grid.jqGrid ('getRowData', rowid);
//                        console.log(rowData["flowByTestFlowId.id"]);
//                        $("#flowByTestFlowId\\.id").val(rowData["flowByTestFlowId.id"]);
                    },
                    recreateForm: true,
                    beforeShowForm: testFlowInit,
                    closeOnEscape: true,
                    zIndex: 9999,
                    cols: 20,
                    bottominfo: "Fields marked with (*) are required.<br/>Fields marked with (F) set to zero will be recalculate."
                },
                {
                    url: '<c:url value="/Worktime/create" />',
                    dataheight: 350,
                    width: 450,
                    closeAfterAdd: false,
                    reloadAfterSubmit: true,
                    errorTextFormat: errorTextFormatF,
                    afterclickPgButtons: cleanEditForm,
                    beforeSubmit: flowVerify,
                    recreateForm: true,
                    beforeShowForm: testFlowInit,
                    closeOnEscape: true,
                    zIndex: 9999,
                    bottominfo: "Fields marked with (*) are required.<br/>Fields marked with (F) set to zero will be recalculate."
                },
                {
                    url: '<c:url value="/Worktime/delete" />',
                    zIndex: 9999,
                    reloadAfterSubmit: true
                },
                {
                    zIndex: 9999,
                    closeAfterSearch: false,
                    reloadAfterSubmit: true
                }
        );

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
                type: "GET",
                url: "<c:url value="/Worktime/unitColumn" />",
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
    <h5>Your permission is: 
        <b class="permission-hint">
            R
            <c:if test="${isAdmin}">
                W
            </c:if>
        </b>
    </h5>
    <table id="list"></table> 
    <div id="pager"></div>
</div>