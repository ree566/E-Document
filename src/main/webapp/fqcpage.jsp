<%-- 
    Document   : getdata
    Created on : 2015/8/26, 上午 11:23:38
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <c:set var="userSitefloor" value="${param.sitefloor}" />
    <c:if test="${(userSitefloor == null) || (userSitefloor == '' || userSitefloor < 1 || userSitefloor > 7)}">
    </c:if>
    <head> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FQC ${userSitefloor} 樓 - ${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="images/favicon.ico"/>
        <style>
            #titleAlert{
                background-color: green;
                color: white;
                text-align: center;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/js/jquery.cookie.js" /> "></script>
        <script src="<c:url value="/webjars/jquery-blockui/2.70/jquery.blockUI.js" /> "></script>
        <script src="<c:url value="/js/jquery.blockUI.Default.js" /> "></script>
        <script src="<c:url value="/js/cookie.check.js" /> "></script>
        <script src="<c:url value="/js/param.check.js" /> "></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>
        <script src="<c:url value="/js/stopwatch.js" /> "></script>

        <script>
            var serverErrorConnMessage = "Error, the textbox can't connect to server now.";

            var userInfoCookieName = "userInfo", //組包步驟一的cookie
                    testLineTypeCookieName = "testLineTypeCookieName", //測試線別的cookie 
                    fqcCookieName = "fqcCookieName";
            var serverMsgTimeout;
            var hnd;//鍵盤輸入間隔

            $(function () {
                initStopWatch();
                
                //Add class to transform the button type to bootstrap.
                $(":button").addClass("btn btn-default");
                $("#refresh").addClass("btn-xs");
                $(":text,select,input[type='number']").addClass("form-control");

                $(":text").each(resizeInput);

                if (checkExistCookies() == false) {
                    return false;
                }

                var lines = getLine();
                if (lines != null) {
                    for (var i = 0; i < lines.length; i++) {
                        setLineOptions(lines[i]);
                    }
                }

                var fqcCookie = $.cookie(fqcCookieName);
                var fqcLoginObj;
                if (fqcCookie != null) {
                    var fqcLoginObj = $.parseJSON(fqcCookie);

                    getLineStatus(fqcLoginObj.lineId);

                    if (fqcLoginObj.floor != $("#userSitefloorSelect").val()) {
                        lockAllUserInput();
                        showMsg("您已經登入其他樓層");
                        return false;
                    }

                    $("#lineId").val(fqcLoginObj.lineId).attr("disabled", true);
                    $("#login, #lineId").attr("disabled", true);
                    $("#logout").attr("disabled", false);

                    poDetect();

                    if (fqcLoginObj.po != null) {
                        $("#po").val(fqcLoginObj.po);
                        $("#begin, #po, #type").attr("disabled", true).unbind("click");
                        $("#end").removeAttr("disabled");
                    } else {
                        $("#end").attr("disabled", true).unbind("click");
                        $("#begin, #po, #type").removeAttr("disabled");
                    }
                    $("#startSchedArea").unblock();
                    showProcessing();
                } else {
                    $("#login, #lineId").attr("disabled", false);
                    $("#logout").attr("disabled", true);
//                    $("#startSchedArea").block({message: "請先登入線別。", css: {cursor: 'default'}, overlayCSS: {cursor: 'default'}});
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
                            floor: $("#userSitefloorSelect").val(),
                            action: "LOGIN"
                        };
                        fqcLineSwitch(obj);
                    }
                });

                $("#logout").click(function () {
                    if (fqcLoginObj != null) {
                        if (confirm("Logout " + fqcLoginObj.lineName.trim() + " ?")) {
                            var obj = {
                                lineId: fqcLoginObj.lineId,
                                action: "LOGOUT"
                            };
                            fqcLineSwitch(obj);
                        }
                    } else {
                        showMsg("Can't find your login status");
                    }
                });

                $("#po").on("keyup", function () {
                    var po = $(this).val().toUpperCase().trim();
                    $(this).val(po);
                    getModel(po, $("#modelname"));
                });

                function checkExistCookies() {
                    var testLineTypeCookie = $.cookie(testLineTypeCookieName);
                    var fqcCookie = $.cookie(fqcCookieName);
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

                function setLineOptions(line) {
                    $("#lineId").append("<option value=" + line.id + " " + (line.lock == 1 ? "disabled style='opacity:0.5'" : "") + ">線別 " + line.name + "</option>");
                }

                function getLine() {
//                    var result;
//                    $.ajax({
//                        type: "Get",
//                        url: "CellLineServlet/findBySitefloor",
//                        data: {
//                            sitefloor: $("#userSitefloorSelect").val()
//                        },
//                        dataType: "json",
//                        async: false,
//                        success: function (response) {
//                            result = response;
//                        },
//                        error: function (xhr, ajaxOptions, thrownError) {
//                            showMsg(xhr.responseText);
//                        }
//                    });
//                    return result;
                }

                function getLineStatus(lineId) {
//                    $.ajax({
//                        type: "Post",
//                        url: "CellLineServlet",
//                        data: {
//                            action: "select",
//                            lineId: lineId
//                        },
//                        dataType: "html",
//                        success: function (response) {
//                            var obj = $.parseJSON(response);
//                            console.log(obj);
//                            if (obj.isused == 0) {
//                                removeCookie(fqcCookieName);
//                                reload();
//                            }
//                        },
//                        error: function () {
//                            showMsg("error");
//                        }
//                    });
                }

                function fqcLineSwitch(data) {
                    $.ajax({
                        type: "Post",
                        url: "CellLineServlet",
                        data: data,
                        dataType: "html",
                        success: function (response) {
                            if (response == "success") {
                                if (data.action == "LOGIN") {
                                    generateCookie(fqcCookieName, JSON.stringify(data));
                                    $("#lineId").attr("disabled", true);
                                } else if (data.action == "LOGOUT") {
                                    removeCookie(fqcCookieName);
                                    $("#lineId").removeAttr("disabled").val(-1);
                                }
                                $("#po").val("");
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

                function poDetect() {
                    $.ajax({
                        type: "Post",
                        url: "CellSearch",
                        data: {
                            lineId: $("#lineId").val(),
                            action: "getProcessing"
                        },
                        success: function (response) {
                            var fqcInfo = response.data;
                            var fqcLoginObj = getCellCookie();
                            if (fqcInfo != null && fqcInfo.length != 0 && fqcLoginObj.po == null) {
                                var fqc = fqcInfo[0]; //always get the first input fqc info
                                addpoInCellCookie(fqc.po);
                                reload();
                            } else if ((fqcInfo == null || fqcInfo.length == 0)) {
                                var po = fqcLoginObj.po;
                                if (po != null) {
                                    addpoInCellCookie(null);
                                }
//                                deleteSchedJob();
                                $("#po").val("");
                            }
                        },
                        error: function () {
                            showMsg("error");
                        }
                    });
                }

                function getModel(text, obj) {
                    var reg = "^[0-9a-zA-Z]+$";
                    if (text != "" && text.match(reg)) {
                        window.clearTimeout(hnd);
                        hnd = window.setTimeout(function () {
                            $.ajax({
                                type: "GET",
                                url: "BabController/findModelNameByPo",
                                data: {
                                    po: text.trim()
                                },
                                dataType: "html",
                                success: function (response) {
                                    obj.val(response);
                                    $("#reSearch").show();
                                },
                                error: function () {
                                    showMsg(serverErrorConnMessage);
                                }
                            });
                        }, 1000);
                    } else {
                        obj.val("");
                    }
                }

                function insertCellInfo(data) {
                    $.ajax({
                        type: "Post",
                        url: "<c:url value="/CellScheduleJobServlet/insert" />",
                        data: data,
                        dataType: "html",
                        success: function (response) {
                            showMsg(response);
                            if (response == "success") {
                                addpoInCellCookie($("#po").val());
                                reload();
                            }
                            showProcessing();
                        },
                        error: function () {
                            showMsg("error");
                        }
                    });
                }

                function deleteSchedJob() {
                    $.ajax({
                        type: "Post",
                        url: "<c:url value="/CellScheduleJobServlet/delete" />",
                        data: {
                            lineId: $("#lineId").val(),
                            po: $("#po").val()
                        },
                        dataType: "html",
                        success: function (response) {
                            showMsg(response);
                            showProcessing();
                            if (response == "success") {
                                addpoInCellCookie(null);
                                reload();
                            }
                        },
                        error: function () {
                            showMsg("error");
                        }
                    });
                }

                function showProcessing() {
//                    $("#processingArea").html("");
//                    $.ajax({
//                        type: "GET",
//                        url: "<c:url value="/CellScheduleJobServlet/select" />",
//                        success: function (response, status, xhr) {
//                            var ct = xhr.getResponseHeader("content-type") || "";
//                            var obj = response;
//                            if (ct.indexOf('json') > -1) {
//                                for (var i = 0; i < obj.length; i++) {
//                                    $("#processingArea").append(JSON.stringify(obj[i]) + "<br />");
//                                }
//                            } else {
//                                $("#processingArea").html(obj);
//                            }
//                        },
//                        error: function () {
//                            $("#serverMsg").html("error");
//                        }
//                    });
                }

                function getCellCookie() {
                    var fqcCookie = $.cookie(fqcCookieName);
                    var fqcLoginObj = $.parseJSON(fqcCookie);
                    return fqcLoginObj;
                }

                //If po is null will remove key from cookie
                function addpoInCellCookie(po) {
                    var fqcLoginObj = getCellCookie();
                    fqcLoginObj.po = po;
                    fqcLoginObj.type = (po == null ? null : $("#type").val());
                    $.cookie(fqcCookieName, JSON.stringify(fqcLoginObj), {expires: getExpireDate()});
                }

                //generate all cookies exist 12 hours
                function generateCookie(name, value) {
                    $.cookie(name, value, {expires: getExpireDate()});
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

                function getExpireDate() {
                    var d = moment().set({hour: 23, minute: 0, second: 0});
                    return d.toDate();
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
                <h1>FQC 效率量測</h1>
            </div>

            <div class="form-group form-inline">
                <label>Line login</label>
                <select id="lineId">
                    <option value="-1">---請選擇線別---</option>
                </select>
                <input type="button" id="login" value="Login" />
                <input type="button" id="logout" value="Logout" />
            </div>

            <div id="startSchedArea">
                <div class="form-group form-inline">
                    <label>Processing</label>
                    <input type="text" id="po" placeholder="Please insert your po" />
                    <input type="text" name="modelname" id="modelname" placeholder="機種" readonly style="background: #CCC; width: 180px" />
                    <select id="type">
                        <option value="BAB">BAB</option>
                        <option value="TEST">Test</option>
                        <option value="PKG">Packing</option>
                    </select>
                </div>

                <div class="form-group form-inline">
                    <div class="stopwatch" id="stopwatch">
                        <button class="btn" id="stopwatch-start"><span class="glyphicon glyphicon-play" />Start</button>
                        <button class="btn" id="stopwatch-stop"><span class="glyphicon glyphicon-stop" />Stop</button>
                        <button class="btn" id="stopwatch-reset"><span class="glyphicon glyphicon-repeat" />Reset</button>
                        <button class="btn btn-success" id="start-productivity-count">開始計算效率</button>
                        <button class="btn btn-danger" id="stop-productivity-count">停止計算效率</button>
                        <div>
                            <label for="time-container">時間: </label>
                            <div id="time-container" class="container">00 : 00 : 00</div>
                        </div>
                    </div>
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
