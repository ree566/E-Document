<%-- 
    Document   : test
    Created on : 2015/11/20, 上午 11:36:50
    Author     : Wei.Cheng
https://datatables.net/forums/discussion/20388/trying-to-access-rowdata-in-render-function-with-ajax-datasource-getting-undefined
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="stylesheet" href="css/jquery.dataTables.min.css">
        <style>
            .nodata{
                opacity: 0.2
            }
            #final_time {
                opacity: 0.5;
                position: fixed;
                right: 10px;
                bottom: 10px;    
                padding: 5px 5px;    
                font-size: 14px;
                background: #777;
                color: white;
            }
            #wigetCtrl{
                margin: 0 auto;
                width: 98%;
            }
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
        </style>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/jquery.dataTables.min.js"></script>
        <script src="js/jquery.cookie.js"></script>
        <script>
            $(document).ready(function () {

                Array.prototype.max = function () {
                    var max = this[0];
                    var len = this.length;
                    for (var i = 1; i < len; i++)
                        if (this[i] > max)
                            max = this[i];
                    return max;
                };

                var d = new Date();
                $("#final_time").text(d);//Get the final polling database time.
                var interval = null;//Polling database variable.
                var testtables = 24;//測試table數量(空值要塞入null)

                //DataTable sort init.
                jQuery.fn.dataTableExt.oSort['pct-asc'] = function (x, y) {
                    x = parseFloat(x);
                    y = parseFloat(y);

                    return ((x < y) ? -1 : ((x > y) ? 1 : 0));
                };

                jQuery.fn.dataTableExt.oSort['pct-desc'] = function (x, y) {
                    x = parseFloat(x);
                    y = parseFloat(y);

                    return ((x < y) ? 1 : ((x > y) ? -1 : 0));
                };

                //測試table initialize.
                var table = $("#data").DataTable({
                    "processing": false,
                    "serverSide": false,
                    "ajax": {
                        "url": "GetTotal",
                        "type": "POST",
                        "data": {"type": "type1"}
                    },
                    "columns": [
                        {data: "name"},
                        {data: "number"},
                        {data: "table"},
                        {data: "PRODUCTIVITY"},
                        {data: "isalarm"}
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: true,
                    "columnDefs": [
                        {
                            "type": "html",
                            "targets": 3,
                            'render': function (data, type, full, meta) {
                                return ((data * 100).toFixed(0) + "%");
                            }
                        },
                        {
                            "type": "html",
                            "targets": 4,
                            'render': function (data, type, full, meta) {
                                if (data == 2) {
                                    return "異常";
                                }
                                return (data == 1 ? "<img src='images/red-light.jpg' width=20>" : "");
                            }
                        }
                    ],
                    "order": [[2, "asc"]],
                    "initComplete": function (settings, json) {
                        insertempty();
                    },
                    displayLength: -1,
                    lengthChange: false,
                    filter: false,
                    paginate: false
                });

                var countdownnumber = 5 * 60;
                var diff = 12;
                $(window).focus(function () {
                    interval = setInterval(function () {
                        if (countdownnumber == 0) {
                            $("#final_time").text("您於此網頁停留時間過久，網頁自動更新功能已經關閉。");
                            clearInterval(interval);
                        } else {
                            table.ajax.reload(function () {
                                insertempty();
                            });
                            d = new Date();
                            $("#final_time").text(d);
                        }
                        countdownnumber -= diff;
                    }, diff * 1000);
                    console.log("timer start");
                }).blur(function () {
                    clearInterval(interval);
                    console.log("timer stop");
                });

                $(window).unload(function () {
                    var cookies = $.cookie();
                    for (var cookie in cookies) {
                        $.removeCookie(cookie);
                    }
                });

                //後端丟出的map中把空隙塞入null值
                function insertempty() {
                    var obj = table.column(2).data();
                    for (var i = 1; i <= testtables; i++) {
                        if (obj.indexOf(i) == -1) {
                            table.rows.add([
                                {
                                    name: 'null',
                                    number: 'null',
                                    table: i,
                                    PRODUCTIVITY: 'null',
                                    isalarm: 'null'
                                }
                            ]).draw(false).nodes().to$().addClass("nodata");
                        }
                    }
                }
            });
        </script>
    </head>
    <body>
        <jsp:include page="admin-header.jsp" />
        <div id="wigetCtrl">
        <h3>測試各站別狀態</h3><!----------------------------------------------->
        <div style="width: 50%; background-color: #F5F5F5">
            <div style="padding: 10px">
                <table id="data" class="display" cellspacing="0" width="100%" style="text-align: center">
                    <thead>
                        <tr>
                            <th>姓名</th>
                            <th>工號</th>
                            <th>桌號</th>
                            <th>生產率</th>
                            <th>亮燈</th>
                        </tr>
                    </thead>

                    <tfoot>
                        <tr>
                            <th>姓名</th>
                            <th>工號</th>
                            <th>桌號</th>
                            <th>生產率</th>
                            <th>亮燈</th>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
        </div>
        <div id="final_time"></div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
