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
        <link href="<c:url value="/css/multi-select.css" />" rel="stylesheet">
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js" />"></script>

        <script src="<c:url value="/js/jqgrid-custom-validator.js" />"></script>

        <script>
            $(function () {
                var msgbox = $("#checkMsg");
                var postdata = {
                    cleanPanel: 10,
                    assy: 0,
                    t1: 1,
                    t2: 0,
                    t3: 0,
                    t4: null,
                    vibration: 2,
                    hiPotLeakage: 0,
                    coldBoot: 15,
                    upBiRi: 0,
                    downBiRi: 0,
                    biCost: 20,
                    burnIn: 'BI',
                    packing: 0,
                    babFlow: 'BAB_ASSY-VB-T1-RI',
                    testFlow: 'TEST_MAC-T2-Wifi-T3-PI(T1-BI)',
                    packingFlow: 'PKG'
                };

//                fieldCheck(postdata);
    
                testFunc(postdata);
                
                function testFunc(postdata){
                    var obj = $.extend(true, {}, postdata);
                    obj.abb = 1;
                    print(JSON.stringify(postdata));
                    
                }

                function fieldCheck(postdata) {
                    for (var i = 0; i < field_check_flow_logic.length; i++) {
                        var logic = field_check_flow_logic[i];
                        var colInfo = logic.checkColumn;

                        var colName = colInfo.name;
                        var checkBool = colInfo.equals;
                        var fieldVal = postdata[colName];
                        var checkVal = colInfo.value;

                        var description = logic.description;

                        var targetColInfo = logic.targetColumn;
                        var targetColName = targetColInfo.name;
                        var targetKeyword = targetColInfo.keyword;
                        var targetColVal = postdata[targetColName];

                        console.log(targetColName);
                        console.log(targetColVal);

                        print(colName + ' ' + description + ' ,' + targetColName + ' must contain ' + targetKeyword + '.(Your val: ' + fieldVal + ' / ' + targetColVal + ')');
                        if (checkBool == true) {
                            checkFlow((fieldVal != null && fieldVal == checkVal), targetColName, targetColVal, targetKeyword);
                        } else if (checkBool == false) {
                            checkFlow((fieldVal != null && fieldVal != checkVal), targetColName, targetColVal, targetKeyword);
                        }
                        print('--------------');
                    }
                    print('Check complete.');
                }

                function checkFlow(bool, targetColName, targetColVal, keyword) {
                    if (bool) {
                        if (targetColVal != null) {
                            var keyCheckFlag = false;
                            for (var i = 0; i < keyword.length; i++) {
                                if (targetColVal.indexOf(keyword[i]) > -1) {
                                    keyCheckFlag = true;
                                    break;
                                }
                            }
                            if (keyCheckFlag) {
                                print('【O】' + targetColName + ': pass. / ' + targetColVal);
                            } else {
                                print('【X】' + targetColName + ' must conain: ' + keyword + ' Your val is:' + targetColVal);
                            }
                        } else {
                            print('【X】' + targetColName + ' must conain: ' + keyword + './ Your val is: ' + targetColVal);
                        }
                    } else {
                        print('Check ignore.');
                    }
                }

                function print(msg) {
                    msgbox.append("<p>" + msg + "</p>");
                }

            });
        </script>
    </head>
    <body>
        <div class="container">
            <div id="checkMsg"></div>
        </div>
    </body>
</html>
