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
    <c:if test="${(userSitefloor == null || !userSitefloor.matches('[0-9]+')) || (userSitefloor == '' || userSitefloor < 1 || userSitefloor > 7)}">
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
                padding-right: 5px;
            }
            .userWiget > .alarm{
                padding-top: 5px;
                color: red;
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

            var serverMsgDialog;

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
                $("#reSearch, #people, .userWiget > .alarm").hide();

                var userInfoCookie = $.cookie(userInfoCookieName);
                var babInfoCookie = $.cookie(babInfoCookieName);
                var isUserInfoExist = (userInfoCookie != null);
                var isBabInfoExist = (babInfoCookie != null);

                console.log(userInfoCookie);
                console.log(babInfoCookie);

                //-------------dialog init
                var serverMsgDialog = $("#dialog-message2").dialog({
                    autoOpen: false,
                    resizable: false,
                    height: "auto",
                    width: 400,
                    modal: true,
                    buttons: {
                        "確定": function () {
                            return true;
                        },
                        "取消": function () {
                            $(this).dialog("close");
                            return false;
                        }
                    }
                });

                //Don't do the code after this check when user cookie info is not vaild.
                var cookieCheckStatus = checkExistCookies();
                if (cookieCheckStatus == false) {
                    return false;
                }

                initUserInputWiget();

                $("#step1").find("select").attr("disabled", isUserInfoExist);

                $("#saveInfo").attr("disabled", isUserInfoExist);
                $("#clearInfo").attr("disabled", !isUserInfoExist);

                $("#babBegin, #clearInfo, .userWiget>div>input:eq(0)").attr("disabled", isBabInfoExist && ($.parseJSON(userInfoCookie).station != firstStation));

                $(":text").keyup(function () {
                    textBoxToUpperCase($(this));
                });

                $(document).on("keyup", "#po", function () {
                    getModel($(this).val(), $(this).next());
                });

                //Re搜尋工單
                $("#reSearch").click(function () {
                    $("#step2").block({message: "Please wait..."});
                    $(this).next().trigger("keyup");
                    setTimeout(function () {
                        lockStep2(false);
                    }, 2000);
                });

                var autoUserLogin = function () {
                    var newJobnumber = $(this).val();
                    window.clearTimeout(hnd);
                    hnd = window.setTimeout(function () {
                        if (!checkVal(newJobnumber) || newJobnumber.length < 5 || !checkUserExist(newJobnumber)) {
                            showMsg(userNotFoundMessage);
                            $("#jobnumber").val("");
                            lockStep2(true);
                            return false;
                        } else {
                            changeUser(newJobnumber);
                            lockStep2(false);
                            console.log("jobnumber change to " + newJobnumber);
                            showMsg("success");
                        }
                    }, 1000);
                };
                
                if ($('#step2').is(':visible')) {
                    $("body").on("keyup", "#jobnumber", autoUserLogin);
                } else {
                    $("body").off("keyup", "#jobnumber", autoUserLogin);

                }

                //儲存使用者資訊
                $("#saveInfo").click(function () {
                    saveUserStatus();
                });

                //重設使用者資訊
                $("#clearInfo").click(function () {
                    if (confirm("確定離開此站別?")) {
                        if (!isUserInfoExist) {
//                            showMsg("步驟1 cookie不存在，無法登出，請聯絡系統管理員。");
                            return false;
                        }
                        var obj = $.parseJSON(userInfoCookie);
                        stationLogout(obj);
                    }
                });

                //投入工單(第一站)
                $("#babBegin").click(function () {
                    var po = $("#po").val();
                    var modelname = $("#modelname").val();
                    var people = $("#people").val();
                    if (confirm("確定開始工單?\n" + "工單:" + po + "\n機種:" + modelname + ("\n人數:" + people))) {
                        startBab();
                    }
                });

                //結束工單(二三站)
                $("#babEnd").click(function () {
                    var line = $("#lineNo").val();
                    var station = $("#station").val();
                    var processingBab = getProcessingBab(line);
                    if (processingBab == null) {
                        showMsg("站別一無工單投入，無法結束");
                        return false;
                    }
                    console.log(processingBab);
                    if (confirm(
                            "站別 " + station + " 確定儲存?" +
                            "\n工單: " + processingBab.PO +
                            "\n機種: " + processingBab.model_name +
                            "\n開始時間: " + processingBab.btime.substring(0, 16)
                            )) {
                        var data = {
                            lineNo: line,
                            station: station,
                            BABid: processingBab.id
                        };
                        babEnd(data);
                    }
                });

                $(":text").focus(function () {
                    $(this).select();
                });

            });

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

            function lockStep2(flag) {
                if (flag) {
                    $("#step2").block({message: "請先在步驟一完成相關步驟。", css: {cursor: 'default'}, overlayCSS: {cursor: 'default'}});
                } else {
                    $("#step2").unblock();
                }
            }

            function lockAllUserInput() {
                $(":input,select").not("#redirectBtn").attr("disabled", "disabled");
            }

            function initUserInputWiget() {
                var userInfoCookie = $.cookie(userInfoCookieName);
                var babInfoCookie = $.cookie(babInfoCookieName);

                showInfo(null);

                if (userInfoCookie != null) {
                    var obj = $.parseJSON(userInfoCookie);
                    $("#lineNo").val(obj.lineNo);
                    $("#jobnumber").val(obj.jobnumber);
                    $("#station").val(obj.station);
                    lockStep2(false);

                    if (obj.station == firstStation) {
                        $("#babEnd, .stationHintMessage").hide();
                        $("#step2Hint")
                                .append("<li>輸入工單</li>")
                                .append("<li class='importantMsg'>確定系統有帶出機種</li>")
                                .append("<li>選擇人數</li>")
                                .append("<li>點選<code>Begin</code>開始投入</li>")
                                .append("<li>如果要更換使用者，請點選<code>換人</code>，填入您的新工號之後進行工號切換</li>");
                        $("#people").show();
                    } else {
                        $("#po, #modelname ,#babBegin").hide();
                        $("#babEnd, .stationHintMessage").show();
                        $("#step2Hint")
                                .append("<li>做完最後一台時點擊<code>結束</code>，告知系統您已經做完了</li>")
                                .append("<li>如果要更換使用者，請點選<code>換人</code>，填入您的新工號之後進行工號切換</li>");
//                        otherStationSearchResult = getProcessingBab(obj.lineNo);
//                        console.log(otherStationSearchResult);
//                        showInfo(otherStationSearchResult);
                    }

                    if (babInfoCookie != null) {
                        var obj = $.parseJSON(babInfoCookie);
                        $("#modelname").val(obj.model_name);
                        $("#modelname").prev().val(obj.PO);

                        if ($("#people").is(":visible")) {
                            $("#people").val(obj.people);
                        }
                        showInfo(obj);
                        showMsg("資料已經儲存");
                    }
                } else {
                    lockStep2(true);
                }
            }

            //最底下顯示作用中的工單
            function showInfo(data) {
                if (data != null) {
                    $("#processingBab").html("工單: " + data.PO + " | 機種 : " + data.model_name + " | 人數 : " + data.people);
                } else {
                    $("#processingBab").html("尚無資料");
                }
            }

            function saveUserStatus() {
                var lineName = $("#lineNo option:selected").text();
                var lineNo = $("#lineNo").val();
                var jobnumber = $("#jobnumber").val();
                var station = $("#station").val();

                if (!confirm("確定您所填入的資料無誤?\n" + "線別:" + lineName + "\n工號:" + jobnumber + "\n站別:" + station)) {
                    return false;
                }

                if (checkVal(lineNo, jobnumber, station) == false || !jobnumber.match(tabreg)) {
                    showMsg(paramNotVaildMessage);
                    return false;
                } else {
                    console.log("input val checking pass");
                }

                if (!checkUserExist(jobnumber)) {
                    showMsg(userNotFoundMessage);
                    return false;
                } else {
                    console.log("jobnumber checking pass");
                }

                stationLogin({
                    lineNo: lineNo,
                    jobnumber: jobnumber,
                    station: station
                });
            }

            //步驟一儲存使用者資訊
            function stationLogin(userInfo) {
                userInfo.floor = $("#userSitefloorSelect").val();
                userInfo.action = STATION_LOGIN;
                changeUserLoginStatus(userInfo);
            }

            function stationLogout(data) {
                data.action = STATION_LOGOUT;
                changeUserLoginStatus(data);
            }

            //換人(把bab登入登出資料表jobnumber update)
            function changeUser(newJobnumber) {
                var data = $.parseJSON($.cookie(userInfoCookieName));
                data.action = "CHANGEUSER";
                data.jobnumber = newJobnumber;
                changeUserLoginStatus(data);
            }

            //站別登入
            function changeUserLoginStatus(data) {
                data.lineType = "babLine";

                $.ajax({
                    type: "Post",
                    url: "StationLoginServlet",
                    data: data,
                    dataType: "html",
                    success: function (response) {
                        if (response == "success") {
                            if (data.action == "CHANGEUSER") {
                                changeJobnumberInCookie(data.jobnumber);
                            } else if (data.action == STATION_LOGIN) {
                                generateCookie(userInfoCookieName, JSON.stringify(data));
                            } else if (data.action == STATION_LOGOUT) {
                                removeAllStepCookie();
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

            //使用者換人時，把cookievaule做更新
            function changeJobnumberInCookie(newJobnumber) {
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

            function getProcessingBab(saveline) {
                var obj;
                $.ajax({
                    type: "Post",
                    url: "BabSearch",
                    async: false,
                    data: {
                        saveline: saveline
                    },
                    dataType: "json",
                    success: function (response) {
                        var json = response;
                        obj = json;
                    },
                    error: function () {
                        showMsg("error");
                    }
                });
                return obj;
            }

            //only start on station 1
            function startBab() {
//                    save the user info right here where servermessage return success information
                if ($.cookie(userInfoCookieName) == null) {
                    showMsg(userNotFoundMessage);
                    return false;
                }

                var data = {
                    po: $("#po").val(),
                    modelname: $("#modelname").val()
                };

                console.log(data);

                if (!checkVal(data.po, data.modelname) || data.modelname == "data not found") {
                    showMsg(paramNotVaildMessage);
                    return false;
                }

                var people = $("#people").val();
                if (!checkVal(people)) {
                    showMsg(paramNotVaildMessage);
                    return false;
                } else {
                    data.people = people;
                }
                babInput(data);
                console.log("First station input new babs.");
            }

            //站別一對資料庫操作
            function babInput(data) {
                var totalUserInfo = $.extend($.parseJSON($.cookie(userInfoCookieName)), data);
                $.ajax({
                    type: "Post",
                    url: "BABInputServlet",
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

            //結束工單(中間站別結束Sensor，最後一站收線)
            function babEnd(data) {
                $.ajax({
                    type: "Post",
                    url: "BABEndServlet",
                    data: data,
                    dataType: "html",
                    success: function (response) {
                        showMsg(response);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showMsg(xhr.responseText);
                    }
                });
            }

            //站別一投入過的資料暫存
            function addValueToBabCookie(data) {
                generateCookie(babInfoCookieName, JSON.stringify({
                    PO: data.po,
                    model_name: data.modelname,
                    people: data.people
                }));
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
                    fadeIn: 0,
                    overlayCSS: {
                        backgroundColor: '#FFFFFF',
                        opacity: .3
                    }
                });
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
        <div id="dialog-message2" title="${initParam.pageTitle}">
            <p>
                <span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
                請務必確認以下資訊
            </p>
            <div id="dialog-content"></div>
        </div>
        <!--Dialogs-->

        <!--Contents-->
        <div class="container">

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
                        <input type="button" id="babEnd" value="結束(儲存線平衡紀錄)" />
                        <input type="button" id="changeUser" value="換人" />
                    </div>
                    <div class="stationHintMessage alarm">
                        <span class="glyphicon glyphicon-alert"></span>
                        做完時請記得做儲存動作
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
