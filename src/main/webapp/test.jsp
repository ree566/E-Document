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
        <title>Simple Sidebar - Start Bootstrap Template</title>
        <link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
        <link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
        <link href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" rel="stylesheet">

        <script src="//code.jquery.com/jquery-1.12.4.min.js" 
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
        crossorigin="anonymous"></script>

        <script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>

        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"
                integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU="
        crossorigin="anonymous"></script>

        <script>
            $(function () {
                $("#sD, #eD").datepicker({dateFormat: 'yy-mm-dd'});
            });

        </script>


    </head>
    <body>
        <form action="test.do">
            <div class="form-inline">
                <input type="text" id="sD" name="startDate" class="form-control" />
                <input type="text" id="eD" name="endDate" class="form-control" />
                <input type="submit" id="test" class="form-control" value="test" />
            </div>
        </form>
        <c:forEach var="pageParameter" items="${param}">
            <c:out value="${pageParameter.key}" /> = <c:out value="${pageParameter.value}" />
            <br />
        </c:forEach>
    </body>
</html>
