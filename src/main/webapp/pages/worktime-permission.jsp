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
<%--<script src="<c:url value="/js/jquery.multi-select.js" />"></script>--%>
<script src="<c:url value="/js/worktime-setting/worktime-columnsetting.js" />"></script>
<h1>Worktime permission change!</h1>

<script>
    $(function () {
//        var unitPermissionTb = $("#unitPermission");
//        var units = ["SPE", "EE", "IE", "QC", "MFG", "unlimited"];
//
//        for (var i = 0; i < units.length; i++) {
//            var str = "";
//            str += "<tr>" +
//                    "<td>" + units[i] + "</td>" +
//                    "<td>" + "<select multiple='multiple' class='my-select' name='" + units[i] + "-select[]'></select>" + "</td>" +
//                    "<td><input type='submit' value='submit' /></td>" +
//                    "</tr>";
//            unitPermissionTb.append(str);
//        }
//
//
//        var multiSel = $(".my-select");
//        for (var i = 0; i < worktimeCol.length; i++) {
//            var colName = worktimeCol[i].name;
//            multiSel.append("<option value='" + colName + "'>" + colName + "</option>");
//            multiSel.multiSelect('addOption', {value: colName, text: colName, index: i});
//        }
//
//        multiSel.multiSelect({});
//
//        $(":submit").on("click", function () {
//            console.log(multiSel.val());
//            return false;
//        });

        setSelectOptions({
            rootUrl: "<c:url value="/" />",
            columnInfo: [
                {name: "unit", isNullable: false}
            ]
        });

        var grid = $("#list");
        var tableName = '${tableName}';

        grid.jqGrid({
            url: '<c:url value="/${tableName}/read" />',
            datatype: 'json',
            mtype: 'GET',
            autoencode: true,
            colModel: [
                {label: 'id', name: "id", width: 60, key: true, editable: true, editoptions: {readonly: 'readonly', disabled: true, defaultValue: "0"}},
                {label: 'unit', name: "unit.id", width: 60, editable: true, edittype: "select", editoptions: {value: selectOptions["unit"]}, formatter: selectOptions["unit_func"]},
                {label: 'name', name: "columnName", width: 60, editable: true, edittype: 'select', editoptions: {
                        value: 'FE:FedEx;TN:TNT;IN:Intim',
                        dataInit: function (elem) {
                            setTimeout(function () {
                                $(elem).multiselect({
                                    minWidth: 100, //'auto',
                                    height: 'auto',
                                    selectedList: 2,
                                    checkAllText: "all",
                                    uncheckAllText: "no",
                                    noneSelectedText: "Any",
                                    open: function () {
                                        var $menu = $(".ui-multiselect-menu:visible");
                                        $menu.width("auto");
                                        return;
                                    }
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


//        $("input").addClass("form-control");
    });
</script>

<form>
    <div>
        <form id="testForm">
            <table id="unitPermission" class="table table-bordered">
            </table>
        </form>
    </div>
    <div>
        <table id="list"></table> 
        <div id="pager"></div>
    </div>
</form>