<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>User Registration Form</title>
</head>
<body>
<h2>User Registration Form</h2>

<form action="${pageContext.request.contextPath}/registerform" method="post">
    <label>First Name: <input type="text" name="firstname" required></label>
    <label>Last Name: <input type="text" name="lastname" required></label>
    <br/>
    <label>Your Email: <input type="email" name="email" required></label>
    <br/>
    <label>Password: <input type="password" name="password" required></label>
    <br/>
    <label>Birthday:
        <select name="month">
            <option value="1">01</option>
            <option value="2">02</option>
            <option value="3">03</option>
            <option value="4">04</option>
            <option value="5">05</option>
            <option value="6">06</option>
            <option value="7">07</option>
            <option value="8">08</option>
            <option value="9">09</option>
            <option value="10">10</option>
            <option value="11">11</option>
            <option value="12">12</option>
        </select>
        <select name="day">
            <c:forEach var="d" begin="1" end="31">
                <option value="${d}">${d}</option>
            </c:forEach>
        </select>
        <select name="year">
            <c:forEach var="y" begin="1950" end="2025">
                <option value="${y}">${y}</option>
            </c:forEach>
        </select>
    </label>
    <br/>
    <label>Gender:
        <input type="radio" name="gender" value="Female"> Female
        <input type="radio" name="gender" value="Male"> Male
    </label>
    <br/>
    <button type="submit">Sign Up</button>
</form>
</body>
</html>
