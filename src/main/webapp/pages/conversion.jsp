<%-- 
    Document   : conversion
    Created on : 2017/6/30, 下午 01:17:44
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    .headerRow{
        font-weight: bold;
    }
</style>
<script src="<c:url value="/js/jqgrid-custom-setting.js" />"></script>
<script>
    $(function () {
        var grid = $("#list");
		//, grid2 = $("#list2"), grid3 = $("#list3");
        var tableName = "燒機成本工時對照表", tableName2 = "吹面板工時對照表", tableName3 = "貼合製程工時對照表";

        grid.jqGrid({
            url: '<c:url value="/json/biCost.json" />',
            datatype: 'json',
            mtype: 'GET',
            autoencode: true,
            colModel: [
                {label: 'Category', name: "Category", cellattr: headerRow, frozen: true},
                {label: 'Power (W)', name: "Power (W)", width: 200, cellattr: headerRow, frozen: true},
                {label: '1 Hr', name: "1 Hr"},
                {label: '2 Hrs', name: "2 Hrs"},
                {label: '4 Hrs', name: "4 Hrs"},
//                {label: '5 Hrs', name: "5 Hrs"},
                {label: '6 Hrs', name: "6 Hrs"},
                {label: '8 Hrs', name: "8 Hrs"},
                {label: '10 Hrs', name: "10 Hrs"},
                {label: '12 Hrs', name: "12 Hrs"},
                {label: '16 Hrs', name: "16 Hrs"},
                {label: '24 Hrs', name: "24 Hrs"},
                {label: '36 Hrs', name: "36 Hrs"},
                {label: '48 Hrs', name: "48 Hrs"},
                {label: '72 Hrs', name: "72 Hrs"}

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
            loadonce: true,
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
            caption: tableName,
//            height: 450,
            sortname: '1 Hr', sortorder: 'asc',
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Ajax Error occurred\n"
                        + "\nstatus is: " + xhr.status
                        + "\nthrownError is: " + thrownError
                        + "\najaxOptions is: " + ajaxOptions
                        );
            }
        });
/*
        grid2.jqGrid({
            url: '<c:url value="/json/cleanPanel.json" />',
            datatype: 'json',
            mtype: 'GET',
            autoencode: true,
            colModel: [
                {label: '面板尺寸/材質', name: "面板尺寸/材質", cellattr: headerRow, frozen: true},
                {label: '不含組裝動作 (min)', name: "清潔面板"},
                {label: '(例:玻璃、音波)含組裝動作 (min)', name: "清潔亮面材質"},
                {label: '(例:電阻、電容)含組裝動作(min)', name: "清潔霧面材質"}

            ],
            rowNum: 20,
            rowList: [20, 50, 100],
            pager: '#pager2',
            viewrecords: true,
            autowidth: true,
            shrinkToFit: true,
            hidegrid: true,
            stringResult: true,
            gridview: true,
            loadonce: true,
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
            caption: tableName2,
//            height: 450,
            sortname: '1 Hr', sortorder: 'asc',
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Ajax Error occurred\n"
                        + "\nstatus is: " + xhr.status
                        + "\nthrownError is: " + thrownError
                        + "\najaxOptions is: " + ajaxOptions
                        );
            }
        });

        grid2.jqGrid('setGroupHeaders', {
            useColSpanStyle: true,
            groupHeaders: [
                {startColumnName: '清潔面板', numberOfColumns: 1, titleText: '<em>清潔面板</em>'},
                {startColumnName: '清潔亮面材質', numberOfColumns: 1, titleText: '<em>清潔亮面材質</em>'},
                {startColumnName: '清潔霧面材質', numberOfColumns: 1, titleText: '<em>清潔霧面材質</em>'}
            ]
        });

        grid3.jqGrid({
            url: '<c:url value="/json/biCost_LCD.json" />',
            datatype: 'json',
            mtype: 'GET',
            autoencode: true,
            colModel: [
                {label: 'LCD Size', name: "LCD Size"},
                {label: 'Touch', name: "Touch"},
                {label: 'Panel', name: "Panel"},
                {label: '貼合<br/>(含清潔)', name: "貼合"},
                {label: 'Test', name: "Test"},
                {label: 'Packing<br/>(不含附件盒)', name: "Packing"},
                {label: 'Total<br/>(Mins)', name: "Total"},
                {label: 'Hourly Rate:<br/> NTD 700', name: "Hourly Rate: NTD 700"},
                {label: '製費<br/>(USD)', name: "製費 (USD)"}

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
            loadonce: true,
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
            caption: tableName3,
//            height: 450,
//            sortname: '1 Hr', sortorder: 'asc',
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Ajax Error occurred\n"
                        + "\nstatus is: " + xhr.status
                        + "\nthrownError is: " + thrownError
                        + "\najaxOptions is: " + ajaxOptions
                        );
            }
        });

        grid3.jqGrid('setGroupHeaders', {
            useColSpanStyle: true,
            groupHeaders: [
                {startColumnName: "LCD Size", numberOfColumns: 1, titleText: "<em>AR製程</em>"},
                {startColumnName: "Touch", numberOfColumns: 6, titleText: "<em>產品工時 (min)</em>"},
                {startColumnName: "Hourly Rate: NTD 700", numberOfColumns: 1, titleText: "<em>製費 (NTD)</em>"}
            ]
        }).jqGrid("setGroupHeaders", {
            groupHeaders: [
                {startColumnName: 'Touch', numberOfColumns: 2, titleText: '<em>PI</em>'}
            ]
        });

*/
        function headerRow(rowId, cellValue, rawObject, cm, rdata) {
            return " class='ui-state-default headerRow'";
        }

    });
</script>

<div id="flow-content">
    <div>
        <table id="list"></table> 
    </div>
    <hr />
	<!--
    <div>
        <table id="list2"></table> 
    </div>
    <hr />
    <div>
        <table id="list3"></table> 
    </div>
	-->
</div>
