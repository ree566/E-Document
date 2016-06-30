<%-- 
    Document   : getdata
    Created on : 2015/8/26, 上午 11:23:38
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="testDAO" class="com.advantech.model.TestDAO" scope="application" />
<!DOCTYPE html>
<html>
    <head> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <style>
            .Div0{
                float:left; width:100%; 
                border-bottom-style:dotted;
                background-color: window;
            }
            .Div1{
                float:left; width:50%; 
                padding: 10px 10px;
            }
            .Div2{
                float:right;width:50%;
                padding-left: 10px;
            }

        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/jquery.cookie.js"></script>
        <script src="js/jquery.blockUI.js"></script>
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

                //Prevent submit the form repeatly.
                $(document).ajaxSend(function () {
                    block();
                });
                $(document).ajaxSuccess(function () {
                    $.unblockUI();
                });

                getTestInfo();

                //Add class to transform the button type to bootstrap.
                $(":button").addClass("btn btn-default");
                $(":text,select,input[type='number']").addClass("form-control");


                var tabreg = /^\d+$/;//Textbox check regex.

                $objgroup = $("#user_number,#table,#begin");
                var tablecookie = $.cookie('table');
                $("#cookieinfo").html(tablecookie != null ? "測試 cookie 已經儲存" : "尚無資料");

                //Get values from cookie and setting html objects.
                if (tablecookie != null) {
                    $objgroup.attr("disabled", true);
                    $("#table").children().eq(tablecookie).attr("selected", true);
                    $("#end").removeAttr("disabled");
                } else {
                    $objgroup.removeAttr("disabled");
                    $("#end").attr("disabled", true);
                }
                $objgroup = null;
                if ($("#tableuno").length == 0 && $.cookie('table')) {
                    $("#clearcookies").removeAttr("disabled");
                }

                //Checking if user is login the babpage.jsp or not.(Get the cookie generate by babpage.jsp)
                //If not, login and check the user input values.
                $("#begin").click(function () {
                    if (!$.cookie('babinfo')) {
                        var usnumber = $("#user_number").val().trim();
                        var table = $("#table").val().trim();
                        if (!table.match(tabreg)) {
                            $("#servermsg").html("error input value");
                            return false;
                        }
                        $.ajax({
                            type: "Post",
                            url: "SaveTestInfo",
                            data: {
                                user_number: usnumber.trim(),
                                table: table.trim()
                            },
                            dataType: "html",
                            success: function (response) {
                                $("#servermsg").html(response);
                                if (response == "success") {
                                    loaddiv();
                                }
                            },
                            error: function () {
                                $("#servermsg").html("error");
                                loaddiv();
                            }
                        });
                    } else {
                        $("#servermsg").html("您已經登入組裝");
                    }
                });

                //TestTable logout.(Delete data from database)
                $("#end").click(function () {
                    var usnumber = $("#user_number").val().trim();
//                    var usname = $("#user_name").val();
                    var table = $("#table").val().trim();
                    if (usnumber != "" && table != "") {
                        if (!table.match(tabreg)) {
                            $("#servermsg").html("error input value");
                            return false;
                        }
                    }
                    $.ajax({
                        type: "Post",
                        url: "SaveTestInfo",
                        data: {
                            remove_user_number: usnumber.trim(),
                            remove_table: table.trim()
                        },
                        dataType: "html",
                        success: function (response) {
                            $("#servermsg").html(response);
                            if (response == "success") {
                                $("#user_number,#table,#begin").removeAttr("disabled");
                            }
                            $(":text").val("");
                            loaddiv();
                        },
                        error: function () {
                            $("#servermsg").html("error");
                            loaddiv();
                        }
                    });
                });

            });
            function loaddiv() {
                window.location.reload();
            }

            //Logout the user saving cookie.
            function deleteAllCookies() {
                var cookies = document.cookie.split(";");
                for (var i = 0; i < cookies.length; i++) {
                    var cookie = cookies[i];
                    var eqPos = cookie.indexOf("=");
                    var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
                    document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
                    loaddiv();
                }
            }

            function getTestInfo() {
                $.ajax({
                    type: "Post",
                    url: "TestTableStatus",
                    dataType: "json",
                    success: function (response) {
                        $("#tableUseStatus, #userInfo").html("");
                        var saveUserNumber = $.cookie("user_number");
                        var saveTable = $.cookie("table");
                        for (var i = 0, j = response.length; i < j; i++) {
                            var obj = response[i];
                            $("#tableUseStatus").append("NO:<b class='tableisused'>" + obj.id + "、 </b>");

                        }
                        if (saveTable != null && saveUserNumber != null) {
                            $("#userInfo").append("<td id='tableno'>" + saveTable + "</td>");
                            $("#userInfo").append("<td id='tableuno'>" + saveUserNumber + "</td>");
                        }
                    },
                    error: function () {
                        $("#servermsg").html("error");
                    }
                });
            }
        </script>
    </head>
    <body>
        <jsp:include page="head.jsp" />
        <c:forEach items="${cookie}" var="currentCookie">  
            <c:if test="${currentCookie.value.name eq 'user_number'}">
                <c:set var="key" value="${currentCookie.value.value}" />
            </c:if>
            <c:if test="${currentCookie.value.name eq 'table'}">
                <c:set var="tb" value="${currentCookie.value.value}" />
            </c:if>
        </c:forEach>
        <div class="Div0" id="inputpannel">
            <div class="Div1 form-inline">
                <input type="text" placeholder="刷入工號" id="user_number" ${key == null ?"":"disabled"} value="${key == null ?"":key}">
                <select id="table" ${key == null ?"":"disabled"} value="${tb == null ?"":tb}">
                    <option value="-1">請選擇桌次</option>
                    <c:forEach var="tab" items="${testDAO.desk}">
                        <option value="${tab.id}">${tab.name}</option>
                    </c:forEach>
                </select>
                <input type="button" value="開始" id="begin" ${key == null ?"":"disabled"}>
                <input type="button" value="結束" id="end">
                <input type="button" value="清除cookie" onclick="deleteAllCookies()" id="clearcookies" ${key != null ?"":"disabled"}>
            </div>
            <div class="Div2">
                <p><h3>步驟1:</h3>先在此處輸入您的<code>桌次</code>以及<code>工號</code>，確認之後按下開始。</p>
            </div>
        </div>
        <div class="Div0"> 
            <div class="Div1">
                <font id="smsg"><伺服器訊息:></font>
                <div id="servermsg"></div>  
            </div>
            <div class="Div2">
                <p><h3>步驟2:</h3>觀看伺服器訊息。</p>
            </div>
        </div>
        <div style="clear:both"></div>
        <div id="totaldata">
            <div class="Div0">
                <div class="Div1">
                    <table class="table table-bordered" style="text-align: center">
                        <tr>
                            <th>桌號</th>
                            <th>使用者代號</th>
                        </tr>
                        <tr id="userInfo">
                        </tr>
                        <c:remove var="key" />
                        <c:remove var="tb" />
                    </table>
                </div>
                <div class="Div2">
                    <h4>此處會紀錄您刷入的工號以及桌號。</h3>
                </div>
            </div>
            <div class="Div0">
                <div class="Div1">目前已使用中的桌次:
                    <p id="tableUseStatus">
                    </p>
                </div>
                <div class="Div2">
                    <p><h3>步驟3:</h3>此處會顯示已經刷入成功的桌次，請確認您的桌次是否在其中。</p>
                </div>
            </div>
            <div class="Div0">
                <div class="Div1">未刷入資料庫的使用者:
                    <p>
                        <c:set var="i" value="0"/>
                        <c:forEach var="user" items="${testDAO.peopleNotInDB}">  
                            ${user.key} 、
                            <c:set var="i" value="${i + 1}"/>
                            <c:choose>
                                <c:when test="${i % 9 == 0}"><br/></c:when>
                            </c:choose>
                        </c:forEach>
                    </p>
                </div>
                <div class="Div2">
                    <p><h3>此處會顯示未刷入資料庫的使用者。</h3>刷入成功者請再次確認您的<code>姓名</code>不在這欄位中。<p/>
                </div>
            </div>
            <div class="Div0">
                <div id="cookieinfo" class="Div1"></div>
                <div class="Div2">
                    <p><h3>完成:</h3>恭喜完成資料儲存</p>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
