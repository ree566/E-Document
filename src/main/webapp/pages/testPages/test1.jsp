<%-- 
    Document   : sensorAdjust
    Created on : 2016/4/14, 下午 02:11:41
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <!--<link rel="shortcut icon" href="images/favicon.ico"/>-->
        <style>

        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script>
            var flag = true;
            
            console.log('testing');
            
            var f1 = function() {
                console.log('f1');
            };
            
            var f2 = function () {
                console.log('f2');
            };
            
            (flag ? f1 : f2)();
        </script>
    </head>
    <body>


    </body>
</html>
