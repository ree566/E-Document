<%-- 
    Document   : abc
    Created on : 2017/5/5, 下午 03:20:39
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Admin page</title>
        <style>
            h5{
                margin: 0px;
            }
        </style>
        <script src="//code.jquery.com/jquery-1.12.4.min.js" 
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
        crossorigin="anonymous"></script>
        <script src="<c:url value="/js/jqgrid-custom-validator.js" />"></script>

        <script>
            $(function () {
                var testflow = [
                    'BAB_ASSY',
                    'BAB_ASSY-AL-T1-BI',
                    'BAB_ASSY-AL-VB-T1-BI',
                    'BAB_ASSY-BI',
                    'BAB_ASSY-H1-LK',
                    'BAB_ASSY-H1-LK-T1',
                    'BAB_ASSY-H1-LK-T1-BI',
                    'BAB_ASSY-H1-LK-T1-RI',
                    'BAB_ASSY-H1-T1',
                    'BAB_ASSY-H1-T1-BI',
                    'BAB_ASSY-H1-T1-RI',
                    'BAB_ASSY-MAC-T1',
                    'BAB_ASSY-T0-T1-BI',
                    'BAB_ASSY-T1',
                    'BAB_ASSY-T1-BI',
                    'BAB_ASSY-T1-BI-DA',
                    'BAB_ASSY-T1-BI-T2-T3-DA',
                    'BAB_ASSY-T1-RI',
                    'BAB_ASSY-T2-T3',
                    'BAB_ASSY-T3',
                    'BAB_ASSY-VB',
                    'BAB_ASSY-VB-BI',
                    'BAB_ASSY-VB-H1-LK-T1-BI',
                    'BAB_ASSY-VB-H1-LK-T1-RI',
                    'BAB_ASSY-VB-H1-T1',
                    'BAB_ASSY-VB-H1-T1-BI',
                    'BAB_ASSY-VB-H1-T1-CB-BI',
                    'BAB_ASSY-VB-T1',
                    'BAB_ASSY-VB-T1-BI',
                    'BAB_ASSY-VB-T1-CB-BI',
                    'BAB_ASSY-VB-T1-RI',
                    'BAB_DRAGER',
                    'BAB_GE_DL',
                    'BAB_GE_DL(NO EC)',
                    'BAB_GE_DL9',
                    'BAB_GE_RTAC',
                    'BAB_T1',
                    'BAB_T1-BI',
                    'BAB_VB-T1-BI',
                    'PI',
                    'T/S Pre-test1-T/S Pre-test2',
                    'TEST_MAC-T2-PI',
                    'TEST_MAC-T2-WiFi-PI',
                    'TEST_T2',
                    'TEST_T2-PI',
                    'TEST_MAC-T2-T3-PI(T1-BI)',
                    'TEST_T2-T3-PI(T1-BI)',
                    'TEST_T2-PI(BI)',
                    'TEST_PI(T1)',
                    'TEST_T3-PI(T1)',
                    'TEST_T2-PI(T1-BI)',
                    'TEST_T2-PI(T1-RI)',
                    'TEST_MAC-T2-T3-PI(T1-RI)',
                    'TEST_MAC-T2-PI(T1-BI)',
                    'TEST_H1-LK-PI(MAC-T1)',
                    'TEST_T3-H1-LK-PI(MAC-T1)',
                    'TEST_T2-RI-T3-PI(T1-BI)',
                    'HS',
                    'TEST_H1-LK-PI(T1)',
                    'TEST_T2-PI(T1)',
                    'TEST_T3-H1-LK-PI(T1)',
                    'TEST_MAC-T2-T3-H1-LK-PI(T1-BI)',
                    'TEST_MAC-T2-WiFi-WWAN-T3-PI(T1-BI)',
                    'TEST_T2-H1-LK-PI(T1-BI)',
                    'TEST_T2-H1-PI(T1-BI)',
                    'TEST_T2-T3-H1-LK-PI(T1-BI)',
                    'TEST_T2-T3-H1-PI(T1-BI)',
                    'TEST_T2-T3-PI-H1-T4(T1-BI)',
                    'TEST_T2-WiFi-PI(T1-BI)',
                    'TEST_T2-WiFi-T3-PI(T1-BI)',
                    'TEST_T2-WiFi-WWAN-T3-PI(T1-BI)',
                    'TEST_MAC-T2-PI(T1-RI)',
                    'TEST_T2-T3-H1-PI(T1-RI)',
                    'TEST_T2-T3-PI(T1-RI)',
                    'TEST_T2-WiFi-WWAN-T3-PI(T1-RI)',
                    'TEST_T2-T3-PI(BI)',
                    'TEST_T2-T3-PI(T1)',
                    'TEST_DRAGER',
                    'TEST_GE_DL',
                    'TEST_GE_RTAC',
                    'TEST_GE_DL(NO EC)',
                    'TEST_GE_RTAC(NO EC)',
                    'TEST_GE_DL9',
                    'TEST_LK-PI(T1)',
                    'TEST_T2-WiFi-WWAN-PI(T1-BI)',
                    'PKG'
                ];
//                var looptime = 0;
//                var BAB_FLOW_GROUP = "BAB";
//                var TEST_FLOW_GROUP = "TEST";
//                var PKG_FLOW_GROUP = "PKG";
//
//                for (var i = 0; i < testflow.length; i++) {
//                    var flowName = testflow[i];
//                    var check_logic = [];
//
//                    if (flowName.includes(BAB_FLOW_GROUP)) {
//                        check_logic = flow_check_logic[BAB_FLOW_GROUP];
//                    } else if (flowName.includes(TEST_FLOW_GROUP)) {
//                        check_logic = flow_check_logic[TEST_FLOW_GROUP];
//                    } else {
//                        check_logic = flow_check_logic[PKG_FLOW_GROUP];
//                    }
//                    
////                    appendTestString(JSON.stringify(check_logic));
//
//                    for (var j = 0; j < check_logic.length; j++) {
//                        var keyword = check_logic[j].keyword;
//                        looptime++;
//
//                        if (typeof keyword == 'string') {
//                            if (flowName.includes(keyword)) {
//                                appendTestString(flowName + " is in validator check.");
//                                appendTestString("&nbsp;&nbsp;&nbsp;" + keyword);
//                            }
//                        } else {
//                            for (var k = 0; k < keyword.length; k++) {
//                                if (flowName.includes(keyword[k])) {
//                                    appendTestString(flowName + " is in validator check.");
//                                    appendTestString("&nbsp;&nbsp;&nbsp;" + keyword.toString());
//                                }
//                            }
//                        }
//
//                    }
//                    
//                    appendTestString("-----------------------");
//                }
//
//                appendTestString("loop " + looptime);
//
//                function appendTestString(st) {
//                    $("#testarea").append("<h5>" + st + "</h5>");
//                }
                var arr = [];
                alert(arr === true);
            });
        </script>
    </head>
    <body>
        Dear <strong>${user}</strong>, Welcome to Admin Page.
        <a href="<c:url value="/logout" />">Logout</a>

        <div id="testarea">

        </div>
    </body>
</html>
