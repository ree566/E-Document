<%-- 
    Document   : roleView
    Created on : 2017/6/7, 上午 10:50:35
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<sec:authorize access="isAuthenticated()"  var="isLogin" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Role</title>
        <script>
            window.onbeforeunload = function (e) {
                e.preventDefault();
                return "Dude, are you sure you want to refresh? Think of the kittens!";
            };
        </script>
    </head>
    <body>
        <c:choose>
            <c:when test="${isLogin}">
                <table>
                    <tr>
                        <td>username</td>
                        <td>${user.username}</td>
                    </tr>
                    <tr>
                        <td>role</td>
                        <td>${user.authorities}</td>
                    </tr>
                    <tr>
                        <td>jobnumber</td>
                        <td>${user.jobnumber}</td>
                    </tr>
                    <c:forEach var="profile" items="${user.userProfiles}">
                        <tr>
                            <td>${profile.name}</td>
                            <td>${profile.description}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <h5>Hello, anonymous.</h5>
            </c:otherwise>
        </c:choose>
    </body>
</html>
