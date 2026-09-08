<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Danh sách sản phẩm</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<table border="1">
    <tr>
        <th>ID</th><th>Tên</th><th>Giá</th><th>Mô tả</th><th>Thao tác</th>
    </tr>
    <c:forEach items="${products}" var="pro">
        <tr>
            <td>${pro.id}</td>
            <td>${pro.name}</td>
            <td>${pro.price}</td>
            <td>${pro.description}</td>
            <td>
                <a href="${pageContext.request.contextPath}/products?action=detail&id=${pro.id}">Xem</a>
                <a href="${pageContext.request.contextPath}/products?action=edit&id=${pro.id}">Sửa</a>
                <a href="${pageContext.request.contextPath}/products?action=delete&id=${pro.id}">Xóa</a>
            </td>
        </tr>
    </c:forEach>

</table>
<a href="${pageContext.request.contextPath}/products?action=new">+ Thêm sản phẩm</a>
</body>
</html>
