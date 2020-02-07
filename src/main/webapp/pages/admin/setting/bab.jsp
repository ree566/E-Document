<%-- 
    Document   : options
    Created on : 2017/1/4, 上午 09:08:54
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />"/>
        <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />">
        <link rel="stylesheet" href="<c:url value="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />" >

        <style>
            table tr td{
                padding: 5px;
            }

            .alarm{
                color: red;
            }
        </style>

        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js" />"></script>
        <script>
            $(function () {

                $(":button").addClass("btn btn-default");
                $(":text, :input[type='number']").addClass("form-control");

                $(".bab-close .close-directly, .close-with-saving").click(function () {
                    console.log($(this).attr("class"));
                });
            });
        </script>
    </head>
    <body>
        <div class="container">
            <div class="page-header">
                <h3>${initParam.pageTitle}設定</h3>
            </div>

            <div class="row">
                <h5>Quartz setting</h5>
                <table class="table table-bordered">
                    <tr>
                        <td>
                            <label>Resched all numLamp job</label>
                        </td>
                        <td>
                            <input type="button" id="numLampResched" value="resched" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Clear all jobKey by group</label>
                        </td>
                        <td class="form-inline">
                            <input type="text" placeholder="Insert jobGroup">
                            <input type="button" id="removeJobGroup" value="remove" />
                            <input type="button" id="getJobs" value="getJobs" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Show all sched jobs</label>
                        </td>
                        <td class="form-inline">
                            <input type="button" id="getJobs" value="getJobs" />
                        </td>
                    </tr>
                    <tr class="danger">
                        <td>
                            <label>Quartz pause or resume.(<strong class="alarm">*Will enter the test mode</strong>)</label>
                        </td>
                        <td>
                            <input type="button" id="quartzPause" value="pause" />
                            <input type="button" id="quartzResume" value="resume" />
                        </td>
                    </tr>
                </table>
            </div>
            <hr />

            <div class="row">
                <h5>Sensor tagName setting</h5>
                <div><input type="button" id="getSelect" value="getSelect"></div>
                <table id="tagNameList">
                </table>
            </div>
            <hr />

            <div class="row">
                <h5>Relative options setting</h5>
            </div>
            <hr />

            <div class="row bab-close">
                <h5>Manual close Bab record</h5>
                <div class="form-inline">
                    <input type="number" class="bab-id" placeholder="bab id here" />
                    <input type="button" class="close-directly" value="直接關閉" />
                    <input type="button" class="close-with-saving" value="關閉+儲存" />
                </div>
            </div>
            <hr />

            <div class="row">
                <h5>Sensor login or logout</h5>
                <div class="form-inline">
                    <input type="text" placeholder="insert tagName here" />
                    <input type="text" placeholder="insert jobnumber here" />
                    <input type="button" value="query" />
                    <input type="button" value="login" />
                    <input type="button" value="logout" />
                </div>
            </div>
            <hr />

            <div class="row">
                <h5>Test table login or logout</h5>
                <div class="form-inline">
                    <input type="text" placeholder="insert table number here" />
                    <input type="text" placeholder="insert jobnumber here" />
                    <input type="button" value="query" />
                    <input type="button" value="login" />
                    <input type="button" value="logout" />
                </div>
            </div>
            <hr />

            <div class="row">
                <h5>TagName Comparison table edit</h5>
                <table></table>
                <input type="button" value="init alarm table">
                <input type="button" value="init alarm table">
            </div>
            <hr />

            <div class="button-message server-message"></div>
        </div>
    </body>
</html>
