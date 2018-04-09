<%-- 
    Document   : threadDump
    Created on : 2018/4/3, 上午 10:41:10
    Author     : Wei.Cheng
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.Set" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Insert title here</title>
    </head>
    <body>
        <%
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
            int i = 0;
            for (Thread threadaux : threadArray) {
                i++;
                out.print(i + " " + threadaux.getName() + "<br/>");
            }
        %>
    </body>

