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
        <link rel="stylesheet" href="css/jquery-ui.css">
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
            .alarm{
                color: red;
            }
        </style>
        <script src="js/jquery-1.11.3.min.js"></script>
        <script src="js/jquery-ui.min.js"></script>
        <script src="js/jquery-ui-1.10.0.custom.min.js"></script>
        <script src="js/jquery.blockUI.js"></script>
        <script src="js/jquery.cookie.js"></script>
        <script src="js/param.check.js"></script>
        <script>
            var totalLineStatus = new Map();

            var hnd;//鍵盤輸入間隔
            var hnd2;//鍵盤輸入間隔
            var serverErrorConnMessage = "Error, the textbox can't connect to server now.";
            var userNotFoundMessage = "使用者不存在，請重新確認，如有問題請通知系統管理人員。",
                    paramNotVaildMessage = "輸入資料有誤，請重新再確認。";

            var userInfoCookieName = "userInfo", testLineTypeCookieName = "testLineTypeCookieName", cellCookieName = "cellCookieName";
            var STATION_LOGIN = "LOGIN", STATION_LOGOUT = "LOGOUT", CHANGE_USER = "CHANGE_USER";
            var BAB_END = "BAB_END";

            var serverMsgTimeout;

            var firstStation = 1;

            var tabreg = /^[0-9a-zA-Z-]+$/;//Textbox check regex.

            $(function () {
                $(document).ajaxSend(function () {
                    block();//Block the screen when ajax is sending, Prevent form submit repeatly.
                });
                $(document).ajaxSuccess(function () {
                    $.unblockUI();//Unblock the ajax when success
                });

                $("select, input").addClass("form-control");
                $("#reSearch").hide();
                $("#people").hide();

                var userInfoCookie = $.cookie(userInfoCookieName);
                var isUserInfoExist = (userInfoCookie != null);

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
                            }
                        },
                        "取消": function () {
                            $(this).dialog("close");
                        }
                    }
                });

                //Don't do the code after this check when user cookie info is not vaild.
                var cookieCheckStatus = checkExistCookies();
                if (cookieCheckStatus == false) {
                    return false;
                }

                initUserInputWiget();

                $("#step1").find(":text,select").attr("disabled", isUserInfoExist);

                $("#saveInfo").attr("disabled", isUserInfoExist);
                $("#clearInfo").attr("disabled", !isUserInfoExist);

//                $("#clearInfo, .userWiget>div>input:eq(0)").attr("disabled", ($.parseJSON(userInfoCookie).station != firstStation));

                $("#lineNo").change(function () {
                    setStationOptions();
                });

                $(":text").keyup(function () {
                    textBoxToUpperCase($(this));
                });

                $(document).on("keyup", "#po", function () {
                    getModel($(this).val(), $(this).next());
                });

                $("#searchProcessing").click(function () {
                    showProcessing();
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
                            return false;
                        }

                        if ($("#station").val() == firstStation) {

                            var obj = $.parseJSON(userInfoCookie);
                            firstStationLogin(obj, STATION_LOGOUT);
                        } else {
                            //Just remove the cookie.
                            removeAllStepCookie();
                            reload();
                        }
                    }
                });

                //站別一操作工單投入
                $("#babBegin").click(function () {
                    var po = $("#po").val();
                    var modelname = $("#modelname").val();
                    var people = $("#people").val();
                    if (checkVal(po, modelname, people) == false) {
                        showMsg(paramNotVaildMessage);
                        return false;
                    }
                    if (confirm("確定開始工單?\n" + "工單:" + po + "\n機種:" + modelname + "\n人數:" + people)) {
                        startBab();
                    }
                });

                var searchResult = null;

                //其他站別結束命令
                $("#babEnd").click(function () {
                    var userInfo = $.parseJSON(userInfoCookie);

                    if (searchResult == null) {
                        var data = searchProcessing();
                        searchResult = data[0];//取最先投入的工單做關閉
                    }

                    if (searchResult == null) {
                        showMsg("站別一無投入的工單，請重新做確認");
                    } else {
                        if (confirm(
                                "站別 " + userInfo.station + " 確定儲存?\n" +
                                "工單: " + searchResult.PO + "\n" +
                                "機種: " + searchResult.Model_name + "\n" +
                                "人數: " + searchResult.people
                                )) {
                            var data = {
                                babId: searchResult.id,
                                station: userInfo.station,
                                jobnumber: userInfo.jobnumber,
                                action: BAB_END
                            };
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

            function getLine() {
                var result;
                $.ajax({
                    type: "Post",
                    url: "GetLine",
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

            //extra functions
            function checkExistCookies() {
                var testLineTypeCookie = $.cookie(testLineTypeCookieName);
                var cellCookie = $.cookie(cellCookieName);
                var babLineTypeCookie = $.cookie(userInfoCookieName);

                if (testLineTypeCookie != null || cellCookie != null) {
                    lockAllUserInput();
                    var message = "您已經登入測試或Cell桌";
                    alert(message);
                    showMsg(message);
                    return false;
                }

                if (babLineTypeCookie != null) {
                    var cookieMsg = $.parseJSON(babLineTypeCookie);
                    if (cookieMsg.floor != null && cookieMsg.floor != $("#userSitefloorSelect").val()) {
                        lockAllUserInput();
                        alert("您已經登入其他樓層");
                        showMsg("您已經登入其他樓層");
                        return false;
                    }
                }
                return true;
            }

            function initUserInputWiget() {
                initLineMap();

                var userInfoCookie = $.cookie(userInfoCookieName);

                $("#searchProcessing").attr("disabled", userInfoCookie == null);

                if (userInfoCookie != null) {
                    var obj = $.parseJSON(userInfoCookie);
                    $("#lineNo").val(obj.lineNo);
                    $("#jobnumber").val(obj.jobnumber);
                    setStationOptions();
                    $("#station").val(obj.station);
                    $("#step2").unblock();
                    showProcessing();

                    if (obj.station == firstStation) {
                        var line = totalLineStatus.get($("#lineNo option:selected").text());
                        if (line.isused == 0) { //isused == 0, The line in database is closed.
                            removeCookie(userInfoCookieName);
                            initUserInputWiget();
                            alert("線別已經跳出，請重新進行步驟一");
                            reload();
                        }

                        $("#otherStationWiget").hide();
                        $("#step2Hint")
                                .append("<li>輸入工單</li>")
                                .append("<li class='importantMsg'>確定系統有帶出機種</li>")
                                .append("<li>選擇人數</li>")
                                .append("<li>點選<code>Begin</code>開始投入</li>")
                                .append("<li>如果要更換使用者，請點選<code>換人</code>，填入您的新工號之後進行工號切換</li>");
                        setPeopleOptions();
                        $("#people").show();
                    } else {
                        $("#firstStationWiget").hide();
                        $("#otherStationWiget").show();
                        $("#step2Hint")
                                .append("<li>做完最後一台時點擊<code>Save</code>，告知系統您已經做完了</li>")
                                .append("<li>如果要更換使用者，請點選<code>換人</code>，填入您的新工號之後進行工號切換</li>");
                    }
                } else {
                    $("#step2").block({message: "請先在步驟一完成相關步驟。", css: {cursor: 'default'}, overlayCSS: {cursor: 'default'}});
                }
            }

            function initLineMap() {
                var lines = getLine();
                for (var i = 0; i < lines.length; i++) {
                    var line = lines[i];
                    var lineName = line.name;
                    line.name = lineName.trim();
                    setLineOptions(line);
                    totalLineStatus.set(lineName.trim(), line);
                }
            }

            function setLineOptions(line) {
                $("#lineNo").append("<option value=" + line.id + " " + (line.lock == 1 ? "disabled style='opacity:0.2'" : "") + ">" + line.name + "</option>");
            }

            function setStationOptions() {
                var selectedLineName = $("#lineNo option:selected").text();
                $("#station").html("");
                var line = totalLineStatus.get(selectedLineName);
                if (line != null) {
                    for (var i = 1; i <= line.people; i++) {
                        $("#station").append("<option value=" + i + ">第 " + i + " 站</option>");
                    }
                }
            }

            function setPeopleOptions() {
                var miniumPeople = 2;
                var selectedLineName = $("#lineNo option:selected").text();
                var line = totalLineStatus.get(selectedLineName);
                for (var i = miniumPeople; i <= line.people; i++) {
                    $("#people").append("<option value=" + i + ">" + i + " 人</option>");
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
                } 

                if (!checkUserExist(userInfo.jobnumber)) {
                    showMsg(userNotFoundMessage);
                    return false;
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

            function changeJobnumber(newJobnumber) {
                var babs = searchProcessing();
                if (babs == null || babs.length == 0) {
                    changeJobnumberInCookie(newJobnumber);
                    reload();
                }
                var bab = babs[0];
                var data = $.parseJSON($.cookie(userInfoCookieName));
                data.babId = bab.id;
                data.jobnumber = newJobnumber;
                data.action = CHANGE_USER;
                otherStation(data);
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

            function searchProcessing() {
                var data;
                $.ajax({
                    type: "Post",
                    url: "BabSearch",
                    data: {
                        saveline: $("#lineNo").val()
                    },
                    dataType: "html",
                    async: false,
                    success: function (response) {
                        var babs = JSON.parse(response);
                        data = babs;
                    },
                    error: function () {
                        showMsg(serverErrorConnMessage);
                    }
                });
                return data;
            }

            function showProcessing() {
                showInfo("");
                var data = searchProcessing();
                if (data.length != 0) {
                    for (var i = 0; i < data.length; i++) {
                        var processingBab = data[i];
                        $("#processingBab").append(
                                "<p" + (i == 0 ? " class='alarm'" : "") + ">工單: " + processingBab.PO +
                                " / 機種: " + processingBab.Model_name +
                                " / 人數: " + processingBab.people +
                                "</p>");
                    }
                } else {
                    showInfo("No data");
                }
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
                if ($.cookie(userInfoCookieName) == null) {
                    showMsg(userNotFoundMessage);
                    return false;
                }

                var data = {
                    po: $("#po").val(),
                    modelname: $("#modelname").val()
                };

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
                saveBabInfo(data);

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

            //其他站別動作
            function otherStation(data) {
                $.ajax({
                    type: "Post",
                    url: "BABOtherStationServlet",
                    data: data,
                    dataType: "html",
                    success: function (response) {
                        if (response == "success") {
                            if (data.action == CHANGE_USER) {
                                changeJobnumberInCookie(data.jobnumber);
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
                }, 3000);
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
            <c:set var="lineInfo" value="${lineDAO.getLine(userSitefloor)}" />
            <div id="step1" class="step">
                <div class="userWiget form-inline">
                    <select id="lineNo" name="lineNo">
                        <option value="-1">---請選擇線別---</option>
                    </select>
                    <input type="text" id="jobnumber" placeholder="請輸入工號" autocomplete="off" maxlength="10"/>
                    <select id='station'>
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
                        <div id="firstStationWiget">
                            <button class='btn btn-default' id='reSearch'><span class="glyphicon glyphicon-repeat"></span></button>
                            <input type="text" name="po" id="po" placeholder="請輸入工單號碼" autocomplete="off" maxlength="50">  
                            <input type="text" name="modelname" id="modelname" placeholder="機種" readonly style="background: #CCC">
                            <select id='people'>
                                <option value="-1">---請選擇人數---</option>
                            </select>
                            <input type="button" id="babBegin" value="Begin" />
                        </div>

                        <div id="otherStationWiget">
                            <input type="button" id="babEnd" value="Save" />
                        </div>

                        <div>
                            <input type="button" id="changeUser" value="換人" />
                        </div>
                    </div>
                    <div class="stationHintMessage alarm">
                        <span class="glyphicon glyphicon-alert"></span>
                        做完時請記得做save。(Please "save" when you finished.)
                        <span class="glyphicon glyphicon-alert"></span>
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
                <div class="form-inline userWiget">
                    <div><input type="button" id="searchProcessing" value="查詢"></div>
                    <div id="processingBab" ></div>
                </div>
                <div class="wigetInfo">
                    <h3>完成:</h3>
                    <h5>此處可點選查詢按鈕搜尋正在進行的工單。</h5>
                    <h5>紅色字體代表目前正在進行線平衡量測的工單。</h5>
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
