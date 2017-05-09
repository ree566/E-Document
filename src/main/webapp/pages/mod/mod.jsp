<%-- 
    Document   : bbb
    Created on : 2017/5/5, 下午 03:49:59
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>MOD page</title>
    </head>
    <body>
        Dear <strong>${user}</strong>, Welcome to MOD Page.
        <a href="<c:url value="/logout" />">Logout</a>
    </body>
</html>
