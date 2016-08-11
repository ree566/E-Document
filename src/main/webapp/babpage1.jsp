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
    <c:if test="${(userSitefloor == null) || (userSitefloor == '')}">
        <c:redirect url="/" />
    </c:if>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
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
                border-bottom-style:dotted;
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
            var userNotFoundMessage = "使用者不存在，請重新確認。";

            var userInfoCookieName = "userInfo", babInfoCookieName = "babInfo";
            var STATION1_LOGIN = true, STATION1_OUT = false;

            var firstStation = 1;

            $(function () {
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
                            if (!checkVal(newJobnumber)) {
                                return false;
                            } else {
                                saveUserStatus();
//                                checkAndStartBab();
                                $(this).dialog("close");
                            }
                        },
                        "取消": function () {
                            $(this).dialog("close");
                            $("#dialog").dialog("open");
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
                            checkAndStartBab();
                            $(this).dialog("close");
                        },
                        "否": function () {
                            $(this).dialog("close");
                            $("#dialog-message").dialog("open");
                        }
                    }
                });

                changeInputPOAction();

                if (isUserInfoExist) {
                    var obj = $.parseJSON(userInfoCookie);
                    $("#lineNo").val(obj.lineNo);
                    $("#jobnumber").val(obj.jobnumber);
                    $("#station").val(obj.station);
                    $("#step2").unblock();
                    obj.station == firstStation ? $("#babEnd, .userWiget > .station1HintMessage").hide() : $("#babEnd, .userWiget > .station1HintMessage").show();
                } else {
                    $("#step2").block({message: "請先在步驟一完成相關步驟。"});
                }

                if (isBabInfoExist) {
                    var obj = $.parseJSON(babInfoCookie);
                    $("#modelname").val(obj.modelname);
                    $("#modelname").prev().val(obj.po);
                    $("#serverMsg").html("資料已經儲存");
                }

                if (isUserInfoExist && isBabInfoExist) {
                    console.log($.extend($.parseJSON(userInfoCookie), $.parseJSON(babInfoCookie)));
                }

                $("#step1").find(":text,select").attr("disabled", isUserInfoExist);

                $("#saveInfo").attr("disabled", isUserInfoExist);
                $("#clearInfo").attr("disabled", !isUserInfoExist);

                $("#babBegin, #clearInfo, .userWiget>div>input:eq(0)").attr("disabled", isBabInfoExist && ($.parseJSON(userInfoCookie).station != firstStation));
                $("#babEnd").attr("disabled", !isBabInfoExist);

                $(document).on("keyup", "#po", function () {
                    textBoxToUpperCase($(this));
                    getModel($(this).val(), $(this).next());
                });

                //從已經從站別1儲存的工單資料中尋找相關資訊(LS_BAB table)
                $(document).on("keyup", "#po1", function () {
                    textBoxToUpperCase($(this));
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
                    if (confirm("saveInfo?")) {
                        saveUserStatus();
                    }
                });

                //重設使用者資訊
                $("#clearInfo").click(function () {
                    if (confirm("clearInfo?")) {
                        if ($("#station").val() == firstStation) {
                            if (!isUserInfoExist) {
                                $("#serverMsg").html("步驟1 cookie不存在，無法登出，請聯絡系統管理員。");
                                return false;
                            }
                            var obj = $.parseJSON(userInfoCookie);
                            console.log(obj);
                            firstStationLogin(obj, STATION1_OUT);
                        }
                        //Just remove the cookie.
                        removeCookie(userInfoCookieName);
                        removeCookie(babInfoCookieName);
                        reload();

                    }
                });

                //操作工單登入
                $("#babBegin").click(function () {
                    if (isBabInfoExist) {
                        dialog.dialog("open");
                    } else {
                        if (confirm("確定儲存?")) {
                            checkAndStartBab();
                        }
                    }
                });

                //操作工單登出
                $("#babEnd").click(function () {
                    var userInfo = $.parseJSON(userInfoCookie);
                    if (confirm("站別 " + userInfo.station + " 確定儲存?")) {
                        removeCookie(babInfoCookieName);
                        reload();
                    }
                });

                $("#clearAllCookie").click(function () {
                    $.removeCookie(userInfoCookieName);
                    $.removeCookie(babInfoCookieName);
                    reload();
                });

            });

            //extra functions

            function saveUserStatus() {
                var userInfo = {
                    lineNo: $("#lineNo").val(),
                    jobnumber: $("#jobnumber").val(),
                    station: $("#station").val()
                };

                if (checkVal(userInfo.lineNo, userInfo.jobnumber, userInfo.station) == false) {
                    $("#serverMsg").html("請檢查輸入欄位");
                    return false;
                }

                if (!checkUserExist(userInfo.jobnumber)) {
                    $("#serverMsg").html(userNotFoundMessage);
                    return false;
                }

                saveUserInfoToCookie(userInfo);
            }

            //步驟一儲存使用者資訊
            function saveUserInfoToCookie(userInfo) {
                if (userInfo.station == firstStation) {
                    firstStationLogin(userInfo, STATION1_LOGIN);
                } else {
                    generateCookie(userInfoCookieName, JSON.stringify(userInfo));
                    reload();
                }
            }

            //看使用者是否存在
            function checkUserExist(jobnumber) {
                var result;
                $.ajax({
                    type: "Post",
                    url: "GetUser",
                    data: {
                        jobnumber: jobnumber
                    },
                    dataType: "html",
                    async: false,
                    success: function (response) {
                        result = response;
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        $("#serverMsg").html(xhr.responseText);
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
                                $("#serverMsg").html(serverErrorConnMessage);
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
                                $("#serverMsg").html("找不到工單資料");
                                $("#modelname").val("data not found");
                            } else {
                                $("#serverMsg").html("找到資料");
                                $("#modelname").val(obj.Model_name);
                            }
                            $("#reSearch").show();
                        },
                        error: function () {
                            $("#serverMsg").html(serverErrorConnMessage);
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
                            if (action == STATION1_LOGIN) {
                                generateCookie(userInfoCookieName, JSON.stringify(data));
                            } else {
                                $.removeCookie(userInfoCookieName);
                            }
                            reload();
                        } else {
                            $("#serverMsg").html(response);
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        $("#serverMsg").html(xhr.responseText);
                    }
                });
            }

            function checkAndStartBab() {
//                    save the user info right here where servermessage return success information

                var data = {
                    po: $("#po").val(),
                    modelname: $("#modelname").val()
                };

                var isFirstStation = ($("#station").val() == firstStation);

//                if (!checkVal(data.lineNo, data.jobnumber, data.station, data.po, data.modelname) || (isFirstStation && !checkVal(data.people)) || data.modelName == "data not found") {
//                    $("#serverMsg").html("輸入資料有誤，請重新再確認");
//                    return false;
//                }

                if (isFirstStation) {
//                    inputNewBab(data);
//                    Need to combine two cookie value.
                    addValueToBabCookie(data);
                    //just find if po modelname exist, input new bab.
                } else {
                    //find station 1 input bab
                    //find bab that exist this station or not
                }
            }

            //投入工單
            function inputNewBab(data) {
                data.action = "insert";
                saveBabInfo(data);
            }

            //後面站別關閉工單
            function removeExistBab(data) {
                data.action = "delete";
                saveBabInfo(data);
            }

            //對資料庫操作
            function saveBabInfo(data) {
                $.ajax({
                    type: "Post",
                    url: "SaveBABInfo",
                    data: data,
                    dataType: "json",
                    success: function (response) {
                        if (data.action == "insert") {
                            addValueToBabCookie(data);
                        } else if (data.action == "delete") {
                            console.log("Save Bab");
                        } else {
                            $("#serverMsg").html(response);
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        $("#serverMsg").html(xhr.responseText);
                    }
                });
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

            //removeCookieByName
            function removeCookie(name) {
                $.removeCookie(name);
            }

            //refresh the window
            function reload() {
                window.location.reload();
            }
        </script>
    </head>
    <body>
        <input id="userSitefloorSelect" type="hidden" value="${userSitefloor}">
        <div id="titleAlert">
            <c:out value="您所選擇的樓層是: ${userSitefloor}" />
            <a href="index.jsp">
                <button id="redirectBtn" class="btn btn-default" >不是我的樓層?</button>
            </a>
        </div>
        <jsp:include page="head.jsp" />
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
            <!--<button id="clearAllCookie" class="btn btn-default">DirectClear</button>-->

            <div id="step1" class="step">
                <div class="userWiget form-inline">
                    <select id="lineNo">
                        <option value="-1">---請選擇線別---</option>
                        <c:forEach var="lines" items="${lineDAO.getLine(param.sitefloor)}">
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
                    <h5>請選擇您的線別。</h5>
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
                    </div>
                    <div class="station1HintMessage alarm">
                        <span class="glyphicon glyphicon-alert"></span>
                        做完時請記得做儲存動作
                        <span class="glyphicon glyphicon-arrow-up"></span>
                    </div>
                </div>
                <div class="wigetInfo">
                    <h3>步驟2:</h3>
                    <h5>請輸入工單號碼。</h5>
                </div>
            </div>

            <div id="step3" class="step">
                <div id='serverMsg' class="userWiget form-inline">        
                </div>
                <div class="wigetInfo">
                    <h3>步驟3:觀看伺服器訊息。</h3>
                    <h5>此處會顯示伺服器訊息。</h5>
                </div>
            </div>

            <div id="step4" class="step">
                <div class="userWiget form-inline">
                    There's the processing PN information.
                </div>
                <div class="wigetInfo">
                    <h3>此處會顯示站別1輸入的工單。</h3>
                </div>
            </div>

            <div id="hintmsg" style="color:red;font-weight: bold;padding-left: 10px">
                <p>※第一站人員請先Key入相關資料再把機子放到定位(否則會少一台紀錄)</p>
                <p>機子擋住Sensor即開始計時，休息時間的操作不列入計算範圍之內。</p>
            </div>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
