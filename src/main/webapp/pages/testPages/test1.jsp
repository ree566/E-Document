<%-- 
    Document   : sensorAdjust
    Created on : 2016/4/14, 下午 02:11:41
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
        <!--<link rel="shortcut icon" href="images/favicon.ico"/>-->
        <style>

        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script>
            $(function () {
                $(":text").each(function(){
                    $(this).attr("placeholder",$(this).attr("name"));
                });
            });
        </script>
    </head>
    <body>
        <form class="form-inline" action="../../CellRecordServlet" method="get">
            <input class="form-control" type="text" name="PO">
            <input class="form-control" type="text" name="lineId">
            <input class="form-control" type="text" name="minPcs">
            <input class="form-control" type="text" name="maxPcs">
            <input class="form-control" type="text" name="startDate">
            <input class="form-control" type="text" name="endDate">
            <input class="form-control" type="submit" value="Submit">
        </form>

    </body>
</html>
