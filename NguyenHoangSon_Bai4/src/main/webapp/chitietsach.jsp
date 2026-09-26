<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>IUH Bookstore - Chi tiết sách</title>
</head>
<body>
<%@ include file="common/header.jsp" %>
<%@ include file="common/sidebar.jsp" %>
    <div class="main">
        <c:if test="${not empty book}">
            <p>Product details: ${book.tittle} - Tác giả: ${book.author}</p>
            <img src="${pageContext.request.contextPath}/images/${book.imgbook}"
                 alt="${book.tittle}" width="180"/>
            <br/><br/>
            Price (VND): <fmt:formatNumber value="${book.price}" type="number" groupingUsed="true"/><br/>
            Quantity: ${book.quantity}<br/><br/>
            <form action="${pageContext.request.contextPath}/cart" method="post">
                <input type="hidden" name="id" value="${book.id}">
                <input type="hidden" name="action" value="add">
                <button type="submit">Add to cart</button>
            </form>
        </c:if>
        <c:if test="${empty book}">
            <p>Không tìm thấy sách.</p>
        </c:if>
        <br/>
        <a href="${pageContext.request.contextPath}/books">Back to Product List</a>
    </div>
<%@ include file="common/footer.jsp" %>
</body>
</html>
