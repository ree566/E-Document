<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authentication var="user" property="principal" />
<sec:authorize access="hasRole('ADMIN')"  var="isAdmin" />
<sec:authorize access="hasRole('USER')"  var="isUser" />
<sec:authorize access="hasRole('OPER')"  var="isOper" />
<sec:authorize access="hasRole('AUTHOR')"  var="isAuthor" />
<sec:authorize access="hasRole('CONTRIBUTOR')"  var="isContributor" />
<sec:authorize access="hasRole('GUEST')"  var="isGuest" />
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

    .danger{
        color: red;
    }
    .hide-emptyName-flow{
        color: transparent;
    }
    .noselect {
        -webkit-touch-callout: none; /* iOS Safari */
        -webkit-user-select: none; /* Safari */
        -khtml-user-select: none; /* Konqueror HTML */
        -moz-user-select: none; /* Firefox */
        -ms-user-select: none; /* Internet Explorer/Edge */
        user-select: none; /* Non-prefixed version, currently
                              supported by Chrome and Opera */
    }
</style>

<script src="<c:url value="/js/urlParamGetter.js" />"></script>
<script src="<c:url value="/js/jqgrid-custom-select-option-reader.js" />"></script>
<script src="<c:url value="/js/jqgrid-custom-setting.js" />"></script>
<script src="<c:url value="/webjars/free-jqgrid/4.14.1/plugins/jquery.jqgrid.showhidecolumnmenu.js" />"></script>
<script src="<c:url value="/js/jquery.blockUI.js" />"></script>
<script src="<c:url value="/js/worktime-setting/column-selector-autofill.js" />"></script>
<script src="<c:url value="/js/worktime-setting/column-setting.js" />"></script>
<script src="<c:url value="/js/worktime-setting/column-validator.js" />"></script>
<script src="<c:url value="/webjars/github-com-johnculviner-jquery-fileDownload/1.4.6/src/Scripts/jquery.fileDownload.js" />"></script>
<script src="<c:url value="/js/worktime-setting/column-custom-callback.js" />"></script>

<script>
    $(function () {
        //保持Edit完畢的scroll position
        var scrollPosition = 0;
        //Grid主體
        var grid = $("#list");
        //依照單位分辨可編輯欄位和不可編輯欄位
        var unitName = '${user.unit.name}';
        var modifyColumns = ${isAdmin || isAuthor || isContributor || isOper} ? getColumn() : [];
        var columnEditableInsetting = modifyColumns.length > 0;

        //User who can only update the worktime
        var isUpdatable = ${isAdmin || isAuthor || isContributor || isOper} && columnEditableInsetting;

        //User who can fully CRUD the worktime.
        var isFullTableControllable = ${isAdmin || isOper || isAuthor} && columnEditableInsetting;

        var editableColumns, readonlyColumns;

        //給使用者隱藏欄位使用(可隱藏非自己部門負責的欄位)
        var toggle_value = false;

        //版本讀取，避免多人同時編輯覆蓋
        var selected_row_revision;
        var table_current_revision;

        var selected_row_formula_id;

        var bottominfo = "Fields marked with (*) are required.<br/>" +
                "勾選套入公式的欄位將會被重新計算.<br/>" +
                "<b class='danger'>※當CleanPanel到Warm Boot的欄位有異動，請選擇工時修改原因</b>";

        //Set param into jqgrid-custom-select-option-reader.js and get option by param selectOptions
        //You can get the floor select options and it's formatter function
        //ex: floor selector -> floor and floor_func

        setSelectOptions({
            rootUrl: "<c:url value="/" />",
            columnInfo: [
                {name: "businessGroup", isNullable: false},
                {name: "floor", isNullable: false},
                {name: "user", nameprefix: "spe_", isNullable: false, dataToServer: "SPE"},
                {name: "user", nameprefix: "ee_", isNullable: true, dataToServer: "EE"},
                {name: "user", nameprefix: "qc_", isNullable: false, dataToServer: "QC"},
                {name: "type", isNullable: false},
                {name: "flow", nameprefix: "bab_", isNullable: false, dataToServer: "1"},
                {name: "flow", nameprefix: "test_", isNullable: true, dataToServer: "3"},
                {name: "flow", nameprefix: "pkg_", isNullable: true, dataToServer: "2"},
                {name: "preAssy", isNullable: true},
                {name: "pending", isNullable: false},
                {name: "modReasonCode", isNullable: true}
            ]
        });

        var modReasonCodes = selectOptions["modReasonCode_options"];

        var hideEmptyBabFlow = function (rowId, val, rawObject) {
            if (val == 111) {
                return " class='hide-emptyName-flow noselect'";
            }
        };

        var checkRevision = function (form) {
            selected_row_revision = getRowRevision();
            if (selected_row_revision > table_current_revision) {
                closeEditDialogWhenError("此欄位有新的版本，請重新整理");
                return false;
            }
        };

        var before_add = function (postdata, formid) {
            var formulaFieldInfo = getFormulaCheckboxField();
            clearCheckErrorIcon();

            var checkResult = checkFlowIsValid(postdata, formid);

            var modelRelativeCheckResult = checkModelIsValid(postdata);
            checkResult = checkResult.concat(modelRelativeCheckResult);

            var biSamplingCheckResult = checkIfBiSampling(postdata);
            checkResult = checkResult.concat(biSamplingCheckResult);

            if (checkResult.length != 0) {
                errorTextFormatF(checkResult); //field // code
                return [false, "There are some errors in the entered data. Hover over the error icons for details."];
            } else {
//                return [false, "Saved."]; //--For debug validator
                $.extend(postdata, formulaFieldInfo);
                return [true, "saved"];
            }
        };

        var before_edit = function (postdata, formid) {
            var formulaFieldInfo = getFormulaCheckboxField();
            formulaFieldInfo["worktimeFormulaSettings[0].id"] = selected_row_formula_id;
            formulaFieldInfo["worktimeFormulaSettings[0].worktime.id"] = postdata.id;
            clearCheckErrorIcon();
            var checkResult = checkFlowIsValid(postdata, formid);

            var modelRelativeCheckResult = checkModelIsValid(postdata);
            checkResult = checkResult.concat(modelRelativeCheckResult);

            var biSamplingCheckResult = checkIfBiSampling(postdata);
            checkResult = checkResult.concat(biSamplingCheckResult);

            if (checkResult.length != 0) {
                errorTextFormatF(checkResult); //field // code
                return [false, "There are some errors in the entered data. Hover over the error icons for details."];
            } else {
//                return [false, "Saved."]; //--For debug validator
                //儲存前再check一次版本，給予覆蓋or取消的選擇
                var revision_number = getRowRevision();
                if (revision_number != selected_row_revision) {
                    return [false, "欄位版本已經被修改，請重新整理檢視新版本"];
                } else {
                    var standardWorkReason = $("#standardWorkReason").val();
                    if (standardWorkReason != "") {
                        postdata.reasonCode = standardWorkReason;
                    }
                    $.extend(postdata, formulaFieldInfo);
                    return [true, "saved"];
                }
            }
        };

        var showServerModifyMessage = function (response, postdata) {
            if (response.status == 200 || response.status == 201) {
                alert("Success");
                return [true, ''];
            }
        };

        var biSample_change_event = [
            {
                type: 'change',
                fn: function (e) {
                    checkBurnIn = $(this).val() == "N";
                }
            }
        ];

        //Jqgrid initialize.
        grid.jqGrid({
            url: '<c:url value="/Worktime/read" />',
            datatype: 'json',
            mtype: 'GET',
//            guiStyle: "bootstrap",
            autoencode: true,
            colModel: [
                {label: 'id', name: "id", width: 60, frozen: true, hidden: false, key: true, search: true, searchoptions: search_decimal_options, editable: true, editrules: {edithidden: true}, editoptions: {readonly: 'readonly', disabled: true, defaultValue: "0"}},
                {label: 'Model', name: "modelName", frozen: true, editable: true, searchrules: {required: true}, searchoptions: search_string_options, editrules: {required: true}, editoptions: {dataInit: autoUpperCase}, formoptions: required_form_options},
                {label: 'TYPE', name: "type.id", edittype: "select", editoptions: {value: selectOptions["type"]}, formatter: selectOptions["type_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["type"], sopt: ['eq']}},
                {label: 'BU', name: "businessGroup.id", edittype: "select", editoptions: {value: selectOptions["businessGroup"], dataEvents: businessGroup_select_event}, formatter: selectOptions["businessGroup_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["businessGroup"], sopt: ['eq']}},
                {label: 'Work Center', name: "workCenter", width: 100, searchrules: {required: true}, searchoptions: search_string_options, editrules: {required: false}},
                {label: 'ProductionWT', name: "productionWt", width: 120, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: {elmsuffix: addFormulaCheckbox("productionWt")}, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Setup Time', name: "setupTime", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: {elmsuffix: addFormulaCheckbox("setupTime")}, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'CleanPanel', name: "cleanPanel", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: 'Total Module', name: "totalModule", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: 'Assembly', name: "assy", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: 'T1', name: "t1", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: 'T2', name: "t2", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: 'T3', name: "t3", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: 'T4', name: "t4", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: 'Packing', name: "packing", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: 'Up_BI_RI', name: "upBiRi", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '2'}},
                {label: 'Down_BI_RI', name: "downBiRi", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '2'}},
                {label: 'BI Cost', name: "biCost", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Vibration', name: "vibration", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: 'Hi-Pot/Leakage', name: "hiPotLeakage", width: 120, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: 'Cold Boot', name: "coldBoot", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: 'Warm Boot', name: "warmBoot", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'ASS_T1', name: "assyToT1", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: {elmsuffix: addFormulaCheckbox("assyToT1")}, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'T2_PACKING', name: "t2ToPacking", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: {elmsuffix: addFormulaCheckbox("t2ToPacking")}, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Floor', name: "floor.id", edittype: "select", editoptions: {value: selectOptions["floor"]}, width: 100, formatter: selectOptions["floor_func"], searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["floor"], sopt: ['eq']}},
                {label: 'Pending', name: "pending.id", edittype: "select", editoptions: {value: selectOptions["pending"], defaultValue: 'N', dataEvents: pending_select_event}, formatter: selectOptions["pending_func"], width: 100, searchrules: number_search_rule, stype: "select", searchoptions: {value: selectOptions["pending"], sopt: ['eq']}},
                {label: 'Pending TIME', name: "pendingTime", width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {required: true, number: true}, editoptions: {defaultValue: '0'}, formoptions: required_form_options},
                {label: 'BI Sampling', name: "biSampling", edittype: "select", editoptions: {value: "N:N;Y:Y", dataEvents: biSample_change_event}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'BurnIn', name: "burnIn", edittype: "select", editoptions: {value: "N:N;BI:BI;RI:RI", dataEvents: burnIn_select_event}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'B/I Time', name: "biTime", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {required: true, number: true}, editoptions: {defaultValue: '0'}, formoptions: required_form_options},
                {label: 'BI_Temperature', name: "biTemperature", width: 120, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {required: true, number: true}, editoptions: {defaultValue: '0'}, formoptions: required_form_options},
                {label: 'SPE Owner', name: "userBySpeOwnerId.id", edittype: "select", editoptions: {value: selectOptions["spe_user"]}, formatter: selectOptions["spe_user_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["spe_user"], sopt: ['eq']}},
                {label: 'EE Owner', name: "userByEeOwnerId.id", edittype: "select", editoptions: {value: selectOptions["ee_user"]}, formatter: selectOptions["ee_user_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["ee_user"], sopt: ['eq']}},
                {label: 'QC Owner', name: "userByQcOwnerId.id", edittype: "select", editoptions: {value: selectOptions["qc_user"]}, formatter: selectOptions["qc_user_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["qc_user"], sopt: ['eq']}},
                {label: '組包SOP', name: "assyPackingSop", width: 100, search: true, searchrules: {required: true}, searchoptions: search_string_options, edittype: "textarea", editoptions: {maxlength: 500}},
                {label: '測試SOP', name: "testSop", width: 100, search: true, searchrules: {required: true}, searchoptions: search_string_options, edittype: "textarea", editoptions: {maxlength: 500}},
                {label: 'KEYPART_A', name: "keypartA", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'KEYPART_B', name: "keypartB", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'PRE-ASSY', name: "preAssy.id", edittype: "select", editoptions: {value: selectOptions["preAssy"]}, formatter: selectOptions["preAssy_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["preAssy"], sopt: ['eq']}},
                {label: 'BAB_FLOW', name: "flowByBabFlowId.id", edittype: "select", editoptions: {value: selectOptions["bab_flow"], dataEvents: babFlow_select_event, defaultValue: "111"}, formatter: selectOptions["bab_flow_func"], cellattr: hideEmptyBabFlow, width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["bab_flow"], sopt: ['eq']}},
                {label: 'TEST_FLOW', name: "flowByTestFlowId.id", edittype: "select", editoptions: {value: selectOptions["test_flow"], dataEvents: testFlow_select_event}, formatter: selectOptions["test_flow_func"], width: 100, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["test_flow"], sopt: ['eq']}},
                {label: 'PACKING_FLOW', name: "flowByPackingFlowId.id", edittype: "select", editoptions: {value: selectOptions["pkg_flow"]}, formatter: selectOptions["pkg_flow_func"], width: 140, searchrules: {required: true}, stype: "select", searchoptions: {value: selectOptions["pkg_flow"], sopt: ['eq']}},
                {label: 'PART-LINK', name: "partLink", edittype: "select", editoptions: {value: "Y:Y;N:N", defaultValue: 'Y'}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'CE', name: "ce", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'UL', name: "ul", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'ROHS', name: "rohs", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'WEEE', name: "weee", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'Made in Taiwan', name: "madeInTaiwan", width: 120, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'FCC', name: "fcc", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'EAC', name: "eac", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'KC', name: "kc", width: 60, searchrules: number_search_rule, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'N合1集合箱', name: "nsInOneCollectionBox", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0', dataEvents: nsInOneCollectionBox_change_event}},
                {label: 'SN是否等於SSN', name: "partNoAttributeMaintain", edittype: "select", editoptions: {value: "N:N; :empty", defaultValue: ' '}, width: 120, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'Test Profile', name: "testProfile", width: 100, search: true, searchrules: {required: true}, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;M5:M5;601:601;B-B:B-B", defaultValue: "601", dataEvents: testProfile_select_event}},
                {label: 'ACW Voltage', name: "acwVoltage", width: 100, search: true, searchrules: {required: true}, searchoptions: search_string_options, edittype: "text", editrules: {required: true}},
                {label: 'IR Voltage', name: "irVoltage", width: 100, search: true, searchrules: {required: true}, searchoptions: search_string_options, edittype: "text", editrules: {required: true}},
                {label: 'GND Value', name: "gndValue", width: 100, search: true, searchrules: {required: true}, searchoptions: search_string_options, edittype: "text", editrules: {required: true}},
                {label: 'LLT Value', name: "lltValue", width: 100, search: true, searchrules: {required: true}, searchoptions: search_string_options, edittype: "text", editrules: {required: true}},
                {label: '禮盒總重量(含配件)', name: "weight", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: '禮盒總重量(附加屬性質)', name: "weightAff", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: '整箱總重量誤差值', name: "tolerance", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: '燒機台車容納數量', name: "burnInQuantity", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {integer: true, required: true}, editoptions: {defaultValue: '0'}},
                {label: '組裝排站人數', name: "assyStation", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: {elmsuffix: addFormulaCheckbox("assyStation")}, editrules: {integer: true}, editoptions: {defaultValue: '0'}},
                {label: '包裝排站人數', name: "packingStation", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: {elmsuffix: addFormulaCheckbox("packingStation")}, editrules: {integer: true}, editoptions: {defaultValue: '0'}},
                {label: '前置時間', name: "assyLeadTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {label: '組裝前置時間'}, editoptions: {defaultValue: '0'}},
                {label: '看板工時', name: "assyKanbanTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {label: '組裝看板工時', elmsuffix: addFormulaCheckbox("assyKanbanTime")}, editoptions: {defaultValue: '0'}},
                {label: '前置時間', name: "packingLeadTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {label: '包裝前置時間'}, editoptions: {defaultValue: '0'}},
                {label: '看板工時', name: "packingKanbanTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {label: '包裝看板工時', elmsuffix: addFormulaCheckbox("packingKanbanTime")}, editoptions: {defaultValue: '0'}},
                {label: 'CleanPanel+Assembly', name: "cleanPanelAndAssembly", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {elmsuffix: addFormulaCheckbox("cleanPanelAndAssembly")}, editoptions: {defaultValue: '0'}},
                {label: 'Create_Date', width: 200, name: "createDate", formatter: 'date', formatoptions: {srcformat: 'Y-m-d H:i:s A', newformat: 'Y-m-d H:i:s A'}, stype: 'text', searchrules: date_search_rule, searchoptions: search_date_options, align: 'center'},
                {label: 'Modified_Date', width: 200, name: "modifiedDate", formatter: 'date', formatoptions: {srcformat: 'Y-m-d H:i:s A', newformat: 'Y-m-d H:i:s A'}, stype: 'text', searchrules: date_search_rule, searchoptions: search_date_options, align: 'center'},
                {label: '藍燈組裝(秒)', width: 80, name: "bwFields.0.assyAvg", index: "bwFields.assyAvg", sortable: true, searchrules: number_search_rule, searchoptions: search_decimal_options},
                {label: '藍燈包裝(秒)', width: 80, name: "bwFields.0.packingAvg", index: "bwFields.packingAvg", sortable: true, searchrules: number_search_rule, searchoptions: search_decimal_options}
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
            loadui: "block",
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false
            },
            navOptions: {reloadGridOptions: {fromServer: true}},
            caption: "工時大表",
            height: 450,
            sortname: 'id', sortorder: 'desc',
            onSelectRow: function (rowid) {
                scrollPosition = grid.closest(".ui-jqgrid-bdiv").scrollTop();
            },
            gridComplete: function () {
                grid.closest(".ui-jqgrid-bdiv").scrollTop(scrollPosition);
                if (isUpdatable || isFullTableControllable) {
                    getGridRevision();
                }
                registerEndsWithIfIe();
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Ajax Error occurred\n"
                        + "\nstatus is: " + xhr.status
                        + "\nthrownError is: " + thrownError
                        + "\najaxOptions is: " + ajaxOptions
                        );
            }
        });
        grid.jqGrid('setGroupHeaders', {
            useColSpanStyle: true,
            groupHeaders: [
                {startColumnName: 'ce', numberOfColumns: 8, titleText: '<em>外箱Label產品資訊 (1：要印   0：不印)</em>'},
                {startColumnName: 'partNoAttributeMaintain', numberOfColumns: 1, titleText: '<em>料號屬性值維護</em>'},
                {startColumnName: 'assyLeadTime', numberOfColumns: 2, titleText: '<em>組裝看板工時</em>'},
                {startColumnName: 'packingLeadTime', numberOfColumns: 2, titleText: '<em>包裝看板工時</em>'},
                {startColumnName: 'testProfile', numberOfColumns: 5, titleText: '<em>hi-pot Test</em>'},
                {startColumnName: 'weight', numberOfColumns: 3, titleText: '<em>包裝重量</em>'}
            ]
        });
        grid.jqGrid('navGrid', '#pager',
                {edit: isUpdatable || isFullTableControllable, add: isFullTableControllable, del: isFullTableControllable, search: true},
                {
                    url: '<c:url value="/Worktime/update" />',
                    dataheight: 350,
                    width: 450,
                    closeAfterEdit: closed_after_edit,
                    reloadAfterSubmit: true,
                    errorTextFormat: errorTextFormatF,
                    beforeSubmit: before_edit,
                    beforeShowForm: function (form) {
                        setTimeout(function () {
                            // do here all what you need (like alert('yey');)
                            $("#flowByBabFlowId\\.id, #businessGroup\\.id").trigger("change");
                            settingFormulaCheckbox();
                            addModReasonCode();
                        }, 50);
                        greyout(form);
                    },
                    afterShowForm: function (form) {
                        modelNameFormat();
                        checkRevision(form);
                        setReasonCodeRelateFieldEvent(form);
                    },
                    afterSubmit: showServerModifyMessage,
                    recreateForm: true,
                    closeOnEscape: true,
                    zIndex: 9999,
                    cols: 20,
                    viewPagerButtons: false,
                    bottominfo: bottominfo
                },
                {
                    url: '<c:url value="/Worktime/create" />',
                    dataheight: 350,
                    width: 450,
                    closeAfterAdd: closed_after_add,
                    reloadAfterSubmit: true,
                    errorTextFormat: errorTextFormatF,
                    beforeSubmit: before_add,
                    beforeShowForm: function (form) {
                        setTimeout(function () {
                            // do here all what you need (like alert('yey');)
                            $("#flowByBabFlowId\\.id, #businessGroup\\.id, #testProfile").trigger("change");
                        }, 50);
                        greyout(form);
                    },
                    afterShowForm: function (form) {
                        modelNameFormat();
                        checkRevision(form);
                    },
                    afterSubmit: showServerModifyMessage,
                    recreateForm: true,
                    closeOnEscape: true,
                    zIndex: 9999,
                    bottominfo: bottominfo
                },
                {
                    url: '<c:url value="/Worktime/delete" />',
                    zIndex: 9999,
                    reloadAfterSubmit: true,
                    afterSubmit: showServerModifyMessage
                },
                {
                    zIndex: 9999,
                    closeAfterSearch: closed_after_search,
                    reloadAfterSubmit: true
                }
        );

        //Button at grid foot.

        if (columnEditableInsetting) {
            grid.navButtonAdd('#pager', {
                caption: "Show / Hide",
                buttonicon: "ui-icon-shuffle",
                onClickButton: function () {
                    if (editableColumns == null || readonlyColumns == null || readonlyColumns.length == 0 || editableColumns.length == 0) {
                        return false;
                    }
                    toggle_value = !toggle_value;
                    grid.jqGrid(toggle_value ? 'hideCol' : 'showCol', readonlyColumns)
                            .jqGrid('destroyFrozenColumns')
                            .jqGrid('setFrozenColumns');
                },
                position: "last"
            });
        }

        if (${!isGuest}) {
            grid.navButtonAdd('#pager', {
                caption: "Export to Excel",
                buttonicon: "ui-icon-disk",
                id: "excelDownload1",
                onClickButton: function () {
                    var button = $("#excelDownload1");
                    excelDownload(button, "<c:url value="/WorktimeDownload/excel2" />");
                    return false;
                },
                position: "last"
            });
        }

        //有可編輯column的人再來分可編輯欄位
        //為0直接hide CRUD的按鈕
        if (columnEditableInsetting) {
            checkAndSetEditableAndReadOnlyField();
        }

        grid.jqGrid('setFrozenColumns');
        grid.jqGrid("showHideColumnMenu");
        $(window).bind('resize', function () {
            setTimeout(function () {
                grid.jqGrid("setGridWidth", $('#worktime-content').width());
            }, 1000);
        }).trigger('resize');

        function excelDownload(buttonId, url) {
            var button = $(buttonId);
            button.addClass('ui-state-disabled');
            $.fileDownload(url, {
                preparingMessageHtml: "We are preparing your report, please wait...",
                failMessageHtml: "There was a problem generating your report, please try again.",
                data: grid.getGridParam("postData"),
                successCallback: function (url) {
                    button.removeClass('ui-state-disabled');
                },
                failCallback: function (html, url) {
                    button.removeClass('ui-state-disabled');
                }
            });
        }

        function getColumn() {
            var result;
            $.ajax({
                type: "GET",
                url: "<c:url value="/WorktimeColumnGroup/byUnit" />",
                dataType: "json",
                async: false,
                success: function (response) {
                    if (response != null) {
                        var columnNameString = response.columnName;
                        result = columnNameString == null ? [] : columnNameString.split(",");
                    } else {
                        result = [];
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log(xhr.responseText);
                }
            });
            return result;
        }

        function checkAndSetEditableAndReadOnlyField() {
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
            editableColumns = modifyColumns.length == 1 && modifyColumns[0] == -1 ? columnNames : modifyColumns;
            readonlyColumns = $(columnNames).not(editableColumns).get();

            for (var i = 0; i < editableColumns.length; i++) {
                var editableColumn = editableColumns[i];
                grid.setColProp(editableColumn, {editable: true});
            }

            for (var i = 0; i < readonlyColumns.length; i++) {
                var readonlyColumn = readonlyColumns[i];
                grid.setColProp(readonlyColumn, {editable: true, editoptions: {readonly: 'readonly', disabled: true}});
            }
        }

        function flowCheck(logicArr, flowName, formObj) {
            if (flowName == null) {
                flowName = '';
            }
            var validationErrors = [];
            for (var i = 0; i < logicArr.length; i++) {
                var logic = logicArr[i];
                if ("disabled" in logic && logic.disabled(formObj) == true) {
                    continue;
                }
                var keyword = logic.keyword;
                for (var j = 0; j < keyword.length; j++) {
                    if (flowName.indexOf(keyword[j]) > -1) {
                        var checkCol = logic.checkColumn;
                        var checkType = logic.checkType;
                        if (checkType == null) { //And logic check
                            for (var k = 0; k < checkCol.length; k++) {
                                var colName = checkCol[k];
                                if (!logic.prmValid(formObj[colName])) {
                                    validationErrors.push({
                                        field: colName,
                                        code: logic.message
                                    });
                                }
                            }
                        } else if (checkType == 'OR') { //Or logic check
                            var checkFlag = false;
                            var tempArr = [];
                            for (var k = 0; k < checkCol.length; k++) {
                                var colName = checkCol[k];
                                if (!logic.prmValid(formObj[colName])) {
                                    tempArr.push({
                                        field: colName,
                                        code: logic.message
                                    });
                                    checkFlag = checkFlag || false;
                                } else {
                                    checkFlag = checkFlag || true;
                                }
                            }
                            if (checkFlag == false) {
                                validationErrors = validationErrors.concat(tempArr);
                            }
                        }
                    }
                }
            }
            return validationErrors;
        }

        function addFormulaCheckbox(fieldName) {
            var str = "<input type='checkbox' id='f_" +
                    fieldName + "' name='f_" + fieldName +
                    "' class='ui-checkbox' checked/><label for='f_" + fieldName + "'>套入公式</label>";
            return str;
        }

        function getFormulaCheckboxField() {
            var formulaCheckboxField = {};
            for (var i = 0; i < formulaColumn.length; i++) {
                var columnName = formulaColumn[i];
                var fieldName = "worktimeFormulaSettings[0]." + columnName;
                formulaCheckboxField[fieldName] = $("#f_" + columnName).is(":checked") ? 1 : 0;
            }
            return formulaCheckboxField;
        }

        function getSelectedRowId() {
            return grid.jqGrid('getGridParam', 'selrow');
        }

        function settingFormulaCheckbox() {
            var rowId = getSelectedRowId();
            if (rowId == null || rowId == "") {
                return false;
            }
            $.ajax({
                type: "GET",
                url: "<c:url value="/WorktimeFormulaSetting/find/" />" + rowId,
                dataType: "json",
                success: function (response) {
                    var setting = response;
                    for (var i = 0; i < formulaColumn.length; i++) {
                        var columnName = formulaColumn[i];
                        $("#f_" + columnName).prop("checked", setting[columnName] == 1);
                    }
                    selected_row_formula_id = setting.id;
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    closeEditDialogWhenError("設定Formula時發生錯誤，請稍後再試");
                    console.log(xhr.responseText);
                }
            });
        }

        function getRowRevision() {
            var result;
            var rowId = getSelectedRowId();
            $.ajax({
                type: "GET",
                url: "<c:url value="/Audit/findLastRevision" />",
                data: {
                    id: rowId
                },
                dataType: "json",
                async: false,
                success: function (response) {
                    result = response;
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    closeEditDialogWhenError("查詢欄位版本時發生錯誤，請稍後再試");
                    console.log(xhr.responseText);
                }
            });
            return result;
        }

        function closeEditDialogWhenError(error_message) {
            alert(error_message);
            $("#TblGrid_list_2").find("#cData").trigger("click");
        }

        function checkFlowIsValid(postdata, formid) {
            var preAssyOptions = selectOptions["preAssy_options"],
                    babOptions = selectOptions["bab_flow_options"],
                    testOptions = selectOptions["test_flow_options"],
                    pkgOptions = selectOptions["pkg_flow_options"];
            var preAssyName = preAssyOptions.get(parseInt(postdata["preAssy.id"])),
                    babFlowName = babOptions.get(parseInt(postdata["flowByBabFlowId.id"])),
                    testFlowName = testOptions.get(parseInt(postdata["flowByTestFlowId.id"])),
                    pkgFlowName = pkgOptions.get(parseInt(postdata["flowByPackingFlowId.id"]));
            var preAssyCheckLogic = flow_check_logic["PRE-ASSY"],
                    babCheckLogic = flow_check_logic.BAB,
                    testCheckLogic = flow_check_logic.TEST,
                    pkgCheckLogic = flow_check_logic.PKG;
            var preAssyCheckMessage = flowCheck(preAssyCheckLogic, preAssyName, postdata);
            var babCheckMessage = flowCheck(babCheckLogic, babFlowName, postdata);
            var testCheckMessage = flowCheck(testCheckLogic, testFlowName, postdata);
            var pkgCheckMessage = flowCheck(pkgCheckLogic, pkgFlowName, postdata);

            var firstCheckResult = babCheckMessage.concat(testCheckMessage).concat(pkgCheckMessage).concat(preAssyCheckMessage);

            var secondCheckResult = fieldCheck(postdata, preAssyName, babFlowName, testFlowName, pkgFlowName);

            var totalAlert = firstCheckResult.concat(secondCheckResult);

            return totalAlert;
        }

        function checkModelIsValid(postdata) {
            var data = {
                modelName: postdata["modelName"],
                "businessGroup\\.id": selectOptions["businessGroup_options"].get(parseInt(postdata["businessGroup.id"]))
            };
            var modelCheckResult = modelNameCheckFieldIsValid(data);
            var otherFieldCheckResult = checkModelNameIsValid(data);
            return modelCheckResult.concat(otherFieldCheckResult);
        }

        function checkIfBiSampling(data) {
            var babOptions = selectOptions["bab_flow_options"];
            var babFlowName = babOptions.get(parseInt(data["flowByBabFlowId.id"]));
            var biSamplingCheckResult = checkWhenBiSampling(data, babFlowName);
            return biSamplingCheckResult;
        }

        function getGridRevision() {
            table_current_revision = getRowRevision();
        }

        function addModReasonCode() {
            var sel = "";
            sel += "<div id='mod-reason' class='fm-button ui-corner-all fm-button-icon-left ui-state-disabled'><label>工時修改原因: </label><select id='standardWorkReason'>";
            modReasonCodes.forEach(function (value, key, map) {
                sel += "<option value='" + key + "'>" + value + "</option>";
            });
            sel += "</select></div>";
            $(sel).prependTo("#Act_Buttons>td.EditButton");
        }

        function setReasonCodeRelateFieldEvent(form) {
            var relativeObj = form.find("#productionWt, #setupTime, #cleanPanel, \n\
                #totalModule, #assy, #t1, #t2, #t3, #t4, #packing, \n\
                #upBiRi, #downBiRi, #biCost, #vibration, #hiPotLeakage, \n\
                #coldBoot, #warmBoot");
            relativeObj.on("keyup, change", function () {
                $("#mod-reason").removeClass("ui-state-disabled");
                relativeObj.unbind("keyup, change");
            });
        }
    });
</script>
<div id="worktime-content">
    <div class="form-inline">
        <h5 class="form-control">Your permission is: 
            <b class="permission-hint">
                R
                <c:if test="${isAdmin || isOper || isAuthor || isContributor}">
                    W
                </c:if>
            </b>
        </h5>
        <c:if test="${isAdmin || isAuthor || isContributor}">
            <h5 style="color:red" class="form-control">
                ※料號負責人、途程、SOP及料號屬性質在該欄位有更動時會上傳至MES，請務必確認資料是否正確
            </h5>
        </c:if>
    </div>
    <table id="list"></table> 
    <div id="pager"></div>
</div>
