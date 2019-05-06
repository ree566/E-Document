/*
 * Open dialog
 * Init error code & action code options
 * Init error code checkbox
 * Action code popout by error code click
 * Save & Update countermeasure
 */
var saveUrl;
var queryUrl;
var actionCodeQueryUrl;
var errorCodeQueryUrl;

var editId;
var table;

var originSop;
var originErrorCon;
var originResponseUser;
var checkedErrorCodes;
var checkedActionCodes;
var checkBoxs;
var errorCodes;
var actionCodes;
var user;

var countermeasureType;

function initUrl(urls) {
    queryUrl = urls["queryUrl"];
    saveUrl = urls["saveUrl"];
    actionCodeQueryUrl = urls["actionCodeQueryUrl"];
    errorCodeQueryUrl = urls["errorCodeQueryUrl"];
}

function initTable(dt) {
    table = dt;
}

function initUserInfo(authUser) {
    user = authUser;
}

function initCountermeasureDialog(urls, dt, cType, userInfo) {
    initUrl(urls);
    initTable(dt);
    initUserInfo(userInfo);
    countermeasureType = cType;
    if (errorCodeQueryUrl && errorCodeQueryUrl != "" && actionCodeQueryUrl && actionCodeQueryUrl != "") {
        initActionCodeAndErrorCodeOptions();
        initErrorCodeCheckbox();
    }
    registOtherEvents();
}

function initActionCodeAndErrorCodeOptions() {
    checkBoxs = $("#actionCode > div").detach();
    errorCodes = getErrorCode();
    actionCodes = getActionCode();
}

function initErrorCodeCheckbox() {
    var errorCodeArea = $("#errorCode .checkbox");
    for (var i = 0; i < errorCodes.length; i++) {
        var errorCode = errorCodes[i];
        errorCodeArea.append(
                "<label class='checkbox-inline'>" +
                "<input type='checkbox' name='errorCode' value=" + errorCode.id + ">" + errorCode.name + "" +
                "</label>");
    }
}

function setupActionCode() {

    $("#actionCode").html("");
    var data = actionCodes;
    var array = $.map($('input[name="errorCode"]:checked'), function (c) {
        return c.value;
    });

    for (var i = 0, j = array.length; i < j; i++) {
        var id = array[i];
        for (var k = 0, l = data.length; k < l; k++) {
            if (data[k].errorCode.id == id) {
                var checkboxObj = checkBoxs.clone();
                checkboxObj.addClass("ec" + id);
                checkboxObj.find(":checkbox").attr("value", data[k].id).after(data[k].name);
                $("#actionCode").append(checkboxObj);
            }
        }
    }
}

function getActionCode() {
    var result;
    $.ajax({
        url: actionCodeQueryUrl,
        type: "GET",
        dataType: "json",
        async: false,
        success: function (response) {
            result = response;
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log(xhr.responseText);
        }
    });
    return result;
}

function getErrorCode() {
    var result;
    $.ajax({
        url: errorCodeQueryUrl,
        type: "GET",
        dataType: "json",
        async: false,
        success: function (response) {
            result = response;
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log(xhr.responseText);
        }
    });
    return result;
}

function setErrorCodeCheckBox(errorCodes) {
    for (var i = 0; i < errorCodes.length; i++) {
        $('input[name="errorCode"][value=' + errorCodes[i] + ']').prop("checked", true);
    }

}

function setActionCodeCheckBox(actionCodes) {
    for (var i = 0; i < actionCodes.length; i++) {
        $('input[name="actionCode"][value=' + actionCodes[i] + ']').prop("checked", true);
    }
}

function counterMeasureModeUndo() {
    $("#saveCountermeasure, #undoContent, #sopHint").hide();
    $("#editCountermeasure").show();
}

function counterMeasureModeEdit() {
    $("#saveCountermeasure, #undoContent, #sopHint").show();
    $("#editCountermeasure").hide();
}

function resetCountermeasureDialog() {
    $(".modal-body #errorCon, #sop, #responseUser").html("N/A");
    $("input[name='errorCode']").prop("checked", false);
    $('input[name="actionCode"]').prop("checked", false);
    $("#responseUser").html("");
    $(".modal-body :checkbox").attr("disabled", true);
    checkedErrorCodes = [];
    checkedActionCodes = [];
    setupActionCode();
    showDialogMsg("");
}


function getCountermeasure(bab_id) {
    resetCountermeasureDialog();

    $.ajax({
        url: queryUrl,
        data: {
            bab_id: bab_id,
            typeName: countermeasureType
        },
        type: "GET",
        dataType: 'json',
        success: function (msg) {
            var jsonData = msg;
            $(".modal-body #errorCon").html(jsonData.solution.replace(/(?:\r\n|\r|\n)/g, '<br />'));

            var errorCodes = msg.errorCodes;
            var actionCodes = msg.actionCodes;

            for (var i = 0; i < errorCodes.length; i++) {
                checkedErrorCodes.push(errorCodes[i].id);
            }

            for (var i = 0; i < actionCodes.length; i++) {
                checkedActionCodes.push(actionCodes[i].id);
            }

            setErrorCodeCheckBox(checkedErrorCodes);
            setupActionCode();
            setActionCodeCheckBox(checkedActionCodes);

            $(".modal-body :checkbox").attr("disabled", true);

            var countermeasureSopRecords = msg.countermeasureSopRecords;

            if (countermeasureSopRecords.length != 0) {
                var sop = countermeasureSopRecords[0].sop;
                var sopTran = sop.replace(/(?:\r\n|\r|\n)/g, '<br />');
                $(".modal-body #sop").html(sopTran);
            }

            var lastEditor = jsonData.lastEditor;
            $(".modal-body #responseUser").append("<span class='label label-default'>#" + (lastEditor == null ? 'N/A' : lastEditor.usernameCh) + "</span> ");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            showDialogMsg(xhr.responseText);
        }
    });
}

function saveCountermeasure(data) {
    $.ajax({
        url: saveUrl,
        data: data,
        type: "POST",
        dataType: 'json',
        success: function (msg) {
            if (msg == true) {
                counterMeasureModeUndo();
                getCountermeasure(data.bab_id);
                $("#searchAvailableBab").trigger("click");
                showDialogMsg("success");
                reload();
            } else {
                showDialogMsg(msg.data);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            showDialogMsg(xhr.responseText);
        }
    });
}

function showDialogMsg(msg) {
    $("#dialog-msg").html(msg);
}

function reload() {
    table.ajax.reload();
}

//隔離特殊字元
function unEntity(str) {
    return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}

function registOtherEvents() {

    $("#actionCode > div").detach();

    $("body").on('click', '.cm-detail', function () {
        var selectData = table.row($(this).parents('tr')).data();
        editId = selectData.id;
        var modal = $($(this).attr("data-target"));
        modal.find(".modal-title").html(
                "號碼: " + editId +
                " / 工單: " + selectData.po +
                " / 機種: " + selectData.modelName +
                " / 線別: " + selectData.lineName +
                " / 時間: " + formatDate(selectData.btime)
                );

        getCountermeasure(selectData.id);

    });

    $("body").on('click', '.babSetting-detail', function () {
        var selectData = table.row($(this).parents('tr')).data();
        editId = selectData.id;
        var modal = $($(this).attr("data-target"));
        modal.find(".modal-title").html(
                "號碼: " + editId +
                " / 工單: " + selectData.po +
                " / 機種: " + selectData.modelName +
                " / 線別: " + selectData.lineName +
                " / 時間: " + formatDate(selectData.btime)
                );
        getBabSettingHistory(selectData.id);
    });

    $('.modal').on('shown.bs.modal', function () {
        $(this).find('.modal-dialog').css({width: '90%',
            height: 'auto',
            'max-height': '100%'});
    });

    $("#saveCountermeasure, #undoContent").hide();

    $("#editCountermeasure").click(function () {
        counterMeasureModeEdit();
        $(":checkbox").removeAttr("disabled");
        originSop = $("#sop").html();
        originErrorCon = $("#errorCon").html().replace(/<br *\/?>/gi, '\n');
        originResponseUser = $("#responseUser").html();
        $("#sop").html("<textarea id='sopText' maxlength='200' style='height:100px'>" + (originSop == "N/A" ? "" : originSop) + "</textarea>");
        $("#errorCon").html("<textarea id='errorConText' maxlength='500'>" + (originErrorCon == "N/A" ? "" : originErrorCon) + "</textarea>");
        $("#responseUser").html("<input type='text' id='responseUserText' maxlength='30' value='" + user + "' readonly disabled>");
    });

    $("#undoContent").click(function () {
        if (!confirm("確定捨棄修改?")) {
            return false;
        }
        counterMeasureModeUndo();
        $(".modal-body :checkbox").attr("disabled", true);
        $("#sop").html(originSop);
        $("#errorCon").html(originErrorCon.replace(/(?:\r\n|\r|\n)/g, '<br />'));
        $("#responseUser").html(originResponseUser);
    });

    $("#saveCountermeasure").click(function () {
        if (confirm("確定修改內容?")) {
            var sop = $("#sopText").val();
            var editor = unEntity($("#responseUserText").val()),
                    solution = unEntity($("#errorConText").val());
            var errorCodes = $.map($('input[name="errorCode"]:checked'), function (c) {
                return c.value;
            });
            var actionCodes = $.map($('input[name="actionCode"]:checked'), function (c) {
                return c.value;
            });
            if (checkVal(editor) == false) {
                showDialogMsg("找不到使用者，請重新確認您的工號是否存在");
                return false;
            } else if (errorCodeQueryUrl && errorCodeQueryUrl != "" && errorCodes.length == 0) {
                showDialogMsg("請選擇至少一項ErrorCode");
                return false;
            } else if (actionCodeQueryUrl && actionCodeQueryUrl != "" && actionCodes.length == 0) {
                showDialogMsg("請選擇至少一項ActionCode");
                return false;
            } else if (checkVal(sop) == false) {
                showDialogMsg("請填入SOP資訊");
                return false;
            } else {
                showDialogMsg("");
            }
            saveCountermeasure({
                bab_id: editId,
                typeName: countermeasureType,
                solution: solution,
                errorCodes: errorCodes,
                actionCodes: actionCodes,
                sop: sop,
                editor: editor
            });
        }

    });

    $('#myModal').on('hidden.bs.modal', function () {
        counterMeasureModeUndo();
    });

    $("#errorCode :checkbox").change(function () {
        if (!$(this).is(":checked")) {
            $("#actionCode").find(".ec" + $(this).val()).remove();
        } else {
            checkedActionCodes = $.map($('input[name="actionCode"]:checked'), function (c) {
                return c.value;
            });
            setupActionCode();
            setActionCodeCheckBox(checkedActionCodes);
            $("#actionCode" + " .ec" + $(this).val()).first().find(":checkbox").prop("checked", true);
        }
    });

    $(document).on("change", "#actionCode :checkbox", function () {
        if ($(this).is(":checked")) {
            checkedActionCodes.push($(this).val());
        } else {
            var removeVal = $(this).val();
            checkedActionCodes = $.grep(checkedActionCodes, function (value) {
                return value != removeVal;
            });
        }
    });

}