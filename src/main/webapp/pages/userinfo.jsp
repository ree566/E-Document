<%-- 
    Document   : welcome
    Created on : 2017/5/5, 下午 03:50:27
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Welcome page</title>
    </head>
    <body>
        <sec:authorize access="isAuthenticated()"> 
            <sec:authentication var="user" property="principal" />
            <p>Username : ${user.username}</p>
            <p>Floor : ${user.floor.name}</p>
            <p>Unit : ${user.unit.name}</p>
            <p>Permission : ${user.permission}</p>
            This is a welcome page.
        </sec:authorize>
    </body>
</html>
