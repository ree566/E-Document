var ltrimReg = /^\s+/;
var rtrimReg = /\s+$/;
var ltrimAndRtrim = function (str) {
    return str.replace(ltrimReg, "").replace(rtrimReg, "");
};
var modelNameFormat = function () {
    $("#modelName").keyup(function (e) {
        var textbox = $(this);
        textbox.val(ltrimAndRtrim(textbox.val()));
    });
};

var autoUpperCase = function (el) {
    $(el).css('text-transform', 'uppercase');
};