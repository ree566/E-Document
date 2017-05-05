var search_string_options = {sopt: ['eq', 'ne', 'cn', 'bw', 'ew']};
var search_decimal_options = {sopt: ['eq', 'lt', 'gt']};
var search_date_options = {sopt: ['eq', 'lt', 'gt'], dataInit: getDate};
var required_form_options = {elmsuffix:"(*必填)"}; //elmprefix: "SSAA", elmsuffix: ''
var formula_hint = {elmsuffix:"(F)"};

var number_search_rule = {number: true, required: true};
var date_search_rule = {date: true, required: true};

var customErrorTextFormat = function (response) {
    return '<span class="ui-icon ui-icon-alert" ' +
            'style="float:left; margin-right:.3em;"></span>' +
            response.responseText;
};

var greyout = function ($form) {
    $form.find(".FormElement[readonly]")
            .prop("disabled", true)
            .addClass("ui-state-disabled")
            .closest(".DataTD")
            .prev(".CaptionTD")
            .prop("disabled", true)
            .addClass("ui-state-disabled");
};

function getDate(el) {
    $(el).datepicker({dateFormat: "yy-mm-dd"});
}