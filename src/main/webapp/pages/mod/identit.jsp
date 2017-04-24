<%-- 
    Document   : page2
    Created on : 2017/4/19, 上午 09:35:51
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <link href="//cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.14.0/css/ui.jqgrid.min.css" rel="stylesheet"/>

        <link href="../css/sb-admin-2.min.css" rel="stylesheet">
        <link href="../css/metisMenu.min.css" rel="stylesheet">

        <script src="//code.jquery.com/jquery-1.12.4.min.js" 
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
        crossorigin="anonymous"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"
                integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU="
        crossorigin="anonymous"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>

        <script src="//cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.14.0/jquery.jqgrid.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.14.0/js/i18n/grid.locale-tw.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.14.0/jquery.jqgrid.src.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.14.0/modules/jqmodal.js"></script>
        <script src="${root}js/jqgrid-custom-param.js"></script>
        <script>
//            $(function () {
//                var grid = $("#list");
//
//                var flowGroup = getSelectOption("FlowGroup");
//
//                var flowGroupFormater = function (cellvalue, options, rowObject) {
//                    var obj = flowGroup[cellvalue];
//                    return obj == null ? "" : obj;
//                };
//
//                grid.jqGrid({
//                    url: '${root}getBla.do/' + "Identit",
//                    datatype: 'json',
//                    mtype: 'GET',
//                    colModel: [
//                        {label: 'id', name: "id", width: 60, key: true, editable: true, editoptions: {readonly: 'readonly', disabled: true, defaultValue: "0"}},
//                        {label: 'name', name: "name", width: 60, editable: true},
//                        {label: 'flow_group', name: "flowGroup.id", editable: true, formatter: flowGroupFormater, edittype: 'select', editoptions: {value: flowGroup}}
//                    ],
//                    rowNum: 20,
//                    rowList: [20, 50, 100],
//                    pager: '#pager',
//                    viewrecords: true,
//                    autowidth: true,
//                    shrinkToFit: true,
//                    hidegrid: true,
//                    stringResult: true,
//                    gridview: true,
//                    jsonReader: {
//                        root: "rows",
//                        page: "page",
//                        total: "total",
//                        records: "records",
//                        repeatitems: false
//                    },
//                    afterSubmit: function () {
//                        $(this).jqGrid("setGridParam", {datatype: 'json'});
//                        return [true];
//                    },
//                    navOptions: {reloadGridOptions: {fromServer: true}},
//                    caption: "Flow modify",
//                    height: 450,
//                    editurl: '${root}updateBla.do/' + "Identit",
//                    sortname: 'id', sortorder: 'asc',
//                    error: function (xhr, ajaxOptions, thrownError) {
//                        alert("Ajax Error occurred\n"
//                                + "\nstatus is: " + xhr.status
//                                + "\nthrownError is: " + thrownError
//                                + "\najaxOptions is: " + ajaxOptions
//                                );
//                    }
//                })
//                        .jqGrid('navGrid', '#pager',
//                                {edit: true, add: true, del: true, search: true},
//                                {
//                                    dataheight: 350,
//                                    width: 450,
//                                    closeAfterEdit: false,
//                                    reloadAfterSubmit: true,
//                                    errorTextFormat: customErrorTextFormat,
//                                    beforeShowForm: greyout,
//                                    recreateForm: true
//                                },
//                                {
//                                    dataheight: 350,
//                                    width: 450,
//                                    closeAfterAdd: false,
//                                    reloadAfterSubmit: true,
//                                    errorTextFormat: customErrorTextFormat,
//                                    beforeShowForm: greyout,
//                                    recreateForm: true
//                                },
//                                {
//                                    reloadAfterSubmit: true
//                                },
//                                {
//                                    sopt: ['eq', 'ne', 'lt', 'gt', 'cn', 'bw', 'ew'],
//                                    closeAfterSearch: false,
//                                    reloadAfterSubmit: true
//                                }
//                        );
//
//                function getSelectOption(columnName, data) {
//                    var result = {};
//                    $.ajax({
//                        type: "GET",
//                        url: "${root}getBla.do/" + columnName,
//                        data: data,
//                        async: false,
//                        success: function (response) {
//                            var arr = response.rows;
//
//                            for (var i = 0; i < arr.length; i++) {
//                                var innerObj = arr[i];
//                                result[innerObj.id] = innerObj.name;
//                            }
//                        },
//                        error: function (xhr, ajaxOptions, thrownError) {
//                            console.log(xhr.responseText);
//                        }
//                    });
//                    return result;
//                }
//
//            });
//

        </script>
    </head>
    <body>
        <div id="flow-content">
            <table id="list"></table> 
            <div id="pager"></div>
        </div>
    </body>
</html>
