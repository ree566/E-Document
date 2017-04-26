<%-- 
    Document   : page2
    Created on : 2017/4/19, 上午 09:35:51
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="root" value="${pageContext.request.contextPath}/"/>
<script>
    $(function () {
        var grid = $("#list");
        var tableName = "PreAssy";

        grid.jqGrid({
            url: '${root}getSelectOption.do/' + tableName,
            datatype: 'json',
            mtype: 'GET',
            colModel: [
                {label: 'id', name: "id", width: 60, key: true, editable: true, editoptions: {readonly: 'readonly', disabled: true, defaultValue: "0"}},
                {label: 'name', name: "name", width: 60, editable: true}
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
            editurl: '${root}updateSelectOption.do/' + tableName,
            sortname: 'id', sortorder: 'asc',
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Ajax Error occurred\n"
                        + "\nstatus is: " + xhr.status
                        + "\nthrownError is: " + thrownError
                        + "\najaxOptions is: " + ajaxOptions
                        );
            }
        })
                .jqGrid('navGrid', '#pager',
                        {edit: true, add: true, del: true, search: true},
                        {
                            dataheight: 350,
                            width: 450,
                            closeAfterEdit: false,
                            reloadAfterSubmit: true,
                            errorTextFormat: customErrorTextFormat,
                            beforeShowForm: greyout,
                            zIndex: 9999,
                            recreateForm: true
                        },
                        {
                            dataheight: 350,
                            width: 450,
                            closeAfterAdd: false,
                            reloadAfterSubmit: true,
                            errorTextFormat: customErrorTextFormat,
                            beforeShowForm: greyout,
                            zIndex: 9999,
                            recreateForm: true
                        },
                        {
                            zIndex: 9999,
                            reloadAfterSubmit: true
                        },
                        {
                            sopt: ['eq', 'ne', 'lt', 'gt', 'cn', 'bw', 'ew'],
                            closeAfterSearch: false,
                            zIndex: 9999,
                            reloadAfterSubmit: true
                        }
                );

        function getSelectOption(columnName, data) {
            var result = {};
            $.ajax({
                type: "GET",
                url: "${root}getSelectOption.do/" + columnName,
                data: data,
                async: false,
                success: function (response) {
                    var arr = response.rows;

                    for (var i = 0; i < arr.length; i++) {
                        var innerObj = arr[i];
                        result[innerObj.id] = innerObj.name;
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log(xhr.responseText);
                }
            });
            return result;
        }

    });
</script>

<div id="flow-content">
    <table id="list"></table> 
    <div id="pager"></div>
</div>
