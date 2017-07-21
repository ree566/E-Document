<%-- 
    Document   : audit
    Created on : 2017/4/25, 下午 02:58:21
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}/"/>
<link href="<c:url value="/css/multi-select.css" />" rel="stylesheet">
<c:set var="tableName" value="WorktimeColumnGroup" />
<script src="<c:url value="/js/jquery.multi-select.js" />"></script>
<script src="<c:url value="/js/worktime-setting/columnsetting.js" />"></script>
<script>
    $(function () {

        setSelectOptions({
            rootUrl: "<c:url value="/" />",
            columnInfo: [
                {name: "unit", isNullable: false}
            ]
        });

        var grid = $("#list");
        var tableName = '${tableName}';

        var worktimeColumnSelection = "";

        for (var i = 0; i < worktimeCol.length; i++) {
            var column = worktimeCol[i].name;
            var editable = worktimeCol[i].editable;
            if (editable != null && editable == false) {
                continue;
            }
            worktimeColumnSelection += (column + ":" + column + ";");
        }

        grid.jqGrid({
            url: '<c:url value="/${tableName}/read" />',
            datatype: 'json',
            mtype: 'GET',
            autoencode: true,
            colModel: [
                {label: 'id', name: "id", width: 60, key: true, editable: true, editoptions: {readonly: 'readonly', disabled: true, defaultValue: "0"}},
                {label: 'unit', name: "unit.id", width: 60, editable: true, edittype: "select", editoptions: {value: selectOptions["unit"]}, formatter: selectOptions["unit_func"]},
                {label: 'name', name: "columnName", width: 60, editable: true, edittype: 'select', editoptions: {
                        value: worktimeColumnSelection,
                        dataInit: function (elem) {
                            setTimeout(function () {
                                $(elem).multiSelect({
                                    selectableHeader: "<div class='custom-header'>可選欄位</div>",
                                    selectionHeader: "<div class='custom-header'>已選欄位</div>"
                                });
                            }, 150);
                        },
                        multiple: true,
                        defaultValue: 'IN'
                    }
                }
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
                    url: '<c:url value="/${tableName}/update" />',
                    dataheight: 350,
                    width: 650,
                    closeAfterEdit: false,
                    reloadAfterSubmit: true,
                    errorTextFormat: customErrorTextFormat,
                    beforeShowForm: greyout,
                    zIndex: 9999,
                    recreateForm: true
                },
                {
                    url: '<c:url value="/${tableName}/create" />',
                    dataheight: 350,
                    width: 650,
                    closeAfterAdd: false,
                    reloadAfterSubmit: true,
                    errorTextFormat: customErrorTextFormat,
                    beforeShowForm: greyout,
                    zIndex: 9999,
                    recreateForm: true
                },
                {
                    url: '<c:url value="/${tableName}/delete" />',
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

<form>
    <div>
        <h5>Worktime permission change!</h5>
    </div>
    <div>
        <table id="list"></table> 
        <div id="pager"></div>
    </div>
</form>