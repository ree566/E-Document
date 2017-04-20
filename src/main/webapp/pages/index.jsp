<%-- 
    Document   : test
    Created on : 2017/3/13, 上午 10:46:44
    Author     : Wei.Cheng
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../images/favicon.ico"/>

        <!-- Bootstrap Core CSS -->
        <link href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" rel="stylesheet">
        <link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
        <link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
        <link href="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/css/ui.jqgrid.css" rel="stylesheet"/>

        <!-- Custom CSS -->
        <link href="../css/simple-sidebar.css" rel="stylesheet">

        <script src="//code.jquery.com/jquery-1.12.4.min.js" 
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
        crossorigin="anonymous"></script>

        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"
                integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU="
        crossorigin="anonymous"></script>

        <script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>

        <script src="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/js/i18n/grid.locale-tw.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/js/jquery.jqGrid.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jqgrid/4.6.0/js/jquery.jqGrid.src.js"></script>

        <script type="text/javascript">
            // Change JQueryUI plugin names to fix name collision with Bootstrap.
            $.widget.bridge('uitooltip', $.ui.tooltip);
            $.widget.bridge('uibutton', $.ui.button);
        </script>

        <script>
            var current_include_page_name = "";
            $(function () {
                $("#sidebar-wrapper > ul > li > a").on("click", function () {
                    var target_page_name = $(this).attr("href");
                    if (target_page_name != current_include_page_name) {
                        current_include_page_name = target_page_name;
                        $("#exchange_page").load(target_page_name + ".jsp");
                    }
                    return false;
                });
                $("#preload_page").trigger("click");
            });
        </script>

    </head>
    <body>
        <div id="wrapper">

            <!-- Sidebar -->
            <div id="sidebar-wrapper">
                <ul class="sidebar-nav">
                    <li class="sidebar-brand">
                        <a href="worktime">${initParam.pageTitle} </a>
                    </li>
                    <li>
                        <a id="preload_page" href="worktime">工時大表</a>
                    </li>
                    <li>
                        <a href="page1">Page1</a>
                    </li>
                    <li>
                        <a href="page2">Page2</a>
                    </li>
                    <li>
                        <a href="page3">Page3</a>
                    </li>
                </ul>
            </div>
            <!-- /#sidebar-wrapper -->

            <!-- Page Content -->
            <div id="page-content-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <a href="#menu-toggle" class="btn btn-default" id="menu-toggle">Toggle Menu</a>
                        </div>
                        <div id="exchange_page" class="col-lg-12">
                            <%--<jsp:include page="worktime.jsp" />--%>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /#page-content-wrapper -->

        </div>
        <!-- /#wrapper -->

        <!-- Menu Toggle Script -->
        <script>
            $("#menu-toggle").click(function (e) {
                e.preventDefault();
                $("#wrapper").toggleClass("toggled");
                $(window).trigger('resize');
            });
        </script>
    </body>
</html>
