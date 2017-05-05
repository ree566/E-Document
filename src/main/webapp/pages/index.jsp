<%-- 
    Document   : page1
    Created on : 2017/4/19, 上午 09:35:39
    Author     : Wei.Cheng
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="root" value="${pageContext.request.contextPath}/"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="${root}images/favicon.ico"/>

        <link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
        <link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
        <link href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" rel="stylesheet">

        <!--<link href="//cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.14.0/css/ui.jqgrid.min.css" rel="stylesheet"/>-->
        <link href="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/css/ui.jqgrid.css" rel="stylesheet"/>

        <link href="../css/sb-admin-2.min.css" rel="stylesheet">
        <link href="../css/metisMenu.min.css" rel="stylesheet">

        <style>
            #loading {
                width: 100%;
                height: 100%;
                position: fixed;
                display: block;
                opacity: 0.7;
                background-color: #fff;
                z-index: 99;
                text-align: center;
            }

            #loading-image {
                position: absolute;
                top: 100px;
                left: 240px;
                z-index: 100;
            }
        </style>

        <script src="//code.jquery.com/jquery-1.12.4.min.js" 
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
        crossorigin="anonymous"></script>

        <script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>

        <script src="//code.jquery.com/ui/1.12.1/jquery-ui.min.js"
                integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU="
        crossorigin="anonymous"></script>

        <script src="//cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.1/jquery.form.min.js"></script>

<!--        <script src="//cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.14.0/jquery.jqgrid.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.14.0/modules/grid.jqueryui.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.14.0/modules/grid.formedit.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.14.0/modules/min/jqmodal.js"></script>-->

        <script src="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/js/i18n/grid.locale-tw.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/js/jquery.jqGrid.min.js"></script>

        <script src="../js/sb-admin-2.min.js"></script>
        <script src="../js/metisMenu.min.js"></script>

        <script src="${root}/js/jqgrid-custom-param.js"></script> 
        <script src="${root}js/sessionExpiredDetect.js"></script>

        <!--<script src="//cdnjs.cloudflare.com/ajax/libs/free-jqgrid/4.14.0/modules/grid.subgrid.js"></script>-->
        <script>
            var current_include_page_name = "";
            $(function () {
                $('#loading').hide();

                $(".redirect-link").on("click", function () {
                    var target_page_name = $(this).attr("href");
                    if (target_page_name != current_include_page_name && target_page_name != "#") {
                        current_include_page_name = target_page_name;
                        $('#loading').show();
                        $("#exchange_page").load(target_page_name + ".jsp", function (response, status, xhr) {
                            if (status == "error") {
                                var msg = "Sorry but there was an error: ";
                                $("#exchange_page").html(msg + xhr.status + " " + xhr.statusText);
                            }
                            $('#loading').hide();
                        });
                    }
                    return false;
                });

                $("#preload_page").trigger("click");
            });
        </script>

    </head>
    <body>
        <div id="wrapper">

            <!-- Navigation -->
            <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="index.html">${initParam.pageTitle}</a>
                </div>
                <!-- /.navbar-header -->

                <ul class="nav navbar-top-links navbar-right">
                    <li class="dropdown">
                    <lable for="jobnumber">Jobnumber: </lable><font id="jobnumber"><c:out value="${sessionScope.user.jobnumber}" /></font> / 
                    <lable for="name">Name: </lable><font id="name"><c:out value="${sessionScope.user.name}" /></font> / 
                    <lable for="unit">Unit: </lable><font id="unit"><c:out value="${sessionScope.user.userType.name}" /></font> / 
                    <lable for="floor">Floor: </lable><font id="floor"><c:out value="${sessionScope.user.floor.name}" /></font> 
                    </li>

                    <!-- /.dropdown -->
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                            <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
                        </a>
                        <ul class="dropdown-menu dropdown-user">
                            <li><a href="#"><i class="fa fa-user fa-fw"></i> User Profile</a>
                            </li>
                            <li><a href="#"><i class="fa fa-gear fa-fw"></i> Settings</a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <form id="logout_form" action="${root}/logout.do" method="post"></form>
                                <a href="#" onclick="$('#logout_form').submit()"><i class="fa fa-sign-out fa-fw"></i> logout</a>
                            </li>
                        </ul>
                        <!-- /.dropdown-user -->
                    </li>
                    <!-- /.dropdown -->
                </ul>
                <!-- /.navbar-top-links -->

                <div class="navbar-default sidebar" role="navigation">
                    <div class="sidebar-nav navbar-collapse">
                        <ul class="nav" id="side-menu">
                            <li>
                                <a href="#"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a>
                            </li>
                            <li>
                                <a href="#"><i class="fa fa-bar-chart-o fa-fw"></i> Charts<span class="fa arrow"></span></a>
                                <ul class="nav nav-second-level">
                                    <li>
                                        <a class="redirect-link" href="chart">Flot Charts</a>
                                    </li>
                                    <li>
                                        <a class="redirect-link" href="chart">Morris.js Charts</a>
                                    </li>
                                </ul>
                                <!-- /.nav-second-level -->
                            </li>
                            <li>
                                <a href="#"><i class="fa fa-table fa-fw"></i> Tables<span class="fa arrow"></span></a>
                                <ul class="nav nav-second-level">
                                    <li>
                                        <a class="redirect-link" href="worktime">工時大表</a>
                                    </li>
                                    <c:if test="${sessionScope.user.permission >= initParam.UNIT_LEADER_PERMISSION}">
                                        <li>
                                            <a class="redirect-link" href="mod/flow">Flow</a>
                                        </li>
                                        <li>
                                            <a class="redirect-link" href="mod/user">User</a>
                                        </li>
                                        <li>
                                            <a class="redirect-link" href="mod/pending">Pending</a>
                                        </li>
                                        <li>
                                            <a class="redirect-link" href="mod/preAssy">PreAssy</a>
                                        </li>
                                        <li>
                                            <a class="redirect-link" href="mod/type">Type</a>
                                        </li>
                                    </c:if>
                                </ul>
                            </li>
                            <c:if test="${sessionScope.user.permission >= initParam.UNIT_LEADER_PERMISSION}">
                                <li>
                                    <a href="#"><i class="fa fa-wrench fa-fw"></i> UI Elements<span class="fa arrow"></span></a>
                                    <ul class="nav nav-second-level">
                                        <li>
                                            <a class="redirect-link" href="fileupload">Excel文件上傳</a>
                                        </li>
                                        <li>
                                            <a class="redirect-link" id="preload_page" href="audit">資料版本查詢</a>
                                        </li>
                                        <li>
                                            <a class="redirect-link" href="worktime-permission">欄位權限設定</a>
                                        </li>
                                        <li>
                                            <a class="redirect-link" href="wowface">Not exist page</a>
                                        </li>
                                    </ul>
                                    <!-- /.nav-second-level -->
                                </li>
                            </c:if>
                        </ul>
                    </div>
                    <!-- /.sidebar-collapse -->
                </div>
                <!-- /.navbar-static-side -->
            </nav>

            <div id="page-wrapper">

                <!-- /.row -->
                <div class="row">
                    <div class="col-lg-12">
                        <!-- /.panel -->
                        <div class="panel panel-default">
                            <div class="panel-body">
                                <div id="loading">
                                    <img id="loading-image" src="../images/loading.gif" alt="Loading..." />
                                </div>
                                <div id="exchange_page" class="row">
                                </div>
                                <!-- /.row -->
                            </div>
                            <!-- /.panel-body -->
                        </div>
                    </div>
                </div>
            </div>
            <!-- /#page-wrapper -->
        </div>
    </body>
</html>
