<%-- 
    Document   : sensorAdjust
    Created on : 2016/4/14, 下午 02:11:41
    Author     : Wei.Cheng
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="lineDAO" class="com.advantech.model.LineDAO" scope="application" />
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
            #goback{
                cursor: pointer;
                color: blue;
            }
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
            #wigetCtrl{
                margin: 0px auto;
                width: 98%
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
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
            <h3>組裝包裝各感應器目前狀態</h3>
            <iframe id="iframe1" style='width:100%; height:500px' src="Sensor?sitefloor=${userSitefloor}"></iframe>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
