<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>IUH Bookstore - Giỏ hàng</title>
</head>
<body>
<%@ include file="common/header.jsp" %>
<%@ include file="common/sidebar.jsp" %>
    <div class="main">
        <h3>YOUR SHOPPING CART</h3>
        <c:if test="${empty sessionScope.cart.items}">
            <p>Cart is empty!</p>
            <a href="${pageContext.request.contextPath}/books">Tiếp tục mua</a>
        </c:if>

        <c:if test="${not empty sessionScope.cart.items}">
            <table class="cart-table">
                <tr>
                    <th>Book ID</th>
                    <th>Book name</th>
                    <th>Price</th>
                    <th>Qty</th>
                    <th>Total</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="item" items="${sessionScope.cart.items}">
                    <tr>
                        <td>pro${item.book.id}</td>
                        <td>${item.book.tittle} - Tác giả: ${item.book.author}</td>
                        <td><fmt:formatNumber value="${item.book.price}" type="number" groupingUsed="true"/></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/cart" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="update"/>
                                <input type="hidden" name="bookId" value="${item.book.id}"/>
                                <input type="number" name="quantity" value="${item.quantity}" min="1"/>
                                <button type="submit">Cập nhật</button>
                            </form>
                        </td>
                        <td><fmt:formatNumber value="${item.subtotal}" type="number" groupingUsed="true"/></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/cart" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="remove"/>
                                <input type="hidden" name="bookId" value="${item.book.id}"/>
                                <button type="submit">Remove</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="4" style="text-align:right;"><strong>Total price (VND):</strong></td>
                    <td colspan="2"><fmt:formatNumber value="${sessionScope.cart.total}" type="number" groupingUsed="true"/></td>
                </tr>
            </table>
            <br/>
            <a class="btn" href="${pageContext.request.contextPath}/checkout">Checkout</a>
            <a class="btn" href="${pageContext.request.contextPath}/books">Continue shopping</a>
        </c:if>
    </div>
<%@ include file="common/footer.jsp" %>
</body>
</html>
