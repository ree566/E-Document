var rootUrl;
var selectOptions = {};
var selectableColumns;

function setSelectOptions(info) {
    rootUrl = info.rootUrl;
    selectableColumns = info.columnInfo;
    syncSelectOptionInfo();
}

function syncSelectOptionInfo() {
    for (var i = 0; i < selectableColumns.length; i++) {
        var column = selectableColumns[i];
        var columnName = column.name;
        var col_options = getSelectOption(columnName, column.isNullable, column.dataToServer);

        if (column.nameprefix != null) {
            columnName = column.nameprefix + columnName;
        }

        selectOptions[columnName] = col_options;
        selectOptions[columnName + "_func"] = getFunc(columnName);
    }
}

function getSelectOption(columnName, isNullable, data) {
    var result = {};
    var url = rootUrl + "SelectOption/" + (data == null ?  columnName : columnName + "/" + data);
    $.ajax({
        type: "GET",
        url: url,
        async: false,
        success: function (response) {
            var arr = response;
            if (isNullable) {
                result[0] = "empty";
            }
            for (var i = 0; i < arr.length; i++) {
                var obj = arr[i];
                if (columnName == 'user') {
                    result[obj.id] = obj.username;
                } else {
                    result[obj.id] = obj.name;
                }
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log(xhr.responseText);
        }
    });
    return result;
}

//http://stackoverflow.com/questions/19696015/javascript-creating-functions-in-a-for-loop
//use closure 
function getFunc(columnName) {
    return function (cellvalue, options, rowObject) {
        var arr = selectOptions[columnName];
        var obj = arr[cellvalue];
        return obj == null ? "" : obj;
    };
}