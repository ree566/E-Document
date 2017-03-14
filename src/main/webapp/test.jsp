<%-- 
    Document   : test
    Created on : 2017/3/13, 上午 10:46:44
    Author     : Wei.Cheng
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="https://code.jquery.com/jquery-1.12.4.min.js" 
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
                crossorigin="anonymous"
        ></script>
        <script>
            $.get("TestServlet", function (responseXml) {                // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response XML...
                var responseData = $(responseXml).find("data").html();
                console.log(responseXml);
                $("#somediv").html(responseData); // Parse XML, find <data> element and append its HTML to HTML DOM element with ID "somediv".
            });
        </script>
    </head>
    <body>
        <h1>Hello World!</h1>
        <div id="somediv"></div>
    </body>
</html>
