<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Chi tiết sản phẩm</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h2>Chi tiết sản phẩm</h2>

<table border="1">
    <tr><th>ID</th><td>${product.id}</td></tr>
    <tr><th>Tên</th><td>${product.name}</td></tr>
    <tr><th>Giá</th><td>${product.price}</td></tr>
    <tr><th>Mô tả</th><td>${product.description}</td></tr>
</table>

<br/>
<a href="${pageContext.request.contextPath}/products">&laquo; Quay lại danh sách</a>
</body>
</html>
