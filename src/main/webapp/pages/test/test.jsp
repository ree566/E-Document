<%-- 
    Document   : abc
    Created on : 2017/5/5, 下午 03:20:39
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
        <title>Admin page</title>
        <link href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />" rel="stylesheet">
        <link href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/themes/ui-lightness/jquery-ui.css" rel="stylesheet">
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js" />"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
        <script src="https://jqueryfiledownload.apphb.com/Scripts/jquery.fileDownload.js"></script>

        <script>
            $(function () {
                var data;
                $.ajax({
                    url: '<c:url value="/json/biCost.json" />',
                    async: false,
                    dataType: 'json',
                    success: function (d) {
                        data = d;
                    }
                });

                var value = transformValue(30.34);
                var i = getValueInJson(value);
                console.log(i);
                
                function transformValue(value){
                    return value.toFixed(2) + ' Mins';
                }

                function getValueInJson(value) {
                    var flag = false;
                    for (var i = 0; i < data.length; i++) {
                        var object = data[i];
                        for (var property in object) {
                            if (object.hasOwnProperty(property)) {
                                if (object[property] == value) {
                                    flag = true;
                                    break;
                                }
                            }
                        }
                    }
                    return flag;
                }
            });
        </script>
    </head>
    <body>
        <div class="container">

        </div>
    </body>
</html>
