<%-- 
    Document   : options
    Created on : 2017/1/4, 上午 09:08:54
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../../../images/favicon.ico"/>
        <link rel="stylesheet" href="../../../css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="../../../css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" href="../../../css/jquery-ui.css">
        <link rel="stylesheet" href="../../../css/buttons.dataTables.min.css">
        <link rel="stylesheet" href="../../../css/bootstrap.min.css">
        <link rel="stylesheet" href="../../../css/bootstrap-table.css">
        <link rel="stylesheet" href="../../../css/bootstrap-editable.css">

        <style>
            table tr td{
                padding: 5px;
            }

            .alarm{
                color: red;
            }
        </style>

        <script src="../../../js/jquery-1.11.3.min.js"></script>
        <script src="../../../js/jquery-ui-1.10.0.custom.min.js"></script>
        <script src="../../../js/jquery.dataTables.min.js"></script>
        <script src="../../../js/jquery.blockUI.js"></script>
        <script src="../../../js/moment.js"></script>
        <script src="../../../js/bootstrap-datetimepicker.min.js"></script>
        <script src="../../../js/bootstrap.min.js"></script>
        <script src="../../../js/bootstrap-table.js"></script>
        <script src="../../../js/bootstrap-table-editable.js"></script>
        <script src="../../../js/bootstrap-table-toolbar.js"></script>
        <script src="../../../js/bootstrap-editable.min.js"></script>
        <script src="../../../js/param.check.js"></script>

        <script>
            $(function () {
                $("#tagNameList").bootstrapTable({
                    idField: 'orginTagName',
                    url: "../../../TagNameComparisonServlet",
                    method: 'POST',
                    columns: [
                        {
                            field: "orginTagName",
                            title: "原始TagName",
                            editable: {
                                type: 'text'
                            },
                            sortable: true
                        },
                        {
                            field: "lampSysTagName",
                            title: "藍燈TagName",
                            editable: {
                                type: 'text'
                            },
                            sortable: true
                        },
                        {
                            field: "lineId",
                            title: "線別",
                            editable: {
                                type: 'select',
                                source: "../../../GetLine",
                                'prepend': {none: "none"}
                            },
                            sortable: true
                        },
                        {
                            field: "stationId",
                            title: "站別",
                            editable: {
                                type: 'number'
                            },
                            sortable: true
                        }
                    ],
                    pagination: true,
                    search: true,
                    escape: true
                });

                $('#tagNameList').on('update', function (e, editable) {
                    alert('new value: ' + editable.value);
                });
            });
        </script>
    </head>
    <body>
        <div class="container">
            <div class="page-header">
                <h3>${initParam.pageTitle}設定</h3>
            </div>

            <div class="row">
                <h5>Quartz setting</h5>
                <table class="table table-bordered">
                    <tr>
                        <td>
                            <label>Resched all numLamp job</label>
                        </td>
                        <td>
                            <input type="button" id="numLampResched" class="btn btn-default" value="resched" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Clear all jobKey by group</label>
                        </td>
                        <td class="form-inline">
                            <input type="text" class="form-control" placeholder="Insert jobGroup">
                            <input type="button" id="removeJobGroup" class="btn btn-default" value="remove" />
                            <input type="button" id="getJobs" class="btn btn-default" value="getJobs" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Show all sched jobs</label>
                        </td>
                        <td class="form-inline">
                            <input type="button" id="getJobs" class="btn btn-default" value="getJobs" />
                        </td>
                    </tr>
                    <tr class="danger">
                        <td>
                            <label>Quartz pause or resume.(<strong class="alarm">*Will enter the test mode</strong>)</label>
                        </td>
                        <td>
                            <input type="button" id="quartzPause" class="btn btn-default" value="pause" />
                            <input type="button" id="quartzResume" class="btn btn-default" value="resume" />
                        </td>
                    </tr>
                </table>
            </div>

            <div class="row">
                <h5>Sensor tagName setting</h5>
                <table id="tagNameList">
                </table>
            </div>

            <div class="row">
                <h5>Relative options setting</h5>
            </div>
        </div>
    </body>
</html>
