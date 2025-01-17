<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="cart" type="com.es.phoneshop.model.product.bean.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <p id="product-description-text">
        Your Cart
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            There was an error
        </div>
    </c:if>
    <c:if test="${empty cart.items}">
        <p id="product-description-text">
            Your cart empty!<a href="${pageContext.servletContext.contextPath}/products">Add some products?</a>
        </p>
    </c:if>
    <c:if test="${not empty cart.items}">
        </p>
        <form method="post" action="${pageContext.servletContext.contextPath}/cart">
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
                    <td></td>
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
                            <fmt:formatNumber value="${cart.items.get(status.index).quantity}" var="quantity"/>
                            <c:set var="error" value="${errors[product.id]}"/>
                            <input name="quantity"
                                   value="${not empty error ? quantityError[product.id] :quantity}"
                                   class="quantity">
                            <input name="productId" value="${product.id}" type="hidden">
                            <c:if test="${not empty error}">
                                <div class="error">
                                        ${error}
                                </div>
                            </c:if>
                        </td>
                        <td>
                            <button form="deleteCartItem"
                                    formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${product.id}">
                                Delete
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td></td>
                    <td></td>
                    <td class="price">Total cost: ${cart.totalCost}</td>
                    <td class="quantity">Total quantity: ${cart.totalQuantity}</td>
                </tr>
            </table>
            <br>
            <p>
                <button>Update</button>
            </p>
        </form>
        <form action="${pageContext.servletContext.contextPath}/checkout">
            <button>Checkout</button>
        </form>
        <form id="deleteCartItem" method="post">
        </form>
    </c:if>
</tags:master>

