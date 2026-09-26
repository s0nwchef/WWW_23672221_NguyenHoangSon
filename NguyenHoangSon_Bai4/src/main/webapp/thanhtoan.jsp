<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>IUH Bookstore - Thanh toán</title>
</head>
<body>
<%@ include file="common/header.jsp" %>
<%@ include file="common/sidebar.jsp" %>
    <div class="main">
        <h3>Checkout - Already registered?</h3>

        <c:if test="${not empty message}">
            <div class="message-success">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message-error">${error}</div>
        </c:if>

        <c:if test="${not empty sessionScope.cart.items}">
            <form class="checkout-form" action="${pageContext.request.contextPath}/checkout" method="post">
                <label>Fullname</label>
                <input type="text" name="fullname" required>

                <label>Shipping address</label>
                <input type="text" name="address" required>

                <label>Total price</label>
                <input type="text" value="<fmt:formatNumber value='${sessionScope.cart.total}' type='number' groupingUsed='true'/>" readonly>

                <label>Payment method</label>
                <div class="payment-options">
                    <label><input type="radio" name="paymentMethod" value="Paypal" checked> Paypal</label>
                    <label><input type="radio" name="paymentMethod" value="ATM Debit"> ATM Debit</label>
                    <label><input type="radio" name="paymentMethod" value="Visa/Master card"> Visa/Master card</label>
                </div>

                <br/>
                <button class="btn" type="submit" name="action" value="save">Save</button>
                <button class="btn" type="submit" name="action" value="cancel">Cancel</button>
            </form>
        </c:if>

        <c:if test="${empty sessionScope.cart.items and empty message}">
            <p>Giỏ hàng đang trống, không có gì để thanh toán.</p>
            <a href="${pageContext.request.contextPath}/books">Tiếp tục mua</a>
        </c:if>
    </div>
<%@ include file="common/footer.jsp" %>
</body>
</html>
