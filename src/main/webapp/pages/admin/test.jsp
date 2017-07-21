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


        <script>
            $(function () {
                var testData = {
                    modelName: "AESBC",
                    "businessGroup.id": "",
                    "workCenter": "ES"
                };

                var modelName_check_logic = [
                    {keyword: "ES", checkColumn: ["businessGroup.id", "workCenter"], message: "Must contain 'ES'"}
                ];

                var field_check_modelName_logic = [
                    {checkColumn: {name: "businessGroup.id", value: "ES"}, description: "內容為ES", targetColumn: {name: "modelName", keyword: ["ES"]}},
                    {checkColumn: {name: "workCenter", value: "ES"}, description: "內容為ES", targetColumn: {name: "modelName", keyword: ["ES"]}}
                ];

                function modelNameCheckFieldIsValid(data) {
                    var validationErrors = [];
                    var modelName = data["modelName"];
                    for (var i = 0; i < modelName_check_logic.length; i++) {
                        var logic = modelName_check_logic[i];
                        var keyword = logic.keyword;
                        if (modelName.endsWith(keyword) == false) {
                            continue;
                        }
                        var checkCols = logic.checkColumn;
                        for (var j = 0, k = checkCols.length; j < k; j++) {
                            var colName = checkCols[j];
                            var checkVal = data[colName];
                            if (checkVal.indexOf(keyword) == -1) {
                                var err = {};
                                err.field = colName;
                                err.code = logic.message;
                                validationErrors.push(err);
                            }
                        }
                    }
                    return validationErrors;
                }

                function checkModelNameIsValid(data) {
                    var validationErrors = [];
                    for (var i = 0; i < field_check_modelName_logic.length; i++) {
                        var logic = field_check_modelName_logic[i];
                        var checkColInfo = logic.checkColumn;
                        var isNeedToCheck = data[checkColInfo.name].indexOf(checkColInfo.value) != -1;
                        if(isNeedToCheck){
                            var targetColInfo = logic.targetColumn;
                            var targetColName = targetColInfo.name;
                            var colVal = data[targetColName];
                            if(colVal.endsWith(targetColInfo.keyword) == false){
                                var err = {};
                                err.field = targetColName;
                                err.code = targetColName + " must contain " + targetColInfo.keyword;
                                appendFieldInfo(checkColInfo.name, logic.description, err);
                                validationErrors.push(err);
                            }
                        }
                    }
                    return validationErrors;
                }

                function appendFieldInfo(field, description, error) {
                    error.code = field + description + ' , ' + error.code;
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
