<%-- 
    Document   : sensorAdjust
    Created on : 2016/4/14, 下午 02:11:41
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <!--<link rel="shortcut icon" href="images/favicon.ico"/>-->
        <style>

        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.8/angular.min.js"></script>
        <script src="controller.js"></script>
    </head>
    <body>
        <c:set var="colMax" value="50" />
        <table style='border:2px solid black'>
            <tr>
                <c:forEach var="i" begin="1" end="${colMax}">
                    <th>${i}</th>
                    </c:forEach>
            </tr>
            <c:forEach var="i" begin="1" end="${colMax}">
                <tr>
                    <c:forEach var="i" begin="1" end="${colMax}">
                        <td>${i}</td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
