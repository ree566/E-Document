//Custom param
var not_null_and_zero_message = "需有值，不可為0";
var when_not_empty_or_null = "不等於0時";
var preAssy = "preAssy\\.id",
        babFlow = "flowByBabFlowId\\.id",
        testFlow = "flowByTestFlowId\\.id",
        packingFlow = "flowByPackingFlowId\\.id";

//Other field check logic
var notZeroOrNull = function (obj) {
    return obj != null && obj != 0;
};

var needBI = function (obj) {
    return obj != null && obj == 'BI';
};

var needRI = function (obj) {
    return obj != null && obj == 'RI';
};

//Flow check logic setting
var flow_check_logic = {
    "PRE-ASSY": [
        {keyword: ["PRE_ASSY"], checkColumn: ["cleanPanel", "totalModule"], checkType: "OR", message: not_null_and_zero_message, prmValid: notZeroOrNull}
    ],
    BAB: [
        {keyword: ["ASSY"], checkColumn: ["assy"], message: not_null_and_zero_message, prmValid: notZeroOrNull},
        {keyword: ["T1"], checkColumn: ["t1"], message: not_null_and_zero_message, prmValid: notZeroOrNull},
        {keyword: ["VB"], checkColumn: ["vibration"], message: not_null_and_zero_message, prmValid: notZeroOrNull},
//        {keyword: ["H1", " LK"], checkColumn: ["hiPotLeakage", "acwVoltage"], message: not_null_and_zero_message, prmValid: notZeroOrNull},
//        {keyword: ["LK"], checkColumn: ["irVoltage", "testProfile", "lltValue"], message: not_null_and_zero_message, prmValid: notZeroOrNull},
        {keyword: ["H1", " LK"], checkColumn: ["hiPotLeakage"], message: not_null_and_zero_message, prmValid: notZeroOrNull},
        {keyword: ["LK"], checkColumn: ["acwVoltage", "irVoltage", "testProfile", "lltValue"], message: not_null_and_zero_message, prmValid: notZeroOrNull},
        {keyword: ["CB"], checkColumn: ["coldBoot"], message: not_null_and_zero_message, prmValid: notZeroOrNull},
        {keyword: ["BI", "RI"], checkColumn: ["upBiRi", "downBiRi", "biCost"], message: not_null_and_zero_message, prmValid: notZeroOrNull},
        {keyword: ["BI"], checkColumn: ["burnIn"], message: "內容須為BI", prmValid: needBI},
        {keyword: ["RI"], checkColumn: ["burnIn"], message: "內容須為RI", prmValid: needRI}
    ],
    TEST: [
        {keyword: ["T2"], checkColumn: ["t2"], message: not_null_and_zero_message, prmValid: notZeroOrNull},
        {keyword: ["T3"], checkColumn: ["t3"], message: not_null_and_zero_message, prmValid: notZeroOrNull},
        {keyword: ["T4"], checkColumn: ["t4"], message: not_null_and_zero_message, prmValid: notZeroOrNull}
    ],
    PKG: [
        {keyword: ["PKG"], checkColumn: ["packing"], message: not_null_and_zero_message, prmValid: notZeroOrNull},
        {keyword: ["PI-PKG(WET)"], checkColumn: ["weight"], message: not_null_and_zero_message, prmValid: notZeroOrNull}
    ]
};

var field_check_flow_logic = [
    {checkColumn: {name: ["cleanPanel"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: preAssy, keyword: ["PRE_ASSY"]}},
    {checkColumn: {name: ["totalModule"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: preAssy, keyword: ["PRE_ASSY"]}},
    {checkColumn: {name: ["assy"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: babFlow, keyword: ["ASSY"]}},
    {checkColumn: {name: ["t1"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: babFlow, keyword: ["T1"]}},
    {checkColumn: {name: ["vibration"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: babFlow, keyword: ["VB"]}},
    {checkColumn: {name: ["hiPotLeakage"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: babFlow, keyword: ["H1", "LK"]}},
    //{checkColumn: {name: ["acwVoltage"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: babFlow, keyword: ["H1"]}},
    {checkColumn: {name: ["acwVoltage", "irVoltage", "testProfile", "lltValue"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: babFlow, keyword: ["LK"]}},
    {checkColumn: {name: ["coldBoot"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: babFlow, keyword: ["CB"]}},
    {checkColumn: {name: ["upBiRi"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: babFlow, keyword: ["BI", "RI"]}},
    {checkColumn: {name: ["downBiRi"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: babFlow, keyword: ["BI", "RI"]}},
    {checkColumn: {name: ["biCost"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: babFlow, keyword: ["BI", "RI"]}},
    {checkColumn: {name: ["burnIn"], equals: true, value: "BI"}, description: "內容為BI", targetColumn: {name: babFlow, keyword: ["BI"]}},
    {checkColumn: {name: ["burnIn"], equals: true, value: "RI"}, description: "內容為RI", targetColumn: {name: babFlow, keyword: ["RI"]}},
    {checkColumn: {name: ["t2"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: testFlow, keyword: ["T2"]}},
    {checkColumn: {name: ["t3"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: testFlow, keyword: ["T3"]}},
    {checkColumn: {name: ["t4"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: testFlow, keyword: ["T4"]}},
    {checkColumn: {name: ["packing"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: packingFlow, keyword: ["PKG"]}},
    {checkColumn: {name: ["weight"], equals: false, value: 0}, description: when_not_empty_or_null, targetColumn: {name: packingFlow, keyword: ["PI-PKG(WET)"]}}
];

//Flow check logic
function fieldCheck(postdata, preAssyVal, babFlowVal, testFlowVal, packingFlowVal) {
    var validationErrors = [];
    for (var i = 0; i < field_check_flow_logic.length; i++) {
        var logic = field_check_flow_logic[i];
        var colInfo = logic.checkColumn;

        var col = colInfo.name;
        var checkBool = colInfo.equals;
        var checkVal = colInfo.value;

        var description = logic.description;

        var targetColInfo = logic.targetColumn;
        var targetColName = targetColInfo.name;
        var targetKeyword = targetColInfo.keyword;
        var targetColVal;

        switch (targetColName) {
            case preAssy:
                targetColVal = preAssyVal;
                break;
            case babFlow:
                targetColVal = babFlowVal;
                break;
            case testFlow:
                targetColVal = testFlowVal;
                break;
            case packingFlow:
                targetColVal = packingFlowVal;
                break;
            default:
                throw 'TargetColName not found!';
        }

        //Check col array's value all equals or not equals to checkCol.val
        var isMatchesRule = col.every(function (el, index, arr) {
            var colName = arr[index];
            var fieldVal = postdata[colName];
            return fieldVal != null && (checkBool == true ? fieldVal == checkVal : fieldVal != checkVal);
        });

        if (checkBool != null) {
            var errorResult = checkFlow(
                    isMatchesRule,
                    targetColName, targetColVal, targetKeyword);
            if (errorResult.field != null) {
                appendFieldInfo(col, description, errorResult);
                validationErrors.push(errorResult);
            }
        } else {
            throw "CheckBool is not setting!";
        }

    }
    return validationErrors;
}

function appendFieldInfo(field, description, error) {
    error.code = field + description + ' , ' + error.code;
}

function checkFlow(bool, targetColName, targetColVal, keyword) {
    var err = {};
    if (bool) {
        if (targetColVal != null && targetColVal != "") {
            var keyCheckFlag = false;
            for (var i = 0; i < keyword.length; i++) {
                if (targetColVal.indexOf(keyword[i]) > -1) {
                    keyCheckFlag = true;
                    break;
                }
            }
            if (keyCheckFlag == false) {
                err.field = targetColName;
                err.code = 'flow must conain: ' + keyword;
            }
        } else {
            err.field = targetColName;
            err.code = 'flow must conain: ' + keyword;
        }
    }
    return err;
}

//Model

//Check logic setting
var modelName_check_logic = [
    {keyword: "ES", checkColumn: ["businessGroup\\.id"], message: "Must contain \"ES\""}
];

var field_check_modelName_logic = [
    {checkColumn: {label: "BU", name: "businessGroup\\.id", value: "ES"}, description: "內容為ES", targetColumn: {name: "modelName", keyword: ["ES"]}}
//    {checkColumn: {name: "workCenter", value: "ES"}, description: "內容為ES", targetColumn: {name: "modelName", keyword: ["ES"]}}
];

//Check logic
function modelNameCheckFieldIsValid(data) {
    var validationErrors = [];
    var modelName = data["modelName"];
    for (var i = 0; i < modelName_check_logic.length; i++) {
        var logic = modelName_check_logic[i];
        var keyword = logic.keyword;
        if (modelName.endsWith(keyword) == false) {
            continue;
        }
        var checkCols = logic.checkColumn;
        for (var j = 0, k = checkCols.length; j < k; j++) {
            var colName = checkCols[j];
            var checkVal = data[colName];
            if (checkVal.indexOf(keyword) == -1) {
                var err = {};
                err.field = colName;
                err.code = logic.message;
                validationErrors.push(err);
            }
        }
    }
    return validationErrors;
}

function checkModelNameIsValid(data) {
    var validationErrors = [];
    for (var i = 0; i < field_check_modelName_logic.length; i++) {
        var logic = field_check_modelName_logic[i];
        var checkColInfo = logic.checkColumn;
        var isNeedToCheck = data[checkColInfo.name].indexOf(checkColInfo.value) != -1;
        if (isNeedToCheck) {
            var targetColInfo = logic.targetColumn;
            var targetColName = targetColInfo.name;
            var colVal = data[targetColName];
            if (colVal.endsWith(("-" + targetColInfo.keyword)) == false) {
                var err = {};
                err.field = targetColName;
                err.code = targetColName + " must contain " + targetColInfo.keyword;
                appendFieldInfo(checkColInfo.label, logic.description, err);
                validationErrors.push(err);
            }
        }
    }
    return validationErrors;
}

function registerEndsWithIfIe() {
    //https://developer.mozilla.org/en/docs/Web/JavaScript/Reference/Global_Objects/String/endsWith
    if (!String.prototype.endsWith) {
        String.prototype.endsWith = function (searchString, position) {
            var subjectString = this.toString();
            if (typeof position !== 'number' || !isFinite(position) || Math.floor(position) !== position || position > subjectString.length) {
                position = subjectString.length;
            }
            position -= searchString.length;
            var lastIndex = subjectString.indexOf(searchString, position);
            return lastIndex !== -1 && lastIndex === position;
        };
    }
}