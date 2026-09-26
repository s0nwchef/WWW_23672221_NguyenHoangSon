<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Product List</title>
    <style>
        .product-class { display: inline-block; width: 200px; margin: 10px; padding: 10px; border: 1px solid #ccc; vertical-align: top; }
        .hinh { width: 150px; height: 150px; object-fit: cover; }
    </style>
</head>
<body>
    <p>
        <a href="cart">View Cart</a>
    </p>
    <c:forEach items="${products}" var="p">
        <div class="product-class">
            <b>${p.model}</b>
            <br/>
            <img src="images/${p.imgURL}" class="hinh"> <br/>
            Price: ${p.price}<br/>
            <form action="${pageContext.request.contextPath}/cart" method="post">
                <input type="text" size="2" value="1" name="quantity"> <br/>
                <input type="hidden" name="id" value="${p.id}">
                <input type="hidden" name="price" value="${p.price}">
                <input type="hidden" name="model" value="${p.model}">
                <input type="hidden" name="action" value="add"><br/>
                <input type="submit" name="addToCart" value="Add To Cart"><br/>
            </form>
            <a href="${pageContext.request.contextPath}/product?id=${p.id}">Product Detail</a><br/>
        </div>
    </c:forEach>
</body>
</html>
