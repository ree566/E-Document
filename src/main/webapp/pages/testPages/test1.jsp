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

    </head>
    <body>
        中文
        <c:forEach var="map" items="${cDAO.getActionCode()}">
            <p>
                <c:forEach var="entry" items="${map}">
                    ( Key: <c:out value="${entry.key}"/> --- 
                    Value: <c:out value="${entry.value}"/> )
                </c:forEach>
            </p>
        </c:forEach>

    </body>
</html>
