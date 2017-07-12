<%-- 
    Document   : audit
    Created on : 2017/4/25, 下午 02:58:21
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/js/jqgrid-custom-select-option-reader.js" />"></script>
<script src="<c:url value="/js/moment.js" />"></script>
<%--<script src="<c:url value="/webjars/free-jqgrid/4.14.1/plugins/ui.multiselect.js" />"></script>--%>
<script>
    $(function () {
        var grid = $("#list");
        var isGridInitialized = false;
        var today = moment().toDate();
        var yesterday = moment().add(-1, 'days').toDate();

        $("#sD").datepicker({dateFormat: 'yy-mm-dd', defaultDate: yesterday}).datepicker("setDate", yesterday);
        $("#eD").datepicker({dateFormat: 'yy-mm-dd', defaultDate: today}).datepicker("setDate", today);

        setSelectOptions({
            rootUrl: "<c:url value="/" />",
            columnInfo: [
                {name: "floor", isNullable: false},
                {name: "user", nameprefix: "spe_", isNullable: false, dataToServer: "SPE"},
                {name: "user", nameprefix: "ee_", isNullable: false, dataToServer: "EE"},
                {name: "user", nameprefix: "qc_", isNullable: false, dataToServer: "QC"},
                {name: "type", isNullable: false},
                {name: "flow", nameprefix: "bab_", isNullable: false, dataToServer: "1"},
                {name: "flow", nameprefix: "test_", isNullable: true, dataToServer: "3"},
                {name: "flow", nameprefix: "pkg_", isNullable: true, dataToServer: "2"},
                {name: "preAssy", isNullable: true},
                {name: "pending", isNullable: false}
            ]
        });

        $("#send").on("click", function () {
            var modelName = $("#modelName").val();
            var version = $("#version").val();
            var startDate = $("#sD").val();
            var endDate = $("#eD").val();

            if (modelName == null || modelName == '') {
                modelName = '-1';
            }

            if (version == null || version == '') {
                version = -1;
            }

            var isSearchAll = modelName == -1;

            if (!isGridInitialized) {
                getEditRecord(modelName, version, $("#sD").val(), $("#eD").val()); //init the table
                isGridInitialized = true;
            } else {
                grid.jqGrid('clearGridData');
                grid.jqGrid('setGridParam', {url: '<c:url value="/Audit/find/" />' + modelName + '/' + version, postData: {startDate: startDate, endDate: endDate}});
                grid.setGridParam({grouping: isSearchAll});
                grid.trigger('reloadGrid');
            }
        });

        $("#send").trigger("click");

        $("#dialog").dialog({
            autoOpen: false,
            show: {
                effect: "blind",
                duration: 1000
            },
            hide: {
                effect: "explode",
                duration: 1000
            }
        });

        var getColumnIndexByName = function (grid, columnName) {
            var cm = grid.jqGrid('getGridParam', 'colModel');
            for (var i = 0, l = cm.length; i < l; i++) {
                if (cm[i].name === columnName) {
                    return i; // return the index
                }
            }
            return -1;
        };

        //Jqgrid 沒有支援複合主鍵，所以自己產生(用SQL的複合主鍵值相乘產生新的唯一鍵)
        function keyFormat(cellvalue, options, rowObject) {
            return rowObject[0].id * rowObject[1].rev;
        }

        function timestampFormat(cellvalue, options, rowObject) {
            var t = moment(cellvalue);
            return t.format('YYYY-MM-DD H:mm:ss');
        }

        function getEditRecord(rowId, version, startDate, endDate) {
            grid.jqGrid({
                url: '<c:url value="/Audit/find/" />' + rowId + '/' + version,
                postData: {
                    startDate: startDate,
                    endDate: endDate
                },
                iconSet: "fontAwesome",
                datatype: 'json',
                mtype: 'GET',
                autoencode: true,
                colModel: [
                    {label: 'CPK', name: 'CPK', key: true, frozen: false, hidden: true, search: false, formatter: keyFormat},
                    {label: '差異', name: 'displayorder', template: "actions", search: false, editable: false},
                    {label: 'id', name: "id", jsonmap: "0.id", frozen: false, hidden: true, search: false},
                    {label: '版本', name: "REV", jsonmap: "1.rev", frozen: false, hidden: false, search: false},
                    {label: '機種', name: "modelName", jsonmap: "0.modelName", frozen: false, searchrules: {required: true}, searchoptions: search_string_options, formoptions: required_form_options},
                    {label: '修改者', name: "username", jsonmap: "1.username", frozen: false, hidden: false, search: false},
                    {label: '動作', name: "REVTYPE", jsonmap: "2", frozen: false, hidden: false, search: false},
                    {label: '修改日期', name: "REVTSTMP", jsonmap: "1.revtstmp", hidden: false, search: false, formatter: timestampFormat}
                ],
                rowNum: 100,
                rowList: [100, 200, 500, 1000],
                pager: '#pager',
                viewrecords: true,
                autowidth: true,
                shrinkToFit: true,
                hidegrid: true,
                stringResult: true,
                gridview: true,
                grouping: true,
//                multiSelect: true,
                jsonReader: {
                    root: "rows",
                    page: "page",
                    total: "total",
                    records: "records",
                    repeatitems: false
                },
                beforeSelectRow: function (rowid, e) {
//                    return false;
                },
                loadComplete: function () {
                    $(".dia-btn").click(function (e) {
                        var selRowId = grid.jqGrid('getGridParam', 'selrow');
                        var cellVal = grid.jqGrid('getCell', selRowId, 'REV');
                        alert(cellVal);
                    });
                },
                navOptions: {reloadGridOptions: {fromServer: true}},
                actionsNavOptions: {
                    editbutton: false,
                    delbutton: false,
                    custom: [
                        {
                            action: "open", position: "first",
                            onClick: function (options) {
                                alert("Open, rowid=" + options.rowid);
                            }
                        }
                    ],
                    openicon: "fa-folder-open-o",
                    opentitle: "Open (Enter)",
                    isDisplayButtons: function (options, rowData) {
                        if (options.rowData.closed) { // or rowData.closed
                            return {post: {hidden: true}, del: {display: false}};
                        }
                    }
                },
                caption: "Worktime_AUD",
                error: function (xhr, ajaxOptions, thrownError) {
                    alert("Ajax Error occurred\n"
                            + "\nstatus is: " + xhr.status
                            + "\nthrownError is: " + thrownError
                            + "\najaxOptions is: " + ajaxOptions
                            );
                }
            });

            grid.jqGrid('navGrid', '#pager',
                    {edit: false, add: false, del: false, search: true},
                    {},
                    {},
                    {},
                    {
                        sopt: ['eq', 'ne', 'lt', 'gt', 'cn', 'bw', 'ew'],
                        closeAfterSearch: false,
                        zIndex: 9999,
                        reloadAfterSubmit: true
                    }
            );

            grid.jqGrid('setFrozenColumns');

            function moveButton(cellvalue, options, rowObject) {
                return "<input class='btn btn-default dia-btn' type='button' name='" + cellvalue + "' value='與前次比較' />";
            }
        }
    });
</script>

<div id="flow-content">
    <h4>大表版本歷史紀錄查詢</h4>
    <div class="form-inline">
        <input type="text" id="modelName" class="form-control" placeholder="modelName" />
        <input type="text" id="version" class="form-control" placeholder="version" style="display: none" />
        <input type="text" id="sD" name="startDate" placeholder="startDate" class="form-control" />
        <input type="text" id="eD" name="endDate" placeholder="endDate" class="form-control" />
        <input type="button" id="send" class="form-control" value="send" />
        <h5 class="form-control alert">※modelName 不指定請留白</h5>
    </div>
    <table id="list"></table> 
    <div id="pager"></div>

    <div id="dialog" title="Basic dialog">
        <p>This is an animated dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.</p>
    </div>
</div>