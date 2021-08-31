<%-- 
    Document   : audit
    Created on : 2017/4/25, 下午 02:58:21
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="<c:url value="/webjars/free-jqgrid/4.14.1/plugins/css/ui.multiselect.min.css" />" rel="stylesheet">
<script src="<c:url value="/js/jqgrid-custom-select-option-reader.js" />"></script>
<script src="<c:url value="/js/moment.js" />"></script>
<script src="<c:url value="/webjars/free-jqgrid/4.14.1/plugins/min/ui.multiselect.js" />"></script>
<script>
    $(function () {
        var grid = $("#list");
        var isGridInitialized = false;
        var today = moment().toDate();
        var yesterday = moment().add(-1, 'days').toDate();
        var lastsel;

        $("#sD").datepicker({dateFormat: 'yy-mm-dd', defaultDate: yesterday}).datepicker("setDate", yesterday);
        $("#eD").datepicker({dateFormat: 'yy-mm-dd', defaultDate: today}).datepicker("setDate", today);

        setSelectOptions({
            rootUrl: "<c:url value="/" />",
            columnInfo: [
                {name: "businessGroup", isNullable: false},
                {name: "workCenter", isNullable: false},
                {name: "floor", isNullable: false},
                {name: "user", nameprefix: "spe_", isNullable: false, dataToServer: "SPE"},
                {name: "user", nameprefix: "ee_", isNullable: false, dataToServer: "EE"},
                {name: "user", nameprefix: "qc_", isNullable: false, dataToServer: "QC"},
                {name: "user", nameprefix: "mpm_", isNullable: true, dataToServer: "MPM"},
                {name: "type", isNullable: false},
                {name: "flow", nameprefix: "bab_", isNullable: false, dataToServer: "1"},
                {name: "flow", nameprefix: "test_", isNullable: true, dataToServer: "3"},
                {name: "flow", nameprefix: "pkg_", isNullable: true, dataToServer: "2"},
                {name: "preAssy", isNullable: true},
                {name: "pending", isNullable: false},
                {name: "remark", isNullable: false}
            ]
        });

        $("#send").click(function () {
            var id = $("#id").val();
            var modelName = $("#modelName").val();
            var version = $("#version").val();
            var startDate = $("#sD").val();
            var endDate = $("#eD").val();

            if (!isGridInitialized) {
                getEditRecord(id, modelName, version, startDate, endDate); //init the table
                isGridInitialized = true;
            } else {
                grid.jqGrid('clearGridData');
                grid.jqGrid('setGridParam', {url: '<c:url value="/Audit/find" />', postData: {id: id, modelName: modelName, version: version, startDate: startDate, endDate: endDate}});
                grid.setGridParam({grouping: false});
                grid.trigger('reloadGrid');
            }
        });

        function timestampFormat(cellvalue, options, rowObject) {
            var t = moment(cellvalue);
            return t.format('YYYY-MM-DD H:mm:ss');
        }

        function getEditRecord(id, modelName, version, startDate, endDate) {
            grid.jqGrid({
                url: '<c:url value="/Audit/find" />',
                postData: {
                    id: id,
                    modelName: modelName,
                    version: version,
                    startDate: startDate,
                    endDate: endDate
                },
                datatype: 'json',
                mtype: 'GET',
                autoencode: true,
                colModel: [
                    {label: 'CPK', name: '0.cpk', width: 60, key: true, frozen: true, hidden: true, search: false, sortable: false},
                    {label: 'revtstmp', name: "REVTSTMP", jsonmap: "1.revtstmp", width: 120, frozen: true, hidden: false, search: false, formatter: timestampFormat},
                    {label: 'REV', name: "REV", jsonmap: "1.rev", width: 60, frozen: true, hidden: true, search: false},
                    {label: 'username', name: "username", jsonmap: "1.username", width: 60, frozen: true, hidden: false, search: false},
                    {label: 'REVTYPE', name: "REVTYPE", jsonmap: "2", width: 60, frozen: true, hidden: false, search: false},
                    {label: 'id', name: "id", jsonmap: "0.id", width: 60, frozen: true, hidden: false, search: false},
                    {label: 'Model', name: "modelName", jsonmap: "0.modelName", frozen: true, searchrules: {required: true}, searchoptions: search_string_options, formoptions: required_form_options},
                    {label: 'TYPE', name: "type_id", jsonmap: "0.type.id", formatter: selectOptions["type_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'BU', name: "businessGroup_id", jsonmap: "0.businessGroup.id", formatter: selectOptions["businessGroup_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'Work Center', name: "workCenter_id", jsonmap: "0.workCenter.id", formatter: selectOptions["workCenter_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'ProductionWT', name: "productionWt", jsonmap: "0.productionWt", width: 120, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Setup Time', name: "setupTime", jsonmap: "0.setupTime", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'AR film attachment', name: "arFilmAttachment", jsonmap: "0.arFilmAttachment", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'SEAL', name: "seal", jsonmap: "0.seal", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'OB', name: "opticalBonding", jsonmap: "0.opticalBonding", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '壓力鍋', name: "pressureCooker", jsonmap: "0.pressureCooker", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'CleanPanel', name: "cleanPanel", jsonmap: "0.cleanPanel", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'PI', name: "pi", jsonmap: "0.pi", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '高亮', name: "highBright", jsonmap: "0.highBright", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '組裝', name: "assy", jsonmap: "0.assy", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '封/貼前框', name: "bondedSealingFrame", jsonmap: "0.bondedSealingFrame", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'T1', name: "t1", jsonmap: "0.t1", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'T2', name: "t2", jsonmap: "0.t2", width: 60, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Packing', name: "packing", jsonmap: "0.packing", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Up_BI_RI', name: "upBiRi", jsonmap: "0.upBiRi", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Down_BI_RI', name: "downBiRi", jsonmap: "0.downBiRi", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'BI Cost', name: "biCost", jsonmap: "0.biCost", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'ASS_T1', name: "assyToT1", jsonmap: "0.assyToT1", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'T2_PACKING', name: "t2ToPacking", jsonmap: "0.t2ToPacking", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Floor', name: "floor_id", jsonmap: "0.floor.id", width: 100, searchrules: {required: true}, searchoptions: search_string_options, formatter: selectOptions["floor_func"]},
                    {label: 'Pending', name: "pending_id", jsonmap: "0.pending.id", formatter: selectOptions["pending_func"], width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'Pending TIME', name: "pendingTime", jsonmap: "0.pendingTime", width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, formoptions: required_form_options},
                    {label: 'BurnIn', name: "burnIn", jsonmap: "0.burnIn", width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'B/I Time', name: "biTime", jsonmap: "0.biTime", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: required_form_options},
                    {label: 'BI_Temperature', name: "biTemperature", jsonmap: "0.biTemperature", width: 120, searchrules: number_search_rule, searchoptions: search_decimal_options, formoptions: required_form_options},
                    {label: 'SPE Owner', name: "userBySpeOwnerId_id", jsonmap: "0.userBySpeOwnerId.id", formatter: selectOptions["spe_user_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'EE Owner', name: "userByEeOwnerId_id", jsonmap: "0.userByEeOwnerId.id", formatter: selectOptions["ee_user_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'QC Owner', name: "userByQcOwnerId_id", jsonmap: "0.userByQcOwnerId.id", formatter: selectOptions["qc_user_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'MPM Owner', name: "userByMpmOwnerId_id", jsonmap: "0.userByMpmOwnerId.id", formatter: selectOptions["mpm_user_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: '組包SOP', name: "assyPackingSop", jsonmap: "0.assyPackingSop", width: 100, searchrules: {required: true}, searchoptions: search_string_options, edittype: "textarea", editoptions: {maxlength: 500}},
                    {label: '測試SOP', name: "testSop", jsonmap: "0.testSop", width: 100, searchrules: {required: true}, searchoptions: search_string_options, edittype: "textarea", editoptions: {maxlength: 500}},
                    {label: 'KEYPART_A', name: "keypartA", jsonmap: "0.keypartA", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'KEYPART_B', name: "keypartB", jsonmap: "0.keypartB", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'PRE-ASSY', name: "preAssy_id", jsonmap: "0.preAssy.id", formatter: selectOptions["preAssy_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'BAB_FLOW', name: "flowByBabFlowId_id", jsonmap: "0.flowByBabFlowId.id", formatter: selectOptions["bab_flow_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'TEST_FLOW', name: "flowByTestFlowId_id", jsonmap: "0.flowByTestFlowId.id", formatter: selectOptions["test_flow_func"], width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'PACKING_FLOW', name: "flowByPackingFlowId_id", jsonmap: "0.flowByPackingFlowId.id", formatter: selectOptions["pkg_flow_func"], width: 140, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'PART-LINK', name: "partLink", jsonmap: "0.partLink", width: 100, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'Remark', name: "remark_id", jsonmap: "0.remark.id", width: 100, formatter: selectOptions["remark_func"], searchrules: {required: true}, searchoptions: search_string_options},
                    {label: 'CE', name: "ce", jsonmap: "0.ce", width: 60, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'UL', name: "ul", jsonmap: "0.ul", width: 60, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'ROHS', name: "rohs", jsonmap: "0.rohs", width: 60, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'WEEE', name: "weee", jsonmap: "0.weee", width: 60, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'Made in Taiwan', name: "madeInTaiwan", jsonmap: "0.madeInTaiwan", width: 120, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'FCC', name: "fcc", jsonmap: "0.fcc", width: 60, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'EAC', name: "eac", jsonmap: "0.eac", width: 60, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'KC', name: "kc", jsonmap: "0.kc", width: 60, searchrules: number_search_rule, searchoptions: search_string_options},
                    {label: 'N合1集合箱', name: "nsInOneCollectionBox", jsonmap: "0.nsInOneCollectionBox", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '料號屬性值維護', name: "partNoAttributeMaintain", jsonmap: "0.partNoAttributeMaintain", width: 120, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: '啟用料號屬性', name: "labelInformation", jsonmap: "0.partNoAttributeMaintain", width: 120, searchrules: {required: true}, searchoptions: search_string_options},
                    {label: '禮盒總重量(含配件)', name: "weight", jsonmap: "0.weight", width: 120, searchrules: {required: true}, searchoptions: search_decimal_options},
                    {label: '整箱總重量誤差值', name: "tolerance", jsonmap: "0.tolerance", width: 120, searchrules: {required: true}, searchoptions: search_decimal_options},
                    {label: 'A膠溶劑量', name: "materialVolumeA", jsonmap: "0.materialVolumeA", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: 'B膠溶劑量', name: "materialVolumeB", jsonmap: "0.materialVolumeB", width: 100, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '前置時間', name: "assyLeadTime", jsonmap: "0.assyLeadTime", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '測試工時', name: "test", jsonmap: "0.test", width: 80, searchrules: number_search_rule, searchoptions: search_decimal_options},
                    {label: '自動化人機協作', width: 200, name: "hrcValues", jsonmap: "0.hrcValues", index: "hrcValues", align: 'center'}
                ],
                rowNum: 100,
                rowList: [100, 200, 500, 1000],
                pager: '#pager',
                viewrecords: true,
                autowidth: true,
                shrinkToFit: false,
                hidegrid: true,
                stringResult: true,
                multiselect: true,
                multiPageSelection: true,
                jsonReader: {
                    root: "rows",
                    page: "page",
                    total: "total",
                    records: "records",
                    repeatitems: false
                },
                beforeSelectRow: function (rowid, e) {
                    e.preventDefault();
                },
                onSelectRow: function (rowId, status, e) {
                    var gridSelRow = grid.getGridParam('selrow');
                    var s;
                    s = grid.getGridParam('selarrrow');
                    if (!s || !s[0]) {
                        grid.resetSelection();
                        lastsel = null;
                        return;
                    }
                    var selected = $.inArray(rowId, s) >= 2;

                    if (rowId && rowId !== lastsel && selected) {

                        if (lastsel)
                            grid.setSelection(lastsel, false);
                    }
                    lastsel = rowId;
                },
                beforeProcessing: function (data) {
                    //Generate pk by jqgrid because database only provide cpk(REV & worktime_id).
                    var arr = data.rows;
                    for (var i = 0; i < arr.length; i++) {
                        var innerData = arr[i];
                        innerData[0].cpk = $.jgrid.randId();
                    }
                },
                navOptions: {reloadGridOptions: {fromServer: true}},
                caption: "Worktime_AUD",
                height: 450,
                sortname: 'REVTSTMP', sortorder: 'desc',
                error: function (xhr, ajaxOptions, thrownError) {
                    alert("Ajax Error occurred\n"
                            + "\nstatus is: " + xhr.status
                            + "\nthrownError is: " + thrownError
                            + "\najaxOptions is: " + ajaxOptions
                            );
                }
            });
            $("#cb_" + grid[0].id).hide();
            grid.jqGrid('setFrozenColumns');
        }
    });
</script>

<div id="flow-content">
    <h4>大表版本歷史紀錄查詢</h4>
    <div class="form-inline">
        <input type="text" id="id" class="form-control" placeholder="id" />
        <input type="text" id="modelName" class="form-control" placeholder="modelName" />
        <input type="text" id="version" class="form-control" placeholder="version" style="display: none"/>
        <input type="text" id="sD" name="startDate" placeholder="startDate" class="form-control" />
        <input type="text" id="eD" name="endDate" placeholder="endDate" class="form-control" />
        <input type="button" id="send" class="form-control" value="send" />
        <h5 class="form-control alert">※id or modelName 不指定請留白</h5>
    </div>
    <table id="list"></table> 
    <div id="pager"></div>
</div>