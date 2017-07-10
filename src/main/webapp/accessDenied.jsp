<%-- 
    Document   : accessDenied
    Created on : 2017/5/5, 下午 03:53:21
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>${initParam.pageTitle} - Access denied.</title>
        <link rel="shortcut icon" href="images/favicon.ico"/>
    </head>
    <body>
        Dear <strong>User</strong>, You are not authorized to access this page
        <a href="<c:url value="/" />">Home</a>
    </body>
</html>
