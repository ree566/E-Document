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
        <link rel="shortcut icon" href="images/favicon.ico"/>
        <style>

        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="../../js/jquery.fullscreen-min.js"></script>
        <script>
            function draw() {
                var canvas = document.getElementById("canvas");
                if (canvas.getContext) {
                    var ctx = canvas.getContext("2d");

                    ctx.fillStyle = "rgb(200,0,0)";
                    ctx.fillRect(10, 10, 55, 50);

                    ctx.fillStyle = "rgba(0, 0, 200, 0.5)";
                    ctx.fillRect(30, 30, 55, 50);
                }
            }
            $(function () {
                $("#fullBtn").click(function () {
                    $("#canvas").fullScreen(true);
                });
            });
        </script>
    </head>
    <body onload="draw();">
        <button id="fullBtn">Full</button>
        <canvas id="canvas" width="150" height="150"></canvas>
    </body>
</html>
