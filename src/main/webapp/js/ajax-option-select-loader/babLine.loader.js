var lineLoaderUrl = "";
var lineObject = {};

function setLineObject() {
    $.ajax({
        type: "GET",
        url: lineLoaderUrl,
        data: {
            sitefloor: $("#userSitefloorSelect").val()
        },
        dataType: "json",
        async: false,
        success: function (response) {
            var lines = response;
            for (var i = 0; i < lines.length; i++) {
                var line = lines[i];
                var lineName = line.name;
                var lineType = line.lineType.name;
                var arr = lineObject[lineType];
                if (arr == null) {
                    arr = [];
                    lineObject[lineType] = arr;
                }
                arr.push(lineName);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            showMsg(xhr.responseText);
        }
    });
}

function initLineOptions(target) {
    for (var property in lineObject) {
        if (lineObject.hasOwnProperty(property)) {
            target.append("<option value=" + property + ">" + property + "</option>");
        }
    }
}