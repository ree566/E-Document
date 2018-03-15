//Js檔用來給予下拉式表單使用(unit可編輯欄位需要區隔，記錄在db之中)
//Js檔用來給予下拉式表單使用(unit可編輯欄位需要區隔，記錄在db之中)
var worktimeCol = [
    {name: "id", editable: false},
    {name: "modelName"},
    {name: "type.id"},
    {name: "productionWt"},
    {name: "setupTime"},                                                                                                                                                                  
    {name: "arFilmAttachment"},                                                                                                                                                                  
    {name: "seal"},
    {name: "opticalBonding"},
    {name: "pressureCooker"},
    {name: "cleanPanel"},
    {name: "pi"},
    {name: "assy"},
    {name: "t1"},
    {name: "t2"},
    {name: "packing"},
    {name: "upBiRi"},
    {name: "downBiRi"},
    {name: "biCost"},
    {name: "vibration"},
    {name: "hiPotLeakage"},
    {name: "coldBoot"},
    {name: "warmBoot"},
    {name: "assyToT1"},
    {name: "t2ToPacking"},
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
    {name: "preAssy.id"},
    {name: "flowByBabFlowId.id"},
    {name: "flowByTestFlowId.id"},
    {name: "flowByPackingFlowId.id"},
    {name: "partLink"},
    {name: "remark"},
    {name: "ce"},
    {name: "ul"},
    {name: "rohs"},
    {name: "weee"},
    {name: "madeInTaiwan"},
    {name: "fcc"},
    {name: "eac"},
    {name: "kc"},
    {name: "nsInOneCollectionBox"},
    {name: "partNoAttributeMaintain"},
    {name: "labelInformation"},
    {name: "weight"},
    {name: "tolerance"},
    {name: "materialVolumeA"},
    {name: "materialVolumeB"},
    {name: "assyLeadTime"},
    {name: "test"},
    {name: "modifiedDate", editable: false},
//    {name: "bwFields.0.assyAvg", editable: false},
//    {name: "bwFields.0.packingAvg", editable: false},
    {name: "businessGroup.id"},
    {name: "workCenter"}

];


//不受show / hide 影響
var do_not_change_columns = [
    "id", "rowId", "createDate", "modifiedDate", "bwFields.0.assyAvg", "bwFields.0.packingAvg"
];

//指定的column要有checkbox
var formulaColumn = [
    "productionWt",
    "setupTime",
    "assyToT1",
    "t2ToPacking",
    "assyLeadTime",
    "test"
];
