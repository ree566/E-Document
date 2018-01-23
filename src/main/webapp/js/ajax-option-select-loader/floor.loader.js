var floorLoaderUrl = "";

function initFloorOptions(target) {
    $.ajax({
        url: floorLoaderUrl,
        type: "GET",
        dataType: "json",
        async: false,
        success: function (response) {
            var arr = response;
            for (var i = 0, j = arr.length; i < j; i++) {
                var floor = arr[i];
                target.append("<option value=" + floor.id + ">" + floor.name + "F</option>");
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log(xhr.responseText);
        }
    });
}
