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
            function something(param1, param2, callback){
                console.log(param1);
                console.log(param2);
                
                callback && callback(param2 + "Z");
            }
            
            function three(param){
                console.log(param);
            }
            
            something("one","two", three);
        </script>
    </head>
    <body>


    </body>
</html>
