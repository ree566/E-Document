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
            #stepMax{
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

            var userInfoCookieName = "userInfo", babInfoCookieName = "babInfo";
            var STATION1_LOGIN = true, STATION1_OUT = false;

            var firstStation = 1;


            $(function () {
                $("select, input").addClass("form-control");
                var textboxPlaceHolder = ["請刷入條碼1", "請刷入條碼2", "請刷入條碼3"];
                var hintMsgMain = ["請刷入", "請刷入個人", "請刷入您正要做的"];
                var hintMsgAlarm = ["個人工號", "所在處資訊", "機種"];

                var maxStep = 3;
                var currentStepOpacity = 1;
                var notCurrentStepOpacity = 0.2;

                $("#barcode1").focus();

                for (var i = 0, j = maxStep; i < j; i++) {
                    var stepIndex = i + 1;
                    $("#barcode" + stepIndex).attr("placeholder", textboxPlaceHolder[i]);
                    $("#step" + stepIndex + " .hintMsgMain").html(hintMsgMain[i]);
                    $("#step" + stepIndex + " .hintMsgAlarm").html(hintMsgAlarm[i]);
                    if (stepIndex != 1) {
                        $("#step" + stepIndex).find(":text").attr("disabled", true);
                        $("#step" + stepIndex).css({opacity: notCurrentStepOpacity});
                    }
                }

                $(":text").keyup(function (e) {
                    var nextStep = $(this).parents(".step").next();
                    if (e.keyCode == 13 && checkVal($(this).val().trim()) && nextStep.attr("id") != "stepMax") {
                        $(".step:not(#stepMax)").fadeTo("fast", notCurrentStepOpacity);
                        nextStep.fadeTo("fast", currentStepOpacity);
                        nextStep.find(":text").attr("disabled", false).focus();
                    }
                });

                $(":text").on("click", function () {
                    if ($(this).parents(".step").css("opacity") != currentStepOpacity) {
                        $(".step:not(#stepMax)").fadeTo("fast", notCurrentStepOpacity);
                        $(this).parents(".step").fadeTo("fast", currentStepOpacity);
                    }
                });

            });

            //取得機種
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

            //取得LS_BAB table
            function getBAB(text, saveline) {
                var reg = "^[0-9a-zA-Z]+$";
                if (text != "" && text.match(reg)) {
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
                } else {
                    $("#serverMsg").val("");
                }
            }

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

            //和伺服器確認該線別是否已經開線，以true(登入)false(登出)做區隔
            function firstStationLogin(data, action) {
                data.action = action;
                $.ajax({
                    type: "Post",
                    url: "LineLogin",
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

            function saveBabInfo(data) {
                $.ajax({
                    type: "Post",
                    url: "SaveBABInfo",
                    data: data,
                    dataType: "json",
                    success: function (response) {
                        $("#servermsg").html(response);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        $("#servermsg").html(xhr.responseText);
                    }
                });
            }

            function textBoxToUpperCase(obj) {
                obj.val(obj.val().trim().toLocaleUpperCase());
            }

            function generateCookie(name, value) {
                var date = new Date();
                var minutes = 12 * 60;
                date.setTime(date.getTime() + (minutes * 60 * 1000));
                $.cookie(name, value, {expires: date});
            }

            function removeCookie(name) {
                $.removeCookie(name);
            }

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
        <!--Contents-->
        <div class="container">
            <!--<button id="clearAllCookie" class="btn btn-default">DirectClear</button>-->

            <c:forEach var="i" begin="1" end="3">
                <div id="step${i}" class="step">
                    <div class="userWiget">
                        <input type="text" id="barcode${i}" class="form-control" />
                    </div>
                    <div class="wigetInfo">
                        <h3 class="hintTitle">步驟${i}:</h3>
                        <h5 class="hintMsg"><font class="hintMsgMain"></font><code class="hintMsgAlarm"></code>。</h5>
                    </div>
                </div>
            </c:forEach>

            <div id="stepMax" class="step">
                <div id='serverMsg' class="userWiget form-inline">        
                </div>
                <div class="wigetInfo">
                    <h3>伺服器訊息。</h3>
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
