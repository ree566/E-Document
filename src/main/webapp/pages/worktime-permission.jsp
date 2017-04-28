<%-- 
    Document   : audit
    Created on : 2017/4/25, 下午 02:58:21
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}/"/>
<link href="${root}css/multi-select.css" rel="stylesheet">
<script src="${root}js/jquery.multi-select.js"></script>
<h1>Worktime permission change!</h1>

<script>
    $(function () {
        var grid_column_name = [
            'modelName', 'typeName', 'productionWt', 'totalModule',
            'setupTime', 'cleanPanel', 'assy', 't1', 't2',
            't3', 't4', 'packing', 'upBiRi', 'downBiRi',
            'biCost', 'vibration', 'hiPotLeakage', 'coldBoot', 'warmBoot',
            'assyToT1', 't2ToPacking', 'floorName', 'pendingName', 'pendingTime',
            'burnIn', 'biTime', 'biTemperature', 'speOwnerName', 'eeOwnerName',
            'qcOwnerName', 'assyPackingSop', 'testSop', 'keypartA', 'keypartB',
            'preAssyName', 'babFlowName', 'testFlowName', 'packingFlowName', 'partLink',
            'ce', 'ul', 'rohs', 'weee', 'madeInTaiwan',
            'fcc', 'eac', 'nInOneCollectionBox', 'partNoAttributeMaintain', 'assyStation',
            'packingStation', 'assyLeadTime', 'assyKanbanTime', 'packingLeadTime', 'packingKanbanTime',
            'cleanPanelAndAssembly'
        ];

        for (var i = 0; i < grid_column_name.length; i++) {
            $("#my-select").append("<option value='" + grid_column_name[i] + "'>" + grid_column_name[i] + "</option>");
        }
        
        $('#my-select').multiSelect({});
    });
</script>

<form>
    <select multiple="multiple" id="my-select" name="my-select[]">
    </select>
</form>