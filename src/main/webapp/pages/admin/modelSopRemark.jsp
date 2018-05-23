<%-- 
    Document   : modelSopRemark
    Created on : 2018/5/22, 上午 11:28:40
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../../images/favicon.ico"/>
        <link rel="stylesheet" href="<c:url value="/webjars/datatables/1.10.16/css/jquery.dataTables.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/bootstrap-datetimepicker.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/buttons.dataTables.min.css" />">
        <style>
            #wigetCtrl{
                margin: 0 auto;
                width: 98%;
            }
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/datatables/1.10.16/js/jquery.dataTables.min.js" /> "></script>
        <script src="<c:url value="/webjars/jquery-blockui/2.70/jquery.blockUI.js" /> "></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>
        <script src="<c:url value="/js/param.check.js"/>"></script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />
        <div id="wigetCtrl">
            <h1>ModelSopRemark</h1>
            <div>
                <div class="form-inline">
                    <input type="text" class="form-control" placeholder="modelName">
                    <input type="text" class="form-control" placeholder="remark">
                </div>
                <div class="form-inline">
                    <input type="text" class="form-control" placeholder="station">
                    <input type="text" class="form-control" placeholder="sopName">
                    <input type="text" class="form-control" placeholder="sopPage">
                </div>
                <div class="form-inline">
                    <input type="submit" class="form-control btn btn-default" value="send">
                </div>
            </div>
        </div>
    </body>
</html>
