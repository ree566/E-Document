<%-- 
    Document   : index
    Created on : 2017/3/6, 下午 02:38:11
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="images/favicon.ico"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/jquery-ui-1.10.0.custom.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="css/ui.jqgrid.css" />
        <style>
            #server_resp{
                font-size: 8px;
            }
        </style>
        <script src="js/jquery-1.11.3.min.js"></script>
        <script src="js/i18n/grid.locale-cn.js"></script>
        <script src="js/jquery.jqGrid.min.js"></script>
        <script>
            $(function () {
                var lastsel, scrollPosition;
                $("#list").jqGrid({
                    url: 'SheetViewServlet',
                    datatype: "json",
                    mtype: 'POST',
                    colNames: [
                        'Model', 'TYPE', 'ProductionWT', 'Total Module', 'Setup Time',
                        'CleanPanel', 'Assembly', 'T1', 'T2', 'T3',
                        'T4', 'Packing', 'Up_BI_RI', 'Down_BI_RI', 'BI Cost',
                        'Vibration', 'Hi-Pot/Leakage', 'Cold Boot', 'Warm Boot', 'ASS_T1',
                        'T2_PACKING', 'Floor', 'Pending', 'Pending TIME', 'BurnIn',
                        'B/I Time', 'BI_Temperature', 'SPE Owner', 'EE Owner', 'QC Owner',
                        '組包SOP', '測試SOP', 'KEYPART_A', 'KEYPART_B', 'PRE-ASSY',
                        'BAB_FLOW', 'TEST_FLOW', 'PACKING_FLOW', 'PART-LINK',
                        'CE', 'UL', 'ROHS', 'WEEE', 'Made in Taiwan',
                        'FCC', 'EAC', 'N合1集合箱', '料號屬性值維護', '組裝排站人數',
                        '包裝排站人數', '前置時間', '看板工時', '前置時間', '看板工時',
                        'CleanPanel+Assembly', 'Modified_Date'
                    ],
                    colModel: [
                        {name: 'Model', index: 'Model', frozen: true},
                        {name: 'TYPE', index: 'TYPE', editable: true},
                        {name: 'ProductionWT', index: 'ProductionWT', editable: true},
                        {name: 'Total_Module', index: 'Total_Module', editable: true},
                        {name: 'SetupTime', index: 'SetupTime', editable: true},
                        {name: 'CleanPanel', index: 'CleanPanel', editable: true},
                        {name: 'Assembly', index: 'Assembly', editable: true},
                        {name: 'T1', index: 'T1', editable: true},
                        {name: 'T2', index: 'T2', editable: true},
                        {name: 'T3', index: 'T3', editable: true},
                        {name: 'T4', index: 'T4', editable: true},
                        {name: 'Packing', index: 'Packing', editable: true},
                        {name: 'Up_BI_RI', index: 'Up_BI_RI', editable: true},
                        {name: 'Down_BI_RI', index: 'Down_BI_RI', editable: true},
                        {name: 'BI_Cost', index: 'BI_Cost', editable: true},
                        {name: 'Vibration', index: 'Vibration', editable: true},
                        {name: 'Hi_Pot_or_Leakage', index: 'Hi_Pot_or_Leakage', editable: true},
                        {name: 'Cold_Boot', index: 'Cold_Boot', editable: true},
                        {name: 'Warm_Boot', index: 'Warm_Boot', editable: true},
                        {name: 'ASS_T1', index: 'ASS_T1', editable: true},
                        {name: 'T2_PACKING', index: 'T2_PACKING', editable: true},
                        {name: 'FLOOR', index: 'FLOOR', editable: true},
                        {name: 'Pending', index: 'Pending', editable: true},
                        {name: 'Pending TIME', index: 'Pending TIME', editable: true},
                        {name: 'BurnIn', index: 'BurnIn', editable: true},
                        {name: 'BI_Time', index: 'BI_Time', editable: true},
                        {name: 'BI_Temperature', index: 'BI_Temperature', editable: true},
                        {name: 'SPE_Owner', index: 'SPE_Owner', editable: true},
                        {name: 'EE_Owner', index: 'EE_Owner', editable: true},
                        {name: 'QC_Owner', index: 'QC_Owner', editable: true},
                        {name: 'ASSY_PKG_SOP', index: 'ASSY_PKG_SOP', editable: true},
                        {name: 'TEST_SOP', index: 'TEST_SOP', editable: true},
                        {name: 'KEYPART_A', index: 'KEYPART_A', editable: true},
                        {name: 'KEYPART_B', index: 'KEYPART_B', editable: true},
                        {name: 'PRE_ASSY', index: 'PRE_ASSY', editable: true},
                        {name: 'BAB_FLOW', index: 'BAB_FLOW', editable: true},
                        {name: 'TEST_FLOW', index: 'TEST_FLOW', editable: true},
                        {name: 'PACKING_FLOW', index: 'PACKING_FLOW', editable: true},
                        {name: 'PART_LINK', index: 'PART_LINK', editable: true},
                        {name: 'CE', index: 'CE', editable: true},
                        {name: 'UL', index: 'UL', editable: true},
                        {name: 'ROHS', index: 'ROHS', editable: true},
                        {name: 'WEEE', index: 'WEEE', editable: true},
                        {name: 'Made_in_Taiwan', index: 'Made_in_Taiwan', editable: true},
                        {name: 'FCC', index: 'FCC', editable: true},
                        {name: 'EAC', index: 'EAC', editable: true},
                        {name: 'N_in_1_collection_box', index: 'N_in_1_collection_box', editable: true},
                        {name: 'Material_Maintain_Property', index: 'Material_Maintain_Property', editable: true},
                        {name: 'Assembly_stations', index: 'Assembly_stations', editable: true},
                        {name: 'Packaging_stations', index: 'Packaging_stations', editable: true},
                        {name: 'Assembly_lead_time', index: 'Assembly_lead_time', editable: true},
                        {name: 'Assembly_kanban_time', index: 'Assembly_kanban_time', editable: true},
                        {name: 'Package_lead_time', index: 'Package_lead_time', editable: true},
                        {name: 'Package_kanban_time', index: 'Package_kanban_time', editable: true},
                        {name: 'CleanPanel and Assembly', index: 'CleanPanel and Assembly', editable: true},
                        {name: 'Modified_Date', index: 'Modified_Date'}
                    ],
                    rowNum: 10,
                    rowList: [10, 20, 30],
                    pager: '#pager',
                    viewrecords: true,
                    shrinkToFit: false,
                    jsonReader: {
                        repeatitems: false
                    },
                    onSelectRow: function (id) {
                        if (id && id !== lastsel) {
                            var scrollItem = $(this).closest(".ui-jqgrid-bdiv");
                            scrollPosition = scrollItem.scrollLeft();
                            $(this).restoreRow(lastsel);
                            $(this).editRow(id, true);
                            scrollItem.scrollLeft(scrollPosition);
                            lastsel = id;
                        }
                    },
                    loadonce: true,
                    caption: "工時大表",
                    height: 'auto',
                    width: '1200',
                    autowidth: false
                });


                $("#list").jqGrid('navGrid', '#pager', {edit: false, add: false, del: false});

                $("#list").jqGrid('setGroupHeaders', {
                    useColSpanStyle: true,
                    groupHeaders: [
                        {startColumnName: 'CE', numberOfColumns: 7, titleText: '<em>外箱Label產品資訊 (1：要印   0：不印)</em>'},
                        {startColumnName: 'Assembly_lead_time', numberOfColumns: 2, titleText: '<em>組裝看板工時</em>'},
                        {startColumnName: 'Package_lead_time', numberOfColumns: 2, titleText: '<em>包裝看板工時</em>'}
                    ]
                });

                $("#list").jqGrid('setFrozenColumns');
            });
        </script>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <table id="list"></table> 
                <div id="pager"></div>
            </div>
        </div>
    </body>
</html>
