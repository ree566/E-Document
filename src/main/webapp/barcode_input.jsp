<%-- 
    Document   : babpage1
    Created on : 2016/8/1, 上午 08:58:43
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <c:set var="userSitefloor" value="${param.sitefloor}" />
    <c:set var="bab_id" value="${bab_id}" />
    <c:set var="tagName" value="${tagName}" />
    <c:if test="${(userSitefloor == null || bab_id == null) || (userSitefloor == '' || (bab_id < 1 || userSitefloor < 1) || userSitefloor > 7)}">
        <%--<c:redirect url="/" />--%>
    </c:if>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>組包裝 ${userSitefloor} 樓 - ID: ${bab_id}</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />"/>
        <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />">
        <style>
            #success {
                color: green;
            }
            #fail {
                color: red;
            }
            #barcode {
                padding: 10px;
                border: 1px solid grey;
                float: left;
                width: 80%;
                background: #f1f1f1;
            }
            /* Style the submit button */
            #status {
                float: left;
                width: 20%;
                padding: 10px;
            }
            #errorMsg {
                color: red;
            }
            .glyphicon-arrow-up {
                color: red;
            }
            .glyphicon-arrow-down {
                color: green;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js" />"></script>
        <script src="<c:url value="/webjars/jquery-blockui/2.70/jquery.blockUI.js" /> "></script>
        <script src="<c:url value="/js/jquery.cookie.js" /> "></script>
        <script src="<c:url value="/js/param.check.js" /> "></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>

        <script>
            var hnd;

            $(function () {
                $(window).focus(function () {
                    $("#barcode").focus();
                });

                $(":text").keyup(function () {
                    $(this).val($(this).val().trim().toLocaleUpperCase());
                }).focus(function () {
                    $(this).select();
                });

                var $status = $("#status");
                var msgBox = $("#errorMsg");
                var lastInput = $("#lastInput");
                var barcode = $("#barcode");

                barcode.on("keyup", function () {
                    var reg = "^[0-9a-zA-Z]+$";
                    var textbox = $(this);
                    window.clearTimeout(hnd);
                    hnd = window.setTimeout(function () {
                        var text = textbox.val();
                        if (text != "" && text.match(reg)) {
                            $.ajax({
                                type: "POST",
                                url: "<c:url value="/BabPassStationRecordController/insert" />",
                                data: {
                                    id: $("#bab_id").val(),
                                    barcode: text,
                                    tagName: $("#tagName").val()
                                },
                                dataType: "html",
                                success: function (response) {
                                    var count = response;
                                    $status.text("V");
                                    textbox.val("");
                                    msgBox.text("");
//                                    lastInput.html("Last input barcode: " + text + " /at: " + moment().format('HH:mm:ss'));
                                    lastInput.html("Last input barcode: " + text + " / " +
                                            ("<span class='glyphicon glyphicon-arrow-" + (count <= 1 ? "down" : "up") + " '>" +
                                                    (count <= 1 ? "Input" : "Output") + "</span>"));
                                },
                                error: function (xhr, ajaxOptions, thrownError) {
                                    msgBox.text(xhr.responseText);
                                    $status.text("X");
                                }
                            });
                        } else {
                            $status.text("X");
                        }
                    }, 1000);

                    $(document).ajaxStart(function () {
                        $("body").block({
                            message: 'Processing'
                        });
                    }).ajaxStop(function () {
                        $("body").unblock();
                    });

                });

                $("#add_error").click(function () {
                    $.ajax({
                        type: "GET",
                        url: "<c:url value="/BabPassStationRecordController/findLastProcessByTagName" />",
                        data: {
                            tagName: $("#tagName").val()
                        },
                        dataType: "json",
                        success: function (response) {
                            var data = response;
                            var barcode = data.barcode;
                            if (confirm(barcode + " 發生異常?\n(" + barcode + " exception occurred?)")) {
                                saveErrorRecord({
                                    id: data.id,
                                    barcode: data.barcode
                                }, null);
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            msgBox.text(xhr.responseText);
                            $status.text("X");
                        }
                    });

                });

                function saveErrorRecord(record, reason) {
                    $.ajax({
                        type: "POST",
                        url: "<c:url value="/BabPassStationErrorRecordController/insert" />",
                        data: record,
                        dataType: "html",
                        success: function (response) {
                            alert(record.barcode + " 異常已儲存\n(" + record.barcode + " exception saved)");
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            msgBox.text(xhr.responseText);
                            $status.text("X");
                        }
                    });
                }
            });

        </script>
    </head>
    <body>
        <input id="userSitefloorSelect" type="hidden" value="${userSitefloor}">
        <input id="bab_id" type="hidden" value="${bab_id}">
        <input id="tagName" type="hidden" value="${tagName}">
        <div class="container">
            <div class="form-group">
                <div>
                    <label for="barcode">Please input barcode:</label>
                    <div class="input-group mb-3">
                        <input type="text" id="barcode" class="form-control" placeholder="Please input barcode" size="20" />
                        <span id="status" class="input-group-addon">V</span>
                        <span class="input-group-btn" >
                            <input type="button" id="add_error" class="btn btn-danger" value="異常"/>
                        </span>
                    </div>
                </div>
                <div id="lastInput"></div>
                <div id="errorMsg"></div>
                <!--                <input type="checkbox" id="manual" />
                                <label for="manual">手動Key in</label>-->
            </div>
        </div>
    </body>
</html>
