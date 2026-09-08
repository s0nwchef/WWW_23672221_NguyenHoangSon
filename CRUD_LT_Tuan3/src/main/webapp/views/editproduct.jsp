<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sửa sản phẩm</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h2>Sửa sản phẩm</h2>

<form action="${pageContext.request.contextPath}/products" method="post">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="id" value="${product.id}">

    <label>Tên: <input type="text" name="name" value="${product.name}" required></label><br/>
    <label>Giá: <input type="number" step="0.01" name="price" value="${product.price}" required></label><br/>
    <label>Mô tả: <textarea name="description">${product.description}</textarea></label><br/>

    <button type="submit">Lưu</button>
    <a href="${pageContext.request.contextPath}/products">Hủy</a>
</form>
</body>
</html>
