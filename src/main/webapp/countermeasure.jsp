<%-- 
    Document   : countermeasure
    Created on : 2016/7/26, 上午 08:42:50
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="cDAO" class="com.advantech.model.CountermeasureDAO" scope="application" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="images/favicon.ico"/>
        <link rel="stylesheet" href="css/jquery.dataTables.min.css">
        <style>
            body{
                font-size: 16px;
                padding-top: 70px;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/jquery.dataTables.min.js"></script>
        <script>
            $(function () {
                $("#dataTest").DataTable();
            });
        </script>
    </head>
    <body>
        <jsp:include page="admin-header.jsp" />
        <div class="container">
            <table id="dataTest" class="table table-bordered">
                <thead>
                    <tr>
                        <th>id</th>
                        <th>BABid</th>
                        <th>reason</th>
                        <th>solution</th>
                        <th>editor</th>
                        <th>editTime</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cm" items="${cDAO.countermeasures}">
                        <tr>
                            <td>${cm.id}</td>
                            <td>${cm.BABid}</td>
                            <td>${cm.reason}</td>
                            <td>${cm.solution}</td>
                            <td>${cm.editor}</td>
                            <td>${cm.editTime}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
