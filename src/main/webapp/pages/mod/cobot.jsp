<%-- 
    Document   : machineWorktime
    Created on : 2022/6/27, 下午 05:00:19
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/js/jqgrid-custom-setting.js" />"></script>
<script>
    $(function () {
        var grid = $("#list");
        var tableName = "Cobot";

        grid.jqGrid({
            url: '<c:url value="/Cobot/read" />',
            datatype: 'json',
            mtype: 'GET',
            autoencode: true,
            colModel: [
                {label: 'id', name: "id", width: 60, key: true, editable: true, editoptions: {readonly: 'readonly', disabled: true, defaultValue: "0"}},
                {label: 'name', name: "name", width: 60, editable: true},
                {label: '機器工時(秒)', name: "worktimeSeconds", width: 60, editable: true},
                {label: '機氣工時(分)', name: "worktimeMinutes", width: 60, editable: true}
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
                    url: '<c:url value="/Cobot/update" />',
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
                    url: '<c:url value="/Cobot/create" />',
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
                    url: '<c:url value="/Cobot/delete" />',
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

<div id="machineWorktime-content">
    <table id="list"></table> 
    <div id="pager"></div>
</div>

