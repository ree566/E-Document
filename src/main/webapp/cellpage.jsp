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
        <script src="js/moment.js"></script>
        <script>
            var userInfoCookieName = "userInfo", testLineTypeCookieName = "testLineTypeCookieName", cellCookieName = "cellCookieName";
            var serverMsgTimeout;
            var hnd;//鍵盤輸入間隔

            $(function () {
                //Add class to transform the button type to bootstrap.
                $(":button").addClass("btn btn-default");
                $("#refresh").addClass("btn-xs");
                $(":text,select,input[type='number']").addClass("form-control");

                $(":text").each(resizeInput);

                if (checkExistCookies() == false) {
                    return false;
                }

                var cellCookie = $.cookie(cellCookieName);
                var cellLoginObj;
                if (cellCookie != null) {
                    var cellLoginObj = $.parseJSON(cellCookie);

                    getLineStatus(cellLoginObj.lineId);

                    if (cellLoginObj.floor != $("#userSitefloorSelect").val()) {
                        lockAllUserInput();
                        showMsg("您已經登入其他樓層");
                        return false;
                    }

                    $("#lineId").val(cellLoginObj.lineId).attr("disabled", true);
                    $("#login, #lineId").attr("disabled", true);
                    $("#logout").attr("disabled", false);

                    poDetect();

                    if (cellLoginObj.PO != null) {
                        $("#PO").val(cellLoginObj.PO);
                        $("#begin, #PO").attr("disabled", true).unbind("click");
                        $("#end").removeAttr("disabled");
                    } else {
                        $("#end").attr("disabled", true).unbind("click");
                        $("#begin, #PO").removeAttr("disabled");
                    }
                    $("#startSchedArea").unblock();
                    showProcessing();
                } else {
                    $("#login, #lineId").attr("disabled", false);
                    $("#logout").attr("disabled", true);
                    $("#startSchedArea").block({message: "請先登入線別。", css: {cursor: 'default'}, overlayCSS: {cursor: 'default'}});
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
                        cellLineSwitch(obj);
                    }
                });

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

                $("#begin").click(function () {
                    var lineId = $("#lineId").val();
                    var PO = $("#PO").val();
                    var modelname = $("#modelname").val();

                    if (checkVal(lineId, PO, modelname) == false || modelname == "data not found") {
                        showMsg("輸入資料有誤，請重新再確認。");
                        return false;
                    }

                    if (confirm("Begin PO: " + PO + " on line " + $("#lineId option:selected").text().trim() + " ?")) {
                        insertCellInfo({
                            lineId: lineId,
                            PO: PO,
                            modelname: modelname
                        });
                    }
                });

                $("#end").click(function () {
                    if (confirm("End this PO?")) {
                        deleteSchedJob();
                    }
                });

                $("#refresh").click(function () {
                    showProcessing();
                    poDetect();
                });

                $("#PO").on("keyup", function () {
                    getModel($(this).val(), $("#modelname"));
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

                function getLineStatus(lineId) {
                    $.ajax({
                        type: "Post",
                        url: "CellLineServlet",
                        data: {
                            action: "select",
                            lineId: lineId
                        },
                        dataType: "html",
                        success: function (response) {
                            var obj = $.parseJSON(response);
                            console.log(obj);
                            if (obj.isused == 0) {
                                removeCookie(cellCookieName);
                                reload();
                            }
                        },
                        error: function () {
                            showMsg("error");
                        }
                    });
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
                                $("#PO").val("");
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
                            var cellInfo = response.data;
                            var cellLoginObj = getCellCookie();
                            if (cellInfo != null && cellInfo.length != 0 && cellLoginObj.PO == null) {
                                var cell = cellInfo[0]; //always get the first input cell info
                                addPOInCellCookie(cell.PO);
                                reload();
                            } else if ((cellInfo == null || cellInfo.length == 0)) {
                                var PO = cellLoginObj.PO;
                                if (PO != null) {
                                    addPOInCellCookie(null);
                                }
//                                deleteSchedJob();
                                $("#PO").val("");
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
                                type: "Post",
                                url: "BabSearch",
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
                    data.action = "insert";
                    $.ajax({
                        type: "Post",
                        url: "CellScheduleJobServlet",
                        data: data,
                        dataType: "html",
                        success: function (response) {
                            showMsg(response);
                            if (response == "success") {
                                addPOInCellCookie($("#PO").val());
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
                        url: "CellScheduleJobServlet",
                        data: {
                            lineId: $("#lineId").val(),
                            PO: $("#PO").val(),
                            action: "delete"
                        },
                        dataType: "html",
                        success: function (response) {
                            showMsg(response);
                            showProcessing();
                            if (response == "success") {
                                addPOInCellCookie(null);
                                reload();
                            }
                        },
                        error: function () {
                            showMsg("error");
                        }
                    });
                }

                function showProcessing() {
                    $("#processingArea").html("");
                    $.ajax({
                        type: "Post",
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

                function getCellCookie() {
                    var cellCookie = $.cookie(cellCookieName);
                    var cellLoginObj = $.parseJSON(cellCookie);
                    return cellLoginObj;
                }

                //If po is null will remove key from cookie
                function addPOInCellCookie(PO) {
                    var cellLoginObj = getCellCookie();
                    cellLoginObj.PO = PO;
                    $.cookie(cellCookieName, JSON.stringify(cellLoginObj), {expires: getExpireDate()});
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
                    var date = moment().startOf('day');
                    date = date.add(2, 'days');
                    return date.toDate();
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
                    <label>Processing</label>
                    <input type="text" id="PO" placeholder="Please insert your PO" />
                    <input type="text" name="modelname" id="modelname" placeholder="機種" readonly style="background: #CCC; width: 180px" />
                    <input type="button" id="begin" value="Begin" />
                    <input type="button" id="end" value="End" />
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
