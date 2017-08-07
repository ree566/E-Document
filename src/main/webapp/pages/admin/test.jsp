<%-- 
    Document   : abc
    Created on : 2017/5/5, 下午 03:20:39
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Admin page</title>
        <link href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />" rel="stylesheet">
        <link href="<c:url value="/css/multi-select.css" />" rel="stylesheet">
        <link href="<c:url value="/webjars/free-jqgrid/4.14.1/css/ui.jqgrid.min.css" />" rel="stylesheet"/>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js" />"></script>
        <script src="<c:url value="/webjars/free-jqgrid/4.14.1/js/jquery.jqgrid.min.js" />"></script>
        <script src="<c:url value="/webjars/free-jqgrid/4.14.1/js/grid.jqueryui.js" />"></script>

        <script>
            $(function () {

                var testData = [
                    {"a": "a", "b": "b", "c": "c"},
                    {"a": "a", "b": "b", "c": "c"},
                    {"a": "a", "b": "b", "c": "c"}
                ];

                var keys = Object.keys(testData[0]);

                var arr = [];
                for (var i = 0; i < keys.length; i++) {
                    var obj = {};
                    for (var j = 0; j < testData.length; j++) {
                        var d = testData[j];
                        obj[j] = d[keys[i]];
                    }
                    arr[i] = obj;
                }

                var data = {
                    "page": "1",
                    "records": "3",
                    "rows": arr
                };
                
                var grid = $("#list");

                grid.jqGrid({
                    guiStyle: "bootstrap",
                    colModel: [
                        {label: 'a', name: "0"},
                        {label: 'b', name: "1"},
                        {label: 'c', name: "2"}
                    ],
                    pager: '#packagePager',
                    datatype: "jsonstring",
                    datastr: data,
                    jsonReader: {repeatitems: false},
                    rowNum: 2,
                    viewrecords: true,
                    caption: "Packages",
                    height: "auto",
                    ignoreCase: true
                });

            });
        </script>
    </head>
    <body>
        <div id="result">
            <table id="list"></table> 
            <div id="pager"></div>
        </div>
    </body>
</html>
