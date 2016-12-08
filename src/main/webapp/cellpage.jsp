<%-- 
    Document   : getdata
    Created on : 2015/8/26, 上午 11:23:38
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="cellLineDAO" class="com.advantech.model.CellLineDAO" scope="application" />
<!DOCTYPE html>
<html>
    <c:set var="userSitefloor" value="${param.sitefloor}" />
    <c:if test="${(userSitefloor == null) || (userSitefloor == '' || userSitefloor < 1 || userSitefloor > 7)}">
        <c:redirect url="/" />
    </c:if>
    <head> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cell ${userSitefloor} 樓 - ${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="images/favicon.ico"/>
        <style>
            #titleAlert{
                background-color: green;
                color: white;
                text-align: center;
            }
        </style>
        <script src="js/jquery-1.11.3.min.js"></script>
        <script src="js/jquery.cookie.js"></script>
        <script src="js/jquery.blockUI.js"></script>
        <script src="js/jquery.blockUI.Default.js"></script>
        <script src="js/cookie.check.js"></script>
        <script src="js/param.check.js"></script>
        <script>
            var userInfoCookieName = "userInfo", testLineTypeCookieName = "testLineTypeCookieName", cellCookieName = "cellCookieName";
            var serverMsgTimeout;

            $(function () {
                //Add class to transform the button type to bootstrap.
                $(":button").addClass("btn btn-default");
                $("#refresh").addClass("btn-xs");
                $(":text,select,input[type='number']").addClass("form-control");

                $(":text").each(resizeInput);

                if (checkExistCookies() == false) {
                    return false;
                }

                $("#login").click(function () {
                    var lineId = $("#lineId").val();
                    var lineName = $("#lineId option:selected").text().trim();

                    if (checkVal(lineId) == false) {
                        showMsg("Please select a line");
                        return false;
                    }
                    if (confirm("Login " + lineName + " ?")) {
                        var obj = {
                            lineId: lineId,
                            lineName: lineName,
                            action: "LOGIN"
                        };
                        cellLineSwitch(obj);
                    }
                });

                var cellCookie = $.cookie(cellCookieName);
                var cellLoginObj;
                if (cellCookie != null) {
                    var cellLoginObj = $.parseJSON(cellCookie);
                    $("#lineId").val(cellLoginObj.lineId).attr("disabled", true);
                    $("#login").attr("disabled", true);
                    $("#logout").attr("disabled", false);
                    $("#startSchedArea").unblock();
                    showProcessing();
                } else {
                    $("#login").attr("disabled", false);
                    $("#logout").attr("disabled", true);
                    $("#startSchedArea").block({message: "請先登入線別。", css: {cursor: 'default'}, overlayCSS: {cursor: 'default'}});
                    return false;
                }

                $("#logout").click(function () {
                    if (cellLoginObj != null) {
                        if (confirm("Logout " + cellLoginObj.lineName.trim() + " ?")) {
                            var obj = {
                                lineId: cellLoginObj.lineId,
                                action: "LOGOUT"
                            };
                            cellLineSwitch(obj);
                        }
                    } else {
                        showMsg("Can't find your login status");
                    }
                });

                $("#send").click(function () {
                    var lineId = $("#lineId").val();
                    var PO = $("#PO").val();
                    if (checkVal(lineId, PO) == false) {
                        showMsg("Input field can not be empty.");
                        return false;
                    }
                    if (confirm("Begin PO: " + PO + " on line " + $("#lineId option:selected").text().trim() + " ?")) {
                        insertCellInfo();
                    }
                });

                $("#clear").click(function () {
                    deleteSchedJob();
                });

                $("#refresh").click(function () {
                    showProcessing();
                });

                function checkExistCookies() {
                    var testLineTypeCookie = $.cookie(testLineTypeCookieName);
                    var cellCookie = $.cookie(cellCookieName);
                    var babLineTypeCookie = $.cookie(userInfoCookieName);

                    if (testLineTypeCookie != null || babLineTypeCookie != null) {
                        lockAllUserInput();
                        var message = "您已經登入組包裝或測試";
                        showMsg(message);
                        return false;
                    }
                }

                function resizeInput() {
                    $(this).attr('size', $(this).attr('placeholder').length);
                }

                function cellLineSwitch(data) {
                    $.ajax({
                        type: "Post",
                        url: "CellLineServlet",
                        data: data,
                        dataType: "html",
                        success: function (response) {
                            if (response == "success") {
                                if (data.action == "LOGIN") {
                                    generateCookie(cellCookieName, JSON.stringify(data));
                                    $("#lineId").attr("disabled", true);
                                } else if (data.action == "LOGOUT") {
                                    removeCookie(cellCookieName);
                                    $("#lineId").removeAttr("disabled").val(-1);
                                }
                                reload();
                            } else {
                                showMsg(response);
                            }
                        },
                        error: function () {
                            showMsg("error");
                        }
                    });
                }

                function insertCellInfo() {
                    $.ajax({
                        type: "Get",
                        url: "CellScheduleJobServlet",
                        data: {
                            lineId: $("#lineId").val(),
                            PO: $("#PO").val(),
                            action: "insert"
                        },
                        dataType: "html",
                        success: function (response) {
                            showMsg(response);
                            showProcessing();
                        },
                        error: function () {
                            showMsg("error");
                        }
                    });
                }

                function deleteSchedJob() {
                    $.ajax({
                        type: "Get",
                        url: "CellScheduleJobServlet",
                        data: {
                            jobKey: $("#JobKey").val(),
                            action: "delete"
                        },
                        dataType: "html",
                        success: function (response) {
                            showMsg(response);
                            showProcessing();
                            $("#JobKey").val("");
                        },
                        error: function () {
                            showMsg("error");
                        }
                    });
                }

                function showProcessing() {
                    $("#processingArea").html("");
                    $.ajax({
                        type: "Get",
                        url: "CellScheduleJobServlet",
                        data: {
                            action: "select"
                        },
                        success: function (response, status, xhr) {
                            var ct = xhr.getResponseHeader("content-type") || "";
                            var obj = response;
                            if (ct.indexOf('json') > -1) {
                                for (var i = 0; i < obj.length; i++) {
                                    $("#processingArea").append(JSON.stringify(obj[i]) + "<br />");
                                }
                            } else {
                                $("#processingArea").html(obj);
                            }
                        },
                        error: function () {
                            $("#serverMsg").html("error");
                        }
                    });
                }

                //generate all cookies exist 12 hours
                function generateCookie(name, value) {
                    var date = new Date();
                    var minutes = 12 * 60;
                    date.setTime(date.getTime() + (minutes * 60 * 1000));
                    $.cookie(name, value, {expires: date});
                }

                //removeCookieByName
                function removeCookie(name) {
                    $.removeCookie(name);
                }

                //refresh the window
                function reload() {
                    window.location.reload();
                }

                function showMsg(msg) {
                    $("#serverMsg").html(msg);
                    clearTimeout(serverMsgTimeout);
                    serverMsgTimeout = setTimeout(function () {
                        $("#serverMsg").html("");
                    }, 5000);
                }

                function lockAllUserInput() {
                    $(":input,select").not("#redirectBtn, #directlyClose").attr("disabled", "disabled");
                }

            });
        </script>
    </head>
    <body>
        <input id="userSitefloorSelect" type="hidden" value="${userSitefloor}">
        <div id="titleAlert">
            <c:out value="您所選擇的樓層是: ${userSitefloor}" />
            <a href="${pageContext.request.contextPath}">
                <button id="redirectBtn">不是我的樓層?</button>
            </a>
        </div>
        <jsp:include page="temp/head.jsp" />
        <div class="container">
            <div class="row">
                <h1>CELL 站別入口</h1>
            </div>

            <div class="form-group form-inline">
                <label>Line login</label>
                <select id="lineId">
                    <option value="-1">請選擇線別</option>
                    <c:forEach var="cellLine" items="${cellLineDAO.findBySitefloor(userSitefloor)}">
                        <option value="${cellLine.id}" ${cellLine.lock == 1 ? "disabled style='opacity:0.5'" : ""}>
                            線別 ${cellLine.name} / 代號 ${cellLine.aps_lineId} 
                        </option>
                    </c:forEach>
                </select>
                <input type="button" id="login" value="Login" />
                <input type="button" id="logout" value="Logout" />
            </div>

            <div id="startSchedArea">
                <div class="form-group form-inline">
                    <label>Insert</label>
                    <input type="text" id="PO" placeholder="Please insert your PO" />
                    <input type="button" id="send" value="Send" />
                </div>
                <div class="form-group form-inline">
                    <label>Remove</label>
                    <input type="text" id="JobKey" placeholder="example: [lineId]_[PO]" />
                    <input type="button" id="clear" value="Clear" />
                </div>

                <div class="form-group">
                    <label>Processing keys</label>
                    <button id="refresh"><span class="glyphicon glyphicon-repeat"></span></button>
                    <div id="processingArea"></div>
                </div>
            </div>

            <div class="form-group">
                <label>Server message</label>
                <div id="serverMsg"></div>
            </div>
        </div>

        <jsp:include page="temp/footer.jsp" />
    </body>
</html>
