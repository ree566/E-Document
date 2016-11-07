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
        <link rel="shortcut icon" href="../../images/favicon.ico"/>
        <style>
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
            img{
                width: 800px;
                height: 400px;
            }
        </style>
        <script src="../../js/jquery-1.11.3.min.js"></script>
        <script>
            $(function () {
            });
        </script>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div class="container">

            <ul>
                <li>
                    <h3>如何填上異常回覆?</h3>
                    <ul>
                        <li>
                            <h5>請先至<a href="BabTotal" target="_blank">線平衡資訊查詢</a></h5>
                        </li>
                    </ul>
                    <ul>
                        <li>
                            <h5>搜尋高於藍燈頻率標準的工單，點選後方<code>檢視詳細</code>button</h5>
                            <img src="../../images/sysDocImg/1.jpg" />
                        </li>
                    </ul>
                    <ul>
                        <li>
                            <h5>點選edit開始編輯相關內容，save儲存，undo取消編輯結果</h5>
                            <img src="../../images/sysDocImg/2.jpg" />
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
