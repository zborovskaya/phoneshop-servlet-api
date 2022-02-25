<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.product.bean.Order" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>
<%@ attribute name="nameValues" required="true" type="java.util.Map" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tr>
    <td>${label}<span style="color:red">*</span></td>
    <td>
        <c:set var="error" value="${errors[name]}"/>
        <input name="${name}"
        <c:if test="${name eq'phone'}">
               pattern="^\+375[0-9]{9}$"
               placeholder="+375xxxxxxxxx"
        </c:if>
        <c:if test="${name eq'firstName'}">
               placeholder="Ivan"
        </c:if>
        <c:if test="${name eq'lastName'}">
               placeholder="Sadovski"
        </c:if>
               value="${not empty error ? nameValues[name] : order[name]}">
        <c:if test="${not empty error}">
            <div class="error">
                    ${error}
            </div>
        </c:if>
    </td>
</tr>
