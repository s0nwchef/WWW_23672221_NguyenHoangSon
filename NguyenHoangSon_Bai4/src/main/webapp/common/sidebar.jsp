<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<aside class="sidebar">
    <div class="box">
        <h4>ABOUT US</h4>
        <p>About us information will be here.... <a href="#">Read More »</a></p>
    </div>
    <div class="box">
        <h4>SEARCH SITE</h4>
        <form action="${pageContext.request.contextPath}/books" method="get">
            <input type="text" name="keyword" placeholder="Tìm tên sách..." value="${keyword}">
        </form>
        <a class="cart-link" href="${pageContext.request.contextPath}/cart">
            Shopping cart (${empty sessionScope.cart ? 0 : sessionScope.cart.itemCount})
        </a>
    </div>
</aside>
