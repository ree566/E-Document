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
    $(document).ajaxSuccess(function () {
        $.unblockUI();//Unblock the ajax when success
    });


    $('[data-toggle="tooltip"]').tooltip();

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
    $objgroup = $("#po,#line,#people,#begin");

    //抓取cookie所儲存的json data讓其他元件做應對
    if (jsonstring != null) {
        $obj = $.parseJSON(jsonstring);
        if ($obj != null) {
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
                        $("#lineselect,#step1next").attr("disabled", true);
                    }
                }
            }
            else {
                if ($obj != null) {
                    $("#lineselect").children().eq($obj.line).attr("selected", true);
                    $("#lineselect,#step1next").attr("disabled", true);
                    $("#step2").show();
                    $("#isfirst").attr("disabled", "disabled");
                }
            }
            var json = getdata(saveline);
            if (json != null) {
                var obj;
                $("#end_line").html("");
                $("#linestate").html("");
                $(".sensorend").html("");
                for (var i = 0; i < json.BABData.length; i++) {
                    obj = JSON.parse(json.BABData[i]);
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
                    sArr[sArr.length] = obj.Model_name;
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
        $("#step4 :button").attr("id", isLastStation ? "end" : "s_end").val("結束按鈕" + isLastStation ? "(儲存線平衡紀錄)" : "");
        $("#step4").show();

        $("#sensordata #div2").html("<iframe style='width:100%; height:80px' scrolling='no' src='Sensor'></iframe>");//Show the sensor time when user is inline.

        $("#sensordata").slideToggle();
    }

    //抓取和伺服器提出開關線別所回傳的值(存在cookie中)
    if ($.cookie('people') != null) {
        $("#people").children().eq($.cookie('people') - 1).attr("selected", true);
        $("#people").attr("disabled", true);
    }

    //Search the ModelName by PO.(Station 1)
    $("#po").on("keyup", function () {
        var text = $(this).val().trim().toLocaleUpperCase();
        $(this).val(text);
        getModel(text, "#modelname");
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
        if (!$.cookie('table')) {
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
            if(!confirm("工單:" + po + " 機種:" + modelname + " 人數:" + people + " \n確認無誤?")){
                return false;
            }
            var obj = toback(po, modelname, line, people, null, 1);
            if (obj.status == "success") {
                $.cookie("people", people);
                reload();
            } else {
                $("#servermsg").html(obj.status);
            }

        } else {
            $("#servermsg").html("您已經登入包裝");
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
            var obj = toback(po, -1, line, -1, babid, 0);
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
        console.log("btn click");
        var line = $("#lineselect").val();
        var type = $("#lineselect option:selected").text().trim();
//                    console.log(line);
        if (line == -1) {
            return;
        }
        if ($("#isfirst").is(":checked")) {
            var id = 1;
            var msg = linelogin();
            //getdata there
            if (msg == "success") {
                if ($.cookie('babinfo') == null) {
                    var json = {
                        line: line,
                        identit: id
                    };
                    var date = new Date();
                    var minutes = 12 * 60;
                    date.setTime(date.getTime() + (minutes * 60 * 1000));
                    $.cookie("babinfo", JSON.stringify(json), {expires: date});
                    generateTagCookie(1, type);
                }
                reload();
            }
        } else {
            $("#searchpeople").show();
            $("#step2").show();
            if ($.cookie('babinfo') == null) {
                var json = {
                    line: line,
                    identit: id
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

    //站別1登入
    function linelogin() {
        //通過後台測試 回傳直
        linevalue = $("#lineselect").val();
        var people = $("#people").val();
        var flag = saveIdentit(linevalue, people, "TRUE");
        return flag;
    }

    //站別1登出
    function linelogout() {
        linevalue = $("#lineselect").val();
        if (confirm('確定離開T1?')) {
            var flag = saveIdentit(linevalue, 0, "FALSE");
            $.removeCookie("servermsg");
            return flag;
        }
    }

    //取得機種
    function getModel(text, appendTo) {
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
                        $(appendTo).val(response);
                    },
                    error: function () {
                        $("#servermsg").html("error");
                    }
                });
            }, 1000);
        } else {
            $(appendTo).val("");
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
                    lineno: linevalue,
                    people: people,
                    Flag: FLAG
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
function toback(po, modelname, line, people, id, type) {
    $("#servermsg").html("");
    var obj;
    $.ajax({
        type: "Post",
        url: "SaveBABInfo",
        async: false,
        data: {
            po: po,
            modelname: modelname,
            line: line,
            people: people,
            id: id,
            type: type
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