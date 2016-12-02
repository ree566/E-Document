<%-- 
    Document   : getdata
    Created on : 2015/8/26, 上午 11:23:38
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="testDAO" class="com.advantech.model.TestDAO" scope="application" />
<!DOCTYPE html>
<html>
    <c:set var="userSitefloor" value="${param.sitefloor}" />
    <c:if test="${(userSitefloor == null) || (userSitefloor == '' || userSitefloor < 1 || userSitefloor > 7)}">
        <c:redirect url="/" />
    </c:if>
    <head> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cell ${userSitefloor} 樓 - ${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="images/favicon.ico"/>
        <style>
            #titleAlert{
                background-color: green;
                color: white;
                text-align: center;
            }
        </style>
        <script src="js/jquery-1.11.3.min.js"></script>
        <script src="js/jquery.cookie.js"></script>
        <script src="js/jquery.blockUI.js"></script>
        <script src="js/jquery.blockUI.Default.js"></script>
        <script src="js/cookie.check.js"></script>
        <script src="js/param.check.js"></script>
        <script>
            $(function () {
                //Add class to transform the button type to bootstrap.
                $(":button").addClass("btn btn-default");
                $(":text,select,input[type='number']").addClass("form-control");

                $(":text").each(resizeInput);

                $("#send").click(function () {
                    $.ajax({
                        type: "Get",
                        url: "CellScheduleJobServlet",
                        data: {
                            lineId: $("#lineId").val(),
                            PO: $("#PO").val()
                        },
                        dataType: "html",
                        success: function (response) {
                            $("#serverMsg").html(response);
                        },
                        error: function () {
                            $("#serverMsg").html("error");
                        }
                    });
                });

                function resizeInput() {
                    $(this).attr('size', $(this).attr('placeholder').length);
                }
            });
        </script>
    </head>
    <body>
        <input id="userSitefloorSelect" type="hidden" value="${userSitefloor}">
        <div id="titleAlert">
            <c:out value="您所選擇的樓層是: ${userSitefloor}" />
            <a href="${pageContext.request.contextPath}">
                <button id="redirectBtn">不是我的樓層?</button>
            </a>
        </div>
        <jsp:include page="temp/head.jsp" />
        <div class="container">
            <div>
                <h1>CELL 站別入口</h1>
            </div>
            <div class="form-inline">
                <input type="text" id="PO" placeholder="Please insert your PO">
                <input type="text" id="lineId" placeholder="Please insert your lineId">
                <input type="button" id="send" value="send">
            </div>
            <div id="serverMsg"></div>
        </div>
        <jsp:include page="temp/footer.jsp" />
    </body>
</html>
