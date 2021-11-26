<%-- 
    Document   : conversion
    Created on : 2017/6/30, ä¸å 01:17:44
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<sec:authorize access="hasRole('ADMIN')"  var="isAdmin" />
<sec:authorize access="hasRole('USER')"  var="isUser" />
<sec:authorize access="hasRole('OPER')"  var="isOper" />
<style>
    .headerRow{
        font-weight: bold;
    }
</style>
<script src="<c:url value="/js/jqgrid-custom-setting.js" />"></script>
<script src="<c:url value="/webjars/github-com-johnculviner-jquery-fileDownload/1.4.6/src/Scripts/jquery.fileDownload.js" />"></script>
<script>
    $(function () {
        var grid = $("#list");
        var tableName = "重工途程排列組合";
        var editable = ${isAdmin || isOper};

        var beforeAdd = function ($form) {
            var lastCodeNum = getLastCodeNum();
            $("#code").val(lastCodeNum);
            greyout($form);
        };

        grid.jqGrid({
            url: '<c:url value="/FlowPermutationsController/read" />',
            datatype: 'json',
            mtype: 'GET',
            autoencode: true,
            colModel: [
                {label: 'id', name: "id", editable: true, hidden: true, defaultValue: 0},
                {label: 'BAB', name: "babFlow", editable: true},
                {label: 'TEST', name: "testFlow", editable: true},
                {label: 'Packing', name: "packingFlow", editable: true},
                {label: 'Code', name: "code", editable: true, editoptions: {readonly: "readonly"}},
                {
                    label: "Edit Actions",
                    name: "actions",
                    width: 100,
                    formatter: "actions",
                    hidden: !editable,
                    formatoptions: {
                        keys: true,
                        editOptions: {},
                        addOptions: {},
                        delOptions: {
                            onclickSubmit: function (options, rowid) {
                                var grid_id = $.jgrid.jqID($("#list")[0].id),
                                        grid_p = $("#list")[0].p,
                                        newPage = grid_p.page;

                                // delete the row
                                $.ajax({
                                    url: '<c:url value="/FlowPermutationsController/delete" />',
                                    type: 'POST',
                                    data: {id: rowid, oper: 'del'},
                                    dataType: "text",
                                    success: function (data, status, xr) {
                                        grid.trigger("reloadGrid");
                                    },
                                    error: function (e) {
                                        //called when there is an error
                                        console.log(e.message);
                                    }
                                });

                                $.jgrid.hideModal("#delmod" + grid_id,
                                        {gb: "#gbox_" + grid_id,
                                            jqm: options.jqModal, onClose: options.onClose});

                                return true;
                            },
                            processing: true
                        }
                    }
                }
            ],
            rowNum: -1,
            rowList: [20, 50, 100, -1],
            pager: '#pager',
            viewrecords: true,
            autowidth: true,
            shrinkToFit: true,
            hidegrid: true,
            stringResult: true,
            gridview: true,
            loadui: "block",
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false
            },
            editurl: '<c:url value="/FlowPermutationsController/update" />',
            loadonce: false,
            navOptions: {reloadGridOptions: {fromServer: true}},
            caption: tableName,
            sortname: 'id', sortorder: 'asc',
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Ajax Error occurred\n"
                        + "\nstatus is: " + xhr.status
                        + "\nthrownError is: " + thrownError
                        + "\najaxOptions is: " + ajaxOptions
                        );
            },
            grouping: true,
            groupingView: {
                groupField: ["babFlow", "testFlow"],
                groupColumnShow: [true, true],
                groupText: [
                    "<b>{0}</b>",
                    "<b>{0}</b>"
                ],
                groupOrder: ["asc", "asc"],
                groupSummary: [true, false],
                groupSummaryPos: ['header', 'header'],
                groupCollapse: true
            }
        });

        if (editable) {
            grid.jqGrid('navGrid', '#pager',
                    {edit: true, add: true, del: true, search: true},
                    {
                        url: '<c:url value="/FlowPermutationsController/update" />',
                        dataheight: 350,
                        width: 450,
                        closeAfterEdit: closed_after_edit,
                        reloadAfterSubmit: true,
                        errorTextFormat: customErrorTextFormat,
                        beforeShowForm: greyout,
                        zIndex: 9999,
                        recreateForm: true
                    },
                    {
                        url: '<c:url value="/FlowPermutationsController/create" />',
                        dataheight: 350,
                        width: 450,
                        closeAfterAdd: closed_after_add,
                        reloadAfterSubmit: true,
                        errorTextFormat: customErrorTextFormat,
                        beforeShowForm: beforeAdd,
                        zIndex: 9999,
                        recreateForm: true
                    },
                    {
                        url: '<c:url value="/FlowPermutationsController/delete" />',
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
        }

//        grid.navButtonAdd('#pager', {
//            caption: "Export to Excel",
//            buttonicon: "ui-icon-disk",
//            id: "excelDownload1",
//            onClickButton: function () {
//                var button = $("#excelDownload1");
//                excelDownload(button, "<c:url value="/FlowPermutationsController/excel" />");
//                return false;
//            },
//            position: "last"
//        });

        $("body").on("keyup", "input", function (e) {
            var textbox = $(this);
            textbox.val(textbox.val().toUpperCase());
        });

        function excelDownload(buttonId, url) {
            var button = $(buttonId);
            button.addClass('ui-state-disabled');
            $.fileDownload(url, {
                preparingMessageHtml: "We are preparing your report, please wait...",
                failMessageHtml: "There was a problem generating your report, please try again.",
                data: grid.getGridParam("postData"),
                successCallback: function (url) {
                    button.removeClass('ui-state-disabled');
                },
                failCallback: function (html, url) {
                    button.removeClass('ui-state-disabled');
                }
            });
        }

        function headerRow(rowId, cellValue, rawObject, cm, rdata) {
            return " class='ui-state-default headerRow'";
        }
        
        function getLastCodeNum() {
            var result;
            $.ajax({
                type: "GET",
                url: "<c:url value="/FlowPermutationsController/findLastCode" />",
                data: {
                },
                dataType: "json",
                async: false,
                success: function (response) {
                    result = response;
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    closeEditDialogWhenError("查詢Code時發生錯誤，請稍後再試");
                    console.log(xhr.responseText);
                }
            });
            return result;
        }
        
        function closeEditDialogWhenError(error_message) {
            alert(error_message);
            $("#TblGrid_list_2").find("#cData").trigger("click");
        }

    });
</script>

<div id="flow-content">
    <h5>編輯完請按 Enter(送出) 或 Esc(取消) 編輯, 若徒程為空請填寫"(空白)"</h5>
    <div>
        <table id="list"></table> 
        <div id="pager"></div>
    </div>
</div>
