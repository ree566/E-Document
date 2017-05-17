var not_null_and_zero_message = "需有值，不可為0";
var flow_check_logic = {
    BAB: [
        {keyword: "ASSY", checkColumn: "assy", message: not_null_and_zero_message},
        {keyword: "T1", checkColumn: "t1", message: not_null_and_zero_message},
        {keyword: "VB", checkColumn: "vibration", message: not_null_and_zero_message},
        {keyword: ["H1", " LK"], checkColumn: "hiPotLeakage", message: not_null_and_zero_message},
        {keyword: "CB", checkColumn: "coldBoot", message: not_null_and_zero_message},
        {keyword: ["BI", "RI"], checkColumn: ["upBiRi", "downBiRi", "biCost"], message: not_null_and_zero_message},
        {keyword: "BI", checkColumn: "burnIn", message: "內容須為BI"},
        {keyword: "RI", checkColumn: "burnIn", message: "內容須為RI"}
    ],
    TEST: [
        {keyword: "T2", checkColumn: "t2", message: not_null_and_zero_message},
        {keyword: "T3", checkColumn: "t3", message: not_null_and_zero_message},
        {keyword: "T4", checkColumn: "t4", message: not_null_and_zero_message}
    ],
    PKG: [
        {keyword: "PKG", checkColumn: "packing", message: not_null_and_zero_message}
    ]
};