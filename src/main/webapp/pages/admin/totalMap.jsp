<%-- 
    Document   : sensorAdjust
    Created on : 2016/4/14, 下午 02:11:41
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <c:set var="userSitefloor" value="${param.sitefloor}" />
    <c:if test="${(userSitefloor == null) || (userSitefloor == '' || userSitefloor < 1 || userSitefloor > 7)}">
        <c:redirect url="SysInfo" />
    </c:if>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../../images/favicon.ico"/>
        <style>
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
            #wigetCtrl{
                margin: 0px auto;
                width: 98%
            }
            iframe1{

            }
        </style>
        <script src="../../js/jquery-1.11.3.min.js"></script>
        <script>
            $(function () {
                $("#iframe1").load(function () {
                    console.log("This table is update.");
                });
            });
        </script>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div id="wigetCtrl">
            <iframe id="iframe1" style='width:100%; height:650px' frameborder="0" scrolling="no" src="map_totalStatus.jsp?sitefloor=${userSitefloor}" webkitAllowFullScreen mozAllowFullScreen allowFullScreen></iframe>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
