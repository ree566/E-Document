<%-- 
    Document   : page2
    Created on : 2017/4/19, 上午 09:35:51
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}/"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../images/favicon.ico"/>

        <link href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" rel="stylesheet">
        <link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
        <link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
        <link href="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/css/ui.jqgrid.css" rel="stylesheet"/>

        <script src="//code.jquery.com/jquery-1.12.4.min.js" 
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
        crossorigin="anonymous"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"
                integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU="
        crossorigin="anonymous"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/js/i18n/grid.locale-tw.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/js/jquery.jqGrid.min.js"></script>

        <script>
            $(function () {
                var grid = $("#list");

                grid.jqGrid({
                    url: '${root}/getBla.do/Flow',
                    datatype: 'json',
                    mtype: 'GET',
                    colModel: [
                        {label: 'rowId', name: "id", width: 60, key: true, editable: true, editoptions: {readonly: 'readonly', disabled: true, defaultValue: "0"}},
                        {label: 'name', name: "name", width: 60, editable: true},
                        {label: 'flow_group_id', name: "flowGroup.id", editable: true}
                    ],
                    rowNum: 20,
                    rowList: [20, 50, 100],
                    pager: '#pager',
                    viewrecords: true,
                    autowidth: true,
                    shrinkToFit: true,
                    hidegrid: true,
                    prmNames: {id: "rowId"},
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
                    caption: "Flow modify",
                    height: 450,
                    editurl: '${root}/testRequest.do',
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
                                    recreateForm: true
                                },
                                {
                                    dataheight: 350,
                                    width: 450,
                                    closeAfterAdd: false,
                                    reloadAfterSubmit: true,
                                    recreateForm: true
                                },
                                {
                                    reloadAfterSubmit: true
                                },
                                {
                                    sopt: ['eq', 'ne', 'lt', 'gt', 'cn', 'bw', 'ew'],
                                    closeAfterSearch: false,
                                    reloadAfterSubmit: true
                                }
                        );

            });
        </script>
    </head>
    <body>
        <div id="flow-content">
            <table id="list"></table> 
            <div id="pager"></div>
        </div>
    </body>
</html>
