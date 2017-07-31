<%-- 
    Document   : fileupload
    Created on : 2017/4/17, 上午 08:51:56
    Author     : Wei.Cheng

    Admin test batch update file date is valid or not
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script>
    $(function () {
        var form = $("#form");
        var form2 = $("#form2");

        $("#sync-img").hide();
        var options = {
            beforeSubmit: function (arr, $form, options) {
                if (!confirm("OK?")) {
                    return false;
                }
            },
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
                $("#message").html(response.responseText);
                $("#sync-img").hide();
            },
            error: function () {
                $("#message").html("<font color='red'> ERROR: unable to upload files</font>");
                $("#sync-img").hide();
            }
        };
        form.ajaxForm(options);
        form2.ajaxForm(options);
    });
</script>
<div>
    <h3>Data check.</h3>
    <form id="form" action="<c:url value="/checkWorktime" />" method="post" enctype="multipart/form-data">
        <div class="form-inline">
            <lable for="file">File to upload: </lable>
            <input type="file" name="file" class="form-control">
        </div>
        <div class="form-inline">
            <input type="submit" value="Upload" class="form-control"> 
            <label>Press here to upload the file!</label>
        </div>
    </form>	
</div>
<hr />
<div>
    <h3>Sync data.</h3>
    <form id="form2" action="<c:url value="/uploadFile" />" method="post" enctype="multipart/form-data">
        <div class="form-inline">
            <lable for="file">File to upload: </lable>
            <input type="file" name="file" class="form-control">
        </div>
        <div class="form-inline">
            <input type="submit" value="Upload" class="form-control"> 
            <label>Press here to upload the file!</label>
        </div>
    </form>	
</div>
<hr />
<div>
    <div id="progressbox">
        <div id="progressbar"></div>
        <div id="percent">0%</div>
        <img id="sync-img" src="<c:url value="/images/hex-loader2.gif" />" />
    </div>
    <div id="message"></div>
</div>
<div>
    <c:forEach items="${message}" var="element">    
        <c:out value="${element}"/>
        <br />
    </c:forEach>
</div>

