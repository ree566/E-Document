<%-- 
    Document   : accessDenied
    Created on : 2017/5/5, 下午 03:53:21
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>AccessDenied page</title>
    </head>
    <body>
        Dear <strong>${user.username}</strong>, You are not authorized to access this page
        <a href="<c:url value="/" />">Home</a>
        <p><c:out value="${user.authorities}" /></p>
    </body>
</html>
