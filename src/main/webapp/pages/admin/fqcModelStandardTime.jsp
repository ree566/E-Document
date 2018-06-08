<%-- 
    Document   : modelSopRemark
    Created on : 2018/5/22, 上午 11:28:40
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="shortcut icon" href="../../images/favicon.ico"/>
        <link rel="stylesheet" href="<c:url value="/webjars/datatables/1.10.16/css/jquery.dataTables.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/bootstrap-datetimepicker.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/buttons.dataTables.min.css" />">
        <link rel="stylesheet" href="<c:url value="/css/select2.min.css" />">
        <style>
            #wigetCtrl{
                margin: 0 auto;
                width: 98%;
            }
            body {
                padding-top: 70px;
                /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
            }
            td.details-control {
                background: url('<c:url value="/images/details_open.png" />') no-repeat center center;
                cursor: pointer;
            }
            tr.shown td.details-control {
                background: url('<c:url value="/images/details_close.png" />') no-repeat center center;
            }
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/webjars/datatables/1.10.16/js/jquery.dataTables.min.js" /> "></script>
        <script src="<c:url value="/webjars/jquery-blockui/2.70/jquery.blockUI.js" /> "></script>
        <script src="<c:url value="/webjars/momentjs/2.18.1/moment.js" /> "></script>
        <script src="<c:url value="/js/param.check.js"/>"></script>
        <script src="<c:url value="/js/dataTables.cellEdit.js"/>"></script>
        <script src="<c:url value="/js/select2.min.js"/>"></script>

        <script>
            ﻿$(function () {
                var table;

                //This function will load the datatable
                LoadTable();

                $("#modelNameCategory").on("keyup change", function () {
                    $(this).val($(this).val().toUpperCase());
                });

                //Hook up the click event for the add/edit customer button
                $("#RemarkInfoPopup").click(function () {
                    //clear form
                    ClearForm();

                    //open modal window
                    $('#addEditRemark').modal('toggle');
                });
                
                //Hook up the click event for the save button on the add/edit popup window
                $("#AddRemarkButton").click(function () {
                    //validate that name and last name were entered

                    var modelNameCategory = $("#modelNameCategory").val();
                    var standardTime = $("#standardTime").val();

                    var errorMsg = "";
                    if (!modelNameCategory) {
                        errorMsg += "\n* enter the modelNameCategory";
                    }

                    if (!standardTime) {
                        errorMsg += "\n* enter the standardTime";
                    }

                    if (errorMsg != "") {
                        errorMsg = "The following errors were found: \n" + errorMsg + "\n\n Please enter the required information and try again.";
                        alert(errorMsg);
                    } else {
                        //JQuery ajax call
                        $.ajax({
                            method: "POST",
                            url: "<c:url value="/FqcModelStandardTimeController/saveOrUpdate" />",
                            dataType: "html",
                            data: {
                                id: $("#currentID").val(),
                                modelNameCategory: modelNameCategory,
                                standardTime: standardTime
                            },
                            success: function (response) {
                                if (response == "success") {
                                    table.ajax.reload(); //refresh the datatable to reflect the changes    
                                    $('#addEditRemark').modal('hide'); //hide the popup window
                                    alert(response);
                                }
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(xhr.responseText);
                            }
                        });
                    }
                });
                
                //hook up event for edit record buttons
                $(document).on('click', '.EditButton', function (event) { //any element with the class EditButton will be handled here
                    var data = table.row($(this).parents('tr')).data();
                    ClearForm();

                    $("#modelNameCategory").val(data.modelNameCategory);
                    $("#standardTime").val(data.standardTime);
                    $("#currentID").val(data.id);

                    $('#addEditRemark').modal('show');
                });

                function ClearForm() {//blank the add/edit popup form
                    var dialog = $("#addEditRemark");
                    dialog.find(":text, textarea, input[type='number']").val("");
                    dialog.find("#currentID").val(0);
                }

                function LoadTable() {
                    table = $('#remark-info').DataTable({
                        dom: 'Bfrtip',
                        "processing": true,
                        "serverSide": false,
                        "ajax": {
                            "url": "<c:url value="/FqcModelStandardTimeController/findAll" />",
                            "type": "GET"
                        },
                        "columnDefs": [{//this definition is set so the column with the action buttons is not sortable
                                "targets": -1, //this references the last column of the data
                                "orderable": false
                            }],
                        "columns": [
                            {data: "id"},
                            {data: "modelNameCategory"},
                            {data: "standardTime"},
                            {
                                "data": "id",
                                "width": "20%",
                                "render": function (data, type, full, meta) { //this column is redefinied to show the action buttons
                                    return '<div class="btn-toolbar"><button class="btn btn-sm btn-primary EditButton">Edit</button><button class="btn btn-sm btn-danger DeleteButton">Delete</button></div>';
                                }
                            }
                        ],
                        "pageLength": 20,
                        "order": [[1, 'asc']]
                    });
                }

                $(document).on('click', '.DeleteButton', function (event) {
                    if (confirm('This action will delete the selected record. Plese click OK to confirm.')) {
                        var data = table.row($(this).parents('tr')).data();
                        //load from database
                        $.ajax({
                            method: "POST",
                            url: "<c:url value="/FqcModelStandardTimeController/delete" />",
                            dataType: "html",
                            data: {
                                id: data.id
                            },
                            success: function (response) {
                                if (response == "success") {
                                    table.ajax.reload();
                                    alert("Record deleted successfully.");
                                }
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(xhr.responseText);
                            }
                        });
                    }
                });
            });
        </script>
    </head>
    <body>
        <c:import url="/temp/admin-header.jsp" />
        <div id="wigetCtrl">
            <div>
                <div class="row">
                    <div class="col-lg-12 text-right">
                        <button type="button" class="btn btn-primary btn-lg" id="RemarkInfoPopup">Add</button>              
                    </div>
                </div>

                <div class="control-label col-sm-12">
                    <div class="alert">
                        <h3>Model系列工時維護</h3>
                    </div>
                    <table id="remark-info" class="table table-bordered display cell-border">
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>KeyWord</th>
                                <th>標工</th>
                                <th>action</th>
                            </tr>
                        </thead>
                    </table>
                </div>

            </div>

            <!-- add/edit popup window -->
            <div class="modal fade" id="addEditRemark" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">                
                            <h4 class="modal-title" id="myModalLabel">Add/Edit Remark</h4>
                        </div>
                        <div class="modal-body">
                            <fieldset>
                                <!-- Form Name -->
                                <!-- Text input-->
                                <label class="control-label col-sm-3" for="modelNameCategory">Keyword: </label>
                                <div class="input-group col-sm-9">
                                    <input id="modelNameCategory" name="modelNameCategory" type="text" class="input-xlarge form-control">
                                </div><br />
                                <label class="control-label col-sm-3" for="standardTime">標工: </label>
                                <div class="input-group col-sm-9">
                                    <input type="number" id="standardTime" name="standardTime" class="input-xlarge form-control">
                                </div><br />
                                <input type="hidden" id="currentID" value="" />                         
                            </fieldset>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" id="AddRemarkButton">Save changes</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>    
            <!-- end popup window -->

        </div>
    </body>
</html>
