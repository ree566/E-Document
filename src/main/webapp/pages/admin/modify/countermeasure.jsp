<%-- 
    Document   : countermeasure
    Created on : 2016/7/26, 上午 08:42:50
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="cDAO" class="com.advantech.model.CountermeasureDAO" scope="application" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../../../images/favicon.ico"/>
        <link rel="stylesheet" href="../../../css/jquery.dataTables.min.css">

        <style>
            body{
                font-size: 16px;
                padding-top: 50px;
            }
            #dataTest td{
                width:20px;
                word-wrap:break-word;
            }
            #dataTest{
                width: 150px;
            }
            /*            th, td { 
                            white-space: nowrap;
                            overflow: hidden; 
                        }*/
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="../../../js/jquery.dataTables.min.js"></script>
        <script src="../../../js/readmore.min.js"></script>
        <script>
            $(function () {
                var columnWidth = 20;
                $("#dataTest").DataTable({
                    "bAutoWidth": false,
                    "ajax": {
                        "url": "<c:url value="/CountermeasureServlet/findAll" />",
                        "type": "GET"
                    },
                    "columns": [
                        {data: "id"},
                        {data: "BABid"},
                        {data: "reason", width: "20px"},
                        {data: "solution"},
                        {data: "editor"},
                        {data: "editTime"}
                    ],
                    "columnDefs": [
//                        {width: columnWidth, targets: [0, 1, 2, 3, 4, 5]},
                        {
                            "type": "html",
                            "targets": [2, 3],
                            'render': function (data, type, full, meta) {
                                return "<article>" + data + "</article>";
                            }
                        }
                    ],
                    paging: false,
                    "initComplete": function (settings, json) {
                        $('article').readmore({
                            collapsedHeight: 50,
                            lessLink: '<a href="#">Read less</a>'
                        });
                    }
                });
            });
        </script>
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <div id="wrapper">

            <%--<jsp:include page="sidebar.jsp" />--%>

            <div class="page-content-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <table id="dataTest" class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>id</th>
                                        <th>BABid</th>
                                        <th>reason</th>
                                        <th>solution</th>
                                        <th>editor</th>
                                        <th>editTime</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
