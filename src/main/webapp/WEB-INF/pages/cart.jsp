<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="cart" type="com.es.phoneshop.model.product.bean.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <p>
        Cart : ${cart}, Total quantity ${cart.totalQuantity}
    </p>
    <p>
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
                               value="${not empty error ? paramValues['quantity'][status.index] :quantity}"
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
        </table>
        <br>
        <p>
            <button>Update</button>
        </p>
    </form>
    <form id="deleteCartItem" method="post">
    </form>
</tags:master>

