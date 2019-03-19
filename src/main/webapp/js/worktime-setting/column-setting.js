//Js檔用來給予下拉式表單使用(unit可編輯欄位需要區隔，記錄在db之中)
//Js檔用來給予下拉式表單使用(unit可編輯欄位需要區隔，記錄在db之中)
var worktimeCol = [
    {name: "id", editable: false},
    {name: "modelName"},
    {name: "type.id"},
    {name: "workCenter.id"},
    {name: "productionWt"},
    {name: "setupTime"},                                                                                                                                                                  
    {name: "assy"},
    {name: "t2"},
    {name: "packing"},
    {name: "upBiRi"},
    {name: "downBiRi"},
    {name: "biCost"},
    {name: "hiPotLeakage"},
    {name: "floor.id"},
    {name: "burnIn"},
    {name: "biTime"},
    {name: "biTemperature"},
    {name: "userBySpeOwnerId.id"},
    {name: "userByEeOwnerId.id"},
    {name: "userByQcOwnerId.id"},
    {name: "assyPackingSop"},
    {name: "testSop"},
    {name: "keypartA"},
    {name: "keypartB"},
    {name: "flowByBabFlowId.id"},
    {name: "flowByTestFlowId.id"},
    {name: "flowByPackingFlowId.id"},
    {name: "partLink"},
    {name: "ce"},
    {name: "ul"},
    {name: "rohs"},
    {name: "madeInTaiwan"},
    {name: "fcc"},
    {name: "eac"},
    {name: "kc"},
    {name: "poCategory"},
    {name: "nsInOneCollectionBox"},
    {name: "partNoAttributeMaintain"},
    {name: "testProfile"},
    {name: "acwVoltage"},
    {name: "irVoltage"},
    {name: "gndValue"},
    {name: "lltValue"},
    {name: "weight"},
    {name: "tolerance"},
    {name: "assyStation"},
    {name: "packingStation"},
    {name: "modifiedDate", editable: false}
//    {name: "bwFields.0.assyAvg", editable: false},
//    {name: "bwFields.0.packingAvg", editable: false},

];


//不受show / hide 影響
var do_not_change_columns = [
    "id", "rowId", "createDate", "modifiedDate", "bwFields.0.assyAvg", "bwFields.0.packingAvg"
];

//指定的column要有checkbox
var formulaColumn = [
    "productionWt",
    "setupTime",
    "assyStation",
    "packingStation"
];
