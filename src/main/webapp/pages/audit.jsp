<%-- 
    Document   : audit
    Created on : 2017/4/25, 下午 02:58:21
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="root" value="${pageContext.request.contextPath}/"/>
<script src="${root}js/jqgrid-custom-select-option-reader.js"></script>
<script>
    $(function () {
        var grid = $("#list");
        var tableName = "Identit";

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

        $("#send").click(function () {
            var rowId = $("#rowId").val();
            var version = $("#version").val();

            grid.jqGrid('clearGridData');
            grid.jqGrid('setGridParam', {url: '${root}getSomeTable.do/' + rowId + '/' + version});
            grid.trigger('reloadGrid');

        });

        getEditRecord(-1, -1);

        function getEditRecord(rowId, version) {
            grid.jqGrid({
                url: '${root}getAudit.do/' + rowId + '/' + version,
                datatype: 'json',
                mtype: 'GET',
                colModel: [
                    {label: 'REV', name: "REV", jsonmap: "1.rev", width: 60, frozen: true, hidden: true, search: false},
                    {label: 'username', name: "username", jsonmap: "1.username", width: 60, frozen: true, hidden: false, search: false},
                    {label: 'REVTYPE', name: "REVTYPE", jsonmap: "2", width: 60, frozen: true, hidden: false, search: false},
                    {label: 'id', name: "id", jsonmap: "0.id", width: 60, frozen: true, hidden: false, search: false},
                    {label: 'Model', name: "modelName", jsonmap: "0.modelName", frozen: true, searchrules: {required: true}, searchoptions: search_string_options, formoptions: required_form_options},
                    {label: 'TYPE', name: "type_id", jsonmap: "0.type.id", formatter: selectOptions["type_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'ProductionWT', name: "productionWt", jsonmap: "0.productionWt", width: 120, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Total Module', name: "totalModule", jsonmap: "0.totalModule", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Setup Time', name: "setupTime", jsonmap: "0.setupTime", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'CleanPanel', name: "cleanPanel", jsonmap: "0.cleanPanel", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Assembly', name: "assy", jsonmap: "0.assy", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'T1', name: "t1", jsonmap: "0.t1", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'T2', name: "t2", jsonmap: "0.t2", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'T3', name: "t3", jsonmap: "0.t3", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'T4', name: "t4", jsonmap: "0.t4", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Packing', name: "packing", jsonmap: "0.packing", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Up_BI_RI', name: "upBiRi", jsonmap: "0.upBiRi", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Down_BI_RI', name: "downBiRi", jsonmap: "0.downBiRi", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'BI Cost', name: "biCost", jsonmap: "0.biCost", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Vibration', name: "vibration", jsonmap: "0.vibration", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Hi-Pot/Leakage', name: "hiPotLeakage", jsonmap: "0.hiPotLeakage", width: 120, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Cold Boot', name: "coldBoot", jsonmap: "0.coldBoot", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Warm Boot', name: "warmBoot", jsonmap: "0.warmBoot", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'ASS_T1', name: "assyToT1", jsonmap: "0.assyToT1", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'T2_PACKING', name: "t2ToPacking", jsonmap: "0.t2ToPacking", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Floor', name: "floor_id", jsonmap: "0.floor.id", width: 100, searchrules: {required: true}, searchoptions: search_string_options, formatter: selectOptions["floor_func"]},
                    {label: 'Pending', name: "pending_id", jsonmap: "0.pending.id", formatter: selectOptions["pending_func"], width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Pending TIME', name: "pendingTime", jsonmap: "0.pendingTime", width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, formoptions: required_form_options},
                    {label: 'BurnIn', name: "burnIn", jsonmap: "0.pendingTime", width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'B/I Time', name: "biTime", jsonmap: "0.biTime", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: required_form_options},
                    {label: 'BI_Temperature', name: "biTemperature", jsonmap: "0.biTemperature", width: 120, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: required_form_options},
                    {label: 'SPE Owner', name: "identitBySpeOwnerId_id", jsonmap: "0.identitBySpeOwnerId.id", formatter: selectOptions["spe_identit_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'EE Owner', name: "identitByEeOwnerId_id", jsonmap: "0.identitByEeOwnerId.id", formatter: selectOptions["ee_identit_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'QC Owner', name: "identitByQcOwnerId_id", jsonmap: "0.identitByQcOwnerId.id", formatter: selectOptions["qc_identit_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: '組包SOP', name: "assyPackingSop", jsonmap: "0.assyPackingSop", width: 100, searchrules: {required: true}, searchoptions: search_string_options, edittype: "textarea", editoptions: {maxlength: 500}},
                    {label: '測試SOP', name: "testSop", jsonmap: "0.testSop", width: 100, searchrules: {required: true}, searchoptions: search_string_options, edittype: "textarea", editoptions: {maxlength: 500}},
                    {label: 'KEYPART_A', name: "keypartA", jsonmap: "0.keypartA", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'KEYPART_B', name: "keypartB", jsonmap: "0.keypartB", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'PRE-ASSY', name: "preAssy_id", jsonmap: "0.preAssy.id", formatter: selectOptions["preAssy_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'BAB_FLOW', name: "flowByBabFlowId_id", jsonmap: "0.flowByBabFlowId.id", formatter: selectOptions["flow_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'TEST_FLOW', name: "flowByTestFlowId_id", jsonmap: "0.flowByTestFlowId.id", formatter: selectOptions["flow_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'PACKING_FLOW', name: "flowByPackingFlowId_id", jsonmap: "0.flowByPackingFlowId.id", formatter: selectOptions["flow_func"], width: 140, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'PART-LINK', name: "partLink", jsonmap: "0.partLink", width: 100, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'CE', name: "ce", jsonmap: "0.ce", width: 60, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'UL', name: "ul", jsonmap: "0.ul", width: 60, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'ROHS', name: "rohs", jsonmap: "0.rohs", width: 60, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'WEEE', name: "weee", jsonmap: "0.weee", width: 60, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'Made in Taiwan', name: "madeInTaiwan", jsonmap: "0.madeInTaiwan", width: 120, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'FCC', name: "fcc", jsonmap: "0.fcc", width: 60, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'EAC', name: "eac", jsonmap: "0.eac", width: 60, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'N合1集合箱', name: "nsInOneCollectionBox", jsonmap: "0.nsInOneCollectionBox", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '料號屬性值維護', name: "partNoAttributeMaintain", jsonmap: "0.partNoAttributeMaintain", width: 120, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: '組裝排站人數', name: "assyStation", jsonmap: "0.assyStation", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '包裝排站人數', name: "packingStation", jsonmap: "0.packingStation", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '前置時間', name: "assyLeadTime", jsonmap: "0.assyLeadTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '看板工時', name: "assyKanbanTime", jsonmap: "0.assyKanbanTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '前置時間', name: "packingLeadTime", jsonmap: "0.packingLeadTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '看板工時', name: "packingKanbanTime", jsonmap: "0.packingKanbanTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'CleanPanel+Assembly', name: "cleanPanelAndAssembly", jsonmap: "0.cleanPanelAndAssembly", width: 200, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Modified_Date', key: true, width: 200, name: "modifiedDate", jsonmap: "0.modifiedDate", index: "modifiedDate", formatter: 'date', formatoptions: {srcformat: 'Y-m-d H:i:s A', newformat: 'Y-m-d H:i:s A'}, stype: 'text', searchrules: date_search_rule, searchoptions: search_date_options, align: 'center'}
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
                prmNames: {id: "modifiedDate"},
                jsonReader: {
                    root: "rows",
                    page: "page",
                    total: "total",
                    records: "records",
                    repeatitems: false
                },
                afterSubmit: function () {
                    $(this).jqGrid("setGridParam", {datatype: 'json'});
                    return [true];
                },
                navOptions: {reloadGridOptions: {fromServer: true}},
                caption: tableName + " modify",
                height: 450,
                sortname: 'REV', sortorder: 'desc',
                error: function (xhr, ajaxOptions, thrownError) {
                    alert("Ajax Error occurred\n"
                            + "\nstatus is: " + xhr.status
                            + "\nthrownError is: " + thrownError
                            + "\najaxOptions is: " + ajaxOptions
                            );
                }
            })
                    .jqGrid('navGrid', '#pager',
                            {edit: false, add: false, del: false, search: true},
                            {},
                            {},
                            {},
                            {
                                sopt: ['eq', 'ne', 'lt', 'gt', 'cn', 'bw', 'ew'],
                                closeAfterSearch: false,
                                zIndex: 9999,
                                reloadAfterSubmit: true
                            }
                    );
            grid.jqGrid('setFrozenColumns');
        }
    });
</script>

<div id="flow-content">
    <div>
        <input type="text" id="rowId" placeholder="table row id" />
        <input type="text" id="version" placeholder="version" />
        <input type="button" id="send" value="send" />
    </div>
    <table id="list"></table> 
    <div id="pager"></div>
</div>