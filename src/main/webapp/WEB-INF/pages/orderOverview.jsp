<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="order" type="com.es.phoneshop.model.product.bean.Order" scope="request"/>
<tags:master pageTitle="Order overview">
    <h1>Order overview</h1>
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
        <tags:orderOverviewRow name="firstName" label="First name" order="${order}"
        ></tags:orderOverviewRow>
        <tags:orderOverviewRow name="lastName" label="Last name" order="${order}"
        ></tags:orderOverviewRow>
        <tags:orderOverviewRow name="phone" label="Phone" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="deliveryAddress" label="Delivery address" order="${order}"
        ></tags:orderOverviewRow>
        <tags:orderOverviewRow name="deliveryDate" label="Delivery Date" order="${order}"
        ></tags:orderOverviewRow>
        <tags:orderOverviewRow name="paymentMethod" label="Payment Method" order="${order}"
        ></tags:orderOverviewRow>
    </table>
</tags:master>
