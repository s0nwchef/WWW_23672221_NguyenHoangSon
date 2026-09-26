<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>IUH Bookstore - Danh sách sách</title>
</head>
<body>
<%@ include file="common/header.jsp" %>
<%@ include file="common/sidebar.jsp" %>
    <div class="main">
        <c:if test="${not empty keyword}">
            <p>Kết quả tìm kiếm cho: "<strong>${keyword}</strong>"</p>
        </c:if>
        <c:if test="${empty books}">
            <p>Không có sách nào.</p>
        </c:if>
        <div class="book-grid">
            <c:forEach items="${books}" var="b">
                <div class="book-card">
                    <b>${b.tittle}</b><br/>
                    <span class="author">Tác giả: ${b.author}</span><br/>
                    <img src="${pageContext.request.contextPath}/images/${b.imgbook}" alt="${b.tittle}"><br/>
                    <fmt:formatNumber value="${b.price}" type="number" groupingUsed="true"/> VND<br/>
                    Quantity: ${b.quantity}<br/>
                    <form action="${pageContext.request.contextPath}/cart" method="post">
                        <input type="hidden" name="id" value="${b.id}">
                        <input type="hidden" name="action" value="add">
                        <button type="submit">Add to cart</button>
                    </form>
                    <a href="${pageContext.request.contextPath}/book?id=${b.id}">Product details</a>
                </div>
            </c:forEach>
        </div>
    </div>
<%@ include file="common/footer.jsp" %>
</body>
</html>
