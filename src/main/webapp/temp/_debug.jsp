<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
    <table class="table table-bordered">
        <c:forEach var="cookieVal" items="${pageContext.request.cookies}" > 
            <tr>
                <td align="right">${cookieVal.name}</td>
                <td>${cookieVal.value}</td>
            </tr>
        </c:forEach>
    </table>
</div>