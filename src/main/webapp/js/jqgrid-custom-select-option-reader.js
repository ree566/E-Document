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

        selectOptions[columnName + '_options'] = col_options;
        selectOptions[columnName] = optionsStringify(col_options);
        selectOptions[columnName + '_func'] = getFunc(columnName);
    }
}

function getSelectOption(columnName, isNullable, data) {
    var result = new Map();
    var url = rootUrl + 'SelectOption/' + (data == null ? columnName : columnName + '/' + data);
    $.ajax({
        type: 'GET',
        url: url,
        async: false,
        success: function (response) {
            var arr = response;
            if (isNullable) {
                result.set(0, 'empty');
            }
            for (var i = 0; i < arr.length; i++) {
                var obj = arr[i];
                if (columnName == 'user') {
                    result.set(obj.id, obj.username);
                } else {
                    result.set(obj.id, obj.name);
                }
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log(xhr.responseText);
        }
    });
    return result;
}

function optionsStringify(map) {
    var str = '';
    map.forEach(function (value, key, map) {
        str += (key + ':' + value + ';');
    });
    return str.replace(/.$/,"");
}

//http://stackoverflow.com/questions/19696015/javascript-creating-functions-in-a-for-loop
//use closure 
function getFunc(columnName) {
    return function (cellvalue, options, rowObject) {
        var map = selectOptions[columnName + '_options'];
        var obj = map.get(cellvalue);
        return obj == null ? '' : obj;
    };
}