<%-- 
    Document   : page2
    Created on : 2017/4/19, 上午 09:35:51
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    .danger{
        color: red;
    }
</style>
<script src="<c:url value="/js/jqgrid-custom-select-option-reader.js" />"></script>
<script src="<c:url value="/js/websocket/sockjs.min.js" />"></script>
<script src="<c:url value="/js/websocket/stomp.min.js" />"></script>
<script src="<c:url value="/js/jqgrid-custom-setting.js" />"></script>
<script>
    $(function () {
        var scrollPosition = 0;
        var grid = $("#list");
        var tableName = "User";
        setSelectOptions({
            rootUrl: "<c:url value="/" />",
            columnInfo: [
                {name: "user-floor", isNullable: false},
                {name: "unit", isNullable: false},
                {name: "userProfiles", isNullable: false},
                {name: "userUserNotifications", isNullable: true}
            ]
        });
        var sendMessageToSocket = function (form) {
//            var rowId = grid.jqGrid('getGridParam', 'selrow');
//            sendMessage(rowId, "LOCK");
            greyout(form);
            setUserProfiles(form);
            setUserNotifications(form);
        };

        var unLockRow = function () {
//            var rowId = grid.jqGrid('getGridParam', 'selrow');
//            sendMessage(rowId, "UNLOCK");
        };

        var setUserProfiles = function (form) {
            var selectRowId = grid.jqGrid('getGridParam', 'selrow');
            $.ajax({
                type: "GET",
                url: "<c:url value="/User/read/userProfiles" />",
                data: {
                    userId: selectRowId
                },
                dataType: "json",
                success: function (response) {
                    var arr = response;
                    var idArr = [];
                    for (var i = 0; i < arr.length; i++) {
                        idArr.push(arr[i].id);
                    }
                    $("#userProfiles").val(idArr);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log(xhr.responseText);
                }
            });
        };

        var setUserNotifications = function (form) {
            var selectRowId = grid.jqGrid('getGridParam', 'selrow');
            $.ajax({
                type: "GET",
                url: "<c:url value="/User/read/userNotifications" />",
                data: {
                    userId: selectRowId
                },
                dataType: "json",
                success: function (response) {
                    var arr = response;
                    var idArr = [];
                    for (var i = 0; i < arr.length; i++) {
                        idArr.push(arr[i].id);
                    }
                    $("#userNotifications").val(idArr);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log(xhr.responseText);
                }
            });
        };

        grid.jqGrid({
            url: '<c:url value="/User/read" />',
            datatype: 'json',
            mtype: 'GET',
            autoencode: true,
            colModel: [
                {label: 'id', name: "id", width: 60, key: true, editable: true, editoptions: {readonly: 'readonly', disabled: true, defaultValue: "0"}, search: false},
                {label: 'jobnumber', name: "jobnumber", width: 60, editable: true, editrules: {required: true}, searchoptions: {sopt: ['eq']}},
                {label: 'password', name: "password", width: 60, editable: true, editrules: {required: true}, edittype: "password", search: false},
                {label: 'username', name: "username", width: 60, editable: true, editrules: {required: true}, searchoptions: {sopt: ['eq']}},
                {label: 'permission', name: "permission", width: 60, editable: false, hidden: true, search: false},
                {label: 'floor', name: "floor.id", width: 60, editable: true, edittype: "select", editoptions: {value: selectOptions["user-floor"]}, formatter: selectOptions["user-floor_func"], search: false},
                {label: 'unit', name: "unit.id", width: 60, editable: true, edittype: "select", editoptions: {value: selectOptions["unit"]}, formatter: selectOptions["unit_func"], search: false},
                {label: 'email', name: "email", width: 60, editable: true, search: false},
                {label: 'state', name: "state", width: 60, editable: true, edittype: "select", editoptions: {value: "Active:Active;Inactive:Inactive;Deleted:Deleted;Locked:Locked"}, search: false},
                {label: '使用者角色', name: "userProfiles", width: 60, editable: true, hidden: true, editrules: {edithidden: true, required: true}, edittype: "select", editoptions: {multiple: true, value: selectOptions["userProfiles"], defaultValue: "3"}, search: false},
                {label: '系統通知', name: "userNotifications", width: 60, editable: true, hidden: true, editrules: {edithidden: true, required: false}, edittype: "select", editoptions: {multiple: true, value: selectOptions["userUserNotifications"]}, search: false}
            ],
            rowNum: 20,
            rowList: [20, 50, 100],
            pager: '#pager',
            viewrecords: true,
            autowidth: true,
            shrinkToFit: true,
            hidegrid: true,
            stringResult: true,
            gridview: true,
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false
            },
            afterSubmit: function () {
                $(this).jqGrid("setGridParam", {datatype: 'json'});
                return [true];
            },
            navOptions: {reloadGridOptions: {fromServer: true}},
            caption: tableName + " modify",
            height: 450,
            sortname: 'id', sortorder: 'asc',
            onSelectRow: function () {
                scrollPosition = grid.closest(".ui-jqgrid-bdiv").scrollTop();
            },
            gridComplete: function () {
                grid.closest(".ui-jqgrid-bdiv").scrollTop(scrollPosition);
//                grid.find("#36").hide();
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Ajax Error occurred\n"
                        + "\nstatus is: " + xhr.status
                        + "\nthrownError is: " + thrownError
                        + "\najaxOptions is: " + ajaxOptions
                        );
            }
        });
        grid.jqGrid('navGrid', '#pager',
                {edit: true, add: true, del: true, search: true},
                {
                    url: '<c:url value="/User/update" />',
                    dataheight: 350,
                    width: 450,
                    closeAfterEdit: closed_after_edit,
                    reloadAfterSubmit: true,
                    errorTextFormat: customErrorTextFormat,
                    beforeShowForm: sendMessageToSocket,
                    onClose: unLockRow,
                    zIndex: 9999,
                    recreateForm: true,
                    viewPagerButtons: false
                },
                {
                    url: '<c:url value="/User/create" />',
                    dataheight: 350,
                    width: 450,
                    closeAfterAdd: closed_after_add,
                    reloadAfterSubmit: true,
                    errorTextFormat: customErrorTextFormat,
                    beforeShowForm: greyout,
                    zIndex: 9999,
                    recreateForm: true
                },
                {
                    url: '<c:url value="/User/delete" />',
                    zIndex: 9999,
                    reloadAfterSubmit: true
                },
                {
                    sopt: ['eq', 'ne', 'lt', 'gt', 'cn', 'bw', 'ew'],
                    closeAfterSearch: closed_after_search,
                    zIndex: 9999,
                    reloadAfterSubmit: true
                }
        );
    });
</script>

<div id="flow-content">
    <table id="list"></table> 
    <div id="pager"></div>
</div>

