<%-- 
    Document   : home
    Created on : 2017/6/2, 下午 05:35:22
    Author     : Wei.Cheng
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String wsPath = "ws://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
    <head>
        <title>Home</title>
    </head>
    <body>
        <h1>
            Hello world!  This is a WebSocket demo!
            <div id="message">

            </div>
        </h1>

        <script src="webjars/jquery/1.12.4/jquery.min.js"></script> 
        <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>


        <script type="text/javascript">

            $(function () {
                //建立socket连接
                var sock;
                if ('WebSocket' in window) {
                    sock = new WebSocket("<%=wsPath%>socketServer");
                } else if ('MozWebSocket' in window) {
                    sock = new MozWebSocket("<%=wsPath%>socketServer");
                } else {
                    sock = new SockJS("<%=basePath%>sockjs/socketServer");
                }
                sock.onopen = function (e) {
                    console.log(e);
                };
                sock.onmessage = function (e) {
                    console.log(e);
                    $("#message").append("<p><font color='red'>" + e.data + "</font>");
                };
                sock.onerror = function (e) {
                    console.log(e);
                };
                sock.onclose = function (e) {
                    console.log(e);
                };
            });

        </script>

    </body>
</html>