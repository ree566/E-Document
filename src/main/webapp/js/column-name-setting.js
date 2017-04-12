var grid_column_name = [
    'rowId', 'modelName', 'typeName', 'productionWt', 'totalModule',
    'setupTime', 'cleanPanel', 'assy', 't1', 't2',
    't3', 't4', 'packing', 'upBiRi', 'downBiRi',
    'biCost', 'vibration', 'hiPotLeakage', 'coldBoot', 'warmBoot',
    'assyToT1', 't2ToPacking', 'floorName', 'pendingName', 'pendingTime',
    'burnIn', 'biTime', 'biTemperature', 'speOwnerName', 'eeOwnerName',
    'qcOwnerName', 'assyPackingSop', 'testSop', 'keypartA', 'keypartB',
    'preAssyName', 'babFlowName', 'testFlowName', 'packingFlowName', 'partLink',
    'ce', 'ul', 'rohs', 'weee', 'madeInTaiwan',
    'fcc', 'eac', 'nInOneCollectionBox', 'partNoAttributeMaintain', 'assyStation',
    'packingStation', 'assyLeadTime', 'assyKanbanTime', 'packingLeadTime', 'packingKanbanTime',
    'cleanPanelAndAssembly', 'modifiedDate'
];

var do_not_change_columns = [
    "id", "modifiedDate", "cleanPanelAndAssembly", "productionWt", "setupTime", 
    "assyToT1", "t2ToPacking", "assyStation", "packingStation", "assyKanbanTime", 
    "packingKanbanTime"
];

var specialRelativeColumn = {
    SPE: [
        "type", "floor", "eeOwner", "speOwner", "qcOwner", 
        "babFlow", "testFlow", "packingFlow", "pending", "preAssy"
    ], 
    EE: [],
    IE: []
};