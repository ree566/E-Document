<%-- 
    Document   : babpage1
    Created on : 2016/8/1, 上午 08:58:43
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="lineDAO" class="com.advantech.model.LineDAO" scope="application" />
<!DOCTYPE html>
<html>
    <c:set var="userSitefloor" value="${param.sitefloor}" />
    <c:if test="${(userSitefloor == null) || (userSitefloor == '' || userSitefloor < 1 || userSitefloor > 7)}">
        <c:redirect url="/" />
    </c:if>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>組包裝 ${userSitefloor} 樓 - ${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="images/favicon.ico"/>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.0/themes/base/jquery-ui.css">
        <style>
            #titleAlert{
                background-color: green;
                color: white;
                text-align: center;
            }
            .step{
                float:left; 
                width:100%; 
                /*border-bottom-style:dotted;*/
                border-left: solid 2px;
                border-bottom: solid 2px;
                border-right: solid 2px;
            }
            #step1{
                background-color: rosybrown;
            }
            #step2{
                background-color: bisque;
            }
            #step3{
                background-color: peachpuff;
            }
            #step4{
                background-color: gainsboro;
            }
            .userWiget{
                float:left; 
                width:60%; 
                padding: 10px 10px;
            }
            .wigetInfo{
                float:right;
                width:40%;
            }
            .userWiget > .alarm{
                padding-top: 5px;
                color: red;
                padding-left: 40%;
            }
            .stepAlarm{
                border-color: red;
            }
            li{
                line-height: 20px;
            }
            .importantMsg{
                color: red;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="//code.jquery.com/ui/1.12.0/jquery-ui.js"></script>
        <script src="js/jquery-ui-1.10.0.custom.min.js"></script>
        <script src="js/jquery.blockUI.js"></script>
        <script src="js/jquery.cookie.js"></script>
        <script src="js/param.check.js"></script>
        <script>
            var hnd;//鍵盤輸入間隔
            var hnd2;//鍵盤輸入間隔
            var serverErrorConnMessage = "Error, the textbox can't connect to server now.";
            var userNotFoundMessage = "使用者不存在，請重新確認，如有問題請通知系統管理人員。",
                    paramNotVaildMessage = "輸入資料有誤，請重新再確認。";

            var userInfoCookieName = "userInfo", babInfoCookieName = "babInfo", testLineTypeCookieName = "testLineTypeCookieName";
            var STATION_LOGIN = "LOGIN", STATION_LOGOUT = "LOGOUT";
            var BAB_END = "BAB_END";

            var firstStation = 1;
            var otherStationSearchResult;

            var tabreg = /^[0-9a-zA-Z-]+$/;//Textbox check regex.

            $(function () {
                $(document).ajaxSend(function () {
                    console.log("block");
                    block();//Block the screen when ajax is sending, Prevent form submit repeatly.
                });
                $(document).ajaxSuccess(function () {
                    console.log("unblock");
                    $.unblockUI();//Unblock the ajax when success
                });

                $("select, input").addClass("form-control");
                $("#reSearch").hide();
                $("#people").hide();

                var userInfoCookie = $.cookie(userInfoCookieName);
                var babInfoCookie = $.cookie(babInfoCookieName);
                var isUserInfoExist = (userInfoCookie != null);
                var isBabInfoExist = (babInfoCookie != null);

                console.log(userInfoCookie);
                console.log(babInfoCookie);

                $(".userWiget > .alarm").hide();

                var dialogMessage = $("#dialog-message").dialog({
                    autoOpen: false,
                    resizable: false,
                    height: "auto",
                    width: 400,
                    modal: true,
                    buttons: {
                        "確定": function () {
                            var newJobnumber = $("#newJobnumber").val();
                            if (!checkVal(newJobnumber) || !checkUserExist(newJobnumber)) {
                                showMsg(userNotFoundMessage);
                                $("#newJobnumber").val("");
                                $(this).dialog("close");
                                return false;
                            } else {
                                changeJobnumber(newJobnumber);
                                $(this).dialog("close");
                                if ($("#station").val() == firstStation) {
                                    startBab();
                                }else{
                                    reload();
                                }
                            }
                        },
                        "取消": function () {
                            $(this).dialog("close");
//                            $("#dialog").dialog("open");
                        }
                    }
                });

                var dialog = $("#dialog").dialog({
                    autoOpen: false,
                    resizable: false,
                    height: "auto",
                    width: 400,
                    modal: true,
                    buttons: {
                        "是": function () {
                            startBab();
                            $(this).dialog("close");
                        },
                        "否": function () {
                            $(this).dialog("close");
                            $("#dialog-message").dialog("open");
                        }
                    }
                });

                $("#directlyClose").click(function () {
                    if (confirm("※強制跳出並不會做資料儲存※\n確定跳出?")) {
                        if ($.cookie(babInfoCookieName) != null) {
                            $.removeCookie(babInfoCookieName);
                            reload();
                        } else {
                            showMsg("沒有步驟二的資料");
                        }
                    }
                });

                //Don't do the code after this check when user cookie info is not vaild.
                var cookieCheckStatus = checkExistCookies();
                if (cookieCheckStatus == false) {
                    return false;
                }

                changeInputPOAction();

                initUserInputWiget();

                if (isUserInfoExist && isBabInfoExist) {
                    console.log($.extend($.parseJSON(userInfoCookie), $.parseJSON(babInfoCookie)));
                }

                $("#step1").find(":text,select").attr("disabled", isUserInfoExist);

                $("#saveInfo").attr("disabled", isUserInfoExist);
                $("#clearInfo").attr("disabled", !isUserInfoExist);

                $("#babBegin, #clearInfo, .userWiget>div>input:eq(0)").attr("disabled", isBabInfoExist && ($.parseJSON(userInfoCookie).station != firstStation));
                $("#babEnd").attr("disabled", !isBabInfoExist);
                $("#changeUser").attr("disabled", isBabInfoExist);

                $(":text").keyup(function () {
                    textBoxToUpperCase($(this));
                });

                $(document).on("keyup", "#po", function () {
                    getModel($(this).val(), $(this).next());
                });

                //從已經從站別1儲存的工單資料中尋找相關資訊(LS_BAB table)
                $(document).on("keyup", "#po1", function () {
                    getBAB($(this).val(), $("#lineNo").val());
                });

                //Re搜尋工單
                $("#reSearch").click(function () {
                    $("#step2").block({message: "Please wait..."});
                    $(this).next().trigger("keyup");
                    setTimeout(function () {
                        $("#step2").unblock();
                    }, 2000);
                });

                //儲存使用者資訊
                $("#saveInfo").click(function () {
                    var lineNo = $("#lineNo option:selected").text();
                    var jobnumber = $("#jobnumber").val();
                    var station = $("#station").val();

                    if (confirm("確定您所填入的資料無誤?\n" + "線別:" + lineNo + "\n工號:" + jobnumber + "\n站別:" + station)) {
                        saveUserStatus();
                    }
                });

                //重設使用者資訊
                $("#clearInfo").click(function () {
                    if (confirm("確定離開此站別?")) {
                        if (!isUserInfoExist) {
//                            showMsg("步驟1 cookie不存在，無法登出，請聯絡系統管理員。");
                            return false;
                        }

                        if ($("#station").val() == firstStation) {

                            var obj = $.parseJSON(userInfoCookie);
                            console.log(obj);
                            firstStationLogin(obj, STATION_LOGOUT);
                        } else {
                            //Just remove the cookie.
                            removeAllStepCookie();
                            reload();
                        }
                    }
                });

                //操作工單登入
                $("#babBegin").click(function () {
                    if (isBabInfoExist) {
                        dialog.dialog("open");
                    } else {
                        var station = $("#station").val();
                        var isFirstStation = station == firstStation;
                        var po = $(isFirstStation ? "#po" : "#po1").val();
                        var modelname = $("#modelname").val();
                        var people = $("#people").val();
                        if (confirm("確定開始工單?\n" + "工單:" + po + "\n機種:" + modelname + (isFirstStation ? ("\n人數:" + people) : ("\n站別:" + station)))) {
                            startBab();
                        }
                    }
                });

                //操作工單登出
                $("#babEnd").click(function () {
                    var userInfo = $.parseJSON(userInfoCookie);
                    var babInfo = $.parseJSON(babInfoCookie);
                    if (confirm("站別 " + userInfo.station + " 確定儲存?")) {
                        var data = {
                            babId: babInfo.babId,
                            station: userInfo.station,
                            jobnumber: userInfo.jobnumber
                        };
                        if (userInfo.station != firstStation) {
                            data.action = BAB_END;
                            otherStation(data);
                        }
                    }
                });

                $("#changeUser").click(function () {
                    dialogMessage.dialog("open");
                });

                $(":text").focus(function () {
                    $(this).select();
                });

            });

            function block() {
                $.blockUI({
                    css: {
                        border: 'none',
                        padding: '15px',
                        backgroundColor: '#000',
                        '-webkit-border-radius': '10px',
                        '-moz-border-radius': '10px',
                        opacity: .5,
                        color: '#fff'
                    },
                    fadeIn: 0
                    , overlayCSS: {
                        backgroundColor: '#FFFFFF',
                        opacity: .3
                    }
                });
            }

            //extra functions
            function checkExistCookies() {
                var testLineTypeCookie = $.cookie(testLineTypeCookieName);
                var babLineTypeCookie = $.cookie(userInfoCookieName);

                if (testLineTypeCookie != null) {
                    lockAllUserInput();
                    showMsg("您已經登入測試");
                    return false;
                }

                if (babLineTypeCookie != null) {
                    var cookieMsg = $.parseJSON(babLineTypeCookie);
                    if (cookieMsg.floor != null && cookieMsg.floor != $("#userSitefloorSelect").val()) {
                        lockAllUserInput();
                        showMsg("您已經登入其他樓層");
                        return false;
                    }
                }
                return true;
            }

            function initUserInputWiget() {
                var userInfoCookie = $.cookie(userInfoCookieName);
                var babInfoCookie = $.cookie(babInfoCookieName);

                if (userInfoCookie != null) {
                    var obj = $.parseJSON(userInfoCookie);
                    $("#lineNo").val(obj.lineNo);
                    $("#jobnumber").val(obj.jobnumber);
                    $("#station").val(obj.station);
                    $("#step2").unblock();

                    if (obj.station == firstStation) {
                        $("#babEnd, .userWiget > .station1HintMessage, #changeUser").hide();
                        $("#step2Hint")
                                .append("<li>輸入工單</li>")
                                .append("<li class='importantMsg'>確定系統有帶出機種</li>")
                                .append("<li>選擇人數</li>")
                                .append("<li>點選<code>Begin</code>開始投入</li>");
                    } else {
                        $("#babEnd").show();
                        $("#step2Hint")
                                .append("<li>輸入工單</li>")
                                .append("<li class='importantMsg'>確定系統有帶出機種</li>")
                                .append("<li>點選<code>Begin</code>開始</li>")
                                .append("<li>做完最後一台時點擊<code>Save</code>，告知系統您已經做完了</li>")
                                .append("<li>如果要更換使用者，請點選<code>換人</code>，填入您的新工號之後進行工號切換</li>");
                    }
                } else {
                    $("#step2").block({message: "請先在步驟一完成相關步驟。"});
                }

                if (babInfoCookie != null) {
                    var obj = $.parseJSON(babInfoCookie);
                    $("#modelname").val(obj.modelname);
                    $("#modelname").prev().val(obj.po);
                    $(".userWiget > .station1HintMessage").show();

                    if ($("#people").is(":visible")) {
                        $("#people").val(obj.people);
                    }
                    showInfo("工單: " + obj.po + " | 機種 : " + obj.modelname + " | 人數 : " + obj.people);
                    showMsg("資料已經儲存");
                } else {
                    showInfo("尚無資料");
                }
            }

            function lockAllUserInput() {
                $(":input,select").not("#redirectBtn, #directlyClose").attr("disabled", "disabled");
            }

            function saveUserStatus() {
                var userInfo = {
                    lineNo: $("#lineNo").val(),
                    jobnumber: $("#jobnumber").val(),
                    station: $("#station").val()
                };

                if (checkVal(userInfo.lineNo, userInfo.jobnumber, userInfo.station) == false || !userInfo.jobnumber.match(tabreg)) {
                    showMsg(paramNotVaildMessage);
                    return false;
                } else {
                    console.log("input val checking pass");
                }

                if (!checkUserExist(userInfo.jobnumber)) {
                    showMsg(userNotFoundMessage);
                    return false;
                } else {
                    console.log("jobnumber checking pass");
                }

                saveUserInfoToCookie(userInfo);
            }

            //步驟一儲存使用者資訊
            function saveUserInfoToCookie(userInfo) {
                userInfo.floor = $("#userSitefloorSelect").val();

                if (userInfo.station == firstStation) {
                    firstStationLogin(userInfo, STATION_LOGIN);
                } else {
                    generateCookie(userInfoCookieName, JSON.stringify(userInfo));
                    reload();
                }
            }

            //使用者換人時，把cookievaule做更新
            function changeJobnumber(newJobnumber) {
                var cookieInfo = $.parseJSON($.cookie(userInfoCookieName));
                $.removeCookie(userInfoCookieName);
                cookieInfo.jobnumber = newJobnumber;
                generateCookie(userInfoCookieName, JSON.stringify(cookieInfo));
            }

            //看使用者是否存在
            function checkUserExist(jobnumber) {
                var result;
                $.ajax({
                    type: "Post",
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

            //當站別不同，搜尋的東西也不同
            function changeInputPOAction() {
                var userInfo = $.cookie("userInfo");
                var isFirstStation;
                if (userInfo != null) {
                    var obj = $.parseJSON(userInfo);
                    isFirstStation = (obj.station == 1);
                } else {
                    isFirstStation = ($("#station").val() == 1);
                }

                $("#modelname").prev("input").attr("id", isFirstStation ? "po" : "po1");
                isFirstStation ? $("#people").show() : $("#people").hide(); //when station 1 login success, check the station and show people selection
            }

            //站別一取得機種
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

            //取得第一站投入的工單(後面站別)
            function getBAB(text, saveline) {
                var reg = "^[0-9a-zA-Z]+$";
                if (text == "" || !text.match(reg)) {
                    $("#modelname").val("data not found");
                    return false;
                }
                window.clearTimeout(hnd2);
                hnd2 = window.setTimeout(function () {
                    $.ajax({
                        type: "Post",
                        url: "BabSearch",
                        async: false,
                        data: {
                            po_getBAB: text,
                            po_saveline: saveline
                        },
                        dataType: "html",
                        success: function (response) {
                            var obj = JSON.parse(response);
                            if (obj == null) {
                                showMsg("找不到工單資料");
                                $("#modelname").val("data not found");
                            } else {
                                showMsg("找到資料");
                                otherStationSearchResult = obj;
                                $("#modelname").val(obj.Model_name);
                            }
                            $("#reSearch").show();
                        },
                        error: function () {
                            showMsg(serverErrorConnMessage);
                        }
                    });
                }, 1000);
            }

            //第一站綁定線別開啟關閉，保持唯一一個能投入工單的電腦
            function firstStationLogin(data, action) {
                data.action = action;
                $.ajax({
                    type: "Post",
                    url: "LineServlet",
                    data: data,
                    dataType: "html",
                    success: function (response) {
                        //傳回來 success or fail
                        if (response == "success") {
                            if (action == STATION_LOGIN) {
                                generateCookie(userInfoCookieName, JSON.stringify(data));
                            } else {
                                removeAllStepCookie();
                            }
                            reload();
                        } else {
                            showMsg(response);
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showMsg(xhr.responseText);
                    }
                });
            }

            //
            function startBab() {
//                    save the user info right here where servermessage return success information
                if ($.cookie(userInfoCookieName) == null) {
                    showMsg(userNotFoundMessage);
                    return false;
                }

                var station = $("#station").val();
                var isFirstStation = (station == firstStation);

                var data = {
                    po: $(isFirstStation ? "#po" : "#po1").val(),
                    modelname: $("#modelname").val()
                };

                console.log(data);

                if (!checkVal(data.po, data.modelname) || data.modelname == "data not found") {
                    showMsg(paramNotVaildMessage);
                    return false;
                }

                if (isFirstStation) {
                    var people = $("#people").val();
                    if (!checkVal(people)) {
                        showMsg(paramNotVaildMessage);
                        return false;
                    } else {
                        data.people = people;
                    }
                    saveBabInfo(data);
                    console.log("First station input new babs.");
                } else {
//                    if (station > otherStationSearchResult.people) {
//                        showMsg("您的站別大於第一站工單投入所輸入的人數 " + otherStationSearchResult.people + " ，請重新確認。");
//                        return false;
//                    } else {
                    var cookieInfo = JSON.parse($.cookie(userInfoCookieName));

                    data.station = cookieInfo.station;
                    data.jobnumber = cookieInfo.jobnumber;
                    data.action = STATION_LOGIN;

                    data.babId = otherStationSearchResult.id;
                    data.people = otherStationSearchResult.people;

                    otherStation(data);
                    showMsg("Begin save.");
//                    }
                    console.log("Other station user login.");
                }
            }

            //站別一對資料庫操作
            function saveBabInfo(data) {
                var totalUserInfo = $.extend($.parseJSON($.cookie(userInfoCookieName)), data);
                $.ajax({
                    type: "Post",
                    url: "BABFirstStationServlet",
                    data: totalUserInfo,
                    dataType: "html",
                    success: function (response) {
                        if (response == "success") {
                            addValueToBabCookie(data);
                        } else {
                            showMsg(response);
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showMsg(xhr.responseText);
                    }
                });
            }

            //其他站別動作
            function otherStation(data) {

                $.ajax({
                    type: "Post",
                    url: "BABOtherStationServlet",
                    data: data,
                    dataType: "html",
                    success: function (response) {
                        if (response == "success") {
                            if (data.action == STATION_LOGIN) {
                                var savingData = {
                                    po: data.po,
                                    modelname: data.modelname,
                                    babId: data.babId,
                                    people: data.people
                                };
                                generateCookie(babInfoCookieName, JSON.stringify(savingData));
                            } else {
                                removeCookie(babInfoCookieName);
                            }
                            showMsg(response);
                            reload();
                        } else {
                            showMsg(response);
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showMsg(xhr.responseText);
                    }
                });
            }

            function otherStationUserLogin() {
                //find data from step1 cookie(check jobnumber vaild before cookie add already)
                //find the data find by
            }

            //data save to cookie(步驟二)
            function addValueToBabCookie(data) {
                generateCookie(babInfoCookieName, JSON.stringify(data));
                reload();
            }

            //auto uppercase the textbox value(PO, ModelName)
            function textBoxToUpperCase(obj) {
                obj.val(obj.val().trim().toLocaleUpperCase());
            }

            //generate all cookies exist 12 hours
            function generateCookie(name, value) {
                var date = new Date();
                var minutes = 12 * 60;
                date.setTime(date.getTime() + (minutes * 60 * 1000));
                $.cookie(name, value, {expires: date});
            }

            function removeAllStepCookie() {
                removeCookie(userInfoCookieName);
                removeCookie(babInfoCookieName);
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
            }

            function showInfo(msg) {
                $("#processingBab").html(msg);
            }
        </script>
    </head>
    <body>
        <input id="userSitefloorSelect" type="hidden" value="${userSitefloor}">
        <div id="titleAlert">
            <c:out value="您所選擇的樓層是: ${userSitefloor}" />
            <a href="${pageContext.request.contextPath}">
                <button id="redirectBtn" class="btn btn-default" >不是我的樓層?</button>
            </a>
        </div>
        <jsp:include page="temp/head.jsp" />
        <script>
            var btn = $.fn.button.noConflict(); // reverts $.fn.button to jqueryui btn
            $.fn.btn = btn; // assigns bootstrap button functionality to $.fn.btn 
        </script>

        <!--Dialogs-->
        <div id="dialog" title="${initParam.pageTitle}">
            <p>您正要切換到下一筆工單，請問是否是相同人在作業?</p>
        </div>

        <div id="dialog-message" title="${initParam.pageTitle}">
            <p>
                <span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
                請再次輸入您的工號。
            </p>
            <p>
                <input type="text" id="newJobnumber">
            </p>
        </div>
        <!--Dialogs-->

        <!--Contents-->
        <div class="container">
            <!--<button id="changeDiv" class="btn btn-default">changeDiv</button>-->
            <!--<button id="clearAllCookie" class="btn btn-default">DirectClear</button>-->

            <div id="step1" class="step">
                <div class="userWiget form-inline">
                    <select id="lineNo">
                        <option value="-1">---請選擇線別---</option>
                        <c:forEach var="lines" items="${lineDAO.getLine(userSitefloor)}">
                            <option value="${lines.id}" ${lines.lock == 1 ? "disabled style='opacity:0.2'" : ""}>${lines.name}</option>
                        </c:forEach>
                    </select>
                    <input type="text" id="jobnumber" placeholder="請輸入工號" autocomplete="off" maxlength="10"/>
                    <select id='station'>
                        <option value="-1">---請選擇站別---</option>
                        <c:forEach var="station" begin="1" end="4">
                            <option value="${station}">第 ${station} 站</option>
                        </c:forEach>
                    </select>
                    <input type="button" id="saveInfo" value="Begin" />
                    <input type="button" id="clearInfo" value="End" />
                </div>
                <div class="wigetInfo">
                    <h3>步驟1:</h3>
                    <h5>請依序填入您的相關資訊。</h5>
                </div>
            </div>

            <div id="step2" class="step">
                <div class="userWiget">
                    <div class="form-inline">
                        <button class='btn btn-default' id='reSearch'><span class="glyphicon glyphicon-repeat"></span></button>
                        <input type="text" name="po" id="po" placeholder="請輸入工單號碼" autocomplete="off" maxlength="50">  
                        <input type="text" name="modelname" id="modelname" placeholder="機種" readonly style="background: #CCC">
                        <select id='people'>
                            <option value="-1">---請選擇人數---</option>
                            <c:forEach var="people" begin="2" end="4">
                                <option value="${people}">${people}</option>
                            </c:forEach>
                        </select>
                        <input type="button" id="babBegin" value="Begin" />
                        <input type="button" id="babEnd" value="Save" />
                        <input type="button" id="changeUser" value="換人" />
                    </div>
                    <div class="station1HintMessage alarm">
                        <span class="glyphicon glyphicon-alert"></span>
                        做完時請記得做儲存動作
                        <span class="glyphicon glyphicon-arrow-up"></span>
                    </div>
                    <div style="text-align:right; padding-top: 40px">
                        <button id="directlyClose" class="btn btn-danger">
                            <span class="glyphicon glyphicon-alert"></span>
                            強制跳出
                        </button>
                    </div>
                </div>
                <div class="wigetInfo">
                    <h3>步驟2:</h3>
                    <h5>請依照下列流程操作。</h5>
                    <h5>
                        <ol id="step2Hint">
                        </ol>
                    </h5>
                </div>
            </div>

            <div id="step3" class="step">
                <div id='serverMsg' class="userWiget">        
                </div>
                <div class="wigetInfo">
                    <h3>步驟3:觀看伺服器訊息。</h3>
                    <h5>此處會顯示伺服器訊息。</h5>
                </div>
            </div>

            <div id="step4" class="step stepAlarm">
                <div id="processingBab" class="userWiget"></div>
                <div class="wigetInfo">
                    <h3>完成:</h3>
                    <h5>此處會顯示您正在進行的工單。</h5>
                </div>
            </div>

            <div id="hintmsg" style="color:red;font-weight: bold;padding-left: 10px">
                <p>※第一站人員請先Key入相關資料再把機子放到定位(否則會少一台紀錄)</p>
                <p>機子擋住Sensor即開始計時，休息時間的操作不列入計算範圍之內。</p>
            </div>
        </div>
        <jsp:include page="temp/footer.jsp" />
    </body>
</html>
