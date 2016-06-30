<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<style>
    a[disabled] {
        pointer-events: none;
    }
    span{
        display: inline;
    }
    #logoImg{
        width: 40px;
        height: 25px;
    }
</style>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<div style="text-align:center; color: red">
    <noscript>For full functionality of this page it is necessary to enable JavaScript. Here are the <a href="http://www.enable-javascript.com" target="_blank"> instructions how to enable JavaScript in your web browser</a></noscript>
</div>
<div id="jquery-require-message" style="text-align:center; color: red">
</div>
<script>
    if (!window.jQuery) {
        document.getElementById("jquery-require-message").innerHTML =
                "Sorry, this page require jquery plugin\
                , please check your system environment or contact system administrator";
    }
</script>
<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="SysInfo">
                <span><img id="logoImg" src="images/bulb.png" alt="sysIcon" /></span>
                    ${initParam.pageTitle}
                <span><img id="logoImg" src="images/bulb.png" alt="sysIcon" /></span>
            </a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li>
                    <a href="TestTotal"><span class="glyphicon glyphicon-list-alt" aria-hidden="true" /> 測試線別狀態</a>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="glyphicon glyphicon-list-alt" aria-hidden="true" /> 組包裝總統計表<span class="caret" /></a>
                    <ul class="dropdown-menu">
                        <li><a href="BabTotal">線平衡資訊查詢</a></li>
                        <li><a href="BabDetailInfo">各站機台時間查詢</a></li>
                        <li><a href="BabDetail">工單亮燈頻率查詢</a></li>
                    </ul>
                </li>
                <li>
                    <a href="TotalMap"><span class="glyphicon glyphicon-list-alt" aria-hidden="true" /> 整體狀態平面圖(5F)</a>
                </li>
                <li>
                    <a href="SensorAdjust"><span class="glyphicon glyphicon-list-alt" aria-hidden="true" /> Sensor狀態(校正用)</a>
                </li>
                <li>
                    <a href="About">關於系統</a>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container -->
</nav>
<!-- /.container -->

<!-- 為了省略include所造成多餘的<html><body>標籤而簡化，encoding會有問題還是要加上開頭 -->
