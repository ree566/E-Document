<%-- 
    Document   : fileupload
    Created on : 2017/4/17, 上午 08:51:56
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
    <h3>Upload single files example.</h3>
    <form method="POST" action="uploadFile" enctype="multipart/form-data">
        <div>
            File to upload: <input type="file" name="file">
        </div>
        <input type="submit" value="Upload"> Press here to upload the file!
    </form>	
</div>
<hr />
<div style="display: none">
    <h3>Upload multiple files example.</h3>
    <form method="POST" action="uploadMultipleFile" enctype="multipart/form-data">
        <div>
            File1 to upload: <input type="file" name="file">
        </div>
        <div>
            File2 to upload: <input type="file" name="file">
        </div>
        <input type="submit" value="Upload"> Press here to upload the file!
    </form>
</div>
<div>
    <c:forEach items="${message}" var="element">    
        <c:out value="${element}"/>
        <br />
    </c:forEach>
</div>

