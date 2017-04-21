var search_string_options = {sopt: ['eq', 'ne', 'cn', 'bw', 'ew']};
var search_decimal_options = {sopt: ['eq', 'lt', 'gt']};
var search_date_options = {sopt: ['eq', 'ne']};
var required_form_options = {elmprefix: "SSAA", elmsuffix: '(*必填)'};

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