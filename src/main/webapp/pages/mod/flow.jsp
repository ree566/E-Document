<%-- 
    Document   : page2
    Created on : 2017/4/19, 上午 09:35:51
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="<c:url value="/css/jquery-ui-multiselect.css" />" rel="stylesheet">
<script src="<c:url value="/js/jquery-ui-multiselect.min.js" />"></script>
<script>
    var scrollPosition = 0;

    $(function () {
        var grid = $("#list");
        var tableName = "Flow";

        setSelectOptions({
            rootUrl: "<c:url value="/" />",
            columnInfo: [
                {name: "flow", isNullable: false},
                {name: "flowGroup", isNullable: false}
            ]
        });

        grid.jqGrid({
            url: '<c:url value="/Flow/find" />',
            datatype: 'json',
            mtype: 'GET',
            autoencode: true,
//            guiStyle: "bootstrap",
            colModel: [
                {label: 'id', name: "id", width: 60, key: true, editable: true, editoptions: {readonly: 'readonly', disabled: true, defaultValue: "0"}},
                {label: 'name', name: "name", width: 60, editable: true, editrules: {required: true}, formoptions: {elmsuffix: "(*必填)"}},
                {label: 'flow_group', name: "flowGroup.id", editable: true, formatter: selectOptions["flowGroup_func"], edittype: 'select', editoptions: {value: selectOptions["flowGroup"]}}
            ],
            rowNum: 20,
            rowList: [20, 50, 100, 1000],
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
            editurl: '<c:url value="/Flow/mod" />',
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
            },
            subGrid: true,
            subGridOptions: {"plusicon": "ui-icon-triangle-1-e",
                "minusicon": "ui-icon-triangle-1-s",
                "openicon": "ui-icon-arrowreturn-1-e",
                "reloadOnExpand": false,
                "selectOnExpand": true},
            subGridRowExpanded: function (subgrid_id, row_id) {
                var subgrid_table_id, pager_id;
                subgrid_table_id = subgrid_id + "_t";
                pager_id = "p_" + subgrid_table_id;
                $("#" + subgrid_id).html("<table id='" + subgrid_table_id + "' class='scroll'></table><div id='" + pager_id + "' class='scroll'></div>");
                $("#" + subgrid_table_id).jqGrid({
                    url: '<c:url value="/Flow/find_sub" />',
                    mtype: 'GET',
                    datatype: "json",
                    postData: {
                        id: row_id
                    },
                    colNames: ['id', 'name'],
                    colModel: [
                        {name: "id", index: "id", width: 80, key: true, editable: true, sortable: false, edittype: 'select', editoptions: {value: selectOptions["flow"]}},
                        {name: "name", index: "name", width: 130, sortable: false}
                    ],
                    rowNum: 20,
                    pager: pager_id,
                    sortname: 'id',
                    autowidth: true,
                    editurl: '<c:url value="/Flow/mod_sub" />',
                    jsonReader: {
                        root: "rows",
                        page: "page",
                        total: "total",
                        records: "records",
                        repeatitems: false
                    },
                    sortorder: "asc", height: '100%'});
                $("#" + subgrid_table_id).jqGrid('navGrid', "#" + pager_id,
                        {edit: false, add: true, del: true, search: false},
                        {},
                        {
                            recreateForm: true,
                            zIndex: 9999,
                            beforeSubmit: function (postdata, form) {
                                if (row_id != null) {
                                    // additional parameters
                                    postdata.parentFlowId = row_id;
                                }

                                return [true];
                            }
                        },
                        {
                            recreateForm: true,
                            zIndex: 9999,
                            onclickSubmit: function (rowid) {
                                var val = $("#" + subgrid_table_id).getCell(rowid, 'id');
                                return {id: val, parentFlowId: row_id};
                            }
                        },
                        {}
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
