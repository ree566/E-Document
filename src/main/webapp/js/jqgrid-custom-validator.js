var not_null_and_zero_message = "需有值，不可為0";
var flow_check_logic = {
    BAB: [
        {keyword: ["ASSY"], checkColumn: ["assy"], message: not_null_and_zero_message, valid: notZeroOrNull},
        {keyword: ["T1"], checkColumn: ["t1"], message: not_null_and_zero_message, valid: notZeroOrNull},
        {keyword: ["VB"], checkColumn: ["vibration"], message: not_null_and_zero_message, valid: notZeroOrNull},
        {keyword: ["H1", " LK"], checkColumn: ["hiPotLeakage"], message: not_null_and_zero_message, valid: notZeroOrNull},
        {keyword: ["CB"], checkColumn: ["coldBoot"], message: not_null_and_zero_message, valid: notZeroOrNull},
        {keyword: ["BI", "RI"], checkColumn: ["upBiRi", "downBiRi", "biCost"], message: not_null_and_zero_message, valid: notZeroOrNull},
        {keyword: ["BI"], checkColumn: ["burnIn"], message: "內容須為BI", valid: needBI},
        {keyword: ["RI"], checkColumn: ["burnIn"], message: "內容須為RI", valid: needRI}
    ],
    TEST: [
        {keyword: ["T2"], checkColumn: ["t2"], message: not_null_and_zero_message, valid: notZeroOrNull},
        {keyword: ["T3"], checkColumn: ["t3"], message: not_null_and_zero_message, valid: notZeroOrNull},
        {keyword: ["T4"], checkColumn: ["t4"], message: not_null_and_zero_message, valid: notZeroOrNull}
    ],
    PKG: [
        {keyword: ["PKG"], checkColumn: ["packing"], message: not_null_and_zero_message, valid: notZeroOrNull}
    ]
};

var notZeroOrNull = function (obj) {
    return obj != null && obj != 0;
};

var needBI = function (obj) {
    return obj != null && obj == 'BI';
};

var needRI = function(obj){
    return obj != null && obj == 'RI';
};