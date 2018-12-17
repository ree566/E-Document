<%-- 
    Document   : worktime-test
    Created on : 2017/12/18, 上午 08:38:10
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
    .entry:not(:first-of-type)
    {
        margin-top: 10px;
    }
    .glyphicon
    {
        font-size: 12px;
    }
</style>

<script>
    $(function () {
        var allowMaxCreate = 10;
        var clickTimes = 1;
        var regex = new RegExp("^[0-9a-zA-Z-]+$");
        var submitBtn = $("#create-series");
        $("#create-series").on("click", function () {
            var baseModel = $("#base-modelName").val();

            var seriesModel = $('.series-modelName').map(function () {
                return $(this).val();
            }).toArray();

            //Remove null or empty object
            seriesModel = jQuery.grep(seriesModel, function (n, i) {
                if (n != null) {
                    n = n.trim();
                }
                return (n != null && n !== "");
            });

            if (baseModel != null) {
                baseModel = baseModel.trim();
            }

            if (baseModel == null || baseModel == "") {
                alert("Please enter a base modelName.");
                return false;
            } else if (regex.test(baseModel) == false) {
                alert("Please enter a valid base modelName.");
                return false;
            }

            if (seriesModel.length == 0) {
                alert("Please at least input one series model.");
                return false;
            } else {
                for (var i = 0; i < seriesModel.length; i++) {
                    if (regex.test(seriesModel[i]) == false) {
                        alert("Please enter a valid series modelName on textbox " + (i + 1));
                        return false;
                    }
                }
            }
            submitBtn.attr("disabled", true).val("儲存中...");
            $.ajax({
                type: "POST",
                url: "<c:url value="/Worktime/createSeries" />",
                dataType: "json",
                data: {
                    baseModelName: baseModel,
                    seriesModelNames: seriesModel
                },
                success: function (response) {
                    alert(response);
                    submitBtn.attr("disabled", false).val("儲存");
                    $(":text").val('');
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert(xhr.responseText);
                    submitBtn.attr("disabled", false).val("儲存");
                }
            });
        });
        $(document).off('click', '.btn-add').off('click', '.btn-remove');
        $(document).on('click', '.btn-add', function (e) {
            e.preventDefault();
            if (clickTimes >= allowMaxCreate) {
                alert("Only allow create " + allowMaxCreate + " series models every time.");
                return false;
            }
            var controlForm = $('#series-modelName'),
                    currentEntry = $(this).parents('.voca:first'),
                    newEntry = $(currentEntry.clone(true)).appendTo(controlForm);
            newEntry.find('input').val('');
            controlForm.find('.btn-add:not(:last)')
                    .removeClass('btn-default').addClass('btn-danger')
                    .removeClass('btn-add').addClass('btn-remove')
                    .html('<span class="glyphicon glyphicon-minus" aria-hidden="true"></span> Remove   ');
            clickTimes++;
        });

        $(document).on('click', '.btn-remove', function (e) {
            $(this).parents('.voca:first').remove();
            e.preventDefault();
            clickTimes--;
            return false;
        });

        $(":text").on("keyup", function () {
            var str = $(this).val();
            $(this).val(str.toUpperCase());
        }).focus(function () {
            $(this).select();
        });
    });
</script>

<div id='insert-series' class="container">
    <div class="control-group" id="fields">
        <div class="controls"> 
            <h3>Insert series</h3>
            <form role="form" autocomplete="off">
                <div id="batch-insert-area">
                    <table class="table table-bordered"> 
                        <tr>
                            <td>
                                <label for="base-modelName">
                                    以此model為基底:
                                </label>
                            </td>
                            <td>
                                <div class="col-md-4">
                                    <input id="base-modelName" class="form-control" type="text" placeholder="參考資料modelName" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="series-modelName">
                                    建立資料:
                                </label>
                            </td>
                            <td>
                                <div class="row" id="series-modelName">
                                    <div class="voca">
                                        <div class="col-md-8 form-inline">
                                            <input class="form-control series-modelName" placeholder="Series modelName" name="voca" type="text" >
                                            <button type="button" class="btn btn-success btn-add" >
                                                <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> Add more
                                            </button>
                                        </div>
                                    </div>
                                </div>  
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input id="create-series" class="btn btn-default" type="button" value="儲存" />
                            </td>
                        </tr>
                    </table>
                </div>
            </form>
            <br>
        </div>
    </div>
</div>


