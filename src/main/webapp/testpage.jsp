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
    <c:set var="userSitefloor" value="${param.sitefloor}" />
    <c:if test="${(userSitefloor == null) || (userSitefloor == '')}">
        <c:redirect url="/" />
    </c:if>
    <head> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <style>
            #titleAlert{
                background-color: green;
                color: white;
                text-align: center;
            }
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
        <script src="js/jquery.blockUI.Default.js"></script>
        <script src="js/cookie.check.js"></script>
        <script src="js/param.check.js"></script>
        <script>
            var userInfoCookieName = "userInfo", testLineTypeCookieName = "testLineTypeCookieName";
            var STATION_LOGIN = "LOGIN", STATION_LOGOUT = "LOGOUT";

            $(document).ready(function () {

                if (!are_cookies_enabled()) {
                    alert(cookie_disabled_message);
                    return;
                }

                //Add class to transform the button type to bootstrap.
                $(":button").addClass("btn btn-default");
                $(":text,select,input[type='number']").addClass("form-control");
                
                console.log($.cookie(testLineTypeCookieName));
                var cookieCheckStatus = checkExistCookies();
                if (cookieCheckStatus == false) {
                    return false;
                }

                //Checking if user is login the babpage.jsp or not.(Get the cookie generate by babpage.jsp)
                //If not, login and check the user input values.
                $("#begin").click(function () {
                    LoginOrLogoutTestLineType(STATION_LOGIN);
                });

                //TestTable logout.(Delete data from database)
                $("#end").click(function () {
                    LoginOrLogoutTestLineType(STATION_LOGOUT);
                });

                $(document).on("keyup", "#user_number", function () {
                    textBoxToUpperCase($(this));
                });

            });

            function checkExistCookies() {
                $("#cookieinfo").html("尚無資料");
                var testLineTypeCookie = $.cookie(testLineTypeCookieName);
                var babLineTypeCookie = $.cookie(userInfoCookieName);

                if (babLineTypeCookie) {
                    lockAllUserInput();
                    showMsg("您已經登入組包裝");
                    return false;
                }

                //Get values from cookie and setting html objects.
                if (testLineTypeCookie != null) {
                    var cookieInfo = $.parseJSON(testLineTypeCookie);
                    if (cookieInfo.floor == $("#userSitefloorSelect").val()) {
                        $("#user_number").val(cookieInfo.userNo);
                        $("#table").val(cookieInfo.tableNo);
                        lockWhenUserIsLogin();
                        $("#cookieinfo").html("測試 cookie 已經儲存");
                        $("#userInfo").html("<td>" + cookieInfo.tableNo + "</td>" + "<td>" + cookieInfo.userNo + "</td>");
                        return true;
                    } else {
                        lockAllUserInput();
                        showMsg("您已經登入其他樓層");
                        return false;
                    }
                } else {
                    unlockLoginInput();
                    return true;
                }
            }

            function lockAllUserInput() {
                $(":input,select").not("#redirectBtn").attr("disabled", "disabled");
            }

            function LoginOrLogoutTestLineType(action) {
                var userNo = $("#user_number").val();
                var tableNo = $("#table").val();
                var data = {
                    userNo: userNo,
                    tableNo: tableNo
                };

                console.log(data);

                if (!checkVal(data.tableNo)) {
                    showMsg("error input value");
                    return false;
                }

                data.floor = $("#userSitefloorSelect").val();
                data.action = action;

                $.ajax({
                    type: "Post",
                    url: "SaveTestInfo",
                    data: data,
                    dataType: "html",
                    success: function (response) {
                        if (response == "success") {
                            if (action == STATION_LOGIN) {
                                generateCookie(testLineTypeCookieName, JSON.stringify(data));
                            } else if (action == STATION_LOGOUT) {
                                $("#user_number,#table,#begin").removeAttr("disabled");
                                $(":text").val("");
                                removeAllStepCookie();
                            }
                            reload();
                        } else {
                            showMsg(response);
                        }
                    },
                    error: function () {
                        showMsg("error");
                        reload();
                    }
                });
            }

            function lockWhenUserIsLogin() {
                $("#user_number,#table,#begin").attr("disabled", true);
                $("#end").removeAttr("disabled");
            }

            function unlockLoginInput() {
                $("#user_number,#table,#begin").removeAttr("disabled");
                $("#end").attr("disabled", true);
            }

            function showTestInfo() {
                console.log($.cookie(testLineTypeCookieName));
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

            //Logout the user saving cookie.
            function removeAllStepCookie() {
                $.removeCookie(testLineTypeCookieName);
            }

            function reload() {
                window.location.reload();
            }

            function showMsg(msg) {
                $("#servermsg").html(msg);
            }
        </script>
    </head>
    <body>
        <input id="userSitefloorSelect" type="hidden" value="${userSitefloor}">
        <div id="titleAlert">
            <c:out value="您所選擇的樓層是: ${userSitefloor}" />
            <a href="index.jsp">
                <button id="redirectBtn">不是我的樓層?</button>
            </a>
        </div>
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
                    <c:forEach var="tab" items="${testDAO.getDesk(userSitefloor)}">
                        <option value="${tab.id}">${tab.name}</option>
                    </c:forEach>
                </select>
                <input type="button" value="開始" id="begin" ${key == null ?"":"disabled"}>
                <input type="button" value="結束" id="end">
                <input type="button" value="清除cookie" onclick="removeAllStepCookie()" id="clearcookies" ${key != null ?"":"disabled"}>
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
