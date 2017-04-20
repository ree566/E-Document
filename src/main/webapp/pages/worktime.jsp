<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}/"/>
<style>
    .ui-jqgrid,
    .ui-jqgrid .ui-jqgrid-view,
    .ui-jqgrid .ui-jqgrid-pager,
    .ui-jqgrid .ui-pg-input {
        font-size: 15px;
    }
    .small-button {
        font-size: .8em !important;
    }
    .ui-button-text {
        font-size: inherit !important;
    } 
    th.ui-th-column div {
        height:auto !important;
    }
    .ui-th-column>div,
    .ui-jqgrid-btable .jqgrow>td {
        word-wrap: break-word; /* IE 5.5+ and CSS3 */
        white-space: pre-wrap; /* CSS3 */
        white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
        white-space: -pre-wrap; /* Opera 4-6 */
        white-space: -o-pre-wrap; /* Opera 7 */
        overflow: hidden;
        vertical-align: middle;
    }
</style>

<script src="${root}/js/urlParamGetter.js"></script>
<script src="${root}/js/sessionExpiredDetect.js"></script>
<script src="${root}/js/column-name-setting.js"></script>

<script>
    $(function () {
//        $(".widget input[type=submit], .widget a, .widget button").button();
        var lastsel, scrollPosition;

        var search_string_options = {sopt: ['eq', 'ne', 'cn', 'bw', 'ew']};
        var search_decimal_options = {sopt: ['eq', 'lt', 'gt']};
        var search_date_options = {sopt: ['eq', 'ne']};
        var required_form_options = {elmsuffix: '(*必填)'};
        var unitName = '${sessionScope.user.userType.name}';
        var modifyColumns = (unitName == null || unitName == "") ? [] : getColumn();
        var grid = $("#list");

        var floorOption = getSelectOption("floor");
        var spe_identitOption = getSelectOption("identit", {unitName: "SPE"});
        var ee_identitOption = getSelectOption("identit", {unitName: "EE"});
        var qc_identitOption = getSelectOption("identit", {unitName: "QC"});
        var typeOption = getSelectOption("type");
        var flowOption = getSelectOption("flow");
        var preAssyOption = getSelectOption("preAssy");
        var pendingOption = getSelectOption("pending");

        var customErrorTextFormat = function (response) {
            return '<span class="ui-icon ui-icon-alert" ' +
                    'style="float:left; margin-right:.3em;"></span>' +
                    response.responseText;
        };

        var greyout = function ($form) {
            $form.find(".FormElement[readonly]")
                    .prop("disabled", true)
                    .addClass("ui-state-disabled")
                    .closest(".DataTD")
                    .prev(".CaptionTD")
                    .prop("disabled", true)
                    .addClass("ui-state-disabled");
        };

        var loopCount = 0;

        grid.jqGrid({
            url: '${root}/getSheetView.do',
            datatype: 'json',
            mtype: 'POST',
            colModel: [
                {label: 'rowId', name: grid_column_name[loopCount++], width: 60, frozen: true, hidden: false, key: true, editable: true, editoptions: {readonly: 'readonly', disabled: true, defaultValue: "0"}},
                {label: 'Model', name: grid_column_name[loopCount++], frozen: true, editable: true, searchrules: {required: true}, searchoptions: search_string_options, editrules: {required: true}, formoptions: required_form_options},
                {label: 'TYPE', name: grid_column_name[loopCount++], edittype: "select", editoptions: {value: typeOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'ProductionWT', name: grid_column_name[loopCount++], width: 120, searchrules: {required: true}, searchoptions: search_decimal_options},
                {label: 'Total Module', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}},
                {label: 'Setup Time', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                {label: 'CleanPanel', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Assembly', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'T1', name: grid_column_name[loopCount++], width: 60, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'T2', name: grid_column_name[loopCount++], width: 60, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'T3', name: grid_column_name[loopCount++], width: 60, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'T4', name: grid_column_name[loopCount++], width: 60, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Packing', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Up_BI_RI', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Down_BI_RI', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'BI Cost', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Vibration', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Hi-Pot/Leakage', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Cold Boot', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'Warm Boot', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'ASS_T1', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                {label: 'T2_PACKING', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                {label: 'Floor', name: grid_column_name[loopCount++], edittype: "select", editoptions: {value: floorOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'Pending', name: grid_column_name[loopCount++], edittype: "select", editoptions: {value: pendingOption}, width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                {label: 'Pending TIME', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {required: true, number: true}, editoptions: {defaultValue: '0'}, formoptions: required_form_options},
                {label: 'BurnIn', name: grid_column_name[loopCount++], edittype: "select", editoptions: {value: "Y:Y;N:N"}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'B/I Time', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {required: true, number: true}, editoptions: {defaultValue: '0'}, formoptions: required_form_options},
                {label: 'BI_Temperature', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {required: true, number: true}, editoptions: {defaultValue: '0'}, formoptions: required_form_options},
                {label: 'SPE Owner', name: grid_column_name[loopCount++], edittype: "select", editoptions: {value: spe_identitOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'EE Owner', name: grid_column_name[loopCount++], edittype: "select", editoptions: {value: ee_identitOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'QC Owner', name: grid_column_name[loopCount++], edittype: "select", editoptions: {value: qc_identitOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: '組包SOP', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_string_options, edittype: "textarea", editoptions: {maxlength: 500}},
                {label: '測試SOP', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_string_options, edittype: "textarea", editoptions: {maxlength: 500}},
                {label: 'KEYPART_A', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'KEYPART_B', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: 'PRE-ASSY', name: grid_column_name[loopCount++], edittype: "select", editoptions: {value: preAssyOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'BAB_FLOW', name: grid_column_name[loopCount++], edittype: "select", editoptions: {value: flowOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'TEST_FLOW', name: grid_column_name[loopCount++], edittype: "select", editoptions: {value: flowOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'PACKING_FLOW', name: grid_column_name[loopCount++], edittype: "select", editoptions: {value: flowOption}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'PART-LINK', name: grid_column_name[loopCount++], edittype: "select", editoptions: {value: ":empty;Y:Y;N:N"}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: 'CE', name: grid_column_name[loopCount++], width: 60, searchrules: {required: true}, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'UL', name: grid_column_name[loopCount++], width: 60, searchrules: {required: true}, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'ROHS', name: grid_column_name[loopCount++], width: 60, searchrules: {required: true}, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'WEEE', name: grid_column_name[loopCount++], width: 60, searchrules: {required: true}, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'Made in Taiwan', name: grid_column_name[loopCount++], width: 120, searchrules: {required: true}, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'FCC', name: grid_column_name[loopCount++], width: 60, searchrules: {required: true}, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'EAC', name: grid_column_name[loopCount++], width: 60, searchrules: {required: true}, searchoptions: search_string_options, edittype: "select", editoptions: {value: "0:0;1:1"}},
                {label: 'N合1集合箱', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, editoptions: {defaultValue: '0'}},
                {label: '料號屬性值維護', name: grid_column_name[loopCount++], edittype: "select", editoptions: {value: "Y:Y;N:N"}, width: 100, searchrules: {required: true}, searchoptions: search_string_options},
                {label: '組裝排站人數', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                {label: '包裝排站人數', name: grid_column_name[loopCount++], width: 100, searchrules: {required: true}, searchoptions: search_decimal_options},
                {label: '前置時間', name: grid_column_name[loopCount++], width: 80, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {elmsuffix: '(組裝)'}, editoptions: {defaultValue: '0'}},
                {label: '看板工時', name: grid_column_name[loopCount++], width: 80, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {elmsuffix: '(組裝)'}},
                {label: '前置時間', name: grid_column_name[loopCount++], width: 80, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {elmsuffix: '(包裝)'}, editoptions: {defaultValue: '0'}},
                {label: '看板工時', name: grid_column_name[loopCount++], width: 80, searchrules: {required: true}, searchoptions: search_decimal_options, editrules: {number: true}, formoptions: {elmsuffix: '(包裝)'}},
                {label: 'CleanPanel+Assembly', name: grid_column_name[loopCount++], width: 200, searchrules: {required: true}, searchoptions: search_decimal_options},
                {label: 'Modified_Date', width: 200, name: grid_column_name[loopCount++], formatter: 'date', formatoptions: {srcformat: 'Y-m-d H:i:s A', newformat: 'Y-m-d H:i:s A'}, searchrules: {required: true}, searchoptions: search_date_options}
            ],
            rowNum: 20,
            rowList: [20, 50, 100],
            pager: '#pager',
            viewrecords: true,
            autowidth: true,
            shrinkToFit: false,
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
//                    onSelectRow: function (rowid, status, e) {
//                        if (lastsel)
//                            grid.jqGrid("restoreRow", lastsel);
//                        if (rowid !== lastsel) {
//                            scrollPosition = grid.closest(".ui-jqgrid-bdiv").scrollLeft();
//                            grid.jqGrid("editRow", rowid, true, function () {
//                                setTimeout(function () {
//                                    $("input, select", e.target).focus();
//                                }, 10);
//                            });
//                            setTimeout(function () {
//                                grid.closest(".ui-jqgrid-bdiv").scrollLeft(scrollPosition);
//                            }, 0);
//                            lastsel = rowid;
//                        } else {
//                            lastsel = null;
//                        }
//                    },
            afterSubmit: function () {
                $(this).jqGrid("setGridParam", {datatype: 'json'});
                return [true];
            },
            navOptions: {reloadGridOptions: {fromServer: true}},
            caption: "工時大表",
            height: 450,
//            width: 1200,
            editurl: '${root}/updateSheet.do',
            sortname: 'id', sortorder: 'desc',
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
                            recreateForm: true,
                            beforeShowForm: greyout
                        },
                        {
                            dataheight: 350,
                            width: 450,
                            closeAfterAdd: false,
                            reloadAfterSubmit: true,
                            errorTextFormat: customErrorTextFormat,
                            recreateForm: true,
                            beforeShowForm: greyout
                        },
                        {
                            reloadAfterSubmit: true
                        },
                        {
                            sopt: ['eq', 'ne', 'lt', 'gt', 'cn', 'bw', 'ew'],
                            closeAfterSearch: false,
                            reloadAfterSubmit: true
                        }
                )
                .jqGrid('setGroupHeaders', {
                    useColSpanStyle: true,
                    groupHeaders: [
                        {startColumnName: 'ce', numberOfColumns: 7, titleText: '<em>外箱Label產品資訊 (1：要印   0：不印)</em>'},
                        {startColumnName: 'assyLeadTime', numberOfColumns: 2, titleText: '<em>組裝看板工時</em>'},
                        {startColumnName: 'packingLeadTime', numberOfColumns: 2, titleText: '<em>包裝看板工時</em>'}
                    ]
                })
                .navButtonAdd('#pager', {
                    caption: "Export to Excel",
                    buttonicon: "ui-icon-disk",
                    onClickButton: function () {
                        grid.jqGrid('excelExport', {"url": "${root}generateExcel.do"});
                    },
                    position: "last"
                });

        var columnNames = grid_column_name;

        //Remove not change column
        columnNames = $.grep(columnNames, function (value) {
            return $.inArray(value, do_not_change_columns) == -1;
        });

        //Separate readyonly column and editable column
        var editableColumns = modifyColumns.length == 1 && modifyColumns[0] == -1 ? columnNames : modifyColumns;
        var readonlyColumns = $(columnNames).not(editableColumns).get();

        if (editableColumns.length != 0) {
            for (var i = 0; i < editableColumns.length; i++) {
                var editableColumn = editableColumns[i];
                grid.setColProp(editableColumn, {editable: true});
            }

            for (var i = 0; i < readonlyColumns.length; i++) {
                var readonlyColumn = readonlyColumns[i];
                grid.setColProp(readonlyColumn, {editable: true, editoptions: {readonly: 'readonly', disabled: true}});
            }
        }

        grid.jqGrid('setFrozenColumns');

        $(window).bind('resize', function () {
            
            setTimeout(function(){ 
                grid.jqGrid("setGridWidth", $('#worktime-content').width());
            }, 1000);
            
        }).trigger('resize');

        function getColumn() {
            var result;
            $.ajax({
                type: "Post",
                url: "${root}/unitColumn.do",
                dataType: "json",
                async: false,
                success: function (response) {
                    result = response;
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log(xhr.responseText);
                }
            });
            return result;
        }

        function getSelectOption(columnName, data) {
            var result;
            $.ajax({
                type: "GET",
                url: "${root}/" + columnName + "Option.do",
                data: data,
                async: false,
                success: function (response) {
                    result = response;
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log(xhr.responseText);
                }
            });
            return result;
        }

        function exportDataToExcel() {
            alert("begin download...");
        }

    });
</script>
<div id="worktime-content">
    <%--<div class="row">
        <div>
            <table class="table table-hover">
                <tr>
                    <td>
                        jobnumber: <c:out value="${sessionScope.user.jobnumber}" /> / 
                        name: <c:out value="${sessionScope.user.jobnumber}" /> / 
                        unit: <c:out value="${sessionScope.user.userType.name}" /> /
                        floor: <c:out value="${sessionScope.user.floor.name}" />
                    </td>
                    <td class="widget">
                        <form action="${root}/logout.do" method="post">
                            <input class="btn btn-default" type="submit" value="Logout" />
                        </form>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="row">--%>
    <table id="list"></table> 
    <div id="pager"></div>
    <!--</div>-->
</div>