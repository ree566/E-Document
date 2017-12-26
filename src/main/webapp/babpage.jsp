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
            #firstStationWiget table tr td{
                padding: 5px;
            }
            #publicWiget table tr td{
                padding: 5px;
            }
            #otherStationWiget table tr td{
                padding: 5px;
            }
        </style>
        <script src="js/jquery-1.11.3.min.js"></script>
        <script src="js/jquery-ui.min.js"></script>
        <script src="js/jquery-ui-1.10.0.custom.min.js"></script>
        <script src="js/jquery.blockUI.js"></script>
        <script src="js/jquery.cookie.js"></script>
        <script src="js/param.check.js"></script>
        <script src="js/moment.js"></script>

        <script>
            var cookie_expired_time = moment().set({hour: 23, minute: 0, second: 0});

            var totalLineStatus = new Map();

            var hnd;//鍵盤輸入間隔
            var hnd2;//鍵盤輸入間隔
            var serverErrorConnMessage = "Error, the textbox can't connect to server now.";
            var userNotFoundMessage = "使用者不存在，請重新確認，如有問題請通知系統管理人員。",
                    paramNotVaildMessage = "輸入資料有誤，請重新再確認。";

            var userInfoCookieName = "userInfo", testLineTypeCookieName = "testLineTypeCookieName", cellCookieName = "cellCookieName";
            var STATION_LOGIN = "LOGIN", STATION_LOGOUT = "LOGOUT", CHANGE_USER = "changeUser";
            var BAB_END = "stationComplete";

            var serverMsgTimeout;

            var firstStation = 1;

            var tabreg = /^[0-9a-zA-Z-]+$/;//Textbox check regex.

            $(function () {
                $(document).ajaxSend(function () {
                    block();//Block the screen when ajax is sending, Prevent form submit repeatly.
                });
                $(document).ajaxComplete(function () {
                    $.unblockUI();//Unblock the ajax when success
                });

                $("select, :text, :button").addClass("form-control");
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

                //Don't set event on dom when user cookie info is not vaild.
                var cookieCheckStatus = checkExistCookies();
                if (cookieCheckStatus == false) {
                    return false;
                }

                initUserInputWiget();

                $("#step1").find(":text,select").attr("disabled", isUserInfoExist);

                $("#saveInfo").attr("disabled", isUserInfoExist);
                $("#clearInfo").attr("disabled", !isUserInfoExist);

                $("#lineId").change(function () {
                    setStationOptions();
                });


                $(":text").keyup(function () {
                    textBoxToUpperCase($(this));
                }).focus(function () {
                    $(this).select();
                });

                //Step 1 event
                //儲存使用者資訊
                $("#saveInfo").click(function () {
                    var selectLine = $("#lineId option:selected");
                    var jobnumber = $("#jobnumber").val();
                    var station = $("#station").val();

                    if (confirm("確定您所填入的資料無誤?\n" + "線別:" + selectLine.text() + "\n工號:" + jobnumber + "\n站別:" + station)) {
                        saveUserStatus(selectLine.val(), jobnumber, station);
                    }
                });

                //當找不到資訊時，相關event註冊到此(只有saveInfo後後續event才會依序註冊)
                if (!isUserInfoExist) {
                    return false;
                }

                //重設使用者資訊
                $("#clearInfo").click(function () {
                    if (confirm("確定離開此站別?")) {
                        if (!isUserInfoExist) {
                            return false;
                        }
                        if ($("#station").val() == firstStation) {
                            lineStatusUpdate($.parseJSON(userInfoCookie), STATION_LOGOUT);
                        } else {
                            //Just remove the cookie.
                            removeAllStepCookie();
                            reload();
                        }
                    }
                });

                //Step 2 event
                $("#po").on("keyup", function () {
                    getModel($(this).val(), $("#modelName"));
                });

                $("#searchProcessing").click(function () {
                    showProcessing();
                });

                //Re搜尋工單
                $("#reSearch").click(function () {
                    $("#step2").block({message: "Please wait..."});
                    $("#po").trigger("keyup");
                    setTimeout(function () {
                        $("#step2").unblock();
                    }, 2000);
                });

                //站別一操作工單投入
                $("#babBegin").click(function () {
                    var po = $("#po").val();
                    var modelName = $("#modelName").val();
                    var people = $("#people").val();

                    if (checkVal(po, modelName, people) == false || modelName == "data not found") {
                        showMsg(paramNotVaildMessage);
                        return false;
                    }

                    if (confirm("確定開始工單?\n" + "工單:" + po + "\n機種:" + modelName + "\n人數:" + people)) {
                        startBab(po, modelName, people);
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

                    if (searchResult == null) { //當查第二次還是沒有結果
                        showMsg("站別一無投入的工單，請重新做確認");
                    } else {
                        if (confirm(
                                "站別 " + userInfo.station + " 確定儲存?\n" +
                                "工單: " + searchResult.po + "\n" +
                                "機種: " + searchResult.modelName + "\n" +
                                "人數: " + searchResult.people
                                )) {

                            otherStationUpdate({
                                bab_id: searchResult.id,
                                station: userInfo.station,
                                jobnumber: userInfo.jobnumber,
                                action: BAB_END
                            });
                        }
                    }
                });

                $("#people").change(function () {
                    var optionsLength = $("#people > option").length;
                    $("#startStation").val(1);
                    var validMaxStartStation = optionsLength - $(this).val() + 1;
                    $("#startStation option:gt(" + (validMaxStartStation - 1) + ")").attr("disabled", true);
                    $("#startStation option:lt(" + validMaxStartStation + ")").removeAttr("disabled");
                });

                $("#changeUser").click(function () {
                    dialogMessage.dialog("open");
                });

//                $("[name='my-checkbox']").bootstrapSwitch();
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
                    fadeIn: 0,
                    overlayCSS: {
                        backgroundColor: '#FFFFFF',
                        opacity: .3
                    }
                });
            }

            function getLine() {
                var result;
                $.ajax({
                    type: "GET",
                    url: "<c:url value="/BabLineController/findAll" />",
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
                    if (cookieMsg["floor.name"] != null && cookieMsg["floor.name"] != $("#userSitefloorSelect").val()) {
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
                console.log(userInfoCookie);

                $("#searchProcessing").attr("disabled", userInfoCookie == null);

                if (userInfoCookie != null) {
                    var obj = $.parseJSON(userInfoCookie);
                    $("#lineId").val(obj["line.id"]);
                    $("#jobnumber").val(obj.jobnumber);
                    setStationOptions();
                    $("#station").val(obj.station);
                    $("#step2").unblock();
                    showProcessing();

                    if (obj.station == firstStation) {
                        var line = totalLineStatus.get($("#lineId option:selected").text());
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
                if (line.lock != 1) {
                    $("#lineId").append("<option value=" + line.id + " >" + line.name + "</option>");
                }
            }

            function setStationOptions() {
                var selectedLineName = $("#lineId option:selected").text();
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
                var selectedLineName = $("#lineId option:selected").text();
                var line = totalLineStatus.get(selectedLineName);
                for (var i = 1; i <= line.people; i++) {
                    if (i >= miniumPeople) {
                        $("#people").append("<option value=" + i + ">" + i + " 人</option>");
                    }
                    if (i < line.people) {
                        $("#startStation").append("<option value=" + i + ">起始於第 " + i + " 站點</option>");
                    }
                }
            }

            function lockAllUserInput() {
                $(":input,select").not("#redirectBtn, #directlyClose").attr("disabled", "disabled");
            }

            function saveUserStatus(lineId, jobnumber, station) {

                if (checkVal(lineId, jobnumber, station) == false || !jobnumber.match(tabreg)) {
                    showMsg(paramNotVaildMessage);
                    return false;
                }

                if (!checkUserExist(jobnumber)) {
                    showMsg(userNotFoundMessage);
                    return false;
                }

                saveUserInfoToCookie({
                    "line.id": lineId,
                    jobnumber: jobnumber,
                    station: station
                });
            }

            //步驟一儲存使用者資訊
            function saveUserInfoToCookie(userInfo) {
                userInfo["floor.name"] = $("#userSitefloorSelect").val();

                if (userInfo.station == firstStation) {
                    lineStatusUpdate(userInfo, STATION_LOGIN);
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
                data["bab_id"] = bab.id;
                data.jobnumber = newJobnumber;
                data.action = CHANGE_USER;
                otherStationUpdate(data);
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
                            type: "GET",
                            url: "BabController/findModelNameByPo",
                            data: {
                                po: text.trim()
                            },
                            dataType: "html",
                            success: function (response) {
                                obj.val(response);
                                $("#reSearch").show();
                                $("#ispre").prop("checked", false);
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
                    type: "GET",
                    url: "BabController/findProcessingByLine",
                    data: {
                        line_id: $("#lineId").val()
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
                                "<p" + (i == 0 ? " class='alarm'" : "") + ">工單: " + processingBab.po +
                                " / 機種: " + processingBab.modelName +
                                " / 人數: " + processingBab.people +
                                " / 起始站別: " + processingBab.startPosition +
                                (processingBab.ispre == 1 ? " / 前置" : "") +
                                "</p>");
                    }
                } else {
                    showInfo("No data");
                }
            }

            //第一站綁定線別開啟關閉，保持唯一一個能投入工單的電腦
            function lineStatusUpdate(data, action) {
                $.ajax({
                    type: "Post",
                    url: "BabLineController/" + action.toLowerCase(),
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
            function startBab(po, modelName, people) {
                if ($.cookie(userInfoCookieName) == null) {
                    showMsg(userNotFoundMessage);
                    return false;
                }

                var startStation = parseInt($("#startStation").val());
                var people = parseInt($("#people").val());
                var maxSelection = $("#people > option").length;
                var ispre = $("#ispre").is(":checked") ? 1 : 0;

                if ((startStation + people - 1) > maxSelection) {
                    showMsg("startPosition or people is not valid");
                    return false;
                }

                if (startStation != firstStation) {
                    if (!confirm("起始站別確認無誤?")) {
                        return false;
                    }
                }

                saveBabInfo({
                    po: po,
                    modelName: modelName,
                    people: people,
                    startPosition: startStation,
                    ispre: ispre
                });
            }

            //站別一對資料庫操作
            function saveBabInfo(data) {
                var totalUserInfo = $.extend($.parseJSON($.cookie(userInfoCookieName)), data);
                $.ajax({
                    type: "Post",
                    url: "BabFirstStationController/insert",
                    data: totalUserInfo,
                    dataType: "html",
                    success: function (response) {
                        $("#searchProcessing").trigger("click");
                        showMsg(response);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        showMsg(xhr.responseText);
                    }
                });
            }

            //其他站別動作
            function otherStationUpdate(data) {
                $.ajax({
                    type: "Post",
                    url: "BabOtherStationController/" + data.action,
                    data: data,
                    dataType: "html",
                    success: function (response) {
                        showMsg(response);
                        if (response == "success") {
                            if (data.action == CHANGE_USER) {
                                changeJobnumberInCookie(data.jobnumber);
                            }
                            reload();
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
                $.cookie(name, value, {expires: cookie_expired_time.toDate()});
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
            <div id="step1" class="step">
                <div class="userWiget form-inline">
                    <select id="lineId" name="lineId">
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
                        <div id="firstStationWiget" class="row">
                            <table>
                                <tr>
                                    <td>輸入工單</td>
                                    <td>
                                        <input type="text" name="po" id="po" placeholder="請輸入工單號碼" autocomplete="off" maxlength="50">  
                                        <input type="text" name="modelName" id="modelName" placeholder="機種" readonly style="background: #CCC">
                                        <button class='btn btn-default' id='reSearch'>重新查詢</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>選擇人數和起始站別</td>
                                    <td>
                                        <select id='people'>
                                            <option value="-1">---請選擇人數---</option>
                                        </select>
                                        <select id='startStation'>
                                        </select>
                                        <input type="checkbox" id="ispre" /><label for="ispre">前置</label>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" class="alarm">※連續投入工單時，前一套工單未關閉的狀況下，將無法修改起始站別(可選擇與前套工單相同起始站別)</td>
                                </tr>
                                <tr>
                                    <td>投入工單</td>
                                    <td>
                                        <div class="col-md-6">
                                            <input type="button" id="babBegin" class="btn-block" value="Begin" />
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <div id="otherStationWiget" class="row">
                            <table>
                                <tr>
                                    <td>儲存結束工單</td>
                                    <td>
                                        <input type="button" id="babEnd" value="Save" />
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <div id="publicWiget" class="row">
                            <table>
                                <tr>
                                    <td>切換使用者</td>
                                    <td>
                                        <input type="button" id="changeUser" value="換人" />
                                    </td>
                                </tr>
                            </table>
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
