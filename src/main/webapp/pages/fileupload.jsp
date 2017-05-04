<%-- 
    Document   : fileupload
    Created on : 2017/4/17, 上午 08:51:56
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}/"/>
<script>
    $(function () {
        $("#sync-img").hide();
        var options = {
            beforeSend: function () {
                $("#progressbox").show();
                // clear everything
                $("#progressbar").width('0%');
                $("#message").empty();
                $("#percent").html("0%");
            },
            uploadProgress: function (event, position, total, percentComplete) {
                $("#progressbar").width(percentComplete + '%');
                $("#percent").html(percentComplete + '%');

                // change message text to red after 50%
                if (percentComplete > 50) {
                    $("#sync-img").show();
                    $("#message").html("<font color='red'>File Upload is in progress</font><p>Data synchronizing...</p>");
                }
            },
            success: function (response) {
                $("#progressbar").width('100%');
                $("#percent").html('100%');
                $("#message").html("<font color='blue'>" + response + "</font>");
            },
            complete: function (response) {
                $("#message").html("<font color='blue'>Your file has been uploaded!</font>");
                $("#sync-img").hide();
            },
            error: function () {
                $("#message").html("<font color='red'> ERROR: unable to upload files</font>");
                $("#sync-img").hide();
            }
        };
        $("#uploadForm").ajaxForm(options);
    });
</script>
<div>
    <h3>Upload single files example.</h3>
    <form id="uploadForm" method="POST" action="${root}uploadFile.do" enctype="multipart/form-data">
        <select name="action">
            <option value="add">add</option>
            <option value="update">update</option>
        </select>
        <div>
            File to upload: <input type="file" name="file">
        </div>
        <input type="submit" value="Upload"> Press here to upload the file!
        <div id="progressbox">
            <div id="progressbar"></div>
            <div id="percent">0%</div>
            <img id="sync-img" src="../images/hex-loader2.gif" />
        </div>
        <br />
        <div id="message"></div>
    </form>	
</div>
<hr />
<div style="display: none">
    <h3>Upload multiple files example.</h3>
    <form method="POST" action="${root}uploadMultipleFile" enctype="multipart/form-data">
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

