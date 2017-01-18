<%-- 
    Document   : error
    Created on : 2016/5/19, 上午 10:46:09
    Author     : Wei.Cheng
    http://www.codejava.net/java-ee/jsp/how-to-handle-exceptions-in-jsp
--%>

<%@ page contentType="text/html" isErrorPage="true" pageEncoding="UTF-8"%>
<%@page import="java.io.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="images/favicon.ico"/>
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <style>
            #goback:hover{
                cursor: pointer;
            }
        </style>

        <script src="js/jquery-1.11.3.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script>
            function goBack() {
                window.history.back();
            }
        </script>
    </head>
    <body>
        <c:set var="ex" value="${pageContext.exception}" />
        <c:if test="${ex == null}">
            <c:redirect url="/" />
        </c:if>
        <div class="container">
            <div class="row">
                <div style="text-align: center">
                    <h2>網頁發生以下錯誤：</h2>
                    <div class="alert alert-danger">
                        <h3>
                            <c:out value="${ex != null ? ex.message : 'Nothing happend'}" />
                        </h3>
                    </div>
                    <h3>若問題持續發生，請聯繫系統管理人員</h3>
                    <a id="goback" onclick="goBack()">回上頁</a>
                </div>
            </div>
        </div>
    </body>
</html>
