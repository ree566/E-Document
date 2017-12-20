<%-- 
    Document   : abc
    Created on : 2017/5/5, 下午 03:20:39
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Admin page</title>
        <link href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />" rel="stylesheet">
        <link href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/themes/ui-lightness/jquery-ui.css" rel="stylesheet">
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js" />"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
        <script src="https://jqueryfiledownload.apphb.com/Scripts/jquery.fileDownload.js"></script>

        <script>
            $(function () {
                $(".download").click(function () {
                    console.log("click");
                    $.fileDownload($(this).attr("href"), {
                        preparingMessageHtml: "We are preparing your report, please wait...",
                        failMessageHtml: "There was a problem generating your report, please try again.",
                        successCallback: function (url) {
                            console.log("successCallback");
                        },
                        failCallback: function (html, url) {
                            console.log("failCallback");
                        }
                    });
                    return false;
                });

                $("#test1").click(function () {
                    $.ajax({
                        type: "GET",
                        url: "<c:url value="/testCtrl/test" />",
                        dataType: "json",
                        success: function (response) {
                            console.log(response);
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            console.log(xhr.responseText);
                        }
                    });
                });
            });
        </script>
    </head>
    <body>
        <div class="container">
            <h3>
                <a class="download" href="<c:url value="/testCtrl/test" />">
                    test
                </a>
            </h3>
            <h3>
                <a class="download" href="<c:url value="/testCtrl/exceptionTest" />">
                    exceptionTest
                </a>
            </h3>
            <h3>
                <a class="download" href="<c:url value="/testCtrl/runtimeExceptionTest" />">
                    runtimeExceptionTest
                </a>
            </h3>
            <h3>
                <a class="download" href="<c:url value="/testCtrl/mavExceptionTest" />">
                    mavExceptionTest
                </a>
            </h3>
        </div>
        <input type="button" id="test1" value="test" />
    </body>
</html>
