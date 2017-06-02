var grid_column_name = [
    'id', 'modelName', 'type.id', 'productionWt', 'totalModule',
    'setupTime', 'cleanPanel', 'assy', 't1', 't2',
    't3', 't4', 'packing', 'upBiRi', 'downBiRi',
    'biCost', 'vibration', 'hiPotLeakage', 'coldBoot', 'warmBoot',
    'assyToT1', 't2ToPacking', 'floor.id', 'pending.id', 'pendingTime',
    'burnIn', 'biTime', 'biTemperature', 'identitBySpeOwnerId.id', 'identitByEeOwnerId.id',
    'identitByQcOwnerId.id', 'assyPackingSop', 'testSop', 'keypartA', 'keypartB',
    'preAssy.id', 'flowByBabFlowId.id', 'flowByTestFlowId.id', 'flowByPackingFlowId.id', 'partLink',
    'ce', 'ul', 'rohs', 'weee', 'madeInTaiwan',
    'fcc', 'eac', 'nInOneCollectionBox', 'partNoAttributeMaintain', 'assyStation',
    'packingStation', 'assyLeadTime', 'assyKanbanTime', 'packingLeadTime', 'packingKanbanTime',
    'cleanPanelAndAssembly', 'modifiedDate'
];

var do_not_change_columns = [
    "id", "rowId", "modifiedDate"
];

var specialRelativeColumn = {
    SPE: [
        "type", "floor", "eeOwner", "speOwner", "qcOwner",
        "babFlow", "testFlow", "packingFlow", "pending", "preAssy"
    ],
    EE: [],
    IE: []
};

var formulaColumn = [
    "productionWt",
    "setupTime",
    "assyToT1",
    "t2ToPacking",
    "assyStation",
    "packingStation",
    "assyKanbanTime",
    "packingKanbanTime",
    "cleanPanelAndAssembly"
];