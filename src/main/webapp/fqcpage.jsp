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
            .timer-container{
                border-style: solid;
                border-color: green;
                padding: 15px;
                background-color: lightgreen;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/js/jquery.cookie.js" /> "></script>
        <script src="<c:url value="/webjars/jquery-blockui/2.70/jquery.blockUI.js" /> "></script>
        <script src="<c:url value="/js/jquery.blockUI.Default.js" /> "></script>
        <script src="<c:url value="/js/cookie.check.js" /> "></script>
        <script src="<c:url value="/js/param.check.js" /> "></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>

        <!--http://jquerytimer.com/-->
        <script src="<c:url value="/js/timer.jquery.min.js" /> "></script>
        <script src="<c:url value="/js/timer.jquery.event.js" /> "></script>

        <script>
            var serverErrorConnMessage = "Error, the textbox can't connect to server now.";

            var userInfoCookieName = "userInfo", //組包步驟一的cookie
                    testLineTypeCookieName = "testLineTypeCookieName", //測試線別的cookie 
                    fqcCookieName = "fqcCookieName";
            var serverMsgTimeout;
            var hnd;//鍵盤輸入間隔
            var timerCount = 0;
            var maxTimerCount = 10;
            var fqcLoginObj;
            var lineArrTemp = [];

            $(function () {

                $.getJSON('<c:url value="/json/fqcRemarkOptions.json" />', function (data) {
                    initFqcRemarkTemplate(data.fqcRemarkTemplate);
                });

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
                        var line = lines[i];
                        setLineOptions(line);
                        lineArrTemp[line.id] = line;
                    }
                }

                fqcFormInit();

                $("#login").click(function () {
                    var lineId = $("#lineId").val();
                    var lineName = $("#lineId option:selected").text().trim();
                    var jobnumber = $("#jobnumber").val().trim();

                    if (checkVal(lineId, jobnumber) == false) {
                        showMsg("Please check you input value.");
                        return false;
                    }

                    if (!checkUserExist(jobnumber)) {
                        showMsg("User is not exist.");
                        return false;
                    }

                    if (confirm("Login " + lineName + " ?")) {
                        var obj = {
                            "fqcLine.id": lineId,
                            lineName: lineName,
                            jobnumber: jobnumber,
                            floor: $("#userSitefloorSelect").val(),
                            action: "LOGIN",
                            factory: lineArrTemp[lineId].factory
                        };
                        fqcLineSwitch(obj);
                    }
                });

                $("#logout").click(function () {
                    fqcLoginObj.action = "LOGOUT";
                    if (fqcLoginObj != null) {
                        if (confirm("Logout " + fqcLoginObj.lineName.trim() + " ?")) {
                            fqcLineSwitch(fqcLoginObj);
                        }
                    } else {
                        showMsg("Can't find your login status");
                    }
                });

                $(".po").on("keyup", function () {
                    var obj = $(this);
                    var modelTextBox = obj.parents(".timer-container").find(".modelName");
                    getModel(obj.val().toUpperCase(), modelTextBox);
                });

                var timerElement = $(".timer-container:first");
                timerElement.hide();

                $("#timer-add").click(function () {
                    if (timerCount >= maxTimerCount) {
                        alert("Reach max timer count " + maxTimerCount + " !!");
                        return false;
                    }
                    var cloneTimer = timerElement.clone(true);
                    timerInit(cloneTimer);
                    cloneTimer.insertAfter("div.timer-container:last");
                    cloneTimer.show();
                    timerCount++;
                });

                $("body").on("click", ".timer-destroy", function () {
                    if (confirm("Remove timer?")) {
                        $(this).parents(".timer-container").remove();
                        timerCount--;
                    }
                });

                $(".start-timer-btn").on("click", function (e) {
                    var container = $(this).parents(".timer-container");
                    var po = container.find(".po").val();
                    var modelName = container.find(".modelName").val();
                    var firstPcsTimeCost = container.find(".timer").data('seconds');

                    if (checkVal(po, modelName) == false || modelName == "data not found") {
                        alert("Po is not valid");
                        e.stopImmediatePropagation();
                        return false;
                    }

                    if (confirm("開始計算效率? 工單: " + po + " 機種: " + modelName)) {
                        var standardTime = findStandardTime(modelName);
                        if ((standardTime == null || standardTime.id == 0)) {
                            if (!confirm("找不到標工，確定投入?")) {
                                e.stopImmediatePropagation();
                                return false;
                            } else {
                                container.find(".fqc-remark").val("(無標工)");
                            }
                        }

                        var cookieInfo = $.parseJSON($.cookie(fqcCookieName));
                        var fqc = findReconnectable(cookieInfo["fqcLine.id"], po);
                        if (fqc.id == 0) {
                            saveFqcInfo({
                                po: po,
                                modelName: modelName
                            }, container, standardTime);
                        } else {
                            reconnectAbnormal(fqc, container);
                            e.stopImmediatePropagation();
                        }
                    } else {
                        e.stopImmediatePropagation();
                    }
                });


                $(".pause-timer-btn").on("click", function (e) {
                    var container = $(this).parents(".timer-container");
                    container.find(".fqc-status").html("計算時間中...(暫停)");
                    modePauseProductivity(container);
                    pauseTimeAndSaveTemp(container.find(".fqc-id").val(), container.find(".timer").data("seconds"));
                });

                $(".resume-timer-btn").on("click", function (e) {
                    var container = $(this).parents(".timer-container");
                    container.find(".fqc-status").html("計算時間中...");
                    modeStartProductivity(container);
                    resumeTime(container.find(".fqc-id").val());
                });

                $(".remove-timer-btn").on("click", function (e) {
                    var container = $(this).parents(".timer-container");
                    var fqc_id = container.find(".fqc-id").val();
                    if (checkVal(fqc_id) == false) {
                        alert("Close fqc id not found");
                        e.stopImmediatePropagation();
                        return false;
                    }
                    if (confirm("結束計算效率?")) {
                        var timeCost = container.find(".timer").data('seconds');
                        if ((timeCost == 0 && confirm("總作業時間為0，儲存?")) || timeCost != 0) {
                            var remark = container.find(".fqc-remark").val();
                            fqcComplete(fqc_id, timeCost, remark, container);
                        } else {
                            e.stopImmediatePropagation();
                        }
                    } else {
                        e.stopImmediatePropagation();
                    }
                });

                $(".fqc-abnormal-btn").on("click", function (e) {

                    var container = $(this).parents(".timer-container");
                    var fqc_id = container.find(".fqc-id").val();
                    var timeCost = container.find(".timer").data('seconds');
                    var remark = container.find(".fqc-remark").val();

                    if (confirm("確認異常?")) {
                        $.ajax({
                            type: "Post",
                            url: "FqcController/addAbnormalReconnectableSignAndClose",
                            data: {
                                "fqc.id": fqc_id,
                                "timeCost": timeCost,
                                "remark": remark
                            },
                            dataType: "html",
                            global: false,
                            success: function (response) {
                                alert(response);
                                if (response == "success") {
                                    modeStopProductivity(container);
                                    container.find(".fqc-status").removeClass("alert-success").html("");
                                    container.find(".po, .timer-destroy").prop("disabled", false);
                                    container.find(".po, .modelName, .fqc-remark").val("");
                                    container.find(".timer").timer('remove');
                                }
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                showMsg(xhr.responseText);
                            }
                        });
                    }
                });

                $(".upperText").on("keyup", function () {
                    var v = $(this).val().toUpperCase().trim();
                    $(this).val(v);
                });

                $("#timer-add").trigger("click").hide();
                $(".timer-destroy, .fqc-abnormal-btn").hide();
                $(".fqc-remark, .remark-template").attr("disabled", true);

                if (fqcLoginObj != null) {
                    initProcessingFqc(fqcLoginObj["fqcLine.id"]);
                }

                $(".remark-template").change(function () {
                    var sel = $(this);
                    var selVal = sel.val();
                    var container = sel.parents(".timer-container");
                    var remarkWiget = container.find(".fqc-remark");
                    if (selVal != -1) {
                        var selText = sel.children(':selected').text();
                        remarkWiget.val("").val(selText);
                    } else {
                        remarkWiget.val("");
                    }
                });

                function fqcFormInit() {
                    var fqcCookie = $.cookie(fqcCookieName);
                    if (fqcCookie != null) {
                        fqcLoginObj = $.parseJSON(fqcCookie);

                        if (fqcLoginObj.floor != $("#userSitefloorSelect").val()) {
                            lockAllUserInput();
                            showMsg("您已經登入其他樓層");
                            return false;
                        }

                        $("#lineId").val(fqcLoginObj["fqcLine.id"]).attr("disabled", true);
                        $("#jobnumber").val(fqcLoginObj.jobnumber).attr("disabled", true);
                        $("#login, #lineId").attr("disabled", true);
                        $("#logout").attr("disabled", false);

                        $("#startSchedArea").unblock();
                    } else {
                        $("#login, #lineId").attr("disabled", false);
                        $("#jobnumber").attr("disabled", false).val("");
                        $("#logout").attr("disabled", true);
                        $("#startSchedArea").block({message: "請先登入線別。", css: {cursor: 'default'}, overlayCSS: {cursor: 'default'}});
                    }
                }

                //站別一對資料庫操作
                function saveFqcInfo(data, container, standardTime) {
                    var totalUserInfo = $.extend($.parseJSON($.cookie(fqcCookieName)), data);
                    $.ajax({
                        type: "Post",
                        url: "FqcController/insert",
                        data: totalUserInfo,
                        dataType: "json",
                        success: function (response) {
                            showMsg("success");

                            var fqcObject = response;

                            modeStartProductivity(container);
                            container.find(".po, .timer-destroy").prop("disabled", true);
                            container.find(".fqc-status").toggleClass("alert-success")
                                    .addClass("alert-danger").html("計算時間中...");
                            container.find(".fqc-id").val(fqcObject.id);
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            showMsg(xhr.responseText);
                            container.find(".pause-timer-btn, .resume-timer-btn, .remove-timer-btn").hide();
                            container.find(".start-timer-btn").show();
                            container.find(".timer").timer('remove');
                        }
                    });
                }

                function reconnectAbnormal(fqc, container) {
                    $.ajax({
                        type: "Post",
                        url: "FqcController/reconnectAbnormal",
                        data: JSON.stringify(fqc),
                        contentType: "application/json",
                        dataType: "json",
                        success: function (response) {
                            var historyRecord = response;

                            var fqcObject = fqc;

                            modeStartProductivity(container);
                            container.find(".po, .timer-destroy").prop("disabled", true);
                            container.find(".fqc-status").toggleClass("alert-success")
                                    .addClass("alert-danger").html("計算時間中...");
                            container.find(".fqc-id").val(fqcObject.id);

                            var timerLastTime = historyRecord.timeCost;

                            alert("已經接續上次的紀錄(持續時間: " + timerLastTime + " 秒, 製作PCS: " + historyRecord.pcs + ")");

                            container.find(".timer").timer({
                                seconds: timerLastTime //Specify start time in seconds
                            });

                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            showMsg(xhr.responseText);
                            container.find(".pause-timer-btn, .resume-timer-btn, .remove-timer-btn").hide();
                            container.find(".start-timer-btn").show();
                            container.find(".timer").timer('remove');
                        }
                    });
                }

                function findReconnectable(fqcLine_id, po) {
                    var result;
                    $.ajax({
                        type: "GET",
                        url: "<c:url value="/FqcController/findReconnectable" />",
                        data: {
                            fqcLine_id: fqcLine_id,
                            po: po
                        },
                        dataType: "json",
                        async: false,
                        success: function (response) {
                            result = response;
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            showMsg(xhr.responseText);
                        }
                    });
                    return result;
                }

                function fqcComplete(fqc_id, timeCost, remark, container) {
                    $.ajax({
                        type: "Post",
                        url: "FqcController/stationComplete",
                        data: {
                            "fqc.id": fqc_id,
                            "timeCost": timeCost,
                            "remark": remark
                        },
                        dataType: "html",
                        global: false,
                        success: function (response) {
                            alert(response);
                            if (response == "success") {
                                container.find(".fqc-status").removeClass("alert-success").html("");
                                container.find(".po, .timer-destroy").prop("disabled", false);
                                container.find(".po, .modelName, .fqc-remark").val("");
                                modeStopProductivity(container);
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            showMsg(xhr.responseText);
                            container.find(".pause-timer-btn, .remove-timer-btn").show();
                            container.find(".start-timer-btn").hide();
                            container.find(".timer").timer({
                                seconds: timeCost
                            }).timer('pause');
                        }
                    });
                }

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
                    var result;
                    $.ajax({
                        type: "Get",
                        url: "FqcLineController/findAll",
                        data: {
                            sitefloor: $("#userSitefloorSelect").val()
                        },
                        dataType: "json",
                        async: false,
                        success: function (response) {
                            result = response;
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            showMsg(xhr.responseText);
                        }
                    });
                    return result;
                }

                function fqcLineSwitch(data) {
                    if (data.action == null) {
                        return false;
                    }
                    $.ajax({
                        type: "Post",
                        url: "FqcLoginController/" + data.action.toLowerCase(),
                        data: data,
                        dataType: "html",
                        success: function (response) {
                            if (response == "success") {
                                if (data.action == "LOGIN") {
                                    generateCookie(fqcCookieName, JSON.stringify(data));
                                    $("#lineId").attr("disabled", true);
                                    initProcessingFqc(data["fqcLine.id"]);
                                    fqcFormInit();
                                    $("#po").val("");
                                } else if (data.action == "LOGOUT") {
                                    removeCookie(fqcCookieName);
                                    reload();
                                }
                            } else {
                                showMsg(response);
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            showMsg(xhr.responseText);
                        }
                    });
                }

                function getModel(text, obj) {
                    var lineId = $("#lineId").val();
                    var reg = "^[0-9a-zA-Z]+$";
                    if (text != "" && text.match(reg)) {
                        window.clearTimeout(hnd);
                        hnd = window.setTimeout(function () {
                            $.ajax({
                                type: "GET",
                                url: "ModelController/findModelNameByPoAndFactory",
                                data: {
                                    po: text.trim(),
                                    factory: lineArrTemp[lineId].factory
                                },
                                dataType: "html",
                                success: function (response) {
                                    obj.val(response);
                                    $("#reSearch").show();
                                },
                                error: function (xhr, ajaxOptions, thrownError) {
                                    showMsg(xhr.responseText);
                                }
                            });
                        }, 1000);
                    } else {
                        obj.val("");
                    }
                }

                //看使用者是否存在
                function checkUserExist(jobnumber) {
                    var result;
                    $.ajax({
                        type: "GET",
                        url: "CheckUser",
                        data: {
                            jobnumber: jobnumber
                        },
                        dataType: "json",
                        async: false,
                        success: function (response) {
                            result = response;
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            showMsg(xhr.responseText);
                        }
                    });
                    return result;
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
                    alert(msg);
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

                function modeStartProductivity(container) {
                    container.find(".start-timer-btn, .resume-timer-btn").hide();
                    container.find(".pause-timer-btn").show();
                    container.find(".remove-timer-btn, .fqc-remark, .remark-template, .fqc-abnormal-btn").show().attr("disabled", true);
                }

                function modePauseProductivity(container) {
                    container.find(".start-timer-btn, .pause-timer-btn").hide();
                    container.find(".resume-timer-btn").show();
                    container.find(".remove-timer-btn, .fqc-abnormal-btn").show().removeAttr("disabled");
                    container.find(".fqc-remark, .remark-template").show().removeAttr("disabled");
                }

                function modeStopProductivity(container) {
                    container.find(".start-timer-btn").show();
                    container.find(".pause-timer-btn, .resume-timer-btn, .remove-timer-btn").hide();
                    container.find(".remove-timer-btn, .fqc-remark, .remark-template, .fqc-abnormal-btn").show().attr("disabled", true);
                }

                function initProcessingFqc(fqcLine_id) {
                    if (fqcLine_id == null) {
                        return false;
                    }
                    $.ajax({
                        type: "Get",
                        url: "FqcController/findProcessing",
                        data: {
                            "fqcLine.id": fqcLine_id
                        },
                        dataType: "json",
                        success: function (response) {
                            var i = 0;
                            if (response.length == 0) {
                                return false;
                            }

                            var timerTempArray = fqcLoginObj["_tempTimers"];
                            console.log(timerTempArray);

                            $(".timer-container:not(:first)").each(function () {
                                var fqcData = response[i++];
                                var container = $(this);
                                container.find(".fqc-id").val(fqcData.id);
                                container.find(".po").val(fqcData.po).attr("disabled", true);
                                container.find(".modelName").val(fqcData.modelName);

                                var tempSecond = 0;
                                if (timerTempArray != null) {
                                    var tempObject = timerTempArray[i];
                                    if (tempObject != null && 'timerPauseTemp' in tempObject) {
                                        tempSecond = tempObject.timerPauseTemp;
                                    }
                                }

                                container.find(".timer").timer({
                                    seconds: tempSecond //Specify start time in seconds
                                }).timer('pause');

                                modePauseProductivity(container);
                            });
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            showMsg(xhr.responseText);
                        }
                    });
                }

                function pauseTimeAndSaveTemp(fqc_id, timePeriod) {
                    if (fqc_id == null) {
                        return false;
                    }
                    $.ajax({
                        type: "Post",
                        url: "FqcController/pauseTimeAndSaveTemp",
                        data: {
                            "fqc.id": fqc_id,
                            "timePeriod": timePeriod
                        },
                        dataType: "html",
                        success: function (response) {
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert("Fail to save timePeriod into temp table.");
                            showMsg(xhr.responseText);
                        }
                    });
                }
                
                function resumeTime(fqc_id) {
                    if (fqc_id == null) {
                        return false;
                    }
                    $.ajax({
                        type: "Post",
                        url: "FqcController/resumeTime",
                        data: {
                            "fqc.id": fqc_id
                        },
                        dataType: "html",
                        success: function (response) {
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            alert("Fail to remove timePeriod into temp table.");
                            showMsg(xhr.responseText);
                        }
                    });
                }

                function findStandardTime(modelName) {
                    var result;
                    $.ajax({
                        type: "Post",
                        url: "FqcController/findStandardTime",
                        data: {
                            modelName: modelName
                        },
                        dataType: "json",
                        async: false,
                        success: function (response) {
                            result = response;
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            showMsg(xhr.responseText);
                        }
                    });
                    return result;
                }

                function initFqcRemarkTemplate(templateOptions) {
                    var target = $(".remark-template:first");
                    for (var i = 0; i < templateOptions.length; i++) {
                        var option = templateOptions[i];
                        target.append("<option value='" + option.id + "'>" + option.val + "</option>");
                    }
                }

            });

            $(window).bind('beforeunload', function () {
                //If po is null will remove key from cookie

                var fqcCookie = $.cookie(fqcCookieName);
                if (fqcCookie != null) {
                    var _tempTimers = [];
                    var i = 0;
                    $(".timer-container").each(function () {
                        var container = $(this);
                        var fqcId = container.find(".fqc-id").val();
                        var timer = container.find(".timer").data("seconds");
                        if (timer != 0 && fqcId != null) {
                            var _tempTimer = {
                                "fqc.id": fqcId,
                                "timerPauseTemp": timer
                            };
                            _tempTimers[i] = _tempTimer;
                        }
                        i++;
                    });

                    var fqcLoginObj = $.parseJSON(fqcCookie);
                    fqcLoginObj["_tempTimers"] = _tempTimers;

                    var d = moment().set({hour: 23, minute: 0, second: 0});
                    $.cookie(fqcCookieName, JSON.stringify(fqcLoginObj), {expires: d.toDate()});

                    return 'are you sure you want to leave?';
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
                <input type="text" id="jobnumber" class="upperText" placeholder="請輸入工號" style="width: 180px" />
                <input type="button" id="login" value="Login" />
                <input type="button" id="logout" value="Logout" />
            </div>

            <div id="startSchedArea">
                <div>
                    <div>
                        <input type="button" id="timer-add" class="btn btn-default" value="+" />
                    </div>
                    <div id="timer-area">
                        <div class="row timer-container">
                            <table class="table table-condensed">
                                <tr>
                                    <td>
                                        <input type="hidden" class="fqc-id">
                                        <input name="po" class="form-control po upperText" placeholder="Please insert your po" type="text">
                                    </td>
                                    <td>
                                        <input name="timer" class="form-control timer" placeholder="0 sec" type="text" readonly="">
                                    </td>
                                    <td>
                                        <button class="btn btn-success start-timer-btn">Start</button>
                                        <button class="btn btn-success resume-timer-btn">Resume</button>
                                        <button class="btn pause-timer-btn">Pause</button>
                                    </td>
                                    <td>
                                        <button class="btn btn-danger remove-timer-btn">儲存作業時間到資料庫</button>
                                    </td>
                                    <td rowspan="2">
                                        <textarea  class="form-control fqc-remark" placeholder="備註欄位"></textarea>
                                        <select class="remark-template">
                                            <option value="-1">使用範本</option>
                                        </select>
                                        <input type="button" class="btn btn-default timer-destroy" value="-" />
                                    </td>
                                    <td rowspan="2">
                                        <div class="fqc-status alert">This is some message.</div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="text" name="modelname" class="modelName" placeholder="機種" readonly style="background: #CCC; width: 180px" />
                                    </td>
                                    <td></td>
                                    <td></td>
                                    <td>
                                        <button class="btn btn-warning fqc-abnormal-btn">異常</button>
                                    </td>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label>Server message</label>
                    <div id="serverMsg"></div>
                </div>
            </div>
            <jsp:include page="temp/_debug.jsp" />
            <jsp:include page="temp/footer.jsp" />
    </body>
</html>
