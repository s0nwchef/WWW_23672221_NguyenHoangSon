<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.sql.Connection" %>
<html>
<head>
    <title>Demo MVC Product</title>
</head>
<body>

<%
    String dbStatus;
    String dbStatusClass;
    try {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/productdb");
        try (Connection con = ds.getConnection()) {
            dbStatus = "Kết nối database thành công! (" + con.getMetaData().getURL() + ")";
            dbStatusClass = "ok";
        }
    } catch (Exception e) {
        dbStatus = "Chưa kết nối được database: " + e.getMessage();
        dbStatusClass = "error";
    }
%>

<p class="<%= dbStatusClass %>"><%= dbStatus %></p>

<hr/>
<a href="${pageContext.request.contextPath}/products">CRUD</a>
</body>
</html>
