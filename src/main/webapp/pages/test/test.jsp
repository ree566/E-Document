<%-- 
    Document   : abc
    Created on : 2017/5/5, 下午 03:20:39
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="user" property="principal" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Admin page</title>
        <link href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />" rel="stylesheet">
        <link href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/themes/ui-lightness/jquery-ui.css" rel="stylesheet">
        <style>
            th{
                color: red;
            }
            .highlight{
                color: lightsalmon;
                font-weight: bold;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js" />"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
        <script src="https://jqueryfiledownload.apphb.com/Scripts/jquery.fileDownload.js"></script>

        <script>
            $(function () {
                var mathType;

                function formatFloat(num, pos) {
                    var size = Math.pow(10, pos);
                    switch (mathType) {
                        case "round":
                            return Math.round(num * size) / size;
                        case "floor":
                            return Math.floor(num * size) / size;
                        case "ceil":
                            return Math.ceil(num * size) / size;
                        case "original":
                            return num;
                        default:
                            return null;
                    }
                }

                var equipmentDepreciationExpenses = function () {
                    return $("#depreciationExpense").val() / $("#avgHour").val();
                };

                var plantFloorAreaUse = function () {
                    return $("#managementFee").val() / $("#avgHour").val();
                };

                var electricityCosts = function () {
                    return $("#consumptionDegreeTime").val() * $("#electricityFeeDegree").val();
                };

                var costCalc = function () {
                    var loadTime = $("#loadTime").val();
                    var useMinuteTime = $("#useMinuteTime").val();
                    var frequency = $("#frequency").val();
                    var cost = (equipmentDepreciationExpenses() * useMinuteTime * frequency / 60 / loadTime) +
                            (plantFloorAreaUse() * useMinuteTime * frequency / 60 / loadTime) +
                            (electricityCosts() * frequency / loadTime);
                    $("#cost").html(formatFloat(cost, 2));

                    $("#worktime").html(formatFloat((cost / 700 * 60), 2));
                };

                var costFormulaCalc = function () {
                    $("#equipmentDepreciationExpenses").val(formatFloat(equipmentDepreciationExpenses(), 2));
                    $("#plantFloorAreaUse").val(formatFloat(plantFloorAreaUse(), 2));
                    $("#electricityCosts").val(formatFloat(electricityCosts(), 2));
                };

                var initMathType = function () {
                    mathType = $("input[name=formatFloatType]:checked").val();
                };

                initMathType();
                costFormulaCalc();

                $("input[type=number]").on("keyup change", function () {
                    costFormulaCalc();
                    costCalc();
                });
                
                $("input[name=formatFloatType]").on("change", function(){
                    initMathType();
                    costFormulaCalc();
                    costCalc();
                });

                $("input[type=number]").first().trigger("keyup");
            });
        </script>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <table class="table table-bordered">
                    <tr>
                        <th colspan="3">一、壓力鍋成本工時計算</th>
                    </tr>
                    <tr>
                        <td>負載量/次</td>
                        <td>使用分鐘/次</td>
                        <td>次數</td>
                    </tr>
                    <tr class="warning">
                        <td>
                            <input type="number" id="loadTime" class="form-control" placeholder="負載量/次" value="15" min="1" />
                        </td>
                        <td>
                            <input type="number" id="useMinuteTime" class="form-control" placeholder="使用分鐘/次" value="60" min="1" />
                        </td>
                        <td>
                            <input type="number" id="frequency" class="form-control" placeholder="次數" value="2" min="1" />
                        </td>
                    </tr>
                    <tr>
                        <td>費用</td>
                        <td colspan="2">
                            <div id="cost"></div>
                        </td>
                    </tr>
                    <tr>
                        <td>換算工時(min)</td>
                        <td colspan="2">
                            <div id="worktime"></div>
                        </td>
                    </tr>
                    <tr>
                        <td>小數點格式</td>
                        <td colspan="2">
                            <div class="custom-control custom-checkbox">
                                <input type="radio" id="round" class="form-check-input" name="formatFloatType" value="round" checked />
                                <label for="round">Round</label>
                                <input type="radio" id="floor" class="form-check-input" name="formatFloatType" value="floor" />
                                <label for="floor">Floor</label>
                                <input type="radio" id="ceil" class="form-check-input" name="formatFloatType" value="ceil" />
                                <label for="ceil">Ceil</label>
                                <input type="radio" id="original" class="form-check-input" name="formatFloatType" value="original" />
                                <label for="original">Original</label>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="row">
                <table class="table table-bordered table-sm">
                    <tr>
                        <th colspan="8">二、壓力鍋成本計算公式</th>
                    </tr>
                    <tr>
                        <td><b>壓力鍋成本=</b></td>
                        <td class="highlight">設備折舊費用</td>
                        <td class="highlight">+</td>
                        <td class="highlight">廠房樓板面積使用</td>
                        <td class="highlight">+</td>
                        <td class="highlight">電力費用</td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="highlight">設備折舊費用=</td>
                        <td>壓力鍋折舊費用/月</td>
                        <td>÷</td>
                        <td>平均攤提時數</td>
                        <td>×</td>
                        <td>使用時數</td>
                        <td>÷</td>
                        <td>負載量</td>
                    </tr>
                    <tr>
                        <td>=</td>
                        <td>
                            <input type="text" id="depreciationExpense" class="form-control" placeholder="壓力鍋折舊費用/月" value="8704" readonly="" />
                        </td>
                        <td>÷</td>
                        <td>
                            <input type="text" id="avgHour" class="form-control" placeholder="平均攤提時數a" value="40.07" readonly="" />
                        </td>
                        <td>×</td>
                        <td>使用時數</td>
                        <td>÷</td>
                        <td>負載量</td>
                    </tr>
                    <tr>
                        <td>=</td>
                        <td>
                            <input type="text" id="equipmentDepreciationExpenses" class="form-control" readonly="" />
                        </td>
                        <td>×</td>
                        <td>使用時數</td>
                        <td>÷</td>
                        <td>負載量</td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="highlight">廠房樓板面積使用=</td>
                        <td>租金+管理費</td>
                        <td>÷</td>
                        <td>平均攤提時數</td>
                        <td>×</td>
                        <td>使用時數</td>
                        <td>÷</td>
                        <td>負載量</td>
                    </tr>
                    <tr>
                        <td>=</td>
                        <td>
                            <input type="text" id="managementFee" class="form-control" placeholder="租金+管理費" value="600" readonly="" />
                        </td>
                        <td>÷</td>
                        <td>
                            <input type="text" class="form-control" placeholder="平均攤提時數b" value="40.07" readonly="" />
                        </td>
                        <td>×</td>
                        <td>使用時數</td>
                        <td>÷</td>
                        <td>負載量</td>
                    </tr>
                    <tr>
                        <td>=</td>
                        <td>
                            <input type="text" id="plantFloorAreaUse" class="form-control" readonly="" />
                        </td>
                        <td>×</td>
                        <td>使用時數</td>
                        <td>÷</td>
                        <td>負載量</td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="highlight">電力費用=</td>
                        <td>壓力鍋耗用度數/次數</td>
                        <td>×</td>
                        <td>電費/度</td>
                        <td>×</td>
                        <td>使用時數</td>
                        <td>÷</td>
                        <td>負載量</td>
                    </tr>
                    <tr>
                        <td>=</td>
                        <td>
                            <input type="text" id="consumptionDegreeTime" class="form-control" placeholder="壓力鍋耗用度數/次數" value="1.5" readonly="" />
                        </td>
                        <td>÷</td>
                        <td>
                            <input type="text" id="electricityFeeDegree" class="form-control" placeholder="電費/度" value="3.73" readonly="" />
                        </td>
                        <td>×</td>
                        <td>使用時數</td>
                        <td>÷</td>
                        <td>負載量</td>
                    </tr>
                    <tr>
                        <td>=</td>
                        <td>
                            <input type="text" id="electricityCosts" class="form-control" readonly="" />
                        </td>
                        <td>×</td>
                        <td>使用時數</td>
                        <td>÷</td>
                        <td>負載量</td>
                        <td></td>
                        <td></td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
