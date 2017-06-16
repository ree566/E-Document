var search_string_options = {sopt: ['eq', 'ne', 'cn', 'bw', 'ew']};
var search_decimal_options = {sopt: ['eq', 'lt', 'gt']};
var search_date_options = {sopt: ['eq', 'lt', 'gt'], dataInit: getDate};
var required_form_options = {elmsuffix: "(*必填)"}; //elmprefix: "SSAA", elmsuffix: ''
var formula_hint = {elmsuffix: "(F)"};

var number_search_rule = {number: true, required: true};
var date_search_rule = {date: true, required: true};

var customErrorTextFormat = function (response) {
    return '<span class="ui-icon ui-icon-alert" ' +
            'style="float:left; margin-right:.3em;"></span>' +
            response.responseText;
//    alert(response.responseText);
};

var greyout = function ($form) {
    $form.find(".FormElement[readonly]")
            .parent(".DataTD")
            .prop("disabled", true)
            .addClass("ui-state-disabled")
            .prev(".CaptionTD")
            .prop("disabled", true)
            .addClass("ui-state-disabled");
};

function getDate(el) {
    $(el).datepicker({dateFormat: "yy-mm-dd"});
}

var errorTextFormatF = function (data) {

    // The JSON object that comes from the server contains an array of strings:
    // odd elements are field names, and even elements are error messages.
    // If your JSON has a different format, the code should be adjusted accordingly.

    var validationErrors = $.isArray(data) ? data : data.responseJSON;

    if (validationErrors != null) {
        for (var i = 0; i < validationErrors.length; i++) {
            var selector = ".DataTD #" + validationErrors[i].field;
            $(selector).after("<span title='" + validationErrors[i].code + "' class='glyphicon glyphicon-alert' style='color:red'></span>");
//            $(selector).after(validationErrors[i].code);
        }
    }

    return "There are some errors in the entered data. Hover over the error icons for details.";
};

var clearCheckErrorIcon = function () {
    $(".glyphicon-alert").remove();
};