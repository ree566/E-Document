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
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <style>
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
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/jquery.cookie.js"></script>
        <script src="js/jquery.blockUI.js"></script>
        <script src="js/babpage.js"></script>
    </head>
    <body>
        <script>
            block();
        </script>
        <jsp:include page="head.jsp" />
        <div style="clear:both"></div>
        <div id="step1">
            <!--判斷該線別有無使用人-->
            <div class="Div0">
                <div class="Div1">
                    <div id="step1obj" class="form-inline">
                        <select style="text-align: center" id="lineselect">
                            <option value="-1">---請選擇線別---</option>
                            <c:forEach var="lines" items="${lineDAO.line}">
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
    <script>
        $(window).load(function () {
            $.unblockUI();
        });
    </script>
</body>
</html>
