<%-- 
    Document   : login
    Created on : 2017/1/4, 上午 09:12:50
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../../images/favicon.ico"/>
        <link rel="stylesheet" href="../../css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="../../css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" href="../../css/jquery-ui.css">
        <link rel="stylesheet" href="../../css/buttons.dataTables.min.css">
    </head>
    <body>
        <h1>Login the option page.</h1>
        <form action="../../Login" method="post">
            <input type="text" name="jobnumber" placeholder="userName">
            <input type="password" name="password" placeholder="password">
            <input type="submit" value="submit">
        </form>
        
        <form action="../../Logout" method="post">
            <input type="submit" value="logout">
        </form>
    </body>
</html>
