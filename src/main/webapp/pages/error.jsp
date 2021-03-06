<%-- 
    Document   : error
    Created on : 2017/4/24, 上午 11:51:00
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />"/>
        <link href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />" rel="stylesheet">
        <link href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap-theme.min.css"/>" rel="stylesheet">
        <style>
            #errormsg-box{
                border: 2px red solid;
            }
        </style>

        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js" />"></script>
    </head>
    <body>
        <div class="container">
            <h1>錯誤</h1>
            <h3>錯誤訊息如下</h3>
            <div id="errormsg-box">
                <h3><c:out value="${exception}" /></h3>
            </div>
            <a href="<c:url value="/" />">回到首頁</a>
        </div>
    </body>
</html>
