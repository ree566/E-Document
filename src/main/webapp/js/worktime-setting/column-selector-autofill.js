//給予大表下拉式選單auto event

var burnIn_select_event = [
    {
        type: 'change',
        fn: function (e) {
            var selectOption = $('option:selected', this).text();
            var defaultValue = {
                BI: [4, 40],
                RI: [4, 0],
                N: [0, 0]
            };
            $('input#biTime').val(defaultValue[selectOption][0]);
            $('input#biTemperature').val(defaultValue[selectOption][1]);
        }
    }
];

var testProfile_select_event = [
    {
        type: 'change',
        fn: function (e) {
            var selectOption = $('option:selected', this).text();
            var defaultValue = {
                "0": ["0", "0", "0", "0"],
                "M5": ["1500", "25", "500", "3500,3500,3500,3500"],
                "601": ["1776", "0", "500", "100,500,500,1000,500,100,500,500,1000,500"],
                "B-B": ["2500", "0", "0", "0"]
            };
            $('input#acwVoltage').val(defaultValue[selectOption][0]);
            $('input#gndValue').val(defaultValue[selectOption][1]);
            $('input#irVoltage').val(defaultValue[selectOption][2]);
            $('input#lltValue').val(defaultValue[selectOption][3]);
        }
    }
];

var pending_select_event = [
    {
        type: 'change',
        fn: function (e) {
            var selectOption = $('option:selected', this).text();
            $('input#pendingTime').val(selectOption == 'N' ? 0 : '');
        }
    }
];

var babFlow_select_event = [
    {
        type: 'change',
        fn: function (e) {
            flow_LK_hint();
            var sel2 = $("#flowByTestFlowId\\.id");
            var sel2Val = sel2.val();
            $.get('../SelectOption/flow-byParent/' + $(this).val(), function (data) {
                sel2.html("");
                sel2.append("<option role='option' value=0>empty</option>");
                for (var i = 0; i < data.length; i++) {
                    sel2.append("<option role='option' value=" + data[i].id + ">" + data[i].name + "</option>");
                }
                sel2.val(sel2Val);
            });
        }
    }
];

var testFlow_select_event = [
    {
        type: 'change',
        fn: function (e) {
            flow_LK_hint();
        }
    }
];

var flow_LK_hint = function (sel) {
    var selVal = $("#flowByBabFlowId\\.id :selected").text();
    var selVal2 = $("#flowByTestFlowId\\.id :selected").text();
    var keypartField = $("#keypartB");
    keypartField.parent().find("b").remove();
    if ((selVal != null && selVal.includes("LK")) || (selVal2 != null && selVal2.includes("LK"))) {
        keypartField.after("<b style='color:red'>後段Key part?</b>");
    }
};

var businessGroup_select_event = [
    {
        type: 'change',
        fn: function (e) {
            var selectOption = $('option:selected', this).text();
            var defaultValue = {
                IAG: "ASSY",
                EDIS: "ASS-01",
                SAG: "ASS-02",
                ES: "ES"
            };
            $('input#workCenter').val(defaultValue[selectOption]);
        }
    }
];

var nsInOneCollectionBox_change_event = [
    {
        type: 'change',
        fn: function (e) {
            var t = $(this).val();
            $('input#weightAff').val(t >= 2 ? 0.05 : 0);
        }
    },
    {
        type: 'keyup',
        fn: function (e) {
            var t = $(this).val();
            $('input#weightAff').val(t >= 2 ? 0.05 : 0);
        }
    }
];
