<%-- 
    Document   : login
    Created on : 2016/5/19, 上午 10:45:16
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
    </head>
    <body>
        <form action="j_security_check" method="post">
            Username<input type="text" name="j_username" /><br />
            Password<input type="password" name="j_password" /><br />
            <input type="submit" />
        </form>
    </body>
</html>
