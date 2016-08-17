<%-- 
    Document   : babpage
    Created on : 2015/9/23, 下午 01:07:42
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
        <title>組包裝 ${userSitefloor} 樓 - ${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="images/favicon.ico"/>
        <style>
            #titleAlert{
                background-color: green;
                color: white;
                text-align: center;
            }
            .Div0{
                float:left; width:100%; 
                border-bottom-style:dotted;
            }
            .Div1{
                float:left; width:60%; 
                padding: 10px 10px;
            }
            .Div2{
                float:right;width:40%;
            }
            #alterinfo:hover{
                cursor: pointer;
            }
            #sensordata{
                /*                display: none;
                                position: fixed;
                                right: 20px;
                                bottom: 20px;    
                                padding: 10px 15px;    
                                background: #777;
                                cursor: pointer;
                                width: 55%;*/
            }
            #div1{
                text-align: center;
                background-color: white;
                color: #880000;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/jquery.cookie.js"></script>
        <script src="js/jquery.blockUI.js"></script>
        <script src="js/cookie.check.js"></script>
        <!--<script src="js/babpage.js"></script>-->
        <script>
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
            $(document).ready(function () {

                $(document).ajaxSend(function () {
                    block();//Block the screen when ajax is sending, Prevent form submit repeatly.
                });
                $(document).on("ajaxSuccess, ajaxComplete",function () {
                    $.unblockUI();//Unblock the ajax when success
                });

                if (!are_cookies_enabled()) {
                    alert(cookie_disabled_message);
                    return;
                }

                var manuallyModelNameInput = false;

//                $('[data-toggle="tooltip"]').tooltip();

                //Init the object to boostrap's form
                $(":button").addClass("btn btn-default");
                $(":text,select,input[type='number']").addClass("form-control");

                var hnd2;//鍵盤輸入間隔
                var linevalue = -1; //Get user selection from the cookie
                var hnd;//鍵盤輸入間隔
                var jsonstring = $.cookie('babinfo');//Get the user saving information from cookie.
                var msgstring = $.cookie('servermsg');
                var saveline;//Get user selection from the cookie

                $("#searchpeople").hide();
                $("#step4").hide();

                //裏頭存了step的四個選項的div
                $step3_objs = $("#step3").children().detach();
                $("#cookieinfo").html(jsonstring != null ? "BAB cookie 已經儲存" : "尚無資料");
                $objgroup = $("#po, #line, #people, #begin");

                //抓取cookie所儲存的json data讓其他元件做應對
                if (jsonstring != null) {
                    $obj = $.parseJSON(jsonstring);
                    if ($obj != null) {
                        var userSitefloorSelect = $obj.userSitefloorSelect;
                        var pageSitefloor = $("#userSitefloorSelect").val();
                        if (userSitefloorSelect != null && userSitefloorSelect != pageSitefloor) {
                            $("#step2, #step3").hide();
                            $("#servermsg").html("您已經登入其他樓層");
                        }
                        saveline = $obj.line;
                        if (msgstring != null) {
                            $("#step3").html($step3_objs.eq(0));
                            var obj = $.parseJSON(msgstring);
                            if (obj.linestate == "success") {
                                if (saveline != null) {
                                    $("#isfirst").attr({
                                        disabled: true,
                                        checked: true
                                    });
                                    $("#lineselect").children().eq(saveline).attr("selected", true);
                                    $("#lineselect, #step1next").attr("disabled", true);
                                    $("#manuallyModelNameInput").prop("checked", false);
                                }
                            }
                        } else {
                            if ($obj != null) {
                                $("#lineselect").children().eq($obj.line).attr("selected", true);
                                $("#lineselect, #step1next, #isfirst").attr("disabled", true);
                                $("#step2").show();
                            }
                        }
                        var json = getdata(saveline);
                        if (json != null) {
                            var obj;
                            $("#end_line").html("");
                            $("#linestate").html("");
                            $(".sensorend").html("");
                            for (var i = 0; i < json.BABData.length; i++) {
                                obj = json.BABData[i];
                                var sArr = [];
                                sArr[sArr.length] = "<div class='bg-success'>編號: ";
                                sArr[sArr.length] = obj.id;
                                sArr[sArr.length] = "<input type='hidden' class='babid' value='";
                                sArr[sArr.length] = obj.id;
                                sArr[sArr.length] = "'>";
                                sArr[sArr.length] = " 工單號碼: ";
                                sArr[sArr.length] = obj.PO;
                                sArr[sArr.length] = "<input type='hidden' class='PO' value='";
                                sArr[sArr.length] = obj.PO;
                                sArr[sArr.length] = "'>";
                                sArr[sArr.length] = " | 機種 ";
                                sArr[sArr.length] = obj.model_name;
                                sArr[sArr.length] = " | 線別 ";
                                sArr[sArr.length] = obj.name;
                                sArr[sArr.length] = "<input type='hidden' class='line' value='";
                                sArr[sArr.length] = obj.line;
                                sArr[sArr.length] = "'>";
                                sArr[sArr.length] = "</div>";
                                var str = sArr.join('');
                                $("#linestate").append(str);
                            }
                        }
                    }
                } else {
                    $("#lineselect").removeAttr("disabled");
                    $objgroup.removeAttr("disabled");
                    $("#alterinfo").hide();
                }

                //Get the cookie info and setting value on the html objects.
                var po_search_result = $.cookie("po_search_result");
                if (po_search_result != null) {
                    var obj = JSON.parse(po_search_result);
                    if (obj != null) {
                        $("#po1").val(obj.PO);
                        $("#step3").html($step3_objs.eq(1));
                        $("#step3 #T_Button").html(generatebutton(obj.people));
                        var state = obj.S_State;
                        if (state != null) {
                            for (var i = 2; i <= obj.people; i++) {
                                var T = "T" + i;
                                if (state[T] == 1) {
                                    $("#T_Button #" + T).attr("disabled", "disabled");
                                }
                            }
                        }
                    }
                }

                //Get the cookie info and setting value on the html objects, 
                //show current sensor time by adding iframe.(WebSocket client connect page)
                var user_sel = $.cookie('user_sel');
                if (user_sel != null) {
                    var obj = JSON.parse(user_sel);
                    var string = "#T_Button #T" + obj.step3_sel;
                    $(string).addClass("btn-success").attr("disabled", true);
                    var isLastStation = $(string).is(':last-child');

                    if (obj.step3_sel == 1) {
                        $("#step4").hide();
                    } else {
                        $("#step4 :button").attr("id", isLastStation ? "end" : "s_end").val("結束按鈕" + (isLastStation ? "(儲存線平衡紀錄)" : ""));
                        $("#step4").show();
                    }

//                    $("#sensordata #div2").html("<iframe style='width:100%; height:80px' scrolling='no' src='Sensor'></iframe>");//Show the sensor time when user is inline.

                    $("#sensordata").slideToggle();
                }

                //抓取和伺服器提出開關線別所回傳的值(存在cookie中)
                if ($.cookie('people') != null) {
                    $("#people").children().eq($.cookie('people') - 1).attr("selected", true);
                    $("#people").attr("disabled", true);
                }

                $("#manuallyModelNameInput").change(function () {
                    manuallyModelNameInput = $(this).is(":checked");
                    $("#modelname").attr("readonly", !manuallyModelNameInput).attr("style", !manuallyModelNameInput ? "background: #CCC" : "").val("");
                });

                //Search the ModelName by PO.(Station 1)
                $("#po").on("keyup", function () {
                    textBoxToUpperCase($(this));
                    if (!manuallyModelNameInput) {
                        getModel($(this).val(), $(this).next());
                    }
                });

                $("#modelname").on("keyup", function () {
                    textBoxToUpperCase($(this));
                });


                //從已經從站別1儲存的工單資料中尋找相關資訊(LS_BAB table)
                $("#po1").on("keyup", function () {
                    var text = $(this).val().trim().toLocaleUpperCase();
                    $(this).val(text);
                    getBAB(text, saveline, $step3_objs);
//                    console.log(JSON.stringify(obj));
                    $.removeCookie("user_sel");
                    $("#step4").html("");
                });

                //當站別1繼續投下一套工單時,給予人數輸入
                $("#po").keydown(function () {
                    $("#people").removeAttr("disabled");
                });

                //站別1投入工單按鈕
                $("#begin").click(function () {

                    console.log();
                    var po = $("#po").val().trim();
                    var modelname = $("#modelname").val().trim();
                    var line = $("#lineselect").val();
                    var people = $("#people").val();
                    if (modelname == 'data not found' || modelname == "" || po == "" || line == -1) {
                        $("#servermsg").html("請確認資料是否正確");
                        return false;
                    }
                    if (parseInt(people) <= 0 || parseInt(people) > 5 || people == "") {
                        $("#servermsg").html("人數範圍錯誤");
                        return false;
                    }
                    if (line == -1) {
                        line = saveline;
                    }
                    if (!confirm("工單:" + po + " 機種:" + modelname + " 人數:" + people + " \n確認無誤?")) {
                        return false;
                    }
                    var obj = toback(po, modelname, line, people, null, "insert");
                    if (obj.status == "success") {
                        $.cookie("people", people);
                        reload();
                    } else {
                        $("#servermsg").html(obj.status);
                    }


                });

                //工單最後一個站別關閉工單用
                $("#end").on("click", function () {
                    var po, babid, line;
                    if (po_search_result != null) {
                        var obj = JSON.parse(po_search_result);
                        po = obj.PO;
                        babid = obj.id;
                        line = obj.line;
                    }
                    if (confirm("結束工單號碼 " + po + "?")) {
                        var obj = toback(po, -1, line, -1, babid, "delete");
                        if (obj.status == "success") {
                            $.removeCookie("po_search_result");
                            reload();
                        } else {
                            $("#servermsg").html(obj.status);
                        }
                    }
                });

                //中間站別結束sensor用
                $("#s_end").on("click", function () {
                    var sensor;
                    var BABid;
                    if (po_search_result != null) {
                        var obj = JSON.parse(po_search_result);
                        BABid = obj.id;
                    }
                    if (user_sel != null) {
                        var obj = JSON.parse(user_sel);
                        sensor = obj.step3_sel;
                    }
                    if (!confirm("確定結束Sensor?")) {
                        return;
                    }
                    if (sensor != null && BABid != null) {
                        $.ajax({
                            type: "Post",
                            url: "StopSensor",
                            data: {
                                BABid: BABid,
                                sensor: sensor,
                                line: saveline
                            },
                            dataType: "json",
                            success: function (response) {
                                var obj = response.servermsg;
                                $("#servermsg").html(
                                        "<p>檢查前顆感應器是否已經結束:" +
                                        (obj.history ? "是" : "否") +
                                        "</p><p>檢查統計值:" +
                                        (obj.total ? "有統計數值" : "空的(sensor有問題?)") +
                                        "</p><p>是否已經關閉感應器:" +
                                        (obj.do_sensor_end ? "是" : "否") +
                                        "</p>");
                            },
                            error: function () {
                                $("#servermsg").html("error");
                            }
                        });
                    }
                });

                //第一步登入用(卡站別1只允許1個人進入)
                $("#step1next").click(function () {
                    var line = $("#lineselect").val();
                    var linetype = $("#lineselect option:selected").text().trim();
//                    console.log(line);
                    if (line == -1) {
                        return false;
                    } else if (!confirm("※確定您選擇的線別為 " + linetype + " ?")) {
                        return false;
                    }

                    if ($("#isfirst").is(":checked")) {
                        var id = 1;
                        var msg = linelogin();
                        //getdata there
                        if (msg == "success") {
                            if (jsonstring == null) {
                                var json = {
                                    line: line,
                                    identit: id,
                                    userSitefloorSelect: $("#userSitefloorSelect").val()
                                };
                                var date = new Date();
                                var minutes = 12 * 60;
                                date.setTime(date.getTime() + (minutes * 60 * 1000));
                                $.cookie("babinfo", JSON.stringify(json), {expires: date});
                                generateTagCookie(1, linetype);
                            }
                            reload();
                        }
                    } else {
                        $("#searchpeople").show();
                        $("#step2").show();
                        if (jsonstring == null) {
                            var json = {
                                line: line,
                                identit: id,
                                userSitefloorSelect: $("#userSitefloorSelect").val()
                            };
                            var date = new Date();
                            var minutes = 12 * 60;
                            date.setTime(date.getTime() + (minutes * 60 * 1000));
                            $.cookie("babinfo", JSON.stringify(json), {expires: date});
                        }
                        reload();
                    }

                });

                //離開站別1(順便clear cookie)
                $("#exitT1").click(function () {
                    if (linelogout() == "success") {
                        clearcookie();
                        reload();
                    }
                });

                //後面站別登入用(站別選擇)
                $("body").on("click", "#T_Button :button", function () {
                    var val = $(this).val();
                    var linetype = $("#lineselect option:selected").text().trim();
                    generateTagCookie(val, linetype);
                    reload();
                });

                //後面站別登出用
                $("#clearcookie").on("click", function () {
                    if (confirm("確定返回步驟1?")) {
                        clearcookie();
                        reload();
                    }
                });

                //將進行中的工單第一個做標記，讓使用者知道目前亮的是哪一張工單的燈號
                if ($("#linestate").children().length > 0) {
                    $("#linestate").children().eq(0).attr("style", "border-color:red; border-style:solid;");
                }

                if ($.cookie('table')) {
                    $(":input,select").not("#redirectBtn").attr("disabled", "disabled");
                    $("#servermsg").html("您已經登入測試");
                }

                function textBoxToUpperCase(obj) {
                    obj.val(obj.val().trim().toLocaleUpperCase());
                }

                //儲存使用者站別cookie
                function generateTagCookie(station, linetype) {
                    var tagname = linetype + "-S-" + station;
                    var obj = {
                        step3_sel: station,
                        sensor_tagname: tagname
                    };
                    $.cookie("user_sel", JSON.stringify(obj));
                }

                //依照工單查詢結果產生button(LS_BAB人數)
                function generatebutton(amount) {
                    var text = "";
                    for (var i = 2; i <= amount; i++) {
                        text += "<input type='button' id='T" + i + "' class='btn btn-default' value='" + i + "'>";
                    }
                    return text;
                }

                function clearcookie() {
                    var cookies = $.cookie();
                    for (var cookie in cookies) {
                        $.removeCookie(cookie);
                    }
                }
                
                var STATION1_LOGIN = true, STATION1_OUT = false;

                //站別1登入
                function linelogin() {
                    //通過後台測試 回傳直
                    linevalue = $("#lineselect").val();
                    var people = $("#people").val();
                    var flag = saveIdentit(linevalue, people, STATION1_LOGIN);
                    return flag;
                }

                //站別1登出
                function linelogout() {
                    linevalue = $("#lineselect").val();
                    if (confirm('確定離開T1?')) {
                        var flag = saveIdentit(linevalue, 0, STATION1_OUT);
                        $.removeCookie("servermsg");
                        return flag;
                    }
                }

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
                                },
                                error: function () {
                                    $("#servermsg").html("error");
                                }
                            });
                        }, 1000);
                    } else {
                        obj.val("");
                    }
                }

                //取得LS_BAB table
                function getBAB(text, saveline, objs) {
                    $("#step3").html("");
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
//                                    console.log(response);
                                    var obj = JSON.parse(response);
                                    if (obj != null) {
                                        $("#step3").html(objs.eq(1));
                                        $("#step3 #T_Button").html(generatebutton(obj.people));
                                        var state = obj.S_State;
                                        if (state != null) {
                                            for (var i = 2; i <= obj.people; i++) {
                                                var T = "T" + i;
                                                if (state[T] == 1) {
                                                    $("#T_Button #" + T).attr("disabled", "disabled");
                                                }
                                            }
                                        }
                                    }
                                    $.cookie("po_search_result", JSON.stringify(obj));
                                    $("#servermsg").html(obj == null ? "找不到工單資料" : "找到資料");
                                },
                                error: function () {
                                    $("#servermsg").html("error");
                                }
                            });
                        }, 1000);
                    } else {
                        $("#servermsg").val("");
                    }
                }

                //和伺服器確認該線別是否已經開線，以true(登入)false(登出)做區隔
                function saveIdentit(linevalue, people, FLAG) {
                    var result;
                    if (linevalue != -1) {
                        $.ajax({
                            type: "Post",
                            url: "LineLogin",
                            async: false,
                            data: {
                                lineNo: linevalue,
                                people: people,
                                action: FLAG
                            },
                            dataType: "html",
                            success: function (response) {
                                //傳回來 success or fail
                                $("#servermsg").html(response);
                                result = response;
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                $("#servermsg").html(xhr.responseText);
                            }
                        });
                    }
                    return result;
                }

                //向伺服器取得該線別目前進行中的工單
                function getdata(saveline) {
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
                            $("#servermsg").html("error");
                        }
                    });
                    return obj;
                }

                $("#refresh").on("click", function () {
                    reload();
                });
            });

//儲存、結束工單
            function toback(po, modelname, line, people, id, action) {
                $("#servermsg").html("");
                var obj;
                $.ajax({
                    type: "Post",
                    url: "SaveBABInfo",
                    async: false,
                    data: {
                        po: po,
                        modelname: modelname,
                        lineNo: line,
                        people: people,
                        id: id,
                        action: action
                    },
                    dataType: "json",
                    success: function (response) {
                        obj = response;
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        $("#servermsg").html(xhr.responseText);
                    }
                });
                return obj;
            }
            function reload() {
                $(":button,select,:checkbox").removeAttr("disabled");
                window.location.reload();
            }
        </script>
    </head>
    <body>
        <input id="userSitefloorSelect" type="hidden" value="${userSitefloor}">
        <div id="titleAlert">
            <c:out value="您所選擇的樓層是: ${userSitefloor}" />
            <a href="index.jsp">
                <button id="redirectBtn" >不是我的樓層?</button>
            </a>
        </div>
        <jsp:include page="head.jsp" />
        <div style="clear:both"></div>
        <div id="step1">
            <!--判斷該線別有無使用人-->
            <div class="Div0">
                <div class="Div1">
                    <div id="step1obj" class="form-inline">
                        <select style="text-align: center" id="lineselect">
                            <option value="-1">---請選擇線別---</option>
                            <c:forEach var="lines" items="${lineDAO.getLine(userSitefloor)}">
                                <option value="${lines.id}" ${lines.lock == 1 ? "disabled style='opacity:0.2'" : ""}>${lines.name}</option>
                            </c:forEach>
                        </select>
                        <div class="checkbox">
                            <label><input type="checkbox" id="isfirst">我是站別1</label>
                        </div>
                        <input type="button" id="step1next" value="下一步">
                        <div style="color: green;font-weight: bold;padding-left: 10px">
                            ※完成後請到<a href="http://172.20.131.208/Line_Balancing/Login.aspx" target="_blank">這裡</a>填寫您工單相關的SOP
                        </div>
                    </div>
                </div>
                <div class="Div2">
                    <p><h3>步驟1:</h3>請選擇您的線別。</p>
                </div>
            </div>
        </div>

        <div id="step2" hidden="hidden">
            <!--判斷該線別有無使用人-->
            <div class="Div0">
                <div class="Div1">
                    <div class="form-inline ">
                        <div class="form-group">
                            <input type="text" name="po" id="po1" placeholder="請輸入工單號碼" autocomplete="off">  
                        </div>
                        <input type="button" id="clearcookie" value="返回步驟1">
                    </div>
                </div>
                <div class="Div2">
                    <p><h3>步驟2:</h3>請輸入工單號碼</p>
                </div>
            </div>
        </div>

        <!--*******************************************************************************************************************---->
        <div id="step3">
            <div id="type1">
                <div id="totaldata">
                    <div class="Div0">
                        <div class="Div1">
                            <div class="form-inline ">
                                <div id="linename" class="form-group"></div>
                                <input type="text" name="po" id="po" placeholder="請輸入工單號碼" autocomplete="off">  
                                <input type="text" name="modelname" id="modelname" placeholder="機種" readonly style="background: #CCC">
                                <select id="people" style="text-align: center">
                                    <option value="-1">請輸入人數</option>
                                    <c:forEach var="i" begin="2" end="4">
                                        <option value="${i}">${i}</option>
                                    </c:forEach>
                                </select>
                                <input type="button" value="開始" id="begin">
                                <lable for="manuallyModelNameInput"><input id="manuallyModelNameInput" type="checkbox" />手動輸入工單模式</lable>
                            </div>
                            <div style="padding:5px 5px">
                                <input type="button" id="exitT1" value="返回步驟1">
                            </div>
                        </div>
                        <div class="Div2">
                            <h3>步驟2:</h3>
                            <p>輸入工單相關資訊之後按下<code>開始</code>，投工單。</p>
                            <p>成功後可到<code>進行中的工單</code>確認相關資訊</p>
                        </div>
                    </div>
                </div>
            </div>

            <div id="type2">
                <!--判斷該線別有無使用人-->
                <div class="Div0">
                    <div id="T_Button" class="Div1">
                    </div>
                    <div class="Div2">
                        <p><h3>步驟3:</h3>請選擇站別。</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="step4">
        <!--判斷該線別有無使用人-->
        <div class="Div0">
            <div class="Div1">
                <div class="form-inline ">
                    <div class="form-group">
                        <input type="button" class="btn btn-default" value="結束按鈕">
                    </div>
                </div>
            </div>
            <div class="Div2">
                <p><h3>步驟4:</h3>完成<code>最後一台</code>機子時，請按下結束按鈕</p>
            </div>
        </div>
    </div>
    <!--*******************************************************************************************************************---->
    <div id="msg_step" class="Div0 ">
        <div class="Div1"><div id="servermsg"></div></div>
        <div class="Div2"><p><h3>伺服器訊息</h3>此處會顯示伺服器訊息。</p></div>
    </div>
    <div id="final_step" class="Div0 ">
        <div id="cookieinfo" class="Div1"></div>
        <div class="Div2">
            <p><h3>完成:</h3>恭喜完成資料儲存</p>
        </div>
    </div>
    <div id="statusstep" class="Div0 ">
        <div id="linestate" style="clear:both" class="bg-success Div1"></div>
        <div class="Div2">
            <h3>進行中的工單:</h3>
            <p>開始人(第一顆sensor)輸入的所有工單</p>
            <p>左邊紅框處為目前警示燈警示的工單號碼</p>
        </div>
    </div>
    <div id="sensordata" hidden>
        <div id="div1">工單最後一站人員，下班前請不要忘記在<code>步驟4</code>關閉工單，謝謝您。</div>
        <div id="div2"></div>
    </div>
    <div id="hintmsg" style="color:red;font-weight: bold;padding-left: 10px">
        <p>※第一站人員請先Key入相關資料再把機子放到定位(否則會少一台紀錄)</p>
        <p>機子擋住Sensor即開始計時，休息時間的操作不列入計算範圍之內。</p>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>
