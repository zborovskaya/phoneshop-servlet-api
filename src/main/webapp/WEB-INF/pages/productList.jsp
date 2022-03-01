<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="recentViewed" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="error">
            There was an error adding to cart
        </div>
    </c:if>
    <form>
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
    <form id="addCart" method="post">
    </form>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink sort="description" order="asc"></tags:sortLink>
                <tags:sortLink sort="description" order="desc"></tags:sortLink>
            </td>
            <td class="quantity">
                Quantity
            </td>
            <td class="price">
                Price
                <tags:sortLink sort="price" order="asc"></tags:sortLink>
                <tags:sortLink sort="price" order="desc"></tags:sortLink>
            </td>
            <td></td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}" varStatus="status">
            <tr>
                <form method="post" action="${pageContext.servletContext.contextPath}/products?productId=${product.id}">
                    <td>
                        <img class="product-tile"
                             src="${product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                                ${product.description}
                        </a>
                    </td>
                    <td>
                        <input name="quantity"
                               value="${not empty param.error and product.id eq param.productId ? param['quantity'] :1}"
                               class="quantity">
                        <c:if test="${not empty param.error and product.id eq param.productId}">
                            <div class="error">
                                    ${param.error}
                            </div>
                        </c:if>
                    </td>
                    <td class="price">
                        <a href="${pageContext.servletContext.contextPath}/products/priceHistory?productId=${product.id}">
                            <fmt:formatNumber value="${product.price}" type="currency"
                                              currencySymbol="${product.currency.symbol}"/>
                        </a>
                    </td>
                    <td>
                        <button>Add to cart</button>
                    </td>
                </form>
            </tr>
        </c:forEach>
    </table>

    <br>
    <div class="container">
        <ul class="nav nav-pills" role="tablist">
            <c:forEach var="recentProduct" items="${recentViewed}">
                <li><a href="${pageContext.servletContext.contextPath}/products/${recentProduct.id}"
                       role="tab" data-toggle="pill">
                        ${recentProduct.description}
                    <p>
                        <img class="product-tile" src=${recentProduct.imageUrl}>
                    </p>
                </a></li>
            </c:forEach>
        </ul>
    </div>
</tags:master>