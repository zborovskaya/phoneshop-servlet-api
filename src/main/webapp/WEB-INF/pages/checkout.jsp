<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="order" type="com.es.phoneshop.model.product.bean.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <c:if test="${not message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            There was an error
        </div>
    </c:if>
    </p>
    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="price">
                    Price
                </td>
                <td class="quantity">
                    Quantity
                </td>
            </tr>
            </thead>
            <c:forEach var="product" items="${products}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile"
                             src="${product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                                ${product.description}
                        </a>
                    </td>
                    <td class="price">
                        <a href="${pageContext.servletContext.contextPath}/products/priceHistory?productId=${product.id}">
                            <fmt:formatNumber value="${product.price}" type="currency"
                                              currencySymbol="${product.currency.symbol}"/>
                        </a>
                    </td>
                    <td class="quantity">
                        <fmt:formatNumber value="${order.items.get(status.index).quantity}" var="quantity"/>
                            ${quantity}
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td class="price"> Total quantity</td>
                <td>${order.totalQuantity}</td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td class="price">Subtotal cost</td>
                <td>${order.subTotalCost}</td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td class="price">Delivery cost</td>
                <td>${order.deliveryCost}</td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td class="price">Total cost</td>
                <td>${order.totalCost}</td>
            </tr>
        </table>
        <h2>
            Your details
        </h2>
        <table>
            <tags:orderFormRow name="firstName" label="First name" order="${order}"
                               errors="${errors}" nameValues="${nameValues}"></tags:orderFormRow>
            <tags:orderFormRow name="lastName" label="Last name" order="${order}"
                               errors="${errors}" nameValues="${nameValues}"></tags:orderFormRow>
            <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}"
                               nameValues="${nameValues}"></tags:orderFormRow>
            <tags:orderFormRow name="deliveryAddress" label="Delivery address" order="${order}"
                               errors="${errors}" nameValues="${nameValues}"></tags:orderFormRow>
            <tr>
                <td>Delivery Date<span style="color:red">*</span></td>
                <td>
                    <c:set var="error" value="${errors['deliveryDate']}"/>
                    <input type="date" name="deliveryDate"
                           value="${not empty error ? param['deliveryDate'] : order.deliveryDate}">
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td>Payment method<span style="color:red">*</span></td>
                <td>
                    <c:set var="error" value="${errors['paymentMethod']}"/>
                    <select name="paymentMethod">
                        <option></option>
                        <c:forEach var="paymentMethod" items="${paymentMethods}">
                            <c:if test="${order.paymentMethod eq paymentMethod.name()}">
                                <option selected>${paymentMethod}</option>
                            </c:if>
                            <c:if test="${not (order.paymentMethod eq paymentMethod.name())}">
                                <option>${paymentMethod}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <br>
        <p>
            <button>Place Order</button>
        </p>
    </form>
</tags:master>

