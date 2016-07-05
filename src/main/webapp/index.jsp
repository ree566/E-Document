<%-- 
    Document   : index
    Created on : 2015/9/23, 上午 10:40:43
    Author     : Wei.Cheng

    http://encosia.com/3-reasons-why-you-should-let-google-host-jquery-for-you/?ref=sidebar_most_popular
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <style>
            a:link,a:visited{
                color: blue;
            }
            a:hover{
                opacity: 0.5;
            }
            .goToButton{
            }
            .clearWiget{
                clear: both;
            }
            #wigetCtrl{
                margin: 0 auto;
                width: 95%
            }
            #titleLogo{
                width: 20%;
                height: 20%;
                float: right;
            }
            .noscript input{
                display: block;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/jquery.blockUI.js"></script>
        <script src="js/jquery.blockUI.Default.js"></script>
        <script src="js/jquery.cookie.js"></script>
        <script>

            $(document).ready(function () {

                $("input").addClass("form-control");

                //Quartz standby and resume.(Please remove when sensor testing is finish)
                $("#title").dblclick(function () {
                    $("#trigobj").slideToggle();
                });

                $("#trigobj :button").on("click", function () {
                    var order = $(this).val();
                    $.ajax({
                        type: "Post",
                        url: "QuartzTriggerControl",
                        data: {
                            order: order
                        },
                        dataType: "html",
                        success: function (response) {
                            $("#servermsg").html(response);
                        },
                        error: function () {
                            $("#servermsg").html("error");
                        }
                    });
                });
            });
        </script>
    </head>
    <body class="noscript">
        
        <script>
            $("body").removeClass("noscript");
        </script>
        <jsp:include page="head.jsp" />
        <div id="wigetCtrl">
            <div>
                <img id="titleLogo" src="images/mfg_lgo_advantech.gif" />
            </div>
            <div>
                <h3 id="title">${initParam.pageTitle}</h3>
            </div>
            <div>
                <div class="form-inline row">
                    <a href="Bab">
                        <input type="button" class="col-xs-6" value="組裝 / 包裝">
                    </a>
                    <a href="Test">
                        <input type="button" class="col-xs-6" value="測試">
                    </a>
                </div>
                <div class="clearWiget">
                    <div id="trigobj" hidden="hidden">
                        trigger order:
                        <input type="button" value="pause">
                        <input type="button" value="resume">
                        <div id="servermsg"></div>
                    </div>
                    <hr/>
                    <h3>Tomcat Version : <%= application.getServerInfo()%></h3>
                    <h5>Servlet Specification Version : <%= application.getMajorVersion()%>.<%= application.getMinorVersion()%> </h5>
                    <h5>JSP version :<%=JspFactory.getDefaultFactory().getEngineInfo().getSpecificationVersion()%></h5>

                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
