<%-- 
    Document   : roleView
    Created on : 2017/6/7, 上午 10:50:35
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Role</title>
    </head>
    <body>
        <table>
            <tr>
                <td>username</td>
                <td>${user.username}</td>
            </tr>
            <tr>
                <td>role</td>
                <td>${user.authorities}</td>
            </tr>
            <c:forEach var="profile" items="${user.userProfiles}">
                <tr>
                    <td>${profile.type}</td>
                    <td>${profile.description}</td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
