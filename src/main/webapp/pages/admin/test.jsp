<%-- 
    Document   : abc
    Created on : 2017/5/5, 下午 03:20:39
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Admin page</title>
        <link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css" />
        <style>
        </style>
        <script src="//code.jquery.com/jquery-1.12.4.min.js" 
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
        crossorigin="anonymous"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>

        <script>
            $(function () {
//                $("input[type='checkbox']").bootstrapSwitch();
            });
        </script>
    </head>
    <body>
        Dear <strong>${user}</strong>, Welcome to Admin Page.
        <a href="<c:url value="/logout" />">Logout</a>

        <div id="testarea">
        </div>
        <form>
            <div class="switch" data-on="primary" data-off="info">
                <input type="checkbox" checked />
            </div>

            <div class="switch" data-on="info" data-off="success">
                <input type="checkbox" checked />
            </div>

            <div class="switch" data-on="success" data-off="warning">
                <input type="checkbox" checked />
            </div>

            <div class="switch" data-on="warning" data-off="danger">
                <input type="checkbox" checked />
            </div>

            <div class="switch" data-on="danger" data-off="primary">
                <input type="checkbox" checked />
            </div>
        </form>
    </body>
</html>
