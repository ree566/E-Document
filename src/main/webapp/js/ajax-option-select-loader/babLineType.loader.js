var lineTypeLoaderUrl = "";
var lineTypeObject;

function setLineObject() {
    $.ajax({
        type: "GET",
        url: lineTypeLoaderUrl,
        dataType: "json",
        async: false,
        success: function (response) {
            lineTypeObject = response;
        },
        error: function (xhr, ajaxOptions, thrownError) {
            showMsg(xhr.responseText);
        }
    });
}

function initOptions(target) {
    for (var i = 0, j = lineTypeObject.length; i < j; i ++) {
        var lineType = lineTypeObject[i];
        target.append("<option value=" + lineType.id + ">" + lineType.name + "</option>");
    }
}