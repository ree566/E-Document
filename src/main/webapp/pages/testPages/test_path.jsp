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
        <style type="text/css">
            .end-element { background-color : #FFCCFF; }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script>
            $(function () {
                $("#submit").click(function () {
                    $.ajax({
                        type: "POST",
                        url: "<c:url value="/TestController/testReadNetworkDriveFile" />",
                        dataType: "json",
                        data: {
                            path: $("#path").val()
                        },
                        success: function (response) {
                            $("#result").html(response);
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            $("#result").html(xhr.responseText);
                        }
                    });
                });
            });
        </script>
    </head>
    <body>
        <input type="text" id="path" placeholder="please insert path here" style="width: 800px"><input type="button" id="submit" value="TEST">
        <div id="result"></div>
    </body>
</html>
