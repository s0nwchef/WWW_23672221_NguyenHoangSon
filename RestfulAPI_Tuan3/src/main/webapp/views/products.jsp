<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Danh sách sản phẩm</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>
<main class="page">
    <header>
        <div>
            <h1>Danh sách sản phẩm</h1>
            <p>Xin chào,
                <strong>${userSession.username}</strong></p>
        </div>
        <form method="post"
              action="${pageContext.request.contextPath}/logout">
            <button class="secondary" type="submit">Đăng
                xuất</button>
        </form>
    </header>
    <section class="card">
        <table>
            <thead><tr><th>Sản
                phẩm</th><th>Giá</th><th></th></tr></thead>
            <tbody>
            <c:forEach items="${products}" var="product">
                <tr>
                    <td>${product.name}</td>
                    <td><fmt:formatNumber value="${product.price}"
                                          type="currency" currencyCode="USD"/></td>
                    <td>
                        <form method="post"
                              action="${pageContext.request.contextPath}/cart/add">
                            <input type="hidden" name="productId"
                                   value="${product.id}">
                            24
                            <button type="submit">Thêm vào
                                giỏ</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>
    <section class="card">
        <h2>Giỏ hàng trong session</h2>
        <c:choose>
            <c:when test="${empty userSession.shoppingCart.items}">
                <p>Giỏ hàng đang trống.</p>
            </c:when>
            <c:otherwise>
                <ul>
                    <c:forEach
                            items="${userSession.shoppingCart.items}" var="item">
                        <li>${item.product.name} × ${item.quantity}
                            —
                            <fmt:formatNumber
                                    value="${item.subtotal}" type="currency" currencyCode="USD"/>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
        <p><strong>Số lượng:</strong>
            ${userSession.shoppingCart.itemCount}</p>
        <p><strong>Tổng tiền:</strong>
            <fmt:formatNumber
                    value="${userSession.shoppingCart.total}" type="currency"
                    currencyCode="USD"/>
        </p>
        <p class="technical">CDI session instance:
            ${userSession.instanceId}</p>
    </section>
</main>
</body>
</html>