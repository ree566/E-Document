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
        type: 'change', fn: function (e) {
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

var businessGroup_select_event = [
    {
        type: 'change', fn: function (e) {
            var selectOption = $('option:selected', this).text();
            var defaultValue = {
                ECG: "LCD_ENHS",
                ES: "LCD_ES",
                Module: "LCD_MOD"
            };
            $('input#workCenter').val(defaultValue[selectOption]);
        }
    }
];