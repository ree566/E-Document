<%-- 
    Document   : SysInfo
    Created on : 2016/4/14, 上午 10:54:20
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />"/>
        <style>
            #title{
                /*border: 1px black solid;*/
                padding: 10px 10px;
            }
            #wigetCtrl{
                /*                margin: 0 auto;
                                width:98%;
                                text-align: center;*/
            }
            .btnGroup{
                padding: 10px 10px;
            }
            img{
                width: 80px;
                height: 50px;
            }
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script>
            $(function () {
                $("button").addClass("btn btn-default");
            });
        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />
        <div id="wigetCtrl" class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <h3>歡迎來到${initParam.pageTitle}管理頁面，請選擇上方導覽列查詢系統資訊。</h3>
                </div>
            </div>
        </div>
        <c:import url="/temp/admin-footer.jsp" />
    </body>
</html>
