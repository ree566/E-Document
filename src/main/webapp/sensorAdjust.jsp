<%-- 
    Document   : sensorAdjust
    Created on : 2016/4/14, 下午 02:11:41
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <style>
            #goback{
                cursor: pointer;
                color: blue;
            }
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
            #wigetCtrl{
                margin: 0px auto;
                width: 98%
            }
        </style>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script>
            $(function () {
                $("#iframe1").load(function () {
                    console.log("This table is update.");
                });
            });
        </script>
    </head>
    <body>
        <jsp:include page="admin-header.jsp" />
        <div id="wigetCtrl">
            <h3>組裝包裝各感應器目前狀態</h3>
            <iframe id="iframe1" style='width:100%; height:500px' src="Sensor"></iframe>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
