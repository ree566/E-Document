<%-- 
    Document   : stomp
    Created on : 2017/6/8, 下午 04:57:39
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript" src="//code.jquery.com/jquery-1.12.4.min.js"></script>
        <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>
        <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>


        <script>
            var noticeSocket = function () {
                var s = new SockJS('<c:url value="/socket" />');
                var stompClient = Stomp.over(s);
                stompClient.connect({}, function () {
                    console.log('notice socket connected!');
                    stompClient.subscribe('<c:url value="/app/greeting" />', function (data) {
                        var response = JSON.parse(data.body);
                        console.log(response);
                        $('#socket-content').html(response);
                    });
                });
            };
            noticeSocket();
        </script>
    </script>
</head>
<body>
    <div>    
        <div>        
            <button id="connect" onclick="noticeSocket();">Connect</button> 
        </div>    
        <div id="conversationDiv">        
            <div id="socket-content"></div>
        </div>
    </div>
</body>
</html>
