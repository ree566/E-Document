<%-- 
    Document   : page2
    Created on : 2017/4/19, 上午 09:35:51
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/js/jqgrid-custom-select-option-reader.js" />"></script>
<script>
    $(function () {
        var scrollPosition = 0;
        
        var grid = $("#list");
        var tableName = "User";

        setSelectOptions({
            rootUrl: "<c:url value="/" />",
            columnInfo: [
                {name: "floor", isNullable: false},
                {name: "unit", isNullable: false}
            ]
        });

        grid.jqGrid({
            url: '<c:url value="/User/find" />',
            datatype: 'json',
            mtype: 'GET',
            autoencode: true,
            colModel: [
                {label: 'id', name: "id", width: 60, key: true, editable: true, editoptions: {readonly: 'readonly', disabled: true, defaultValue: "0"}},
                {label: 'jobnumber', name: "jobnumber", width: 60, editable: true, editoptions: {readonly: 'readonly', disabled: true}},
                {label: 'password', name: "password", width: 60, editable: true, edittype: "password"},
                {label: 'username', name: "username", width: 60, editable: true},
                {label: 'permission', name: "permission", width: 60, editable: true},
                {label: 'floor', name: "floor.id", width: 60, editable: true, edittype: "select", editoptions: {value: selectOptions["floor"]}, formatter: selectOptions["floor_func"]},
                {label: 'unit', name: "unit.id", width: 60, editable: true, edittype: "select", editoptions: {value: selectOptions["unit"]}, formatter: selectOptions["unit_func"]},
                {label: 'email', name: "email", width: 60, editable: true},
                {label: 'state', name: "state", width: 60, editable: true, edittype: "select", editoptions: {value: "Active:Active;Inactive:Inactive;Deleted:Deleted;Locked:Locked"}}
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
            editurl: '<c:url value="/User/mod" />',
            sortname: 'id', sortorder: 'asc',
            onSelectRow: function () {
                scrollPosition = grid.closest(".ui-jqgrid-bdiv").scrollTop();
            },
            gridComplete: function () {
                grid.closest(".ui-jqgrid-bdiv").scrollTop(scrollPosition);
            },
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


    });
</script>

<div id="flow-content">
    <table id="list"></table> 
    <div id="pager"></div>
</div>

