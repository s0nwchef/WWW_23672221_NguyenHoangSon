<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Thêm sản phẩm</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h2>Thêm sản phẩm mới</h2>

<form action="${pageContext.request.contextPath}/products" method="post">
    <label>Tên: <input type="text" name="name" required></label><br/>
    <label>Giá: <input type="number" step="0.01" name="price" required></label><br/>
    <label>Mô tả: <textarea name="description"></textarea></label><br/>

    <button type="submit">Thêm</button>
    <a href="${pageContext.request.contextPath}/products">Hủy</a>
</form>
</body>
</html>
