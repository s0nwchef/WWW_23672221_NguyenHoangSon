<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Danh sách tài khoản</title>
</head>
<body>
<h2>Đăng ký thành công!</h2>

<table border="1">
    <tr>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Email</th>
        <th>Date of Birth</th>
    </tr>
    <c:forEach items="${accounts}" var="acc">
        <tr>
            <td>${acc.firstname}</td>
            <td>${acc.lastname}</td>
            <td>${acc.email}</td>
            <td>${acc.dateOfBirth}</td>
        </tr>
    </c:forEach>
</table>

<br/>
<a href="${pageContext.request.contextPath}/RegisterForm.jsp">Đăng ký tài khoản khác</a>
</body>
</html>
